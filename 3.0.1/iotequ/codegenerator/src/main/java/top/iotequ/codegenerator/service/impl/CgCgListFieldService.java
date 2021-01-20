package top.iotequ.codegenerator.service.impl;
import top.iotequ.codegenerator.pojo.CgListField;
import top.iotequ.codegenerator.dao.CgListFieldDao;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingClass;
import top.iotequ.framework.exception.IotequException;
import top.iotequ.framework.flow.IFlowService;
import top.iotequ.framework.service.impl.CgService;
import top.iotequ.framework.service.IDaoService;
import org.springframework.stereotype.Service;
import top.iotequ.util.*;
import top.iotequ.util.Util;

import java.util.*;

/**************************************************
Create by iotequ codegenerator 3.0.0
Author : Qinyoyo
"**************************************************/
@ConditionalOnMissingClass({"top.iotequ.codegenerator.service.impl.CgListFieldService"})
@Service(value="cgListFieldService")
public class CgCgListFieldService extends CgService<CgListField>  {
private static final Logger log = LoggerFactory.getLogger(CgCgListFieldService.class);
    @Autowired
    private CgListFieldDao cgListFieldDao;
    @Override
    public Class<CgListField> getEntityClass() {
        return CgListField.class;
    }
    @Override
    public IDaoService<CgListField> getDaoService() {
        return cgListFieldDao ;
    }
    @Override
    public IFlowService<CgListField> getFlowService() {
        return null;
    }
    @Override
    public Map<String,Object> getDictionary(CgListField obj, Boolean useTree, String dynaFields) {
        Map<String,Object> map=new HashMap<>();
        return map;
    }
    @Override
    public CgListField getDefaultObject(CgListField cgListField) throws IotequException {
        checkAvailable();
        if (cgListField==null)  cgListField=new CgListField();
        else cgListField.setId(null);
        return cgListField;
    }
    @Override
    public void changeEmpty2Null(CgListField cgListField) {
        if (Objects.nonNull(cgListField)) {
        }
    }
    @Override
    public void changeNull2Default(CgListField cgListField) {
        if (cgListField.getId()==null) {
            cgListField.setId(0);
        }
        if (cgListField.getListId()==null) {
            cgListField.setListId("");
        }
        if (cgListField.getOrderNum()==null) {
            cgListField.setOrderNum(0);
        }
        if (cgListField.getEntityField()==null) {
            cgListField.setEntityField("");
        }
        if (cgListField.getQueryMode()==null) {
            cgListField.setQueryMode(0);
        }
        if (cgListField.getFix()==null) {
            cgListField.setFix(Util.boolValue("0"));
        }
        if (cgListField.getExpand()==null) {
            cgListField.setExpand(Util.boolValue("0"));
        }
        if (cgListField.getOverflowTooltip()==null) {
            cgListField.setOverflowTooltip(Util.boolValue("1"));
        }
        if (cgListField.getAlign()==null) {
            cgListField.setAlign("left");
        }
        if (cgListField.getWidth()==null) {
            cgListField.setWidth(128);
        }
        if (cgListField.getHidden()==null) {
            cgListField.setHidden(Util.boolValue("0"));
        }
        if (cgListField.getEditInline()==null) {
            cgListField.setEditInline(Util.boolValue("0"));
        }
    }
}
