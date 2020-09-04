package top.iotequ.ewallet.service.impl;
import top.iotequ.ewallet.pojo.EwUser;
import top.iotequ.ewallet.dao.EwUserDao;
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
@ConditionalOnMissingClass({"top.iotequ.ewallet.service.impl.EwUserService"})
@Service(value="ewUserService")
public class CgEwUserService extends CgService<EwUser>  {
private static final Logger log = LoggerFactory.getLogger(CgEwUserService.class);
    @Autowired
    private EwUserDao ewUserDao;
    @Override
    public Class<EwUser> getEntityClass() {
        return EwUser.class;
    }
    @Override
    public IDaoService<EwUser> getDaoService() {
        return ewUserDao ;
    }
    @Override
    public IFlowService<EwUser> getFlowService() {
        return null;
    }
    @Override
    public Map<String,Object> getDictionary(EwUser obj, Boolean useTree, String dynaFields) {
        Map<String,Object> map=new HashMap<>();
        if (Objects.isNull(dynaFields) || StringUtil.containsItem(dynaFields,"gender")) map.put("dictGender", DictionaryUtil.getDictListFromDatabase(obj,"sys_sex",null,null,null,false,null));
        if (Objects.isNull(dynaFields) || StringUtil.containsItem(dynaFields,"idType")) map.put("dictIdType", DictionaryUtil.getDictListFromDatabase(obj,"sys_id_type",null,null,null,false,null));
        return map;
    }
    @Override
    public EwUser getDefaultObject(EwUser ewUser) throws IotequException {
        checkAvailable();
        if (ewUser==null)  ewUser=new EwUser();
        else ewUser.setUserNo(null);
        return ewUser;
    }
    @Override
    public void changeEmpty2Null(EwUser ewUser) {
        if (Objects.nonNull(ewUser)) {
            if ("".equals(ewUser.getMobilePhone())) ewUser.setMobilePhone(null);
            if ("".equals(ewUser.getEmail())) ewUser.setEmail(null);
            if ("".equals(ewUser.getWechatOpenid())) ewUser.setWechatOpenid(null);
        }
    }
    @Override
    public void changeNull2Default(EwUser ewUser) {
        if (ewUser.getUserNo()==null) {
            ewUser.setUserNo("");
        }
        if (ewUser.getName()==null) {
            ewUser.setName("");
        }
        if (ewUser.getGender()==null) {
            ewUser.setGender(0);
        }
        if (ewUser.getIdType()==null) {
            ewUser.setIdType(0);
        }
        if (ewUser.getIdNo()==null) {
            ewUser.setIdNo("");
        }
        if (ewUser.getBonusPoint()==null) {
            ewUser.setBonusPoint(0);
        }
        if (ewUser.getAmountMoney()==null) {
            ewUser.setAmountMoney(0);
        }
        if (ewUser.getCostLimit()==null) {
            ewUser.setCostLimit(0);
        }
        if (ewUser.getDayLimit()==null) {
            ewUser.setDayLimit(0);
        }
        if (ewUser.getCheckCode()==null) {
            ewUser.setCheckCode("");
        }
    }
}
