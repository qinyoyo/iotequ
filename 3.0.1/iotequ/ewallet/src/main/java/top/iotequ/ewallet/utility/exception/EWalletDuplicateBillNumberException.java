package top.iotequ.ewallet.utility.exception;

import top.iotequ.pay.exception.PayException;

/**
 * Exception that corresponding to duplicate bill number (PK)
 * Created by ao on 2019-07-25
 */
public class EWalletDuplicateBillNumberException extends EWalletException {

    public EWalletDuplicateBillNumberException(String message) {
        super(PayException.DuplicateBillNumber.getCode(),message);
    }
}
