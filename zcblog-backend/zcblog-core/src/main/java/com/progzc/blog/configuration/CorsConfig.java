package com.progzc.blog.configuration;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * @Description 配置跨域
 * @Author zhaochao
 * @Date 2020/10/28 0:36
 * @Email zcprog@foxmail.com
 * @Version V1.0
 */
@Configuration
public class CorsConfig implements WebMvcConfigurer {
    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**") // 表示对所有发往控制器的请求都放行
                .allowedOrigins("*") // *表示对所有的地址都可以访问
                .allowCredentials(true) // 可以携带cookie，最终的结果是可以 在跨域请求的时候获取同一个session
                .allowedMethods("GET", "POST", "PUT", "DELETE", "OPTIONS")  // 允许提交GET/POST/PUT/DELETE/OPTIONS请求
                .maxAge(3600); // 准备响应前的缓存持续的最大时间为3600s
    }
}
