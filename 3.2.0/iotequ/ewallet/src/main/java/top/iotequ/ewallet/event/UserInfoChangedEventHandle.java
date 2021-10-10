package top.iotequ.ewallet.event;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Service;
import top.iotequ.framework.util.EntityUtil;
import top.iotequ.svasclient.SvasService;
import top.iotequ.ewallet.dao.EwUserDao;
import top.iotequ.ewallet.pojo.EWallet;
import top.iotequ.ewallet.pojo.EwUser;
import top.iotequ.framework.util.SqlUtil;
import top.iotequ.framework.util.Util;

@Service
public class UserInfoChangedEventHandle implements ApplicationListener<UserInfoChangedEvent> {
    @Autowired
    private SvasService svasService;
    @Autowired
    private EwUserDao ewUserDao;

    @Override
    public void onApplicationEvent(UserInfoChangedEvent event) {
        if (!(event.getSource() instanceof EwUserController)) {
            try {
                if (event.getRemove()) {
                    String ids = event.getUser().getUserNo();
                    SqlUtil.sqlExecute("update ew_user set is_active=0 where user_no = ?", ids.split(","));
                } else {
                    EwUser user = event.getUser();
                    String userNo = user.getUserNo();
                    if (Util.isEmpty(userNo)) {
                        user.setBonusPoint(0);
                        user.setAmountMoney(0);
                        userNo = svasService.getUserNo(user.getIdType(), user.getIdNo(), user.getName(), null, null);
                        if (Util.isEmpty(userNo)) throw new Exception("svasClient失败，不支持直接创建账户");
                        user.setUserNo(userNo);
                        EWallet.updateCheckCode(user);
                        ewUserDao.insert(user);
                    } else {  // 禁止修改余额和积分
                        EwUser ewUser = ewUserDao.select(userNo);
                        if (ewUser == null) {
                            user.setBonusPoint(0);
                            user.setAmountMoney(0);
                            EWallet.updateCheckCode(user);
                            ewUserDao.insert(user);
                        } else {
                            Integer a = ewUser.getAmountMoney();
                            Integer b = ewUser.getBonusPoint();
                            EntityUtil.copyNonNullField(ewUser, user);
                            ewUser.setAmountMoney(a);
                            ewUser.setBonusPoint(b);
                            EWallet.updateCheckCode(ewUser);
                            ewUserDao.update(ewUser);
                        }
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
