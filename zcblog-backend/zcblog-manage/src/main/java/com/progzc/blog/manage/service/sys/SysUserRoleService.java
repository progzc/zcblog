package com.progzc.blog.manage.service.sys;

import com.baomidou.mybatisplus.extension.service.IService;
import com.progzc.blog.entity.sys.SysUserRole;

import java.util.List;

/**
 * @Description 用户与角色对应关系
 * @Author zhaochao
 * @Date 2020-10-25
 * @Email zcprog@foxmail.com
 * @Version V1.0
 */
public interface SysUserRoleService extends IService<SysUserRole> {

    /**
     * 删除用户角色之间的关系
     * @param userIds
     */
    void deleteBatchByUserIds(Long[] userIds);

    /**
     * 根据用户id获取用户角色id
     * @param userId
     * @return
     */
    List<Long> queryRoleIdList(Long userId);

    /**
     * 保存或更新用户角色关系
     * @param userId
     * @param roleIdList
     */
    void saveOrUpdate(Long userId, List<Long> roleIdList);

}
