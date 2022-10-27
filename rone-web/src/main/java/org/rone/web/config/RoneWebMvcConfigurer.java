package org.rone.web.config;

import org.rone.web.interceptor.RoneInterceptor;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * @author rone
 */
@Configuration
public class RoneWebMvcConfigurer implements WebMvcConfigurer {

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(new RoneInterceptor())
                // 配置哪些请求会被拦截，可使用通配符，可配置多个
                .addPathPatterns("/testFilterInterceptorAdviceAspect")
                // 在上一个配置中剔除无需拦截的请求，可配置多个
                .excludePathPatterns("/user/getNamesByIds")
                // 拦截顺序
                .order(0);
        // 此处还可以同时注册多个拦截器
        WebMvcConfigurer.super.addInterceptors(registry);
    }

    /**
     * 配置对于一些常见的请求的处理，例如根目录
     * @param registry
     */
    @Override
    public void addViewControllers(ViewControllerRegistry registry) {
        registry.addViewController("/").setViewName("/index.html");
        WebMvcConfigurer.super.addViewControllers(registry);
    }
}
