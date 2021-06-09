/**************************************************
    Create by iotequ generator 3.0.0
    Author : Qinyoyo
**************************************************/

package top.iotequ.ewallet.pojo;
import top.iotequ.util.StringUtil;
import top.iotequ.framework.pojo.CgEntity;
import lombok.Getter;
import lombok.Setter;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import top.iotequ.framework.serializer.jackson.DateDeserializer;
import top.iotequ.framework.serializer.jackson.DatetimeSerializer;
import com.google.gson.annotations.SerializedName;
import top.iotequ.util.CgFieldAnnotation;
import top.iotequ.util.CgTableAnnotation;
import java.util.*;

//  Pojo entity : EwBill (消费明细)
@CgTableAnnotation(name="ew_bill",
                   title="ewBill",
                   join="LEFT JOIN pay_shop ON ew_bill.shop_id = pay_shop.id",
                   pk="no",
                   entityPk="no",
                   baseUrl="/ewallet/ewBill",
                   hasLicence=false,
                   pkType="String",
                   pkKeyType="3",
                   generatorName="ewBill",
                   module="ewallet")
@Getter
@Setter
public class EwBill implements CgEntity, EWallet {
    @SerializedName(value = "no", alternate = {"NO"})
    @CgFieldAnnotation(name="ew_bill.no",jdbcType="VARCHAR",length=32,nullable=false,format="@")
    private String no;		//流水号 db field:no

    @SerializedName(value = "canceled", alternate = {"CANCELED"})
    @CgFieldAnnotation(name="ew_bill.canceled",jdbcType="TINYINT",nullable=false,format="")
    private Boolean canceled;		//已取消 db field:canceled

    @SerializedName(value = "isCharge", alternate = {"is_charge","IS_CHARGE"})
    @CgFieldAnnotation(name="ew_bill.is_charge",jdbcType="TINYINT",nullable=false,format="")
    private Boolean isCharge;		//是否充值 db field:is_charge

    @SerializedName(value = "userNo", alternate = {"user_no","USER_NO"})
    @CgFieldAnnotation(name="ew_bill.user_no",jdbcType="VARCHAR",length=45,nullable=false,format="@")
    private String userNo;		//用户编号 db field:user_no

    @SerializedName(value = "batchNo", alternate = {"batch_no","BATCH_NO"})
    @CgFieldAnnotation(name="ew_bill.batch_no",jdbcType="VARCHAR",length=45,nullable=false,format="@")
    private String batchNo;		//批次号 db field:batch_no

    @JsonDeserialize(using = DateDeserializer.class)
    @JsonSerialize(using = DatetimeSerializer.class)
    @SerializedName(value = "dt", alternate = {"DT"})
    @CgFieldAnnotation(name="ew_bill.dt",jdbcType="TIMESTAMP",nullable=false,format="yyyy-mm-dd hh:mm")
    private Date dt;		//时间 db field:dt

    @SerializedName(value = "operationType", alternate = {"operation_type","OPERATION_TYPE"})
    @CgFieldAnnotation(name="ew_bill.operation_type",jdbcType="INTEGER",nullable=false,format="")
    private Integer operationType;		//操作代码 db field:operation_type

    @SerializedName(value = "costType", alternate = {"cost_type","COST_TYPE"})
    @CgFieldAnnotation(name="ew_bill.cost_type",jdbcType="INTEGER",nullable=false,format="")
    private Integer costType;		//交易类别 db field:cost_type

    @SerializedName(value = "projectId", alternate = {"project_id","PROJECT_ID"})
    @CgFieldAnnotation(name="ew_bill.project_id",jdbcType="INTEGER",nullable=false,format="")
    private Integer projectId;		//交易项目 db field:project_id

    @SerializedName(value = "projectName", alternate = {"project_name","PROJECT_NAME"})
    @CgFieldAnnotation(name="ew_bill.project_name",jdbcType="VARCHAR",length=45,nullable=false,format="@")
    private String projectName;		//交易名称 db field:project_name

    @SerializedName(value = "projectPrice", alternate = {"project_price","PROJECT_PRICE"})
    @CgFieldAnnotation(name="ew_bill.project_price",jdbcType="INTEGER",nullable=false,format="")
    private Integer projectPrice;		//产品单价 db field:project_price

    @SerializedName(value = "amount", alternate = {"AMOUNT"})
    @CgFieldAnnotation(name="ew_bill.amount",jdbcType="INTEGER",nullable=false,format="")
    private Integer amount;		//交易额 db field:amount

    @SerializedName(value = "amountBefore", alternate = {"amount_before","AMOUNT_BEFORE"})
    @CgFieldAnnotation(name="ew_bill.amount_before",jdbcType="INTEGER",nullable=false,format="")
    private Integer amountBefore;		//交易前钱包值 db field:amount_before

