package org.rone.web.job;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.Date;

/**
 * 简单的单线程任务调度，在方法上使用@Scheduled来实现(当上一次任务没完成时，下一次任务是不会触发的)
 * 要求：需要在启动类使用 @EnableScheduling 注解，标明支持scheduling，{@link org.rone.web.WebApplication}
 * @author rone
 */
@Component
public class EasyJob {

    private static Logger logger = LoggerFactory.getLogger(EasyJob.class);

    /**
     * 支持以下形式定时
     * 1. cron = "0/5 * * * * ?"    cron表达式
     * 2. fixedDelay = 1000*10      该任务执行完后间隔10s后再次执行
     * 3. fixedRate = 1000*10       10s执行一次，如果上个任务完成时已超出此次开始的时间则立即执行
     *
     * 若使用 @Async 注解来实现单个任务多次执行的并发的话，会使用spring的AsyncConfigurer接口实现类中getAsyncExecutor()方法返回的线程池来执行任务
     */
    @Scheduled(cron = "0 0 * * * ?")
    // @Async
    public void easyJob() {
        logger.debug("简单的任务，线程：{}，时间：{}", Thread.currentThread().getName(), new Date());
        try {
            Thread.sleep(1000 * 6);
        } catch (InterruptedException e) {
            logger.error(e.getMessage(), e);
        }
    }
}
