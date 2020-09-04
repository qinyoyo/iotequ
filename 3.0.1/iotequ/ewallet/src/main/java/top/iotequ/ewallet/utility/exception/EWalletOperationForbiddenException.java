package top.iotequ.ewallet.utility.exception;

import top.iotequ.pay.exception.PayException;

/**
 * Exception that corresponding to operation is forbidden
 * Created by ao on 2019-07-26
 */
public class EWalletOperationForbiddenException extends EWalletException {

    public EWalletOperationForbiddenException(String message) {
        super(PayException.OperationForbidden.getCode(),message);
    }
}
