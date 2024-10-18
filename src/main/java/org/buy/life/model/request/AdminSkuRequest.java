package org.buy.life.model.request;

import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * @menu TODO
 * @Author YourJustin
 * @Date 2024/8/24 9:21 PM
 * I am a code man ^_^ !!
 */
@Data
public class AdminSkuRequest extends BaseRequest implements Serializable {

    /**
     * 分类：genshin_impact、star_rail、zenless_zone_zero
     */
    private String classification;

    private List<String> status;
}
