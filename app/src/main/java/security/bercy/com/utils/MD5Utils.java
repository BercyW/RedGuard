package security.bercy.com.utils;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * Created by Bercy on 8/11/17.
 * <p>
 * MD5 加密
 */

public class MD5Utils {
    public static String encode(String password) {
        try {
            MessageDigest instance = MessageDigest.getInstance("MD5");
            byte[] digest = instance.digest(password.getBytes());
            StringBuffer sb = new StringBuffer();

            for (byte b : digest) {
                int i = b & 0xff;
                String hexString = Integer.toHexString(i);
                // System.out.println(hexString);
                if (hexString.length() < 2) {
                    hexString = "0" + hexString;

                }
                sb.append(hexString);
            }
            return sb.toString();
        } catch (NoSuchAlgorithmException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        return "";

    }
}