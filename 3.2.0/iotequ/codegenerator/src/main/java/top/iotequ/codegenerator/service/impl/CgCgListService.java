package top.iotequ.codegenerator.service.impl;
import top.iotequ.codegenerator.pojo.CgList;
import top.iotequ.codegenerator.dao.CgListDao;
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
@ConditionalOnMissingClass({"top.iotequ.codegenerator.service.impl.CgListService"})
@Service(value="cgListService")
public class CgCgListService extends CgService<CgList>  {
    private static final Logger log = LoggerFactory.getLogger(CgCgListService.class);
    @Autowired
    private CgListDao cgListDao;
    @Override
    public Class<CgList> getEntityClass() {
        return CgList.class;
    }
    @Override
    public IDaoService<CgList> getDaoService() {
        return cgListDao ;
    }
    @Override
    public IFlowService<CgList> getFlowService() {
        return null;
    }
    @Override
    public Map<String,Object> getDictionary(CgList obj, Boolean useTree, String dynaFields) {
        Map<String,Object> map=new HashMap<>();
        if (Objects.isNull(dynaFields) || StringUtil.containsItem(dynaFields,"tableId")) map.put("dictTableId", DictionaryUtil.getDictListFromDatabase(obj,"select * from cg_table where creator=${system.user.name}","id","code",null,false,null));
        if (Objects.isNull(dynaFields) || StringUtil.containsItem(dynaFields,"sons")) map.put("dictSons", DictionaryUtil.getDictListFromDatabase(obj,"select * from (select table_id,name, head_title from cg_list union all select table_id,concat(name,'|u') ,head_title from cg_form) a order by name","name","concat(name,':',head_title)",null,false,"table_id in (select id from cg_table where project_id in (select project_id from cg_table where id=${tableId}))"));
        if (Objects.isNull(dynaFields) || StringUtil.containsItem(dynaFields,"titleField")) map.put("dictTitleField", DictionaryUtil.getDictListFromDatabase(obj,"select * from cg_list_field","entity_field","entity_field",null,false,"list_id=${id}"));
        if (Objects.isNull(dynaFields) || StringUtil.containsItem(dynaFields,"actionList")) map.put("dictActionList", DictionaryUtil.getDictListFromDatabase(obj,"select * from (select action_list as action,id as table_id from cg_table union select action,table_id from cg_button where FIND_IN_SET('tb',display_properties)>0) t","action","action",null,false,"table_id=${tableId}"));
        return map;
    }
    @Override
    public CgList getDefaultObject(CgList cgList) throws IotequException {
        checkAvailable();
        if (cgList==null)  cgList=new CgList();
        else cgList.setId(null);
        return cgList;
    }
    @Override
    public void changeEmpty2Null(CgList cgList) {
        if (Objects.nonNull(cgList)) {
        }
    }
    @Override
    public void changeNull2Default(CgList cgList) {
        if (cgList.getId()==null) {
            cgList.setId("");
        }
        if (cgList.getName()==null) {
            cgList.setName("");
        }
        if (cgList.getPath()==null) {
            cgList.setPath("list");
        }
        if (cgList.getTableId()==null) {
            cgList.setTableId("");
        }
        if (cgList.getEditInline()==null) {
            cgList.setEditInline(false);
        }
        if (cgList.getDetailInline()==null) {
            cgList.setDetailInline(false);
        }
        if (cgList.getSonAlign()==null) {
            cgList.setSonAlign("v");
        }
        if (cgList.getGeneratorType()==null) {
            cgList.setGeneratorType(0);
        }
        if (cgList.getShowSearch()==null) {
            cgList.setShowSearch(false);
        }
        if (cgList.getToolbarMode()==null) {
            cgList.setToolbarMode(2);
        }
        if (cgList.getTableHeight()==null) {
            cgList.setTableHeight(0);
        }
        if (cgList.getPagination()==null) {
            cgList.setPagination(false);
        }
        if (cgList.getStripe()==null) {
            cgList.setStripe(Util.boolValue("1"));
        }
        if (cgList.getBorder()==null) {
            cgList.setBorder(Util.boolValue("0"));
        }
        if (cgList.getHighlightCurrentRow()==null) {
            cgList.setHighlightCurrentRow(Util.boolValue("1"));
        }
        if (cgList.getMultiple()==null) {
            cgList.setMultiple(Util.boolValue("0"));
        }
        if (cgList.getShowHeader()==null) {
            cgList.setShowHeader(Util.boolValue("1"));
        }
        if (cgList.getLocalExport()==null) {
            cgList.setLocalExport(Util.boolValue("0"));
        }
    }
}
