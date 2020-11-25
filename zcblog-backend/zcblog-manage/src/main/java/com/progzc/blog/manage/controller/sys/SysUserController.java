package com.progzc.blog.manage.controller.sys;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.progzc.blog.common.Result;
import com.progzc.blog.common.base.AbstractController;
import com.progzc.blog.common.constants.SysConstants;
import com.progzc.blog.common.utils.EncryptUtils;
import com.progzc.blog.common.utils.ValidatorUtils;
import com.progzc.blog.common.validation.AddGroup;
import com.progzc.blog.common.validation.UpdateGroup;
import com.progzc.blog.entity.MyPage;
import com.progzc.blog.entity.sys.SysUser;
import com.progzc.blog.entity.sys.vo.PasswordForm;
import com.progzc.blog.entity.sys.vo.SysUserVO;
import com.progzc.blog.manage.service.sys.SysUserRoleService;
import com.progzc.blog.manage.service.sys.SysUserService;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.ArrayUtils;
import org.apache.commons.lang.RandomStringUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.apache.shiro.crypto.hash.Sha256Hash;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.groups.Default;
import java.util.List;
import java.util.Map;

/**
 * @Description 用户控制器
 * @Author zhaochao
 * @Date 2020-10-25
 * @Email zcprog@foxmail.com
 * @Version V1.0
 */
@Slf4j
@RestController
@RequestMapping("/admin/sys/user")
public class SysUserController extends AbstractController {

    @Autowired
    private ValidatorUtils validatorUtils;

    @Autowired
    private SysUserService sysUserService;

    @Autowired
    private SysUserRoleService sysUserRoleService;

    /**
     * 获取登录用户信息
     * @return
     */
    @GetMapping("/info")
    @ApiOperation(value = "获取登录用户信息")
    public Result info() {
        SysUser sysUser = getUser();
        SysUserVO sysUserVO = new SysUserVO();
        BeanUtils.copyProperties(sysUser, sysUserVO);
        sysUserVO.setUsername(EncryptUtils.encrypt(sysUserVO.getUsername())); // 用户名加密传输
        return Result.ok().put("user", sysUserVO);
    }

    /**
     * 修改用户密码
     * @param passwordForm
     * @return
     */
    @PutMapping("/password")
    @ApiOperation(value = "用户修改密码")
    public Result password(@RequestBody PasswordForm passwordForm) {
        // 解密
        String psd = EncryptUtils.decrypt(passwordForm.getPassword());
        String newPsd = EncryptUtils.decrypt(passwordForm.getNewPassword());

        // 校验密码格式
        passwordForm.setPassword(psd);
        passwordForm.setNewPassword(newPsd);
        validatorUtils.validateEntity(passwordForm, Default.class);

        // 设置新密码
        String password = new Sha256Hash(psd, getUser().getSalt()).toHex();
        String newPassword = new Sha256Hash(newPsd, getUser().getSalt()).toHex();
        boolean flag = sysUserService.updatePassword(getUserId(), password, newPassword);
        if (!flag) {
            return Result.error("原密码不正确");
        }
        return Result.ok();
    }

    /**
     * 查询用户列表
     * @param params
     * @return
     */
    @GetMapping("/list")
    @RequiresPermissions("sys:user:list")
    @ApiOperation(value = "查询用户列表")
    public Result list(@RequestParam Map<String, Object> params) {
        params.put("createUserId", getUserId());
        MyPage page = sysUserService.queryPage(params);
        // 若list不为空，则将用户名加密传输
        List<SysUser> list = (List<SysUser>) page.getList();
        if (CollectionUtils.isNotEmpty(list)) {
            list.stream().forEach(sysUser -> sysUser.setUsername(EncryptUtils.encrypt(sysUser.getUsername())));
            page.setList(list);
        }
        return Result.ok().put("page", page);
    }

