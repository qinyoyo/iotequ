package top.iotequ.checkIn.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import top.iotequ.checkIn.dao.CkRegisterDao;
import top.iotequ.checkIn.pojo.CkRegister;
import top.iotequ.framework.exception.IotequException;
import top.iotequ.svasclient.SvasService;
import top.iotequ.svasclient.SvasTypes;
import top.iotequ.util.*;

import javax.servlet.http.HttpServletRequest;
import java.util.Date;
import java.util.Map;

@Service
public class CkRegisterService extends CgCkRegisterService {
    @Autowired
    private SvasService svasService;
    @Autowired
    private CkRegisterDao ckRegisterDao;
    @Override
    public RestJson doAction(String action, String id, HttpServletRequest request) throws IotequException {
        RestJson j = new RestJson();
        try {
            if ("register".equals(action)) {
                Map<String,Object> params = HttpUtils.getRequestBody(request);
                int orgCode = Integer.parseInt(params.get("orgCode").toString());
                int mode = Integer.parseInt(params.get("mode").toString());
                String template = params.get("template").toString();
                SvasTypes.SvasMatched matchInfo = svasService.auth(template, 0);
                if (!Util.isEmpty(matchInfo) && matchInfo.count==1) {
                    String userNo = matchInfo.list.get(0).userNo;
                    String name = matchInfo.list.get(0).name;
                    return register(orgCode,mode,userNo, name);
                } else {
                    j.setSuccess(false);
                    j.setMessage("验证失败");
                }
            }
        } catch (Exception e) {
            j.setMessage(e);
        }
        return j;
    }
    public RestJson register(int orgCode, int mode, String userNo, String name) throws Exception {
        Date now = new Date();
        Date date0 = DateUtil.startOf(now,DateUtil.DAY), date1 = DateUtil.endOf(now,DateUtil.DAY);
        String id = SqlUtil.sqlQueryString("select id from ck_register where org_code=? and user_no=? and in_time between ? and ?", orgCode, userNo, date0, date1);

        if (mode==1) {
            if (!Util.isEmpty(id)) return new RestJson().setSuccess(false).setMessage(name + ": 您已上机, 无需重复登记");
            else return on(orgCode,now,userNo,name);
        } else if (mode == 3) {
            if (Util.isEmpty(id)) return new RestJson().setSuccess(false).setMessage(name+": 尚未办理上机");
            else return off(id,now,name);
        } else if (mode == 0) {
            if (Util.isEmpty(id)) return on(orgCode,now,userNo,name);
            else return off(id,now,name);
        } else return new RestJson().setSuccess(true).setMessage(name+": 签到成功");
    }
    private RestJson on(int orgCode, Date now, String userNo, String name) throws Exception {
        CkRegister rec = new CkRegister();
        rec.setInTime(now);
        rec.setOrgCode(orgCode);
        rec.setUserNo(userNo);
        ckRegisterDao.insert(rec);
        return new RestJson().setSuccess(true).setMessage(name+": 上机成功");
    }
    private RestJson off(String id, Date now,String name) throws Exception {
        SqlUtil.sqlExecute("update ck_register set out_time=? where id=?", now, id);
        return new RestJson().setSuccess(true).setMessage(name+": 下机成功");
    }
}