    @SerializedName(value = "bonus", alternate = {"BONUS"})
    @CgFieldAnnotation(name="ew_bill.bonus",jdbcType="INTEGER",nullable=false,format="")
    private Integer bonus;		//获得积分 db field:bonus

    @SerializedName(value = "bonusBefore", alternate = {"bonus_before","BONUS_BEFORE"})
    @CgFieldAnnotation(name="ew_bill.bonus_before",jdbcType="INTEGER",nullable=false,format="")
    private Integer bonusBefore;		//交易前积分 db field:bonus_before

    @SerializedName(value = "deviceNo", alternate = {"device_no","DEVICE_NO"})
    @CgFieldAnnotation(name="ew_bill.device_no",jdbcType="VARCHAR",length=45,nullable=false,format="@")
    private String deviceNo;		//设备编号 db field:device_no

    @SerializedName(value = "deviceStream", alternate = {"device_stream","DEVICE_STREAM"})
    @CgFieldAnnotation(name="ew_bill.device_stream",jdbcType="VARCHAR",length=45,nullable=false,format="@")
    private String deviceStream;		//设备流水 db field:device_stream

    @JsonDeserialize(using = DateDeserializer.class)
    @JsonSerialize(using = DatetimeSerializer.class)
    @SerializedName(value = "deviceDt", alternate = {"device_dt","DEVICE_DT"})
    @CgFieldAnnotation(name="ew_bill.device_dt",jdbcType="TIMESTAMP",nullable=false,format="yyyy-mm-dd hh:mm")
    private Date deviceDt;		//设备时间 db field:device_dt

    @SerializedName(value = "tradeNo", alternate = {"trade_no","TRADE_NO"})
    @CgFieldAnnotation(name="ew_bill.trade_no",jdbcType="VARCHAR",length=45,nullable=false,format="@")
    private String tradeNo;		//订单号 db field:trade_no

    @SerializedName(value = "operatorNo", alternate = {"operator_no","OPERATOR_NO"})
    @CgFieldAnnotation(name="ew_bill.operator_no",jdbcType="VARCHAR",length=45,nullable=false,format="@")
    private String operatorNo;		//操作员编号 db field:operator_no

    @SerializedName(value = "checkCode", alternate = {"check_code","CHECK_CODE"})
    @CgFieldAnnotation(name="ew_bill.check_code",jdbcType="VARCHAR",length=45,nullable=false,format="@")
    private String checkCode;		//检验码 db field:check_code

    @SerializedName(value = "linkNo", alternate = {"link_no","LINK_NO"})
    @CgFieldAnnotation(name="ew_bill.link_no",jdbcType="VARCHAR",length=45,nullable=true,format="@")
    private String linkNo;		//关联流水号 db field:link_no

    @SerializedName(value = "loginId", alternate = {"login_id","LOGIN_ID"})
    @CgFieldAnnotation(name="ew_bill.login_id",jdbcType="INTEGER",nullable=false,format="")
    private Integer loginId;		//签到id db field:login_id

    @SerializedName(value = "shopId", alternate = {"shop_id","SHOP_ID"})
    @CgFieldAnnotation(name="ew_bill.shop_id",jdbcType="VARCHAR",length=45,nullable=false,format="@")
    private String shopId;		//商户编号 db field:shop_id
    @SerializedName(value = "name", alternate = {"NAME"})
    @CgFieldAnnotation(name="pay_shop.name",jdbcType="VARCHAR")
    private String name;

    private Boolean isValid;		//有效性 非数据库字段

    @Override public Object getPkValue(){ return getNo(); }
    @Override
    public void setPkValue(Object value) {
        if (value==null) setNo(null);
        else setNo(String.valueOf(value.toString()));
    }
    @Override
    public String toString() {
    	return StringUtil.toJsonString(this);
    }
//^_^ Your code start:
    @Override
    public String toSortedString() {
        return "[EwBill]"
                .concat(" no:" + this.no)
                .concat(" dt:" + this.dt)
                .concat(" is charge:" + (this.isCharge?"1":"0"))
                .concat(" canceled:" + (this.canceled?"1":"0"))
                .concat(" operation type:" + this.operationType)
                .concat(" user no:" + this.userNo)
                .concat(" cost type:" + this.costType)
                .concat(" project id:" + this.projectId)
                .concat(" amount:" + this.amount)
                .concat(" amount before:" + this.amountBefore)
                .concat(" bonus:" + this.bonus)
                .concat(" bonus before:" + this.bonusBefore)
                .concat(" device no:" + this.deviceNo)
                .concat(" shop id:" + this.shopId)
                .concat(" device stream:" + this.deviceStream)
                .concat(" device dt:" + this.deviceDt)
                .concat(" trade no:" + this.tradeNo)
                .concat(" operator no:" + this.operatorNo)
                .concat(" login id:" + this.loginId)
                .concat(" link no:" + (this.linkNo==null?"":this.linkNo));
    }
//^_^ Your code end.
}
