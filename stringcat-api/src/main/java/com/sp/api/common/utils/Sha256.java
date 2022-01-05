package com.sp.api.common.utils;

import lombok.extern.slf4j.Slf4j;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

@Slf4j
public class Sha256 {

    //SHA-256으로 해싱하는 메서드
    public static String encode(String message) {
        try {
            MessageDigest messageDigest = MessageDigest.getInstance("SHA-256");
            messageDigest.update(message.getBytes());

            return bytesToHex(messageDigest.digest());

        } catch (NoSuchAlgorithmException exception) {
            throw new RuntimeException(exception);
        }
    }

    //바이트를 Hex 값으로 변환하는 메서드
    public static String bytesToHex(byte[] bytes) {
        StringBuilder sb = new StringBuilder();

        for(byte b : bytes) {
            sb.append(String.format("%02x", b));
        }

        return sb.toString();
    }
}
