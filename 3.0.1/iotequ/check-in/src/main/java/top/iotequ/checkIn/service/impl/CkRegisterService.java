package top.iotequ.checkIn.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import top.iotequ.checkIn.dao.CkRegisterDao;
import top.iotequ.checkIn.pojo.CkRegister;
import top.iotequ.framework.dao.OrgDao;
import top.iotequ.framework.exception.IotequException;
import top.iotequ.framework.pojo.Org;
import top.iotequ.reader.dao.DevPeopleDao;
import top.iotequ.reader.pojo.DevPeople;
import top.iotequ.svasclient.SvasService;
import top.iotequ.svasclient.SvasTypes;
import top.iotequ.util.*;

import javax.servlet.http.HttpServletRequest;
import java.util.*;

@Service
public class CkRegisterService extends CgCkRegisterService {
    @Autowired
    private SvasService svasService;
    @Autowired
    private CkRegisterDao ckRegisterDao;
    @Autowired
    private DevPeopleDao devPeopleDao;
    @Autowired
    private OrgDao orgDao;

    static final String sound_err_on = "sound_err_on.mp3";
    static final String text_err_on = "您已上机, 无需重复登记";
    static final String sound_err_not_on = "sound_err_not_on.mp3";
    static final String text_err_not_on = "尚未办理上机";
    static final String sound_err_not_off = "sound_err_not_off.mp3";
    static final String text_err_not_off = "尚未下机";

    static final String sound_suc_on = "sound_suc_on.mp3";
    static final String text_suc_on = "上机成功";

    static final String sound_suc_off = "sound_suc_off.mp3";
    static final String text_suc_off = "下机成功";

    static final String sound_suc_cancel = "sound_suc_cancel.mp3";
    static final String text_suc_cancel = "取消下机成功";

    static final String sound_suc_remove = "sound_suc_remove.mp3";
    static final String text_suc_remove = "取消上机成功";

    static final String sound_sign = "sound_sign.mp3";
    static final String text_sign = "认证成功";

    static final String sound_err_sign = "sound_err_sign.mp3";
    static final String text_err_sign = "认证失败";

    @Override
    public RestJson doAction(String action, String id, HttpServletRequest request) throws IotequException {
        RestJson j = new RestJson();
        try {
            if ("register".equals(action)) {
                Map<String,Object> params = HttpUtils.getRequestBody(request);
                int orgCode = Integer.parseInt(params.get("orgCode").toString());
                String mode = params.get("mode").toString();
                if ("check".equals(mode)) { // 心跳检查，防止掉线
                    return j.setSuccess(true);
                }
                String template = params.get("template").toString();
                SvasTypes.SvasMatched matchInfo = svasService.auth(template, 0);
                if (!Util.isEmpty(matchInfo) && matchInfo.count==1) {
                    String userNo = matchInfo.list.get(0).userNo;
                    String name = matchInfo.list.get(0).name;
                    return register(orgCode,mode,userNo, name);
                } else {
                    j.setSuccess(false).data("sound",sound_err_sign)
                     .setMessage(text_err_sign);
                }
            }
        } catch (Exception e) {
            j.setMessage(e);
        }
        return j;
    }


