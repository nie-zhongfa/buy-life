package org.buy.life.model.response;

import lombok.Data;

import java.io.Serializable;

/**
 * @menu TODO
 * @Author YourJustin
 * @Date 2024/8/24 9:07 PM
 * I am a code man ^_^ !!
 */
@Data
public class AdminSeriesResponse implements Serializable {

    /**
     * IP
     */
    private String classification;

    /**
     * 类别编码
     */
    private String categoryCode;

    /**
     * 类别名称
     */
    private String categoryName;

    /**
     * 系列编码
     */
    private String seriesCode;

    /**
     * 系列名称
     */
    private String seriesName;

    /**
     * 封面地址
     */
    private String cover;

}
