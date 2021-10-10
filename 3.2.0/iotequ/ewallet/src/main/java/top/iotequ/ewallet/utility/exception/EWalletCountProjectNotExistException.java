package top.iotequ.ewallet.utility.exception;

import top.iotequ.pay.exception.PayException;

/**
 * Exception that corresponding to item not exist in ew_count_project table.
 * Created by ao on 2019-07-24
 */
public class EWalletCountProjectNotExistException extends EWalletException {

    public EWalletCountProjectNotExistException(String message) {
        super(PayException.CountProjectNotFound.getCode(),message);
    }
}
