package com.progzc.blog.common.utils;

import org.junit.Test;

import java.util.Arrays;

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
    public void passwordTest() {
        String[] strs = {"!@~#$^%&^%#@", "12345678}{|", "A12345678", "12345678:;;",
                "444412345678", "1234)5678", "、、、、、、、、", "11111111",
                "bbbbbbbbb", "无1bbbbbbb", "无bbbbbbbbbbb", "12345678900", "a1234567",
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


    /**
     * 邮箱密码测试
     * 1.@之前必须有内容且只能是字母（大小写）、数字、下划线(_)、减号（-）、点（.）
     * 2.@和最后一个.之间必须有内容且只能是字母（大小写）、数字、点（.）、减号（-），且两个点不能紧挨着
     * 3.最后一个.之后必须有内容且内容只能是字母（大小写）、数字且长度为大于等于2个字符，小于等于6个字符
     */
    @Test
    public void emailTest() {
        String regex = "^[a-zA-Z0-9_.-]+@[a-zA-Z0-9-]+(\\.[a-zA-Z0-9-]+)*\\.[a-zA-Z0-9]{2,6}$";

        String[] strs = {"441030517@QQ..com", "119941779@qq,com", "5579001QQ@.COM", "1107531656@q?q?.com",
                "654088115@@qq.com", "495456580@qq@139.com", "279985462@qq。com.cn", "chen@foxmail.com)m",
                "2990814514@?￡QQ.COM", "xxxxxxxxx@___.com.cn", "xxxxxxxxx@wwew_163sadasdf.com.cn",
                "xxxxxxx@163.com", "xxxxxxxxx@wwew-163.com.cn", "hjkjhk@645654.2121-6878.com.wcn"};
        Arrays.stream(strs).forEach(str -> {
            StringBuilder result = new StringBuilder().append(str).append("   ").append(str.matches(regex));
            System.out.println(result);
        });
    }

    /**
     * 校验手机号码是否合法
     * 可用号段主要有(不包括上网卡)：130~139、150~153，155~159，180~189、170~171、176~178
     */
    @Test
    public void phoneTest() {
        String regex = "^((13[0-9])|(17[0-1,6-8])|(15[^4,\\D])|(18[0-9]))\\d{8}$";

        String[] strs = {"13012394593", "15212394593", "15412394593", "18712394593", "17112394593", "17512394593"};
        Arrays.stream(strs).forEach(str -> {
            StringBuilder result = new StringBuilder().append(str).append("   ").append(str.matches(regex));
            System.out.println(result);
        });
    }
}
