package top.iotequ.ewallet.utility.exception;

/**
 * Exception that corresponding to unexpected return value when invoking *Dao.list()
 * Created by ao on 2019-07-24
 */
public class EWalletListException extends EWalletException {

    public EWalletListException(String message) {
        super(1,message);
    }
}
