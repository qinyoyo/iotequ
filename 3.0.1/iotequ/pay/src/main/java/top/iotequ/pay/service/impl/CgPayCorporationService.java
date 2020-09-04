package top.iotequ.pay.service.impl;
import top.iotequ.pay.pojo.PayCorporation;
import top.iotequ.pay.dao.PayCorporationDao;
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
@ConditionalOnMissingClass({"top.iotequ.pay.service.impl.PayCorporationService"})
@Service(value="payCorporationService")
public class CgPayCorporationService extends CgService<PayCorporation>  {
private static final Logger log = LoggerFactory.getLogger(CgPayCorporationService.class);
    @Autowired
    private PayCorporationDao payCorporationDao;
    @Override
    public Class<PayCorporation> getEntityClass() {
        return PayCorporation.class;
    }
    @Override
    public IDaoService<PayCorporation> getDaoService() {
        return payCorporationDao ;
    }
    @Override
    public IFlowService<PayCorporation> getFlowService() {
        return null;
    }
    @Override
    public Map<String,Object> getDictionary(PayCorporation obj, Boolean useTree, String dynaFields) {
        Map<String,Object> map=new HashMap<>();
        if (Objects.isNull(dynaFields) || StringUtil.containsItem(dynaFields,"parentId")) {
            if (useTree) map.put("dictParentId", DictionaryUtil.getTreeViewData(obj,"pay_corporation","id","name","parent_id","id",null,null,null));
            else map.put("dictParentId", DictionaryUtil.getDictListFromDatabase(obj,"pay_corporation","id","name",null,false,null));
        }
        return map;
    }
    @Override
    public PayCorporation getDefaultObject(PayCorporation payCorporation) throws IotequException {
        checkAvailable();
        if (payCorporation==null)  payCorporation=new PayCorporation();
        else payCorporation.setId(null);
        return payCorporation;
    }
    @Override
    public void changeEmpty2Null(PayCorporation payCorporation) {
        if (Objects.nonNull(payCorporation)) {
        }
    }
}
