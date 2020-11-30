package com.progzc.blog.manage.serviceImpl.operation;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.progzc.blog.common.exception.MyException;
import com.progzc.blog.entity.MyPage;
import com.progzc.blog.entity.Query;
import com.progzc.blog.entity.operation.Tag;
import com.progzc.blog.manage.service.operation.TagService;
import com.progzc.blog.mapper.operation.TagMapper;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;

/**
 * @Description 标签
 * @Author zhaochao
 * @Date 2020-10-25
 * @Email zcprog@foxmail.com
 * @Version V1.0
 */
@Service
public class TagServiceImpl extends ServiceImpl<TagMapper, Tag> implements TagService {

    @Resource
    private TagMapper tagMapper;

    /**
     * 查询标签列表
     * @param params
     * @return
     */
    @Override
    public MyPage queryPage(Map<String, Object> params) {
        Query<Tag> query = new Query<>(params);
        // 若keyWord为null，则.like条件不存在，表示查询所有记录
        IPage<Tag> page = tagMapper.selectPage(query.getPage(),
                new QueryWrapper<Tag>().lambda().like(query.getKeyWord() != null, Tag::getName, query.getKeyWord()));
        return new MyPage(page);
    }

    /**
     * 根据id和类型查询标签列表
     * @param linkId
     * @param type   标签类别：0代表文章，1代表相册
     * @return
     */
    @Override
    public List<Tag> listByLinkId(Integer linkId, int type) {
        return tagMapper.listByLinkId(linkId, type);
    }

    /**
     * 新增标签
     * @param tag
     */
    @Override
    public void saveTag(Tag tag) {
        List<Tag> tagList = tagMapper.selectList(new UpdateWrapper<Tag>().lambda()
                .eq(Tag::getName, tag.getName()).eq(Tag::getType, tag.getType()));
        if (!CollectionUtils.isEmpty(tagList)) {
            throw new MyException("系统中已存在该标签，请重新添加");
        }
        tagMapper.insert(tag);
    }
}
