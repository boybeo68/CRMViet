package vn.altalab.app.crmvietpack.utility;

import android.support.annotation.NonNull;

import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * Created by HieuDT on 6/2/2016.
 */
public class MD5Utils {

    public static String getMD5(String input) {
        try {
            MessageDigest md = MessageDigest.getInstance("MD5");
            byte[] messageDigest = md.digest(input.getBytes());
            BigInteger number = new BigInteger(1, messageDigest);
            String hashtext = number.toString(16);
            // Now we need to zero pad it if you actually want the full 32
            // chars.
            while (hashtext.length() < 32) {
                hashtext = "0" + hashtext;
            }
            return hashtext;
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }
    }

    @NonNull
    public static String encryptMD5(String password) {
        try {
            MessageDigest md = MessageDigest.getInstance("MD5");
            md.update(password.getBytes());

            byte byteData[] = md.digest();

            // convert the byte to hex format method 1
            StringBuffer sb = new StringBuffer();
            for (int i = 0; i < byteData.length; i++) {
                sb.append(Integer.toString((byteData[i] & 0xff) + 0x100, 16)
                        .substring(1));
            }

//			System.out.println("Digest(in hex format):: " + sb.toString());
            // convert the byte to hex format method 2
            // StringBuffer hexString = new StringBuffer();
            // for (int i = 0; i < byteData.length; i++) {
            // String hex = Integer.toHexString(0xff & byteData[i]);
            // if (hex.length() == 1)
            // hexString.append('0');
            // hexString.append(hex);
            // }
            // System.out.println("Digest(in hex format):: " +
            // hexString.toString());
            return sb.toString();
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }
    }

}
