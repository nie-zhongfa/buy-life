package org.buy.life.model.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @menu TODO
 * @Author YourJustin
 * @Date 2024/9/2 6:52 PM
 * I am a code man ^_^ !!
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ChangeKeyValueDto {

    private String oldValue;

    private String newValue;
}
