package org.buy.life.utils;

import lombok.Data;

import java.util.List;

/**
 * @Author derek_mao
 * @Date 2023/2/10
 */
@Data
public class DefaultPageResp<T> {
    /**
     * 分页数据
     */
    private List<T> list;
    /**
     * 当前页码
     */
    private Long pageNum;
    /**
     *
     */
    private Long pageSize;
    /**
     * 总数
     */
    private Long total;


}
