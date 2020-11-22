package com.progzc.blog.auth.core;

import com.progzc.blog.auth.service.ShiroService;
import com.progzc.blog.common.enums.ErrorEnum;
import com.progzc.blog.common.exception.MyException;
import com.progzc.blog.entity.sys.SysUser;
import com.progzc.blog.entity.sys.auth.SysUserToken;
import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.SimpleAuthenticationInfo;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Set;

/**
 * @Description Shiro认证：方法的顺序也是执行的顺序
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
     * 认证逻辑
     * @param authenticationToken
     * @return
     * @throws AuthenticationException
     */
    @Override
    protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken authenticationToken) {
        // 根据用户token从Redis获取用户token+用户id信息
        String token = (String) authenticationToken.getPrincipal();
        SysUserToken sysUserToken = shiroService.queryByToken(token);

        // 若token失效
        if (sysUserToken == null) {
            log.error(ErrorEnum.TOKEN_EXPIRED.getMsg());
            throw new MyException(ErrorEnum.TOKEN_EXPIRED);
        }

        // 根据用户id从数据库查询用户信息
        SysUser sysUser = shiroService.queryByUserId(sysUserToken.getUserId());
        // 若用户账号被锁定
        if (Boolean.FALSE.equals(sysUser.getStatus())) {
            log.error(ErrorEnum.USER_ACCOUNT_LOCKED.getMsg());
            throw new MyException(ErrorEnum.USER_ACCOUNT_LOCKED);
        }
        // 续期
        shiroService.refreshToken(sysUserToken.getUserId(), token);

        return new SimpleAuthenticationInfo(sysUser, token, getName());
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
        log.debug(userPerms.toString());
        authorizationInfo.setStringPermissions(userPerms);
        return authorizationInfo;
    }
}
