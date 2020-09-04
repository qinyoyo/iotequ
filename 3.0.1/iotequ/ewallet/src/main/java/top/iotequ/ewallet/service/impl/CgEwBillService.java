package top.iotequ.ewallet.service.impl;
import top.iotequ.ewallet.pojo.EwBill;
import top.iotequ.ewallet.dao.EwBillDao;
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
@ConditionalOnMissingClass({"top.iotequ.ewallet.service.impl.EwBillService"})
@Service(value="ewBillService")
public class CgEwBillService extends CgService<EwBill>  {
private static final Logger log = LoggerFactory.getLogger(CgEwBillService.class);
    @Autowired
    private EwBillDao ewBillDao;
    @Override
    public Class<EwBill> getEntityClass() {
        return EwBill.class;
    }
    @Override
    public IDaoService<EwBill> getDaoService() {
        return ewBillDao ;
    }
    @Override
    public IFlowService<EwBill> getFlowService() {
        return null;
    }
    @Override
    public Map<String,Object> getDictionary(EwBill obj, Boolean useTree, String dynaFields) {
        Map<String,Object> map=new HashMap<>();
        return map;
    }
    @Override
    public EwBill getDefaultObject(EwBill ewBill) throws IotequException {
        return ewBill;
    }
    @Override
    public void changeEmpty2Null(EwBill ewBill) {
        if (Objects.nonNull(ewBill)) {
        }
    }
    @Override
    public void changeNull2Default(EwBill ewBill) {
        if (ewBill.getNo()==null) {
            ewBill.setNo("");
        }
        if (ewBill.getCanceled()==null) {
            ewBill.setCanceled(Util.boolValue("0"));
        }
        if (ewBill.getIsCharge()==null) {
            ewBill.setIsCharge(Util.boolValue("0"));
        }
        if (ewBill.getUserNo()==null) {
            ewBill.setUserNo("");
        }
        if (ewBill.getBatchNo()==null) {
            ewBill.setBatchNo("");
        }
        if (ewBill.getDt()==null) {
            ewBill.setDt(new Date());
        }
        if (ewBill.getOperationType()==null) {
            ewBill.setOperationType(0);
        }
        if (ewBill.getCostType()==null) {
            ewBill.setCostType(1);
        }
        if (ewBill.getProjectId()==null) {
            ewBill.setProjectId(0);
        }
        if (ewBill.getProjectName()==null) {
            ewBill.setProjectName("消费");
        }
        if (ewBill.getProjectPrice()==null) {
            ewBill.setProjectPrice(0);
        }
        if (ewBill.getAmount()==null) {
            ewBill.setAmount(0);
        }
        if (ewBill.getAmountBefore()==null) {
            ewBill.setAmountBefore(0);
        }
        if (ewBill.getBonus()==null) {
            ewBill.setBonus(0);
        }
        if (ewBill.getBonusBefore()==null) {
            ewBill.setBonusBefore(0);
        }
        if (ewBill.getDeviceNo()==null) {
            ewBill.setDeviceNo("");
        }
        if (ewBill.getShopId()==null) {
            ewBill.setShopId("");
        }
        if (ewBill.getName()==null) {
            ewBill.setName("");
        }
        if (ewBill.getDeviceStream()==null) {
            ewBill.setDeviceStream("");
        }
        if (ewBill.getDeviceDt()==null) {
            ewBill.setDeviceDt(new Date());
        }
        if (ewBill.getTradeNo()==null) {
            ewBill.setTradeNo("");
        }
        if (ewBill.getOperatorNo()==null) {
            ewBill.setOperatorNo("");
        }
        if (ewBill.getCheckCode()==null) {
            ewBill.setCheckCode("");
        }
        if (ewBill.getLoginId()==null) {
            ewBill.setLoginId(0);
        }
    }
}
