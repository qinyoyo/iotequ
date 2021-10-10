package top.iotequ.ewallet.utility.exception;

import top.iotequ.pay.exception.PayException;

/**
 * Exception that corresponding to balance not enough.
 * Created by ao on 2019-07-24
 */
public class EWalletInsufficientBalanceException extends EWalletException {

    public EWalletInsufficientBalanceException(String message) {
        super(PayException.InsufficientBalance.getCode(),message);
    }
}
