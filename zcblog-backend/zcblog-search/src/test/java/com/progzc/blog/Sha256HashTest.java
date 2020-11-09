package com.progzc.blog;

import com.progzc.blog.entity.sys.SysUser;
import com.progzc.blog.mapper.sys.SysUserMapper;
import org.apache.commons.lang.RandomStringUtils;
import org.apache.shiro.crypto.hash.Sha256Hash;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

/**
 * @Description Sha256散列算法测试
 * @Author zhaochao
 * @Date 2020/11/9 10:17
 * @Email zcprog@foxmail.com
 * @Version V1.0
 */
@RunWith(SpringRunner.class)
@SpringBootTest
public class Sha256HashTest {

    @Autowired
    private SysUserMapper sysUserMapper;

    @Test
    public void test(){

        String username = "admin123";
        String password = "admin123";
        String salt = RandomStringUtils.randomAlphanumeric(20);
        String email = "zcprog@foxmail.com";
        String encryptPassword = new Sha256Hash(password, salt).toString();

        System.out.println(salt);
        System.out.println(encryptPassword);

        SysUser sysUser = new SysUser();
        sysUser.setUsername(username);
        sysUser.setPassword(encryptPassword);
        sysUser.setSalt(salt);
        sysUser.setEmail(email);
        sysUser.setCreateUserId(1L);

        sysUserMapper.insert(sysUser);

    }
}
