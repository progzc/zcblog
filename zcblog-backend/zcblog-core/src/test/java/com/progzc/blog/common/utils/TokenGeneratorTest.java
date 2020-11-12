package com.progzc.blog.common.utils;

import org.junit.Test;

/**
 * @Description Token生成器测试
 * @Author zhaochao
 * @Date 2020/11/9 9:08
 * @Email zcprog@foxmail.com
 * @Version V1.0
 */
public class TokenGeneratorTest {

    @Test
    public void test() {
        String token = TokenGeneratorUtils.generateValue();
        System.out.println(token.length());
        System.out.println(token);
    }
}
