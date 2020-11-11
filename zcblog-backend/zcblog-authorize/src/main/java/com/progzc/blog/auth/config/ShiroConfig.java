package com.progzc.blog.auth.config;

import com.progzc.blog.auth.core.Oauth2Filter;
import com.progzc.blog.auth.core.Oauth2Realm;
import org.apache.shiro.mgt.SecurityManager;
import org.apache.shiro.session.mgt.SessionManager;
import org.apache.shiro.spring.web.ShiroFilterFactoryBean;
import org.apache.shiro.web.mgt.DefaultWebSecurityManager;
import org.apache.shiro.web.session.mgt.DefaultWebSessionManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.servlet.Filter;
import java.util.HashMap;
import java.util.Map;

/**
 * @Description Shiro配置类
 * @Author zhaochao
 * @Date 2020/11/10 23:55
 * @Email zcprog@foxmail.com
 * @Version V1.0
 */
@Configuration
public class ShiroConfig {

    /**
     * 配置会话管理器
     * @return
     */
    @Bean
    public SessionManager sessionManager(){
        DefaultWebSessionManager sessionManager = new DefaultWebSessionManager();
        // 是否定时检查session
        sessionManager.setSessionValidationSchedulerEnabled(false);
        return sessionManager;
    }

    /**
     * 配置安全管理器
     * @param oauth2Realm
     * @param sessionManager
     * @return
     */
    @Bean
    public SecurityManager securityManager(Oauth2Realm oauth2Realm, SessionManager sessionManager){
        DefaultWebSecurityManager securityManager = new DefaultWebSecurityManager();
        securityManager.setRealm(oauth2Realm); // 设置认证与鉴权逻辑
        securityManager.setSessionManager(sessionManager); // 设置会话管理器
        return securityManager;
    }

    /**
     * 配置过滤器
     * @param securityManager
     * @return
     */
    @Bean
    public ShiroFilterFactoryBean shiroFilter(SecurityManager securityManager){
        ShiroFilterFactoryBean shiroFilter = new ShiroFilterFactoryBean();
        shiroFilter.setSecurityManager(securityManager); // 设置安全管理器

        Map<String, Filter> filters = new HashMap<>();
        filters.put("oauth2", new Oauth2Filter());

        return shiroFilter;
    }
}
