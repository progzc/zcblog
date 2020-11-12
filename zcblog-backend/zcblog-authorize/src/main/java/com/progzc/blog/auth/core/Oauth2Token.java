package com.progzc.blog.auth.core;

import org.apache.shiro.authc.AuthenticationToken;

/**
 * @Description Shiro认证令牌
 * @Author zhaochao
 * @Date 2020/11/11 10:01
 * @Email zcprog@foxmail.com
 * @Version V1.0
 */
public class Oauth2Token implements AuthenticationToken {

    private static final long serialVersionUID = 1L;

    private String token;

    public Oauth2Token(String token) {
        this.token = token;
    }

    /**
     * 获取身份信息
     * @return
     */
    @Override
    public Object getPrincipal() {
        return token;
    }

    /**
     * 获取身份凭证
     * @return
     */
    @Override
    public Object getCredentials() {
        return token;
    }
}
