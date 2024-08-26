package org.buy.life.model.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * @menu TODO
 * @Author YourJustin
 * @Date 2024/8/26 10:24 PM
 * I am a code man ^_^ !!
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AdminOrderResponse implements Serializable {

    /**
     * 状态
     */
    private String status;

}
