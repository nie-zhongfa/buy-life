/*
 * Copyright (c) 2015-2020 BiliBili Inc.
 */

package org.buy.life.filter;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
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
public class FireFilter implements Filter {

    @Autowired
    @Qualifier("handlerExceptionResolver")
    private HandlerExceptionResolver handlerExceptionResolver;


    @Override
    public void doFilter(ServletRequest servletRequest,
                         ServletResponse servletResponse,
                         FilterChain filterChain) throws IOException, ServletException {
        String fireTraceId = "";
        HttpServletRequest request = (HttpServletRequest) servletRequest;
        HttpServletResponse response = (HttpServletResponse) servletResponse;
        try {
            fireTraceId = UUID.randomUUID().toString().replace("-", "");
            //增加mdc日志
            MDC.put("fireTraceId", fireTraceId);
            response.setHeader("fireTraceId", fireTraceId);
        } catch (Exception e) {
            log.error("get mdc fireTraceId fail", e);
        }
        log.info("fireTraceId doFilter url is {}", request.getRequestURL());
        if(request.getRequestURI().contains("/authMember/doLogin")||
                request.getRequestURI().contains("/fireDayNotify/notify")){
            filterChain.doFilter(request, response);
        }else {
//            if (request.getRequestURI().contains("/swagger-ui.html")
//                    || request.getRequestURI().contains("/swagger-resources")
//                    || request.getRequestURI().contains("/webjars")) {
//                filterChain.doFilter(request, response);
//            }
            try {
               /* String token = request.getHeader(FireConstant.FIRE_TOKEN_HEADER);
                if(StringUtils.isEmpty(token)){
                    throw new BusinessException(ServerCodeEnum.UNAUTHORIZED);
                }
                String jsonData = redisClientService.get(FireConstant.FIRE_TOKEN_REDIS + token);
                if(StringUtils.isEmpty(jsonData)){
                    throw new BusinessException(ServerCodeEnum.UNAUTHORIZED);
                }
                redisClientService.setExpire(FireConstant.FIRE_TOKEN_REDIS + token,jsonData,1800l);
                RequestData fireUserInfo = JSONObject.parseObject(jsonData, RequestData.class);
                TtlUtils.setSPCtx(fireUserInfo);
                filterChain.doFilter(request, response);
                TtlUtils.removeSPCtx();*/
            }catch (Exception e){
                handlerExceptionResolver.resolveException(request, response, null, e);//交给全局异常处理类处理
                return;
            }
        }

    }
}

