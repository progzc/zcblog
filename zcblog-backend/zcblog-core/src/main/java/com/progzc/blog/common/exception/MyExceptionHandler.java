package com.progzc.blog.common.exception;

import com.progzc.blog.common.Result;
import com.progzc.blog.common.enums.ErrorEnum;
import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.ShiroException;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.NoHandlerFoundException;

/**
 * @Description 处理全局异常：范围从小到大
 * @Author zhaochao
 * @Date 2020/11/8 20:10
 * @Email zcprog@foxmail.com
 * @Version V1.0
 */
@RestControllerAdvice
@Slf4j
public class MyExceptionHandler {

    /**
     * 处理自定义异常：包含校验异常、认证异常
     * @param e
     * @return
     */
    @ExceptionHandler(MyException.class)
    public Result handleMyException(MyException e) {
        log.error(e.getMessage(), e);
        Result result = new Result();
        result.put("code", e.getCode());
        result.put("msg", e.getMsg());
        return result;
    }

    /**
     * 处理路径找不到的异常
     * @param e
     * @return
     */
    @ExceptionHandler(NoHandlerFoundException.class)
    public Result hanlerNoFoundException(NoHandlerFoundException e) {
        log.error(e.getMessage(), e);
        return Result.exception(ErrorEnum.PATH_NOT_FOUND);
    }

    /**
     * 处理DAO异常
     * @param e
     * @return
     */
    @ExceptionHandler(DuplicateKeyException.class)
    public Result handleDuplicateKeyException(DuplicateKeyException e) {
        log.error(e.getMessage(), e);
        return Result.exception(ErrorEnum.DUPLICATE_KEY);
    }


    /**
     * 处理鉴权异常以及认证中的除自定义外的异常
     * @param e
     * @return
     */
    @ExceptionHandler(ShiroException.class)
    public Result hanldeAuthorizationException(ShiroException e) {
        log.error(e.getMessage(), e);
        return Result.exception(ErrorEnum.NO_AUTH);
    }

    /**
     * 处理Exception异常
     * @param e
     * @return
     */
    @ExceptionHandler(Exception.class)
    public Result handleException(Exception e) {
        log.error(e.getMessage(), e);
        return Result.exception();

    }
}
