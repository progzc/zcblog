package com.progzc.blog.manage.controller.oss;

import com.progzc.blog.common.Result;
import com.progzc.blog.common.base.AbstractController;
import com.progzc.blog.common.exception.MyException;
import com.progzc.blog.entity.oss.OssResource;
import com.progzc.blog.manage.service.oss.CloudStorageService;
import com.progzc.blog.manage.service.oss.OssResourceService;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

/**
 * @Description 云存储资源表
 * @Author zhaochao
 * @Date 2020-10-25
 * @Email zcprog@foxmail.com
 * @Version V1.0
 */
@Slf4j
@RestController
@RequestMapping("/admin/oss/resource")
public class OssResourceController extends AbstractController {

    @Autowired
    private OssResourceService ossResourceService;

    @Autowired
    private CloudStorageService cloudStorageService;

    /**
     * 上传：包含图片/文档/...
     * @param file
     * @return
     */
    @PostMapping("/upload")
    @ApiOperation(value = "上传文件至OSS")
    public Result upload(MultipartFile file) {
        if (file == null || file.isEmpty()) {
            throw new MyException("上传文件不能为空");
        }

        OssResource ossResource = ossResourceService.upload(file);
        return Result.ok().put("resource", ossResource);
    }

    /**
     * 删除：包含图片/文档/...
     * @param url 删除的文档的链接
     * @return
     */
    @DeleteMapping("/delete")
    @ApiOperation(value = "删除OSS里的文件")
    public Result delete(@RequestParam String url) {
        if (StringUtils.isBlank(url)) {
            throw new MyException("删除的文件不能为空");
        }

        ossResourceService.deleteFileByUrl(url);
        return Result.ok();
    }

}

