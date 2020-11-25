package com.progzc.blog.common.validation.verify;

import com.progzc.blog.common.validation.annotation.MyPhone;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

/**
 * @Description 自定义手机号格式校验类
 * @Author zhaochao
 * @Date 2020/11/24 23:21
 * @Email zcprog@foxmail.com
 * @Version V1.0
 */
@Slf4j
public class MyPhoneVerify implements ConstraintValidator<MyPhone, String> {

    @Override
    public void initialize(MyPhone constraintAnnotation) {

    }

    @Override
    public boolean isValid(String phone, ConstraintValidatorContext context) {
        if (StringUtils.isEmpty(phone)) {
            log.error("手机号为空");
            return false;
        }
        // 可用号段主要有(不包括上网卡)：130~139、150~153，155~159，180~189、170~171、176~178
        String regex = "^((13[0-9])|(17[0-1,6-8])|(15[^4,\\D])|(18[0-9]))\\d{8}$";
        return phone.matches(regex);
    }
}
