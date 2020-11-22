package com.progzc.blog.manage.serviceImpl.sys;

import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.progzc.blog.entity.sys.SysUser;
import com.progzc.blog.manage.service.sys.SysUserService;
import com.progzc.blog.mapper.sys.SysUserMapper;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
 * @Description 用户
 * @Author zhaochao
 * @Date 2020-10-25
 * @Email zcprog@foxmail.com
 * @Version V1.0
 */
@Service
public class SysUserServiceImpl extends ServiceImpl<SysUserMapper, SysUser> implements SysUserService {

    @Resource
    private SysUserMapper sysUserMapper;

    /**
     * 查询用户菜单id
     * @param userId
     * @return
     */
    @Override
    public List<Long> queryAllMenuId(Long userId) {
        return sysUserMapper.queryAllMenuId(userId);
    }

    /**
     * 修改用户密码
     * @param userId
     * @param password
     * @param newPassword
     * @return
     */
    @Override
    public boolean updatePassword(Long userId, String password, String newPassword) {
        SysUser sysUser = new SysUser();
        sysUser.setPassword(newPassword);
        return update(sysUser, new UpdateWrapper<SysUser>().lambda()
                .eq(SysUser::getUserId, userId).eq(SysUser::getPassword, password));
    }
}
