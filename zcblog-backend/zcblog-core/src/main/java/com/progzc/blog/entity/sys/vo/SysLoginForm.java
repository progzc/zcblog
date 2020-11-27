package com.progzc.blog.entity.sys.vo;

import com.progzc.blog.common.validation.annotation.MyPassword;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;

/**
 * @Description 封装表单登录数据
 * @Author zhaochao
 * @Date 2020/11/8 20:49
 * @Email zcprog@foxmail.com
 * @Version V1.0
 */
@Data
@NoArgsConstructor
@EqualsAndHashCode(callSuper = false)
@ApiModel(value = "登录表单提交数据")
public class SysLoginForm {

    @ApiModelProperty(value = "登录用户名")
    @MyPassword(message = "用户名格式不正确")
    @NotBlank(message = "用户名不能为空")
    private String username;

    @NotBlank(message = "登录密码不能为空")
    @MyPassword(message = "登录密码格式不正确")
    @ApiModelProperty(value = "登录密码")
    private String password;

    @NotBlank(message = "随机UUID不能为空")
    @ApiModelProperty(value = "随机uuid")
    private String uuid;

    @NotBlank(message = "验证码不能为空")
    @ApiModelProperty(value = "验证码")
    private String captcha;
}
