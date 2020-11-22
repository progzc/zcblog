package com.progzc.blog.manage.controller.sys;

import com.progzc.blog.auth.service.ShiroService;
import com.progzc.blog.common.Result;
import com.progzc.blog.common.base.AbstractController;
import com.progzc.blog.entity.sys.SysMenu;
import com.progzc.blog.manage.service.sys.SysMenuService;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Set;

/**
 * @Description 菜单管理
 * @Author zhaochao
 * @Date 2020-10-25
 * @Email zcprog@foxmail.com
 * @Version V1.0
 */
@Slf4j
@RestController
@RequestMapping("/admin/sys/menu")
public class SysMenuController extends AbstractController {

    @Autowired
    private SysMenuService sysMenuService;

    @Autowired
    private ShiroService shiroService;

    @GetMapping("/nav")
    @ApiOperation(value = "查询导航菜单")
    public Result nav() {
        List<SysMenu> menuList = sysMenuService.listUserMenu(getUserId());
        Set<String> perms = shiroService.getUserPerms(getUserId());
        return Result.ok().put("menuList", menuList).put("perms", perms);
    }
}