    /**
     * 删除用户
     * @param userIds
     * @return
     */
    @DeleteMapping("/delete")
    @RequiresPermissions("sys:user:delete")
    @ApiOperation(value = "删除用户")
    public Result delete(@RequestBody Long[] userIds) {
        if (ArrayUtils.contains(userIds, SysConstants.SUPER_ADMIN)) {
            return Result.error("系统管理员不能删除");
        }
        if (ArrayUtils.contains(userIds, getUserId())) {
            return Result.error("当前用户不能删除");
        }
        sysUserService.deleteBatch(userIds);
        return Result.ok();
    }

    /**
     * 根据用户id获取用户+角色信息
     * @param userId
     * @return
     */
    @GetMapping("/info/{userId}")
    @RequiresPermissions("sys:user:info")
    @ApiOperation("根据用户id获取用户信息")
    public Result info(@PathVariable("userId") Long userId) {
        // 查询用户
        SysUser sysUser = sysUserService.getOne(new QueryWrapper<SysUser>().lambda()
                // 只返回用户id/用户名/手机号/邮箱/用户状态
                .select(SysUser::getUserId, SysUser::getUsername, SysUser::getPhone, SysUser::getEmail, SysUser::getStatus)
                .eq(SysUser::getUserId, userId));
        // 加密用户名
        sysUser.setUsername(EncryptUtils.encrypt(sysUser.getUsername()));
        // 查询用户的角色
        List<Long> roleIdList = sysUserRoleService.queryRoleIdList(userId);
        // 封装返回的结果
        sysUser.setRoleIdList(roleIdList);

        return Result.ok().put("user", sysUser);
    }

    /**
     * 新增用户
     * @param sysUser
     * @return
     */
    @PostMapping("/save")
    @RequiresPermissions("sys:user:save")
    @ApiOperation("新增用户")
    public Result save(@RequestBody SysUser sysUser) {
        // 解密
        String username = EncryptUtils.decrypt(sysUser.getUsername());
        String password = EncryptUtils.decrypt(sysUser.getPassword());

        // 校验用户名/密码/手机号/邮箱
        sysUser.setUsername(username);
        sysUser.setPassword(password);
        // 校验 用户名 + 密码 + 手机号 + 邮箱
        validatorUtils.validateEntity(sysUser, AddGroup.class);

        // 设置createUserId
        sysUser.setCreateUserId(getUserId());
        // 设置密码盐及加密密码
        String salt = RandomStringUtils.randomAlphanumeric(20);
        sysUser.setSalt(salt);
        sysUser.setPassword(new Sha256Hash(sysUser.getPassword(), salt).toHex());

        sysUserService.saveNewUser(sysUser);
        return Result.ok();
    }

    /**
     * 更新用户信息
     * @param sysUser
     * @return
     */
    @PutMapping("/update")
    @RequiresPermissions("sys:user:update")
    @ApiOperation(value = "更新用户")
    public Result update(@RequestBody SysUser sysUser) {
        // 解密
        String username = EncryptUtils.decrypt(sysUser.getUsername());
        String password = EncryptUtils.decrypt(sysUser.getPassword());

        sysUser.setUsername(username);
        if (StringUtils.isBlank(password)) { // 若密码为""，则表示不更新密码
            sysUser.setPassword(null);
            // 校验 用户id + 用户名 + 手机号 + 邮箱
            validatorUtils.validateEntity(sysUser, Default.class);
        } else { // 若密码不为""，则表示更新密码
            sysUser.setPassword(password);
            // 校验 用户id + 用户名 + 密码 + 手机号 + 邮箱
            validatorUtils.validateEntity(sysUser, UpdateGroup.class);
            // 设置密码盐及加密密码
            String salt = RandomStringUtils.randomAlphanumeric(20);
            sysUser.setSalt(salt);
            sysUser.setPassword(new Sha256Hash(sysUser.getPassword(), salt).toHex());
        }
        sysUser.setCreateUserId(getUserId());

        sysUserService.updateUser(sysUser);
        return Result.ok();
    }
}
