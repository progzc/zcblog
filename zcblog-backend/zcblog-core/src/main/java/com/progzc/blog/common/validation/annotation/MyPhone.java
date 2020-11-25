package com.progzc.blog.common.validation.annotation;

import com.progzc.blog.common.validation.verify.MyPhoneVerify;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.*;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

/**
 * @Description 自定义手机号校验注解
 * @Author zhaochao
 * @Date 2020/11/24 23:17
 * @Email zcprog@foxmail.com
 * @Version V1.0
 */

@Documented
@Constraint(validatedBy = MyPhoneVerify.class)
@Target({METHOD, FIELD, ANNOTATION_TYPE, CONSTRUCTOR, PARAMETER, TYPE_USE})
@Retention(RUNTIME)
public @interface MyPhone {

    String message() default "";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
