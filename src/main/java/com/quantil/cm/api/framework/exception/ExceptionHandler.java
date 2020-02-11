package com.quantil.cm.api.framework.exception;

import java.util.HashMap;
import java.util.Map;
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
    
    private final static String ACTIVE_ERROR = "The specified active does not exist";
    private final static String WRONG_ID = "The specified purgeId does not exist! ";
    private final static String WRONG_OFFSET = "The offset must be a valid positive integer";
    private final static String WRONG_LIMIT = "The value of limit should be between 1 and 200";
    private final static String DATEFROM_INVALID = "Datefrom is invalid";
    private final static String DATETO_INVALID = "Dateto is invalid";
    private final static String SORT_INVALID = "The sort field is invalid";
    private final static String ORDER_INVALID = "The order is invalid";
    private final static String ACTION_EXCEPTION = "UnSupportAction";
    private final static String TARGET_EXCEPTION = "UnSupportTarget";
    private final static String ID_EXCEPTION = "PurgeIdNoFound";
    private final static String OFFSET_EXCEPTION = "InvalidListOffset";
    private final static String LIMIT_EXCEPTION = "InvalidListLimit";
    private final static String FROM_EXCEPTION = "InvalidDateFrom";
    private final static String TO_EXCEPTION = "InvalidDateTo";
    private final static String SORT_EXCEPTION = "InvalidSortField";
    private final static String ORDER_EXCEPTION = "InvalidOrder";
    
    private static Map<String, String> EXCEPTIONMAP = new HashMap<String, String> (){
        private static final long serialVersionUID = 1L;

    {
        put(ACTION_EXCEPTION, ACTIVE_ERROR);
        put(TARGET_EXCEPTION, ACTIVE_ERROR);
        put(ID_EXCEPTION, WRONG_ID);
        put(OFFSET_EXCEPTION, WRONG_OFFSET);
        put(LIMIT_EXCEPTION, WRONG_LIMIT);
        put(FROM_EXCEPTION, DATEFROM_INVALID);
        put(TO_EXCEPTION, DATETO_INVALID);
        put(SORT_EXCEPTION, SORT_INVALID);
        put(ORDER_EXCEPTION, ORDER_INVALID);
    }};
    
    @org.springframework.web.bind.annotation.ExceptionHandler(value = Exception.class)
    @ResponseBody
    public ResponseEntity<Object> defaultErrorHandler(Exception e) throws Exception {

        logger.error("Catche exception:",e);
        HttpHeaders headers = new HttpHeaders();
        headers.add(HttpHeaders.CONTENT_TYPE, "application/json;charset=UTF-8");
        ResponseEntity<Object> entity = null;
        RespBody respBody = new RespBody();
        if (e instanceof NoHandlerFoundException || e instanceof NoFoundException) {
            respBody.setCode(e.getMessage());
            respBody.setMessage(EXCEPTIONMAP.get(e.getMessage()));
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
            String code = sb.toString().substring(0, sb.length() - 1);
            respBody.setCode(code);
            respBody.setMessage(EXCEPTIONMAP.get(code));
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
            respBody.setCode(e.getMessage());
            respBody.setMessage(EXCEPTIONMAP.get(e.getMessage()));
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
