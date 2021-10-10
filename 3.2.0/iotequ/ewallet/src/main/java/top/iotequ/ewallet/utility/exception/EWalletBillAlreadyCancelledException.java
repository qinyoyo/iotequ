package top.iotequ.ewallet.utility.exception;

import top.iotequ.pay.exception.PayException;

/**
 * Exception that corresponding to access cancelled bill record
 * Created by ao on 2019-07-30
 */
public class EWalletBillAlreadyCancelledException extends EWalletException {

    public EWalletBillAlreadyCancelledException(String message) {
        super(PayException.AlreadyCancelled.getCode(),message);
    }
}
