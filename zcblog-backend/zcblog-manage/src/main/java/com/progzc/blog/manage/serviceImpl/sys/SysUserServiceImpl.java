package com.progzc.blog.manage.serviceImpl.sys;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.progzc.blog.common.constants.SysConstants;
import com.progzc.blog.common.exception.MyException;
import com.progzc.blog.entity.MyPage;
import com.progzc.blog.entity.Query;
import com.progzc.blog.entity.sys.SysUser;
import com.progzc.blog.manage.service.sys.SysRoleService;
import com.progzc.blog.manage.service.sys.SysUserRoleService;
import com.progzc.blog.manage.service.sys.SysUserService;
import com.progzc.blog.mapper.sys.SysUserMapper;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

/**
 * @Description 用户
 * @Author zhaochao
 * @Date 2020-10-25
 * @Email zcprog@foxmail.com
 * @Version V1.0
 */
@Service
@Slf4j
public class SysUserServiceImpl extends ServiceImpl<SysUserMapper, SysUser> implements SysUserService {

    @Resource
    private SysUserMapper sysUserMapper;

    @Autowired
    private SysUserRoleService sysUserRoleService;

    @Autowired
    private SysRoleService sysRoleService;

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

    /**
     * 查询用户列表
     * @param params
     * @return
     */
    @Override
    public MyPage queryPage(Map<String, Object> params) {
        Query<SysUser> query = new Query<>(params);
        Long createUserId = (Long) query.get("createUserId");

        // 若是超级管理员，flag = false，表示.eq条件不存在，则可以查询所有用户
        boolean flag = !SysConstants.SUPER_ADMIN.equals(createUserId);

        IPage<SysUser> page = sysUserMapper.selectPage(query.getPage(), new QueryWrapper<SysUser>().lambda()
                // 查询时只输出"用户id/用户名/用户邮箱/用户电话/用户状态/用户创建时间"字段
                .select(SysUser::getUserId, SysUser::getUsername, SysUser::getEmail, SysUser::getPhone, SysUser::getStatus, SysUser::getCreateTime)
                .like(StringUtils.isNotBlank(query.getKeyWord()), SysUser::getUsername, query.getKeyWord())
                .eq(flag, SysUser::getCreateUserId, createUserId));
        return new MyPage(page);
    }

    /**
     * 删除用户
     * @param userIds
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void deleteBatch(Long[] userIds) {
        // 删除用户
        sysUserMapper.deleteBatchIds(Arrays.asList(userIds));
        // 删除用户与角色之间的关联关系
        sysUserRoleService.deleteBatchByUserIds(userIds);
    }

    /**
     * 新增用户
     * @param sysUser
     * @return
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean saveNewUser(SysUser sysUser) {
        // 查询用户名/手机号/邮箱是否已存在(即出现次数大于0)
        checkExist(sysUser, false);
        sysUserMapper.insert(sysUser);
        // 检查角色是否越权
        checkRole(sysUser, false);
        // 保存用户与角色关系
        sysUserRoleService.saveOrUpdate(sysUser.getUserId(), sysUser.getRoleIdList());
        return true;
    }

    /**
     * 更新用户信息
     * @param sysUser
     * @return
     */
    @Override
    public boolean updateUser(SysUser sysUser) {
        // 查询用户名/手机号/邮箱是否已存在(即除自身外，出现次数大于0)
        checkExist(sysUser, true);
        sysUserMapper.updateById(sysUser);
        // 检查角色是否越权
        checkRole(sysUser, true);
        // 保存用户与角色关系
        sysUserRoleService.saveOrUpdate(sysUser.getUserId(), sysUser.getRoleIdList());
        return true;
    }

    /**
     * 检查用户名/手机号/邮箱是否已存在
     * @param sysUser
     * @param flag    标志位，false代表新增用户信息，true表示更新用户信息
     */
    private void checkExist(SysUser sysUser, boolean flag) {
        String message = !flag ? "添加" : "更新";
        // 查询系统中该用户名是否已存在
        Integer count1 = sysUserMapper.selectCount(new QueryWrapper<SysUser>().lambda()
                .eq(SysUser::getUsername, sysUser.getUsername())
                .ne(flag, SysUser::getUserId, sysUser.getUserId()));
        if (count1 != null && count1 > 0) {
            throw new MyException(message + "失败，该用户名已存在，请重新设置");
        }
        // 查询系统中该手机号是否已存在
        Integer count2 = sysUserMapper.selectCount(new QueryWrapper<SysUser>().lambda()
                .eq(SysUser::getPhone, sysUser.getPhone())
                .ne(flag, SysUser::getUserId, sysUser.getUserId()));
        if (count2 != null && count2 > 0) {
            throw new MyException(message + "失败，该手机号已存在，请重新设置");
        }
        // 查询系统中该邮箱是否已存在
        Integer count3 = sysUserMapper.selectCount(new QueryWrapper<SysUser>().lambda()
                .eq(SysUser::getEmail, sysUser.getEmail())
                .ne(flag, SysUser::getUserId, sysUser.getUserId()));
        if (count3 != null && count3 > 0) {
            throw new MyException(message + "失败，该邮箱已存在，请重新设置");
        }
    }

    /**
     * 检查角色是否越权
     * @param sysUser
     * @param flag    标志位，false代表新增用户信息，true表示更新用户信息
     */
    private void checkRole(SysUser sysUser, boolean flag) {
        String message = !flag ? "添加" : "更新";
        // 检查是否设置至少一种角色
        if (CollectionUtils.isEmpty(sysUser.getRoleIdList())) {
            throw new MyException(message + "失败，请为用户设置至少一种角色");
        }
        // 若不是超级管理员，则检查用户角色是否是自己创建
        if (SysConstants.SUPER_ADMIN.equals(sysUser.getCreateUserId())) {
            List<Long> roleIdList = sysRoleService.queryRoleIdList(sysUser.getCreateUserId());
            if (!roleIdList.containsAll(sysUser.getRoleIdList())) {
                throw new MyException(message + "失败，用户所选角色不是由本人创建");
            }
        }
    }
}
