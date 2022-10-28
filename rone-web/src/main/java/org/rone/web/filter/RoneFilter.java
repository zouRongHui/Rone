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
 * ●.当部分请求无需过滤时，解决方案可参考如下
 *     1).将需要过滤的请求统一增加一层路径，然后将filter的配置改为只过滤该层以下的请求
 *         eg.将所有需要过滤的请求增加一层 /api 的路径，然后filter配置改为 @WebFilter(urlPatterns = {"/api/*"})
 *     2).在自定义的filter的doFilter()方法中通过代码忽略无需过滤的url
 *         eg.if (url.endsWith(".css")) {filterChain.doFilter(servletRequest, servletResponse);return;}
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
