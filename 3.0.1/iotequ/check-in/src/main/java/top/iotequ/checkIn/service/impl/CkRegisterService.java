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
    static final String text_err_on = "您已登记, 无需重复登记";
    static final String sound_err_not_on = "sound_err_not_on.mp3";
    static final String text_err_not_on = "您尚未办理登记";
    static final String sound_err_not_off = "sound_err_not_off.mp3";
    static final String text_err_not_off = "您尚未下机";

    static final String sound_suc_on = "sound_suc_on.mp3";
    static final String text_suc_on = "登记成功";

    static final String sound_suc_off = "sound_suc_off.mp3";
    static final String text_suc_off = "下机完成";

    static final String sound_suc_cancel = "sound_suc_cancel.mp3";
    static final String text_suc_cancel = "已经取消下机";

    static final String sound_suc_remove = "sound_suc_remove.mp3";
    static final String text_suc_remove = "成功删除登记";

    static final String sound_sign = "sound_sign.mp3";
    static final String text_sign = "认证成功";

    static final String sound_err_sign = "sound_err_sign.mp3";
    static final String text_err_sign = "识别失败";

    @Override
    public RestJson doAction(String action, String id, HttpServletRequest request) throws IotequException {
        RestJson j = new RestJson();
        try {
            if ("register".equals(action)) {
                Map<String,Object> params = HttpUtils.getRequestBody(request);
                int orgCode = Integer.parseInt(params.get("orgCode").toString());
                Org org = orgDao.select(orgCode);
                if (org==null) return new RestJson().setSuccess(false).data("sound",sound_err_sign).setMessage(text_err_sign);
                String mode = params.get("mode").toString();
                if ("check".equals(mode)) { // 心跳检查，防止掉线
                    return j.setSuccess(true);
                }
                String template = params.get("template").toString();
                SvasTypes.SvasMatched matchInfo = svasService.auth(template, 0);
                if (!Util.isEmpty(matchInfo) && matchInfo.count==1) {
                    String userNo = matchInfo.list.get(0).userNo;
                    DevPeople people = devPeopleDao.select(userNo);
                    if (people==null) return new RestJson().setSuccess(false).data("sound",sound_err_sign).setMessage(text_err_sign);
                    return register(orgCode,mode,userNo, people, org);
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


    public RestJson register(int orgCode, String mode, String userNo, DevPeople people, Org org) throws Exception {
        RestJson j = new RestJson();
        Date now = new Date();
        Date date0 = DateUtil.startOf(now,DateUtil.DAY);
        CkRegister rec = ckRegisterDao.selectByUserNoOrgCodeInDate(userNo,orgCode,date0);
        j.data("name",people.getRealName());
        if ("on".equals(mode)) {
            if (rec!=null) return j.setSuccess(false).data("sound",sound_err_on).setMessage(text_err_on);
            else return on(orgCode,now,people,org);
        } else if ("off".equals(mode)) {
            if (rec==null) return j.setSuccess(false).data("sound",sound_err_not_on).setMessage(text_err_not_on);
            else return off(rec,now);
        } else if ("auto".equals(mode)) {
            if (rec==null) return on(orgCode,now,people,org);
            else return off(rec,now);
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
    private RestJson on(int orgCode, Date now, DevPeople people, Org org) throws Exception {
        CkRegister rec = new CkRegister();
        rec.setOrgCode(orgCode);
        rec.setOrgName(org.getName());
        rec.setUserNo(people.getUserNo());
        rec.setName(people.getRealName());
        rec.setSex(people.getSex());
        rec.setBirthDate(people.getBirthDate());
        rec.setInDate(now);
        rec.setOnTime(now);
        ckRegisterDao.insert(rec);
        return new RestJson().setSuccess(true)
                .data("name",people.getRealName())
                .data("sound",sound_suc_on).setMessage(text_suc_on);
    }
    private RestJson off(CkRegister rec, Date now) throws Exception {
        rec.setOffTime(now);
        ckRegisterDao.update(rec);
        return new RestJson().setSuccess(true)
                .data("name",rec.getName())
                .data("sound",sound_suc_off).setMessage(text_suc_off);
    }

    @Override
    public RestJson sqlQuery(Map<String, Object> params) throws Exception {
        String action = StringUtil.toString(params.get("action"));
        Integer orgCode = (params.get("orgCode")==null ? null : Integer.parseInt(params.get("orgCode").toString()));
        String orgFilter = (orgCode==null?"" : " and org_code in ("+OrgUtil.getOrgAndChildrenOrgList(orgCode)+") ");
        Date dt0 = DateUtil.string2Date(params.get("date0").toString());
        Date dt1 = DateUtil.string2Date(params.get("date1").toString());
        RestJson j=new RestJson();
        String sql = "";
        if ("amountByDay".equals(action)) {  // 流量按天统计
            sql = "select in_date, org_name,  org_code, count(*) as amount from ck_register " +
                    "where in_date between ? and ? " + orgFilter +
                    "group by in_date, org_name, org_code";
        } else if ("amountByMonth".equals(action)) {  // 流量按照天统计
            sql = "select DATE_FORMAT(in_date,'%Y-%m') as month, org_name,  org_code, count(*) as amount from ck_register " +
                    "where in_date between ? and ? "  + orgFilter +
                    "group by month, org_name, org_code";
        } else if ("amountByAge".equals(action)) {  // 区间年龄段统计
            sql = "select concat(truncate(round(datediff(CURDATE(),birth_date)/365.25,0)/10,0)*10, '-', truncate(round(datediff(CURDATE(),birth_date)/365.25,0)/10,0)*10 + 9) as age, " +
                    "org_name,  org_code, count(*) as amount from ck_register " +
                    "where in_date between ? and ?"  + orgFilter +
                    "group by age, org_name, org_code";
        } else if ("amountByAgeMonth".equals(action)) {  // 区间年龄段月统计
            sql = "select concat(truncate(round(datediff(CURDATE(),birth_date)/365.25,0)/10,0)*10, '-', truncate(round(datediff(CURDATE(),birth_date)/365.25,0)/10,0)*10 + 9) as age, " +
                    "DATE_FORMAT(in_date,'%Y-%m') as month, count(*) as amount from ck_register " +
                    "where in_date between ? and ?"  + orgFilter +
                    "group by age, month";
        }
        System.out.println(SqlUtil.sqlString(sql,dt0,dt1));
        List<Map<String,Object>> data = SqlUtil.sqlQuery(orgCode==null, sql, dt0, dt1);
        j.data(data);
        return j;
    }
}
