package top.iotequ.ewallet.utility.exception;

import top.iotequ.pay.exception.PayException;

/**
 * Generic Exception Class for E-wallet.
 * Created by ao on 2019-07-24
 */
public class EWalletException extends PayException {

    public EWalletException(int code, String message) {
        super(code,message);
    }
}
