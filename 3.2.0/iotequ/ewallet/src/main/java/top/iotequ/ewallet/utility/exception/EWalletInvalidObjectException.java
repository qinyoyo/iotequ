package top.iotequ.ewallet.utility.exception;

import top.iotequ.pay.exception.PayException;

/**
 * Exception that corresponding to retrieving invalid object.
 * Created by ao on 2019-07-25
 */
public class EWalletInvalidObjectException extends EWalletException {

    public EWalletInvalidObjectException(String message) {
        super(PayException.InvalidObject.getCode(),message);
    }
}
