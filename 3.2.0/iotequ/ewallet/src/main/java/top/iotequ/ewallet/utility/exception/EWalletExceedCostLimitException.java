package top.iotequ.ewallet.utility.exception;

import top.iotequ.pay.exception.PayException;

/**
 * Exception that corresponding to exceed the limit of cost per transaction
 * Created by ao on 2019-07-29
 */
public class EWalletExceedCostLimitException extends EWalletException {

    public EWalletExceedCostLimitException(String message) {
        super(PayException.ExceedCostLimit.getCode(),message);
    }
}
