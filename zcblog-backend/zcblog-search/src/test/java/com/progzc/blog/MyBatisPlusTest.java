package com.progzc.blog;

import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.progzc.blog.entity.sys.SysRoleMenu;
import com.progzc.blog.mapper.sys.SysRoleMenuMapper;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import javax.annotation.Resource;
import java.util.Arrays;

/**
 * @Description MyBatisPlus测试类
 * @Author zhaochao
 * @Date 2020/11/26 15:14
 * @Email zcprog@foxmail.com
 * @Version V1.0
 */
@RunWith(SpringRunner.class)
@SpringBootTest
public class MyBatisPlusTest {

    @Resource
    private SysRoleMenuMapper sysRoleMenuMapper;

    /**
     * MyBatisPlus的条件构造器有一个非常重要的细节需要注意：
     * 即在条件构造器后添加单个条件约束时，当条件约束的condition为false时，
     * new UpdateWrapper<>().lambda()表示选择所有（特别是在删除/更新操作时非常危险），
     * 因此谨慎使用带condition的单个条件约束的条件构造器来操作数据库。
     */
    @Test
    public void test() {
        Long[] roleIds = {2L, 1L};
        Arrays.stream(roleIds).forEach(roleId -> {
            sysRoleMenuMapper.delete(new UpdateWrapper<SysRoleMenu>().lambda()
                    .eq(false, SysRoleMenu::getRoleId, roleId)); // eq条件为false时，会删除所有
        });
    }
}
