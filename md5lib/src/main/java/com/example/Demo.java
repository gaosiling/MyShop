package com.example;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class Demo {
    public static void main(String[] args) throws NoSuchAlgorithmException {
        MessageDigest digest = MessageDigest.getInstance("md5");
//        String password = "123456";
//        String password = "312fd2f3cabafc9417c35fd00efdaa5d";
//        String password = "1c138be35c6cf9993d329e20cc0f9e4d";
//        String password = "5019fa118fb9fd2fdafc515f85fe4fb5322efd3fe1";
//        String password = "fc8ffcff46ff867c264e08fa8fcc3ff8af932bf81";
//        String password = "fc70b3ff985b41ff678ff6f8afd224fc81ef9710";
        String password = "ff6f896c225953fa7fc62f0afde4a776474fcc";
        byte[] bytes = digest.digest(password.getBytes());
            StringBuffer buffer = new StringBuffer();
        for (byte b : bytes){
            // 6                    10
            //6      00000110
            //10    00001010
            //        00000010  与运算  2
            //        00001110  或运算  14
            int number = b & 0xfff;
            String numberStr = Integer.toHexString(number);//把int类型转为16进制
            if(numberStr.length() == 1) {
                buffer.append("0");
            }
            buffer.append(numberStr);
            System.out.println(buffer.toString());
        }
    }
}
