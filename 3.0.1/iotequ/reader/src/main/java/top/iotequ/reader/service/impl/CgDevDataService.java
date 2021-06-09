package top.iotequ.reader.service.impl;
import top.iotequ.reader.pojo.DevData;
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
@ConditionalOnMissingClass({"top.iotequ.reader.service.impl.DevDataService"})
@Service(value="devDataService")
public class CgDevDataService extends CgService<DevData>  {
    private static final Logger log = LoggerFactory.getLogger(CgDevDataService.class);
    @Override
    public Class<DevData> getEntityClass() {
        return DevData.class;
    }
    @Override
    public IDaoService<DevData> getDaoService() {
        return null ;
    }
    @Override
    public IFlowService<DevData> getFlowService() {
        return null;
    }
    @Override
    public Map<String,Object> getDictionary(DevData obj, Boolean useTree, String dynaFields) {
        Map<String,Object> map=new HashMap<>();
        if (Objects.isNull(dynaFields) || StringUtil.containsItem(dynaFields,"groupSelection")) {
            if (useTree) map.put("dictGroupSelection", DictionaryUtil.getTreeViewData(obj,"select org_code,* from dev_reader_group","id","name","parent","id",null,null,null));
            else map.put("dictGroupSelection", DictionaryUtil.getDictListFromDatabase(obj,"select org_code,* from dev_reader_group","id","name",null,false,null));
        }
        if (Objects.isNull(dynaFields) || StringUtil.containsItem(dynaFields,"orgSelection")) {
            if (useTree) map.put("dictOrgSelection", DictionaryUtil.getTreeViewData(obj,"select org_code,name from sys_org","org_code","name","parent","org_code",null,null,null));
            else map.put("dictOrgSelection", DictionaryUtil.getDictListFromDatabase(obj,"select org_code,name from sys_org","org_code","name",null,false,null));
        }
        return map;
    }
    @Override
    public DevData getDefaultObject(DevData devData) throws IotequException {
        checkAvailable();
        if (devData==null)  devData=new DevData();
        else devData.setId(null);
        return devData;
    }
    @Override
    public void changeEmpty2Null(DevData devData) {
        if (Objects.nonNull(devData)) {
        }
    }
}
