package com.progzc.blog.entity.sys.auth;

import io.swagger.annotations.ApiModel;
import lombok.Data;

import java.io.Serializable;

/**
 * @Description 封装用户Token+用户id
 * @Author zhaochao
 * @Date 2020/11/11 10:46
 * @Email zcprog@foxmail.com
 * @Version V1.0
 */
@Data
@ApiModel(value = "SysUserToken对象", description = "系统用户Token")
public class SysUserToken implements Serializable {
    public static final long serialVersionUID = 1L;
    private Long userId;
    private String token;
}