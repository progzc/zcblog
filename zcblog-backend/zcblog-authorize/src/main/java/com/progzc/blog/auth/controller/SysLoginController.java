package com.progzc.blog.auth.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.IOUtils;
import com.progzc.blog.auth.service.SysCaptchaService;
import com.progzc.blog.auth.service.SysUserTokenService;
import com.progzc.blog.common.Result;
import com.progzc.blog.common.base.AbstractController;
import com.progzc.blog.common.enums.ErrorEnum;
import com.progzc.blog.common.utils.EncryptUtils;
import com.progzc.blog.common.utils.ValidatorUtils;
import com.progzc.blog.entity.sys.SysUser;
import com.progzc.blog.entity.sys.vo.SysLoginForm;
import com.progzc.blog.mapper.sys.SysUserMapper;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.crypto.hash.Sha256Hash;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import sun.misc.BASE64Encoder;

import javax.annotation.Resource;
import javax.imageio.ImageIO;
import javax.validation.groups.Default;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

/**
 * @Description 系统登录控制器
 * @Author zhaochao
 * @Date 2020/11/8 15:27
 * @Email zcprog@foxmail.com
 * @Version V1.0
 */
@Slf4j
@RestController
public class SysLoginController extends AbstractController {

    @Autowired
    private ValidatorUtils validatorUtils;

    @Autowired
    private SysCaptchaService sysCaptchaService;

    @Resource
    private SysUserMapper sysUserMapper;

    @Autowired
    private SysUserTokenService sysUserTokenService;
    
    /**
     * 获取验证码
     * @param uuid
     * @return
     * @throws IOException
     */
    @GetMapping("/captcha.jpg")
    @ApiOperation(value = "获取验证码")
    public Result captcha(String uuid) throws IOException {
        BufferedImage image = sysCaptchaService.getCaptcha(uuid);
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        ImageIO.write(image, "jpg", out);

        BASE64Encoder encoder = new BASE64Encoder();
        String byteImage = encoder.encode(out.toByteArray()); // 将图片转为Base64进行传输
        IOUtils.closeQuietly(out);

        return Result.ok().put("captchaPath", byteImage);
    }

    /**
     * 提交表单，进行登录
     * @param form
     * @return
     */
    @PostMapping("/admin/sys/login")
    @ApiOperation(value = "提交表单，进行登录")
    public Result login(@RequestBody SysLoginForm form) {
        // 进行AES解密
        String username = EncryptUtils.decrypt(form.getUsername());
        String password = EncryptUtils.decrypt(form.getPassword());
        form.setUsername(username);
        form.setPassword(password);
        // 校验用户名/密码格式
        validatorUtils.validateEntity(form, Default.class);

        // 校验验证码
        boolean flag = sysCaptchaService.validate(form.getUuid(), form.getCaptcha());
        if (!flag) {
            log.error(ErrorEnum.CAPTCHA_WRONG.getMsg());
            return Result.error(ErrorEnum.CAPTCHA_WRONG);
        }

        SysUser sysUser = sysUserMapper.selectOne(new QueryWrapper<SysUser>()
                .lambda()
                .eq(SysUser::getUsername, username));
        // 用户不存在或密码不正确
        if (sysUser == null || !sysUser.getPassword().equals(new Sha256Hash(password, sysUser.getSalt()).toString())) {
            log.error(ErrorEnum.USERNAME_OR_PASSWORD_WRONG.getMsg());
            return Result.error(ErrorEnum.USERNAME_OR_PASSWORD_WRONG);
        }

        // 用户被禁用
        if (Boolean.FALSE.equals(sysUser.getStatus())) {
            log.error(ErrorEnum.USER_ACCOUNT_LOCKED.getMsg());
            return Result.error(ErrorEnum.USER_ACCOUNT_LOCKED);
        }

        // 校验成功，生成token
        return sysUserTokenService.createToken(sysUser.getUserId());
    }

    /**
     * 退出登录
     * @return
     */
    @PostMapping("/admin/sys/logout")
    @ApiOperation(value = "退出登录")
    public Result logout() {
        sysUserTokenService.logout(getUserId());
        return Result.ok();
    }
}
