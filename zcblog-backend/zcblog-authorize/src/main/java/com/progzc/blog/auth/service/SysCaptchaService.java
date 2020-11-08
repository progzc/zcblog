package com.progzc.blog.auth.service;

import java.awt.image.BufferedImage;

/**
 * @Description 验证码服务接口
 * @Author zhaocho
 * @Date 2020/11/8 15:33
 * @Email zcprog@foxmail.com
 * @Version V1.0
 */
public interface SysCaptchaService {
    /**
     * 获取验证码
     * @param uuid
     * @return
     */
    BufferedImage getCaptcha(String uuid);

    /**
     * 校验验证码
     * @param uuid
     * @param captcha
     * @return
     */
    boolean validate(String uuid, String captcha);
}
