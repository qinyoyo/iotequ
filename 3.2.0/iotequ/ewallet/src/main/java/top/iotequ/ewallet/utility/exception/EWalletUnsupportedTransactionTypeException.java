package top.iotequ.ewallet.utility.exception;

import top.iotequ.pay.exception.PayException;

/**
 * TODO: Add description Here.
 * Created by ao on 2019-08-09
 */
public class EWalletUnsupportedTransactionTypeException extends EWalletException{

    public EWalletUnsupportedTransactionTypeException(String message) {
        super(PayException.UnsupportedType.getCode(), message);
    }
}
