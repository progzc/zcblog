package com.progzc.blog.manage.service.oss;

import com.progzc.blog.common.utils.DateUtils;
import com.progzc.blog.configuration.QiniuConfig;
import org.apache.commons.lang.StringUtils;

import java.io.InputStream;
import java.util.Date;
import java.util.UUID;

/**
 * @Description 提供将文件上传至OSS的服务
 * @Author zhaochao
 * @Date 2020/11/28 23:48
 * @Email zcprog@foxmail.com
 * @Version V1.0
 */
public abstract class CloudStorageService {
    /**
     * 云存储配置信息
     */
    protected QiniuConfig config;

    /**
     * 根据后缀和前缀生成文件路径
     * @param prefix 前缀
     * @param suffix 后缀
     * @return 返回上传路径
     */
    public String getPath(String prefix, String suffix) {
        //生成uuid
        String uuid = UUID.randomUUID().toString().replaceAll("-", "");
        //文件路径
        String path = DateUtils.format(new Date(), "yyyy/MM/dd") + "/" + uuid;

        if (StringUtils.isNotBlank(prefix)) {
            path = prefix + "/" + path;
        }

        // 上传路径即：前缀/yyyy/MM/dd/随机uuid + 后缀
        return path + suffix;
    }

    /**
     * 上传本地文件
     * @param filePath 本地文件路径
     * @return
     */
    public abstract String upload(String filePath);

    /**
     * 字节数组上传：文件路径（包含文件名）
     * @param data 文件字节数组
     * @param path 文件路径，包含文件名
     * @return 返回http地址
     */
    public abstract String upload(byte[] data, String path);

    /**
     * 数据流上传：文件路径（包含文件名）
     * @param inputStream 字节流
     * @param path        文件路径，包含文件名
     * @return 返回http地址
     */
    public abstract String upload(InputStream inputStream, String path);

    /**
     * 字节数组上传：只包含后缀
     * @param data   文件字节数组
     * @param suffix 后缀
     * @return 返回http地址
     */
    public abstract String uploadSuffix(byte[] data, String suffix);

    /**
     * 数据流上传：只包含后缀
     * @param inputStream 字节流
     * @param suffix      后缀
     * @return 返回http地址
     */
    public abstract String uploadSuffix(InputStream inputStream, String suffix);

    /**
     * 删除文件：根据url删除文件
     * @param url
     * @return
     */
    public abstract void delete(String url);
}
