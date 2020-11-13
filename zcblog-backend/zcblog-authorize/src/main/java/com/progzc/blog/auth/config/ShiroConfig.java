package com.progzc.blog.auth.config;

import com.progzc.blog.auth.core.Oauth2Filter;
import com.progzc.blog.auth.core.Oauth2Realm;
import org.apache.shiro.mgt.SecurityManager;
import org.apache.shiro.session.mgt.SessionManager;
import org.apache.shiro.spring.LifecycleBeanPostProcessor;
import org.apache.shiro.spring.security.interceptor.AuthorizationAttributeSourceAdvisor;
import org.apache.shiro.spring.web.ShiroFilterFactoryBean;
import org.apache.shiro.web.mgt.DefaultWebSecurityManager;
import org.apache.shiro.web.session.mgt.DefaultWebSessionManager;
import org.springframework.aop.framework.autoproxy.DefaultAdvisorAutoProxyCreator;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.DependsOn;

import javax.servlet.Filter;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * @Description Shiro配置类：方法的顺序也是Bean执行的顺序
 * @Author zhaochao
 * @Date 2020/11/10 23:55
 * @Email zcprog@foxmail.com
 * @Version V1.0
 */
@Configuration
public class ShiroConfig {

    /**
     * 管理Shiro Bean的生命周期：其实在ShiroBeanConfiguration中已经配置好了，多次一举了
     * @return
     */
    @Bean
    public LifecycleBeanPostProcessor lifecycleBeanPostProcessor() {
        return new LifecycleBeanPostProcessor();
    }

    /**
     * DefaultAdvisorAutoProxyCreator实现了BeanProcessor接口,
     * 当ApplicationContext读如所有的Bean配置信息后，这个类将扫描上下文，寻找所有的Advisor
     * @return
     */
    @Bean
    @DependsOn({"lifecycleBeanPostProcessor"})
    public DefaultAdvisorAutoProxyCreator defaultAdvisorAutoProxyCreator() {
        DefaultAdvisorAutoProxyCreator proxyCreator = new DefaultAdvisorAutoProxyCreator();
        proxyCreator.setProxyTargetClass(true);
        return proxyCreator;
    }

    /**
     * 配置会话管理器
     * @return
     */
    @Bean
    public SessionManager sessionManager() {
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
    public SecurityManager securityManager(Oauth2Realm oauth2Realm, SessionManager sessionManager) {
        DefaultWebSecurityManager securityManager = new DefaultWebSecurityManager();
        securityManager.setRealm(oauth2Realm); // 设置认证与鉴权逻辑
        securityManager.setSessionManager(sessionManager); // 设置会话管理器
        return securityManager;
    }

    /**
     * 配置Shiro过滤器
     * @param securityManager
     * @return
     */
    @Bean
    public ShiroFilterFactoryBean shiroFilter(SecurityManager securityManager) {
        ShiroFilterFactoryBean shiroFilter = new ShiroFilterFactoryBean();
        shiroFilter.setSecurityManager(securityManager); // 设置安全管理器

        Map<String, Filter> filters = new HashMap<>();
        filters.put("oauth2", new Oauth2Filter());
        shiroFilter.setFilters(filters); // 对拦截到的页面请求进行捕获进行认证与鉴权

        Map<String, String> filterMap = new LinkedHashMap<>();
        // 两个url规则都可以同时匹配同一个url，且只执行第一个
        filterMap.put("/admin/sys/login", "anon"); // 放行zcblog-front2manage的登录页面
        filterMap.put("/admin/**", "oauth2"); // zcblog-front2manage的其他页面需要认证和授权
        filterMap.put("/**", "anon"); // 放行zcblog-front2client项目页面
        shiroFilter.setFilterChainDefinitionMap(filterMap); // 设置页面请求拦截

        return shiroFilter;
    }

    /**
     * 通知，启用Shiro注解
     * @param securityManager
     * @return
     */
    @Bean
    public AuthorizationAttributeSourceAdvisor authorizationAttributeSourceAdvisor(SecurityManager securityManager) {
        AuthorizationAttributeSourceAdvisor advisor = new AuthorizationAttributeSourceAdvisor();
        advisor.setSecurityManager(securityManager); // 设置安全管理器
        return advisor;
    }

}
