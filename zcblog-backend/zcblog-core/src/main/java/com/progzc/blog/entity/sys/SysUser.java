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
import com.progzc.blog.common.validation.annotation.MyEmail;
import com.progzc.blog.common.validation.annotation.MyPassword;
import com.progzc.blog.common.validation.annotation.MyPhone;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.format.annotation.DateTimeFormat;

import javax.validation.constraints.NotNull;
import javax.validation.groups.Default;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.List;

/**
 * @Description 用户
 * @Author zhaochao
 * @Date 2020-10-25
 * @Email zcprog@foxmail.com
 * @Version V1.0
 */
@Data
@NoArgsConstructor
@EqualsAndHashCode(callSuper = false)
@ApiModel(value = "SysUser对象", description = "用户")
public class SysUser implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "主键")
    @TableId(value = "user_id", type = IdType.AUTO)
    @NotNull(message = "用户id为空", groups = {UpdateGroup.class, Default.class})
    @Id
    private Long userId;

    @ApiModelProperty(value = "用户名")
    @MyPassword(message = "用户名格式不正确", groups = {AddGroup.class, UpdateGroup.class, Default.class})
    private String username;

    @ApiModelProperty(value = "用户密码")
    @MyPassword(message = "密码格式不正确", groups = {AddGroup.class, UpdateGroup.class})
    private String password;

    @ApiModelProperty(value = "用户邮箱")
    @MyEmail(message = "邮箱格式不正确", groups = {AddGroup.class, UpdateGroup.class, Default.class})
    private String email;

    @ApiModelProperty(value = "用户手机号")
    @MyPhone(message = "手机号格式不正确", groups = {AddGroup.class, UpdateGroup.class, Default.class})
    private String phone;

    @ApiModelProperty(value = "盐")
    private String salt;

    @ApiModelProperty(value = "创建者的user_id")
    private Long createUserId;

    @ApiModelProperty(value = "用户状态：0-禁用，1-正常")
    private Boolean status;

    @TableField(fill = FieldFill.INSERT)
    @JsonDeserialize(using = LocalDateTimeDeserializer.class) // 解决反序列化问题
    @JsonSerialize(using = LocalDateTimeSerializer.class) // 解决序列化问题
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8") // 解决输出时的格式问题
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") // 解决前台输入时的格式问题
    @ApiModelProperty(value = "自动填充：创建时间")
    private LocalDateTime createTime;

    @TableField(exist = false)
    private List<Long> roleIdList;

}
