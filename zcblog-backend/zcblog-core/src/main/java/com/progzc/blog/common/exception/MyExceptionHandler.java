package com.progzc.blog.common.exception;

import com.progzc.blog.common.Result;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

/**
 * @Description 处理全局异常
 * @Author zhaochao
 * @Date 2020/11/8 20:10
 * @Email zcprog@foxmail.com
 * @Version V1.0
 */
@RestControllerAdvice
@Slf4j
public class MyExceptionHandler {

    /**
     * 处理自定义异常
     * @param e
     * @return
     */
    @ExceptionHandler(MyException.class)
    public Result handleMyException(MyException e) {
        Result result = new Result();
        result.put("code", e.getCode());
        result.put("msg", e.getMsg());
        return result;
    }
}
