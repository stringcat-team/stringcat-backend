package com.sp.api.common.utils;

import com.sp.api.common.exception.ApiException;
import org.apache.tomcat.util.codec.binary.Base64;
import org.apache.commons.lang3.StringUtils;

import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

public class Aes256 {
    final private static String IV = "http://string-cat.so";
    final private static String KEY = "string-cat.so";

    // 암호화
    public static String encrypt(String str) {
        return encrypt(str, IV);
    }

    public static String encrypt(String str, String ivString) {
        try {
            byte[] keyBytes = KEY.substring(0, 16).getBytes("UTF-8");
            SecretKeySpec keySpec = new SecretKeySpec(keyBytes, "AES");
            String iv = getIv(ivString);

            Cipher c = Cipher.getInstance("AES/CBC/PKCS5Padding");
            c.init(Cipher.ENCRYPT_MODE, keySpec, new IvParameterSpec(iv.getBytes("UTF-8")));

            byte[] encrypted = c.doFinal(str.getBytes("UTF-8"));
            String enStr = new String(Base64.encodeBase64(encrypted));

            return enStr;
        } catch(Exception e) {
            throw new ApiException("암호화중 에러 발생 :: " + e.getMessage());
        }
    }

    //복호화
    public static String decrypt(String str) {
        return decrypt(str, IV);
    }

    public static String decrypt(String str, String ivString) {
        try {
            byte[] keyBytes = KEY.substring(0, 16).getBytes("UTF-8");
            SecretKeySpec keySpec = new SecretKeySpec(keyBytes, "AES");
            String iv = getIv(ivString);
            Cipher c = Cipher.getInstance("AES/CBC/PKCS5Padding");
            c.init(Cipher.DECRYPT_MODE, keySpec, new IvParameterSpec(iv.getBytes("UTF-8")));

            byte[] byteStr = Base64.decodeBase64(str.getBytes());

            return new String(c.doFinal(byteStr),"UTF-8");
        } catch(Exception e) {
            throw new ApiException("복화중 에러 발생 :: " + e.getMessage());
        }
    }

    public static String getIv(String iv) {
        if (iv == null) {
            return IV.substring(0, 16);
        } else if (iv.length() >= 15) {
            return iv.substring(0, 16);
        } else {
            return StringUtils.rightPad(iv, 32).substring(0, 16);
        }
    }
}
