package top.iotequ.codegenerator.service.impl;

import org.springframework.stereotype.Service;
import top.iotequ.codegenerator.pojo.CgForm;
import top.iotequ.framework.exception.IotequException;


import java.util.Objects;

@Service
public class CgFormService extends CgCgFormService {
    @Override
    public void setPrimaryKey(CgForm obj) throws IotequException {
        if (Objects.nonNull(obj)) {
            obj.setId(obj.getTableId() + "_" + obj.getName());
        }
    }
}
