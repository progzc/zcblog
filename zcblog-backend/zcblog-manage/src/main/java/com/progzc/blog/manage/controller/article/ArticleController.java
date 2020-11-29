package com.progzc.blog.manage.controller.article;

import com.progzc.blog.common.Result;
import com.progzc.blog.common.base.AbstractController;
import com.progzc.blog.common.utils.EncryptUtils;
import com.progzc.blog.entity.article.Article;
import com.progzc.blog.manage.service.article.ArticleService;
import io.swagger.annotations.ApiOperation;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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
        if (article.getPassword() != null) {
            article.setPassword(EncryptUtils.encrypt(article.getPassword()));
        }
        return Result.ok().put("article", article);
    }
}
