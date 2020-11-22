package com.progzc.blog.entity;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.progzc.blog.common.xss.SQLFilterUtils;
import lombok.Data;
import org.apache.commons.lang.StringUtils;

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
    private long limit = 10;

    public Query(Map<String, Object> params) {
        this.putAll(params);
        if (params.get("page") != null) {
            currentPage = Integer.parseInt((String) params.get("page"));
        }
        if (params.get("limit") != null) {
            limit = Integer.parseInt((String) params.get("limit"));
        }
        this.put("offset", (currentPage - 1) * limit);
        this.put("page", currentPage);
        this.put("limit", limit);

        //防止SQL注入（因为sidx、order是通过拼接SQL实现排序的，会有SQL注入风险）
        String sidx = SQLFilterUtils.sqlInject((String) params.get("sidx"));
        String order = SQLFilterUtils.sqlInject((String) params.get("order"));
        this.put("sidx", sidx);
        this.put("order", order);

        this.page = new Page<>(currentPage, limit);

        //排序
        if (StringUtils.isNotBlank(sidx) && StringUtils.isNotBlank(order)) {
            if ("ASC".equalsIgnoreCase(order)) {
                this.page.setAsc(sidx);
            } else {
                this.page.setDesc(sidx);
            }

        }
    }
}
