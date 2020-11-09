package com.progzc.blog.common.utils;

import org.junit.Test;

/**
 * @Description Json工具类测试
 * @Author zhaochao
 * @Date 2020/11/9 16:12
 * @Email zcprog@foxmail.com
 * @Version V1.0
 */
public class JsonUtilsTest {

    /**
     * 需要区分普通字符串与json字符串之间的区别；
     * JsonUtils只能实现json字符串与对象之间的转化；
     * JsonUtils对于普通字符串与对象之间的转化可能会报异常（这个我在Redis工具类中进行了过滤操作）
     *
     */
    @Test
    public void test1(){
        // 以下三种情况都是普通字符串与对象之间的转化，不要使用JsonUtils进行转换
        Integer s1 = JsonUtils.toObj("34", Integer.class);
        String s2 = JsonUtils.toObj("345", String.class); // 这个不会报错
        String s3 = JsonUtils.toObj("pd56n", String.class); // 这个为什么会报错？
        System.out.println(s1);
        System.out.println(s2);
        System.out.println(s3); // 输出null
    }
}
