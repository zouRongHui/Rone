package org.rone.web.advice;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.MethodParameter;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpInputMessage;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.stereotype.Controller;
import org.springframework.util.StreamUtils;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.mvc.method.annotation.RequestBodyAdvice;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Type;
import java.nio.charset.Charset;

/**
 * RequestBodyAdvice的示例
 * 仅对使用了@RqestBody注解的生效 , 因为它原理上还是AOP , 所以GET方法是不会操作的。
 * 可用来对请求参数做一些统一的操作 , 例如参数的过滤 , 字符的编码 , 第三方的解密等等
 * @ControllerAdvice 说明
 *  basePackages    此处设置需要当前Advice执行的域 , 省略默认全局生效
 *  annotations 配置使用了哪些注解的controller会触发当前的拦截
 * 执行前后顺序为：filter -> interceptor -> advice -> aspect
 * @author rone
 */
@ControllerAdvice
public class RequestAdvice implements RequestBodyAdvice {

    private static Logger logger = LoggerFactory.getLogger(RequestAdvice.class);

    @Override
    public boolean supports(MethodParameter methodParameter, Type type, Class<? extends HttpMessageConverter<?>> aClass) {
        // true：该类后续的 beforeBodyRead、afterBodyRead 才能继续执行。false则不会执行。
        // 该方法会在 beforeBodyRead、afterBodyRead 两个方法前各执行一次。
        logger.debug("advice supports");
        return true;
    }

    /**
     * 在请求数据映射到具体的controller方法的参数前执行
     * @param inputMessage
     * @param parameter
     * @param targetType
     * @param converterType
     * @return
     * @throws IOException
     */
    @Override
    public HttpInputMessage beforeBodyRead(HttpInputMessage inputMessage, MethodParameter parameter, Type targetType, Class<? extends HttpMessageConverter<?>> converterType) throws IOException {
        logger.debug("advice beforeBodyRead");
        String requestData = StreamUtils.copyToString(inputMessage.getBody(), Charset.defaultCharset());
        return new HttpInputMessage() {
            @Override
            public InputStream getBody() throws IOException {
                return new ByteArrayInputStream(requestData.getBytes());
            }

            @Override
            public HttpHeaders getHeaders() {
                return inputMessage.getHeaders();
            }
        };
        // return super.beforeBodyRead(inputMessage, parameter, targetType, converterType);
    }

    @Override
    public Object afterBodyRead(Object o, HttpInputMessage httpInputMessage, MethodParameter methodParameter, Type type, Class<? extends HttpMessageConverter<?>> aClass) {
        logger.debug("advice afterBodyRead");
        return o;
    }

    @Override
    public Object handleEmptyBody(Object o, HttpInputMessage httpInputMessage, MethodParameter methodParameter, Type type, Class<? extends HttpMessageConverter<?>> aClass) {
        logger.debug("advice handleEmptyBody");
        return null;
    }
}
