package top.iotequ.ewallet.pojo;

import top.iotequ.framework.util.StringUtil;
import top.iotequ.framework.util.Util;

import java.util.Objects;

/**
 * Interface of three types of E-wallet.
 * Created by ao on 2019-07-24
 */
public interface EWallet {

    static String encryptEWalletSortedString(String string) {
        if (Objects.isNull(string)) return "";
        else {
            return StringUtil.encodePassword(string);
        }
    }

    static boolean isValid(EWallet eWallet) {
        if (Objects.isNull(eWallet)) return false;
        String checkCode = eWallet.getCheckCode(),
                sortedString = eWallet.toSortedString();
        if (Util.isEmpty(checkCode)) return Util.isEmpty(sortedString);
        else return checkCode.equals(encryptEWalletSortedString(sortedString));
    }

    static void updateCheckCode(EWallet eWallet) {
        if (Objects.nonNull(eWallet)) {
            eWallet.setCheckCode(encryptEWalletSortedString(eWallet.toSortedString()));
        }
    }

    String getCheckCode();

    void setCheckCode(String checkCode);

    String toSortedString();
}
