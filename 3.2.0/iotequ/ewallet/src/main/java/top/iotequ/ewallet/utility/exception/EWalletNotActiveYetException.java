package top.iotequ.ewallet.utility.exception;

import top.iotequ.pay.exception.PayException;

/**
 * Created by ao on 2019-08-08
 */
public class EWalletNotActiveYetException extends EWalletException {

    public EWalletNotActiveYetException(String message) {
        super(PayException.UserNotActivate.getCode(), message);
    }
}
