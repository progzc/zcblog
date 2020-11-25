package com.progzc.blog.manage.service.sys;

import com.baomidou.mybatisplus.extension.service.IService;
import com.progzc.blog.entity.sys.SysRole;

import java.util.List;

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
}
