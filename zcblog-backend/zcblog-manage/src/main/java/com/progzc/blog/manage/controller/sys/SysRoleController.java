package com.progzc.blog.manage.controller.sys;

import com.progzc.blog.common.Result;
import com.progzc.blog.common.base.AbstractController;
import com.progzc.blog.common.constants.SysConstants;
import com.progzc.blog.common.utils.ValidatorUtils;
import com.progzc.blog.common.validation.AddGroup;
import com.progzc.blog.common.validation.UpdateGroup;
import com.progzc.blog.entity.MyPage;
import com.progzc.blog.entity.sys.SysRole;
import com.progzc.blog.manage.service.sys.SysRoleMenuService;
import com.progzc.blog.manage.service.sys.SysRoleService;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Collection;
import java.util.HashMap;
import java.util.List;
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
    private ValidatorUtils validatorUtils;

    @Autowired
    private SysRoleService sysRoleService;

    @Autowired
    private SysRoleMenuService sysRoleMenuService;

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

    /**
     * 分页查询当前用户所创建的角色列表
     * @param params
     * @return
     */
    @GetMapping("/list")
    @RequiresPermissions("sys:role:list")
    @ApiOperation(value = "查询当前用户所创建的角色列表")
    public Result list(@RequestParam Map<String, Object> params) {
        // 若不是超级管理员，则只查询自己所创建的角色列表
        if (!SysConstants.SUPER_ADMIN.equals(getUserId())) {
            params.put("createUserId", getUserId());
        }
        MyPage page = sysRoleService.queryPage(params);
        return Result.ok().put("page", page);
    }

    /**
     * 删除角色
     * @param roleIds
     * @return
     */
    @DeleteMapping("/delete")
    @RequiresPermissions("sys:role:delete")
    @ApiOperation(value = "删除角色")
    public Result delete(@RequestBody Long[] roleIds) {
        sysRoleService.deleteBatch(roleIds);
        return Result.ok();

    }

    /**
     * 获取角色信息
     * @param roleId
     * @return
     */
    @GetMapping("/info/{roleId}")
    @RequiresPermissions("sys:role:info")
    @ApiOperation(value = "获取角色信息")
    public Result info(@PathVariable Long roleId) {
        SysRole sysRole = sysRoleService.getById(roleId);
        List<Long> menuIdList = sysRoleMenuService.queryMenuIdList(roleId);
        sysRole.setMenuIdList(menuIdList);
        return Result.ok().put("role", sysRole);
    }

    /**
     * 新增角色
     * @param sysRole
     * @return
     */
    @PostMapping("/save")
    @RequiresPermissions("sys:role:save")
    @ApiOperation(value = "新增角色")
    public Result save(@RequestBody SysRole sysRole) {
        validatorUtils.validateEntity(sysRole, AddGroup.class);
        sysRole.setCreateUserId(getUserId());
        sysRoleService.saveRoleInfo(sysRole);
        return Result.ok();
    }

    /**
     * 更新角色
     * @param sysRole
     * @return
     */
    @PutMapping("/update")
    @RequiresPermissions("sys:role:update")
    @ApiOperation(value = "更新角色")
    public Result update(@RequestBody SysRole sysRole) {
        validatorUtils.validateEntity(sysRole, UpdateGroup.class);
        sysRole.setCreateUserId(getUserId());
        sysRoleService.updateRoleInfo(sysRole);
        return Result.ok();
    }

}
