package org.buy.life.utils;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;

import java.util.function.Supplier;


/**
 * @Author derek_mao
 * @Date 2023/2/10
 */
public class BeanFormatUtils {
    public static DefaultPageResp convert(Page page){
        DefaultPageResp defaultPageResp = new DefaultPageResp();
        defaultPageResp.setPageNum(page.getCurrent());
        defaultPageResp.setPageSize(page.getSize());
        defaultPageResp.setList(page.getRecords());
        defaultPageResp.setTotal(page.getTotal());
        return defaultPageResp;
    }

    public static <T> DefaultPageResp convertList(Page page, Supplier<T> target){
        DefaultPageResp defaultPageResp = new DefaultPageResp();
        defaultPageResp.setPageNum(page.getCurrent());
        defaultPageResp.setPageSize(page.getSize());
        defaultPageResp.setList(BeanConvertUtils.convertListTo(page.getRecords(),target));
        defaultPageResp.setTotal(page.getTotal());
        return defaultPageResp;
    }
}
