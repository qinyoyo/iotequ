/**************************************************
    Create by iotequ generator 3.0.0
    Author : Qinyoyo
**************************************************/

package top.iotequ.reader.pojo;
import top.iotequ.util.*;
import top.iotequ.util.StringUtil;
import top.iotequ.framework.pojo.CgEntity;
import lombok.Getter;
import lombok.Setter;
import com.google.gson.annotations.SerializedName;
import top.iotequ.util.CgFieldAnnotation;
import top.iotequ.util.CgTableAnnotation;
import java.util.*;

//  Pojo entity : DevReader (终端设备表|Device)
@CgTableAnnotation(name="dev_reader",
                   title="devReader",
                   baseUrl="/reader/devReader",
                   hasLicence=true,
                   trialDays=160,
                   pkType="String",
                   pkKeyType="2",
                   generatorName="devReader",
                   module="reader")
@Getter
@Setter
public class DevReader implements CgEntity {
    @SerializedName(value = "id", alternate = {"ID"})
    @CgFieldAnnotation(name="id",jdbcType="CHAR",length=36,nullable=false,format="@")
    private String id;		//主键 db field:id

    @SerializedName(value = "readerNo", alternate = {"reader_no","READER_NO"})
    @CgFieldAnnotation(name="reader_no",jdbcType="VARCHAR",length=20,nullable=false,format="@")
    private String readerNo;		//设备编号 db field:reader_no

    @SerializedName(value = "name", alternate = {"NAME"})
    @CgFieldAnnotation(name="name",jdbcType="VARCHAR",length=30,nullable=false,format="@")
    private String name;		//名称 db field:name

    @SerializedName(value = "type", alternate = {"TYPE"})
    @CgFieldAnnotation(name="type",jdbcType="VARCHAR",length=30,nullable=false,format="@")
    private String type;		//型号 db field:type

    @SerializedName(value = "readerGroup", alternate = {"reader_group","READER_GROUP"})
    @CgFieldAnnotation(name="reader_group",jdbcType="INTEGER",nullable=false,format="")
    private Integer readerGroup;		//设备组 db field:reader_group

    @SerializedName(value = "address", alternate = {"ADDRESS"})
    @CgFieldAnnotation(name="address",jdbcType="VARCHAR",length=100,nullable=true,format="@")
    private String address;		//地点 db field:address

    @SerializedName(value = "capacity", alternate = {"CAPACITY"})
    @CgFieldAnnotation(name="capacity",jdbcType="INTEGER",length=10,nullable=true,format="")
    private Integer capacity;		//容量 db field:capacity

    @SerializedName(value = "connectType", alternate = {"connect_type","CONNECT_TYPE"})
    @CgFieldAnnotation(name="connect_type",jdbcType="VARCHAR",length=11,nullable=false,format="@")
    private String connectType;		//连接类型 db field:connect_type

    @SerializedName(value = "ip", alternate = {"IP"})
    @CgFieldAnnotation(name="ip",jdbcType="VARCHAR",length=20,nullable=false,format="@")
    private String ip;		//IP地址 db field:ip

    @SerializedName(value = "snNo", alternate = {"sn_no","SN_NO"})
    @CgFieldAnnotation(name="sn_no",jdbcType="VARCHAR",length=36,nullable=true,format="@")
    private String snNo;		//设备序列号 db field:sn_no

    @SerializedName(value = "devMode", alternate = {"dev_mode","DEV_MODE"})
    @CgFieldAnnotation(name="dev_mode",jdbcType="VARCHAR",length=32,nullable=false,format="@")
    private String devMode;		//设备模式 db field:dev_mode

    @SerializedName(value = "firmware", alternate = {"FIRMWARE"})
    @CgFieldAnnotation(name="firmware",jdbcType="VARCHAR",length=40,nullable=true,format="@")
    private String firmware;		//固件版本 db field:firmware

    @SerializedName(value = "isOnline", alternate = {"is_online","IS_ONLINE"})
    @CgFieldAnnotation(name="is_online",jdbcType="VARCHAR",nullable=false,format="")
    private Boolean isOnline;		//在线 db field:is_online

    @SerializedName(value = "isTimeSync", alternate = {"is_time_sync","IS_TIME_SYNC"})
    @CgFieldAnnotation(name="is_time_sync",jdbcType="VARCHAR",nullable=false,format="")
    private Boolean isTimeSync;		//同步 db field:is_time_sync

    @SerializedName(value = "alignMethod", alternate = {"align_method","ALIGN_METHOD"})
    @CgFieldAnnotation(name="align_method",jdbcType="TINYINT",nullable=false,format="")
    private Byte alignMethod;		//验证方式 db field:align_method

    @SerializedName(value = "blacklightTime", alternate = {"blacklight_time","BLACKLIGHT_TIME"})
    @CgFieldAnnotation(name="blacklight_time",jdbcType="TINYINT",nullable=false,format="")
    private Byte blacklightTime;		//背光时间 db field:blacklight_time

    @SerializedName(value = "voiceprompt", alternate = {"VOICEPROMPT"})
    @CgFieldAnnotation(name="voiceprompt",jdbcType="VARCHAR",nullable=false,format="")
    private Boolean voiceprompt;		//语言提示 db field:voiceprompt

