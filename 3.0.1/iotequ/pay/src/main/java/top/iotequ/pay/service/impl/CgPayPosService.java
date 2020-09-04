package top.iotequ.pay.service.impl;
import top.iotequ.pay.pojo.PayPos;
import top.iotequ.pay.dao.PayPosDao;
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
@ConditionalOnMissingClass({"top.iotequ.pay.service.impl.PayPosService"})
@Service(value="payPosService")
public class CgPayPosService extends CgService<PayPos>  {
private static final Logger log = LoggerFactory.getLogger(CgPayPosService.class);
    @Autowired
    private PayPosDao payPosDao;
    @Override
    public Class<PayPos> getEntityClass() {
        return PayPos.class;
    }
    @Override
    public IDaoService<PayPos> getDaoService() {
        return payPosDao ;
    }
    @Override
    public IFlowService<PayPos> getFlowService() {
        return null;
    }
    @Override
    public Map<String,Object> getDictionary(PayPos obj, Boolean useTree, String dynaFields) {
        Map<String,Object> map=new HashMap<>();
        if (Objects.isNull(dynaFields) || StringUtil.containsItem(dynaFields,"shopId")) map.put("dictShopId", DictionaryUtil.getDictListFromDatabase(obj,"pay_shop","id","name",null,false,null));
        if (Objects.isNull(dynaFields) || StringUtil.containsItem(dynaFields,"countProjectList")) map.put("dictCountProjectList", DictionaryUtil.getDictListFromDatabase(obj,"SELECT id as value,name as text FROM ew_count_project order by value","value","text",null,false,null));
        if (Objects.isNull(dynaFields) || StringUtil.containsItem(dynaFields,"timeProjectList")) map.put("dictTimeProjectList", DictionaryUtil.getDictListFromDatabase(obj,"SELECT id as value,name as text FROM ew_time_project order by value","value","text",null,false,null));
        return map;
    }
    @Override
    public PayPos getDefaultObject(PayPos payPos) throws IotequException {
        checkAvailable();
        if (payPos==null)  payPos=new PayPos();
        else payPos.setId(null);
        return payPos;
    }
    @Override
    public void changeEmpty2Null(PayPos payPos) {
        if (Objects.nonNull(payPos)) {
        }
    }
    @Override
    public void changeNull2Default(PayPos payPos) {
        if (payPos.getId()==null) {
            payPos.setId(0);
        }
        if (payPos.getNo()==null) {
            payPos.setNo("");
        }
        if (payPos.getShopId()==null) {
            payPos.setShopId(0);
        }
        if (payPos.getEwalletActive()==null) {
            payPos.setEwalletActive(Util.boolValue("1"));
        }
    }
}
