package com.progzc.blog.common.validation.verify;

import com.progzc.blog.common.validation.annotation.MyPassword;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

/**
 * @Description 自定义密码格式校验类
 * @Author zhaochao
 * @Date 2020/11/24 23:21
 * @Email zcprog@foxmail.com
 * @Version V1.0
 */
@Slf4j
public class MyPasswordVerify implements ConstraintValidator<MyPassword, String> {

    @Override
    public void initialize(MyPassword constraintAnnotation) {

    }

    @Override
    public boolean isValid(String password, ConstraintValidatorContext context) {
        if (StringUtils.isEmpty(password)) {
            log.error("密码为空");
            return false;
        }
        // 不能包含空格和中文字符
        String regex1 = "^[^\\s\\u4e00-\\u9fa5]+$";
        // 字母数/数字以及标点符号至少包含2种
        String regex2 = "(?!^[0-9]+$)(?!^[A-Za-z]+$)(?!^[`~!@#$%^&*()\\-_+={}\\[\\]|;:\"'<>,.?/]+$)(?!^[^\\x21-\\x7e]+$)^.+$";
        // 长度为8~16
        String regex3 = "^.{8,16}$";
        return password.matches(regex1) && password.matches(regex2) && password.matches(regex3);
    }
}
