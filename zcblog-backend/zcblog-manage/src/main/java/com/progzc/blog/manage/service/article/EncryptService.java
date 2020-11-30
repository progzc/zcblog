package com.progzc.blog.manage.service.article;

import com.baomidou.mybatisplus.extension.service.IService;
import com.progzc.blog.entity.article.Article;
import com.progzc.blog.entity.operation.Encrypt;

/**
 * @Description Encrypt服务接口
 * @Author zhaochao
 * @Date 2020/11/30 10:13
 * @Email zcprog@foxmail.com
 * @Version V1.0
 */
public interface EncryptService extends IService<Encrypt> {

    /**
     * 保存文章密码
     * @param article
     */
    void saveByArticle(Article article);
}
