package top.iotequ.pay.dto;

import lombok.NonNull;
import top.iotequ.pay.exception.PayException;

public interface ParameterChecker {
    /**
     * 检查参数是否正确
     *
     * @param dto PayDto对象
     * @throws PayException 设备状态异常时抛出异常
     */
    void checkParameters(@NonNull PayDto dto) throws PayException;
}
