/*
 * Copyright (c) 2015-2022 BiliBili Inc.
 */

package org.buy.life.filter;

import com.alibaba.fastjson.JSONObject;
import lombok.extern.slf4j.Slf4j;
import org.buy.life.exception.BusinessException;
import org.buy.life.exception.ServerCodeEnum;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.web.HttpMediaTypeNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

import java.util.List;
import java.util.Objects;

/**
 * @author kavin
 */
@ControllerAdvice
@Slf4j
public class ControllerExceptionAdvice {

    @ExceptionHandler(BusinessException.class)
    @ResponseBody
    public ResponseEntity<?> handleBusinessException(BusinessException e) {
        log.error(">>>>>>>> 业务异常", e);
        return buildResponse(e.getCode(), e.getMessage(),e.getArgs());
    }

    /**
     * 运行时异常统一消息处理
     */
    @ExceptionHandler(RuntimeException.class)
    @ResponseBody
    public ResponseEntity<?> bindExceptionHandler(RuntimeException e) {
        log.error("runwaterRuntimeException",e);
        return buildResponse(ServerCodeEnum.INTERNAL_SERVER_ERROR);
    }


    /**
     * 异常统一消息处理
     */
    @ExceptionHandler(Exception.class)
    @ResponseBody
    public ResponseEntity<?> bindExceptionHandler(Exception e) {
        log.error("runwater Exception",e);
        return buildResponse(ServerCodeEnum.INTERNAL_SERVER_ERROR);
    }


    @ExceptionHandler(MissingServletRequestParameterException.class)
    public ResponseEntity<?> handleMissingServletRequestParameterException(MissingServletRequestParameterException e) {
        log.error(">>>>>>>> 参数丢失", e);
        return buildResponse(ServerCodeEnum.INVALID_PARAMETERS_ERROR);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<?> handleMethodArgumentNotValidException(MethodArgumentNotValidException e) {
        log.error(">>>>>>>> 参数校验失败", e);
        BindingResult bindingResult = e.getBindingResult();
        List<ObjectError> objErrorList = bindingResult.getAllErrors();

        if (objErrorList.size() > 0) {
            return buildResponse(ServerCodeEnum.INTERNAL_SERVER_ERROR.getCode(),(objErrorList.get(0).getDefaultMessage()));
        }
        return buildResponse(ServerCodeEnum.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<?> handleHttpMessageNotReadableException(HttpMessageNotReadableException e) {
        log.error(">>>>>>>> 参数接收失败", e);
        return buildResponse(ServerCodeEnum.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(HttpMediaTypeNotSupportedException.class)
    public ResponseEntity<?> handleHttpMediaTypeNotSupportedException(HttpMediaTypeNotSupportedException e) {
        log.error(">>>>>>>> MediaType不支持", e);
        return buildResponse(ServerCodeEnum.HTTP_MEDIA_TYPE_NOT_SUPPORT_ERROR);
    }

    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    public ResponseEntity<?> handleMethodArgumentTypeMismatchException(MethodArgumentTypeMismatchException e) {
        log.error(">>>>>>>> 参数转换错误", e);
        return buildResponse(ServerCodeEnum.INTERNAL_SERVER_ERROR);
    }


    private ResponseEntity<?> buildResponse(Integer code,String msg,Object... args) {
        JSONObject body = new JSONObject();
        body.put("code",code);
        body.put("message", (Objects.isNull(args)||args.length==0)?msg.replaceAll("%s",""):String.format(msg,args));
        return new ResponseEntity<>(body, HttpStatus.OK);
    }

    private ResponseEntity<?> buildResponse(ServerCodeEnum serverCodeEnum,Object... args) {
        JSONObject body = new JSONObject();
        body.put("code",serverCodeEnum.getCode());
        body.put("message",serverCodeEnum.getMsg());
        body.put("message",(Objects.isNull(args)||args.length==0)?serverCodeEnum.getMsg().replaceAll("%s",""):String.format(serverCodeEnum.getMsg(),args));
        return new ResponseEntity<>(body, HttpStatus.OK);
    }
}