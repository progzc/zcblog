package com.progzc.blog.manage.service.sys;

import com.baomidou.mybatisplus.extension.service.IService;
import com.progzc.blog.entity.sys.SysRoleMenu;

import java.util.List;

/**
 * @Description 角色与菜单对应关系
 * @Author zhaochao
 * @Date 2020-10-25
 * @Email zcprog@foxmail.com
 * @Version V1.0
 */
public interface SysRoleMenuService extends IService<SysRoleMenu> {

    /**
     * 删除角色菜单关系
     * @param roleIds
     */
    void deleteBatchByRoleId(Long[] roleIds);

    /**
     * 根据角色id获取菜单id列表
     * @param roleId
     * @return
     */
    List<Long> queryMenuIdList(Long roleId);

    /**
     * 新增或更新角色菜单关系
     * @param roleId
     * @param menuIdList
     */
    void saveOrUpdateRoleInfo(Long roleId, List<Long> menuIdList);

}
