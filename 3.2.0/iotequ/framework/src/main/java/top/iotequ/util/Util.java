package top.iotequ.util;

import ch.qos.logback.classic.Level;
import com.google.gson.*;
import com.google.gson.reflect.TypeToken;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.env.PropertiesPropertySourceLoader;
import org.springframework.boot.env.YamlPropertySourceLoader;
import org.springframework.boot.system.ApplicationHome;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationEvent;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import top.iotequ.framework.controller.RandCodeImageService;
import top.iotequ.framework.dao.DataDictDao;
import top.iotequ.framework.dao.MessageDao;
import top.iotequ.framework.dao.SysLogDao;
import top.iotequ.framework.exception.IotequException;
import top.iotequ.framework.exception.IotequThrowable;
import top.iotequ.framework.pojo.*;
import top.iotequ.framework.service.ISmsService;
import top.iotequ.framework.context.SpringContext;
import top.iotequ.framework.serializer.gson.*;
import org.apache.commons.codec.binary.Base64;
import javax.servlet.http.*;

import lombok.NonNull;
import java.io.*;
import java.lang.management.ManagementFactory;
import java.lang.management.RuntimeMXBean;
import java.sql.Time;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 通用工具类，提供一些常用的静态函数方法
 */

public class Util extends Utils {
    private static final Logger logger = LoggerFactory.getLogger(Util.class);
    public  static final String ADDITIONAL_CONDITION = "ADDITIONAL_CONDITION";
    public  static final String ORG_FILTER_CONDITION = "ORG_FILTER_CONDITION";
    private static final String SEND_VERIFY_CODE_TIME = "SVCT_";
    private static final String SENT_VERIFY_CODE = "VC_";
    public static boolean runInIdeMode = false;

    static public String getPath(Class<?> clazz) {
        ApplicationHome home = new ApplicationHome(clazz == null ? File.class : clazz);
        File file = home.getDir();
        if (file != null) return file.getPath();
        else return null;
    }

    static public String getSource(Class<?> clazz) {
        ApplicationHome home = new ApplicationHome(clazz == null ? File.class : clazz);
        File file = home.getSource();
        if (file != null) return file.getPath();
        else return null;
    }

    /**
     * 检验会话的验证码是否正确
     * @param randCode 验证码
     * @return 正确返回true
     */
    public static boolean checkRandCode(String randCode) {
        if (Util.isEmpty(randCode)) return false;
        String rc = RandCodeImageService.getSessionRandCode();
        if (Util.isEmpty(rc)) return false;
        return randCode.toLowerCase().equals(rc.toLowerCase());
    }
    /**
     * 获得contextPath
     *
     * @return contextPath
     */
    static public String getContextPath() {
        HttpServletRequest request = getRequest();
        return request.getContextPath();
    }

    /**
     * 获得完整url地址
     *
     * @param url url
     * @return 完整地址
     */
    static public String realUrl(String url) {
        if (url == null) return null;
        if (url.toLowerCase().startsWith("http")) return url;
        String contextPath = getContextPath();

        if (isEmpty(contextPath) || contextPath.equals("/"))
            return url;
        if (contextPath.endsWith("/") && url.startsWith("/"))
            return contextPath + url.substring(1);
        else
            return contextPath + url;
    }

    /**
     * 判断资源文件是否存在
     *
     * @param resourceFile 资源文件名，全名，包括static,template等目录前缀
     * @return 是否存在
     */
    static public boolean exists(String resourceFile) {
        Resource resource = new ClassPathResource(resourceFile);
        try {
            InputStream is = resource.getInputStream();
            is.close();
            return true;
        } catch (IOException e) {
            return false;
        }
    }

