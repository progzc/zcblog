package com.progzc.blog;

import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.progzc.blog.common.utils.JsonUtils;
import com.progzc.blog.common.utils.RedisUtils;
import com.progzc.blog.entity.sys.vo.SysLoginForm;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * @Description Redis工具类测试
 * @Author zhaochao
 * @Date 2020/11/9 22:31
 * @Email zcprog@foxmail.com
 * @Version V1.0
 */
@RunWith(SpringRunner.class)
@SpringBootTest
public class RedisUtilsTest {

    @Autowired
    private RedisUtils redisUtils;

    private ObjectMapper objMapper = new ObjectMapper();

    /**
     * 测试普通字符串
     */
    @Test
    public void test1() {
        redisUtils.set("abc", "abc11111", 60 * 5L);
        String str = redisUtils.getObj("abc", String.class);
        System.out.println(str); // 输出abc11111
    }

    /**
     * 测试对象
     */
    @Test
    public void test2() {
        SysLoginForm sysLoginForm = new SysLoginForm();
        sysLoginForm.setCaptcha("abcde");
        sysLoginForm.setUsername("admin123");
        sysLoginForm.setPassword("admin123");
        sysLoginForm.setUuid("uuid");

        redisUtils.set("abc2", sysLoginForm, 60 * 5L);
        SysLoginForm str2 = redisUtils.getObj("abc2", SysLoginForm.class);
        System.out.println(str2);
    }

    /**
     * 测试集合
     */
    @Test
    public void test3() {
        List<SysLoginForm> list = new ArrayList<>();
        for (int i = 0; i < 4; i++) {
            SysLoginForm sysLoginForm = new SysLoginForm();
            sysLoginForm.setCaptcha("abcde" + i);
            sysLoginForm.setUsername("admin" + i);
            sysLoginForm.setPassword("admin" + i);
            sysLoginForm.setUuid("uuid" + i);
            list.add(sysLoginForm);
        }
        redisUtils.set("abc3", list, 60 * 5L);
        redisUtils.getObj("abc3", SysLoginForm.class); // 报异常
    }

    /**
     * 测试集合
     */
    @Test
    public void test4() throws IOException {
        List<SysLoginForm> list = new ArrayList<>();
        for (int i = 0; i < 4; i++) {
            SysLoginForm sysLoginForm = new SysLoginForm();
            sysLoginForm.setCaptcha("abcde" + i);
            sysLoginForm.setUsername("admin" + i);
            sysLoginForm.setPassword("admin" + i);
            sysLoginForm.setUuid("uuid" + i);
            list.add(sysLoginForm);
        }
        redisUtils.set("abc3", list, 60 * 5L);
        String str4 = redisUtils.getObj("abc3", String.class);
        JavaType javaType = getCollectionType(ArrayList.class, SysLoginForm.class);
        List<SysLoginForm> queryList = objMapper.readValue(str4, javaType);
        queryList.forEach(System.out::println);
    }

    private JavaType getCollectionType(Class<?> collectionClass, Class<?>... elementClasses) {
        return objMapper.getTypeFactory().constructParametricType(collectionClass, elementClasses);
    }

    /**
     * 测试集合
     */
    @Test
    public void test5() {
        List<SysLoginForm> list = new ArrayList<>();
        for (int i = 0; i < 4; i++) {
            SysLoginForm sysLoginForm = new SysLoginForm();
            sysLoginForm.setCaptcha("abcde" + i);
            sysLoginForm.setUsername("admin" + i);
            sysLoginForm.setPassword("admin" + i);
            sysLoginForm.setUuid("uuid" + i);
            list.add(sysLoginForm);
        }
        redisUtils.set("abc3", list, 60 * 5L);
        String str5 = redisUtils.getObj("abc3", String.class);
        ArrayList<SysLoginForm> queryList = JsonUtils.toObjArray(str5, ArrayList.class, SysLoginForm.class);
        queryList.forEach(System.out::println);
    }
}
