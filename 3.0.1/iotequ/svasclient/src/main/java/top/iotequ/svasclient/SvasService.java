package top.iotequ.svasclient;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.ApplicationListener;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;
import top.iotequ.framework.event.PeopleInfoChangedEvent;
import top.iotequ.framework.exception.IotequException;
import top.iotequ.framework.exception.IotequThrowable;
import top.iotequ.util.EntityUtil;
import top.iotequ.util.IotequVersionInfo;
import top.iotequ.util.Util;
import top.iotequ.svasclient.SvasTypes.*;
import top.iotequ.svasclient.db.dao.SvasUserNoDao;
import top.iotequ.svasclient.db.pojo.SvasUserNo;

import java.io.File;
import java.util.*;

/**
 * 这是调用svas服务的bean入口<br>
 * 通过autowired注入使用<br>
 * 函数使用方法与{@link top.iotequ.svasclient.SvasClient}完全一致<br>
 * 配置参数<br>
 * svas.url  可选，指定svas地址以及端口.如果项目集成了svas服务模块，该参数无效<br>
 * svas.user-no-prefix 可选，指定默认用户号前缀，数值串请使用""<br>
 * 如果不指定，请在调用getUserNo是指定或使用svas服务器的默认值
 *
 * @author Qinyoyo
 * @version 1.0
 */
@Service

public class SvasService implements ApplicationRunner, ApplicationContextAware, ApplicationListener<PeopleInfoChangedEvent> {
    private static final Logger log = LoggerFactory.getLogger(SvasService.class);
    @Autowired
    private ApplicationContext appContext = null;
    SvasClient svasClient = null;
    @Autowired
    private Environment env;
    @Autowired
    SvasUserNoDao svasUserNoDao;

