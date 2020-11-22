package com.progzc.blog.configuration;

import com.baomidou.mybatisplus.extension.plugins.PaginationInterceptor;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @Description MyBatisPlus分页插件配置
 * @Author zhaochao
 * @Date 2020/11/22 14:51
 * @Email zcprog@foxmail.com
 * @Version V1.0
 */
@Configuration
@MapperScan("com.progzc.blog.mapper")
public class MybatisPlusConfig {
    @Bean
    public PaginationInterceptor paginationInterceptor(){
        PaginationInterceptor paginationInterceptor = new PaginationInterceptor();
        return paginationInterceptor;
    }
}
