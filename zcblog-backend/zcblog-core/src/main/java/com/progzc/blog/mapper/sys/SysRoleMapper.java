package com.progzc.blog.mapper.sys;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.progzc.blog.entity.sys.SysRole;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * @Description 角色
 * @Author zhaochao
 * @Date 2020-10-25
 * @Email zcprog@foxmail.com
 * @Version V1.0
 */
@Mapper
public interface SysRoleMapper extends BaseMapper<SysRole> {

    /**
     * 根据创建者查询角色列表
     * @param createUserId
     * @return
     */
    List<Long> queryRoleIdList(Long createUserId);
}