    private int licence = 0, daysLeft = 0;
    private String version = "3.0.0";


    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        appContext = applicationContext;
    }

    @Override
    public void run(ApplicationArguments args) throws IotequException {
        Object svasServer = null;
        String svasUrl = env.getProperty("svas.url");
        String prefix = env.getProperty("svas.user-no-prefix");
        if (prefix == null) prefix = "";
        try {
            svasServer = appContext.getBean("svasServer");
        } catch (Exception e) {
        }
        svasClient = new SvasClient(svasServer, svasUrl, prefix, env.getProperty("svas.client_id"), env.getProperty("svas.client_secret"), env.getProperty("svas.scope"));
        svasClient.setSvasUserNoDao(svasUserNoDao);
        version = getVersion();
        if (version.compareTo("3.0") < 0)
            throw new IotequException(IotequThrowable.VERSION_NOT_MATCHED, "svas版本太低，请使用3.0以上版本");
        licence = getLicence();
        daysLeft = getTrialDays();
        if (licence > 0 && daysLeft > 3650) {
            log.info(String.format("---------- Svas version %s, licence = %d", version, licence));
            IotequVersionInfo.licencesInfo.put("svas","licence = "+String.valueOf(licence));
        } else {
            if (daysLeft > 0) {
                log.info(String.format("---------- Svas trial version %s, licence = %d, %d days left", version, licence, daysLeft));
                Timer timer = new Timer();
                Calendar ca = Calendar.getInstance();
                ca.add(Calendar.DATE, daysLeft);
                timer.schedule(new TimerTask() {
                    @Override
                    public void run() {
                        daysLeft = 0;
                    }
                }, ca.getTime());
                IotequVersionInfo.licencesInfo.put("svas",daysLeft+" days left , licence = "+licence);
            } else {
                log.info(String.format("---------- Svas trial version expired"));
                IotequVersionInfo.licencesInfo.put("svas", "expired");
            }
        }
        svasClient.setLicence(licence);
    }

    /**
     * 获得svas版本号
     *
     * @return 版本号
     * @throws IotequException 异常
     */
    public String getVersion() throws IotequException {
        return svasClient.getVersion();
    }

    /**
     * 获得授权的licence数
     *
     * @return licence。licence表示做多可以支持的指静脉模板数量
     * @throws IotequException 异常
     */
    public int getLicence() throws IotequException {
        return svasClient.getLicence();
    }
    /**
     * 获得授权的剩余licence数
     *
     * @return 剩余可用的licence。licence表示做多可以支持的指静脉模板数量
     * @throws IotequException 异常
     */
    public int getLicenceAvailable() throws IotequException {
        return svasClient.getLicenceAvailable();
    }
    /**
     * 获得软件使用时间
     *
     * @return 试用剩余天数。如果为正式版本，返回一个 大于3650的数字
     * @throws IotequException 异常
     */
    public int getTrialDays() throws IotequException {
        return svasClient.getTrialDays();
    }
    /**
     * 获得唯一识别的用户号userNo。如果已经存在，返回存在的用户号。用户号只能为数字串，其他字符非法
     *
     * @param idType 证件类别编号
     * @param idNo   证件号码
     * @param name   姓名
     * @param def    默认值，如果def未被使用，新建时使用此值
     * @param prefix 指定用户号前缀而不是用默认值
     * @return 用户号
     * @throws IotequException svas失败时返回的错误及代码
     */
    public String getUserNo(Integer idType, String idNo, String name, String def, String prefix) throws IotequException {
        if (daysLeft <= 0) throw new IotequException(IotequThrowable.VERSION_EXPIRED, "软件已经过期");
        return svasClient.getUserNo(idType, idNo, name, def, prefix);
    }
    /**
     * 获得用户信息
     *
     * @param userNo 用户号
     * @throws IotequException svas失败时返回错误
     * @return 用户信息
     */
    public SvasUserInfo getUserInfo(String userNo) throws IotequException {
        if (daysLeft <= 0) throw new IotequException(IotequThrowable.VERSION_EXPIRED, "软件已经过期");
        return svasClient.getUserInfo(userNo);
    }

    /**
     * 获得用户的所有信息
     * @param userNo userNo
     * @param includePhoto		是否包含photo字段
     * @return 用户信息
     * @throws IotequException 异常
     */
    public PeopleInfoChangedEvent.People getUserAllInfo(String userNo,Boolean includePhoto) throws IotequException {
        if ("3.0.1".compareTo(version) > 0) throw new IotequException(IotequThrowable.VERSION_NOT_MATCHED, "svas版本太低，请使用3.0.1以上版本");
        if (daysLeft <= 0) throw new IotequException(IotequThrowable.VERSION_EXPIRED, "软件已经过期");
        SvasUserNo user = svasClient.getUserAllInfo(userNo,includePhoto);
        return formSvasUserNo(user);
    }
    /**
     * 从模板读取uid信息
     * @param templates 模板，最多三个词典
     * @return 最多三个userNo
     * @throws IotequException 错误
     */
    public String[] getUserNoFromDict(String templates) throws IotequException {
        if (daysLeft <= 0) throw new IotequException(IotequThrowable.VERSION_EXPIRED, "软件已经过期");
        return svasClient.getUserNoFromDict(templates);
    }
    /**
     * 修改模板的uid信息
     * @param templates 模板
     * @param userNo 新的userno
     * @return 新的模板
     * @throws IotequException 错误
     */
    public String setUserNoForDict(String templates,String userNo) throws IotequException {
        if (daysLeft <= 0) throw new IotequException(IotequThrowable.VERSION_EXPIRED, "软件已经过期");
        return svasClient.setUserNoForDict(templates, userNo);
    }
    private SvasUserNo formPeople(PeopleInfoChangedEvent.People people) {
        if (people==null) return null;
        SvasUserNo u = EntityUtil.entityCopyFrom(SvasUserNo.class,people);
        u.setName(people.getRealName());
        u.setIdNo(people.getIdNumber());
        return u;
    }
    private PeopleInfoChangedEvent.People formSvasUserNo(SvasUserNo u) {
        if (u==null) return null;
        PeopleInfoChangedEvent.People people = EntityUtil.entityCopyFrom(PeopleInfoChangedEvent.People.class,u);
        people.setRealName(u.getName());
        people.setIdNumber(u.getIdNo());
        return people;
    }

    /**
     * 修改user-No对应的证件号码及类别，user-No不变
     *
     * @param userNo 用户号
     * @param idType 新的证件类别，为null时，保持不变
     * @param idNo   新的证件号码，为null时，保持不变
     * @param name   新的姓名，为null时，保持不变
     * @return 用户号
     * @throws IotequException svas失败时返回的错误及代码
     */
    public String changeUserInfo(String userNo, Integer idType, String idNo, String name) throws IotequException {
        if (daysLeft <= 0) throw new IotequException(IotequThrowable.VERSION_EXPIRED, "软件已经过期");
        return svasClient.changeUserInfo(userNo, idType, idNo, name);
    }

    public String changeUserInfo(PeopleInfoChangedEvent.People people) throws IotequException {
        if ("3.0.1".compareTo(version) > 0) throw new IotequException(IotequThrowable.VERSION_NOT_MATCHED, "svas版本太低，请使用3.0.1以上版本");
        if (daysLeft <= 0) throw new IotequException(IotequThrowable.VERSION_EXPIRED, "软件已经过期");
        SvasUserNo user = formPeople(people);
        return svasClient.changeUserInfo(user);
    }
    /**
     * 删除用户号以及用户号对应注册的指静脉信息
     *
     * @param userNo 用户号
     * @throws IotequException svas服务调用失败时返回的错误及代码
     * @return 删除的用户号
     */
    public String removeUserNo(String userNo) throws IotequException {
        if (daysLeft <= 0) throw new IotequException(IotequThrowable.VERSION_EXPIRED, "软件已经过期");
        return svasClient.removeUserNo(userNo);
    }
    /**
     * 删除指定的指静脉信息
     *
     * @param userNo   用户号
     * @param fingerNo 手指编号，0表示全部删除
     * @return 是否成功
     * @throws IotequException svas服务调用失败时返回的错误及代码
     */
    public boolean removeTemplate(String userNo, Integer fingerNo) throws IotequException {
        if (daysLeft <= 0) throw new IotequException(IotequThrowable.VERSION_EXPIRED, "软件已经过期");
        boolean r = svasClient.removeTemplate(userNo, fingerNo);
        if (r) {
            appContext.publishEvent(new TemplatesChangedEvent(this, userNo, "remove", getFingerCount(userNo)));
        }
        return r;
    }
    /**
     * 更新用户的指静脉词典。如果该注册信息不存在，调用addTemplate操作。默认warning=false<br>
     * 该操作会删除已经存在的与本词典匹配的注册指静脉信息
     *
     * @param userNo    用户号
     * @param fingerNo	手指编号
     * @param fingerType	手指
     * @param templates 指静脉词典，可包含1,2,3个辞书。一个注册手指最多三个辞书
     * @return 是否修改成功
     * @throws IotequException svas服务调用失败时返回的错误及代码
     */
    public boolean updateTemplate(String userNo, Integer fingerNo, Integer fingerType, String templates) throws IotequException {
        if (daysLeft <= 0) throw new IotequException(IotequThrowable.VERSION_EXPIRED, "软件已经过期");
        boolean r = svasClient.updateTemplate(userNo, fingerNo, fingerType, templates);
        if (r) {
            appContext.publishEvent(new TemplatesChangedEvent(this, userNo, "update", getFingerCount(userNo)));
        }
        return r;
    }
    /**
     * 增加指静脉<br>
     * 该操作会删除已经存在的与本词典匹配的注册指静脉信息
     *
     * @param userNo     用户号
     * @param fingerNo   手指编号，1或2
     * @param fingerType 手指标识编号
     * @param templates  指静脉词典，可包含1,2,3个辞书。一个注册手指最多三个辞书
     * @param warning    是否胁迫
     * @throws IotequException svas服务调用失败时返回的错误及代码
     * @return 是否注册成功
     */
    public boolean addTemplate(String userNo, Integer fingerNo, Integer fingerType, String templates, Boolean warning) throws IotequException {
        if (daysLeft <= 0) throw new IotequException(IotequThrowable.VERSION_EXPIRED, "软件已经过期");
        boolean r = svasClient.addTemplate(userNo, fingerNo, fingerType, templates, warning);
        if (r) {
            appContext.publishEvent(new TemplatesChangedEvent(this, userNo, "add", getFingerCount(userNo)));
        }
        return r;
    }

    /**
     * 一次设置两个手指模板
     * @param userNo  用户号
     * @param fingerType1 编号1的手指类别
     * @param warning1 编号1的胁迫标识
     * @param templates1 编号1的模板
     * @param fingerType2 编号2的手指类别
     * @param warning2 编号2的胁迫标识
     * @param templates2 编号2的模板
     * @return 是否注册成功
     * @throws IotequException  svas服务调用失败时返回的错误及代码
     */

    public boolean setTemplates(String userNo, Integer fingerType1, Boolean warning1, String templates1, Integer fingerType2, Boolean warning2, String templates2) throws IotequException {
        if (daysLeft <= 0) throw new IotequException(IotequThrowable.VERSION_EXPIRED, "软件已经过期");
        boolean r = svasClient.setTemplates(userNo, fingerType1, warning1, templates1,fingerType2,warning2,templates2);
        if (r) {
            appContext.publishEvent(new TemplatesChangedEvent(this, userNo, "update", getFingerCount(userNo)));
        }
        return r;
    }

    /**
     * 设置照片
     * @param userNo 用户号
     * @param photo 照片的base64图片流，格式为 data:image/xxx;base64,base64串，为空将删除照片
     * @return 是否成功
     * @throws IotequException svas服务调用失败时返回的错误及代码
     */
    public boolean setPhotoByBase64Stream(String userNo, String photo) throws IotequException {
        if (daysLeft <= 0) throw new IotequException(IotequThrowable.VERSION_EXPIRED, "软件已经过期");
        boolean r = svasClient.setPhoto(userNo, photo);
        return r;
    }
    /**
     * 设置照片
     * @param userNo 用户号
     * @param imageFile 照片文件,为空或不存在会抛出异常
     * @return 是否成功
     * @throws IotequException svas服务调用失败时返回的错误及代码
     */
    public boolean setPhoto(String userNo, File imageFile) throws IotequException {
        if (daysLeft <= 0) throw new IotequException(IotequThrowable.VERSION_EXPIRED, "软件已经过期");
        return setPhotoByBase64Stream(userNo, Util.getImageBase64String(imageFile));
    }
    /**
     * 获取指静脉词典
     *
     * @param userNo   用户号
     * @param fingerNo 手指编号
     * @return 词典
     * @throws IotequException svas服务调用失败时返回的错误及代码
     */
    public String getTemplate(String userNo, Integer fingerNo) throws IotequException {
        if (daysLeft <= 0) throw new IotequException(IotequThrowable.VERSION_EXPIRED, "软件已经过期");
        return svasClient.getTemplate(userNo, fingerNo);
    }

    /**
     * 获得用户注册的指静脉手指数
     *
     * @param userNo 用户号
     * @return 注册的手指数
     * @throws IotequException svas服务调用失败时返回的错误及代码
     */
    public int getFingerCount(String userNo) throws IotequException {
        if (daysLeft <= 0) throw new IotequException(IotequThrowable.VERSION_EXPIRED, "软件已经过期");
        return svasClient.getFingerCount(userNo);
    }

    /**
     * 获得用户注册的指静脉数据详细信息
     *
     * @param userNo 用户号
     * @return 注册指静脉数据列表
     * @throws IotequException svas服务调用失败时返回的错误及代码
     */
    public List<SvasTemplates> getFingerInfo(String userNo) throws IotequException {
        if (daysLeft <= 0) throw new IotequException(IotequThrowable.VERSION_EXPIRED, "软件已经过期");
        return svasClient.getFingerInfo(userNo);
    }

    /**
     * 指静脉数据认证
     *
     * @param template 一个辞书
     * @param thresh  指定阈值，0或空 表示使用系统默认值
     * @throws IotequException svas服务调用失败时返回的错误及代码
     * @return 认证结果
     */
    public SvasMatched auth(String template, Integer thresh) throws IotequException {
        if (daysLeft <= 0) throw new IotequException(IotequThrowable.VERSION_EXPIRED, "软件已经过期");
        return svasClient.auth(template,thresh);
    }
    public Map<String,Object> getEnvProperties() throws IotequException {
        return svasClient.getEnvProperties();
    }

    @Override
    public void onApplicationEvent(PeopleInfoChangedEvent event) {
        try {
            int mode = event.getMode();
        /*
        if (mode == PeopleInfoChangedEvent.delete) {
            String ids = event.getUserNoList();
            if(!Util.isEmpty(ids)) {
                try {
                    for (String s : ids.split(",")) {
                        removeUserNo(s);
                    }
                } catch (IotequException e) {
                }
            }
        } else  */
            if (mode == PeopleInfoChangedEvent.update || mode == PeopleInfoChangedEvent.insert) {
                PeopleInfoChangedEvent.People people = event.getPeople();
                if (people != null) {
                    if (!Util.isEmpty(people.getPhoto()) && people.getPhoto().indexOf("data:") != 0) {
                        try {
                            people.setPhoto(Util.getImageBase64String(new File(people.getPhoto())));
                        } catch (IotequException e) {
                            people.setPhoto(null);
                        }
                    }
                    try {
                        changeUserInfo(people);
                    } catch (IotequException e) {
                        e.printStackTrace();
                    }
                }
            }
        } catch (Exception e) {}
    }
}
