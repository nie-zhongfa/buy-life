package org.buy.life.model.request;

import lombok.Data;

import java.util.List;

/**
 * @menu TODO
 * @Author YourJustin
 * @Date 2024/11/6 18:20
 * I am a code man ^_^ !!
 */
@Data
public class GetOrderRequest {

    private List<String> ip;

    private List<String> status;

    private String startCtime;

    private String endCtime;

    private boolean downLoadDetail;
}
