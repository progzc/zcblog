package com.progzc.blog.common.base;

import com.progzc.blog.entity.sys.SysUser;
import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.SecurityUtils;

/**
 * @Description 登录与鉴权控制器基类
 * @Author zhaochao
 * @Date 2020/11/8 15:18
 * @Email zcprog@foxmail.com
 * @Version V1.0
 */
@Slf4j
public class AbstractController {
    protected SysUser getUser() {
        return (SysUser) SecurityUtils.getSubject().getPrincipal();
    }

    protected Long getUserId() {
        return getUser().getUserId();
    }
}
