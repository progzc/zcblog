package com.progzc.blog.mapper.operation;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.progzc.blog.entity.operation.TagLink;
import org.apache.ibatis.annotations.Mapper;

/**
 * @Description 标签多对多维护表
 * @Author zhaochao
 * @Date 2020-10-25
 * @Email zcprog@foxmail.com
 * @Version V1.0
 */
@Mapper
public interface TagLinkMapper extends BaseMapper<TagLink> {

}
