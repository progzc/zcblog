package com.progzc.blog.entity.sys.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;

/**
 * @Description 修改密码的表单对象
 * @Author zhaochao
 * @Date 2020/11/18 15:50
 * @Email zcprog@foxmail.com
 * @Version V1.0
 */
@Data
@EqualsAndHashCode(callSuper = false)
@ApiModel(value = "PasswordFormVO对象", description = "前台修改密码")
public class PasswordForm implements Serializable {

    private static final long serialVersionUID = 5630496926025447983L;

    @ApiModelProperty(value = "原密码")
    private String password;

    @ApiModelProperty(value = "新密码")
    private String newPassword;
}
