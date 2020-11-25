package com.progzc.blog.entity.sys.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

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
    private String username;

    @ApiModelProperty(value = "登录密码")
    private String password;

    @ApiModelProperty(value = "随机uuid")
    private String uuid;

    @ApiModelProperty(value = "验证码")
    private String captcha;
}
