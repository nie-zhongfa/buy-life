/*
 * Copyright (c) 2015-2020 BiliBili Inc.
 */

package org.buy.life.filter;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.buy.life.constant.BuyLifeConstant;
import org.buy.life.entity.BuyAdminEntity;
import org.buy.life.entity.BuyUserEntity;
import org.buy.life.exception.BusinessException;
import org.buy.life.exception.ServerCodeEnum;
import org.buy.life.service.IBuyAdminService;
import org.buy.life.service.IBuyUserService;
import org.buy.life.utils.TtlUtils;
import org.slf4j.MDC;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.web.servlet.HandlerExceptionResolver;

import javax.annotation.Resource;
import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Enumeration;
import java.util.Objects;
import java.util.UUID;

/**
 * @Author: kavin
 * @Date: 2022/10/3 1:56 下午
 * @menu Offer过滤器
 * @ I am a code man -_-!
 */
@Slf4j
@Order(1)
@WebFilter(urlPatterns = "/*")
@Configuration
public class BuyLifeFilter implements Filter {

    @Autowired
    @Qualifier("handlerExceptionResolver")
    private HandlerExceptionResolver handlerExceptionResolver;

    @Resource
    private IBuyAdminService buyAdminService;

    @Resource
    private IBuyUserService buyUserService;

    @Override
    public void doFilter(ServletRequest servletRequest,
                         ServletResponse servletResponse,
                         FilterChain filterChain) throws IOException, ServletException {
        String buyTraceId = "";
        HttpServletRequest request = (HttpServletRequest) servletRequest;
        HttpServletResponse response = (HttpServletResponse) servletResponse;
        try {
            buyTraceId = UUID.randomUUID().toString().replace("-", "");
            //增加mdc日志
            MDC.put("buyTraceId", buyTraceId);
            response.setHeader("buyTraceId", buyTraceId);
        } catch (Exception e) {
            log.error("get mdc buyTraceId fail", e);
        }
        //buySku/skuList
        log.info("buyTraceId doFilter url is {}", request.getRequestURL());
        if(request.getRequestURI().contains("/buyUser/doLogin") || request.getRequestURI().contains("/admin/login")||
                request.getRequestURI().contains("/buyUser/create")||request.getRequestURI().contains("/buySkuDict/dict")
                ||request.getRequestURI().contains("/buyUser/resendPwd")||request.getRequestURI().contains("/buySku/skuList")){
            filterChain.doFilter(request, response);
        }else {
            try {
                if(request.getRequestURI().contains("/admin")){
                    String token = getToken(request);
                    if(StringUtils.isEmpty(token)){
                        throw new BusinessException(ServerCodeEnum.UNAUTHORIZED);
                    }
                    BuyAdminEntity admin = buyAdminService.getAdmin(token);
                    if(Objects.isNull(admin)){
                        throw new BusinessException(ServerCodeEnum.UNAUTHORIZED);
                    }
                    if(admin.getLstTokenExpire().isBefore(LocalDateTime.now())){
                        throw new BusinessException(ServerCodeEnum.UNAUTHORIZED);
                    }
                    LocalDateTime lstTokenExpire = LocalDateTime.now().plusMinutes(30);
                    admin.setLstTokenExpire(lstTokenExpire);
                    buyAdminService.updateById(admin);
                    CurrentAdminUser.setUserId(admin.getUserId());
                    filterChain.doFilter(request, response);
                    CurrentAdminUser.remove();
                } else {
                    String token = getToken(request);
                    if(StringUtils.isEmpty(token)){
                        throw new BusinessException(ServerCodeEnum.UNAUTHORIZED);
                    }
                    BuyUserEntity byToken = buyUserService.findByToken(token);

                    if(Objects.isNull(byToken)){
                        throw new BusinessException(ServerCodeEnum.UNAUTHORIZED);
                    }
                    long nowTimeStamp = LocalDateTime.now().atZone(ZoneId.systemDefault()).toInstant().getEpochSecond();
                    long lstTimeStamp = byToken.getLstLoginTime().atZone(ZoneId.systemDefault()).toInstant().getEpochSecond();
                    if(nowTimeStamp-lstTimeStamp>1800){
                        throw new BusinessException(ServerCodeEnum.UNAUTHORIZED);
                    }else {
                        byToken.setLstLoginTime(LocalDateTime.now());
                        buyUserService.updateById(byToken);
                    }
                    TtlUtils.setSPCtx(byToken);
                    filterChain.doFilter(request, response);
                    TtlUtils.removeSPCtx();
                }

            }catch (Exception e){
                handlerExceptionResolver.resolveException(request, response, null, e);//交给全局异常处理类处理
            }
        }

    }

    private String getToken(HttpServletRequest request) {
        // 获取所有header的名称
        Enumeration<String> headerNames = request.getHeaderNames();
        // 遍历所有header名称
        while (headerNames.hasMoreElements()) {
            String headerName = headerNames.nextElement();
            String headerValue = request.getHeader(headerName);
            // 输出header的名称和值
            log.info("输出header的名称和值 >>>>> "+ headerName + ": " + headerValue);
        }
        String token = request.getHeader(BuyLifeConstant.BUY_TOKEN_HEADER);
        log.info("getToken>>>>>>>>1 :{}", token);
        if (StringUtils.isBlank(token)) {
            token = request.getHeader(BuyLifeConstant.BUY_TOKEN_HEADER.toLowerCase());
            log.info("getToken>>>>>>>>2 :{}", token);
        }
        return token;
    }
}

