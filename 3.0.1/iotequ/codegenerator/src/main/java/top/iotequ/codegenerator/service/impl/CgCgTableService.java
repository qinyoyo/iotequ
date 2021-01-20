package top.iotequ.codegenerator.service.impl;
import top.iotequ.codegenerator.pojo.CgTable;
import top.iotequ.codegenerator.dao.CgTableDao;
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

import java.util.*;

/**************************************************
Create by iotequ codegenerator 3.0.0
Author : Qinyoyo
"**************************************************/
@ConditionalOnMissingClass({"top.iotequ.codegenerator.service.impl.CgTableService"})
@Service(value="cgTableService")
public class CgCgTableService extends CgService<CgTable>  {
private static final Logger log = LoggerFactory.getLogger(CgCgTableService.class);
    @Autowired
    private CgTableDao cgTableDao;
    @Override
    public Class<CgTable> getEntityClass() {
        return CgTable.class;
    }
    @Override
    public IDaoService<CgTable> getDaoService() {
        return cgTableDao ;
    }
    @Override
    public IFlowService<CgTable> getFlowService() {
        return null;
    }
    @Override
    public Map<String,Object> getDictionary(CgTable obj, Boolean useTree, String dynaFields) {
        Map<String,Object> map=new HashMap<>();
        if (Objects.isNull(dynaFields) || StringUtil.containsItem(dynaFields,"flowDynaFieldsOp")) map.put("dictFlowDynaFieldsOp", DictionaryUtil.getDictListFromDatabase(obj,"select 'flowSelection' as value, 'flowSelection' as text union all SELECT entity_name as value, entity_name as text FROM cg_field",null,null,null,false,"table_id=${id}"));
        return map;
    }
    @Override
    public CgTable getDefaultObject(CgTable cgTable) throws IotequException {
        checkAvailable();
        if (cgTable==null)  cgTable=new CgTable();
        else cgTable.setId(null);
        return cgTable;
    }
    @Override
    public void changeEmpty2Null(CgTable cgTable) {
        if (Objects.nonNull(cgTable)) {
        }
    }
    @Override
    public void changeNull2Default(CgTable cgTable) {
        if (cgTable.getId()==null) {
            cgTable.setId("");
        }
        if (cgTable.getProjectId()==null) {
            cgTable.setProjectId("");
        }
        if (cgTable.getProject()==null) {
            cgTable.setProject("");
        }
        if (cgTable.getCode()==null) {
            cgTable.setCode("");
        }
        if (cgTable.getTitle()==null) {
            cgTable.setTitle("");
        }
        if (cgTable.getTemplate()==null) {
            cgTable.setTemplate("");
        }
        if (cgTable.getCreator()==null) {
            cgTable.setCreator("");
        }
    }
}
