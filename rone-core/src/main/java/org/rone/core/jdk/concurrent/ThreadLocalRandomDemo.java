package org.rone.core.jdk.concurrent;

import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;

/**
 * ThreadLocalRandom
 *  Random是线程安全的，并发时多个线程会去争抢更新random的seed的权利，未抢到的线程通过自旋来等待，影响性能。
 *  ThreadLocalRandom是单个线程自有的，性能上会有保障。
 *  缺点：因为都是伪随机，所以在并发情况下会出现不同的线程获取到相同的随机数(具体原因基于Random的实现原理)。
 * @author rone
 */
public class ThreadLocalRandomDemo {

    public static void main(String[] args) {
        System.out.println(ThreadLocalRandom.current().nextInt());
        System.out.println(ThreadLocalRandom.current().nextInt());
        System.out.println(ThreadLocalRandom.current().nextInt());
        System.out.println(ThreadLocalRandom.current().nextInt());

        Random random = new Random();
        System.out.println(random.nextInt());
        System.out.println(random.nextInt());
        System.out.println(random.nextInt());
        System.out.println(random.nextInt());

    }
}
