package top.iotequ.ewallet.utility.exception;

import top.iotequ.pay.exception.PayException;

/**
 * TODO: Add description Here.
 * Created by ao on 2019-07-31
 */
public class EWalletInactiveUserException extends EWalletException {

    public EWalletInactiveUserException(String message) {
        super(PayException.UserInactive.getCode(),message);
    }
}
