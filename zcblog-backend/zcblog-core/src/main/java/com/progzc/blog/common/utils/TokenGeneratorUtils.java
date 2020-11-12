package com.progzc.blog.common.utils;

import com.progzc.blog.common.enums.ErrorEnum;
import com.progzc.blog.common.exception.MyException;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.UUID;

/**
 * @Description token工具类
 * @Author zhaochao
 * @Date 2020/11/8 22:59
 * @Email zcprog@foxmail.com
 * @Version V1.0
 */
public class TokenGeneratorUtils {

    public static final char[] hexCode = "0123456789abcdef".toCharArray();

    /**
     * 根据随机UUID生成token
     * @return
     */
    public static String generateValue() {
        return generateValue(UUID.randomUUID().toString());
    }

    private static String generateValue(String param) {
        try {
            MessageDigest md5 = MessageDigest.getInstance("MD5");
            md5.reset();
            md5.update(param.getBytes());
            byte[] digest = md5.digest();
            return toHexString(digest);
        } catch (NoSuchAlgorithmException e) {
            throw new MyException(ErrorEnum.TOKEN_GENERATOR_ERROR, e);
        }
    }

    private static String toHexString(byte[] digest) {
        if (digest == null) {
            return null;
        }
        StringBuilder sb = new StringBuilder(digest.length * 2);
        for (byte b : digest) {
            sb.append(hexCode[(b >> 4) & 0xF]);
            sb.append(hexCode[(b & 0xF)]);
        }
        return sb.toString();
    }
}
