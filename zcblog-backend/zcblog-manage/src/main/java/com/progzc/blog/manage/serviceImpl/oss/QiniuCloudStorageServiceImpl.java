package com.progzc.blog.manage.serviceImpl.oss;

import com.progzc.blog.common.enums.ErrorEnum;
import com.progzc.blog.common.exception.MyException;
import com.progzc.blog.configuration.QiniuConfig;
import com.progzc.blog.manage.service.oss.CloudStorageService;
import com.qiniu.common.Zone;
import com.qiniu.http.Response;
import com.qiniu.storage.Configuration;
import com.qiniu.storage.UploadManager;
import com.qiniu.util.Auth;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.IOUtils;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.io.InputStream;

/**
 * @Description 七牛云存储服务实现类
 * @Author zhaochao
 * @Date 2020/11/29 0:14
 * @Email zcprog@foxmail.com
 * @Version V1.0
 */
@Service
@Slf4j
public class QiniuCloudStorageServiceImpl extends CloudStorageService {
    
    private UploadManager uploadManager;
    private String token;
    private Auth auth;

    public QiniuCloudStorageServiceImpl(QiniuConfig config) {
        this.config = config;
        //初始化
        init();
    }

    private void init() {
        uploadManager = new UploadManager(new Configuration(Zone.autoZone()));
        auth = Auth.create(config.getQiniuAccessKey(), config.getQiniuSecretKey());
        token = auth.uploadToken(config.getQiniuBucketName());
    }

    /**
     * 文件上传
     * @param data 文件字节数组
     * @param path 文件路径，包含文件名
     * @return 返回http地址
     */
    @Override
    public String upload(byte[] data, String path) {
        try {
            token = auth.uploadToken(config.getQiniuBucketName());
            Response res = uploadManager.put(data, path, token);
            if (!res.isOK()) {
                throw new RuntimeException("上传七牛出错：" + res.toString());
            }
        } catch (Exception e) {
            log.error(e.getMessage());
            throw new MyException(ErrorEnum.OSS_CONFIG_ERROR);
        }

        return config.getQiniuDomain() + "/" + path;
    }

    /**
     * 文件上传
     * @param inputStream 字节流
     * @param path        文件路径，包含文件名
     * @return 返回http地址
     */
    @Override
    public String upload(InputStream inputStream, String path) {
        try {
            byte[] data = IOUtils.toByteArray(inputStream);
            return this.upload(data, path);
        } catch (IOException e) {
            log.error(e.getMessage());
            throw new MyException(ErrorEnum.OSS_CONFIG_ERROR);
        }
    }

    /**
     * 文件上传
     * @param data   文件字节数组
     * @param suffix 后缀
     * @return 返回http地址
     */
    @Override
    public String uploadSuffix(byte[] data, String suffix) {
        return upload(data, getPath(config.getQiniuPrefix(), suffix));
    }

    /**
     * 文件上传
     * @param inputStream 字节流
     * @param suffix      后缀
     * @return 返回http地址
     */
    @Override
    public String uploadSuffix(InputStream inputStream, String suffix) {
        return upload(inputStream, getPath(config.getQiniuPrefix(), suffix));
    }
}
