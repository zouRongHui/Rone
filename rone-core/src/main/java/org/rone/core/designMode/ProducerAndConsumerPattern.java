package org.rone.core.designMode;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.ThreadPoolExecutor;

/**
 * 生产者消费者模式，MQ消息队列就是典型的案例
 * ●.将一个业务流程拆分交于多个线程去执行，
 * 	例如餐馆点餐，
 * 		生产者(我)：提交一个订单(需要一碗面条)，
 * 		消费者(厨房)：完成订单(做出一碗面条)，
 * 		内存缓冲区(服务员)：记录订单。
 * @author rone
 */
public class ProducerAndConsumerPattern {

    public static void main(String[] args) {
        int length = 10;
        List<Long> queue = new ArrayList<>(10);
        Producter p1 = new Producter(queue, length);
        Producter p2 = new Producter(queue, length);
        Producter p3 = new Producter(queue, length);
        Consumer c1 = new Consumer(queue);
        Consumer c2 = new Consumer(queue);
        Consumer c3 = new Consumer(queue);

        ThreadPoolExecutor executor = new ScheduledThreadPoolExecutor(10, r -> new Thread(r, "生产者消费者模式"));
        executor.execute(p1);
        executor.execute(p2);
        executor.execute(p3);
        executor.execute(c1);
        executor.execute(c2);
        executor.execute(c3);
    }

    static class Consumer implements Runnable {

        private final List<Long> queue;

        public Consumer(List<Long> queue) {
            this.queue = queue;
        }

        @Override
        public void run() {
            try {
                while (!Thread.currentThread().isInterrupted()) {
                    Long data;
                    synchronized (queue) {
                        if (queue.size() == 0) {
                            queue.wait();
                            queue.notifyAll();
                        }
                        data = queue.remove(0);
                    }
                    System.out.println(Thread.currentThread().getId() + "消费了：" + data);
                    Thread.sleep(1000);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

    }

    static class Producter implements Runnable {
        private final List<Long> queue;
        private final int length;

        public Producter(List<Long> queue, int length) {
            this.queue = queue;
            this.length = length;
        }

        @Override
        public void run() {
            try {
                while (!Thread.currentThread().isInterrupted()) {
                    Long temp = (new Random()).nextLong();
                    System.out.println(Thread.currentThread().getId() + "生产了：" + temp);
                    synchronized (queue) {
                        if (queue.size() >= length) {
                            queue.notifyAll();
                            queue.wait();
                        } else {
                            queue.add(temp);
                        }
                        Thread.sleep(1000);
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

    }

}
