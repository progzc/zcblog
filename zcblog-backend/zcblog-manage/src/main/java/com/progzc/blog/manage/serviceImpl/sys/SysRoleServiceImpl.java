package com.progzc.blog.manage.serviceImpl.sys;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.progzc.blog.common.constants.SysConstants;
import com.progzc.blog.common.exception.MyException;
import com.progzc.blog.entity.MyPage;
import com.progzc.blog.entity.Query;
import com.progzc.blog.entity.sys.SysRole;
import com.progzc.blog.manage.service.sys.SysRoleMenuService;
import com.progzc.blog.manage.service.sys.SysRoleService;
import com.progzc.blog.manage.service.sys.SysUserRoleService;
import com.progzc.blog.manage.service.sys.SysUserService;
import com.progzc.blog.mapper.sys.SysRoleMapper;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

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

    @Autowired
    private SysUserService sysUserService;

    @Autowired
    private SysRoleMenuService sysRoleMenuService;

    @Autowired
    private SysUserRoleService sysUserRoleService;

    /**
     * 根据创建者查询角色列表
     * @param createUserId
     * @return
     */
    @Override
    public List<Long> queryRoleIdList(Long createUserId) {
        return sysRoleMapper.queryRoleIdList(createUserId);
    }

    /**
     * 分页查询当前用户所创建的角色列表
     * @param params
     * @return
     */
    @Override
    public MyPage queryPage(Map<String, Object> params) {
        Query<SysRole> query = new Query<>(params);
        Long createUserId = (Long) query.get("createUserId");

        // 若是超级管理员，flag = false，表示.eq条件不存在，则可以查询所有用户
        boolean flag = createUserId != null && !SysConstants.SUPER_ADMIN.equals(createUserId);

        IPage<SysRole> page = sysRoleMapper.selectPage(query.getPage(), new QueryWrapper<SysRole>().lambda()
                .like(StringUtils.isNotBlank(query.getKeyWord()), SysRole::getRoleName, query.getKeyWord())
                .eq(flag, SysRole::getCreateUserId, createUserId));
        return new MyPage(page);
    }

    /**
     * 删除角色
     * @param roleIds
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void deleteBatch(Long[] roleIds) {
        // 删除角色
        sysRoleMapper.deleteBatchIds(Arrays.asList(roleIds));
        // 删除角色菜单关系
        sysRoleMenuService.deleteBatchByRoleId(roleIds);
        // 删除角色用户关系
        sysUserRoleService.deleteBatchByUserIds(roleIds);
    }


    /**
     * 新增角色
     * @param sysRole
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void saveRoleInfo(SysRole sysRole) {
        sysRoleMapper.insert(sysRole);
        // 检查角色是否越权
        checkPerms(sysRole);
        // 保存角色菜单关系
        sysRoleMenuService.saveOrUpdateRoleInfo(sysRole.getRoleId(), sysRole.getMenuIdList());
    }

    /**
     * 更新角色
     * @param sysRole
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void updateRoleInfo(SysRole sysRole) {
        sysRoleMapper.updateById(sysRole);
        // 检查角色是否越权
        checkPerms(sysRole);
        // 保存角色与菜单关系
        sysRoleMenuService.saveOrUpdateRoleInfo(sysRole.getRoleId(), sysRole.getMenuIdList());
    }

    /**
     * 检查角色是否越权
     * @param sysRole
     */
    private void checkPerms(SysRole sysRole) {
        // 若不是超级管理员，则需要判断角色是否越权
        if (SysConstants.SUPER_ADMIN.equals(sysRole.getCreateUserId())) {
            return;
        }
        List<Long> menuIdList = sysUserService.queryAllMenuId(sysRole.getCreateUserId());
        if (!menuIdList.containsAll(sysRole.getMenuIdList())) {
            throw new MyException("新增角色的权限，已超出你的权限范围");
        }
    }
}
