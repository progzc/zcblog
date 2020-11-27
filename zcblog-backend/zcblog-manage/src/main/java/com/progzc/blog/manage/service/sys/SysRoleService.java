package com.progzc.blog.manage.service.sys;

import com.baomidou.mybatisplus.extension.service.IService;
import com.progzc.blog.entity.MyPage;
import com.progzc.blog.entity.sys.SysRole;

import java.util.List;
import java.util.Map;

/**
 * @Description 角色
 * @Author zhaochao
 * @Date 2020-10-25
 * @Email zcprog@foxmail.com
 * @Version V1.0
 */
public interface SysRoleService extends IService<SysRole> {

    /**
     * 根据创建者查询角色列表
     * @param createUserId
     * @return
     */
    List<Long> queryRoleIdList(Long createUserId);

    /**
     * 分页查询当前用户所创建的角色列表
     * @param params
     * @return
     */
    MyPage queryPage(Map<String, Object> params);

    /**
     * 删除角色
     * @param roleIds
     */
    void deleteBatch(Long[] roleIds);

    /**
     * 新增角色
     * @param sysRole
     */
    void saveRoleInfo(SysRole sysRole);

    /**
     * 更新角色
     * @param sysRole
     */
    void updateRoleInfo(SysRole sysRole);
}
