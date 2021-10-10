package top.iotequ.attendance.employee.service.impl;

import org.springframework.stereotype.Service;
import top.iotequ.attendance.employee.pojo.AdEmployee;
import top.iotequ.framework.exception.IotequException;

import javax.servlet.http.HttpServletRequest;

@Service
public class AdEmployeeService extends CgAdEmployeeService {
    @Override
    public void beforeSave(AdEmployee obj0, AdEmployee obj, boolean updateSelective, HttpServletRequest request) throws IotequException {
        if (obj0==null) {
            if (obj.getOvertimeMinutes()==null) obj.setOvertimeMinutes(0);
        }
    }

    @Override
    public String listFilter(String path) {
        if ("join".equals(path))  return "is_attendance=1 and (enter_date is null or enter_date < now()) && (leave_date is null or leave_date > now())";
        else return null;
    }
}
