package com.progzc.blog.entity.sys;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateTimeDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;
import com.progzc.blog.common.validation.AddGroup;
import com.progzc.blog.common.validation.UpdateGroup;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.format.annotation.DateTimeFormat;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.List;

/**
 * @Description 角色
 * @Author zhaochao
 * @Date 2020-10-25
 * @Email zcprog@foxmail.com
 * @Version V1.0
 */
@Data
@NoArgsConstructor
@EqualsAndHashCode(callSuper = false)
@ApiModel(value = "SysRole对象", description = "角色")
public class SysRole implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "role_id", type = IdType.AUTO)
    @Id
    @NotNull(message = "角色id不能为空", groups = UpdateGroup.class)
    @ApiModelProperty(value = "主键")
    private Long roleId;

    @ApiModelProperty(value = "角色名称")
    @NotBlank(message = "角色不能为空", groups = {UpdateGroup.class, AddGroup.class})
    private String roleName;

    @NotBlank(message = "角色备注信息不能为空", groups = {UpdateGroup.class, AddGroup.class})
    @ApiModelProperty(value = "备注")
    private String remark;

    @ApiModelProperty(value = "创建者ID")
    private Long createUserId;

    @TableField(fill = FieldFill.INSERT)
    @JsonDeserialize(using = LocalDateTimeDeserializer.class) // 解决反序列化问题
    @JsonSerialize(using = LocalDateTimeSerializer.class) // 解决序列化问题
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8") // 解决输出时的格式问题
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") // 解决前台输入时的格式问题
    @ApiModelProperty(value = "自动填充：创建时间")
    private LocalDateTime createTime;


    @TableField(exist = false)
    @NotNull(message = "菜单id列表为空", groups = {UpdateGroup.class, AddGroup.class})
    @Size(min = 1, message = "菜单id列表长度为0", groups = {UpdateGroup.class, AddGroup.class})
    @ApiModelProperty(value = "菜单id列表")
    private List<Long> menuIdList;

}
