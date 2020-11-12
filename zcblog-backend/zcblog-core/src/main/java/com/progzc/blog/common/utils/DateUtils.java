package com.progzc.blog.common.utils;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * @Description 日期工具类
 * @Author zhaochao
 * @Date 2020/11/3 20:55
 * @Email zcprog@foxmail.com
 * @Version V1.0
 */
public class DateUtils {

    /**
     * 格式化日期
     * @param date    需要格式化的日期
     * @param pattern 格式化形式，如
     * @return
     */
    public static String format(Date date, String pattern) {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(pattern);
        return simpleDateFormat.format(date);
    }

}
