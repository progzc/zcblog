package com.progzc.blog.manage.serviceImpl.sys;

import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.progzc.blog.entity.sys.SysUserRole;
import com.progzc.blog.manage.service.sys.SysUserRoleService;
import com.progzc.blog.mapper.sys.SysUserRoleMapper;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * @Description 用户与角色对应关系
 * @Author zhaochao
 * @Date 2020-10-25
 * @Email zcprog@foxmail.com
 * @Version V1.0
 */
@Service
public class SysUserRoleServiceImpl extends ServiceImpl<SysUserRoleMapper, SysUserRole> implements SysUserRoleService {

    @Resource
    private SysUserRoleMapper sysUserRoleMapper;

    /**
     * 删除用户与角色之间的关系
     * @param userIds
     */
    @Override
    public void deleteBatchByUserIds(Long[] userIds) {
        Arrays.stream(userIds).forEach(userId -> {
            if (userId != null) {
                sysUserRoleMapper.delete(new UpdateWrapper<SysUserRole>().lambda()
                        .eq(SysUserRole::getUserId, userId));
            }
        });
    }

    /**
     * 根据用户id获取用户角色id列表
     * @param userId
     * @return
     */
    @Override
    public List<Long> queryRoleIdList(Long userId) {
        return sysUserRoleMapper.queryRoleIdList(userId);
//        下面这种做法虽然也能实现，但是效率比较，推荐使用XML写SQL的方式
//        List<SysUserRole> sysUserRoles = sysUserRoleMapper.selectList(new QueryWrapper<SysUserRole>().lambda()
//                .select(SysUserRole::getRoleId)
//                .eq(SysUserRole::getUserId, userId));
//        return sysUserRoles.stream().map(SysUserRole::getRoleId).collect(Collectors.toList());
    }

    /**
     * 保存或更新用户角色关系
     * @param userId
     * @param roleIdList
     */
    @Override
    public void saveOrUpdate(Long userId, List<Long> roleIdList) {
        // 这里userId必不为null
        // 原因：若是新建用户，则插入后会自动生成userId；若是更新用户，则Controller层已经校验
        sysUserRoleMapper.delete(new UpdateWrapper<SysUserRole>().lambda()
                .eq(SysUserRole::getUserId, userId));
        // 保存用户与角色关系
        List<SysUserRole> list = new ArrayList<>(roleIdList.size());
        for (Long roleId : roleIdList) {
            SysUserRole SysUserRole = new SysUserRole();
            SysUserRole.setUserId(userId);
            SysUserRole.setRoleId(roleId);
            list.add(SysUserRole);
        }
        this.saveBatch(list);
    }
}
