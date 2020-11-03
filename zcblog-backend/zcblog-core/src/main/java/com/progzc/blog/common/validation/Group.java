package com.progzc.blog.common.validation;

import javax.validation.GroupSequence;

/**
 * @Description 定义校验顺序：若AddGroup组校验失败，则UpdateGroup组不会再校验
 * @Author zhaocho
 * @Date 2020/11/3 10:57
 * @Email zcprog@foxmail.com
 * @Version V1.0
 */

@GroupSequence({AddGroup.class, UpdateGroup.class})
public interface Group {
}
