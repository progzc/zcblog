package com.progzc.blog.manage.service.article;

import com.baomidou.mybatisplus.extension.service.IService;
import com.progzc.blog.entity.MyPage;
import com.progzc.blog.entity.article.Article;

import java.util.Map;

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

    /**
     * 新增文章
     * @param article
     */
    void saveArticle(Article article);

    /**
     * 更新文章
     * @param article
     */
    void updateArticle(Article article);

    /**
     * 查询文章列表
     * @param params
     * @return
     */
    MyPage queryPage(Map<String, Object> params);

    /**
     * 删除文章
     * @param articleIds
     */
    void deleteBatch(Integer[] articleIds);
}
