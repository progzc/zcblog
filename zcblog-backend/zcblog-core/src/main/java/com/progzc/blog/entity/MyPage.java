package com.progzc.blog.entity;

import com.baomidou.mybatisplus.core.metadata.IPage;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.List;

/**
 * @Description 封装分页返回结果
 * @Author zhaochao
 * @Date 2020/11/22 14:00
 * @Email zcprog@foxmail.com
 * @Version V1.0
 */
@Data
@NoArgsConstructor
public class MyPage implements Serializable {

    private static final long serialVersionUID = 357772678246248578L;
    /**
     * 总记录数
     */
    private long totalCount;
    /**
     * 每页记录数
     */
    private long pageSize;
    /**
     * 总页数
     */
    private long totalPage;
    /**
     * 当前页
     */
    private long currentPage;
    /**
     * 列表数据
     */
    private List<?> list;

    /**
     * 分页
     * @param list        列表数据
     * @param totalCount  总记录数
     * @param pageSize    每页记录数
     * @param currentPage 当前页数
     */
    public MyPage(List<?> list, int totalCount, int pageSize, int currentPage) {
        this.list = list; // 总记录
        this.totalCount = totalCount; // 总记录数
        this.pageSize = pageSize; // 每页记录数
        this.currentPage = currentPage; // 当前页数
        this.totalPage = (int) Math.ceil((double) totalCount / pageSize); // 总页数
    }

    /**
     * 分页
     * @param page
     */
    public MyPage(IPage<?> page) {
        this.list = page.getRecords(); // 总记录
        this.totalCount = page.getTotal(); // 总记录数
        this.pageSize = page.getSize(); // 每页记录数
        this.currentPage = page.getCurrent(); // 当前页数
        this.totalPage = page.getPages(); // 总页数
    }
}
