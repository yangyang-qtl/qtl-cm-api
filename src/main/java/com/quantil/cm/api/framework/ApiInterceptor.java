package com.quantil.cm.api.framework;

import java.lang.reflect.Field;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import javax.servlet.ServletRequest;

import org.springframework.beans.MutablePropertyValues;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;
import org.springframework.web.servlet.mvc.method.annotation.ExtendedServletRequestDataBinder;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerAdapter;
import org.springframework.web.servlet.mvc.method.annotation.ServletModelAttributeMethodProcessor;

import com.alibaba.fastjson.annotation.JSONField;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.alibaba.fastjson.support.config.FastJsonConfig;
import com.alibaba.fastjson.support.spring.FastJsonHttpMessageConverter;

@SuppressWarnings("deprecation")
@Configuration
public class ApiInterceptor extends WebMvcConfigurerAdapter {

    @Autowired
    RequestMappingHandlerAdapter requestMappingHandlerAdapter;

    /**
     * 增加拦截器，配置拦截路径
     */
    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(new AuthenticateInterceptor()).addPathPatterns("/**")
                .excludePathPatterns("/version", "/health").excludePathPatterns("/*/version", "/*/health");
    }

    /**
     * 用fastJson替换springbootli默认的jacksonJson处理HTTP请求与响应数据
     */
    @Override
    public void configureMessageConverters(List<HttpMessageConverter<?>> converters) {
        Iterator<HttpMessageConverter<?>> iterator = converters.iterator();
        while(iterator.hasNext()){
            HttpMessageConverter<?> converter = iterator.next();
            if(converter instanceof MappingJackson2HttpMessageConverter){
                iterator.remove();
            }
        }
        FastJsonHttpMessageConverter fastConverter = new FastJsonHttpMessageConverter();
        FastJsonConfig fastJsonConfig = new FastJsonConfig();
        fastJsonConfig.setSerializerFeatures(SerializerFeature.PrettyFormat);
        fastConverter.setFastJsonConfig(fastJsonConfig);
        converters.add(fastConverter);
    }

    /**
     * {@inheritDoc}
     * <p>This implementation is empty.
     */
    @Override
    public void addArgumentResolvers(List<HandlerMethodArgumentResolver> argumentResolvers) {
        argumentResolvers.add(new FastJsonServletModelAttributeMethodProcessor(true));
    }

    /**
     * FastJson只能绑定数据类型为json格式的数据
     * 如果是url传参的话（如user-name=admin&user-age=18）无法通过注解@JsonField绑定到Model{userName,userAge}中
     * 这个类允许通过@JsonField的方式来绑定url中的参数
     */
    public class FastJsonServletModelAttributeMethodProcessor extends ServletModelAttributeMethodProcessor {

        private final Map<Class<?>, Map<String, String>> replaceMap = new ConcurrentHashMap<>();

        public FastJsonServletModelAttributeMethodProcessor(boolean annotationNotRequired) {
            super(annotationNotRequired);
        }

        @Override
        protected void bindRequestParameters(WebDataBinder binder, NativeWebRequest nativeWebRequest) {
            Object target = binder.getTarget();
            Class<?> targetClass = target.getClass();
            if (!replaceMap.containsKey(targetClass)) {
                Map<String, String> mapping = analyzeClass(targetClass);
                replaceMap.put(targetClass, mapping);
            }
            Map<String, String> mapping = replaceMap.get(targetClass);
            ParamNameDataBinder paramNameDataBinder = new ParamNameDataBinder(target, binder.getObjectName(), mapping);
            requestMappingHandlerAdapter.getWebBindingInitializer().initBinder(paramNameDataBinder, nativeWebRequest);
            super.bindRequestParameters(paramNameDataBinder, nativeWebRequest);
        }

        private Map<String, String> analyzeClass(Class<?> targetClass) {
            Field[] fields = targetClass.getDeclaredFields();
            Map<String, String> renameMap = new HashMap<>();
            for (Field field : fields) {
                JSONField paramNameAnnotation = field.getAnnotation(JSONField.class);
                if (paramNameAnnotation != null && !paramNameAnnotation.name().isEmpty()) {
                    renameMap.put(paramNameAnnotation.name(), field.getName());
                }
            }
            if (renameMap.isEmpty()) return Collections.emptyMap();
            return renameMap;
        }

        public class ParamNameDataBinder extends ExtendedServletRequestDataBinder {

            private final Map<String, String> renameMapping;

            public ParamNameDataBinder(Object target, String objectName, Map<String, String> renameMapping) {
                super(target, objectName);
                this.renameMapping = renameMapping;
            }

            @Override
            protected void addBindValues(MutablePropertyValues mpvs, ServletRequest request) {
                super.addBindValues(mpvs, request);
                for (Map.Entry<String, String> entry : renameMapping.entrySet()) {
                    String from = entry.getKey();
                    String to = entry.getValue();
                    if (mpvs.contains(from)) {
                        mpvs.add(to, mpvs.getPropertyValue(from).getValue());
                    }
                }
            }
        }
    }

}
