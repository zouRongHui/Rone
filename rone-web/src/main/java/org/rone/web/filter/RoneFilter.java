package org.rone.web.filter;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.annotation.Order;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;

/**
 * 过滤器
 * WebFilter注解，urlPatterns指定哪些请求会执行此过滤器
 * 执行前后顺序为：filter -> interceptor -> advice -> aspect
 * @author rone
 */
@Order(2)
@WebFilter(urlPatterns = {"/testFilterInterceptorAdviceAspect"})
public class RoneFilter implements Filter {

    private static Logger logger = LoggerFactory.getLogger(RoneFilter.class);

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        logger.debug("RoneFilter init");
    }

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        HttpServletRequest httpServletRequest = (HttpServletRequest) servletRequest;
        String url = httpServletRequest.getRequestURI();
        logger.debug("RoneFilter doFilter");
        filterChain.doFilter(servletRequest, servletResponse);
    }

    @Override
    public void destroy() {
        logger.debug("RoneFilter destroy");
    }
}
