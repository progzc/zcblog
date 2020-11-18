package com.progzc.blog.common.utils;

import org.junit.Test;

/**
 * @Description 正则表达式测试
 * @Author zhaochao
 * @Date 2020/11/17 21:22
 * @Email zcprog@foxmail.com
 * @Version V1.0
 */
public class RegExpTest {
    /**
     * 测试密码校验方式：8-16个字符,不包含空格,必须包含数字,字母或字符至少两种，不允许有空格、中文
     */
    @Test
    public void test(){
        String[] strs = {"!@~#$^%&^%#@", "12345678}{|", "A12345678", "12345678:;;",
                         "444412345678", "1234)5678", "、、、、、、、、", "11111111",
                         "bbbbbbbbb", "无1bbbbbbb","无bbbbbbbbbbb", "12345678900", "a1234567",
                         ",12345678", "ijhfsshahahah", ".,.,.,,.,.,.", "chaojiwudi22ah",
                         ".,.,.,,.,55", "123456781234654687321343513213", "!!!!AAAAA", "a.a.a.a.a.a.a", "11 1"};
        for (String str : strs) {
            StringBuilder sb = new StringBuilder();
            sb.append(str).append("   ");
//            sb.append(checkChineseAndSpace(str) ? "" : "包含中文和空格").append("   ");
//            sb.append(checkInclude2Kind(str) ? "": "不包含任意两种").append("   ");
//            sb.append(checkLength(str) ? "" : "长度超标").append("   ");
            sb.append(checkChineseAndSpace(str) && checkInclude2Kind(str) && checkLength(str));
            System.out.println(sb.toString());
        }

    }

    // 不能包含空格和中文字符
    public boolean checkChineseAndSpace(String value) {
        String regex = "^[^\\s\\u4e00-\\u9fa5]+$";
        return value.matches(regex);
    }

    // 字母数/数字以及标点符号至少包含2种
    public boolean checkInclude2Kind(String value) {
        String regex = "(?!^[0-9]+$)(?!^[A-Za-z]+$)(?!^[`~!@#$%^&*()\\-_+={}\\[\\]|;:\"'<>,.?/]+$)(?!^[^\\x21-\\x7e]+$)^.+$";
        return value.matches(regex);
    }

    // 长度为8~16
    public boolean checkLength(String value) {
        String regex = "^.{8,16}$";
        return value.matches(regex);
    }
}