    /**
     * 获得当前对话的 HttpServletRequest
     *
     * @return 获得当前对话的 HttpServletRequest
     */
    public static HttpServletRequest getRequest() {
        if (RequestContextHolder.getRequestAttributes() != null) {
            HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes())
                    .getRequest();
            return request;
        } else return null;
    }

    public static HttpServletResponse getResponse() {
        if (RequestContextHolder.getRequestAttributes() != null) {
            HttpServletResponse response = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes())
                    .getResponse();
            return response;
        } else return null;
    }
    /**
     * 获取当前对话
     *
     * @return 获取当前对话
     */
    public static HttpSession getSession() {
        if (getRequest()==null) return null;
        HttpSession session = getRequest().getSession();
        return session;
    }

    /**
     * 返回当前对话对应键值的属性值
     *
     * @param key 键值
     * @return 返回当前对话对应键值的属性值
     */
    public static Object getSessionAttribute(String key) {
        HttpSession session = getSession();
        if (session != null) return session.getAttribute(key);
        else return null;
    }

    /**
     * 设置当前对话的键值属性
     *
     * @param key  键值
     * @param attr 设置当前对话的键值属性
     */
    public static void setSessionAttribute(String key, Object attr) {
        HttpSession session = getSession();
        if (session != null) session.setAttribute(key, attr);
    }

    /**
     * 删除当前对话的键值属性
     *
     * @param key 键值
     */
    public static void removeSessionAttribute(String key) {
        HttpSession session = getSession();
        if (session != null) session.removeAttribute(key);
    }

    /**
     * 获得会话处理进度
     *
     * @return 当前进度, 0-100
     */
    public static Integer getProgress() {
        Object o = getSessionAttribute("progress");
        if (o != null) {
            try {
                int i = Integer.parseInt(o.toString());
                if (i >= 0 && i <= 100) return i;
            } catch (Exception e) {
                return 100;
            }
        }
        return 100;
    }

    /**
     * 设置会话的处理进度
     *
     * @param p 进度，0-100
     */
    public static void setProgress(Integer p) {
        if (p != null && p >= 0 && p <= 100) setSessionAttribute("progress", p);
        else setSessionAttribute("progress", null);
    }

    /**
     * 当前用户
     *
     * @return 返回当前对话的用户
     */
    public static User getUser() {
        SecurityContext sc = SecurityContextHolder.getContext();
        if (sc != null) {
            Authentication au = sc.getAuthentication();
            if (au != null) {
                Object obj = au.getPrincipal();
                if (obj != null && obj instanceof User) {
                    return (User) obj;
                }
            }
        }
        return null;
    }
    public static String getLanguage() {
        return StringUtil.toJsonString(getSessionAttribute("iotequ_language"));
    }
    public static void setLanguage(String locale) {
        setSessionAttribute("iotequ_language",locale);
    }
    public static boolean hasRoleAdmin() {
        User user = getUser();
        if (user==null || "guest".equals(user.getName())) return false;
        else if ("admin".equals(user.getName())) return true;
        else {
            String roles = user.getRoleList();
            if (isEmpty(roles)) return false;
            return SqlUtil.sqlExist(false,"select * from sys_role where code='admin' and id in ("+roles+")");
        }
    }
    /**
     * 认证状态
     *
     * @return 当前对话是否已经认证
     */
    public static boolean isAuthenticated() {
        SecurityContext sc = SecurityContextHolder.getContext();
        if (sc != null) {
            Authentication au = sc.getAuthentication();
            if (au != null) {
                return au.isAuthenticated() && au.getPrincipal() instanceof User;
            }
        }
        return false;
    }

    /**
     * 获取ip地址
     *
     * @param request request
     * @return 从 request获得客户ip地址
     */
    public static String getIpAddr(HttpServletRequest request) {
        if (request == null)
            request = getRequest();
        if (request == null)
            return null;
        String ip = request.getHeader("x-forwarded-for");
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("Proxy-Client-IP");
        }
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("WL-Proxy-Client-IP");
        }
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getRemoteAddr();
        }
        if (ip.equals("0:0:0:0:0:0:0:1")) {
            ip = "localhost";
        }
        return ip;
    }

    public static Gson getGson() {
        Gson gson = new GsonBuilder()
                .setLenient()
                .registerTypeAdapter(Boolean.class, new GsonBooleanTypeAdapter())
                .registerTypeAdapter(Date.class, new GsonDatetimeTypeAdapter())
                .registerTypeAdapter(Time.class, new GsonSqlTimeTypeAdapter())
                .registerTypeAdapter(java.sql.Date.class,new GsonSqlDateTypeAdapter())
                .setDateFormat("yyyy-MM-dd HH:mm:ss")
                .registerTypeAdapter(new TypeToken<Map<String, Object>>() {}.getType(), new MapTypeAdapter())
                .create();
        return gson;
    }

    static public Map<String,Object> mapFromJson(String json) {
        return getGson().fromJson(json,new TypeToken<Map<String, Object>>() {}.getType());
    }
    /**
     * 获得cookie值
     *
     * @param request request
     * @param name    名称
     * @return cookie值
     */
    static public String getCookie(HttpServletRequest request, String name) {
        if (request == null || name == null)
            return null;
        Cookie[] cookies = request.getCookies();
        if (cookies != null) {
            for (Cookie c : cookies) {
                if (EntityUtil.entityEquals(c.getName(), name))
                    return c.getValue();
            }
        }
        return null;
    }

    /**
     * 设置cookie值
     *
     * @param request  request
     * @param response response
     * @param name     名称
     * @param value    值
     * @param age      时效
     */
    static public void setCookie(HttpServletRequest request, HttpServletResponse response, String name, String value, int age) {
        if (response == null || name == null)
            return;
        Cookie cookie = new Cookie(name, value);
        cookie.setMaxAge(age);
        String contextPath = request.getContextPath();
        cookie.setPath(contextPath.length() > 0 ? contextPath : "/");
        cookie.setSecure(request.isSecure());
        response.addCookie(cookie);
    }

    /**
     * 获得指定类bean的对象
     *
     * @param clazz 类
     * @param <T>   泛型类型
     * @return bean对象
     */
    public static <T> T getBean(Class<T> clazz) {
        return SpringContext.getBean(clazz);
    }

    public static <T> List<T> getImplementedBean(Class<T> clazz) {
        List<T> list = new ArrayList<T>();
        ApplicationContext context = getApplicationContext();
        Map<String, T> result = context.getBeansOfType(clazz);
        int p = clazz.getName().lastIndexOf(".");
        String myName = StringUtil.firstLetterLower(p >= 0 ? clazz.getName().substring(p + 1) : clazz.getName());
        for (String k : result.keySet()) {
            if (k.equals(myName)) continue;
            T t = result.get(k);
            list.add(t);
        }
        return list;
    }

    /**
     * 通过bean名称获得bean
     *
     * @param beanName bean名
     * @return bean对象
     */
    public static Object getBean(String beanName) {
        return SpringContext.getBean(beanName);
    }

    public static ApplicationContext getApplicationContext() {
        return SpringContext.getApplicationContext();
    }

    public static Object getIotequModuleProperty(String groupId, String artifactId, String property) {
        try {
            String beanName = groupId + "." + artifactId + ".IotequModule";
            Object bean = getBean(beanName);
            if (bean != null) {
                return ObjectUtil.getPrivateField(bean, property);
            } else return null;
        } catch(Exception e) {
            return null;
        }
    }

    public static void publishEvent(ApplicationEvent event) {
        if (event!=null) getApplicationContext().publishEvent(event);
    }
    /**
     * 当前用户发消息
     *
     * @param receiver 接受者
     * @param title    标题
     * @param content  内容
     * @param url      参考url地址
     * @param eventId  关联对象id
     * @return 消息ID编号，null表示未成功
     */
    public static Integer sendMessage(String receiver, String title, String content, String url, String eventId) {
        return sendMessage(receiver, getUser(), title, content, url, eventId);
    }

    /**
     * 某用户发消息
     *
     * @param receiver 接受者
     * @param sender   发送者，为空世表示系统消息
     * @param title    标题
     * @param content  内容
     * @param url      参考url地址
     * @param eventId  关联对象id
     * @return 消息ID编号，null 表示未成功
     */
    public static Integer sendMessage(String receiver, User sender, String title, String content, String url, String eventId) {
        if (isEmpty(title) || isEmpty(content))
            return 0;
        Message msg = new Message();
        if (sender != null) {
            msg.setSenderName(sender.getRealName());
            if (sender.getName().equals("guest") && Objects.nonNull(sender.getWechatOpenid()))
                msg.setSender(sender.getWechatOpenid());
            else
                msg.setSender(sender.getId());
            /*
            if (!isEmpty(eventId) && !isEmpty(receiver)) { // 删除未读的重复信息
                try {
                    SqlUtil.sqlExecute("delete FROM sys_message where read_time is null and receiver=? and event_id=? and sender=?",
                            receiver, eventId, sender.getId());
                } catch (Exception e) {
                }
            }
            */
        } else {
            msg.setSenderName("system");
            /*
            if (!isEmpty(eventId) && !isEmpty(receiver)) { // 删除未读的重复信息
                try {
                    SqlUtil.sqlExecute(
                            "delete FROM sys_message where read_time is null and receiver=? and event_id=? and sender is null",
                            receiver, eventId);
                } catch (Exception e) {
                }
            }
             */
        }
        msg.setCreateTime(new Date());
        msg.setTitle(title);
        msg.setContent(content);
        msg.setEventId(eventId);
        if (!isEmpty(url) && !url.trim().equals("/"))
            msg.setUrl(url.trim());
        if (!isEmpty(receiver)) {
            msg.setReceiver(receiver);
            Object nm = null;
            try {
                nm = SqlUtil.sqlQueryField("select real_name from sys_user where id=?", receiver);
            } catch (Exception e) {
            }
            if (nm != null)
                msg.setReceiverName(nm.toString());
        }
        MessageDao messageDao = SpringContext.getBean(MessageDao.class);
        messageDao.insert(msg);
        return msg.getId();
    }



    /**
     * 获得系统数据字典
     *
     * @param dict 字典名
     * @return 一个Map，键值为 value，text
     */
    public static List<Map<String, Object>> getDataDict(String dict) {
        return SpringContext.systemDataDict.get(dict);
    }

    /**
     * 设置系统数据字典
     *
     * @param dict   字典名
     * @param values 值序列
     * @param names  显示名称序列
     */
    public static void setDataDict(String dict, Object[] values, String[] names) {
        List<Map<String, Object>> old = getDataDict(dict);
        if (old == null) old = new ArrayList<Map<String, Object>>();
        for (int i = 0; i < values.length; i++) {
            Map<String, Object> map = null;
            for (Map<String, Object> m : old) {
                if (EntityUtil.entityEquals(m.get("value"), values[i])) {
                    map = m;
                    break;
                }
            }
            if (map == null) {
                map = new HashMap<String, Object>();
                map.put("value", values[i].toString());
                map.put("text", i < names.length ? names[i].toString() : "???");
                old.add(map);
            } else {
                map.put("text", i < names.length ? names[i].toString() : map.get("text"));
            }
        }
        DataDictDao dictDao = getBean(DataDictDao.class);
        List<DataDict> list = dictDao.listBy("dict='" + dict + "'", "order_num");
        for (int i = 0; i < old.size(); i++) {
            Map<String, Object> m = old.get(i);
            int mode = 1; // 0: skip, 1: insert 2: update
            if (list != null) {
                for (DataDict d : list) {
                    if (m.get("value").toString().equals(d.getCode())) {
                        if (m.get("text").toString().equals(d.getText()))
                            mode = 0;
                        else {
                            d.setText(m.get("text").toString());
                            dictDao.update(d);
                        }
                        ;
                        break;
                    }
                }
            }
            if (mode == 1) {
                DataDict d = new DataDict();
                d.setCode(m.get("value").toString());
                d.setDict(dict);
                d.setOrderNum(i + 1);
                d.setText(m.get("text").toString());
                dictDao.insert(d);
            }
        }
    }

    /**
     * 获得数据字典显示值
     *
     * @param dict  字典
     * @param value 值
     * @return 显示值
     */

    public static String getDataDictText(String dict, Object value) {
        List<Map<String, Object>> mm = getDataDict(dict);
        if (mm != null && mm.size() > 0) {
            for (Map<String, Object> m : mm) {
                if (EntityUtil.entityEquals(m.get("value"), value)) return StringUtil.toString(m.get("text"));
            }
        }
        return null;
    }

    /**
     * 写日志
     *
     * @param logger  logger
     * @param keyword 关键字
     * @param user    产生日志的用户
     * @param format  格式
     * @param args    参数
     */
    public static void writeLog(Logger logger, String keyword, User user, String format, Object... args) {
        String note = String.format(format, args);
        if (note.length() > 400) note = note.substring(0, 400) + "...";
        if (logger != null) logger.debug(note);
        SysLog log = new SysLog();
        log.setKeyword(keyword);
        log.setNote(note);
        log.setTime(new Date());
        if (user == null) user = getUser();
        if (user != null) {
            log.setUserInfo(user.getRealName());
            log.setUserType(user.getName());
        }
        getBean(SysLogDao.class).insert(log);
    }

    /**
     * 获得试用期剩余时间
     *
     * @param trialDays 试用天数
     * @return 剩余天数
     */
    static public int getTrialDaysLeft(@NonNull String module, int trialDays) {
        Date dt = getVersionBuildTime(module);
        int ds = (int) ((new Date().getTime() - dt.getTime()) / 1000 / 3600 / 24);
        if (ds >= trialDays) return 0;
        else return trialDays - ds;
    }

    /**
     * 获得特定序列号的licence
     *
     * @param sn 序列号
     * @return licence
     */
    static public int getLicence(String sn) {
        try {
            return MachineInfo.getLicence(sn);
        } catch (Exception e) {
            return 0;
        }
    }

    /**
     * 获得特定模块序列号的licence
     *
     * @param sn     序列号
     * @param module 模块名称
     * @return licence
     */
    static public int getLicence(String sn, String module) {
        try {
            return MachineInfo.getLicence(sn, module);
        } catch (Exception e) {
            return 0;
        }
    }

    public static String get2DCode(Object content, int width, int height, String logoFile) {
        if (content == null) return null;
        File logoPic = isEmpty(logoFile) ? null : new File(logoFile);
        if (logoPic != null && !logoPic.exists()) {
            Resource resource = new ClassPathResource(logoFile);
            if (resource != null) {
                try {
                    logoPic = resource.getFile();
                } catch (IOException e) {
                    logoPic = null;
                }
            } else logoPic = null;
            if (!logoPic.exists()) logoPic = null;
        }
        try {
            return ZxingCode.getQRCode(content.toString(), width, height, logoPic);
        } catch (Exception e) {
            return null;
        }
    }

    private static String additionalPropertyFile(@NonNull String path, @NonNull String fileName) {
        PropertiesPropertySourceLoader propLoader = new PropertiesPropertySourceLoader();
        YamlPropertySourceLoader yamlLoader = new YamlPropertySourceLoader();
        String ext = null;
        File file = null;
        int dp = fileName.lastIndexOf(".");
        if (dp > 0) {
            ext = fileName.substring(dp + 1).toLowerCase();
            file = new File(path, fileName);
            if (file != null && file.exists()) {
                if (Arrays.asList(yamlLoader.getFileExtensions()).contains(ext)) {
                    return file.getAbsolutePath();
                } else {
                    if (Arrays.asList(propLoader.getFileExtensions()).contains(ext)) {
                        return file.getAbsolutePath();
                    }
                }
            }
        } else {
            for (String e : propLoader.getFileExtensions()) {
                file = new File(path, fileName + "." + e);
                if (file.exists()) {
                    return file.getAbsolutePath();
                }
            }
            if (file == null || !file.exists()) {
                for (String e : yamlLoader.getFileExtensions()) {
                    file = new File(path, fileName + "." + e);
                    if (file.exists()) {
                        return file.getAbsolutePath();
                    }
                }
            }
        }
        return null;
    }

    private static String additionalPropertyFile(@NonNull String fileName) {
        String path = SpringContext.getProjectHomePath();
        if (isEmpty(path)) return null;
        String fullName = additionalPropertyFile(path, fileName);
        return fullName;
    }

    public static void sendVerifyCodeToMobile(@NonNull String mobilePhone) throws IotequException {
        // 调用验证码发送外部接口
        Object o = getSessionAttribute(SEND_VERIFY_CODE_TIME + mobilePhone);
        if (o != null) {
            Long dt = (Long) o;
            long now = new Date().getTime() / 1000;
            if (now - dt < 120) {   // 1分钟之内不重发
                throw new IotequException(IotequThrowable.SMS_TOO_FREQUENTLY, String.valueOf(now - dt));
            }
        }
        //生成随机验证码
        final StringBuffer sb = new StringBuffer();
        final Random random = new Random();
        final String sourseStr = "0123456789";
        for (int i = 0; i < 6; i++) {
            sb.append(sourseStr.charAt(random.nextInt(sourseStr.length())));
        }
        String vc = sb.toString();
        //调用短信发送接口发送短信
        List<ISmsService> smsServiceList = getImplementedBean(ISmsService.class);
        if (smsServiceList != null) {
            for (ISmsService sms : smsServiceList) {
                if (sms.enabled()) {  // 打开短信登录
                    sms.sendVerifyCodeSms(mobilePhone, vc);
                    break;
                }
            }
        } else {
            throw new IotequException(IotequThrowable.SMS_SERVICE_MISS, "没有短信服务");
        }
        //记录验证码和发送时间，手机号码
        setSessionAttribute(SENT_VERIFY_CODE + mobilePhone, vc);
        Long now = new Date().getTime() / 1000;
        setSessionAttribute(SEND_VERIFY_CODE_TIME + mobilePhone, now);
    }

    public static String getSmsRandCode(String mobilePhone) {
        return StringUtil.toString(getSessionAttribute(SENT_VERIFY_CODE + mobilePhone));
    }
    public static void sendTemplateSmsToMobile(String mobilePhone, String templateName, Map<String, Object> map) throws IotequException {
        //调用短信发送接口发送短信
        List<ISmsService> smsServiceList = getImplementedBean(ISmsService.class);
        if (smsServiceList != null) {
            String pattern = "^1([358][0-9]|4[579]|66|7[0135678]|9[89])[0-9]{8}$";
            if (mobilePhone == null || !Pattern.matches(pattern, mobilePhone))
                throw new IotequException(IotequThrowable.IO_FORMATTER_ERROR, "错误的手机号码");
            Object prevSendTime = getSessionAttribute(isEmpty(templateName, "DEFAULT_SMS_TEMPLATE") + "_" + mobilePhone);
            if (prevSendTime != null) {
                long timepassed = new Date().getTime() - (Long) prevSendTime;
                if (timepassed < 60000) throw new IotequException(IotequThrowable.SMS_TOO_FREQUENTLY, "1分钟内禁止频繁发送");
            }
            for (ISmsService sms : smsServiceList) {
                if (sms.enabled()) {  // 打开短信登录
                    sms.sendTemplateSms(mobilePhone, templateName, map);
                    setSessionAttribute(isEmpty(templateName, "DEFAULT_SMS_TEMPLATE") + "_" + mobilePhone, new Date().getTime());
                    break;
                }
            }
        } else {
            throw new IotequException(IotequThrowable.SMS_SERVICE_MISS, "没有短信服务");
        }
    }

    public static void mobileVerifyCodeCheck(@NonNull String mobilePhone, @NonNull String vc) throws IotequException {
        Object o = getSessionAttribute(SEND_VERIFY_CODE_TIME + mobilePhone);
        if (o != null) {
            Object v = getSessionAttribute(SENT_VERIFY_CODE + mobilePhone);
            if (v == null) throw new IotequException(IotequException.INVALID_VERIFICATION_CODE,"null");
            Long dt = (Long) o;
            long now = new Date().getTime() / 1000;
            if (now - dt > 300) {   // 5分钟之后已经失效
                throw new IotequException(IotequException.INVALID_VERIFICATION_CODE,"invalid");
            }
            if (Util.equals(vc, v.toString())) return;
            else throw new IotequException(IotequException.INVALID_VERIFICATION_CODE,"incorrect");
        } else {
            throw new IotequException(IotequException.INVALID_VERIFICATION_CODE,mobilePhone);
        }
    }

    /**
     * 获得版本信息
     *
     * @return 版本信息
     */

    static public String getVersion(@NonNull String module) {
        IotequVersionInfo ver = IotequVersionInfo.getVersion(module);
        if (ver != null) {
            return ver.getVersion();
        }
        return null;
    }
    /**
     * 获得版本时间
     *
     * @return 版本build时间
     */
    static public Date getVersionBuildTime(@NonNull String module) {
        IotequVersionInfo ver = IotequVersionInfo.getVersion(module);
        if (ver != null) {
            Date dt = ver.getBuildTime();
            if (Objects.nonNull(dt)) return dt;
        }
        return new Date();
    }

    public static int getProcessID(Logger log) {
        RuntimeMXBean runtimeMXBean = ManagementFactory.getRuntimeMXBean();
        log.info(runtimeMXBean.getName());
        return Integer.valueOf(runtimeMXBean.getName().split("@")[0])
                .intValue();
    }

    public static void commonApplicationRun(@NonNull Class<?> clazz, String applicationProperties, String customerProperties, String[] args) {
        Logger log = LoggerFactory.getLogger(Util.class);
        setLevel(log,"all");
        runInIdeMode = ("file".equals(clazz.getResource("").getProtocol()));
        SpringContext.setProjectHomePath(FileIOUtil.getCurrentPath());
        int pid = getProcessID(log);
        try {
            FileUtil.writeToFile(String.valueOf(pid), new File(SpringContext.getProjectHomePath(),"pid.log"));
        } catch (Exception e) {}
        SpringContext.buildTime = IotequVersionInfo.getBuildTime();
        SpringApplicationBuilder appBuilder = new SpringApplicationBuilder(clazz);
        appBuilder.properties("file.encoding=UTF-8");
        String myPropertyFile = additionalPropertyFile(customerProperties == null ? "iotequ" : customerProperties);
        if (myPropertyFile != null) SpringContext.setPropertyFile(myPropertyFile);
        if (applicationProperties != null) {
            String location = "spring.config.location=classpath:/" + applicationProperties +
                    (myPropertyFile == null ? "" : "," + myPropertyFile);
            appBuilder.properties(location);
        } else {
            if (myPropertyFile != null) {
                appBuilder.properties("spring.config.additional-location=" + myPropertyFile);
            }
        }
        appBuilder.run(args);
    }
    public static void sendRedisMessage(String channel,Object message) {
        Object  redisMessageReceiver = getBean("redisMessageReceiver");
        if (redisMessageReceiver!=null) {
            StringRedisTemplate template = getBean(StringRedisTemplate.class);
            if (template != null) {
                String strMsg = getGson().toJson(message);
                logger.debug("Send redis {} : {}", channel, message);
                template.convertAndSend(channel, strMsg);
            }
        }
    }

    public static void setLevel(Logger log, String level) {
        if (log!=null && level!=null && log instanceof ch.qos.logback.classic.Logger) {
            ((ch.qos.logback.classic.Logger)log).setLevel(Level.toLevel(level));
        }
    }
    public static String getLevel(Logger log) {
        if (log!=null && log instanceof ch.qos.logback.classic.Logger) {
            Level level = ((ch.qos.logback.classic.Logger)log).getLevel();
            if (level!=null) return level.levelStr;
        }
        return null;
    }
    public static String getImageBase64String(File file) throws IotequException {
        if (file==null) throw new IotequException(IotequThrowable.NULL_OBJECT,"file is null");
        String fileContentBase64 = null;
        String fileType = "image/";
        if (file.getName().lastIndexOf('.')>=0)  fileType+=file.getName().substring(file.getName().lastIndexOf('.')+1).toLowerCase();
        else throw new IotequException(IotequThrowable.FAILURE,"file type unknown");
        String base64Str = "data:" + fileType + ";base64,";
        String content = null;
        InputStream in = null;
        try {
            in = new FileInputStream(file);
            byte [] data = new byte[in.available()];
            in.read(data);
            in.close();
            if (data == null || data.length == 0) {
                return null;
            }
            content = Base64.encodeBase64String(data);
            if (content == null || "".equals(content)) {
                return null;
            }
            fileContentBase64 = base64Str + content;
        } catch (Exception e) {
            throw IotequException.newInstance(e);
        } finally {
            if (in != null) {
                try {
                    in.close();
                } catch (IOException e) {}
            }
        }
        return fileContentBase64;
    }

    public static String saveBase64Image(String nameWhithoutExt, String imgBase64) {
        if (isEmpty(nameWhithoutExt) || isEmpty(imgBase64)) return null;
        String ext = StringUtil.regGroup("image\\/([a-z]+);",imgBase64,1);
        String data = StringUtil.regGroup(";base64,(.*)$",imgBase64,1);
        if (isEmpty(ext) || isEmpty(data)) return null;
        String name = nameWhithoutExt + "." + ext;
        File outf = new File(name);
        if (!outf.getParentFile().exists()) outf.getParentFile().mkdirs();
        else if (outf.exists()) outf.delete();
        byte bin[] = Base64.decodeBase64(data);
        try {
            OutputStream out = new FileOutputStream(outf);
            out.write(bin);
            out.flush();
            out.close();
        } catch (Exception e) { return null; }
        return name;
    }
    static public String getParameter(String key, HttpServletRequest request) throws Exception{
        if (request == null || key==null)	return null;
        String contentType = request.getHeader("content-type");
        String method = request.getMethod();
        if (!Util.isEmpty(contentType) && !Util.isEmpty(method)
                && method.toLowerCase().equals("post") 	&& contentType.toLowerCase().contains("json")) {  // json post 模式
            return HttpUtils.getRequestBodyParameter(key,request);
        } else return request.getParameter(key);
    }
    static public boolean isMobile(HttpServletRequest request) {
        String userAgent = request.getHeader( "USER-AGENT" );
        if(isEmpty(userAgent)) return false;
        final String phoneReg = "\\b(ip(hone|od)|android|opera m(ob|in)i"
                +"|windows (phone|ce)|blackberry"
                +"|s(ymbian|eries60|amsung)|p(laybook|alm|rofile/midp"
                +"|laystation portable)|nokia|fennec|htc[-_]"
                +"|mobile|up.browser|[1-4][0-9]{2}x[1-4][0-9]{2})\\b";
        Pattern phonePat = Pattern.compile(phoneReg, Pattern.CASE_INSENSITIVE);
        Matcher matcherPhone = phonePat.matcher(userAgent);
        return matcherPhone.find();
    }
}
