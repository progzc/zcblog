package com.progzc.blog.manage.service.operation;

import com.baomidou.mybatisplus.extension.service.IService;
import com.progzc.blog.entity.MyPage;
import com.progzc.blog.entity.operation.Tag;

import java.util.List;
import java.util.Map;

/**
 * @Description 标签
 * @Author zhaochao
 * @Date 2020-10-25
 * @Email zcprog@foxmail.com
 * @Version V1.0
 */
public interface TagService extends IService<Tag> {

    /**
     * 查询标签列表
     * @param params
     * @return
     */
    MyPage queryPage(Map<String, Object> params);

    /**
     * 根据id和类型查询标签列表
     * @param linkId
     * @param type   标签类别：0代表文章，1代表相册
     * @return
     */
    List<Tag> listByLinkId(Integer linkId, int type);
}
