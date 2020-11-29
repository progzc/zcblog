package com.progzc.blog.entity.article;

import com.baomidou.mybatisplus.annotation.*;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateTimeDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;
import com.progzc.blog.common.validation.AddGroup;
import com.progzc.blog.common.validation.UpdateGroup;
import com.progzc.blog.entity.operation.Tag;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.DateFormat;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;
import org.springframework.format.annotation.DateTimeFormat;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.List;

/**
 * @Description 文章
 * @Author zhaochao
 * @Date 2020-10-25
 * @Email zcprog@foxmail.com
 * @Version V1.0
 */
@Data
@NoArgsConstructor
@EqualsAndHashCode(callSuper = false)
@ApiModel(value = "Article对象", description = "文章")
@Document(indexName = "zcblog", type = "article")
public class Article implements Serializable {

    private static final long serialVersionUID = 1L;

    @NotNull(message = "文章id不能为空", groups = UpdateGroup.class)
    @ApiModelProperty(value = "主键")
    @TableId(value = "id", type = IdType.AUTO)
    @Id
    private Integer id;

    @NotBlank(message = "文章标题不能为空", groups = {UpdateGroup.class, AddGroup.class})
    @ApiModelProperty(value = "文章标题")
    private String title;

    @NotBlank(message = "文章描述不能为空", groups = {UpdateGroup.class, AddGroup.class})
    @ApiModelProperty(value = "文章描述")
    private String description;

    @NotBlank(message = "文章作者不能为空", groups = {UpdateGroup.class, AddGroup.class})
    @ApiModelProperty(value = "文章作者")
    private String author;

    @NotBlank(message = "文章内容不能为空", groups = {UpdateGroup.class, AddGroup.class})
    @ApiModelProperty(value = "文章内容")
    private String content;

    @NotBlank(message = "文章内容格式化后不能为空", groups = {UpdateGroup.class, AddGroup.class})
    @ApiModelProperty(value = "html的content")
    private String contentFormat;

    @ApiModelProperty(value = "阅读量")
    private Integer readNum;

    @ApiModelProperty(value = "点赞量")
    private Integer likeNum;

    @ApiModelProperty(value = "是否推荐文章：0-不推荐，1-推荐")
    private Boolean recommend;

    @ApiModelProperty(value = "是否发布：0-不发布，1-发布")
    private Boolean publish;

    @ApiModelProperty(value = "是否置顶：0-不置顶，1-置顶")
    private Boolean top;

    @ApiModelProperty(value = "是否加密：0-不加密，1-加密")
    private Boolean needEncrypt;

    @Field(type = FieldType.Date, format = DateFormat.none)
    @TableField(fill = FieldFill.INSERT)
    @JsonDeserialize(using = LocalDateTimeDeserializer.class) // 解决反序列化问题
    @JsonSerialize(using = LocalDateTimeSerializer.class) // 解决序列化问题
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8") // 解决输出时的格式问题
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") // 解决前台输入时的格式问题
    @ApiModelProperty(value = "自动填充：创建时间")
    private LocalDateTime createTime;

    @Field(type = FieldType.Date, format = DateFormat.none)
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

    @TableField(exist = false)
    @ApiModelProperty(value = "文章加密的密码")
    private String password;

    @TableField(exist = false)
    @ApiModelProperty(value = "文章对应的标签")
    private List<Tag> tagList;

}
