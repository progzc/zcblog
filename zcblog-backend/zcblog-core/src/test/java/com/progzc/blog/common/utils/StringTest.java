package com.progzc.blog.common.utils;

import org.junit.Test;

import java.util.Arrays;

/**
 * @Description 字符串测试
 * @Author zhaochao
 * @Date 2020/11/29 19:05
 * @Email zcprog@foxmail.com
 * @Version V1.0
 */
public class StringTest {

    @Test
    public void test() {
        String url = "http://cloud.progzc.com/blog/2020/11/29/893c44270e994e7995e862fe1895582f.png";
        String domain = "http://cloud.progzc.com";
        System.out.println(url.indexOf(domain));
        System.out.println(url.substring(domain.length() + 1));
    }

    @Test
    public void test2() {
        String url = "http://cloud.progzc.com/blog/2020/11/29/893c44270e994e7995e862fe1895582f.png";
        String domain = "http://cloud.progzc.com";
        String src = url.substring(domain.length() + 1);
        String suffix = src.substring(src.lastIndexOf("."));
        String backup = src.substring(0, src.lastIndexOf(".")) + "-backup" + suffix;

        System.out.println(src);
        System.out.println(suffix);
        System.out.println(backup);
    }

    @Test
    public void test3() {
        String[] urlList = {"abc", "def", "ghi"};
        Arrays.stream(urlList).forEach(url -> {
            url = url.substring(0, 2);
        });
        System.out.println(Arrays.asList(urlList)); //  [abc, def, ghi]
    }

    @Test
    public void test4() {
        String[] urlList = {"abc", "def", "ghi"};
        for (String url : urlList) {
            url = url.substring(0, 2);
        }
        System.out.println(Arrays.asList(urlList)); //  [abc, def, ghi]
    }

    @Test
    public void test5() {
        String[] urlList = {"abc", "def", "ghi"};
        for (int i = 0; i < urlList.length; i++) {
            urlList[i] = urlList[i].substring(0, 2);
        }
        System.out.println(Arrays.asList(urlList)); //  [ab, de, gh]
    }
}
