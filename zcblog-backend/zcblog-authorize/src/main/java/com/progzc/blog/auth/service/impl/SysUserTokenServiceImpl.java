package com.progzc.blog.auth.service.impl;

import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.progzc.blog.auth.service.SysUserTokenService;
import com.progzc.blog.common.Result;
import com.progzc.blog.common.constants.RedisKeyConstants;
import com.progzc.blog.common.utils.RedisUtils;
import com.progzc.blog.common.utils.TokenGeneratorUtils;
import com.progzc.blog.entity.sys.auth.SysUserToken;
import org.springframework.beans.factory.annotation.Autowired;
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

    @Autowired
    private RedisUtils redisUtils;

    /**
     * 根据用户id创建token
     * @param userId
     * @return
     */
    @Override
    public Result createToken(Long userId) {
        // 生成token
        String token = TokenGeneratorUtils.generateValue();
        String tokenKey = RedisKeyConstants.MANAGE_SYS_USER_TOKEN + token;
        String userIdKey = RedisKeyConstants.MANAGE_SYS_USER_TOKEN + userId;

        String tokenCache = redisUtils.getObj(userIdKey, String.class);
        // 若token存在Redis缓存中，则先删除再添加
        if (!StringUtils.isEmpty(tokenCache)) {
            redisUtils.delete(RedisKeyConstants.MANAGE_SYS_USER_TOKEN + tokenCache);
        }
        // 在Redis缓存中设置token和userId的管理映射
        redisUtils.set(tokenKey, userId, EXPIRE);
        redisUtils.set(userIdKey, token, EXPIRE);

        return Result.ok().put("token", token).put("expire", EXPIRE);
    }

    /**
     * 从Redis查询token
     * @param token
     * @return
     */
    @Override
    public SysUserToken queryByToken(String token) {
        String userId = redisUtils.getObj(RedisKeyConstants.MANAGE_SYS_USER_TOKEN + token, String.class);
        if (StringUtils.isEmpty(userId)) {
            return null;
        }
        SysUserToken sysUserToken = new SysUserToken();
        sysUserToken.setToken(token);
        sysUserToken.setUserId(Long.parseLong(userId));
        return sysUserToken;
    }

    /**
     * 续期
     * @param userId
     * @param token
     */
    @Override
    public void refreshToken(Long userId, String token) {
        redisUtils.updateExpire(RedisKeyConstants.MANAGE_SYS_USER_TOKEN + token);
        redisUtils.updateExpire(RedisKeyConstants.MANAGE_SYS_USER_TOKEN + userId);
    }

}
