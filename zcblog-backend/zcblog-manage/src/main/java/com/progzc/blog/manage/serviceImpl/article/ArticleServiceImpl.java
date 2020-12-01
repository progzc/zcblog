package com.progzc.blog.manage.serviceImpl.article;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.progzc.blog.common.constants.QiniuExpireConstants;
import com.progzc.blog.common.enums.OssTypeEnum;
import com.progzc.blog.common.enums.TagTypeEnum;
import com.progzc.blog.entity.MyPage;
import com.progzc.blog.entity.Query;
import com.progzc.blog.entity.article.Article;
import com.progzc.blog.entity.operation.Encrypt;
import com.progzc.blog.entity.operation.Tag;
import com.progzc.blog.entity.operation.TagLink;
import com.progzc.blog.entity.oss.OssResource;
import com.progzc.blog.manage.service.article.ArticleService;
import com.progzc.blog.manage.service.article.EncryptService;
import com.progzc.blog.manage.service.operation.TagLinkService;
import com.progzc.blog.manage.service.operation.TagService;
import com.progzc.blog.manage.service.oss.CloudStorageService;
import com.progzc.blog.mapper.article.ArticleMapper;
import com.progzc.blog.mapper.operation.EncryptMapper;
import com.progzc.blog.mapper.oss.OssResourceMapper;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

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
    private EncryptService encryptService;

    @Autowired
    private TagLinkService tagLinkService;

    @Autowired
    private TagService tagService;

    @Resource
    private OssResourceMapper ossResourceMapper;

    @Autowired
    private CloudStorageService cloudStorageService;

    /**
     * 匹配图片
     */
    private static final String REGEX = "http://cloud.progzc.com/blog/[a-zA-Z0-9/]*(\\.jpg|\\.png|\\.gif|\\.tif|\\.bmp|\\.jpeg)";

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

    /**
     * 新增文章
     * @param article
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void saveArticle(Article article) {
        // 保存文章
        articleMapper.insert(article);

        // 设置文章中的图片永不过期
        // 1. 找出所有的"http://cloud.progzc.com/blog/xxxxxx.png"文件
        List<String> urlList = findAllImg(article);
        // 2. 批量设置图片文件不过期
        setBatchNotExpire(urlList, article);

        // 保存标签（如果有新增的话）及文章与标签之间的关系
        tagLinkService.saveList(article.getId(), article.getTagList());

        // 如果有加密，保存加密密码
        if (article.getNeedEncrypt().equals(true)) {
            encryptService.saveByArticle(article);
        }
    }

    /**
     * 更新文章
     * @param article
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void updateArticle(Article article) {
        // 更新时添加乐观锁
        updateArticleByLock(article);

        // 设置文章中的图片永不过期
        // 1. 找出所有的"http://cloud.progzc.com/blog/xxxxxx.png"文件
        List<String> urlList = findAllImg(article);
        // 2. 找出删除后的文件将其设置为备份过期
        List<OssResource> ossResourceList = ossResourceMapper.selectList(new QueryWrapper<OssResource>().lambda()
                .eq(OssResource::getLinkId, article.getId())
                .eq(OssResource::getType, OssTypeEnum.ARTILCLE.getValue()));
        if (CollectionUtils.isEmpty(ossResourceList)) {
            setBatchNotExpire(urlList, article);
        } else {
            List<String> collect = ossResourceList.stream().map(OssResource::getUrl).collect(Collectors.toList());
            List<String> saveUrlList = (List<String>) CollectionUtils.removeAll(urlList, collect);
            List<String> deleteUrlList = (List<String>) CollectionUtils.removeAll(collect, urlList);
            setBatchNotExpire(saveUrlList, article);
            deleteBatch(deleteUrlList, article);
        }

        // 先删除文章与标签之间的关系（不管是否存在与否）；然后下一步保存标签（如果有新增的话）及文章与标签之间的关系
        tagLinkService.remove(new UpdateWrapper<TagLink>().lambda()
                .eq(TagLink::getLinkId, article.getId())
                .eq(TagLink::getType, TagTypeEnum.ARTILCLE.getValue()));
        tagLinkService.saveList(article.getId(), article.getTagList());

        // 先删除原密码（不管是否存在与否）；然后下一步如果有加密，保存加密密码
        encryptService.remove(new UpdateWrapper<Encrypt>().lambda()
                .eq(Encrypt::getArticleId, article.getId()));
        if (article.getNeedEncrypt().equals(true)) {
            encryptService.saveByArticle(article);
        }
    }

    /**
     * 查询文章列表
     * @param params
     * @return
     */
    @Override
    public MyPage queryPage(Map<String, Object> params) {
        Query<Article> query = new Query<>(params);
        // 按照更新时间降序排列
        List<Article> articleList = articleMapper.selectList(new QueryWrapper<Article>().lambda()
                .like(query.getKeyWord() != null, Article::getTitle, query.getKeyWord())
                .orderByDesc(Article::getUpdateTime));
        for (Article article : articleList) {
            List<TagLink> tagLinkList = tagLinkService.list(new QueryWrapper<TagLink>().lambda()
                    .eq(TagLink::getLinkId, article.getId())
                    .eq(TagLink::getType, TagTypeEnum.ARTILCLE.getValue()));
            List<Integer> tagIdList = tagLinkList.stream().map(TagLink::getTagId).collect(Collectors.toList());
            List<Tag> tagList = tagService.list(new QueryWrapper<Tag>().lambda()
                    .in(Tag::getId, tagIdList));
            article.setTagList(tagList);
        }
        Page<Article> page = query.getPage();
        page.setRecords(articleList);
        return new MyPage(page);
    }

    /**
     * 删除文章
     * @param articleIds
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void deleteBatch(Integer[] articleIds) {
        for (Integer articleId : articleIds) {
            // 1. 删除文章
            Article article = articleMapper.selectById(articleId);
            List<String> urlList = findAllImg(article);
            if (CollectionUtils.isNotEmpty(urlList)) {
                // 1.1 找出文章中的所有图片的url，并从OSS中删除
                deleteBatch(urlList, article);
                // 1.2 删除oss_resource中的图片
                ossResourceMapper.delete(new UpdateWrapper<OssResource>().lambda()
                        .eq(OssResource::getLinkId, articleId)
                        .eq(OssResource::getType, OssTypeEnum.ARTILCLE.getValue()));
            }
            articleMapper.deleteById(articleId);

            // 2. 删除文章与标签的关系
            tagLinkService.remove(new UpdateWrapper<TagLink>().lambda()
                    .eq(TagLink::getLinkId, articleId)
                    .eq(TagLink::getType, TagTypeEnum.ARTILCLE.getValue()));

            // 3. 删除密码
            encryptMapper.delete(new UpdateWrapper<Encrypt>().lambda()
                    .eq(Encrypt::getArticleId, articleId));
        }
    }

    /**
     * 更新时添加乐观锁
     * @param article
     */
    private void updateArticleByLock(Article article) {
        Article oldArticle = this.getById(article);
        oldArticle.setTitle(article.getTitle());
        oldArticle.setDescription(article.getDescription());
        oldArticle.setAuthor(article.getAuthor());
        oldArticle.setContent(article.getContent());
        oldArticle.setContentFormat(article.getContentFormat());
        oldArticle.setRecommend(article.getRecommend());
        oldArticle.setPublish(article.getPublish());
        oldArticle.setTop(article.getTop());
        oldArticle.setNeedEncrypt(article.getNeedEncrypt());
        articleMapper.updateById(oldArticle);
    }

    /**
     * 找出所有的"http://cloud.progzc.com/blog/xxxxxx.png"文件
     * @param article
     * @return
     */
    private List<String> findAllImg(Article article) {
        List<String> urlList = new ArrayList<>();
        String content = article.getContent();
        Pattern pattern = Pattern.compile(REGEX);
        Matcher matcher = pattern.matcher(content);
        while (matcher.find()) {
            urlList.add(matcher.group());
        }
        return urlList;
    }

    /**
     * 批量设置图片文件不过期
     * @param urlList
     * @param article
     */
    private void setBatchNotExpire(List<String> urlList, Article article) {
        if (CollectionUtils.isNotEmpty(urlList)) {
            for (String url : urlList) {
                // 将图片与文章id关联起来
                OssResource ossResource = ossResourceMapper.selectOne(new QueryWrapper<OssResource>().lambda()
                        .eq(OssResource::getUrl, url));
                ossResource.setLinkId(article.getId());
                ossResourceMapper.updateById(ossResource);
            }
            // 设置图片文件不过期
            String[] urls = new String[urlList.size()];
            urlList.toArray(urls);
            cloudStorageService.setNotExpire(urls);
        } else {
            return;
        }
    }

    /**
     * 批量删除并设置备份
     * @param urlList
     * @param article
     */
    private void deleteBatch(List<String> urlList, Article article) {
        if (CollectionUtils.isNotEmpty(urlList)) {
            for (String url : urlList) {
                // 删除图片与文章的关联
                ossResourceMapper.delete(new UpdateWrapper<OssResource>().lambda()
                        .eq(OssResource::getUrl, url));
            }
            String[] urls = new String[urlList.size()];
            urlList.toArray(urls);
            cloudStorageService.deleteBatch(urls, QiniuExpireConstants.DELETE_EXPIRE);
        } else {
            return;
        }
    }
}
