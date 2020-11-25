package com.progzc.blog.manage.service.sys;

import com.baomidou.mybatisplus.extension.service.IService;
import com.progzc.blog.entity.MyPage;
import com.progzc.blog.entity.sys.SysUser;

import java.util.List;
import java.util.Map;

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

    /**
     * 查询用户列表
     * @param params
     * @return
     */
    MyPage queryPage(Map<String, Object> params);

    /**
     * 删除用户
     * @param userIds
     */
    void deleteBatch(Long[] userIds);

    /**
     * 新增用户
     * @param sysUser
     * @return
     */
    boolean saveNewUser(SysUser sysUser);

    /**
     * 更新用户
     * @param sysUser
     * @return
     */
    boolean updateUser(SysUser sysUser);
}
