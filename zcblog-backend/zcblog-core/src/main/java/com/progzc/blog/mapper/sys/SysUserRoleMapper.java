package com.progzc.blog.mapper.sys;

import com.progzc.blog.entity.sys.SysUserRole;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

/**
 * @Description 用户与角色对应关系
 * @Author zhaochao
 * @Date 2020-10-25
 * @Email zcprog@foxmail.com
 * @Version V1.0
 */
@Mapper
public interface SysUserRoleMapper extends BaseMapper<SysUserRole> {

}
