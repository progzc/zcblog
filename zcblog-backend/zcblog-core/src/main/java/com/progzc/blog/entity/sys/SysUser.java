package com.progzc.blog.entity.sys;

import com.baomidou.mybatisplus.annotation.IdType;
import java.util.Date;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
import java.io.Serializable;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springframework.data.annotation.Id;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;

/**
 * @Description 用户
 * @Author zhaochao
 * @Date 2020-10-25
 * @Email zcprog@foxmail.com
 * @Version V1.0
 */
@Data
@EqualsAndHashCode(callSuper = false)
@ApiModel(value="SysUser对象", description="用户")
public class SysUser implements Serializable {

    private  static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "主键")
    @TableId(value = "user_id", type = IdType.AUTO)
    @Id
    private Long userId;

    @ApiModelProperty(value = "用户名")
    @NotBlank(message = "用户名不能为空") // 校验
    private String username;

    @ApiModelProperty(value = "用户密码")
    @NotBlank(message = "密码不能为空") // 校验
    private String password;

    @ApiModelProperty(value = "用户邮箱")
    @Email(message = "邮箱格式不正确") // 校验（主要在前台进行校验，降低服务器压力）
    @NotBlank(message = "邮箱不能为空") // 校验
    private String email;

    @ApiModelProperty(value = "盐")
    private String salt;

    @ApiModelProperty(value = "创建者的user_id")
    private Long createUserId;

    @ApiModelProperty(value = "用户状态：0-禁用，1-正常")
    private Boolean status;

    @ApiModelProperty(value = "用户创建时间")
    @TableField(fill = FieldFill.INSERT)
    private Date createTime;

}
