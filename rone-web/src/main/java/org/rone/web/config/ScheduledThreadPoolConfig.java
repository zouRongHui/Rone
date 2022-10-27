package org.rone.web.config;

import org.apache.commons.lang3.concurrent.BasicThreadFactory;
import org.springframework.scheduling.annotation.SchedulingConfigurer;
import org.springframework.scheduling.config.ScheduledTaskRegistrar;

import java.util.concurrent.ScheduledThreadPoolExecutor;

/**
 * 调度任务的线程池配置
 * 若不配置则调度任务默认只是用一个线程来执行
 * @author rone
 */
public class ScheduledThreadPoolConfig implements SchedulingConfigurer {
    @Override
    public void configureTasks(ScheduledTaskRegistrar taskRegistrar) {
        // 设置定时任务并发执行(多任务并发执行，单任务的多次执行是串行)
        // 任务和任务之间通过线程次并发执行，同一个任务的多次执行是串行的(即上一个任务未执行完则下一个任务是不会触发的)
        taskRegistrar.setScheduler(new ScheduledThreadPoolExecutor(3, new BasicThreadFactory.Builder().namingPattern("schedule-pool-%d").daemon(true).build()));
    }
}