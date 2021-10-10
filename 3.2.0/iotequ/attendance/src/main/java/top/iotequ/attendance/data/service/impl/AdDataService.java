package top.iotequ.attendance.data.service.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Service;
import top.iotequ.attendance.data.dao.AdDataDao;
import top.iotequ.attendance.data.pojo.AdData;
import top.iotequ.attendance.util.AdUtil;

import top.iotequ.framework.event.DeviceEvent;
import top.iotequ.util.SqlUtil;
import top.iotequ.util.Util;

import java.util.Date;

@Service
public class AdDataService extends CgAdDataService implements ApplicationListener<DeviceEvent> {
    private static final Logger log = LoggerFactory.getLogger(AdDataService.class);
    @Autowired
    AdDataDao adDataDao;
    @Override
    public String listFilter(String path) {
        String c0 = "is_attendance = 1";
        String c1 = AdUtil.dataFilter(true, AdData.class);
        if (Util.isEmpty(c1)) return c0;
        else return "(" + c0 + ") AND (" + c1 + ")";
    }
    @Override
    public void onApplicationEvent(DeviceEvent event) {
        try {
            int adMode = -1;
            String mode=event.getDeviceMode().toLowerCase();
            if (Util.isEmpty(mode))	{
                log.error("device properties is null");
                return;
            }
            mode=","+mode+",";
            if (mode.contains(",ad,") || (mode.contains(",on,") && mode.contains(",off,"))) adMode=0;
            else if (mode.contains(",on,")) adMode=1;
            else if (mode.contains(",off,")) adMode=2;
            if (adMode >= 0) {
                String uno=event.getUserNo();
                if (Util.isEmpty(uno)) {
                    log.error("user no is null");
                    return;
                }
                Date time = event.getTime();
                if (time==null) {
                    log.error("time is null");
                    return;
                }
                AdData dat = new AdData();
                String eno= SqlUtil.sqlQueryString("select employee_no from ad_employee where id=?", uno);
                if (Util.isEmpty(eno)) {
                    log.error("employee no not found");
                    return;
                }
                dat.setEmployeeNo(eno);
                dat.setRecTime(time);
                dat.setRecType(adMode);
                dat.setRecSourceType(event.getDeviceType());
                dat.setRecSource(event.getDeviceNo());
                dat.setIsUsed(false);
                adDataDao.insert(dat);
            } else log.debug("event has not properties for attendance");
        } catch (Exception e) {}
    }

}
