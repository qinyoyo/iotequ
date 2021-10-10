package top.iotequ.ewallet.utility.exception;

import top.iotequ.pay.exception.PayException;

/**
 * Exception that corresponding to item not exist in ew_user
 * Created by ao on 2019-07-24
 */
public class EWalletUserNotExistException extends EWalletException {

    public EWalletUserNotExistException(String message) {
        super(PayException.UserNotFound.getCode(),message);
    }
}
