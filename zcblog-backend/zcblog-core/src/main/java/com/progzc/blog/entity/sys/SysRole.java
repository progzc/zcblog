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

import javax.validation.constraints.NotBlank;

/**
 * @Description 角色
 * @Author zhaochao
 * @Date 2020-10-25
 * @Email zcprog@foxmail.com
 * @Version V1.0
 */
@Data
@EqualsAndHashCode(callSuper = false)
@ApiModel(value="SysRole对象", description="角色")
public class SysRole implements Serializable {

    private  static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "主键")
    @TableId(value = "role_id", type = IdType.AUTO)
    @Id
    private Long roleId;

    @ApiModelProperty(value = "角色名称")
    @NotBlank(message = "角色不能为空") // 校验
    private String roleName;

    @ApiModelProperty(value = "备注")
    private String remark;

    @ApiModelProperty(value = "创建者ID")
    private Long createUserId;

    @ApiModelProperty(value = "用户创建时间")
    @TableField(fill = FieldFill.INSERT)
    private Date createTime;

}
