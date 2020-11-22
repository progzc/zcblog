package com.progzc.blog.manage.controller.operation;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.ObjectUtils;
import com.progzc.blog.common.Result;
import com.progzc.blog.common.base.AbstractController;
import com.progzc.blog.common.enums.TagTypeEnum;
import com.progzc.blog.common.utils.ValidatorUtils;
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
    @ApiOperation(value = "删除标签[列表]")
    public Result delete(@RequestBody String[] ids) {
        for (String id : ids) {
            List<TagLink> tagLinks = tagLinkService.list(new QueryWrapper<TagLink>().lambda()
                    .eq(TagLink::getTagId, id)); // 注意这里：TagLink::getTagId返回Integer类型，id是字符串类型？？
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
    @ApiOperation(value = "根据id查询标签信息")
    public Result info(@PathVariable("id") String id) {
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
    @ApiOperation(value = "新增标签")
    public Result save(@RequestBody Tag tag) {
        validatorUtils.validateEntity(tag);
        List<Tag> tagList = tagService.list(new QueryWrapper<Tag>().lambda()
                .eq(Tag::getName, tag.getName()).eq(Tag::getType, tag.getType()));
        if (CollectionUtils.isEmpty(tagList)) {
            return Result.error("系统中已存在该标签，请重新添加");
        }
        tagService.save(tag);
        return Result.ok();
    }
}

