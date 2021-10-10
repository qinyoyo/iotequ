package top.iotequ.codegenerator.service.impl;

import org.springframework.stereotype.Service;
import top.iotequ.codegenerator.pojo.CgList;
import top.iotequ.framework.exception.IotequException;
import top.iotequ.util.Util;


import javax.servlet.http.HttpServletRequest;
import java.util.Objects;

@Service
public class CgListService extends CgCgListService {
    @Override
    public void setPrimaryKey(CgList obj) throws IotequException {
        if (Objects.nonNull(obj)) {
            obj.setId(obj.getTableId() + "_" + obj.getName());
        }
    }

    @Override
    public void beforeSave(CgList obj0, CgList obj, boolean updateSelective, HttpServletRequest request) throws IotequException {
        if (obj!=null && !Util.isEmpty(obj.getSons()) && (obj.getGeneratorType()==null || obj.getGeneratorType()==0)) {
            obj.setGeneratorType(50);
        }
    }
}
