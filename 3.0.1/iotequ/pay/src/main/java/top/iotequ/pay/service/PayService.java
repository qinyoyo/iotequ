package top.iotequ.pay.service;

import org.springframework.lang.NonNull;
import top.iotequ.ewallet.pojo.EwBill;
import top.iotequ.ewallet.pojo.EwUser;
import top.iotequ.pay.dto.request.*;
import top.iotequ.pay.dto.response.*;
import top.iotequ.pay.dto.response.UpdateInfoResponse.TradeAccount;
import top.iotequ.pay.exception.PayException;

import java.util.Date;
import java.util.List;
import java.util.Map;

public interface PayService {

    /**
     * 获得设备可用的商品信息
     *
     * @param version          设备版本号
     * @param deviceNo         设备编号
     * @param ewalletActive    设备是否支持现金钱包
     * @param countProjectList 设备支持的计次项目清单
     * @param timeProjectList  设备支持的计时项目清单
     * @return 可用消费商品信息
     * @throws PayException 错误异常
     */

    List<TradeAccount> getTradeAccountInfo(String version, String deviceNo, boolean ewalletActive, String countProjectList, String timeProjectList)
            throws PayException;

    /**
     * 获取账户信息
     *
     * @param ack         已经实例化的应答对象
     * @param userInfo    用户信息
     * @param accountType 账户类别，null列举所有账户
     * @return 账户信息列表
     * @throws PayException 错误异常
     */
    List<QueryBalanceResponse.AccountInfo> getAccountInfo(QueryBalanceResponse ack, String userInfo, Integer accountType)
            throws PayException;

    /**
     * 根据消费请求进行消费
     *
     * @param request 请求
     * @return 消费结果
     * @throws PayException 错误异常
     */
    CostResponse cost(@NonNull CostRequest request) throws PayException;

    /**
     * 末笔交易查询
     *
     * @param deviceNo 设备号
     * @return 末笔交易
     * @throws PayException 错误异常
     */
    LastTradeInfoResponse lastTradeInfo(String deviceNo) throws PayException;

    /**
     * 取消交易
     *
     * @param request 请求信息
     * @return 应答
     * @throws PayException 错误异常
     */
    CancelTradeResponse cancelTrade(CancelTradeRequest request) throws PayException;

    /**
     * 充值
     *
     * @param request 充值请求
     * @return 结果
     * @throws PayException 异常
     */
    ChargeResponse charge(ChargeRequest request) throws PayException;

    /**
     * 签退
     *
     * @param request 交易统计信息
     * @return 显示信息，如果账务不平，包含账务统计信息
     * @throws PayException 错误异常
     */
    LogoutResponse logout(LogoutRequest request) throws PayException;

    CreateAccountResponse createAccount(CreateAccountRequest request) throws PayException;

    EwUser queryUser(String userNo);

    public Map<String, Object> queryBill(String userNo, Date dt0, Date dt1, Integer offset, Integer limit) throws PayException;

}
