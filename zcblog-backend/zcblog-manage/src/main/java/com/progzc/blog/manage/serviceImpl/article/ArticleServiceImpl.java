package com.progzc.blog.manage.serviceImpl.article;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.progzc.blog.entity.article.Article;
import com.progzc.blog.manage.service.article.ArticleService;
import com.progzc.blog.mapper.article.ArticleMapper;
import org.springframework.stereotype.Service;

/**
 * @Description 文章
 * @Author zhaochao
 * @Date 2020-10-25
 * @Email zcprog@foxmail.com
 * @Version V1.0
 */
@Service
public class ArticleServiceImpl extends ServiceImpl<ArticleMapper, Article> implements ArticleService {

}
