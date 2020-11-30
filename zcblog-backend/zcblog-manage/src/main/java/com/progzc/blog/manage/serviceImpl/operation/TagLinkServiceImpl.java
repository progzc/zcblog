package com.progzc.blog.manage.serviceImpl.operation;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.progzc.blog.common.utils.ValidatorUtils;
import com.progzc.blog.common.validation.AddGroup;
import com.progzc.blog.entity.operation.Tag;
import com.progzc.blog.entity.operation.TagLink;
import com.progzc.blog.manage.service.operation.TagLinkService;
import com.progzc.blog.manage.service.operation.TagService;
import com.progzc.blog.mapper.operation.TagLinkMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * @Description 标签多对多维护表
 * @Author zhaochao
 * @Date 2020-10-25
 * @Email zcprog@foxmail.com
 * @Version V1.0
 */
@Service
public class TagLinkServiceImpl extends ServiceImpl<TagLinkMapper, TagLink> implements TagLinkService {

    @Autowired
    private TagService tagService;

    @Autowired
    private ValidatorUtils validatorUtils;

    /**
     * 批量保存TagLink
     * @param id      文章或相册的id
     * @param tagList
     */
    @Override
    public void saveList(Integer id, List<Tag> tagList) {
        List<TagLink> tagLinks = new ArrayList<>();
        for (Tag tag : tagList) {
            if (tag.getId() == null) {
                validatorUtils.validateEntity(tag, AddGroup.class);
                tagService.saveTag(tag);
            }
            TagLink tagLink = new TagLink();
            tagLink.setTagId(tag.getId());
            tagLink.setLinkId(id);
            tagLink.setType(tag.getType());
            tagLinks.add(tagLink);
        }

        this.saveBatch(tagLinks);
    }
}
