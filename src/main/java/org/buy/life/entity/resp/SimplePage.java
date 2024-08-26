/*
 * Copyright (c) 2015-2022 BiliBili Inc.
 */

package org.buy.life.entity.resp;

import com.baomidou.mybatisplus.core.metadata.IPage;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.buy.life.utils.BeanConvertUtils;
import org.buy.life.utils.BeanCopiesUtils;
import org.springframework.util.CollectionUtils;

import java.util.Collections;
import java.util.List;
import java.util.function.Supplier;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class SimplePage<T> {

    private static final Long DEFAULT_EMPTY_TOTAL = 0L;
    private List<T> list;
    private Long pageNum;
    private Long pageSize;
    private Long total;

    public static <P> SimplePage<P> convert(IPage<P> page) {
        SimplePage<P> simplePage = new SimplePage<>();
        simplePage.setPageSize(page.getSize());
        simplePage.setPageNum(page.getCurrent());
        simplePage.setTotal(page.getTotal());
        simplePage.setList(page.getRecords());
        return simplePage;
    }

    public static <P> SimplePage<P> convert(long total, long pageNum, long pageSize, List<P> records) {
        SimplePage<P> simplePage = new SimplePage<>();
        simplePage.setPageSize(pageSize);
        simplePage.setPageNum(pageNum);
        simplePage.setTotal(total);
        simplePage.setList(records);
        return simplePage;
    }

    public static <P, D> SimplePage<D> convert(IPage<P> page, Supplier<D> target) {
        SimplePage<P> simplePage = SimplePage.convert(page);
        return SimplePage.convert(simplePage, target);
    }


    /**
     * 分页转化器，将分页里的 list 数据类型进行转换
     *
     * @param pageInfo 分页数据
     * @param target   要转的类型
     * @param <P>      转换前的类型
     * @param <D>      转换后的类型
     * @return 转换后的数据
     */
    public static <P, D> SimplePage<D> convert(SimplePage<P> pageInfo, Supplier<D> target) {
        SimplePage<D> simplePage = new SimplePage<>();
        if (CollectionUtils.isEmpty(pageInfo.getList())) {
            return SimplePage.emptyPage(pageInfo.getPageSize(), pageInfo.getPageNum(), pageInfo.getTotal());
        }
        simplePage.setList(BeanConvertUtils.convertListTo(pageInfo.getList(), target));
        simplePage.setPageSize(pageInfo.getPageSize());
        simplePage.setPageNum(pageInfo.getPageNum());
        simplePage.setTotal(pageInfo.getTotal());
        return simplePage;
    }

    public static <P, D> SimplePage<D> convert(SimplePage<P> pageInfo, List<D> list) {
        SimplePage<D> simplePage = new SimplePage<>();
        if (CollectionUtils.isEmpty(pageInfo.getList())) {
            return SimplePage.emptyPage(pageInfo.getPageSize(), pageInfo.getPageNum(), pageInfo.getTotal());
        }
        simplePage.setList(list);
        simplePage.setPageSize(pageInfo.getPageSize());
        simplePage.setPageNum(pageInfo.getPageNum());
        simplePage.setTotal(pageInfo.getTotal());
        return simplePage;
    }

    public static <P, D> SimplePage<D> convert(SimplePage<P> pageInfo, Class<D> target) {
        SimplePage<D> simplePage = new SimplePage<>();
        if (CollectionUtils.isEmpty(pageInfo.getList())) {
            return SimplePage.emptyPage(pageInfo.getPageSize(), pageInfo.getPageNum(), pageInfo.getTotal());
        }
        List<D> targetList = BeanCopiesUtils.copyList(pageInfo.getList(), target);
        simplePage.setList(targetList);
        simplePage.setPageSize(pageInfo.getPageSize());
        simplePage.setPageNum(pageInfo.getPageNum());
        simplePage.setTotal(pageInfo.getTotal());
        return simplePage;
    }

    public static <T> SimplePage<T> emptyPage(Long pageSize, Long pageNum, Long total) {
        return new SimplePage<>(Collections.emptyList(), pageSize, pageNum, total);
    }

    public static <T> SimplePage<T> emptyPage() {
        return emptyPage(10L, 1L, 0L);
    }


    public static <T> SimplePage<T> emptyPage(Long pageSize, Long pageNum) {
        return emptyPage(pageSize, pageNum, 0L);
    }


    public static <T> SimplePage<T> emptyPage(Integer pageSize, Integer pageNum) {
        return emptyPage(getLongByNumber(pageSize), getLongByNumber(pageNum), 0L);
    }

    public static <T> SimplePage<T> emptyPage(Integer pageSize, Integer pageNum, Long totalNum) {
        return emptyPage(getLongByNumber(pageSize), getLongByNumber(pageNum), totalNum);
    }

    private static long getLongByNumber(Integer pageSize) {
        return pageSize == null ? 0 : pageSize;
    }


    /**
     * 是否存在下一页
     *
     * @return true / false
     */
    public boolean hasNext() {
        return this.pageNum < this.getPages();
    }

    /**
     * 当前分页总页数
     */
    public long getPages() {
        if (getPageSize() == null || getPageSize() == 0) {
            return 0L;
        }
        long pages = getTotal() / getPageSize();
        if (getTotal() % getPageSize() != 0) {
            pages++;
        }
        return pages;
    }

    public long getSize() {
        if (list != null) {
            return list.size();
        }
        return 0;
    }
}
