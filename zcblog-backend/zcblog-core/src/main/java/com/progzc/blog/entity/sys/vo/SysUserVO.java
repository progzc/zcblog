package com.progzc.blog.entity.sys.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;

/**
 * @Description 返回前台的用户类
 * @Author zhaochao
 * @Date 2020/11/17 15:40
 * @Email zcprog@foxmail.com
 * @Version V1.0
 */

@Data
@EqualsAndHashCode(callSuper = false)
@ApiModel(value = "SysUserVO对象", description = "前台用户")
public class SysUserVO implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "用户id")
    private Long userId;

    @ApiModelProperty(value = "用户名")
    private String username;
}
