package com.progzc.blog.entity.oss;

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
import lombok.experimental.Accessors;
import org.springframework.data.annotation.Id;

/**
 * @Description 云存储资源表
 * @Author zhaochao
 * @Date 2020-10-25
 * @Email zcprog@foxmail.com
 * @Version V1.0
 */
@Data
@EqualsAndHashCode(callSuper = false)
@ApiModel(value="OssResource对象", description="云存储资源表")
public class OssResource implements Serializable {

    private  static final long serialVersionUID = 1L;

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
