package com.progzc.blog.manage.service.oss;

import com.baomidou.mybatisplus.extension.service.IService;
import com.progzc.blog.entity.oss.OssResource;
import org.springframework.web.multipart.MultipartFile;

/**
 * @Description 云存储资源表
 * @Author zhaochao
 * @Date 2020-10-25
 * @Email zcprog@foxmail.com
 * @Version V1.0
 */
public interface OssResourceService extends IService<OssResource> {


    /**
     * 删除：包含图片/文档/...
     * @param url 删除的文档的链接
     */
    void deleteFileByUrl(String url);

    /**
     * 上传：包含图片/文档/...
     * @param file
     * @return
     */
    OssResource upload(MultipartFile file);
}
