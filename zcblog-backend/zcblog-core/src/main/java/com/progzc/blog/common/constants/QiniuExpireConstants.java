package com.progzc.blog.common.constants;

/**
 * @Description 七牛云云存储过期时间常量类
 * @Author zhaochao
 * @Date 2020/11/30 19:38
 * @Email zcprog@foxmail.com
 * @Version V1.0
 */
public class QiniuExpireConstants {

    /**
     * -1表示不过期
     */
    public static final int NOT_EXPIRE = -1;

    /**
     * 删除OSS资源时，备份文件过期时间为30天
     */
    public static final int DELETE_EXPIRE = 30;

    /**
     * 文件上传时的过期时间为30天
     */
    public static final int ADD_EXPIRE = 30;
    
}
