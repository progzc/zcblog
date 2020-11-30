package com.progzc.blog.manage.service.operation;

import com.baomidou.mybatisplus.extension.service.IService;
import com.progzc.blog.entity.operation.Tag;
import com.progzc.blog.entity.operation.TagLink;

import java.util.List;

/**
 * @Description 标签多对多维护表
 * @Author zhaochao
 * @Date 2020-10-25
 * @Email zcprog@foxmail.com
 * @Version V1.0
 */
public interface TagLinkService extends IService<TagLink> {

    /**
     * 批量保存TagLink
     * @param id      文章或相册的id
     * @param tagList
     */
    void saveList(Integer id, List<Tag> tagList);
}
