package org.rone.web.job;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.SchedulingConfigurer;
import org.springframework.scheduling.config.ScheduledTaskRegistrar;
import org.springframework.scheduling.support.CronTrigger;
import org.springframework.stereotype.Component;

import java.util.Date;

/**
 * 多线程任务调度，通过SchedulingConfigurer接口来实现(每次都会额外开启一个线程去执行任务，不管上一次任务有没有完成)
 * 相比简单的@Scheduled ，此方案可动态修改执行时间。
 * 要求：
 *  1.需要在启动类使用 @EnableScheduling 注解，标明支持scheduling，{@link org.rone.web.WebApplication}
 *  2.需要配置使用的线程池，{@link org.rone.web.config.ScheduledThreadPoolConfig}
 * Tips：定时任务的开启时间尽量避免 0点 这个时间点，曾遇到过任务9点执行，但每次日志输出的时间点都提前的两秒，怀疑是服务器9点自动取同步互联网时间导致的差错(没深究)
 * @author rone
 */
@Component
public class TimingJob implements SchedulingConfigurer {

    private static Logger logger = LoggerFactory.getLogger(TimingJob.class);

    @Override
    public void configureTasks(ScheduledTaskRegistrar scheduledTaskRegistrar) {
        //添加定时任务
        scheduledTaskRegistrar.addTriggerTask(() -> {
            logger.debug("==============开始定时任务，线程：{}，时间：{}============", Thread.currentThread().getName(), new Date());
            //定时任务
            try {
                Thread.sleep(1000 * 20);
            } catch (InterruptedException e) {
                logger.error(e.getMessage(), e);
            }
            logger.debug("==============定时任务完成，线程：{}，时间：{}============", Thread.currentThread().getName(), new Date());
        }, triggerContext -> {
            //可以通过 return null 来控制定时任务是否继续执行，也可以自己手动的控制下一次执行的时间
            // if (true) {
            //    return null;
            // }

            //遵循corn表达式
            String cron = "0 1 * * * ?";
            CronTrigger cronTrigger = new CronTrigger(cron);
            Date nextTime = cronTrigger.nextExecutionTime(triggerContext);
            logger.debug("下次执行任务的时间是：{}", nextTime);

            return nextTime;
        });
        //可以同时添加多个任务
        scheduledTaskRegistrar.addTriggerTask(() -> {
            logger.debug("==============开始第二个任务，线程：{}，时间：{}============", Thread.currentThread().getName(), new Date());
            //定时任务
            try {
                Thread.sleep(1000 * 30);
            } catch (InterruptedException e) {
                logger.error(e.getMessage(), e);
            }
            logger.debug("==============第二个任务完成，线程：{}，时间：{}============", Thread.currentThread().getName(), new Date());
        }, triggerContext -> {
            String cron = "0 2 * * * ?";
            CronTrigger cronTrigger = new CronTrigger(cron);
            Date nextTime = cronTrigger.nextExecutionTime(triggerContext);
            logger.debug("下次执行第二个任务的时间是：{}", nextTime);

            return nextTime;
        });
    }
}
