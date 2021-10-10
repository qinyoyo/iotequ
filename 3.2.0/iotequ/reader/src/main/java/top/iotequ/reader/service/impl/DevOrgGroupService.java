package top.iotequ.reader.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import top.iotequ.framework.exception.IotequException;
import top.iotequ.framework.exception.IotequThrowable;
import top.iotequ.util.RestJson;
import top.iotequ.util.Util;
import top.iotequ.reader.dao.DevOrgGroupDao;
import top.iotequ.reader.pojo.DevOrgGroup;

import javax.servlet.http.HttpServletRequest;

@Service
public class DevOrgGroupService extends CgDevOrgGroupService {
    @Autowired
    private DevOrgGroupDao devOrgGroupDao;
    @Override
    public RestJson doSave(boolean isNew, String flowCode, Integer totalFilePart, DevOrgGroup obj, String idSaved, HttpServletRequest request) throws Exception {
        if (isNew && Util.isEmpty(obj.getOrgId()))  {
            String orgList = obj.getOrgName();
            if (Util.isEmpty(orgList)) throw new IotequException(IotequThrowable.NULL_OBJECT,"org_code");
            String [] oo = orgList.split(",");
            int count = 0;
            for (String o : oo) {
                obj.setId(null);
                try {
                    obj.setOrgId(Integer.parseInt(o));
                    devOrgGroupDao.insert(obj);
                    count ++;
                }catch (Exception e) {}
            }
            return new RestJson().parameter("rows",count).parameter("refresh",true);
        }
        else return super.doSave(isNew, flowCode, totalFilePart, obj, idSaved, request);
    }
}
