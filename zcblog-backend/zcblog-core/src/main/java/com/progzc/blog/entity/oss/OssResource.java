package com.progzc.blog.entity.oss;

import com.baomidou.mybatisplus.annotation.*;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateTimeDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import org.springframework.data.annotation.Id;
import org.springframework.format.annotation.DateTimeFormat;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * @Description 云存储资源表
 * @Author zhaochao
 * @Date 2020-10-25
 * @Email zcprog@foxmail.com
 * @Version V1.0
 */
@Data
@NoArgsConstructor
@EqualsAndHashCode(callSuper = false)
@ApiModel(value = "OssResource对象", description = "云存储资源表")
public class OssResource implements Serializable {

    private static final long serialVersionUID = 1L;

    public OssResource(String url, String name) {
        this.name = name;
        this.url = url;
    }

    @ApiModelProperty(value = "主键")
    @TableId(value = "id", type = IdType.AUTO)
    @Id
    private Integer id;

    @ApiModelProperty(value = "名称")
    @NonNull
    private String name;

    @ApiModelProperty(value = "资源链接")
    @NonNull
    private String url;

    @TableField(fill = FieldFill.INSERT)
    @JsonDeserialize(using = LocalDateTimeDeserializer.class) // 解决反序列化问题
    @JsonSerialize(using = LocalDateTimeSerializer.class) // 解决序列化问题
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8") // 解决输出时的格式问题
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") // 解决前台输入时的格式问题
    @ApiModelProperty(value = "自动填充：创建时间")
    private LocalDateTime createTime;

    @TableField(fill = FieldFill.INSERT_UPDATE)
    @JsonDeserialize(using = LocalDateTimeDeserializer.class) // 解决反序列化问题
    @JsonSerialize(using = LocalDateTimeSerializer.class) // 解决序列化问题
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8") // 解决输出时的格式问题
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") // 解决前台输入时的格式问题
    @ApiModelProperty(value = "自动填充：更新时间")
    private LocalDateTime updateTime;

    @ApiModelProperty(value = "乐观锁")
    @Version
    private Integer version;

    @ApiModelProperty(value = "逻辑删除：0-未删除，1-已删除")
    @TableLogic
    private Integer deleted;
}
