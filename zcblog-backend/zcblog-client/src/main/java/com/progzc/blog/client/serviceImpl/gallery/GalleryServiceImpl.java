package com.progzc.blog.client.serviceImpl.gallery;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.progzc.blog.client.service.gallery.GalleryService;
import com.progzc.blog.entity.gallery.Gallery;
import com.progzc.blog.mapper.gallery.GalleryMapper;
import org.springframework.stereotype.Service;

/**
 * @Description 相册
 * @Author zhaochao
 * @Date 2020-10-25
 * @Email zcprog@foxmail.com
 * @Version V1.0
 */
@Service("galleryClientService")
public class GalleryServiceImpl extends ServiceImpl<GalleryMapper, Gallery> implements GalleryService {

}
