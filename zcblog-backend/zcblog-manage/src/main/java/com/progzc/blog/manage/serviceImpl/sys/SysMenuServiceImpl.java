package com.progzc.blog.manage.serviceImpl.sys;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.progzc.blog.common.constants.SysConstants;
import com.progzc.blog.common.enums.MenuTypeEnum;
import com.progzc.blog.entity.sys.SysMenu;
import com.progzc.blog.manage.service.sys.SysMenuService;
import com.progzc.blog.manage.service.sys.SysUserService;
import com.progzc.blog.mapper.sys.SysMenuMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

/**
 * @Description 菜单管理
 * @Author zhaochao
 * @Date 2020-10-25
 * @Email zcprog@foxmail.com
 * @Version V1.0
 */
@Service
public class SysMenuServiceImpl extends ServiceImpl<SysMenuMapper, SysMenu> implements SysMenuService {

    @Resource
    private SysMenuMapper sysMenuMapper;

    @Autowired
    private SysUserService sysUserService;

    /**
     * 获取用户的所有菜单
     * @param userId
     * @return
     */
    @Override
    public List<SysMenu> listUserMenu(Long userId) {
        // 若是系统管理员，则拥有最高权限
        if (SysConstants.SUPER_ADMIN.equals(userId)) {
            return getAllMenuList(null);
        }
        // 若不是系统管理员
        List<Long> menuIdList = sysUserService.queryAllMenuId(userId);

        return getAllMenuList(menuIdList);
    }

    private List<SysMenu> getAllMenuList(List<Long> menuIdList) {
        // 查询用户所属所有目录
        List<SysMenu> menuList = queryListParentId(0L, menuIdList);
        // 递归生成特定格式的菜单列表
        getMenuTreeList(menuList, menuIdList);
        return menuList;
    }

    // 递归
    private List<SysMenu> getMenuTreeList(List<SysMenu> menuList, List<Long> menuIdList) {
        List<SysMenu> subMenuList = new ArrayList<>();
        for (SysMenu menu : menuList) {
            if (menu.getType() == MenuTypeEnum.CATALOG.getValue()) {
                menu.setList(getMenuTreeList(queryListParentId(menu.getMenuId(), menuIdList), menuIdList));
            }
            subMenuList.add(menu);
        }
        return subMenuList;
    }

    /**
     * 根据父菜单，查询子菜单
     * @param parentId
     * @param menuIdList
     * @return
     */
    private List<SysMenu> queryListParentId(Long parentId, List<Long> menuIdList) {
        List<SysMenu> menuList = queryListParentId(parentId);
        if (menuIdList == null) {
            return menuList;
        }

        List<SysMenu> userMenuList = new ArrayList<>();
        for (SysMenu menu : menuList) {
            if (menuIdList.contains(menu.getMenuId())) {
                userMenuList.add(menu);
            }
        }
        return userMenuList;
    }

    /**
     * 根据父菜单，查询子菜单
     * @param parentId
     * @return
     */
    private List<SysMenu> queryListParentId(Long parentId) {
        return sysMenuMapper.queryListParentId(parentId);
    }

}
