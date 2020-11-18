package com.progzc.blog.common.utils;

import com.progzc.blog.entity.sys.vo.SysUserVO;
import org.junit.Test;

/**
 * @Description 加密解密工具类测试
 * @Author zhaochao
 * @Date 2020/11/16 1:51
 * @Email zcprog@foxmail.com
 * @Version V1.0
 */
public class EncryptUtilsTest {
    @Test
    public void test() {
        String plaintext = "admin123";
        System.out.println("明文：" + plaintext);
        System.out.println("加密：" + EncryptUtils.encrypt(plaintext));
        System.out.println("解密：" + EncryptUtils.decrypt(EncryptUtils.encrypt(plaintext)));
    }

    @Test
    public void test2() {
        SysUserVO sysUserVO = new SysUserVO();
        sysUserVO.setUsername("admin123");

        String encrypt = EncryptUtils.encrypt(sysUserVO.getUsername());
        System.out.println(encrypt);
    }
}
