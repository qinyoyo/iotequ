package top.iotequ.codegenerator.service.impl;
import top.iotequ.codegenerator.pojo.CgField;
import top.iotequ.codegenerator.dao.CgFieldDao;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingClass;
import top.iotequ.framework.exception.IotequException;
import top.iotequ.framework.flow.IFlowService;
import top.iotequ.framework.service.impl.CgService;
import top.iotequ.framework.service.IDaoService;
import org.springframework.stereotype.Service;
import top.iotequ.framework.service.utils.DictionaryUtil;
import top.iotequ.util.*;
import top.iotequ.util.StringUtil;
import top.iotequ.util.Util;

import java.util.*;

/**************************************************
Create by iotequ codegenerator 3.0.0
Author : Qinyoyo
"**************************************************/
@ConditionalOnMissingClass({"top.iotequ.codegenerator.service.impl.CgFieldService"})
@Service(value="cgFieldService")
public class CgCgFieldService extends CgService<CgField>  {
private static final Logger log = LoggerFactory.getLogger(CgCgFieldService.class);
    @Autowired
    private CgFieldDao cgFieldDao;
    @Override
    public Class<CgField> getEntityClass() {
        return CgField.class;
    }
    @Override
    public IDaoService<CgField> getDaoService() {
        return cgFieldDao ;
    }
    @Override
    public IFlowService<CgField> getFlowService() {
        return null;
    }
    @Override
    public Map<String,Object> getDictionary(CgField obj, Boolean useTree, String dynaFields) {
        Map<String,Object> map=new HashMap<>();
        if (Objects.isNull(dynaFields) || StringUtil.containsItem(dynaFields,"fkTable")) map.put("dictFkTable", DictionaryUtil.getDictListFromDatabase(obj,"SELECT * from cg_table where name is not null and name!='' order by name;","name","name",null,false,"project_id in (select 'qinyoyo.sys' union select project_id from cg_table where id=${tableId})"));
        if (Objects.isNull(dynaFields) || StringUtil.containsItem(dynaFields,"fkColumn")) map.put("dictFkColumn", DictionaryUtil.getDictListFromDatabase(obj,"select * from cg_field, cg_table","cg_field.name","cg_field.name",null,false," cg_field.key_type in ('1','2','3','4') and cg_field.table_id=cg_table.id and cg_table.name=${fkTable}"));
        return map;
    }
    @Override
    public CgField getDefaultObject(CgField cgField) throws IotequException {
        checkAvailable();
        if (cgField==null)  cgField=new CgField();
        else cgField.setId(null);
        return cgField;
    }
    @Override
    public void changeEmpty2Null(CgField cgField) {
        if (Objects.nonNull(cgField)) {
        }
    }
    @Override
    public void changeNull2Default(CgField cgField) {
        if (cgField.getId()==null) {
            cgField.setId("");
        }
        if (cgField.getTableId()==null) {
            cgField.setTableId("");
        }
        if (cgField.getOrderNum()==null) {
            cgField.setOrderNum(0);
        }
        if (cgField.getEntityName()==null) {
            cgField.setEntityName("");
        }
        if (cgField.getTitle()==null) {
            cgField.setTitle("");
        }
        if (cgField.getKeyType()==null) {
            cgField.setKeyType("0");
        }
        if (cgField.getShowType()==null) {
            cgField.setShowType("text");
        }
        if (cgField.getIsNull()==null) {
            cgField.setIsNull(Util.boolValue("0"));
        }
        if (cgField.getDictMultiple()==null) {
            cgField.setDictMultiple(Util.boolValue("0"));
        }
        if (cgField.getDictFullName()==null) {
            cgField.setDictFullName(Util.boolValue("0"));
        }
        if (cgField.getType()==null) {
            cgField.setType("varchar");
        }
    }
}
