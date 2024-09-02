package org.buy.life.model.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.Map;

/**
 * @menu TODO
 * @Author YourJustin
 * @Date 2024/9/2 5:45 PM
 * I am a code man ^_^ !!
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ChangeLogDto implements Serializable {

    private String orderId;

    private String skuId;

    private Map<String, ChangeKeyValueDto> changeLog;

    private Long oldSkuNum;

    private Long newSkuNum;

    private Long oldPrice;

    private Long newPrice;

    private Long oldCurrency;

    private Long newCurrency;
}
