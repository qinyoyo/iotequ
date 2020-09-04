package top.iotequ.ewallet.utility.exception;

import top.iotequ.pay.exception.PayException;

/**
 * Exception that corresponding to trying to create duplicate {@link top.iotequ.ewallet.pojo.EwUserCount}
 * Created by ao on 2019-07-30
 */
public class EWalletUserCountAlreadyExistException extends EWalletException {

    public EWalletUserCountAlreadyExistException(String message) {
        super(PayException.UserCountAlreadyExist.getCode(),message);
    }
}
