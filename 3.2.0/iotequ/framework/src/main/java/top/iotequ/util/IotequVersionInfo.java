package top.iotequ.util;

import lombok.NonNull;
import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.system.ApplicationHome;

import java.io.File;
import java.io.FileFilter;
import java.util.*;
import java.util.jar.*;

@Getter
@Setter
public class IotequVersionInfo {
    private static List<IotequVersionInfo> IotequVersionInfoList = null;
    private String groupId;
    private String module;
    private String version;
    private Date buildTime;
    private Integer verLicence=null;
    private Integer verTrialDays=null;
    private Integer licenceLeft = null;
    private Integer trialDaysLeft = null;
    //public  static Map<String,String> licencesInfo = new HashMap();
    private boolean isIotequModule=false;
    static public Date getBuildTime() {
        if (Util.isEmpty(IotequVersionInfoList)) return null;
        else return IotequVersionInfoList.get(0).buildTime;
    }
    static public IotequVersionInfo getModuleVersion(String module) {
        if (Util.isEmpty(IotequVersionInfoList)) return null;
        Optional<IotequVersionInfo> ov = IotequVersionInfoList.stream().filter(v -> module.equals(v.module)).findFirst();
        if (ov.isPresent()) return ov.get();
        else return null;
    }
    static public void setModuleLicence(String module,Integer licence) {
        IotequVersionInfo m = getModuleVersion(module);
        if (m!=null) m.verLicence = licence;
    }
    static public void setModuleLicenceLeft(String module,Integer licenceLeft) {
        IotequVersionInfo m = getModuleVersion(module);
        if (m!=null) m.licenceLeft = licenceLeft;
    }
    static public void setModuleTrialDays(String module,Integer trialDays) {
        IotequVersionInfo m = getModuleVersion(module);
        if (m!=null) m.verTrialDays = trialDays;
    }
    static public void setModuleTrialDaysLeft(String module,Integer trialDaysLeft) {
        IotequVersionInfo m = getModuleVersion(module);
        if (m!=null) m.trialDaysLeft = trialDaysLeft;
    }
    static public void addModule(Object beanObj) {
        if (IotequVersionInfoList==null) IotequVersionInfoList=new ArrayList<>();
        IotequVersionInfo md = new IotequVersionInfo(beanObj);
        if (md.isIotequModule) IotequVersionInfoList.add(md);
    }
    public static IotequVersionInfo getVersion(@NonNull String module) {
        if (!Util.isEmpty(IotequVersionInfoList)) {
            for (IotequVersionInfo md : IotequVersionInfoList) {
                if (module.equals(md.module)) return md;
            }
        }
        return null;
    }
    public static List<IotequVersionInfo> getAllVersions() {
        return IotequVersionInfoList;
    }
    public static String getAllVersionJsonInfo() {
        if (!Util.isEmpty(IotequVersionInfoList)) {
            StringBuilder sb = new StringBuilder();
            sb.append("{\"modules\":[");
            for (int i = 0 ; i<IotequVersionInfoList.size() ; i++) {
                IotequVersionInfo md = IotequVersionInfoList.get(i);
                if (i!=0) sb.append(",");
                sb.append(md.toJsonString());
            }
            sb.append("]}");
            return sb.toString();
        }
        return null;
    }

    public IotequVersionInfo(Object iotequModule) {
        if (Objects.nonNull(iotequModule)) {
            try {
                module = StringUtil.toString(ObjectUtil.getPrivateField(iotequModule, "name"));
                groupId = StringUtil.toString(ObjectUtil.getPrivateField(iotequModule, "groupId"));
                version = StringUtil.toString(ObjectUtil.getPrivateField(iotequModule, "version"));
                Object dt = ObjectUtil.runMethod(iotequModule, "getBuildTime");
                if (dt == null) buildTime = new Date();
                else buildTime = (Date) dt;
                isIotequModule = (boolean)ObjectUtil.runMethod(iotequModule,"isIotequModule");
                if (isIotequModule) {
                    System.out.println("---------- module: "+groupId+"."+module+" "+version+" build at "+DateUtil.date2String(buildTime,null) +" --------");
                }
                //if (buildTime==null) buildTime=new Date();
                //else buildTime.setTime(buildTime.getTime()+8L*3600000L);
            } catch (Exception e) {}
        }
    }
    public String toJsonString() {
        if (isIotequModule) {
            return "{\"groupId\":\""  + groupId + "\","
                    + "\"module\":\""  + module + "\","
                    + "\"version\":\""  + version + "\","
                    + "\"buildTime\":\""  + DateUtil.date2String(buildTime, null) + "\"}";
        } else return null;
    }
    @Override
    public String toString() {
        if (isIotequModule) {
            String ver = groupId + "." + module + " : " + version + " (" + DateUtil.date2String(buildTime, null) + ")";
            if (verLicence!=null) ver += (" Licence = "+verLicence);
            if (licenceLeft!=null) ver += (" LicenceLeft = "+licenceLeft);
            if (verTrialDays!=null) ver += (" TrialDays = "+verTrialDays);
            if (trialDaysLeft!=null) ver += (" TrialDaysLeft = "+trialDaysLeft);
            return ver;
        } else return null;
    }
}
