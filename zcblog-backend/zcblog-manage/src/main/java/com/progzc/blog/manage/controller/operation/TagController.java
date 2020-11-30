package com.progzc.blog.manage.controller.operation;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.core.toolkit.ObjectUtils;
import com.progzc.blog.common.Result;
import com.progzc.blog.common.base.AbstractController;
import com.progzc.blog.common.enums.TagTypeEnum;
import com.progzc.blog.common.utils.ValidatorUtils;
import com.progzc.blog.common.validation.AddGroup;
import com.progzc.blog.common.validation.UpdateGroup;
import com.progzc.blog.entity.MyPage;
import com.progzc.blog.entity.operation.Tag;
import com.progzc.blog.entity.operation.TagLink;
import com.progzc.blog.manage.service.operation.TagLinkService;
import com.progzc.blog.manage.service.operation.TagService;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

/**
 * @Description 标签
 * @Author zhaochao
 * @Date 2020-10-25
 * @Email zcprog@foxmail.com
 * @Version V1.0
 */
@Slf4j
//@CacheConfig(cacheNames = RedisCacheNames.TAG)
@RestController
@RequestMapping("/admin/operation/tag")
public class TagController extends AbstractController {

    @Autowired
    private ValidatorUtils validatorUtils;

    @Autowired
    private TagService tagService;

    @Autowired
    private TagLinkService tagLinkService;

    /**
     * 查询标签列表
     * @param params
     * @return
     */
    @GetMapping("/list")
    @RequiresPermissions("operation:tag:list")
//    @Cacheable
    @ApiOperation(value = "查询标签列表")
    public Result list(@RequestParam Map<String, Object> params) {
        MyPage page = tagService.queryPage(params);
        return Result.ok().put("page", page);
    }

    /**
     * 删除标签[列表]
     * @param ids
     * @return
     */
    @DeleteMapping("/delete")
    @RequiresPermissions("operation:tag:delete")
//    @CacheEvict(allEntries = true)
    @ApiOperation(value = "删除标签[列表]")
    public Result delete(@RequestBody Integer[] ids) {
        for (Integer id : ids) {
            List<TagLink> tagLinks = tagLinkService.list(new QueryWrapper<TagLink>().lambda()
                    .eq(TagLink::getTagId, id));
            if (!CollectionUtils.isEmpty(tagLinks)) {
                Tag tag = tagService.getById(tagLinks.get(0).getTagId());
                if (!ObjectUtils.isEmpty(tag)) {
                    if (tag.getType().equals(TagTypeEnum.ARTILCLE.getValue())) {
                        return Result.error("该标签下有文章，无法删除");
                    }
                    if (tag.getType().equals(TagTypeEnum.GALLERY.getValue())) {
                        return Result.error("该标签下有相册，无法删除");
                    }
                }
            }
        }
        tagService.removeByIds(Arrays.asList(ids));
        return Result.ok();
    }

    /**
     * 根据id查询标签信息
     * @param id
     * @return
     */
    @GetMapping("/info/{id}")
    @RequiresPermissions("operation:tag:info")
//    @Cacheable
    @ApiOperation(value = "根据id查询标签信息")
    public Result info(@PathVariable("id") Integer id) {
        Tag tag = tagService.getById(id);
        return Result.ok().put("tag", tag);
    }

    /**
     * 新增标签
     * @param tag
     * @return
     */
    @PostMapping("/save")
    @RequiresPermissions("operation:tag:save")
//    @CacheEvict(allEntries = true)
    @ApiOperation(value = "新增标签")
    public Result save(@RequestBody Tag tag) {
        validatorUtils.validateEntity(tag, AddGroup.class);
        tagService.saveTag(tag);
        return Result.ok();
    }


    /**
     * 修改标签
     * @param tag
     * @return
     */
    @PutMapping("/update")
    @RequiresPermissions("operation:tag:update")
//    @CacheEvict(allEntries = true)
    @ApiOperation(value = "修改标签")
    public Result update(@RequestBody Tag tag) {
        validatorUtils.validateEntity(tag, UpdateGroup.class);
        List<Tag> tagList = tagService.list(new UpdateWrapper<Tag>().lambda()
                .eq(Tag::getName, tag.getName()).eq(Tag::getType, tag.getType()));
        if (!CollectionUtils.isEmpty(tagList)) {
            return Result.error("系统中已存在该标签，请重新修改");
        }
        // 乐观锁
        Tag oldTag = tagService.getById(tag);
        oldTag.setName(tag.getName());
        oldTag.setType(tag.getType());
        tagService.updateById(oldTag);
        return Result.ok();
    }

    /**
     * 根据标签类别查询所有标签
     * @param type
     * @return
     */
    @GetMapping("/select")
    @RequiresPermissions("operation:tag:list")
    @ApiOperation(value = "根据标签类别查询所有标签")
    public Result select(@RequestParam("type") Integer type) {
        if (type != null) {
            List<Tag> tagList = tagService.list(new QueryWrapper<Tag>().lambda()
                    .eq(Tag::getType, type));
            return Result.ok().put("tagList", tagList);
        }
        return Result.error("该标签类别不存在，请重试");
    }
}

