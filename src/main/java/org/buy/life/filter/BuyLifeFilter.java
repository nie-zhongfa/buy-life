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
import java.time.ZoneOffset;
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
        log.info("buyTraceId doFilter url is {}", request.getRequestURL());
        if(request.getRequestURI().contains("/buyUser/doLogin")||request.getRequestURI().contains("/buyUser/getTokenInfo")
        || request.getRequestURI().contains("/admin/login")||request.getRequestURI().contains("/")){
            filterChain.doFilter(request, response);
        }else {
            try {
                if(request.getRequestURI().contains("/admin")){
                    String token = request.getHeader(BuyLifeConstant.BUY_TOKEN_HEADER);
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
                }else {
                    String token = request.getHeader(BuyLifeConstant.BUY_TOKEN_HEADER);
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
}

