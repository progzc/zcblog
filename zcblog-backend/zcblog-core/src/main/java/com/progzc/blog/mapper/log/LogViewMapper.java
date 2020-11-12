package com.progzc.blog.mapper.log;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.progzc.blog.entity.log.LogView;
import org.apache.ibatis.annotations.Mapper;

/**
 * @Description 阅读日志
 * @Author zhaochao
 * @Date 2020-10-25
 * @Email zcprog@foxmail.com
 * @Version V1.0
 */
@Mapper
public interface LogViewMapper extends BaseMapper<LogView> {

}
