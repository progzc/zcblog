package com.progzc.blog.entity.sys;

import com.baomidou.mybatisplus.annotation.FieldStrategy;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;

import java.io.Serializable;
import java.util.List;

/**
 * @Description 菜单管理
 * @Author zhaochao
 * @Date 2020-10-25
 * @Email zcprog@foxmail.com
 * @Version V1.0
 */
@Data
@NoArgsConstructor
@EqualsAndHashCode(callSuper = false)
@ApiModel(value = "SysMenu对象", description = "菜单管理")
public class SysMenu implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "主键,菜单id")
    @TableId(value = "menu_id", type = IdType.AUTO)
    @Id
    private Long menuId;

    @ApiModelProperty(value = "父级菜单id")
    private Long parentId;

    @ApiModelProperty(value = "菜单名称")
    private String name;

    @ApiModelProperty(value = "路由地址")
    private String url;

    @ApiModelProperty(value = "组件路径")
    private String component;

    @ApiModelProperty(value = "权限")
    @TableField(strategy = FieldStrategy.IGNORED)
    private String perms;

    @ApiModelProperty(value = "菜单类型：0-目录，1-菜单，2-按钮")
    private Integer type;

    @ApiModelProperty(value = "菜单图标")
    private String icon;

    @ApiModelProperty(value = "同级菜单排序")
    private Integer orderNum;

    /**
     * 父级菜单名称
     */
    @TableField(exist = false)
    private String parentName;

    /**
     * z-tree属性
     */
    @TableField(exist = false)
    private Boolean open;

    /**
     * 子菜单
     */
    @TableField(exist = false)
    private List<?> list;

}
