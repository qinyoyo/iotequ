package top.iotequ.checkIn.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Service;
import top.iotequ.checkIn.dao.CkRegisterDao;
import top.iotequ.checkIn.pojo.CkRegister;
import top.iotequ.framework.dao.OrgDao;
import top.iotequ.framework.event.DeviceEvent;
import top.iotequ.framework.event.PeopleInfoChangedEvent;
import top.iotequ.framework.exception.IotequException;
import top.iotequ.framework.pojo.Org;
import top.iotequ.reader.dao.DevPeopleDao;
import top.iotequ.reader.pojo.DevPeople;
import top.iotequ.reader.service.impl.DevPeopleService;
import top.iotequ.svasclient.SvasService;
import top.iotequ.svasclient.SvasTypes;
import top.iotequ.util.*;

import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.util.*;

@Service
public class CkRegisterService extends CgCkRegisterService implements ApplicationListener<DeviceEvent> {
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

    static final String sound_err_too_many = "sound_err_too_many.mp3";
    static final String text_err_too_many = "请勿频繁打卡";

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
                    DeviceEvent event = new DeviceEvent(this);
                    event.setDeviceType("U53");
                    event.setDeviceNo("<null>");
                    event.setDeviceMode("AD");
                    event.setTime(new Date());
                    event.setUserNo(userNo);
                    event.setWarning(false);
                    event.put("authType", (byte)0);
                    event.put("auditeeAuthType", (byte)0);
                    Util.getApplicationContext().publishEvent(event);
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
        if (rec!=null) {
            Date lastTime =  (rec.getOffTime()==null ? rec.getOnTime() : rec.getOffTime());
            int lt = DateUtil.get(lastTime,DateUtil.HOUR) * 60 + DateUtil.get(lastTime,DateUtil.MINUTE);
            int nw = DateUtil.get(now,DateUtil.HOUR) * 60 + DateUtil.get(now,DateUtil.MINUTE);
            if (nw - lt < 2 || (!"cancel".equals(mode) && !"remove".equals(mode) && nw - lt < 5))
                return j.setSuccess(false).data("name",people.getRealName()).data("sound",sound_err_too_many).setMessage(text_err_too_many);
        }
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
        Date dt0 = params.get("date0")==null?null:DateUtil.string2Date(params.get("date0").toString());
        Date dt1 = params.get("date1")==null?null:DateUtil.string2Date(params.get("date1").toString());
        RestJson j=new RestJson();
        List<Map<String,Object>> data = queryStatData(action,orgCode,dt0,dt1);
        if (data!=null) j.data(data);
        return j;
    }

    public List<Map<String,Object>> queryStatData(String action,Integer orgCode,Date dt0,Date dt1) throws Exception {
        String orgFilter = (orgCode==null || orgCode==OrgUtil.ALL_PERMISSION ? "" : "org_code in ("+OrgUtil.getOrgAndChildrenOrgList(orgCode)+") ");
        String dateFilter = "";
        if (dt0!=null && dt1!=null) {
            dateFilter = SqlUtil.sqlString("in_date between ? and ?",dt0,dt1);
        } else if (dt0!=null) {
            dateFilter = SqlUtil.sqlString("in_date >= ?",dt0);
        } else if (dt1!=null) {
            dateFilter = SqlUtil.sqlString("in_date <= ?",dt1);
        }
        String filter = "";
        if (!orgFilter.isEmpty() && !dateFilter.isEmpty()) filter = "where "+orgFilter + " and " + dateFilter +" ";
        else if (!orgFilter.isEmpty()) filter = "where "+orgFilter+" ";
        else if (!dateFilter.isEmpty()) filter = "where "+dateFilter+" ";

        String sql = "";

        if ("amountByDay".equals(action)) {  // 流量按天统计
            sql = "select in_date, org_name,  org_code, count(*) as amount from ck_register " +
                    filter +
                    "group by in_date, org_name, org_code";
        } else if ("amountByMonth".equals(action)) {  // 流量按照天统计
            sql = "select DATE_FORMAT(in_date,'%Y-%m') as month, org_name,  org_code, count(*) as amount from ck_register " +
                    filter +
                    "group by month, org_name, org_code";
        } else if ("amountByAge".equals(action)) {  // 区间年龄段统计
            sql = "select concat(truncate(round(datediff(CURDATE(),birth_date)/365.25,0)/10,0)*10, '-', truncate(round(datediff(CURDATE(),birth_date)/365.25,0)/10,0)*10 + 9) as age, " +
                    "org_name,  org_code, count(*) as amount from ck_register " +
                    filter +
                    "group by age, org_name, org_code";
        } else if ("amountByAgeMonth".equals(action)) {  // 区间年龄段月统计
            sql = "select concat(truncate(round(datediff(CURDATE(),birth_date)/365.25,0)/10,0)*10, '-', truncate(round(datediff(CURDATE(),birth_date)/365.25,0)/10,0)*10 + 9) as age, " +
                    "DATE_FORMAT(in_date,'%Y-%m') as month, count(*) as amount from ck_register " +
                    filter +
                    "group by age, month";
        } else if ("distributionByAge".equals(action)) {  // 年龄分布统计
            sql = "select concat(truncate(round(datediff(CURDATE(),birth_date)/365.25,0)/10,0)*10, '-', truncate(round(datediff(CURDATE(),birth_date)/365.25,0)/10,0)*10 + 9) as age, " +
                    "count(*) as amount from ck_register " +
                    filter +
                    "group by age";
        } else if ("timeByDay".equals(action)) {  // 使用时长按天统计
            sql = "select in_date, org_name,  org_code, round(sum(time_to_sec(off_time)-time_to_sec(on_time))/3600,1) as amount from ck_register  " +
                    (filter.isEmpty() ? "where off_time is not null " : filter+" and off_time is not null ") +
                    "group by in_date, org_name, org_code";
        } else if ("amountByArea".equals(action)) {
            orgCode = 1;
            sql = "SELECT " +
                    "  case " +
                    "    when note regexp '成都市郫都区' then '郫都区'" +
                    "    when note regexp '四川省郫县' then '郫都区'" +
                    "    when note regexp '四川省.*县' then substring(note,4,position('县' in note)-3) " +
                    "    when note regexp '四川省.*市' then substring(note,4,position('市' in note)-3) " +
                    "    when note regexp '成都市' then left(note,3)" +
                    "    else '其他' " +
                    "  end as area," +
                    "  count(*) as amount" +
                    " FROM dev_people" +
                    " group by area order by amount desc";
        }
        else return null;
        //System.out.println(sql);
        List<Map<String,Object>> data = SqlUtil.sqlQuery(orgCode==null || orgCode==OrgUtil.ALL_PERMISSION, sql);
        if ("amountByArea".equals(action) && data!=null && data.size()>20) {
            int otherAmout = 0, otherIndex = -1;
            List<Map<String,Object>> list = new ArrayList<>();
            for (int i=0;i<data.size();i++) {
                Map<String,Object> m = data.get(i);
                if (i<20) {
                    list.add(m);
                    if ("其他".equals(StringUtil.toString(m.get("area")))) {
                        otherIndex = i;
                        otherAmout = Integer.parseInt(m.get("amount").toString());
                    }
                } else {
                    otherAmout += Integer.parseInt(m.get("amount").toString());
                }
            }
            if (otherIndex>=0) list.get(otherIndex).put("amount",otherAmout);
            else {
                Map<String,Object> o = new HashMap<String,Object>();
                o.put("area","其他");
                o.put("amount",otherAmout);
                list.add(o);
            }
            return list;
        } else return data;
    }
    @Override
    public void onApplicationEvent(DeviceEvent event) {
        try {
            if (event.getSource() instanceof CkRegisterService) return;
            String readerNo = event.getDeviceNo();
            if (readerNo==null) return;
            String userNo = event.getUserNo();
            if (userNo==null) return;
            Integer orgCode = SqlUtil.sqlQueryInteger(false,"select org_code from dev_reader_group g, dev_reader r where r.reader_group = g.id and r.reader_no = ?",readerNo);
            if (orgCode==null) return;
            DevPeople people = devPeopleDao.select(userNo);
            if (people==null) return;
            Org org = orgDao.select(orgCode);
            if (org==null) return;
            register(orgCode, "auto",  userNo,  people,  org);
        } catch (Exception e) {}
    }
}
