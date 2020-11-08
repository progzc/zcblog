package com.progzc.blog.configuration;

import lombok.Data;
import org.jasypt.encryption.StringEncryptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

import java.io.Serializable;

/**
 * @Description 七牛云配置类
 * @Author zhaochao
 * @Date 2020/11/4 9:08
 * @Email zcprog@foxmail.com
 * @Version V1.0
 */
@Configuration
@Data
public class QiniuConfig implements Serializable {

    public static final long serialVersionUID = 1L;

    @Autowired
    private StringEncryptor stringEncryptor;

    /**
     * 七牛云外链域名
     */
    @Value("${oss.qiniu.domain}")
    private String qiniuDomain;

    /**
     * 七牛云路径前缀
     */
    @Value("${oss.qiniu.prefix}")
    private String qiniuPrefix;

    /**
     * 七牛云access Key
     */
    @Value("${oss.qiniu.accessKey}")
    private String qiniuAccessKey;

    /**
     * 七牛云secret Key
     */
    @Value("${oss.qiniu.secretKey}")
    private String qiniuSecretKey;

    /**
     * 七牛云空间名
     */
    @Value("${oss.qiniu.bucketName}")
    private String qiniuBucketName;
}
