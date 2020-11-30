package com.progzc.blog.common.enums;

import lombok.Getter;

/**
 * @Description OSS资源类别枚举
 * @Author zhaocho
 * @Date 2020/11/22 18:06
 * @Email zcprog@foxmail.com
 * @Version V1.0
 */
@Getter
public enum OssTypeEnum {

    /**
     * 属于文章类型的资源
     */
    ARTILCLE(0),

    /**
     * 属于相册类型的资源
     */
    GALLERY(1);
    private int value;

    OssTypeEnum(int value) {
        this.value = value;
    }
}
