package top.iotequ.ewallet.utility.exception;

import top.iotequ.pay.exception.PayException;

/**
 * Exception that corresponding to no bill found.
 * Created by ao on 2019-07-26
 */
public class EWalletNoBillFoundException extends EWalletException {

    public EWalletNoBillFoundException(String message) {
        super(PayException.NoBillFound.getCode(),message);
    }
}
