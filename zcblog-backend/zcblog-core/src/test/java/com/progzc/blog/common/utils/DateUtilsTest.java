package com.progzc.blog.common.utils;

import org.junit.Test;

import java.util.Date;

/**
 * @Description 日期格式测试
 * @Author zhaochao
 * @Date 2020/11/29 0:01
 * @Email zcprog@foxmail.com
 * @Version V1.0
 */
public class DateUtilsTest {

    @Test
    public void test() {
        String date = DateUtils.format(new Date(), "yyyyMMdd");
        System.out.println(date);
    }

    @Test
    public void test2() {
        String date = DateUtils.format(new Date(), "yyyy/MM/dd");
        System.out.println(date);
    }

    @Test
    public void test3() {
        String date = DateUtils.format(new Date(), "yyyy-MM-dd");
        System.out.println(date);
    }
}
