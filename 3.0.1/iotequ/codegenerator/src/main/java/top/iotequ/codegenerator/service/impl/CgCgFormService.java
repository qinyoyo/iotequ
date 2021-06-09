package top.iotequ.codegenerator.service.impl;
import top.iotequ.codegenerator.pojo.CgForm;
import top.iotequ.codegenerator.dao.CgFormDao;
import javax.servlet.http.HttpServletRequest;
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
import top.iotequ.framework.service.utils.UploadDownUtil;
import top.iotequ.framework.service.utils.QueryUtil;
import top.iotequ.util.*;
import java.util.*;

/**************************************************
Create by iotequ codegenerator 3.0.0
Author : Qinyoyo
"**************************************************/
@ConditionalOnMissingClass({"top.iotequ.codegenerator.service.impl.CgFormService"})
@Service(value="cgFormService")
public class CgCgFormService extends CgService<CgForm>  {
    private static final Logger log = LoggerFactory.getLogger(CgCgFormService.class);
    @Autowired
    private CgFormDao cgFormDao;
    @Override
    public Class<CgForm> getEntityClass() {
        return CgForm.class;
    }
    @Override
    public IDaoService<CgForm> getDaoService() {
        return cgFormDao ;
    }
    @Override
    public IFlowService<CgForm> getFlowService() {
        return null;
    }
    @Override
    public Map<String,Object> getDictionary(CgForm obj, Boolean useTree, String dynaFields) {
        Map<String,Object> map=new HashMap<>();
        if (Objects.isNull(dynaFields) || StringUtil.containsItem(dynaFields,"tableId")) map.put("dictTableId", DictionaryUtil.getDictListFromDatabase(obj,"select * from cg_table where creator=${system.user.name}","id","code",null,false,null));
        if (Objects.isNull(dynaFields) || StringUtil.containsItem(dynaFields,"actionList")) map.put("dictActionList", DictionaryUtil.getDictListFromDatabase(obj,"select * from (select 'flow' as action,id as table_id from cg_table where FIND_IN_SET('flow',action_list)>0 union select action,table_id from cg_button where FIND_IN_SET('ed',display_properties)>0) t","action","action",null,false,"table_id=${tableId}"));
        return map;
    }
    @Override
    public CgForm getDefaultObject(CgForm cgForm) throws IotequException {
        checkAvailable();
        if (cgForm==null)  cgForm=new CgForm();
        else cgForm.setId(null);
        return cgForm;
    }
    @Override
    public void changeEmpty2Null(CgForm cgForm) {
        if (Objects.nonNull(cgForm)) {
        }
    }
    @Override
    public void changeNull2Default(CgForm cgForm) {
        if (cgForm.getId()==null) {
            cgForm.setId("");
        }
        if (cgForm.getName()==null) {
            cgForm.setName("");
        }
        if (cgForm.getPath()==null) {
            cgForm.setPath("record");
        }
        if (cgForm.getTableId()==null) {
            cgForm.setTableId("");
        }
        if (cgForm.getHeadTitle()==null) {
            cgForm.setHeadTitle("");
        }
        if (cgForm.getIsFlow()==null) {
            cgForm.setIsFlow(Util.boolValue("0"));
        }
        if (cgForm.getTagTitle()==null) {
            cgForm.setTagTitle("");
        }
        if (cgForm.getLabelPosition()==null) {
            cgForm.setLabelPosition("top");
        }
        if (cgForm.getIsDialog()==null) {
            cgForm.setIsDialog(Util.boolValue("0"));
        }
        if (cgForm.getContinueAdd()==null) {
            cgForm.setContinueAdd(Util.boolValue("1"));
        }
    }
}
