package com.progzc.blog.common.enums;

import lombok.Getter;

/**
 * @Description 标签类型枚举
 * @Author zhaocho
 * @Date 2020/11/22 18:06
 * @Email zcprog@foxmail.com
 * @Version V1.0
 */
@Getter
public enum TagTypeEnum {

    /**
     * 属于文章类型的标签
     */
    ARTILCLE(0),

    /**
     * 属于相册类型的标签
     */
    GALLERY(1);
    private int value;

    TagTypeEnum(int value) {
        this.value = value;
    }
}
