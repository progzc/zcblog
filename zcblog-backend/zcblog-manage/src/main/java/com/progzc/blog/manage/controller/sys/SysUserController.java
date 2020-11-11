package com.progzc.blog.manage.controller.sys;

import com.progzc.blog.common.Result;
import com.progzc.blog.common.base.AbstractController;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @Description 用户
 * @Author zhaochao
 * @Date 2020-10-25
 * @Email zcprog@foxmail.com
 * @Version V1.0
 */
@Slf4j
@RestController
@RequestMapping("/admin/sys/user")
public class SysUserController extends AbstractController {

    /**
     * 获取登录用户的信息
     * @return
     */
    @GetMapping("/info")
    public Result info() {
        log.debug("正在获取用户信息...");
        return Result.ok().put("user", getUser());
    }
}

