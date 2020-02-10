package com.quantil.cm.api.framework.exception;

import java.util.Set;

import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;

import org.apache.ibatis.binding.BindingException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.util.StringUtils;
import org.springframework.validation.ObjectError;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.NoHandlerFoundException;

import com.alibaba.fastjson.JSONException;
import com.quantil.cm.api.constant.Constants;
import com.quantil.cm.api.framework.RespBody;
import com.quantil.cm.api.framework.util.ExceptionUtil;


@RestController
@ControllerAdvice
public class ExceptionHandler {

    private static final Logger logger = LoggerFactory.getLogger(ExceptionHandler.class);

    @org.springframework.web.bind.annotation.ExceptionHandler(value = Exception.class)
    @ResponseBody
    public ResponseEntity<Object> defaultErrorHandler(Exception e) throws Exception {

        logger.error("Catche exception:",e);
        HttpHeaders headers = new HttpHeaders();
        headers.add(HttpHeaders.CONTENT_TYPE, "application/json;charset=UTF-8");
        ResponseEntity<Object> entity = null;
        RespBody respBody = new RespBody();
        if (e instanceof NoHandlerFoundException || e instanceof NoFoundException) {
            respBody.setCode(Constants.ERROR_404);
            respBody.setMessage(e.getMessage());
            entity = new ResponseEntity<>(respBody, headers, HttpStatus.NOT_FOUND);
        } else if (e instanceof JSONException || e instanceof MissingServletRequestParameterException || e instanceof HttpMessageNotReadableException) {
            respBody.setCode(Constants.ERROR_400_BAD_REQUEST);
            if(e instanceof HttpMessageNotReadableException){
                respBody.setMessage("request body can not be empty");
            }else if(e instanceof JSONException){
                respBody.setMessage("request body or the data in database is not an correct Json data");  
            }else{
                respBody.setMessage(e.getMessage());
            }
            entity = new ResponseEntity<>(respBody, headers, HttpStatus.BAD_REQUEST);
        } else if (e instanceof MethodArgumentNotValidException) {
            MethodArgumentNotValidException exception = (MethodArgumentNotValidException) e;
            StringBuilder sb = new StringBuilder();
            for (ObjectError error : exception.getBindingResult().getAllErrors()) {
                if (StringUtils.isEmpty(error.getDefaultMessage())) {
                    continue;
                }
                sb.append(error.getDefaultMessage()).append(";");
            }
            respBody.setCode(Constants.ERROR_400_BAD_REQUEST);
            respBody.setMessage(sb.toString().substring(0, sb.length() - 1));
            entity = new ResponseEntity<>(respBody, headers, HttpStatus.BAD_REQUEST);
        } else if(e instanceof ConstraintViolationException){
            ConstraintViolationException exception = (ConstraintViolationException) e;
            Set<ConstraintViolation<?>> constraintViolations = exception.getConstraintViolations();
            StringBuilder sb = new StringBuilder();
            for(ConstraintViolation<?> violation:constraintViolations){
                sb.append(violation.getMessage()).append(";");
            }
            respBody.setCode(Constants.ERROR_400_BAD_REQUEST);
            respBody.setMessage(sb.toString().substring(0, sb.length() - 1));
            entity = new ResponseEntity<>(respBody, headers, HttpStatus.BAD_REQUEST);
        }else if (e instanceof BindingException || e instanceof InternalException
                || e instanceof NullPointerException) {
            respBody.setCode(Constants.ERROR_500);
            respBody.setMessage("Please contact the api administrator");
            entity = new ResponseEntity<>(respBody, headers, HttpStatus.INTERNAL_SERVER_ERROR);
        } else if (e instanceof HttpRequestMethodNotSupportedException) {
            respBody.setCode(Constants.ERROR_405);
            respBody.setMessage(e.getMessage());
            entity = new ResponseEntity<>(respBody, headers, HttpStatus.METHOD_NOT_ALLOWED);
        } else if (e instanceof AccessDeniedException) {
            respBody.setCode(((AccessDeniedException) e).getCode());
            respBody.setMessage(e.getMessage());
            entity = new ResponseEntity<>(respBody, headers, HttpStatus.FORBIDDEN);
        } else if (e instanceof BadRequestException) {
            respBody.setCode(((BadRequestException) e).getCode());
            respBody.setMessage(e.getMessage());
            entity = new ResponseEntity<>(respBody, headers, HttpStatus.BAD_REQUEST);
        } else if(e instanceof UnauthorizedException){
            respBody.setCode(Constants.ERROR_401);
            respBody.setMessage(e.getMessage());
            entity = new ResponseEntity<>(respBody, headers, HttpStatus.UNAUTHORIZED); 
        }
        else {
            respBody.setCode(Constants.ERROR_500);
            respBody.setMessage("Please contact the api administrator");
            entity = new ResponseEntity<>(respBody, headers, HttpStatus.INTERNAL_SERVER_ERROR);
            logger.error(ExceptionUtil.printTrace(e));
            throw e;
        }
        return entity;
    }
}
