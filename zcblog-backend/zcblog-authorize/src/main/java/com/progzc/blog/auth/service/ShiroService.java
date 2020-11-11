package com.progzc.blog.auth.service;

import com.progzc.blog.entity.sys.SysUser;
import com.progzc.blog.entity.sys.auth.SysUserToken;

import java.util.Set;

/**
 * @Description Shiro服务接口
 * @Author zhaochao
 * @Date 2020/11/11 10:36
 * @Email zcprog@foxmail.com
 * @Version V1.0
 */
public interface ShiroService {

    /**
     * 从Redis查询token
     * @param token
     * @return
     */
    SysUserToken queryByToken(String token);

    /**
     * 从数据库根据用户id查询用户
     * @param userId
     * @return
     */
    SysUser queryByUserId(Long userId);

    /**
     * 续期
     * @param userId
     * @param token
     */
    void refreshToken(Long userId, String token);

    /**
     * 根据id获取用户的所有权限
     * @param userId
     */
    Set<String> getUserPerms(Long userId);
}
