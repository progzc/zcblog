package com.progzc.blog.auth.service;

import com.progzc.blog.common.Result;
import com.progzc.blog.entity.sys.auth.SysUserToken;

/**
 * @Description 用户token服务接口
 * @Author zhaocho
 * @Date 2020/11/8 22:48
 * @Email zcprog@foxmail.com
 * @Version V1.0
 */
public interface SysUserTokenService {

    /**
     * 根据用户id创建token
     * @param userId
     * @return
     */
    Result createToken(Long userId);

    /**
     * 从Redis查询token
     * @param token
     * @return
     */
    SysUserToken queryByToken(String token);

    /**
     * 续期
     * @param userId
     * @param token
     */
    void refreshToken(Long userId, String token);

    /**
     * 退出登录
     * @param userId
     */
    void logout(Long userId);
}
