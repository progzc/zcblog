package com.progzc.blog.common.constants;

/**
 * @Description 定义Redis缓存空间
 * @Author zhaochao
 * @Date 2020/11/3 15:26
 * @Email zcprog@foxmail.com
 * @Version V1.0
 */
public class RedisCacheNames {

    /**
     * 缓存空间统一前缀
     */
    public final static String PREFIX = "ZCBLOG:";

    /**
     * 文章缓存空间名称
     */
    public final static String ARTICLE = PREFIX + "ARTICLE";

    /**
     * 相册缓存空间名称
     */
    public final static String GALLERY = PREFIX + "GALLERY";

    /**
     * 标签空间名称
     */
    public final static String TAG = PREFIX + "TAG";

}
