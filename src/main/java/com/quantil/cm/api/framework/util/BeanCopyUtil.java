package com.quantil.cm.api.framework.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;


public class BeanCopyUtil  extends Object{
    
    private  final static Logger logger=LoggerFactory.getLogger(BeanCopyUtil.class);
    
    //复制bean
    public static void beanCopy(Object source,Object target){
        if(source!=null){
            BeanUtils.copyProperties(source, target);
        }else{
            logger.debug("source is empty, can not copy to target");
        }
    }
    //忽略部分字段
    public static void beanCopyWithChara(Object source,Object target,String chara){
        if(source!=null){
            BeanUtils.copyProperties(source, target, new String[] { chara });
        }else{
            logger.debug("source is empty, can not copy to target");
        }
    }
    
    
}
