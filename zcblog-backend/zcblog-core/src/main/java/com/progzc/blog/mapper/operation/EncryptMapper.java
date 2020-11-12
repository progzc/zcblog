package com.progzc.blog.mapper.operation;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.progzc.blog.entity.operation.Encrypt;
import org.apache.ibatis.annotations.Mapper;

/**
 * @Description 文章加密
 * @Author zhaochao
 * @Date 2020-10-25
 * @Email zcprog@foxmail.com
 * @Version V1.0
 */
@Mapper
public interface EncryptMapper extends BaseMapper<Encrypt> {

}
