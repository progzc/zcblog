package com.progzc.blog.client.serviceImpl.operation;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.progzc.blog.client.service.operation.TagService;
import com.progzc.blog.entity.operation.Tag;
import com.progzc.blog.mapper.operation.TagMapper;
import org.springframework.stereotype.Service;

/**
 * @Description 标签
 * @Author zhaochao
 * @Date 2020-10-25
 * @Email zcprog@foxmail.com
 * @Version V1.0
 */
@Service
public class TagServiceImpl extends ServiceImpl<TagMapper, Tag> implements TagService {

}
