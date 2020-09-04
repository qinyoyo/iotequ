package top.iotequ.ewallet.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Service;
import top.iotequ.ewallet.dao.EwUserDao;
import top.iotequ.ewallet.event.UserInfoChangedEvent;
import top.iotequ.ewallet.pojo.EWallet;
import top.iotequ.ewallet.pojo.EwUser;
import top.iotequ.ewallet.utility.EwDataRepo;
import top.iotequ.framework.exception.IotequException;
import top.iotequ.framework.exception.IotequThrowable;
import top.iotequ.framework.service.BaseIotequService;
import top.iotequ.framework.util.RestJson;
import top.iotequ.framework.util.Util;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Objects;

@Service
public class EwUserService extends BaseIotequService<EwUser> {
    @Autowired
    ApplicationContext applicationContext;
    @Autowired
    private EwUserDao ewUserDao;
    @Autowired
    private top.iotequ.svasclient.SvasService svasService;
    @Autowired
    private EwDataRepo ewDataRepo;

    @Override
    public void beforeList(List<EwUser> list, HttpServletRequest request) {
        super.beforeList(list, request);
        for (EwUser user : list) {
            user.setIsValid(ewDataRepo.isValid(user));
        }
    }

    @Override
    public void beforeUpdate(EwUser obj, HttpServletRequest request) throws IotequException {
        super.beforeUpdate(obj, request);
        if (!Objects.isNull(obj.getUserNo())) {
            obj.setIsValid(ewDataRepo.isValid(obj));
        }
    }

    @Override
    public void beforeSave(EwUser obj, HttpServletRequest request) throws IotequException {
        super.beforeSave(obj, request);
        if (Util.isEmpty(obj.getUserNo())) {
            obj.setBonusPoint(0);
            obj.setAmountMoney(0);
            String userNo = svasService.getUserNo(obj.getIdType(), obj.getIdNo(), obj.getName(), null, null);
            if (Util.isEmpty(userNo))
                throw new IotequException(IotequThrowable.INVALID_SCOPE, "svasClient失败，不支持直接创建账户");
            obj.setUserNo(userNo);
        } else {  // 禁止修改余额和积分
            EwUser ewUser = ewUserDao.select(obj.getUserNo());
            obj.setAmountMoney(ewUser.getAmountMoney());
            obj.setBonusPoint(ewUser.getBonusPoint());
        }
        EWallet.updateCheckCode(obj);
    }

    @Override
    public void afterSave(EwUser obj0, EwUser obj, HttpServletRequest request, RestJson j) throws IotequException {
        super.afterSave(obj0, obj, request, j);
        if (!Util.isEmpty(obj)) {
            applicationContext.publishEvent(new UserInfoChangedEvent(this, obj));
        }
    }

    @Override
    public void afterDelete(String ids, HttpServletRequest request, RestJson j)  throws IotequException {
        super.afterDelete(ids, request, j);
        if (!Util.isEmpty(ids)) {
            applicationContext.publishEvent(new UserInfoChangedEvent(this, ids));
        }
    }
}
