package com.progzc.blog.common.validation.verify;

import com.progzc.blog.common.validation.annotation.MyEmail;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

/**
 * @Description 自定义邮箱格式校验类
 * @Author zhaochao
 * @Date 2020/11/24 23:21
 * @Email zcprog@foxmail.com
 * @Version V1.0
 */
@Slf4j
public class MyEmailVerify implements ConstraintValidator<MyEmail, String> {

    @Override
    public void initialize(MyEmail constraintAnnotation) {

    }

    @Override
    public boolean isValid(String email, ConstraintValidatorContext context) {
        if (StringUtils.isEmpty(email)) {
            log.error("邮箱为空");
            return false;
        }
        String regex = "^[a-zA-Z0-9_.-]+@[a-zA-Z0-9-]+(\\.[a-zA-Z0-9-]+)*\\.[a-zA-Z0-9]{2,6}$";
        return email.matches(regex);
    }
}
