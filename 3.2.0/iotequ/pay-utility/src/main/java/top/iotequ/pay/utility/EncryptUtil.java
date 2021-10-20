package top.iotequ.pay.utility;

import net.iharder.Base64;
import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.spec.SecretKeySpec;
import java.io.IOException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;


public class EncryptUtil {

    private static final String DEFAULT_KEY = "8705087";
    public static String encryptString(String str, String key, boolean encryption)
            throws NoSuchPaddingException, NoSuchAlgorithmException, InvalidKeyException, BadPaddingException, IllegalBlockSizeException, IOException {
        if (PayUtil.isEmpty(str)) {
            return str;
        }
        byte[] bin = encryption ? str.getBytes() : Base64.decode(str);
        SecretKeySpec sKeySpec = new SecretKeySpec(getKeyBytes(key), "AES");
        Cipher cipher = Cipher.getInstance("AES/ECB/PKCS5Padding");
        cipher.init(encryption ? Cipher.ENCRYPT_MODE : Cipher.DECRYPT_MODE, sKeySpec);
        byte[] result = cipher.doFinal(bin);
        if (encryption) {
            return Base64.encodeBytes(result);
        } else {
            return new String(result);
        }
    }

    private static byte[] getKeyBytes(String key) {
        if (PayUtil.isEmpty(key)) {
            key = DEFAULT_KEY;
        }
        byte[] r = new byte[16];
        byte[] s = key.getBytes();
        for (int i = 0; i < 16 && i < s.length; i++) {
            r[i] = s[i];
        }
        for (int i = s.length; i < 16; i++) {
            r[i] = 68;
        }
        return r;
    }

    public static Integer encryptInt(Integer value, String key, boolean encryption) {
        byte[] bin = getKeyBytes(key);
        int distance = 0;
        for (byte aBin : bin) {
            distance += (int) aBin;
        }
        distance %= 32;
        return encryption ? Integer.rotateLeft(value, distance)
                : Integer.rotateRight(value, distance);
    }

    public static Byte encryptByte(Byte value, String key, boolean encryption) {
        byte[] keyValue = getKeyBytes(key);
        return (byte) (value ^ keyValue[0]);
    }
}
