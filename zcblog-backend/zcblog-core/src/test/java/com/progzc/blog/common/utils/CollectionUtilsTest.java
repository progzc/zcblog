package com.progzc.blog.common.utils;

import org.apache.commons.collections.CollectionUtils;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

/**
 * @Description CollectionUtils工具包测试
 * @Author zhaochao
 * @Date 2020/11/30 22:19
 * @Email zcprog@foxmail.com
 * @Version V1.0
 */
public class CollectionUtilsTest {

    /**
     * 学习org.apache.commons.collections.CollectionUtils工具包
     */
    @Test
    public void test() {
        List<Integer> list1 = new ArrayList<>(); // [1,2,3]
        list1.add(1);
        list1.add(2);
        list1.add(3);

        List<Integer> list2 = new ArrayList<>(); // [2,3,4]
        list2.add(2);
        list2.add(3);
        list2.add(4);

        System.out.println(CollectionUtils.disjunction(list1, list2)); // [1,4]
        System.out.println(CollectionUtils.removeAll(list1, list2)); // [1]
        System.out.println(CollectionUtils.removeAll(list2, list1)); // [4]
        System.out.println(CollectionUtils.retainAll(list1, list2)); // [2,3]
    }
}
