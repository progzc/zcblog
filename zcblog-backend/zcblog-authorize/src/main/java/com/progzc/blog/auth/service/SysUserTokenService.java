package com.progzc.blog.auth.service;

import com.progzc.blog.common.Result;

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
}
