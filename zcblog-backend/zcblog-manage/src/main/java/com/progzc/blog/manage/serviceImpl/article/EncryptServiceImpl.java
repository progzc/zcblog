package com.progzc.blog.manage.serviceImpl.article;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.progzc.blog.entity.article.Article;
import com.progzc.blog.entity.operation.Encrypt;
import com.progzc.blog.manage.service.article.EncryptService;
import com.progzc.blog.mapper.operation.EncryptMapper;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * @Description Encrypt加密服务接口实现类
 * @Author zhaochao
 * @Date 2020/11/30 10:17
 * @Email zcprog@foxmail.com
 * @Version V1.0
 */
@Service
public class EncryptServiceImpl extends ServiceImpl<EncryptMapper, Encrypt> implements EncryptService {

    @Resource
    private EncryptMapper encryptMapper;

    /**
     * 保存文章密码
     * @param article
     */
    @Override
    public void saveByArticle(Article article) {
        Encrypt encrypt = new Encrypt();
        // 设置密码关联的文章id
        encrypt.setArticleId(article.getId());
        // 设置Sha256加密的密码
        encrypt.setPassword(article.getPassword());
        encryptMapper.insert(encrypt);
    }
}
