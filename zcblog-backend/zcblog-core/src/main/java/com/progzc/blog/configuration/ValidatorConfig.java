package com.progzc.blog.configuration;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.validation.Validation;
import javax.validation.Validator;

/**
 * @Description 校验配置类
 * @Author zhaochao
 * @Date 2020/11/23 0:10
 * @Email zcprog@foxmail.com
 * @Version V1.0
 */
@Configuration
public class ValidatorConfig {

    @Bean
    public Validator validatorTemplate() {
        return Validation.buildDefaultValidatorFactory().getValidator();
    }

}
