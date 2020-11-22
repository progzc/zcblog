package com.progzc.blog.manage.controller.sys;

import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.progzc.blog.common.Result;
import com.progzc.blog.common.base.AbstractController;
import com.progzc.blog.common.utils.EncryptUtils;
import com.progzc.blog.entity.sys.SysUser;
import com.progzc.blog.entity.sys.vo.PasswordForm;
import com.progzc.blog.entity.sys.vo.SysUserVO;
import com.progzc.blog.manage.service.sys.SysUserService;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.crypto.hash.Sha256Hash;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

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
    private SysUserService sysUserService;

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
        if (StringUtils.isEmpty(passwordForm.getPassword())) {
            return Result.error("原密码不能为空");
        }
        if (StringUtils.isEmpty(passwordForm.getNewPassword())) {
            return Result.error("新密码不能为空");
        }

        // 解密
        String psd = EncryptUtils.decrypt(passwordForm.getPassword());
        String newPsd = EncryptUtils.decrypt(passwordForm.getNewPassword());

        // 设置新密码
        String password = new Sha256Hash(psd, getUser().getSalt()).toHex();
        String newPassword = new Sha256Hash(newPsd, getUser().getSalt()).toHex();
        boolean flag = sysUserService.updatePassword(getUserId(), password, newPassword);
        if (!flag) {
            return Result.error("原密码不正确");
        }
        return Result.ok();
    }
}

