package com.progzc.blog.common.enums;

import lombok.Getter;

/**
 * @Description 菜单类型
 * @Author zhaochao
 * @Date 2020/11/17 9:50
 * @Email zcprog@foxmail.com
 * @Version V1.0
 */
@Getter
public enum MenuTypeEnum {
    /**
     * 目录（一级菜单）
     */
    CATALOG(0),
    /**
     * 二级菜单
     */
    MENU(1),
    /**
     * 按钮
     */
    BUTTON(2);

    private int value;

    MenuTypeEnum(int value){
        this.value = value;
    }

    public int getValue() {
        return value;
    }
}
