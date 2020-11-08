package com.progzc.blog.common.exception;

import com.progzc.blog.common.enums.ErrorEnum;
import lombok.Data;

/**
 * @Description 自定义异常类
 * @Author zhaochao
 * @Date 2020/11/8 15:44
 * @Email zcprog@foxmail.com
 * @Version V1.0
 */
@Data
public class MyException extends RuntimeException {

    private String msg;
    /**
     * 默认异常是服务器内部错误
     */
    private int code = 500;

    public MyException() {
        super(ErrorEnum.UNKNOWN.getMsg());
        this.msg = ErrorEnum.UNKNOWN.getMsg();
    }

    public MyException(String msg) {
        super(msg);
        this.msg = msg;
    }

    public MyException(ErrorEnum errorEnum) {
        super(errorEnum.getMsg());
        this.msg = errorEnum.getMsg();
        this.code = errorEnum.getCode();
    }

    public MyException(ErrorEnum errorEnum, Throwable e) {
        super(errorEnum.getMsg(), e);
        this.msg = errorEnum.getMsg();
        this.code = errorEnum.getCode();
    }
}
