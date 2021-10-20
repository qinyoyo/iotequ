package top.iotequ.pay.dto;


import lombok.NonNull;
import top.iotequ.pay.exception.PayException;

public interface DeviceChecker {

    /**
     * 检查设备状态是否正常
     *
     * @param dto VPayDto对象
     * @throws PayException 设备状态异常时抛出异常
     */
    void checkDeviceStatus(@NonNull PayDto dto) throws PayException;
}
