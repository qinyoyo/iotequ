package top.iotequ.svasclient;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.core.env.Environment;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import top.iotequ.framework.exception.IotequException;
import top.iotequ.util.DateUtil;
import top.iotequ.util.EntityUtil;
import top.iotequ.util.SqlUtil;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.text.ParseException;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
@RestController
@RequestMapping("/res/svastest")
@ConditionalOnProperty(value = "svas.matchTestSyncMinutes")
public class SvasMatchTestController {
    /* 以下为测试需要的同步函数*/
    volatile Date testStartTime = null;
    volatile int  testClient = 0;
    @Autowired
    SvasService svasService;
    @Autowired
    Environment environment;
    void initDb() {
        String sqlCreate = "CREATE TABLE `dev_svas_test` (" +
                "`id` INT NOT NULL AUTO_INCREMENT," +
                "`us_match` INT NOT NULL," +
                "`us_min_match` INT NOT NULL," +
                "`us_max_match` INT NOT NULL," +
                "`us_total` INT NOT NULL," +
                "`us_min_used` INT NOT NULL," +
                "`us_max_used` INT NOT NULL," +
                "`tm_success` INT NOT NULL," +
                "`tm_failed` INT NOT NULL," +
                "`tm_found` INT NOT NULL," +
                "`tm_not_found` INT NOT NULL," +
                "`tm_multi_found` INT NOT NULL," +
                "`dt_start` DATETIME NOT NULL," +
                "`dt_end` DATETIME NOT NULL," +
                "PRIMARY KEY (`id`))";
        try {
            SqlUtil.sqlExecute(sqlCreate);
        } catch (IotequException e) {
        }
    }
    @RequestMapping(value = "/env")
    public Map<String,Object> env() throws IotequException {
        return svasService.getEnvProperties();
    }
    @RequestMapping(value = "/auth")
    public Map<String,Object> authTest(String template, HttpServletRequest request, HttpServletResponse response) throws IotequException {
        if (template==null) template = "4BE47DEE48CD62703B41E3C57CCD5590E53F4736CF13686C76955DAE06FA813197ACD7D1C967861EFD980B3C79463F8F9D0C5CF036FD184D48D32D0523BF298ABDF6B5421DE13627FC74B0EADE5DD1E816F22342BA4C2BC388A9597F392071AD5C7704EC06FB23D749BF55CC07470E680AEBB21AEE28C42970120349BC75F14C090DE5FD479F5659CB1B856329085ECA65B1AEFD3FAFD586D878C19D4E5EAF64E5113A827861BA2346A417C2BD6ED3FD6EEC9D48F114E94D8FC1EAC0A6A4F7D4F799D3F37BC615EBE260E68F321A024A91D62BF30A10CC206606EE8AF9104DBE73842C1A2DB9614ACF7E3B7FA1723513F873031C7A05F076D1DB112B7A7083A83E3C9FD94193ABFC90F1B9868384F08B6C1068A3831E3806BC7C52BABE824380";
        SvasTypes.SvasMatched matched = svasService.auth(template, 0);
        return EntityUtil.mapFromEntity(matched);
    }
    @RequestMapping(value = "/reset")
    public Map<String,Object> testReset() throws ParseException {
            testClient = 0;
            testStartTime = null;
            Map<String, Object> map = new HashMap<>();
            map.put("success", true);
            return map;
    }
    @RequestMapping(value = "/client")
    public Map<String,Object> status() {
        Map<String,Object> map=new HashMap<>();
        map.put("success",true);
        map.put("client",testClient);
        map.put("startAt", DateUtil.date2String(testStartTime,null));
        return map;
    }
    @RequestMapping(value = "/start")
    public Map<String,Object> testStart() throws ParseException {
            testClient++;
            if (testClient == 1) initDb();
            long tm = 0;
            if (testStartTime != null) {
                tm = testStartTime.getTime() - new Date().getTime();
            } else {
                testStartTime = new Date();
                try {
                    tm = 60000 * Integer.parseInt(environment.getProperty("svas.matchTestSyncMinutes"));
                } catch (Exception e) {
                    tm = 0;
                }
                testStartTime.setTime((testStartTime.getTime()+59999)/60000*60000 + tm);
            }
            tm = testStartTime.getTime() - new Date().getTime();
            if (tm < 0) tm = 0;
            Map<String, Object> map = new HashMap<>();
            map.put("sleep", tm);
            return map;
    }
    @RequestMapping(value = "/end")
    public Map<String,Object> testEnd(Integer usMatch,Integer usMinMatch, Integer usMaxMatch,
                                      Integer usUsed,Integer usMinUsed,Integer usMaxUsed,
                                      Integer tmSuccess,Integer tmException,
                                      Integer tmFound,Integer tmNotFound,Integer tmMultiFound) throws Exception {
            String sql = "insert into dev_svas_test(us_match,us_min_match,us_max_match,us_total,us_min_used,us_max_used,tm_success,tm_failed,tm_found,tm_not_found,tm_multi_found,dt_start,dt_end) values(?,?,?,?,?,?,?,?,?,?,?,?,?)";
            int rows = SqlUtil.sqlExecute(sql, usMatch, usMinMatch, usMaxMatch,
                    usUsed, usMinUsed, usMaxUsed,
                    tmSuccess, tmException, tmFound, tmNotFound, tmMultiFound,
                    testStartTime, new Date());
            testClient--;
            if (testClient <= 0) {
                testStartTime = null;
                testClient = 0;
            }
            Map<String, Object> map = new HashMap<>();
            map.put("rows", rows);
            return map;
    }
}
