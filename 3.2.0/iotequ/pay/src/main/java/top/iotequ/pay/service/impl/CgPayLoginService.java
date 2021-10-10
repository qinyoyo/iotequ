package top.iotequ.pay.service.impl;
import top.iotequ.pay.pojo.PayLogin;
import top.iotequ.pay.dao.PayLoginDao;
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
@ConditionalOnMissingClass({"top.iotequ.pay.service.impl.PayLoginService"})
@Service(value="payLoginService")
public class CgPayLoginService extends CgService<PayLogin>  {
    private static final Logger log = LoggerFactory.getLogger(CgPayLoginService.class);
    @Autowired
    private PayLoginDao payLoginDao;
    @Override
    public Class<PayLogin> getEntityClass() {
        return PayLogin.class;
    }
    @Override
    public IDaoService<PayLogin> getDaoService() {
        return payLoginDao ;
    }
    @Override
    public IFlowService<PayLogin> getFlowService() {
        return null;
    }
    @Override
    public Map<String,Object> getDictionary(PayLogin obj, Boolean useTree, String dynaFields) {
        Map<String,Object> map=new HashMap<>();
        if (Objects.isNull(dynaFields) || StringUtil.containsItem(dynaFields,"shopId")) map.put("dictShopId", DictionaryUtil.getDictListFromDatabase(obj,"pay_shop","id","name",null,false,null));
        return map;
    }
    @Override
    public PayLogin getDefaultObject(PayLogin payLogin) throws IotequException {
        return payLogin;
    }
    @Override
    public void changeEmpty2Null(PayLogin payLogin) {
        if (Objects.nonNull(payLogin)) {
        }
    }
    @Override
    public void changeNull2Default(PayLogin payLogin) {
        if (payLogin.getId()==null) {
            payLogin.setId(0);
        }
        if (payLogin.getPosId()==null) {
            payLogin.setPosId(0);
        }
        if (payLogin.getShopId()==null) {
            payLogin.setShopId(0);
        }
        if (payLogin.getOperatorId()==null) {
            payLogin.setOperatorId(0);
        }
        if (payLogin.getName()==null) {
            payLogin.setName("");
        }
        if (payLogin.getBatchNo()==null) {
            payLogin.setBatchNo("");
        }
        if (payLogin.getLoginTime()==null) {
            payLogin.setLoginTime(new Date());
        }
        if (payLogin.getRandomNo()==null) {
            payLogin.setRandomNo("");
        }
        if (payLogin.getAppVersion()==null) {
            payLogin.setAppVersion("");
        }
        if (payLogin.getTradeCount()==null) {
            payLogin.setTradeCount(0);
        }
        if (payLogin.getFailureCount()==null) {
            payLogin.setFailureCount(0);
        }
    }
}
