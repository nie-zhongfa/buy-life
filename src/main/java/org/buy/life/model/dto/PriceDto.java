package org.buy.life.model.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @menu TODO
 * @Author YourJustin
 * @Date 2024/8/26 7:31 PM
 * I am a code man ^_^ !!
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PriceDto {

    private String price;

    private String currency;
}
