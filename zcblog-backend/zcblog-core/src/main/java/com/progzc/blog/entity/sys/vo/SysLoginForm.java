package com.progzc.blog.entity.sys.vo;

import lombok.Data;

/**
 * @Description 封装表单登录数据
 * @Author zhaochao
 * @Date 2020/11/8 20:49
 * @Email zcprog@foxmail.com
 * @Version V1.0
 */
@Data
public class SysLoginForm {
    private String username;
    private String password;
    private String uuid;
    private String captcha;
}
