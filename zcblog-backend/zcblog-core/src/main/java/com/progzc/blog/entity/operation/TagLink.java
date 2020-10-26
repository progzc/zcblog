package com.progzc.blog.entity.operation;

import com.baomidou.mybatisplus.annotation.IdType;
import java.util.Date;
import com.baomidou.mybatisplus.annotation.Version;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableLogic;
import com.baomidou.mybatisplus.annotation.TableField;
import java.io.Serializable;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NonNull;
import org.springframework.data.annotation.Id;

/**
 * @Description 标签多对多维护表
 * @Author zhaochao
 * @Date 2020-10-25
 * @Email zcprog@foxmail.com
 * @Version V1.0
 */
@Data
@EqualsAndHashCode(callSuper = false)
@ApiModel(value="TagLink对象", description="标签多对多维护表")
public class TagLink implements Serializable {

    private  static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "主键")
    @TableId(value = "id", type = IdType.AUTO)
    @Id
    private Integer id;

    @ApiModelProperty(value = "标签Id")
    @NonNull
    private Integer tagId;

    @ApiModelProperty(value = "关联Id")
    @NonNull
    private Integer linkId;

    @ApiModelProperty(value = "所属类别：0-文章，1-相册")
    @NonNull
    private Integer type;

    @ApiModelProperty(value = "自动填充：创建时间")
    @TableField(fill = FieldFill.INSERT)
    private Date createTime;

    @ApiModelProperty(value = "自动填充：更新时间")
    @TableField(fill = FieldFill.INSERT_UPDATE)
    private Date updateTime;

    @ApiModelProperty(value = "乐观锁")
    @Version
    private Integer version;

    @ApiModelProperty(value = "逻辑删除：0-未删除，1-已删除")
    @TableLogic
    private Integer deleted;

}
