package org.buy.life.entity.req;

import lombok.Data;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;

/**
 * <p>
 * todo
 * </p>
 *
 * @author jinwenxin
 * @since 2024/5/15
 */
@Data
public class PageBasicReq<T> {




    /**
     * 当前页 从1开始
     */
    @NotNull(message = "当前页必填")
    private Long pageNum;



    /**
     * 页码
     */
    @NotNull(message = "页码必填")
    private Long pageSize;


    /**
     * 查询条件
     */
    @Valid
    @NotNull(message = "condition不能为空")
    private T condition;



}
