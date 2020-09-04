package top.iotequ.pay.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import top.iotequ.framework.exception.IotequException;
import top.iotequ.framework.service.BaseIotequService;
import top.iotequ.framework.util.EntityUtil;
import top.iotequ.framework.util.Util;
import top.iotequ.pay.dao.PayOperatorDao;
import top.iotequ.pay.pojo.PayOperator;
import top.iotequ.pay.utility.EncryptUtil;

import javax.servlet.http.HttpServletRequest;
import java.util.Objects;

@Service
public class PayOperatorService extends BaseIotequService<PayOperator> {
    @Autowired
    private PayOperatorDao payOperatorDao;

    void encryptPassword(PayOperator obj) throws IotequException {
        String pwd = obj.getPassword();
        if (Util.isEmpty(pwd)) pwd = "123456";
        try {
            obj.setPassword(EncryptUtil.encryptString(pwd, null, true));
        } catch (Exception e) {
            throw IotequException.newInstance(e);
        }
    }

    @Override
    public void beforeSave(PayOperator obj, HttpServletRequest request) throws IotequException {
        super.beforeSave(obj, request);
        if (Objects.isNull(obj.getId())) {
            encryptPassword(obj);
        } else {
            PayOperator obj0 = payOperatorDao.select(obj.getId());
            if (Objects.isNull(obj0) || !EntityUtil.entityEquals(obj0.getPassword(), obj.getPassword()))
                encryptPassword(obj);
        }
    }
}
