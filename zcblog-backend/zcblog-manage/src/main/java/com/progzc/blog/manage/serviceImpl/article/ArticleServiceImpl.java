package com.progzc.blog.manage.serviceImpl.article;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.progzc.blog.common.enums.TagTypeEnum;
import com.progzc.blog.entity.article.Article;
import com.progzc.blog.entity.operation.Encrypt;
import com.progzc.blog.entity.operation.Tag;
import com.progzc.blog.manage.service.article.ArticleService;
import com.progzc.blog.manage.service.operation.TagService;
import com.progzc.blog.mapper.article.ArticleMapper;
import com.progzc.blog.mapper.operation.EncryptMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
 * @Description 文章
 * @Author zhaochao
 * @Date 2020-10-25
 * @Email zcprog@foxmail.com
 * @Version V1.0
 */
@Service
public class ArticleServiceImpl extends ServiceImpl<ArticleMapper, Article> implements ArticleService {

    @Resource
    private ArticleMapper articleMapper;

    @Resource
    private EncryptMapper encryptMapper;

    @Autowired
    private TagService tagService;


    /**
     * 根据id查询文章信息
     * @param articleId
     * @return
     */
    @Override
    public Article getArticleInfo(Integer articleId) {
        Article article = articleMapper.selectById(articleId);

        List<Tag> tagList = tagService.listByLinkId(articleId, TagTypeEnum.ARTILCLE.getValue());
        article.setTagList(tagList);

        if (article.getNeedEncrypt().equals(true)) {
            Encrypt encrypt = encryptMapper.selectOne(new QueryWrapper<Encrypt>().lambda()
                    .eq(Encrypt::getArticleId, articleId));
            article.setPassword(encrypt.getPassword());
        }

        return article;
    }
}
