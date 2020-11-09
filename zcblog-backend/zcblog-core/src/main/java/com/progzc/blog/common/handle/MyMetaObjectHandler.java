package com.progzc.blog.common.handle;

import com.baomidou.mybatisplus.core.handlers.MetaObjectHandler;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.reflection.MetaObject;
import org.springframework.stereotype.Component;

import java.util.Date;

/**
 * @Description 处理MyBatisPlus的自动填充
 * @Author zhaochao
 * @Date 2020/11/9 14:22
 * @Email zcprog@foxmail.com
 * @Version V1.0
 */
@Slf4j
@Component
public class MyMetaObjectHandler implements MetaObjectHandler {

    /**
     * 插入时自动填充生成创建时间、更新时间
     * @param metaObject
     */
    @Override
    public void insertFill(MetaObject metaObject) {
        log.info("start insert fill...");
        if(metaObject.hasGetter("createTime")){
            setFieldValByName("createTime", new Date(), metaObject);
        }

        if(metaObject.hasGetter("updateTime")){
            setFieldValByName("updateTime", new Date(), metaObject);
        }

    }

    /**
     * 更新时，自动填充生成更新时间
     * @param metaObject
     */
    @Override
    public void updateFill(MetaObject metaObject) {
        log.info("start update fill...");
        if(metaObject.hasGetter("updateTime")){
            setFieldValByName("updateTime", new Date(), metaObject);
        }
    }
}
