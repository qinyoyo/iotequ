package top.iotequ.pay.service.impl;
import top.iotequ.pay.pojo.PayShop;
import top.iotequ.pay.dao.PayShopDao;
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
@ConditionalOnMissingClass({"top.iotequ.pay.service.impl.PayShopService"})
@Service(value="payShopService")
public class CgPayShopService extends CgService<PayShop>  {
    private static final Logger log = LoggerFactory.getLogger(CgPayShopService.class);
    @Autowired
    private PayShopDao payShopDao;
    @Override
    public Class<PayShop> getEntityClass() {
        return PayShop.class;
    }
    @Override
    public IDaoService<PayShop> getDaoService() {
        return payShopDao ;
    }
    @Override
    public IFlowService<PayShop> getFlowService() {
        return null;
    }
    @Override
    public Map<String,Object> getDictionary(PayShop obj, Boolean useTree, String dynaFields) {
        Map<String,Object> map=new HashMap<>();
        if (Objects.isNull(dynaFields) || StringUtil.containsItem(dynaFields,"corporationId")) {
            if (useTree) map.put("dictCorporationId", DictionaryUtil.getTreeViewData(obj,"pay_corporation","id","name","parent_id",null,null,null,null));
            else map.put("dictCorporationId", DictionaryUtil.getDictListFromDatabase(obj,"pay_corporation","id","name",null,false,null));
        }
        return map;
    }
    @Override
    public PayShop getDefaultObject(PayShop payShop) throws IotequException {
        checkAvailable();
        if (payShop==null)  payShop=new PayShop();
        else payShop.setId(null);
        return payShop;
    }
    @Override
    public void changeEmpty2Null(PayShop payShop) {
        if (Objects.nonNull(payShop)) {
        }
    }
}
