package com.progzc.blog.common;

import com.progzc.blog.common.enums.ErrorEnum;

import java.util.HashMap;

/**
 * @Description 封装服务端响应数据
 * @Author zhaochao
 * @Date 2020/10/27 23:59
 * @Email zcprog@foxmail.com
 * @Version V1.0
 */
public class Result extends HashMap<String, Object> {

    @Override
    public Result put(String key, Object value) {
        super.put(key, value); // 利用HashMap来封装响应数据
        return this;
    }

    // 默认响应成功
    public Result(){
        put("code", 200);
        put("msg", "success");
    }

    // 响应成功200
    public static Result ok(){
        return new Result();
    }

    // 响应系统错误500
    public static Result error(){
        return error(ErrorEnum.UNKNOWN);
    }

    // 响应系统错误500
    public static Result error(String msg){
        return new Result().put("code", ErrorEnum.UNKNOWN.getCode()).put("msg", msg);
    }

    // 响应系统错误500
    public static Result exception() {
        return exception(ErrorEnum.UNKNOWN);
    }

    // 响应错误信息
    public static Result exception(ErrorEnum errorEnum) {
        return new Result().put("code", errorEnum.getCode()).put("msg", errorEnum.getMsg());
    }

    // 响应错误信息
    public static Result error(ErrorEnum errorEnum) {
        return new Result().put("code", errorEnum.getCode()).put("msg", errorEnum.getMsg());
    }

    // 响应错误信息
    public static  Result error(Integer code, String msg){
        return new Result().put("code", code).put("msg", msg);
    }

}
