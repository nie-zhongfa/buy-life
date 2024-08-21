package org.buy.life.utils;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Data
public class RequestData implements Cloneable {

    private String account;
    private String fireToken;
    private LocalDateTime ctime;
    private List<Long> roleId;
    private String accountName;
    private Long appId;
    private String appName;
    private String wlwUrl;
    private String zxyUrl;


    @Override
    public RequestData clone() {
        RequestData data = null;
        try {
            data = (RequestData) super.clone();
        } catch (CloneNotSupportedException e) {
            e.printStackTrace();
        }
        return data;
    }
}