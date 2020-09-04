package top.iotequ.pay.service.impl;
import top.iotequ.pay.pojo.PayOperator;
import top.iotequ.pay.dao.PayOperatorDao;
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
@ConditionalOnMissingClass({"top.iotequ.pay.service.impl.PayOperatorService"})
@Service(value="payOperatorService")
public class CgPayOperatorService extends CgService<PayOperator>  {
private static final Logger log = LoggerFactory.getLogger(CgPayOperatorService.class);
    @Autowired
    private PayOperatorDao payOperatorDao;
    @Override
    public Class<PayOperator> getEntityClass() {
        return PayOperator.class;
    }
    @Override
    public IDaoService<PayOperator> getDaoService() {
        return payOperatorDao ;
    }
    @Override
    public IFlowService<PayOperator> getFlowService() {
        return null;
    }
    @Override
    public Map<String,Object> getDictionary(PayOperator obj, Boolean useTree, String dynaFields) {
        Map<String,Object> map=new HashMap<>();
        if (Objects.isNull(dynaFields) || StringUtil.containsItem(dynaFields,"shopId")) map.put("dictShopId", DictionaryUtil.getDictListFromDatabase(obj,"pay_shop","id","name",null,false,null));
        return map;
    }
    @Override
    public PayOperator getDefaultObject(PayOperator payOperator) throws IotequException {
        checkAvailable();
        if (payOperator==null)  payOperator=new PayOperator();
        else payOperator.setId(null);
        return payOperator;
    }
    @Override
    public void changeEmpty2Null(PayOperator payOperator) {
        if (Objects.nonNull(payOperator)) {
            if ("".equals(payOperator.getUserNo())) payOperator.setUserNo(null);
        }
    }
}
