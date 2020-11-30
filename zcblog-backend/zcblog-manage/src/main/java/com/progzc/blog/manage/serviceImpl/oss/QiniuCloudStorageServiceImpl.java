package com.progzc.blog.manage.serviceImpl.oss;

import com.progzc.blog.common.constants.QiniuExpireConstants;
import com.progzc.blog.common.enums.ErrorEnum;
import com.progzc.blog.common.exception.MyException;
import com.progzc.blog.configuration.QiniuConfig;
import com.progzc.blog.manage.service.oss.CloudStorageService;
import com.qiniu.common.QiniuException;
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
    private String bucket;

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
        bucket = config.getQiniuBucketName();
        token = auth.uploadToken(bucket);
    }

    /**
     * 上传本地文件: 不过期
     * @param filePath 本地文件路径
     * @param day      设置多少天后过期
     * @return
     */
    @Override
    public String upload(String filePath, int day) {
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
            token = auth.uploadToken(bucket);
            Response res = uploadManager.put(filePath, path, token);
            // 判断响应是否成功，并且是否设置过期时间
            postProcess(res, path, day);
        } catch (Exception e) {
            log.error(e.getMessage());
            throw new MyException(ErrorEnum.OSS_ARTICLE_UPLOAD_ERROR);
        }

        return config.getQiniuDomain() + "/" + path;
    }

    /**
     * 字节数组上传：文件路径（包含文件名）
     * @param data 文件字节数组
     * @param path 文件路径，包含文件名
     * @param day  设置多少天后过期
     * @return 返回http地址
     */
    @Override
    public String upload(byte[] data, String path, int day) {
        try {
            // token重新获取是防止由于token失效导致的文件上传失败
            token = auth.uploadToken(bucket);
            Response res = uploadManager.put(data, path, token);
            // 判断响应是否成功，并且是否设置过期时间
            postProcess(res, path, day);
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
     * @param day         设置多少天后过期
     * @return 返回http地址
     */
    @Override
    public String upload(InputStream inputStream, String path, int day) {
        try {
            byte[] data = IOUtils.toByteArray(inputStream);
            return this.upload(data, path, day);
        } catch (IOException e) {
            log.error(e.getMessage());
            throw new MyException(ErrorEnum.OSS_ARTICLE_UPLOAD_ERROR);
        }
    }

    /**
     * 字节数组上传：只包含后缀
     * @param data   文件字节数组
     * @param suffix 后缀
     * @param day    设置多少天后过期
     * @return 返回http地址
     */
    @Override
    public String uploadSuffix(byte[] data, String suffix, int day) {
        return upload(data, getPath(config.getQiniuPrefix(), suffix), day);
    }

    /**
     * 数据流上传：只包含后缀（30天过期）
     * @param inputStream 字节流
     * @param suffix      后缀
     * @param day         设置多少天后过期
     * @return 返回http地址
     */
    @Override
    public String uploadSuffix(InputStream inputStream, String suffix, int day) {
        return upload(inputStream, getPath(config.getQiniuPrefix(), suffix), day);
    }

    /**
     * 批量设置文件不过期
     * @param urlList
     */
    @Override
    public void setNotExpire(String[] urlList) {
        BucketManager.BatchOperations batchOperations = new BucketManager.BatchOperations();
        // 将数据中的url转换为OSS中的路径
        toPathKey(urlList);
        // 批量复制和删除及重命名
        for (String url : urlList) {
            String suffix = url.substring(url.lastIndexOf("."));
            String temp = url.substring(0, url.lastIndexOf(".")) + "-copy" + suffix;
            batchOperations.addCopyOp(bucket, url, bucket, temp);
            batchOperations.addDeleteOp(bucket, url);
            batchOperations.addMoveOp(bucket, temp, bucket, url);
        }
        try {
            bucketManager.batch(batchOperations);
        } catch (IOException e) {
            log.error(e.getMessage());
            throw new MyException(ErrorEnum.OSS_ARTICLE_DELETE_ERROR);
        }
    }


    /**
     * 批量删除文件
     * @param urlList
     */
    @Override
    public void deleteBatch(String[] urlList, int day) {
        BucketManager.BatchOperations batchOperations = new BucketManager.BatchOperations();
        // 将数据中的url转换为OSS中的路径
        toPathKey(urlList);
        // 批量删除及备份
        for (String url : urlList) {
            String suffix = url.substring(url.lastIndexOf("."));
            String backup = url.substring(0, url.lastIndexOf(".")) + "-backup" + suffix;
            batchOperations.addCopyOp(bucket, url, bucket, backup);
            batchOperations.addDeleteAfterDaysOps(bucket, day, backup);
            batchOperations.addDeleteOp(bucket, url);
        }
        try {
            bucketManager.batch(batchOperations);
        } catch (IOException e) {
            log.error(e.getMessage());
            throw new MyException(ErrorEnum.OSS_ARTICLE_DELETE_ERROR);
        }
    }

    // 判断响应是否成功，并且是否设置过期时间
    private void postProcess(Response res, String path, int day) {
        if (!res.isOK()) {
            throw new RuntimeException("上传七牛出错：" + res.toString());
        } else {
            if (day != QiniuExpireConstants.NOT_EXPIRE) {
                try {
                    bucketManager.deleteAfterDays(bucket, path, day);
                } catch (QiniuException e) {
                    log.error(e.getMessage());
                    throw new MyException(ErrorEnum.OSS_EXPIRE_ERROR);
                }
            }
        }
    }

    // 将数据中的url转换为OSS中的路径
    // 例如：数据库中：http://cloud.progzc.com/blog/2020/11/29/a4b1ea5bfb5b46af81e124ab6e201374.png
    //      OSS中：blog/2020/11/29/a4b1ea5bfb5b46af81e124ab6e201374.png
    private void toPathKey(String[] urlList) {
        for (int i = 0; i < urlList.length; i++) {
            urlList[i] = urlList[i].substring(config.getQiniuDomain().length() + 1);
        }
    }
}
