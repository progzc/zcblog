package com.progzc.blog.entity;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.progzc.blog.common.xss.SQLFilterUtils;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * @Description 分页查询参数
 * @Author zhaochao
 * @Date 2020/11/22 15:43
 * @Email zcprog@foxmail.com
 * @Version V1.0
 */
@Data
@EqualsAndHashCode(callSuper = false)
public class Query<T> extends LinkedHashMap<String, Object> {

    private static final long serialVersionUID = -6706175593173109120L;

    /**
     * MyBatisPlus分页参数
     */
    private Page<T> page;
    /**
     * 当前页码
     */
    private long currentPage = 1;

    /**
     * 每页记录数
     */
    private long pageSize = 10;

    /**
     * 搜索关键字
     */
    private String keyWord;

    public Query(Map<String, Object> params) {
        this.putAll(params);
        if (params.get("currentPage") != null) {
            currentPage = Integer.parseInt((String) params.get("currentPage"));
        }
        if (params.get("pageSize") != null) {
            pageSize = Integer.parseInt((String) params.get("pageSize"));
        }
        if (params.get("keyWord") != null) {
            String keyWord = SQLFilterUtils.sqlInject((String) params.get("keyWord")); // 防止SQL注入
            this.keyWord = keyWord;
        }

        this.put("currentPage", currentPage);
        this.put("pageSize", pageSize);
        this.put("keyWord", keyWord);

        this.page = new Page<>(currentPage, pageSize);
    }
}
