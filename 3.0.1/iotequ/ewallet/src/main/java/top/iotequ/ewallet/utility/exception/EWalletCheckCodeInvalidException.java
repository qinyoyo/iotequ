package top.iotequ.ewallet.utility.exception;

import top.iotequ.pay.exception.PayException;

/**
 * Exception that corresponding to checkCode inconsistency.
 * Created by ao on 2019-07-24
 */
public class EWalletCheckCodeInvalidException extends EWalletException {

    public EWalletCheckCodeInvalidException(String message) {
        super(PayException.InvalidCheckCode.getCode(),message);
    }
}
