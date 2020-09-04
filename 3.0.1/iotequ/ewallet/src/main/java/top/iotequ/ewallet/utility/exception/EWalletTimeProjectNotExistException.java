package top.iotequ.ewallet.utility.exception;

import top.iotequ.pay.exception.PayException;

/**
 * Exception that corresponding to item not exist in ew_time_project table.
 * Created by ao on 2019-07-24
 */
public class EWalletTimeProjectNotExistException extends EWalletException {

    public EWalletTimeProjectNotExistException(String message) {
        super(PayException.TimeProjectNotFound.getCode(),message);
    }
}
