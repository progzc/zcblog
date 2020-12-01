package com.progzc.blog.manage.controller.article;

import com.progzc.blog.common.Result;
import com.progzc.blog.common.base.AbstractController;
import com.progzc.blog.common.constants.RedisCacheNames;
import com.progzc.blog.common.exception.MyException;
import com.progzc.blog.common.utils.EncryptUtils;
import com.progzc.blog.common.utils.ValidatorUtils;
import com.progzc.blog.common.validation.AddGroup;
import com.progzc.blog.common.validation.UpdateGroup;
import com.progzc.blog.entity.MyPage;
import com.progzc.blog.entity.article.Article;
import com.progzc.blog.manage.service.article.ArticleService;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.lang.StringUtils;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.Map;
import java.util.Set;

/**
 * @Description 文章
 * @Author zhaochao
 * @Date 2020-10-25
 * @Email zcprog@foxmail.com
 * @Version V1.0
 */
@RestController
@RequestMapping("/admin/article")
public class ArticleController extends AbstractController {

    @Autowired
    private RedisTemplate<String, Object> redisTemplate;

    @Autowired
    private ValidatorUtils validatorUtils;

    @Autowired
    private ArticleService articleService;

    /**
     * 根据id查询文章信息
     * @param articleId
     * @return
     */
    @GetMapping("/info/{articleId}")
    @RequiresPermissions("article:list")
    @ApiOperation(value = "根据id查询文章信息")
    public Result info(@PathVariable Integer articleId) {
        Article article = articleService.getArticleInfo(articleId);
        return Result.ok().put("article", article);
    }

    /**
     * 新增文章
     * @param article
     * @return
     */
    @PostMapping("/save")
    @RequiresPermissions("article:save")
    @ApiOperation(value = "保存文章")
    public Result save(@RequestBody Article article) {
        validatorUtils.validateEntity(article, AddGroup.class);
        checkEncrypt(article);
        articleService.saveArticle(article);
        return Result.ok();
    }

    /**
     * 更新文章
     * @param article
     * @return
     */
    @PutMapping("/update")
    @RequiresPermissions("article:update")
    @ApiOperation(value = "更新文章")
    public Result update(@RequestBody Article article) {
        validatorUtils.validateEntity(article, UpdateGroup.class);
        checkEncrypt(article);
        articleService.updateArticle(article);
        return Result.ok();
    }

    /**
     * 查询文章列表
     * @param params
     * @return
     */
    @GetMapping("/list")
    @RequiresPermissions("article:list")
    @ApiOperation(value = "查询文章列表")
    public Result list(@RequestParam Map<String, Object> params) {
        MyPage page = articleService.queryPage(params);
        return Result.ok().put("page", page);
    }

    /**
     * 删除文章
     * @param articleIds
     * @return
     */
    @DeleteMapping("/delete")
    @RequiresPermissions("article:delete")
    @ApiOperation(value = "删除文章")
    public Result delete(@RequestBody Integer[] articleIds) {
        if (articleIds == null || articleIds.length < 1) {
            throw new MyException("未选中任何待删除的文章");
        }

        articleService.deleteBatch(articleIds);
        return Result.ok();
    }

    @PutMapping("/update/status")
    @RequiresPermissions("article:update")
    @ApiOperation(value = "更新文章的状态：发布/置顶/推荐/")
    public Result updateStatus(@RequestBody Article article) {
        Article oldArticle = articleService.getById(article.getId());
        LocalDateTime now = LocalDateTime.now();
        if (article.getPublish() != null) {
            oldArticle.setPublish(article.getPublish());
        }
        if (article.getTop() != null) {
            oldArticle.setTop(article.getTop());
        }
        if (article.getRecommend() != null) {
            oldArticle.setRecommend(article.getRecommend());
        }
        // 由于自动填充未生效，这里手动插入
        oldArticle.setUpdateTime(now);
        articleService.updateById(oldArticle);

        return Result.ok();
    }

    @DeleteMapping("/cache/refresh")
    @RequiresPermissions("article:cache:refresh")
    @ApiOperation(value = "刷新缓存")
    public Result refresh() {
        Set<String> keys = redisTemplate.keys(RedisCacheNames.PREFIX + "*");
        redisTemplate.delete(keys);
        return Result.ok();
    }

    private void checkEncrypt(Article article) {
        if (article.getNeedEncrypt().equals(false)) {
            article.setPassword(null);
        } else {
            if (StringUtils.isBlank(article.getPassword())
                    || StringUtils.isBlank(EncryptUtils.decrypt(article.getPassword()))) {
                throw new MyException("您已选择加密文章，却未设置加密密码");
            }
        }
    }
}
