package top.iotequ.ewallet.utility.exception;

import top.iotequ.pay.exception.PayException;

/**
 * Created by ao on 2019-08-08
 */
public class EWalletUserExpireException extends EWalletException {

    public EWalletUserExpireException(String message) {
        super(PayException.UserExpired.getCode(), message);
    }
}
