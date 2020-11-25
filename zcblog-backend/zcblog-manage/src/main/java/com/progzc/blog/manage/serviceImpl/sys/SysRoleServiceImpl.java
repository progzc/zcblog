package com.progzc.blog.manage.serviceImpl.sys;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.progzc.blog.entity.sys.SysRole;
import com.progzc.blog.manage.service.sys.SysRoleService;
import com.progzc.blog.mapper.sys.SysRoleMapper;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
 * @Description 角色
 * @Author zhaochao
 * @Date 2020-10-25
 * @Email zcprog@foxmail.com
 * @Version V1.0
 */
@Service
public class SysRoleServiceImpl extends ServiceImpl<SysRoleMapper, SysRole> implements SysRoleService {

    @Resource
    private SysRoleMapper sysRoleMapper;

    /**
     * 根据创建者查询角色列表
     * @param createUserId
     * @return
     */
    @Override
    public List<Long> queryRoleIdList(Long createUserId) {
        return sysRoleMapper.queryRoleIdList(createUserId);
    }
}
