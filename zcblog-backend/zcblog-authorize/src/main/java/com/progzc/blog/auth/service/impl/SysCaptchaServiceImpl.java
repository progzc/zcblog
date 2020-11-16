package com.progzc.blog.auth.service.impl;

import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.google.code.kaptcha.Producer;
import com.progzc.blog.auth.service.SysCaptchaService;
import com.progzc.blog.common.constants.KaptchaConstants;
import com.progzc.blog.common.constants.RedisKeyConstants;
import com.progzc.blog.common.enums.ErrorEnum;
import com.progzc.blog.common.exception.MyException;
import com.progzc.blog.common.utils.RedisUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.awt.image.BufferedImage;

/**
 * @Description 验证码服务接口实现
 * @Author zhaochao
 * @Date 2020/11/8 15:36
 * @Email zcprog@foxmail.com
 * @Version V1.0
 */
@Slf4j
@Service
public class SysCaptchaServiceImpl implements SysCaptchaService {

    @Autowired
    private Producer producer;

    @Autowired
    private RedisUtils redisUtils;

    /**
     * 设置验证码过期时间为5分钟
     */
    public static final long CAPTCHA_EXPIRE = 60 * 5L;

    /**
     * 获取验证码
     * @param uuid
     * @return
     */
    @Override
    public BufferedImage getCaptcha(String uuid) {
        if (StringUtils.isEmpty(uuid)) {
            log.error(ErrorEnum.NO_UUID.getMsg());
            throw new MyException(ErrorEnum.NO_UUID);
        }
        String captcha = producer.createText();
        KaptchaConstants.captcha = captcha; // 使用类变量记录谷歌验证码，用于程序自动登录
        log.debug("生成验证码：" + captcha);
        redisUtils.set(RedisKeyConstants.MANAGE_SYS_CAPTCHA + uuid, captcha, CAPTCHA_EXPIRE);
        return producer.createImage(captcha);
    }

    /**
     * 校验验证码
     * @param uuid
     * @param captcha
     * @return
     */
    @Override
    public boolean validate(String uuid, String captcha) {
        if (StringUtils.isEmpty(uuid) || StringUtils.isEmpty(captcha)) {
            return false;
        }
        String captchaCache = redisUtils.getObj(RedisKeyConstants.MANAGE_SYS_CAPTCHA + uuid, String.class);
        redisUtils.delete(RedisKeyConstants.MANAGE_SYS_CAPTCHA + uuid);
        KaptchaConstants.captcha = ""; // 重置类变量
        return captcha.equalsIgnoreCase(captchaCache);
    }
}
