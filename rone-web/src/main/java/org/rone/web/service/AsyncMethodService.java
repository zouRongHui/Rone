package org.rone.web.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

/**
 * 异步方法
 * @author rone
 */
@Component
public class AsyncMethodService {

    private static Logger logger = LoggerFactory.getLogger(AsyncMethodService.class);

    /**
     * 有@Async注解的方法，默认就是异步执行的，会在默认的线程池中执行，但是此方法不能在本类调用；
     * 调用类需添加@EnableAsync
     */
    @Async
    public void test(int i) {
        logger.info("{} 子线程名称：{} ----------", i, Thread.currentThread().getName());
        try {
            Thread.sleep(1000 * 10);
            logger.info("{} 子线程over.....", i);
        } catch (InterruptedException e) {
            logger.error(e.getMessage(), e);
        }
    }
}
