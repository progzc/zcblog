package com.progzc.blog.auth.service.impl;

import com.progzc.blog.auth.service.SysUserTokenService;
import com.progzc.blog.common.Result;
import org.springframework.stereotype.Service;

/**
 * @Description 用户token服务实现类
 * @Author zhaochao
 * @Date 2020/11/8 22:50
 * @Email zcprog@foxmail.com
 * @Version V1.0
 */
@Service
public class SysUserTokenServiceImpl implements SysUserTokenService {

    /**
     * token有效期是12h
     */
    private static final long EXPIRE = 3600 * 12L;

    /**
     * 根据用户id创建token
     * @param userId
     * @return
     */
    @Override
    public Result createToken(Long userId) {
        return null;
    }


}
