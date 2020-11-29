package com.progzc.blog.manage.service.article;

import com.baomidou.mybatisplus.extension.service.IService;
import com.progzc.blog.entity.article.Article;

/**
 * @Description 文章
 * @Author zhaochao
 * @Date 2020-10-25
 * @Email zcprog@foxmail.com
 * @Version V1.0
 */
public interface ArticleService extends IService<Article> {

    /**
     * 根据id查询文章信息
     * @param articleId
     * @return
     */
    Article getArticleInfo(Integer articleId);
}
