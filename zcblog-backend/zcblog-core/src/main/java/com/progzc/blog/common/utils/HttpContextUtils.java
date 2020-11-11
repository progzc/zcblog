package com.progzc.blog.common.utils;

import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;

/**
 * @Description HTTP工具类
 * @Author zhaochao
 * @Date 2020/11/12 0:12
 * @Email zcprog@foxmail.com
 * @Version V1.0
 */
public class HttpContextUtils {

    /**
     * 获取http请求
     * @return
     */
    public static HttpServletRequest getHttpServletRequest() {
        return ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
    }

    /**
     * 获取域名
     * @return
     */
    public static String getDomain(){
        HttpServletRequest request = getHttpServletRequest();
        StringBuffer url = request.getRequestURL();
        return url.delete(url.length() - request.getRequestURI().length(), url.length()).toString();
    }

    /**
     * 获取源请求地址
     * @return
     */
    public static String getOrigin(){
        HttpServletRequest httpServletRequest = getHttpServletRequest();
        return httpServletRequest.getHeader("Origin");
    }
}
