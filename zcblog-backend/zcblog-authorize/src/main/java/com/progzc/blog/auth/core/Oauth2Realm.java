package com.progzc.blog.auth.core;

import com.progzc.blog.auth.service.ShiroService;
import com.progzc.blog.entity.sys.SysUser;
import com.progzc.blog.entity.sys.auth.SysUserToken;
import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.authc.*;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Set;

/**
 * @Description Shiro认证
 * @Author zhaochao
 * @Date 2020/11/11 0:26
 * @Email zcprog@foxmail.com
 * @Version V1.0
 */
@Slf4j
@Component
public class Oauth2Realm extends AuthorizingRealm {

    @Autowired
    private ShiroService shiroService;

    /**
     * 识别登录数据类型
     * @param authenticationToken
     * @return
     */
    @Override
    public boolean supports(AuthenticationToken authenticationToken) {
        return authenticationToken instanceof Oauth2Token;
    }

    /**
     * 鉴权逻辑
     * @param principals
     * @return
     */
    @Override
    protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principals) {
        SysUser sysUser = (SysUser) principals.getPrimaryPrincipal();
        Long userId = sysUser.getUserId();
        Set<String> userPerms = shiroService.getUserPerms(userId);
        SimpleAuthorizationInfo authorizationInfo = new SimpleAuthorizationInfo();
        authorizationInfo.setStringPermissions(userPerms);
        return authorizationInfo;
    }

    /**
     * 认证逻辑
     * @param authenticationToken
     * @return
     * @throws AuthenticationException
     */
    @Override
    protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken authenticationToken) throws AuthenticationException {
        // 根据用户token从Redis获取用户token+用户id信息
        String token = (String) authenticationToken.getPrincipal();
        SysUserToken sysUserToken = shiroService.queryByToken(token);

        // 若token失效
        if (sysUserToken == null) {
            log.debug("token已失效，请重新登录");
            throw new IncorrectCredentialsException("token已失效，请重新登录");
        }

        // 根据用户id从数据库查询用户信息
        SysUser sysUser = shiroService.queryByUserId(sysUserToken.getUserId());
        // 若用户账号被锁定
        if (Boolean.FALSE.equals(sysUser.getStatus())) {
            log.debug("账号已被锁定，请联系管理员");
            throw new LockedAccountException("账号已被锁定，请联系管理员");
        }
        // 续期
        shiroService.refreshToken(sysUserToken.getUserId(), token);

        return new SimpleAuthenticationInfo(sysUser, token, getName());
    }
}
