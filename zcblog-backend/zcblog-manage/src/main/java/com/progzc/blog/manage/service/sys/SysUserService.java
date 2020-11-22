package com.progzc.blog.manage.service.sys;

import com.baomidou.mybatisplus.extension.service.IService;
import com.progzc.blog.entity.sys.SysUser;

import java.util.List;

/**
 * @Description 用户
 * @Author zhaochao
 * @Date 2020-10-25
 * @Email zcprog@foxmail.com
 * @Version V1.0
 */
public interface SysUserService extends IService<SysUser> {

    /**
     * 查询用户菜单id
     * @param userId
     * @return
     */
    List<Long> queryAllMenuId(Long userId);

    /**
     * 修改用户密码
     * @param userId
     * @param password
     * @param newPassword
     * @return
     */
    boolean updatePassword(Long userId, String password, String newPassword);
}