    @SerializedName(value = "menuTime", alternate = {"menu_time","MENU_TIME"})
    @CgFieldAnnotation(name="menu_time",jdbcType="TINYINT",nullable=false,format="")
    private Byte menuTime;		//菜单时间 db field:menu_time

    @SerializedName(value = "wengenform", alternate = {"WENGENFORM"})
    @CgFieldAnnotation(name="wengenform",jdbcType="TINYINT",nullable=false,format="")
    private Byte wengenform;		//韦根格式 db field:wengenform

    @SerializedName(value = "wengenOutput", alternate = {"wengen_output","WENGEN_OUTPUT"})
    @CgFieldAnnotation(name="wengen_output",jdbcType="TINYINT",nullable=false,format="")
    private Byte wengenOutput;		//韦根输出 db field:wengen_output

    @SerializedName(value = "wengenOutArea", alternate = {"wengen_out_area","WENGEN_OUT_AREA"})
    @CgFieldAnnotation(name="wengen_out_area",jdbcType="TINYINT",nullable=false,format="")
    private Byte wengenOutArea;		//韦根输出区位码 db field:wengen_out_area

    @SerializedName(value = "regfingerOutTime", alternate = {"regfinger_out_time","REGFINGER_OUT_TIME"})
    @CgFieldAnnotation(name="regfinger_out_time",jdbcType="TINYINT",nullable=false,format="")
    private Byte regfingerOutTime;		//指静脉注册超时时长 db field:regfinger_out_time

    @SerializedName(value = "authfingerOutTime", alternate = {"authfinger_out_time","AUTHFINGER_OUT_TIME"})
    @CgFieldAnnotation(name="authfinger_out_time",jdbcType="TINYINT",nullable=false,format="")
    private Byte authfingerOutTime;		//验证超时时长 db field:authfinger_out_time

    @SerializedName(value = "wgOrder", alternate = {"wg_order","WG_ORDER"})
    @CgFieldAnnotation(name="wg_order",jdbcType="TINYINT",length=2,nullable=true,format="")
    private Byte wgOrder;		//高低位 db field:wg_order

    @SerializedName(value = "relayTime", alternate = {"relay_time","RELAY_TIME"})
    @CgFieldAnnotation(name="relay_time",jdbcType="TINYINT",length=11,nullable=true,format="")
    private Byte relayTime;		//继电器开启时间 db field:relay_time

    @SerializedName(value = "fixedValue", alternate = {"fixed_value","FIXED_VALUE"})
    @CgFieldAnnotation(name="fixed_value",jdbcType="VARCHAR",length=36,nullable=true,format="@")
    private String fixedValue;		//固定码 db field:fixed_value

    @SerializedName(value = "alarmEnable", alternate = {"alarm_enable","ALARM_ENABLE"})
    @CgFieldAnnotation(name="alarm_enable",jdbcType="TINYINT",length=2,nullable=true,format="")
    private Byte alarmEnable;		//防拆告警使能 db field:alarm_enable

    @SerializedName(value = "openEnable", alternate = {"open_enable","OPEN_ENABLE"})
    @CgFieldAnnotation(name="open_enable",jdbcType="TINYINT",length=2,nullable=true,format="")
    private Byte openEnable;		//开门按钮使能 db field:open_enable

    @SerializedName(value = "doorState", alternate = {"door_state","DOOR_STATE"})
    @CgFieldAnnotation(name="door_state",jdbcType="TINYINT",length=2,nullable=true,format="")
    private Byte doorState;		//开门状态检测使能 db field:door_state

    @SerializedName(value = "relayEnable", alternate = {"relay_enable","RELAY_ENABLE"})
    @CgFieldAnnotation(name="relay_enable",jdbcType="TINYINT",length=2,nullable=true,format="")
    private Byte relayEnable;		//继电器使能 db field:relay_enable

    @SerializedName(value = "doorbellEnable", alternate = {"doorbell_enable","DOORBELL_ENABLE"})
    @CgFieldAnnotation(name="doorbell_enable",jdbcType="TINYINT",length=2,nullable=true,format="")
    private Byte doorbellEnable;		//门铃输出使能 db field:doorbell_enable

    @SerializedName(value = "wifiSsid", alternate = {"wifi_ssid","WIFI_SSID"})
    @CgFieldAnnotation(name="wifi_ssid",jdbcType="VARCHAR",length=36,nullable=true,format="@")
    private String wifiSsid;		//wifi名称 db field:wifi_ssid

    @SerializedName(value = "wifiPsw", alternate = {"wifi_psw","WIFI_PSW"})
    @CgFieldAnnotation(name="wifi_psw",jdbcType="VARCHAR",length=36,nullable=true,format="@")
    private String wifiPsw;		//wifi密码|Wifi password db field:wifi_psw

    @Override public Object getPkValue(){ return getId(); }
    @Override
    public void setPkValue(Object value) {
        if (value==null) setId(null);
        else setId(String.valueOf(value.toString()));
    }
    @Override
    public String toString() {
    	return StringUtil.toJsonString(this);
    }
//^_^ Your code start:
//^_^ Your code end.
}
