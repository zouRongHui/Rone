package org.rone.core.jdk.concurrent;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * 线程池定义
 * 线程池中资源使用顺序：优先核心线程处理 -> 存到缓冲队列中 -> 新建线程处理(算上核心线程数量总数量不能超过最先数量限制)(所以存在后面的任务先执行的情况) -> 触发 RejectedExecutionHandler 处理器
 * tips:
 *  线程池的定义主要关注两块，1)这些数据的配置要根据实际业务量合理设置，2)达到上限后的任务的处理逻辑
 * @author zouRongHui
 * @date 2023/3/1
 */
public class ThreadPoolDemo {

    private static final Logger LOGGER = LoggerFactory.getLogger(ThreadPoolDemo.class);

    public static void main(String[] args) {
        /*
        * 定义线程池
        * corePoolSize：核心线程数量，除非设置了 allowCoreThreadTimeOut ，否则一直存活知道整个程序终止
        * maximumPoolSize：最大线程数量，超出核心线程数量的部分会在等待了 keepAliveTime 时长后仍空闲时销毁
        * keepAliveTime：见上
        * TimeUnit：keepAliveTime的时间单位
        * BlockingQueue：缓冲队列
        * ThreadFactory：实例化线程的工厂
        * RejectedExecutionHandler：当核心线程用完、缓冲队列已满、最大线程用完，后仍有线程任务需要执行时的处理逻辑，此处的逻辑由调用者线程执行，若响应的出现线程阻塞等情况也是发生在调用者线程身上。
        *   jdk提供了如下的四种默认处理逻辑
        *   ThreadPoolExecutor.AbortPolicy：丢弃任务，抛运行时异常
        *   ThreadPoolExecutor.CallerRunsPolicy：由调用者线程处理新任务
        *   ThreadPoolExecutor.DiscardPolicy：忽略
        *   ThreadPoolExecutor.DiscardOldestPolicy：从队列中挤掉最先进入队列的那个任务
        */
        ThreadPoolExecutor threadPool = new ThreadPoolExecutor(1, 2, 5, TimeUnit.SECONDS, new LinkedBlockingQueue<>(2), new ThreadFactory() {
            private final AtomicInteger threadNumber = new AtomicInteger(1);
            @Override
            public Thread newThread(Runnable r) {
                Thread thread = new Thread(r, "rone-thread-" + threadNumber.getAndIncrement());
                thread.setDaemon(true);
                return thread;
            }
        }, new RejectedExecutionHandler() {
            @Override
            public void rejectedExecution(Runnable r, ThreadPoolExecutor executor) {
                // 线程池队列已满的处理情况
                LOGGER.info("线程池资源耗尽，线程{}被拒，需手动处理", ((Demo) r).getIndex());
                try {
                    // 这里重新将线程任务放到阻塞的缓冲队列中，会造成调用线程的阻塞
                    executor.getQueue().put(r);
                } catch (InterruptedException e) {
                    LOGGER.error("线程{}进入阻塞状态失败", ((Demo) r).getIndex(), e);
                }
            }
        });

        for (int i = 0; i < 6; i++) {
            threadPool.execute(new Demo(i + 1));
            LOGGER.info("线程{}已进入线程池", (i + 1));
        }

        try {
            Thread.sleep(1000 * 60);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

    }

    static class Demo implements Runnable {

        private int index;

        public Demo(int index) {
            this.index = index;
        }

        public int getIndex() {
            return index;
        }

        @Override
        public void run() {
            LOGGER.info("线程{}开始执行", index);
            try {
                Thread.sleep(1000 * 10);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            LOGGER.info("线程{}执行结束", index);
        }
    }
}
