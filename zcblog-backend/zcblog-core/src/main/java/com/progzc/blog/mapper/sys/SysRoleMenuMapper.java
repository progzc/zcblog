package com.progzc.blog.mapper.sys;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.progzc.blog.entity.sys.SysRoleMenu;
import org.apache.ibatis.annotations.Mapper;

/**
 * @Description 角色与菜单对应关系
 * @Author zhaochao
 * @Date 2020-10-25
 * @Email zcprog@foxmail.com
 * @Version V1.0
 */
@Mapper
public interface SysRoleMenuMapper extends BaseMapper<SysRoleMenu> {

}
