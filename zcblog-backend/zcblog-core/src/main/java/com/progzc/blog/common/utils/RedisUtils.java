package com.progzc.blog.common.utils;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.*;
import org.springframework.stereotype.Component;

import java.util.concurrent.TimeUnit;

/**
 * @Description Redis工具类
 * @Author zhaochao
 * @Date 2020/11/3 22:14
 * @Email zcprog@foxmail.com
 * @Version V1.0
 */
@Component
public class RedisUtils {

    @Autowired
    private RedisTemplate<String, Object> redisTemplate;

    @Autowired
    private ValueOperations<String, Object> valueOperations;
    @Autowired
    private ListOperations<String, Object> listOperations;
    @Autowired
    private SetOperations<String, Object> setOperations;
    @Autowired
    private ZSetOperations<String, Object> zSetOperations;
    @Autowired
    private HashOperations<String, String, Object> hashOperations;

    /**
     * 默认过期时长（单位：秒）
     */
    public static final long DEFAULT_EXPIRE = 60 * 60 * 24L;
    /**
     * 不设置过期时长：永不过期
     */
    public static final long NOT_EXPIRE = -1;

    /**
     * String类型设置key-vue及过期时间
     */
    public void set(String key, Object value, long expire) {
        // 若是字符串则直接存储；若是对象先转化为json字符串再存储
        if (value.getClass() == String.class) {
            valueOperations.set(key, value);
        } else {
            valueOperations.set(key, JsonUtils.toJson(value));
        }

        if (expire != NOT_EXPIRE) {
            redisTemplate.expire(key, expire, TimeUnit.SECONDS);
        }
    }

    /**
     * String类型设置key-value,默认过期时间为1天
     */
    public void set(String key, Object value) {
        set(key, value, DEFAULT_EXPIRE);
    }

    /**
     * String类型根据key获取value,同时设置过期时间
     */
    public <T> T getObj(String key, Class<T> clazz, long expire) {
        String value = (String) valueOperations.get(key);
        if (expire != NOT_EXPIRE) {
            redisTemplate.expire(key, expire, TimeUnit.SECONDS);
        }

        // 若获取的是字符串，直接返回即可；若获取的是对象，则将json字符串转化为对象再返回
        return clazz == String.class ? (T) value : JsonUtils.toObj(value, clazz);
    }

    /**
     * String类型根据key获取value,不设置过期时间
     */
    public <T> T getObj(String key, Class<T> clazz) {
        return getObj(key, clazz, NOT_EXPIRE);
    }

    /**
     * String类型根据key删除value
     */
    public Boolean delete(String key) {
        return redisTemplate.delete(key);
    }

    /**
     * String类型根据key更新过期时间为1天
     */
    public Boolean update(String key) {
        return redisTemplate.expire(key, DEFAULT_EXPIRE, TimeUnit.SECONDS);
    }
}
