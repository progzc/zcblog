package com.progzc.blog.auth.service.impl;

import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.progzc.blog.auth.service.ShiroService;
import com.progzc.blog.auth.service.SysUserTokenService;
import com.progzc.blog.common.constants.SysConstants;
import com.progzc.blog.entity.sys.SysMenu;
import com.progzc.blog.entity.sys.SysUser;
import com.progzc.blog.entity.sys.auth.SysUserToken;
import com.progzc.blog.mapper.sys.SysMenuMapper;
import com.progzc.blog.mapper.sys.SysUserMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * @Description Shiro服务接口实现
 * @Author zhaochao
 * @Date 2020/11/11 10:37
 * @Email zcprog@foxmail.com
 * @Version V1.0
 */
@Service
public class ShiroServiceImpl implements ShiroService {

    @Autowired
    private SysUserMapper sysUserMapper;

    @Autowired
    private SysMenuMapper sysMenuMapper;

    @Autowired
    private SysUserTokenService sysUserTokenService;

    /**
     * 从Redis查询token
     * @param token
     * @return
     */
    @Override
    public SysUserToken queryByToken(String token) {
        return sysUserTokenService.queryByToken(token);
    }

    /**
     * 从数据库根据用户id查询用户
     * @param userId
     * @return
     */
    @Override
    public SysUser queryByUserId(Long userId) {
        return sysUserMapper.selectById(userId);
    }

    /**
     * 续期
     * @param userId
     * @param token
     */
    @Override
    public void refreshToken(Long userId, String token) {
        sysUserTokenService.refreshToken(userId, token);
    }

    /**
     * 查询用户的所有权限
     * @param userId
     * @return
     */
    @Override
    public Set<String> getUserPerms(Long userId) {
        List<String> userPerms;
        // 若用户是超级管理员
        if (SysConstants.SUPER_ADMIN.equals(userId)){
            List<SysMenu> sysMenus = sysMenuMapper.selectList(null);
            userPerms = new ArrayList<>(sysMenus.size());
            sysMenus.forEach(sysMenu -> userPerms.add(sysMenu.getPerms()));
        }else {
            userPerms = sysUserMapper.queryPermsByUserId(userId);
        }

        return userPerms.stream()
                .filter(userPerm -> !StringUtils.isEmpty(userPerm)) // 过滤空字符串
                .flatMap(userPerm -> Arrays.stream(userPerm.split(","))) // 合并
                .collect(Collectors.toSet());
    }
}
