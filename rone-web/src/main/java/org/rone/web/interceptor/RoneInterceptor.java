package org.rone.web.interceptor;

import cn.hutool.core.util.RandomUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * 拦截器
 * 需要在WebMvcConfigurer注册才能生效 {@link org.rone.web.config.RoneWebMvcConfigurer}
 * 执行前后顺序为：filter -> interceptor -> advice -> aspect
 * @author rone
 */
@Component
public class RoneInterceptor implements HandlerInterceptor {

    private static Logger logger = LoggerFactory.getLogger(RoneInterceptor.class);

    private ThreadLocal<String> logSeq = new ThreadLocal<>();


    /**
     * 预处理，在调用业务方法或者下一个拦截器之前执行，可以进行登录拦截，编码处理、安全控制、权限校验等处理
     * arg2: 具体拦截的方法
     * return true 时会继续执行业务方法或者下一个拦截器方法
     * return false 时则不会继续执行
     */
    @Override
    public boolean preHandle(HttpServletRequest arg0, HttpServletResponse arg1, Object arg2) throws Exception {
        logSeq.set(RandomUtil.randomString(32));
        logger.info("Interceptor#preHandle：当前的请求url为:{}。序号：{}", arg0.getRequestURL(), logSeq.get());
        return true;
    }

    /**
     * 后处理，在业务处理器处理请求执行完成后，生成视图之前被调用。即调用了Service并返回ModelAndView，但未进行页面渲染，可以修改ModelAndView
     * arg2：具体拦截的方法
     */
    @Override
    public void postHandle(HttpServletRequest arg0, HttpServletResponse arg1, Object arg2, ModelAndView arg3)
            throws Exception {
        logger.info("Interceptor#postHandle：当前的请求url为:{}。序号：{}", arg0.getRequestURL(), logSeq.get());
    }

    /**
     * 返回处理，在DispatcherServlet完全处理完请求后被调用，可用于清理资源等
     * arg2：具体拦截的方法
     */
    @Override
    public void afterCompletion(HttpServletRequest arg0, HttpServletResponse arg1, Object arg2, Exception arg3)
            throws Exception {
        logger.info("Interceptor#afterCompletion：当前的请求url为:{}。序号：{}", arg0.getRequestURL(), logSeq.get());
    }

}
