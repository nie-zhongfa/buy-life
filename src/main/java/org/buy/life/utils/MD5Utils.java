package org.buy.life.utils;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * @menu TODO
 * @Author YourJustin
 * @Date 2024/8/26 3:34 PM
 * I am a code man ^_^ !!
 */
public class MD5Utils {

    public static String encryptMD5(String input) {
        try {
            // 创建MD5加密对象
            MessageDigest md5 = MessageDigest.getInstance("MD5");
            // 执行加密操作
            byte[] messageDigest = md5.digest(input.getBytes());
            // 将字节数组转换为16进制字符串
            StringBuilder hexString = new StringBuilder();
            for (byte b : messageDigest) {
                String hex = Integer.toHexString(0xff & b);
                if (hex.length() == 1) {
                    hexString.append('0');
                }
                hexString.append(hex);
            }
            // 返回加密后的字符串
            return hexString.toString();
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }
    }

    public static void main(String[] args) {
        String password = "123456";
        String encryptedPassword = encryptMD5(password);
        System.out.println("Original Password: " + password);
        System.out.println("Encrypted Password: " + encryptedPassword);
    }
}
