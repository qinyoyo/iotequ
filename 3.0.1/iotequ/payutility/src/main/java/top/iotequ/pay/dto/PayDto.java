package top.iotequ.pay.dto;

import top.iotequ.pay.exception.PayException;

public interface PayDto {


    /**
     * 获取设备号
     *
     * @return 设备号
     */
    String getDeviceNo();

    /**
     * 获得数据的校验值。校验值对需要传输的字段（签名字段外)按照名称排序后，忽略null和空串，相加得到的串进行签名计算
     *
     * @return 签名校验值
     */
    String getSignature();

    /**
     * 设置签名字段
     *
     * @param signature 新的签名值
     */
    void setSignature(String signature);

    /**
     * 获得参数检查器
     *
     * @return 设备检查器或null
     */
    ParameterChecker getParameterChecker();

    /**
     * 设置参数检查器
     *
     * @param checker 检查器
     */
    void setParameterChecker(ParameterChecker checker);

    /**
     * 获得设备检查器
     *
     * @return 设备检查器或null
     */
    DeviceChecker getChecker();

    /**
     * 设置设备检查器
     *
     * @param checker 检查器
     */
    void setChecker(DeviceChecker checker);

    /**
     * 获取关联设备对象
     *
     * @return 设备对象
     */
    Object getPayPos();

    /**
     * 设置关联pos设备
     *
     * @param pos 设备对象
     */
    void setPayPos(Object pos);

    /**
     * 获取工作密钥
     *
     * @return 工作密钥
     */
    String getWorkKey();

    /**
     * 设置工作加密密钥
     *
     * @param key 密钥
     */
    void setWorkKey(String key);

    /**
     * 在发送数据前进行数据检查，计算md5，加密等操作
     *
     * @param key 默认加解密key，为null时根据pos状态获得。终端设备上自动记录相关的密钥
     * @throws PayException 异常
     */

    void getReadyToSend(String key) throws PayException;

    /**
     * 接收到数据后进行解密，md5检查和数据检查
     *
     * @param key 默认加解密key，为null时根据pos状态获得。终端设备上自动记录相关的密钥
     * @throws PayException 异常
     */
    void afterReceived(String key) throws PayException;
}
