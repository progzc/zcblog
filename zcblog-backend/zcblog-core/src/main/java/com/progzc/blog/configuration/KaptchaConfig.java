package com.progzc.blog.configuration;

import com.google.code.kaptcha.impl.DefaultKaptcha;
import com.google.code.kaptcha.util.Config;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Properties;

/**
 * @Description 验证码配置码
 * @Author zhaochao
 * @Date 2020/11/4 10:21
 * @Email zcprog@foxmail.com
 * @Version V1.0
 */
@Configuration
public class KaptchaConfig {

    @Bean
    public DefaultKaptcha producer() {
        Properties properties = new Properties();
        properties.put("kaptcha.border", "no"); // 不需要边框
        properties.put("kaptcha.textproducer.font.color", "black"); // 字体颜色设置为黑色
        properties.put("kaptcha.textproducer.char.space", "5");  // 字符串间隔
        Config config = new Config(properties);
        DefaultKaptcha defaultKaptcha = new DefaultKaptcha();
        defaultKaptcha.setConfig(config);
        return defaultKaptcha;
    }
}
