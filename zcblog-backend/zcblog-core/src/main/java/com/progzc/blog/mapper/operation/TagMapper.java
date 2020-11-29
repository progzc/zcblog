package com.progzc.blog.mapper.operation;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.progzc.blog.entity.operation.Tag;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * @Description 标签
 * @Author zhaochao
 * @Date 2020-10-25
 * @Email zcprog@foxmail.com
 * @Version V1.0
 */
@Mapper
public interface TagMapper extends BaseMapper<Tag> {

    /**
     * 根据id和类型查询标签列表
     * @param linkId
     * @param type
     * @return
     */
    List<Tag> listByLinkId(Integer linkId, int type);
}
