package top.iotequ.reader.service.impl;
import top.iotequ.reader.pojo.DevReader;
import top.iotequ.reader.dao.DevReaderDao;
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
import top.iotequ.framework.util.*;
import java.util.*;

/**************************************************
Create by iotequ codegenerator 3.0.0
Author : Qinyoyo
"**************************************************/
@ConditionalOnMissingClass({"top.iotequ.reader.service.impl.DevReaderService"})
@Service(value="devReaderService")
public class CgDevReaderService extends CgService<DevReader>  {
private static final Logger log = LoggerFactory.getLogger(CgDevReaderService.class);
    @Autowired
    private DevReaderDao devReaderDao;
    @Override
    public Class<DevReader> getEntityClass() {
        return DevReader.class;
    }
    @Override
    public IDaoService<DevReader> getDaoService() {
        return devReaderDao ;
    }
    @Override
    public IFlowService<DevReader> getFlowService() {
        return null;
    }
    @Override
    public Map<String,Object> getDictionary(DevReader obj, Boolean useTree, String dynaFields) {
        Map<String,Object> map=new HashMap<>();
        if (Objects.isNull(dynaFields) || StringUtil.containsItem(dynaFields,"type")) map.put("dictType", DictionaryUtil.getDictListFromDatabase(obj,"dev_type",null,null,null,false,null));
        if (Objects.isNull(dynaFields) || StringUtil.containsItem(dynaFields,"readerGroup")) {
            if (useTree) map.put("dictReaderGroup", DictionaryUtil.getTreeViewData(obj,"select id,name,org_code from dev_reader_group","id","name","parent","id",null,null,null));
            else map.put("dictReaderGroup", DictionaryUtil.getDictListFromDatabase(obj,"select id,name,org_code from dev_reader_group","id","name",null,false,null));
        }
        if (Objects.isNull(dynaFields) || StringUtil.containsItem(dynaFields,"connectType")) map.put("dictConnectType", DictionaryUtil.getDictListFromDatabase(obj,"dev_connect_type",null,null,null,false,null));
        if (Objects.isNull(dynaFields) || StringUtil.containsItem(dynaFields,"devMode")) map.put("dictDevMode", DictionaryUtil.getDictListFromDatabase(obj,"dev_mode",null,null,null,false,null));
        if (Objects.isNull(dynaFields) || StringUtil.containsItem(dynaFields,"alignMethod")) map.put("dictAlignMethod", DictionaryUtil.getDictListFromDatabase(obj,"dev_auth_type",null,null,null,false,null));
        if (Objects.isNull(dynaFields) || StringUtil.containsItem(dynaFields,"wengenform")) map.put("dictWengenform", DictionaryUtil.getDictListFromDatabase(obj,"dev_wg_form",null,null,null,false,null));
        if (Objects.isNull(dynaFields) || StringUtil.containsItem(dynaFields,"wengenOutput")) map.put("dictWengenOutput", DictionaryUtil.getDictListFromDatabase(obj,"dev_wg_out",null,null,null,false,null));
        return map;
    }
    @Override
    public DevReader getDefaultObject(DevReader devReader) throws IotequException {
        checkAvailable();
        if (devReader==null)  devReader=new DevReader();
        else devReader.setId(null);
        return devReader;
    }
    @Override
    public void changeEmpty2Null(DevReader devReader) {
        if (Objects.nonNull(devReader)) {
        }
    }
    @Override
    public void changeNull2Default(DevReader devReader) {
        if (devReader.getId()==null) {
            devReader.setId("");
        }
        if (devReader.getReaderNo()==null) {
            devReader.setReaderNo("");
        }
        if (devReader.getName()==null) {
            devReader.setName("");
        }
        if (devReader.getType()==null) {
            devReader.setType("D10");
        }
        if (devReader.getReaderGroup()==null) {
            devReader.setReaderGroup(0);
        }
        if (devReader.getConnectType()==null) {
            devReader.setConnectType("");
        }
        if (devReader.getIp()==null) {
            devReader.setIp("");
        }
        if (devReader.getDevMode()==null) {
            devReader.setDevMode("");
        }
        if (devReader.getIsOnline()==null) {
            devReader.setIsOnline(Util.boolValue("0"));
        }
        if (devReader.getIsTimeSync()==null) {
            devReader.setIsTimeSync(Util.boolValue("0"));
        }
        if (devReader.getAlignMethod()==null) {
        }
        if (devReader.getBlacklightTime()==null) {
        }
        if (devReader.getVoiceprompt()==null) {
            devReader.setVoiceprompt(Util.boolValue("1"));
        }
        if (devReader.getMenuTime()==null) {
        }
        if (devReader.getWengenform()==null) {
        }
        if (devReader.getWengenOutput()==null) {
        }
        if (devReader.getWengenOutArea()==null) {
        }
        if (devReader.getRegfingerOutTime()==null) {
        }
        if (devReader.getAuthfingerOutTime()==null) {
        }
    }
}
