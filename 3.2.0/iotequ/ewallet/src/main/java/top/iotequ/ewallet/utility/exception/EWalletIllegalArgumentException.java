package top.iotequ.ewallet.utility.exception;

import top.iotequ.pay.exception.PayException;

/**
 * Exception that corresponding to providing illegal argument.
 * Created by ao on 2019-07-25
 */
public class EWalletIllegalArgumentException extends EWalletException {

    public EWalletIllegalArgumentException(String message) {
        super(PayException.IllegalArgument.getCode(),message);
    }
}
