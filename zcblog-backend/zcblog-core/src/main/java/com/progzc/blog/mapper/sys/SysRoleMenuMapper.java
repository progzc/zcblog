package com.progzc.blog.mapper.sys;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.progzc.blog.entity.sys.SysRoleMenu;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * @Description 角色与菜单对应关系
 * @Author zhaochao
 * @Date 2020-10-25
 * @Email zcprog@foxmail.com
 * @Version V1.0
 */
@Mapper
public interface SysRoleMenuMapper extends BaseMapper<SysRoleMenu> {

    /**
     * 根据角色id获取菜单id列表
     * @param roleId
     * @return
     */
    List<Long> queryMenuIdList(Long roleId);
}
