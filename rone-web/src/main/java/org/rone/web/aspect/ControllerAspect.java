package org.rone.web.aspect;

import com.alibaba.fastjson.JSON;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.rone.web.model.vo.Result;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.util.UUID;

/**
 * Spring AOP示例
 * 当有多个切面时，使用@Order 来设置优先级，值越小越先执行;
 * 执行前后顺序为：filter -> interceptor -> advice -> aspect
 * @author rone
 */
@Order(0)
@Aspect
@Component
public class ControllerAspect {

    private static Logger logger = LoggerFactory.getLogger(ControllerAspect.class);

    /**
     * 切点表达式语法： https://www.cnblogs.com/zhangxufeng/p/9160869.html
     * 1.execution(modifiers-pattern? ret-type-pattern declaring-type-pattern?name-pattern(param-pattern) throws-pattern?)
     *  execution表达式粒度为方法，可以用于明确指定方法返回类型，类名，方法名和参数名等与方法相关的部件。
     *  语法参数说明：
     *      这里问号表示当前项可以有也可以没有，其中各项的语义如下：
     *      modifiers-pattern：方法的可见性，如public，protected；
     *      ret-type-pattern：方法的返回值类型，如int，void等；
     *      declaring-type-pattern：方法所在类的全路径名，如com.spring.Aspect；
     *      name-pattern：方法名类型，如businessService()；
     *      param-pattern：方法的参数类型，如java.lang.String；
     *      throws-pattern：方法抛出的异常类型，如java.lang.Exception；
     *  Demo：
     *      execution(public * com.spring.service.BusinessObject.businessService(java.lang.String,..))
     *          匹配使用public修饰，返回值为任意类型，并且是com.spring.BusinessObject类中名称为businessService的方法，方法可以有多个参数，但是第一个参数必须是java.lang.String类型的方法。
     *      execution(* com.spring.service.BusinessObject.*())
     *          匹配返回值为任意类型，在com.spring.service.BusinessObject类中，并且参数个数为零的方法。
     *      execution(* com.spring.service.Business*.*())
     *          匹配返回值为任意类型，在com.spring.service包中，以Business为前缀的类，并且是类中参数个数为零方法。
     *      execution(* com.spring.service..*.businessService())
     *          匹配返回值为任意类型，并且是com.spring.service包及其子包下的任意类的名称为businessService的方法，而且该方法不能有任何参数。
     *  通配符：
     *      *，该通配符主要用于匹配单个单词
     *      ..通配符，该通配符表示0个或多个项，主要用于declaring-type-pattern和param-pattern中，如果用于declaring-type-pattern中，则表示匹配当前包及其子包，如果用于param-pattern中，则表示匹配0个或多个参数。
     * 2.within(declaring-type-pattern)
     *  within表达式的粒度为类，其参数为全路径的类名（可使用通配符），表示匹配当前表达式的所有类都将被当前方法环绕。
     *  Demo：
     *      within(com.spring.service.BusinessObject)
     *          匹配com.spring.service.BusinessObject中的所有方法。
     *      within(com.spring.service..*)
     *          匹配com.spring.service包及子包下的所有类的所有方法。
     * 3.@within(annotation-type)
     *  表示匹配带有指定注解的类
     *  Demo：@within(com.spring.annotation.BusinessAspect)  匹配使用com.spring.annotation.BusinessAspect注解标注的类。
     * 4.@annotation(annotation-type)
     *  指定注解标注的方法将会被环绕
     *  Demo: @annotation(com.spring.annotation.BusinessAspect) 匹配使用com.spring.annotation.BusinessAspect注解标注的方法。
     */
    // private static final String POINT_CUT = "execution(* org.rone.web.controller.*..*(..))";
    private static final String POINT_CUT = "execution(* org.rone.web.controller.RestControllerDemo.testFilterInterceptorAdviceAspect(..))";
    /**
     * ThreadLocal<T> 为每个使用该变量的线程提供独立的变量副本，所以每一个线程都可以独立的改变自己的副本而不会影响其他线程的副本
     */
    private ThreadLocal<Long> startTime = new ThreadLocal<>();

    /**
     * @Before 表示在目标方法执行之前执行
     * @After 表示后置通知: 在方法执行之后执行的代码. 无论是否抛出异常.
     * @AfterReturning 返回通知，需要在注解中表明接受的返回对象名；
     * @AfterThrowing 异常通知，产生异常触发。需要表明异常对象名；
     * @Around 环绕通知，等同于代理者模式。包含其他四中类型的通知。
     * @param joinPoint
     */
    @Before(value = POINT_CUT)
    public void before(JoinPoint joinPoint) {
        // 执行前后顺序为：filter -> advice -> aspect
        logger.debug("aspect @Before");
        startTime.set(System.currentTimeMillis());
    }

    @AfterReturning(value = POINT_CUT, returning = "result")
    public void afterReturning(JoinPoint joinPoint, Object result) {
        String currentSeq = UUID.randomUUID().toString();
        long total = System.currentTimeMillis() - startTime.get();
        ServletRequestAttributes attr = (ServletRequestAttributes) RequestContextHolder.currentRequestAttributes();
        String requestParam = null;
        Object[] args = joinPoint.getArgs();
        if (args != null) {
            requestParam = JSON.toJSONString(args);
        }
        String returnResult = null;
        if (result != null) {
            // 此处可对返回的数据做统一的加密处理
            returnResult = JSON.toJSONString(result);
        }
        logger.info("请求序号：{}\n请求URL：{}，请求IP：{}，请求耗时：{} 毫秒\n请求参数：{}\n响应数据：{}\n", currentSeq, attr.getRequest().getRequestURI(), getIpAddress(attr.getRequest()), total, requestParam, returnResult);
    }

    @Around(POINT_CUT)
    public Object aroundMethod(ProceedingJoinPoint joinPoint) {
        try {
            // 此处相当于 @Before 前置通知
            Object result = joinPoint.proceed();
            // 此处相当于 @AfterReturning 返回通知
            return result;
        } catch (Throwable e) {
            // 此处相当于@AfterThrowing 异常通知
            // 对异常的返回做统一处理
            logger.error(e.getMessage(), e);
            return Result.fault(e.getMessage());
        }
        // 此处相当于 @After 后置通知
    }

    /**
     * 获取请求的IP
     * @param request
     * @return
     */
    private static String getIpAddress(HttpServletRequest request) {
        String ip = request.getHeader("x-forwarded-for");
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("Proxy-Client-IP");
        }
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("WL-Proxy-Client-IP");
        }
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("HTTP_CLIENT_IP");
        }
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("HTTP_X_FORWARDED_FOR");
        }
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getRemoteAddr();
        }
        return ip;
    }
}
