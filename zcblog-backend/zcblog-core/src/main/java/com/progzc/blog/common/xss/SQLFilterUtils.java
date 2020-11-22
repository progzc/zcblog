package com.progzc.blog.common.xss;

import com.progzc.blog.common.enums.ErrorEnum;
import com.progzc.blog.common.exception.MyException;
import org.springframework.util.StringUtils;

/**
 * @Description SQL过滤工具类
 * @Author zhaochao
 * @Date 2020/11/22 16:08
 * @Email zcprog@foxmail.com
 * @Version V1.0
 */
public class SQLFilterUtils {
    /**
     * 过滤SQL，防止SQL注入
     * @param str
     * @return
     */
    public static String sqlInject(String str) {

        if (StringUtils.isEmpty(str)) {
            return null;
        }
        //去掉'|"|;|\字符
        str = StringUtils.replace(str, "'", "");
        str = StringUtils.replace(str, "\"", "");
        str = StringUtils.replace(str, ";", "");
        str = StringUtils.replace(str, "\\", "");

        //转换成小写
        str = str.toLowerCase();

        //非法字符
        String[] keywords = {"master", "truncate", "insert", "select", "delete", "update", "declare", "alert", "drop"};

        //判断是否包含非法字符
        for (String keyword : keywords) {
            if (str.contains(keyword)) {
                throw new MyException(ErrorEnum.SQL_ILLEGAL);
            }
        }
        return str;
    }
}
