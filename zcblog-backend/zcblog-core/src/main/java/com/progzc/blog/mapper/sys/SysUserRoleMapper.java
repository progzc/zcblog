package com.progzc.blog.mapper.sys;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.progzc.blog.entity.sys.SysUserRole;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * @Description 用户与角色对应关系
 * @Author zhaochao
 * @Date 2020-10-25
 * @Email zcprog@foxmail.com
 * @Version V1.0
 */
@Mapper
public interface SysUserRoleMapper extends BaseMapper<SysUserRole> {

    /**
     * 根据用户id获取用户角色id列表
     * @param userId
     * @return
     */
    List<Long> queryRoleIdList(Long userId);
}
