package com.progzc.blog.mapper.sys;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.progzc.blog.entity.sys.SysUser;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * @Description 用户
 * @Author zhaochao
 * @Date 2020-10-25
 * @Email zcprog@foxmail.com
 * @Version V1.0
 */
@Mapper
public interface SysUserMapper extends BaseMapper<SysUser> {

    /**
     * 根据用户id查询权限
     * @param userId
     * @return
     */
    List<String> queryPermsByUserId(Long userId);

    /**
     * 查询用户菜单id
     * @param userId
     * @return
     */
    List<Long> queryAllMenuId(Long userId);
}
