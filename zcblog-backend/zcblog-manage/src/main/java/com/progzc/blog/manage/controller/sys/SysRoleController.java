package com.progzc.blog.manage.controller.sys;

import com.progzc.blog.common.Result;
import com.progzc.blog.common.base.AbstractController;
import com.progzc.blog.common.constants.SysConstants;
import com.progzc.blog.entity.sys.SysRole;
import com.progzc.blog.manage.service.sys.SysRoleService;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

/**
 * @Description 角色
 * @Author zhaochao
 * @Date 2020-10-25
 * @Email zcprog@foxmail.com
 * @Version V1.0
 */
@Slf4j
@RestController
@RequestMapping("/admin/sys/role")
public class SysRoleController extends AbstractController {

    @Autowired
    private SysRoleService sysRoleService;

    /**
     * 查询当前用户所创建的所有角色
     * @return
     */
    @GetMapping("/select")
    @RequiresPermissions("sys:role:info")
    @ApiOperation(value = "查询当前用户所创建的所有角色")
    public Result select() {
        Map<String, Object> map = new HashMap<>();
        // 若不是超级管理员，则只查询自己所创建的角色列表
        if (!SysConstants.SUPER_ADMIN.equals(getUserId())) {
            map.put("createUserId", getUserId());
        }
        Collection<SysRole> list = sysRoleService.listByMap(map);
        return Result.ok().put("list", list);
    }
}

