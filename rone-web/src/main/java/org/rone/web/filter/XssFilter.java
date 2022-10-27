package org.rone.web.filter;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.annotation.Order;
import org.springframework.util.StringUtils;
import org.springframework.web.util.HtmlUtils;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;
import java.io.IOException;

/**
 * XSS攻击过滤器
 * @author rone
 */
@Order(1)
@WebFilter(urlPatterns = "/testFilterInterceptorAdviceAspect")
public class XssFilter implements Filter {

    private static Logger logger = LoggerFactory.getLogger(XssFilter.class);

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        logger.debug("XssFilter init");
    }

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        logger.debug("XssFilter doFilter");
        filterChain.doFilter(new XssHttpServletRequest((HttpServletRequest) servletRequest), servletResponse);
    }

    @Override
    public void destroy() {
        logger.debug("XssFilter destroy");
    }

    /**
     * @author Rone
     */
    class XssHttpServletRequest extends HttpServletRequestWrapper {

        private HttpServletRequest request;

        public XssHttpServletRequest(HttpServletRequest request) {
            super(request);
            this.request = request;
        }

        @Override
        public String getParameter(String name) {
            String target = request.getParameter(name);
            if (!StringUtils.isEmpty(target)) {
                return HtmlUtils.htmlEscape(target);
            }
            return target;
        }

        @Override
        public String[] getParameterValues(String name) {
            String[] targetValues = request.getParameterValues(name);
            if (targetValues != null) {
                for (int i = 0; i < targetValues.length; i++) {
                    targetValues[i] = HtmlUtils.htmlEscape(targetValues[i]);
                }
            }
            return targetValues;
        }
    }
}