    public RestJson register(int orgCode, String mode, String userNo, String name) throws Exception {
        RestJson j = new RestJson();
        Date now = new Date();
        Date date0 = DateUtil.startOf(now,DateUtil.DAY), date1 = DateUtil.endOf(now,DateUtil.DAY);
        CkRegister rec = ckRegisterDao.selectByUserNoOrgCodeInDate(userNo,orgCode,date0);
        j.data("name",name);
        if ("on".equals(mode)) {
            if (rec!=null) return j.setSuccess(false).data("sound",sound_err_on).setMessage(text_err_on);
            else return on(orgCode,now,userNo,name);
        } else if ("off".equals(mode)) {
            if (rec==null) return j.setSuccess(false).data("sound",sound_err_not_on).setMessage(text_err_not_on);
            else return off(rec.getId(),now,name);
        } else if ("auto".equals(mode)) {
            if (rec==null) return on(orgCode,now,userNo,name);
            else return off(rec.getId(),now,name);
        } else if ("cancel".equals(mode)) {
            if (rec!=null) {
                rec.setOffTime(null);
                ckRegisterDao.update(rec);
                return j.setSuccess(true).data("sound",sound_suc_cancel).setMessage(text_suc_cancel);
            }
            else return j.setSuccess(false).data("sound",sound_err_not_off).setMessage(text_err_not_off);
        } else if ("remove".equals(mode)) {
            if (rec!=null) {
                ckRegisterDao.delete(rec.getId());
                return j.setSuccess(true).data("sound",sound_suc_remove).setMessage(text_suc_remove);
            }
            else return j.setSuccess(false).data("sound",sound_err_not_on).setMessage(text_err_not_on);
        } else  return j.setSuccess(true).data("sound",sound_sign).setMessage(text_sign);
    }
    private RestJson on(int orgCode, Date now, String userNo, String name) throws Exception {
        DevPeople people = devPeopleDao.select(userNo);
        if (people==null) return new RestJson().setSuccess(false).data("sound",sound_err_sign).setMessage(text_err_sign);
        Org org = orgDao.select(orgCode);
        if (org==null) return new RestJson().setSuccess(false).data("sound",sound_err_sign).setMessage(text_err_sign);
        CkRegister rec = new CkRegister();
        rec.setOrgCode(orgCode);
        rec.setOrgName(org.getName());
        rec.setUserNo(userNo);
        rec.setName(people.getRealName());
        rec.setSex(people.getSex());
        rec.setBirthDate(people.getBirthDate());
        rec.setInDate(now);
        rec.setOnTime(now);
        ckRegisterDao.insert(rec);
        return new RestJson().setSuccess(true)
                .data(name,people.getRealName())
                .data("sound",sound_suc_on).setMessage(text_suc_on);
    }
    private RestJson off(String id, Date now,String name) throws Exception {
        SqlUtil.sqlExecute("update ck_register set out_time=? where id=?", now, id);
        return new RestJson().setSuccess(true)
                .data(name,name)
                .data("sound",sound_suc_off).setMessage(text_suc_off);
    }

    @Override
    public RestJson sqlQuery(Map<String, Object> params) throws Exception {
        String action = StringUtil.toString(params.get("action"));
        Integer orgCode = (params.get("orgCode")==null ? null : Integer.parseInt(params.get("orgCode").toString()));
        String orgFilter = (orgCode==null?"" : " and org_code = "+orgCode);
        Date dt0 = DateUtil.string2Date(params.get("date0").toString());
        Date dt1 = DateUtil.string2Date(params.get("date1").toString());
        RestJson j=new RestJson();
        String sql = "";
        List<Object> xAxis = new ArrayList<>();
        if ("amountByDay".equals(action)) {
            sql = "select in_date, org_name,  org_code, count(*) as amount from ck_register " +
                    "where in_date between ? and ? " + orgFilter +
                    "group by in_date, org_name, org_code";
            Date d = dt0;
            while (d.getTime()<dt1.getTime()) {
                xAxis.add(d);
                d = DateUtil.dateAdd(d,1,DateUtil.DAY);
            }
        } else if ("amountByMounth".equals(action)) {
            sql = "select DATE_FORMAT(in_date,'%Y-%m') as mounth, org_name,  org_code, count(*) as amount from ck_register " +
                    "where in_date between ? and ? "  + orgFilter +
                    "group by mounth, org_name, org_code";
            Date d = DateUtil.startOf(dt0,DateUtil.MONTH);
            while (d.getTime()<dt1.getTime()) {
                xAxis.add(d);
                d = DateUtil.dateAdd(d,1,DateUtil.MONTH);
            }
        } else if ("amountByAge".equals(action)) {
            sql = "select concat(truncate(round(datediff(CURDATE(),birth_date)/365.25,0)/10,0)*10, '-', truncate(round(datediff(CURDATE(),birth_date)/365.25,0)/10,0)*10 + 9) as age, " +
                    "org_name,  org_code, count(*) as amount from ck_register " +
                    "where in_date between ? and ?"  + orgFilter +
                    "group by age, org_name, org_code";
            while (d.getTime()<dt1.getTime()) {
                xAxis.add(d);
                d = DateUtil.dateAdd(d,1,DateUtil.MONTH);
            }
        }
        List<Map<String,Object>> data = SqlUtil.sqlQuery(orgCode==null, sql, dt0, dt1);
        if (!Util.isEmpty(data)) {
            for (Map<String,Object> m : data) {
                int org = Integer.parseInt(m.get("org_code").toString());
                m.put("orgName",OrgUtil.getOrgFullName(org));
            }
        }
        j.data(data);

        return j;
    }
}
