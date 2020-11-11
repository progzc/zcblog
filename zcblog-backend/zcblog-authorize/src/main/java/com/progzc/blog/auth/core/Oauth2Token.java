package com.progzc.blog.auth.core;

import org.apache.shiro.authc.AuthenticationToken;

/**
 * @Description Shiro认证令牌
 * @Author zhaochao
 * @Date 2020/11/11 10:01
 * @Email zcprog@foxmail.com
 * @Version V1.0
 */
public class Oauth2Token  implements AuthenticationToken {

    private String token;

    public Oauth2Token(String token){
        this.token = token;
    }

    @Override
    public Object getPrincipal() {
        return token;
    }

    @Override
    public Object getCredentials() {
        return token;
    }
}
