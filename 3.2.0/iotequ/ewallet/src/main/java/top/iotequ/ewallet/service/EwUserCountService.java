package top.iotequ.ewallet.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import top.iotequ.ewallet.pojo.EWallet;
import top.iotequ.ewallet.pojo.EwUserCount;
import top.iotequ.ewallet.utility.EwDataRepo;
import top.iotequ.framework.exception.IotequException;
import top.iotequ.framework.exception.IotequThrowable;
import top.iotequ.framework.service.BaseIotequService;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Objects;

@Service
public class EwUserCountService extends BaseIotequService<EwUserCount> {
    @Autowired
    private EwDataRepo ewDataRepo;

    @Override
    public void beforeList(List<EwUserCount> list, HttpServletRequest request) {
        super.beforeList(list, request);
        for (EwUserCount userCount : list) {
            userCount.setIsValid(ewDataRepo.isValid(userCount));
        }
    }

    @Override
    public void beforeSave(EwUserCount obj, HttpServletRequest request) throws IotequException {
        if (obj.getIsValid()) {
            super.beforeSave(obj, request);
            if (obj != null)
                EWallet.updateCheckCode(obj);
        } else throw new IotequException(IotequThrowable.INVALID_CLIENT,"账户无效被锁定");
    }

    @Override
    public void beforeUpdate(EwUserCount obj, HttpServletRequest request) throws IotequException {
        super.beforeUpdate(obj, request);
        if (!Objects.isNull(obj.getId())) {
            obj.setIsValid(ewDataRepo.isValid(obj));
        }
    }
}
