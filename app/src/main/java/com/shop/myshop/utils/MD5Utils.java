package com.shop.myshop.utils;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * Created by Administrator on 2017/3/2 0002.
 */

public class MD5Utils {
    public static String encoder(String password) {
        MessageDigest digest = null;
        try {
            digest = MessageDigest.getInstance("md5");
            byte[] bytes = digest.digest(password.getBytes());
            StringBuffer buffer = new StringBuffer();
            for (byte b : bytes) {
                // 6                    10
                //6      00000110
                //10    00001010
                //        00000010  与运算  2
                //        00001110  或运算  14
                int number = b & 0xfff;
                String numberStr = Integer.toHexString(number);//把int类型转为16进制
                if (numberStr.length() == 1) {
                    buffer.append("0");
                }
                buffer.append(numberStr);
                return buffer.toString();
            }
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return "";
    }
}
