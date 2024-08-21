/*
 * Copyright (c) 2015-2021 BiliBili Inc.
 */

package org.buy.life.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;
/**
 * @author kavin
 */
@Component
@ConfigurationProperties(prefix = "buy.data")
@Data
public class BuyDataProperties {
    /**
     *
     *
     */
    private String url;

    private String projectServerUrl;

    private String appKey;

    private String masterSecret;

    private String wlwName;

    private String wlwPass;

    private String wlwUrl;

    private String rootDept;

    private String tenantKey;

    private String wlwNoAuthUrl;

    private String zxyNoAuthUrl;
}
