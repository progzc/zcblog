package com.progzc.blog.common.utils;

import com.progzc.blog.common.exception.MyException;
import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import javax.validation.ConstraintViolation;
import javax.validation.Validator;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * @Description 校验工具类
 * @Author zhaochao
 * @Date 2020/11/23 0:07
 * @Email zcprog@foxmail.com
 * @Version V1.0
 */
@Component
public class ValidatorUtils {

    @Resource
    private Validator validatorTemplate;

    public void validateEntity(Object object, Class<?>... groups) {
        Set<ConstraintViolation<Object>> violations = validatorTemplate.validate(object, groups);
        if (!violations.isEmpty()) {
            List<String> collect = violations.stream().map(ConstraintViolation::getMessage).collect(Collectors.toList());
            String msg = StringUtils.join(collect, ",");
            throw new MyException(msg);
        }
    }
}
