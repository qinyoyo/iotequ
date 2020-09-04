package top.iotequ.attendance.data.service.impl;

import org.springframework.stereotype.Service;
import top.iotequ.attendance.data.pojo.AdData;
import top.iotequ.attendance.util.AdUtil;

import top.iotequ.framework.util.Util;

@Service
public class AdDataService extends CgAdDataService {
    @Override
    public String listFilter(String path) {
        String c0 = "is_attendance = 1";
        String c1 = AdUtil.dataFilter(true, AdData.class);
        if (Util.isEmpty(c1)) return c0;
        else return "(" + c0 + ") AND (" + c1 + ")";
    }
}
