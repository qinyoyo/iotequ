package top.iotequ.ewallet.utility.exception;

import top.iotequ.pay.exception.PayException;

/**
 * Exception that corresponding to exceed the limit of everyday transaction
 * Created by ao on 2019-07-29
 */
public class EWalletExceedDayLimitException extends EWalletException {

    public EWalletExceedDayLimitException(String message) {
        super(PayException.ExceedDayLimit.getCode(),message);
    }
}
