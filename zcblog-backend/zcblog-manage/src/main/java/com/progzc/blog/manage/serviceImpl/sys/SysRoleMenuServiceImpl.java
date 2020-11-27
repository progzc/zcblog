package com.progzc.blog.manage.serviceImpl.sys;

import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.progzc.blog.entity.sys.SysRoleMenu;
import com.progzc.blog.manage.service.sys.SysRoleMenuService;
import com.progzc.blog.mapper.sys.SysRoleMenuMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * @Description 角色与菜单对应关系
 * @Author zhaochao
 * @Date 2020-10-25
 * @Email zcprog@foxmail.com
 * @Version V1.0
 */
@Service
public class SysRoleMenuServiceImpl extends ServiceImpl<SysRoleMenuMapper, SysRoleMenu> implements SysRoleMenuService {

    @Resource
    private SysRoleMenuMapper sysRoleMenuMapper;

    /**
     * 删除角色菜单关系
     * @param roleIds
     */
    @Override
    public void deleteBatchByRoleId(Long[] roleIds) {
        Arrays.stream(roleIds).forEach(roleId -> {
            if (roleId != null) {
                sysRoleMenuMapper.delete(new UpdateWrapper<SysRoleMenu>().lambda()
                        .eq(SysRoleMenu::getRoleId, roleId));
            }
        });
    }

    /**
     * 根据角色id获取菜单id列表
     * @param roleId
     * @return
     */
    @Override
    public List<Long> queryMenuIdList(Long roleId) {
        return sysRoleMenuMapper.queryMenuIdList(roleId);
    }

    /**
     * 新增或更新角色菜单关系
     * @param roleId
     * @param menuIdList
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void saveOrUpdateRoleInfo(Long roleId, List<Long> menuIdList) {
        // 先删除角色菜单关系
        if (roleId != null) {
            sysRoleMenuMapper.delete(new UpdateWrapper<SysRoleMenu>().lambda()
                    .eq(SysRoleMenu::getRoleId, roleId));
        }

        // 保存角色与菜单关系
        List<SysRoleMenu> list = new ArrayList<>(menuIdList.size());
        menuIdList.stream().forEach(menuId -> {
            SysRoleMenu sysRoleMenu = new SysRoleMenu();
            sysRoleMenu.setMenuId(menuId);
            sysRoleMenu.setRoleId(roleId);
            list.add(sysRoleMenu);
        });
        this.saveBatch(list);
    }
}
