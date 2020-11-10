package com.progzc.blog.auth.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.IOUtils;
import com.progzc.blog.auth.service.SysCaptchaService;
import com.progzc.blog.auth.service.SysUserTokenService;
import com.progzc.blog.common.Result;
import com.progzc.blog.common.base.AbstractController;
import com.progzc.blog.common.enums.ErrorEnum;
import com.progzc.blog.entity.sys.SysUser;
import com.progzc.blog.entity.sys.vo.SysLoginForm;
import com.progzc.blog.mapper.sys.SysUserMapper;
import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.crypto.hash.Sha256Hash;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import sun.misc.BASE64Encoder;

import javax.imageio.ImageIO;
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
    private SysCaptchaService sysCaptchaService;

    @Autowired
    private SysUserMapper sysUserMapper;

    @Autowired
    private SysUserTokenService sysUserTokenService;


    /**
     * 获取验证码
     *
     * @param uuid
     * @return
     * @throws IOException
     */
    @GetMapping("captcha.jpg")
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
     *
     * @param form
     * @return
     */
    @PostMapping("/admin/sys/login")
    public Result login(@RequestBody SysLoginForm form) {
        boolean flag = sysCaptchaService.validate(form.getUuid(), form.getCaptcha());
        if (!flag) {
            return Result.error(ErrorEnum.CAPTCHA_WRONG);
        }

        SysUser sysUser = sysUserMapper.selectOne(new QueryWrapper<SysUser>()
                                       .lambda()
                                       .eq(SysUser::getUsername, form.getUsername()));
        // 用户不存在或密码不正确
        if (sysUser == null || !sysUser.getPassword().equals(new Sha256Hash(form.getPassword(), sysUser.getSalt()).toString())) {
            return Result.error(ErrorEnum.USERNAME_OR_PASSWORD_WRONG);
        }

        // 用户被禁用
        if (Boolean.FALSE.equals(sysUser.getStatus())) {
            return Result.error(ErrorEnum.USER_ACCOUNT_LOCKED);
        }

        // 校验成功，生成token
        return sysUserTokenService.createToken(sysUser.getUserId());
    }
}
