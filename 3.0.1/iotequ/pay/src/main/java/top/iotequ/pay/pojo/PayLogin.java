/**************************************************
    Create by iotequ generator 3.0.0
    Author : Qinyoyo
**************************************************/

package top.iotequ.pay.pojo;
import top.iotequ.framework.util.StringUtil;
import top.iotequ.framework.pojo.CgEntity;
import lombok.Getter;
import lombok.Setter;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import top.iotequ.framework.serializer.jackson.DateDeserializer;
import top.iotequ.framework.serializer.jackson.DatetimeSerializer;
import com.google.gson.annotations.SerializedName;
import top.iotequ.framework.util.CgFieldAnnotation;
import top.iotequ.framework.util.CgTableAnnotation;
import java.util.*;

//  Pojo entity : PayLogin (签到日志)
@CgTableAnnotation(name="pay_login",
                   title="payLogin",
                   join="LEFT JOIN pay_operator ON pay_login.operator_id = pay_operator.id",
                   baseUrl="/pay/payLogin",
                   hasLicence=false,
                   pkType="Integer",
                   pkKeyType="1",
                   generatorName="payLogin",
                   module="pay")
@Getter
@Setter
public class PayLogin implements CgEntity {
    @SerializedName(value = "id", alternate = {"ID"})
    @CgFieldAnnotation(name="pay_login.id",jdbcType="INTEGER",nullable=false,format="")
    private Integer id;		//ID db field:id

    @SerializedName(value = "posId", alternate = {"pos_id","POS_ID"})
    @CgFieldAnnotation(name="pay_login.pos_id",jdbcType="INTEGER",nullable=false,format="")
    private Integer posId;		//pos终端 db field:pos_id

    @SerializedName(value = "shopId", alternate = {"shop_id","SHOP_ID"})
    @CgFieldAnnotation(name="pay_login.shop_id",jdbcType="INTEGER",nullable=false,format="")
    private Integer shopId;		//商店 db field:shop_id

    @SerializedName(value = "batchNo", alternate = {"batch_no","BATCH_NO"})
    @CgFieldAnnotation(name="pay_login.batch_no",jdbcType="VARCHAR",length=45,nullable=false,format="@")
    private String batchNo;		//批次号 db field:batch_no

    @JsonDeserialize(using = DateDeserializer.class)
    @JsonSerialize(using = DatetimeSerializer.class)
    @SerializedName(value = "loginTime", alternate = {"login_time","LOGIN_TIME"})
    @CgFieldAnnotation(name="pay_login.login_time",jdbcType="TIMESTAMP",nullable=false,format="yyyy-mm-dd hh:mm")
    private Date loginTime;		//签到时间 db field:login_time

    @JsonDeserialize(using = DateDeserializer.class)
    @JsonSerialize(using = DatetimeSerializer.class)
    @SerializedName(value = "logoutTime", alternate = {"logout_time","LOGOUT_TIME"})
    @CgFieldAnnotation(name="pay_login.logout_time",jdbcType="TIMESTAMP",nullable=true,format="yyyy-mm-dd hh:mm")
    private Date logoutTime;		//签退时间 db field:logout_time

    @SerializedName(value = "deviceStream", alternate = {"device_stream","DEVICE_STREAM"})
    @CgFieldAnnotation(name="pay_login.device_stream",jdbcType="VARCHAR",length=45,nullable=true,format="@")
    private String deviceStream;		//设备流水号 db field:device_stream

    @SerializedName(value = "randomNo", alternate = {"random_no","RANDOM_NO"})
    @CgFieldAnnotation(name="pay_login.random_no",jdbcType="VARCHAR",length=45,nullable=false,format="@")
    private String randomNo;		//随机密钥 db field:random_no

    @SerializedName(value = "appVersion", alternate = {"app_version","APP_VERSION"})
    @CgFieldAnnotation(name="pay_login.app_version",jdbcType="VARCHAR",length=45,nullable=false,format="@")
    private String appVersion;		//应用版本号 db field:app_version

    @SerializedName(value = "tradeCount", alternate = {"trade_count","TRADE_COUNT"})
    @CgFieldAnnotation(name="pay_login.trade_count",jdbcType="INTEGER",nullable=false,format="")
    private Integer tradeCount;		//交易次数 db field:trade_count

    @SerializedName(value = "failureCount", alternate = {"failure_count","FAILURE_COUNT"})
    @CgFieldAnnotation(name="pay_login.failure_count",jdbcType="INTEGER",nullable=false,format="")
    private Integer failureCount;		//失败次数 db field:failure_count

    @SerializedName(value = "operatorId", alternate = {"operator_id","OPERATOR_ID"})
    @CgFieldAnnotation(name="pay_login.operator_id",jdbcType="INTEGER",nullable=false,format="")
    private Integer operatorId;		//操作人员 db field:operator_id
    @SerializedName(value = "name", alternate = {"NAME"})
    @CgFieldAnnotation(name="pay_operator.name",jdbcType="INTEGER")
    private String name;

    @Override public Object getPkValue(){ return getId(); }
    @Override
    public void setPkValue(Object value) {
        if (value==null) setId(null);
        else setId(Integer.valueOf(value.toString()));
    }
    @Override
    public String toString() {
    	return StringUtil.toJsonString(this);
    }
//^_^ Your code start:
//^_^ Your code end.
}
