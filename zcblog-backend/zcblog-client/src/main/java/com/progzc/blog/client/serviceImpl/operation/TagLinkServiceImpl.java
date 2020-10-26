package com.progzc.blog.client.serviceImpl.operation;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.progzc.blog.client.service.operation.TagLinkService;
import com.progzc.blog.entity.operation.TagLink;
import com.progzc.blog.mapper.operation.TagLinkMapper;
import org.springframework.stereotype.Service;

/**
 * @Description 标签多对多维护表
 * @Author zhaochao
 * @Date 2020-10-25
 * @Email zcprog@foxmail.com
 * @Version V1.0
 */
@Service("tagLinkClientService")
public class TagLinkServiceImpl extends ServiceImpl<TagLinkMapper, TagLink> implements TagLinkService {

}
