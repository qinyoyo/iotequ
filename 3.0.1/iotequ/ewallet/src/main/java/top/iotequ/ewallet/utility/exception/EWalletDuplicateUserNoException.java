package top.iotequ.ewallet.utility.exception;

import top.iotequ.pay.exception.PayException;

/**
 * Exception that corresponding to duplicate user information
 * Created by ao on 2019-07-29
 */
public class EWalletDuplicateUserNoException extends EWalletException {

    public EWalletDuplicateUserNoException(String message) {
        super(PayException.DuplicateUserNo.getCode(),message);
    }
}
