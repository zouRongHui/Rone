package org.rone.core.jdk.concurrent;

import java.util.concurrent.locks.ReentrantLock;

/**
 * Java中的锁
 * ●.公平锁
 *     定义：就是很公平，在并发环境中，每个线程在获取锁时会先查看此锁维护的等待队列，如果为空，或者当前线程线程是等待队列的第一个，就占有锁，否则就会加入到等待队列中，以后会按照FIFO的规则从队列中取到自己。
 *     优点：所有的线程都能得到资源，不会饿死在队列中。
 *     缺点：吞吐量会下降很多，队列里面除了第一个线程，其他的线程都会阻塞，cpu唤醒阻塞线程的开销会很大。
 *     {@link #fairLock()}
 * ●.非公平锁
 *     定义：线程加锁时直接尝试获取锁，获取不到就自动到队尾等待。
 *     优点：非公平锁性能高于公平锁性能，非公平锁能更充分的利用cpu的时间片，尽量的减少cpu空闲的状态时间。
 *     缺点：可能导致队列中间的线程一直获取不到锁或者长时间获取不到锁，导致饿死。
 *     {@link #noFairLock}
 * ●.可重入锁
 *     定义：可重入锁指的是可重复可递归调用的锁，在外层使用锁之后，在内层仍然可以使用，并且不发生死锁（前提得是同一个对象或者class），这样的锁就叫做可重入锁。
 *     ReentrantLock和synchronized都是可重入锁。
 *     {@link #reentryLock}
 * ●.读写锁
 *     定义：共享读，独占写。读读不互斥，读写互斥，写写互斥。一般的独占锁是：读读互斥，读写互斥，写写互斥。
 *     优点：应用场景中往往读远远大于写，读写锁就是为了这种优化而创建出来的一种机制。
 *     缺点：额外的开销。独占锁的效率低来源于高并发下对临界区的激烈竞争导致线程上下文切换，因此当并发不是很高的情况下，读写锁由于需要额外维护读锁的状态，可能还不如独占锁的效率高。
 *     ReentrantReadWriteLock是一种读写锁。
 *         公平选择性： 支持非公平（默认）和公平的锁获取方式，吞吐量还是非公平优于公平。
 *         重进入： 读锁和写锁都支持线程重进入。
 *         锁降级： 锁降级是指把持住（当前拥有的）写锁，再获取到读锁，随后释放（先前拥有的）写锁的过程
 * ●.乐观锁
 *     定义：乐观锁总是认为不存在并发问题，每次去取数据的时候，总认为不会有其他线程对数据进行修改，因此不会上锁，但是在更新时会判断其他线程在这之前有没有对数据进行修改。
 *     实现：版本机制、CAS操作。
 * ●.悲观锁
 *     定义：悲观锁认为对于同一个数据的并发操作，一定会发生修改的，哪怕没有修改，也会认为修改。因此对于同一份数据的并发操作，悲观锁采取加锁的形式。
 *     例如：数据库的查询 for update
 *         在对任意记录进行修改前，先尝试为该记录加上排他锁（exclusive locking）。
 *         如果加锁失败，说明该记录正在被修改，那么当前查询可能要等待或者抛出异常。具体响应方式由开发者根据实际需要决定。
 *         如果成功加锁，那么就可以对记录做修改，事务完成后就会解锁了。期间如果有其他对该记录做修改或加排他锁的操作，都会等待我们解锁或直接抛出异常。
 * ●.分段锁
 *     定义：细化锁的粒度，当操作不需要更新整个数据的时候，就仅仅针对数据中的一项进行加锁操作。常用于数组、链表、Map等数据结构中。
 * ●.自旋锁
 *     定义：指尝试获取锁的线程不会立即阻塞，而是采用循环的方式去尝试获取锁。
 *     优点：减少线程上下文切换的消耗。
 *     缺点：循环会消耗CPU。
 * ●.偏向锁
 *     定义：是指一段同步代码一直被一个线程所访问，那么该线程会自动获取锁。降低获取锁的代价。
 * ●.轻量级锁
 *     定义：是指当锁是偏向锁的时候，被另一个线程所访问，偏向锁就会升级为轻量级锁，其他线程会通过自旋的形式尝试获取锁，不会阻塞，提高性能。
 * ●.重量级锁
 *     定义：是指当锁为轻量级锁的时候，另一个线程虽然是自旋，但自旋不会一直持续下去，当自旋一定次数的时候，还没有获取到锁，就会进入阻塞，该锁膨胀为重量级锁。重量级锁会让他申请的线程进入阻塞，性能降低。
 * ●.独享锁
 *     定义：指该锁一次只能被一个线程所持有。
 *     Synchronized、ReentrantLock、ReadWriteLock中写锁为独享锁。
 * ●.共享锁
 *     定义：指该锁可被多个线程所持有。
 *     ReadWriteLock中读锁为共享锁。
 * @author rone
 */
public class LockDemo {

    public static void main(String[] args) {
        fairLock();
        noFairLock();
        reentryLock();
    }

    /**
     * 公平锁
     * 按照获取请求锁的顺序去获取锁
     */
    public static void fairLock() {
        ReentrantLock lock = new ReentrantLock(true);
        Runnable runnable = () -> {
            System.out.println(Thread.currentThread().getName() + "启动");
            lock.lock();
            try {
                System.out.println(Thread.currentThread().getName() + "获得了锁");
            } finally {
                lock.unlock();
            }
        };
        Thread[] threadArray = new Thread[10];
        for (int i = 0; i < 10; i++) {
            threadArray[i] = new Thread(runnable);
        }
        for (int i = 0; i < 10; i++) {
            threadArray[i].start();
        }
    }

    /**
     * 非公平锁
     * 请求锁时会直接去尝试获取锁，若此时能获取到锁则直接获取锁否则按照公平锁的机制处理
     */
    public static void noFairLock() {
        ReentrantLock lock = new ReentrantLock(false);
        Runnable runnable = () -> {
            System.out.println(Thread.currentThread().getName() + "启动");
            lock.lock();
            try {
                System.out.println(Thread.currentThread().getName() + "获得了锁");
            } finally {
                lock.unlock();
            }
        };
        Thread[] threadArray = new Thread[10];
        for (int i = 0; i < 10; i++) {
            threadArray[i] = new Thread(runnable);
        }
        for (int i = 0; i < 10; i++) {
            threadArray[i].start();
        }
    }

    /**
     * 可重入锁
     * 在同一线程中支持多次获取锁。
     * ReentrantLock和synchronized都是可重入锁。
     */
    public static void reentryLock() {
        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                testA();
            }
            public synchronized void testA() {
                System.out.println(Thread.currentThread().getName());
                testB();
            }
            public synchronized void testB() {
                System.out.println(Thread.currentThread().getName());
            }
        };
        for (int i = 0; i < 10; i++) {
            new Thread(runnable).start();
        }

        ReentrantLock lock = new ReentrantLock(true);
        Runnable runnable1 = new Runnable() {
            @Override
            public void run() {
                testA();
            }
            public void  testA(){
                lock.lock();
                try {
                    System.out.println(Thread.currentThread().getName() + "第一次获得锁");
                    testB();
                } finally {
                    lock.unlock();
                }
            }
            public void testB(){
                lock.lock();
                try {
                    System.out.println(Thread.currentThread().getName() + "第二次获得锁");
                } finally {
                    lock.unlock();
                }
                System.out.println(Thread.currentThread().getName());
            }
        };
        for (int i = 0; i < 10; i++) {
            new Thread(runnable1).start();
        }
    }
}
