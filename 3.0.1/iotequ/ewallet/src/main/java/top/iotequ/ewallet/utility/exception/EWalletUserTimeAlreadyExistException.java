package top.iotequ.ewallet.utility.exception;

import top.iotequ.pay.exception.PayException;

/**
 * Exception that corresponding to trying to create duplicate {@link top.iotequ.ewallet.pojo.EwUserTime}
 * Created by ao on 2019-07-30
 */
public class EWalletUserTimeAlreadyExistException extends EWalletException {

    public EWalletUserTimeAlreadyExistException(String message) {
        super(PayException.UserTimeAlreadyExist.getCode(),message);
    }
}
