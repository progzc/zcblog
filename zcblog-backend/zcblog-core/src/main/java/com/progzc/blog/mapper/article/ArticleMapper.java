package com.progzc.blog.mapper.article;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.progzc.blog.entity.article.Article;
import org.apache.ibatis.annotations.Mapper;

/**
 * @Description 文章
 * @Author zhaochao
 * @Date 2020-10-25
 * @Email zcprog@foxmail.com
 * @Version V1.0
 */

@Mapper
public interface ArticleMapper extends BaseMapper<Article> {

}
