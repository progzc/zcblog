package com.progzc.blog.common.utils;

import com.progzc.blog.common.enums.ErrorEnum;
import com.progzc.blog.common.exception.MyException;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.io.UnsupportedEncodingException;
import java.nio.charset.Charset;
import java.security.NoSuchAlgorithmException;

/**
 * @Description 加密解密工具类
 * @Author zhaochao
 * @Date 2020/11/16 0:46
 * @Email zcprog@foxmail.com
 * @Version V1.0
 */
@Slf4j
public class EncryptUtils {

    /**
     * 后台密钥
     * 对应前台密钥：6466326531633534343362323263623965323866373862323937613061666630
     */
    private static final String KEY = "df2e1c5443b22cb9e28f78b297a0aff0";
    /**
     *
     * 后台偏移量：偏移量字符串必须是16位，当模式是CBC的时候必须设置偏移量0123456789abcdef
     * 对应前台偏移量为：30313233343536373839616263646566
     */
    private static final String IV = "0123456789abcdef";
    private static final String ALGORITHM = "AES"; // 加密算法
    private static final String ALGORITHMPROVIDER = "AES/CBC/PKCS5Padding"; // 算法/模式/补码方式

    /**
     * AES加密
     * @param plaintext
     * @return
     */
    public static String encrypt(String plaintext){
        try {
            return byteToHexString(encrypt(plaintext, KEY.getBytes("utf-8")));
        } catch (UnsupportedEncodingException e) {
            log.error(ErrorEnum.ENCRYPT_FAILED.getMsg());
            throw new MyException(ErrorEnum.ENCRYPT_FAILED);
        }
    }

    /**
     * AES加密
     * @param plaintext
     * @param key
     * @return
     */
    public static byte[] encrypt(String plaintext, byte[] key) {
        SecretKey secretKey = new SecretKeySpec(key, ALGORITHM);
        try {
            IvParameterSpec ivParameterSpec = getIv();
            Cipher cipher = Cipher.getInstance(ALGORITHMPROVIDER);
            cipher.init(Cipher.ENCRYPT_MODE, secretKey, ivParameterSpec);
            return cipher.doFinal(plaintext.getBytes(Charset.forName("utf-8")));
        }catch (Exception e){
            log.error(ErrorEnum.ENCRYPT_FAILED.getMsg());
            throw new MyException(ErrorEnum.ENCRYPT_FAILED);
        }
    }

    /**
     * AES解密
     * @param ciphertext
     * @return
     */
    public static String decrypt(String ciphertext){

        try {
            return new String(decrypt(ciphertext, KEY.getBytes("utf-8")),"utf-8");
        } catch (UnsupportedEncodingException e) {
            log.error(ErrorEnum.DENCRYPT_FAILED.getMsg());
            throw  new MyException(ErrorEnum.DENCRYPT_FAILED);
        }
    }

    /**
     * AES解密
     * @param ciphertext
     * @param key
     * @return
     */
    public static byte[] decrypt(String ciphertext, byte[] key) {
        SecretKey secretKey = new SecretKeySpec(key, ALGORITHM);
        try {
            IvParameterSpec ivParameterSpec = getIv();
            Cipher cipher = Cipher.getInstance(ALGORITHMPROVIDER);
            cipher.init(Cipher.DECRYPT_MODE, secretKey, ivParameterSpec);
            byte[] hexBytes = hexStringToBytes(ciphertext);
            return cipher.doFinal(hexBytes);
        } catch (Exception e) {
            log.error(ErrorEnum.DENCRYPT_FAILED.getMsg());
            throw  new MyException(ErrorEnum.DENCRYPT_FAILED);
        }
    }

    /**
     * 生成随机key
     * @return
     * @throws NoSuchAlgorithmException
     */
    public static byte[] generatorKey() throws NoSuchAlgorithmException {
        KeyGenerator keyGenerator = KeyGenerator.getInstance(ALGORITHM);
        keyGenerator.init(256); //默认128，获得无政策权限后可为192或256
        SecretKey secretKey = keyGenerator.generateKey();
        return secretKey.getEncoded();
    }

    /**
     * 生成随机偏移量
     * @return
     * @throws UnsupportedEncodingException
     */
    public static IvParameterSpec getIv() throws UnsupportedEncodingException {
        IvParameterSpec ivParameterSpec = new IvParameterSpec(IV.getBytes("utf-8"));
        return ivParameterSpec;
    }

    /**
     * 将byte转换为16进制字符串
     * @param src
     * @return
     */
    private static String byteToHexString(byte[] src) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < src.length; i++) {
            int v = src[i] & 0xff;
            String hv = Integer.toHexString(v);
            if (hv.length() < 2) {
                sb.append("0");
            }
            sb.append(hv);
        }
        return sb.toString();
    }

    /**
     * 将16进制字符串装换为byte数组
     * @param hexString
     * @return
     */
    private static byte[] hexStringToBytes(String hexString) {
        hexString = hexString.toUpperCase();
        int length = hexString.length() / 2;
        char[] hexChars = hexString.toCharArray();
        byte[] b = new byte[length];
        for (int i = 0; i < length; i++) {
            int pos = i * 2;
            b[i] = (byte) (charToByte(hexChars[pos]) << 4 | charToByte(hexChars[pos + 1]));
        }
        return b;
    }

    private static byte charToByte(char c) {
        return (byte) "0123456789ABCDEF".indexOf(c);
    }

    @Test
    public void test() throws Exception{
        byte key[] = KEY.getBytes("utf-8");
        String plaintext = "admin123";
        System.out.println("密钥:" + byteToHexString(key)); // 6466326531633534343362323263623965323866373862323937613061666630
        System.out.println("偏移量：" + byteToHexString(getIv().getIV())); // 30313233343536373839616263646566
        System.out.println("原字符串:" + plaintext);
        String enc = byteToHexString(encrypt(plaintext, key));
        System.out.println("加密:" + enc);
        System.out.println("解密:" + new String(decrypt(enc, key), "utf-8"));

        System.out.println("-------------------------------------------");
        System.out.println("密钥:" + byteToHexString(key)); // 6466326531633534343362323263623965323866373862323937613061666630
        System.out.println("偏移量：" + byteToHexString(getIv().getIV())); // 30313233343536373839616263646566
        System.out.println("原字符串:" + plaintext);
        System.out.println("加密:" + encrypt(plaintext));
        System.out.println("解密:" + decrypt(encrypt(plaintext)));
    }
}
