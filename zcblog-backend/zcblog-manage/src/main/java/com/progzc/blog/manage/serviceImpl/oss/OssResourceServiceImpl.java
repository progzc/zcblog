package com.progzc.blog.manage.serviceImpl.oss;

import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.progzc.blog.common.enums.ErrorEnum;
import com.progzc.blog.common.exception.MyException;
import com.progzc.blog.entity.oss.OssResource;
import com.progzc.blog.manage.service.oss.CloudStorageService;
import com.progzc.blog.manage.service.oss.OssResourceService;
import com.progzc.blog.mapper.oss.OssResourceMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import java.io.IOException;

/**
 * @Description 云存储资源表
 * @Author zhaochao
 * @Date 2020-10-25
 * @Email zcprog@foxmail.com
 * @Version V1.0
 */
@Slf4j
@Service
public class OssResourceServiceImpl extends ServiceImpl<OssResourceMapper, OssResource> implements OssResourceService {

    @Resource
    private OssResourceMapper ossResourceMapper;

    @Autowired
    private CloudStorageService cloudStorageService;


    /**
     * 上传：包含图片/文档/...
     * @param file
     * @return
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public OssResource upload(MultipartFile file) {
        // 获取文件后缀名
        String originName = file.getOriginalFilename();
        if (originName == null || originName.lastIndexOf(".") < 0) {
            throw new MyException("上传文件必须带后缀名");
        }
        String suffix = originName.substring(originName.lastIndexOf("."));
        String url = null;
        try {
            url = cloudStorageService.uploadSuffix(file.getBytes(), suffix);
        } catch (IOException e) {
            log.error("文件转换为字节数组出错");
            throw new MyException(ErrorEnum.UNKNOWN);
        }
        OssResource ossResource = new OssResource(url, originName);
        ossResourceMapper.insert(ossResource);

        return ossResource;
    }

    /**
     * 删除：包含图片/文档/...
     * @param url 删除的文档的链接
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void deleteFileByUrl(String url) {
        ossResourceMapper.delete(new UpdateWrapper<OssResource>().lambda()
                .eq(OssResource::getUrl, url));
        cloudStorageService.delete(url);
    }
}
