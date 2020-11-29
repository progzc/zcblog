package com.progzc.blog.manage.serviceImpl.oss;

import com.progzc.blog.common.enums.ErrorEnum;
import com.progzc.blog.common.exception.MyException;
import com.progzc.blog.configuration.QiniuConfig;
import com.progzc.blog.manage.service.oss.CloudStorageService;
import com.qiniu.http.Response;
import com.qiniu.storage.BucketManager;
import com.qiniu.storage.Configuration;
import com.qiniu.storage.Region;
import com.qiniu.storage.UploadManager;
import com.qiniu.util.Auth;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang.StringUtils;
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
    private BucketManager bucketManager;
    private String token;
    private Auth auth;

    /**
     * 备份时间为30天
     */
    private static final int BACKUP_EXPIRE_DAY = 30;

    public QiniuCloudStorageServiceImpl(QiniuConfig config) {
        this.config = config;
        //初始化
        init();
    }

    private void init() {
        Configuration cfg = new Configuration(Region.autoRegion());
        uploadManager = new UploadManager(cfg);
        auth = Auth.create(config.getQiniuAccessKey(), config.getQiniuSecretKey());
        bucketManager = new BucketManager(auth, cfg);
        token = auth.uploadToken(config.getQiniuBucketName());
    }

    /**
     * 上传本地文件
     * @param filePath 本地文件路径
     * @return
     */
    @Override
    public String upload(String filePath) {
        if (StringUtils.isEmpty(filePath)) {
            throw new MyException("上传文件不能为空");
        }
        if (filePath.lastIndexOf(".") < 0) {
            throw new MyException("上传文件必须带后缀名");
        }
        String suffix = filePath.substring(filePath.lastIndexOf("."));
        String path = getPath(config.getQiniuPrefix(), suffix);
        try {
            // token重新获取是防止由于token失效导致的文件上传失败
            token = auth.uploadToken(config.getQiniuBucketName());
            Response res = uploadManager.put(filePath, path, token);
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
     * 字节数组上传：文件路径（包含文件名）
     * @param data 文件字节数组
     * @param path 文件路径，包含文件名
     * @return 返回http地址
     */
    @Override
    public String upload(byte[] data, String path) {
        try {
            // token重新获取是防止由于token失效导致的文件上传失败
            token = auth.uploadToken(config.getQiniuBucketName());
            Response res = uploadManager.put(data, path, token);
            if (!res.isOK()) {
                throw new RuntimeException("上传七牛出错：" + res.toString());
            }
        } catch (Exception e) {
            log.error(e.getMessage());
            throw new MyException(ErrorEnum.OSS_ARTICLE_UPLOAD_ERROR);
        }

        return config.getQiniuDomain() + "/" + path;
    }

    /**
     * 数据流上传：文件路径（包含文件名）
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
            throw new MyException(ErrorEnum.OSS_ARTICLE_UPLOAD_ERROR);
        }
    }

    /**
     * 字节数组上传：只包含后缀
     * @param data   文件字节数组
     * @param suffix 后缀
     * @return 返回http地址
     */
    @Override
    public String uploadSuffix(byte[] data, String suffix) {
        return upload(data, getPath(config.getQiniuPrefix(), suffix));
    }

    /**
     * 数据流上传：只包含后缀
     * @param inputStream 字节流
     * @param suffix      后缀
     * @return 返回http地址
     */
    @Override
    public String uploadSuffix(InputStream inputStream, String suffix) {
        return upload(inputStream, getPath(config.getQiniuPrefix(), suffix));
    }

    /**
     * 删除文件：根据url删除文件
     * 注意：为了安全操作，这里的删除并非真正的删除，会先生成一个具有过期时间（一个月）的副本备份，然后再删除源文件
     * 作用：类似于回收站功能
     * @param url
     * @return
     */
    @Override
    public void delete(String url) {
        try {
            String bucket = config.getQiniuBucketName();
            String src = url.substring(config.getQiniuDomain().length() + 1);
            String suffix = src.substring(src.lastIndexOf("."));
            String backup = src.substring(0, src.lastIndexOf(".")) + "-backup" + suffix;
            // 复制副本
            bucketManager.copy(bucket, src, bucket, backup);
            // 设置副本生命周期为30天
            bucketManager.deleteAfterDays(bucket, backup, BACKUP_EXPIRE_DAY);
            // 删除原文件
            bucketManager.delete(bucket, src);
        } catch (IOException e) {
            log.error(e.getMessage());
            throw new MyException(ErrorEnum.OSS_ARTICLE_DELETE_ERROR);
        }
    }
}
