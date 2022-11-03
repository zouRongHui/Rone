package org.rone.core.jdk.queue;

import java.util.Date;
import java.util.LinkedList;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.LinkedTransferQueue;
import java.util.concurrent.TimeUnit;

/**
 * https://mp.weixin.qq.com/s/XEbcV0MmYITig29qfAEeCA
 * ●.Queue 队列，接口，继承了Collection。队列是集合中的一员，是所有队列最上层的接口。
 *     标准队列为FIFO先进先出，有例如Deque(双端队列)、PriorityQueue(优先级队列)等变种。
 *     核心方法：前者失败时会抛出异常而后者返回null或者false等特殊值
 *         新增：add(e)       offer(e)
 *         移除：remove()     poll()
 *         查看：element()    peek()
 * ●.队列分类
 *     阻塞队列和非阻塞队列
 *         阻塞队列，提供了可阻塞的 put 和 take 方法。如果队列满了 put 方法会被阻塞，如果队列是空的，那么 take 方法也会阻塞。
 *             阻塞队列中包含 BlockingQueue 关键字。例如 ArrayBlockingQueue、LinkedBlockingQueue、PriorityBlockingQueue
 *         非阻塞队列，它不会包含 put 和 take 方法，当队列满之后如果还有新元素入列会直接返回错误，并不会阻塞的等待着添加元素。
 *     有界队列和无界队列
 *         有界队列，是指有固定大小的队列，比如设定了固定大小的 ArrayBlockingQueue，又或者大小为 0 的 SynchronousQueue。
 *         无界队列，指的是没有设置固定大小的队列，但其实如果没有设置固定大小也是有默认值的，只不过默认值是 Integer.MAX_VALUE，
 *             当然实际的使用中不会有这么大的容量（超过 Integer.MAX_VALUE），所以从使用者的角度来看相当于 “无界”的。
 *     按功能分类
 *         普通队列，先进先出的基本队列。常见方法如下：
 *             offer()：添加元素，如果队列已满直接返回 false，队列未满则直接插入并返回 true；
 *             poll()：删除并返回队头元素，当队列为空返回 null；
 *             add()：添加元素，此方法是对 offer 方法的简单封装，如果队列已满，抛出 IllegalStateException 异常；
 *             remove()：直接删除队头元素；
 *             put()：添加元素，如果队列已经满，则会阻塞等待插入；
 *             take()：删除并返回队头元素，当队列为空，则会阻塞等待；
 *             peek()：查询队头元素，但不会进行删除；
 *             element()：对 peek 方法进行简单封装，如果队头元素存在则取出并不删除，如果不存在抛出 NoSuchElementException 异常。
 *         双端队列，指队列的头部和尾部都可以同时入队和出队的数据结构。
 *         优先队列，优先级高的元素先出队，可自定义优先级的逻辑。
 *         延迟队列，基于优先队列 PriorityQueue 实现的，它可以看作是一种以时间为度量单位的优先的队列，当入队的元素到达指定的延迟时间之后方可出队。
 *         其他队列，SynchronousQueue 内部没有容器，每次进行 put() 数据后（添加数据），必须等待另一个线程拿走数据后才可以再次添加数据。
 * ●.AbstractQueue 提供了一些Queue操作的骨架实现的抽象类。
 * ●.BlockDeque 双端阻塞队列，接口，继承BlockingQueue、Deque。
 *     核心方法：通过First和Last来扩展方法实现双端可操作。
 *     BlockingDeque 是 线程安全的。
 * ●.PriorityQueue 优先级队列，类，继承AbstractQueue抽闲类。
 *     通过传入自定义的 Comparator 比较器来实现优先级。
 * ●.ConcurrentLinkedQueue 并发安全的队列
 * ●.ArrayDeque 双向数组队列，类，实现了Deque。
 * ●.ConcurrentLinkedDeque 并发安全的双向队列，类。
 * ●.ArrayBlockingQueue 阻塞的数组队列，类。
 * ●.LinkedBlockingQueue 阻塞的链表队列，类。
 * ●.LinkedBlockingDeque 阻塞的链表双向队列，类。
 * ●.LinkedTransferQueue 链表结构+TransferQueue队列，类。
 * ●.SynchronousQueue 同步队列，类，实现了BlockingQueue，继承AbstractQueue。
 *     每一个put操作必须等待一个take操作完成，否则不能添加元素。
 * ●.PriorityBlockingQueue 优先级阻塞队列，类，实现了BlockingQueue，继承AbstractQueue。
 *     PriorityQueue + BlockingQueue
 * ●.DelayQueue 延时阻塞队列，类，实现了BlockingQueue，继承AbstractQueue。
 *     队列中的元素必须实现Delayed接口。
 *     添加元素时，指定延时多久可以从队列中获取元素。
 *     获取元素的方法poll需要等待延时时间过了才能获取到元素。
 * @author rone
 */
public class QueueDemo {

    public static void main(String[] args) throws InterruptedException {
        // dequeDemo();
        // blockingQueueDemo();
        transferQueueDemo();
    }

    /**
     * ●.Deque Double-Ended queue双端队列，接口，继承了Queue。
     *     可作为FIFO的标准队列，也可作为LIFO的栈。
     *     核心方法，First对队列头部的操作，Last是对队列尾部的操作
     *         新增：addFirst(e)       offerFirst(e)  addLast(e)       offerLast(e)
     *         移除：removeFirst()     pollFirst()    removeLast()     pollLast()
     *         查看：element()         peekFirst()    peekLast()
     *     LinkedList、ArrayDeque、ConcurrentLinkedDeque、LinkedBlockingDeque 实现了 Deque接口
     */
    public static void dequeDemo() {
        LinkedList<String> deque = new LinkedList<>();
        deque.addFirst("a");
        deque.addLast("z");
        deque.add("n");
        deque.offerFirst("b");
        deque.offerLast("y");
        deque.offer("m");
        deque.forEach(e -> System.out.print(e));
        System.out.println();

        System.out.println(deque.element());
        System.out.println(deque.peek());
        System.out.println(deque.peekFirst());
        System.out.println(deque.peekLast());
        System.out.println("#######################################");
        System.out.println(deque.removeFirst());
        System.out.println(deque.removeLast());
        System.out.println(deque.remove());
        System.out.println(deque.pollFirst());
        System.out.println(deque.pollLast());
        System.out.println(deque.poll());
    }

    /**
     * ●.BlockingQueue 阻塞缓冲队列，接口，继承了Queue。
     *     阻塞的插入：当队列满时，队列会阻塞插入元素的线程，直到队列不满。
     *     阻塞的移除：当队列为空，获取元素的线程会等待队列变为非空。
     *     核心方法：
     *         put(e)  阻塞式插入
     *         offer(e,time,timeUnit)  插入，超时退出
     *         take()  阻塞式移除
     *         poll(time,timeUnit) 移除，超时退出
     *     实现了BlockingQueue接口的类
     *         ArrayBlockingQueue类 - 由数组构成的有界阻塞队列
     *         LinkedBlockingQueue类 - 由链表构成的有界阻塞队列，界限默认大小为Integer.MAX_Value（2^31-1），值非常大，相当于无界。
     *         LinkedBlockingDeque类 - 由链表构成的双向阻塞队列
     *         LinkedTransferQueue类 - 由链表构成的无界阻塞队列
     *         SynchronousQueue类 - 不存储元素的阻塞队列，只有一个元素进行数据传递。
     *         LinkedTransferQueue类 - 由链表构成的无界阻塞TransferQueue队列
     *         DelayQueue类 - 使用优先级队列实现的延迟无界阻塞队列
     */
    public static void blockingQueueDemo() throws InterruptedException {
        ArrayBlockingQueue<String> blockingQueue = new ArrayBlockingQueue<>(3);
        blockingQueue.put("a");
        blockingQueue.offer("b", 1000, TimeUnit.MILLISECONDS);
        blockingQueue.forEach(e -> System.out.println(e));
        System.out.println();
        System.out.println(blockingQueue.take());
        System.out.println(blockingQueue.poll(1000, TimeUnit.MILLISECONDS));
    }

    /**
     * ●.TransferQueue 接口，LinkedTransferQueue实现了该接口
     *     当插入数据时，只有当数据被别的线程取出了才会返回。
     *     核心方法：
     *         transfer(e) 插入数据，只有在数据被别的线程取出时才会返回。
     *         tryTransfer(e)  插入数据，当插入时即别的线程取出则返回true，若没有被立即取出则返回false。
     *         tryTransfer(e,timeout,timeUnit) 插入数据，若数据在时间范围内被别的线程取出则返回true，否则返回false。
     *         getWaitingConsumerCount()   返回等待接收元素的消费者数量。
     *         hasWaitingConsumer()    判断是否有别的线程在等待插入数据。
     */
    public static void transferQueueDemo() throws InterruptedException {
        LinkedTransferQueue<String> transferQueue = new LinkedTransferQueue<>();

        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                for (int i = 0; i < 3; i++) {
                    try {
                        Thread.sleep(1000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    System.out.println("另起的线程：" + transferQueue.poll() + "，时间为：" + new Date());
                }
            }
        });
        thread.start();

        transferQueue.transfer("a");
        System.out.println("transfer()返回的时间 " + new Date());
        System.out.println("tryTransfer()返回的时间 " + new Date() + "，返回的结果：" + transferQueue.tryTransfer("b"));
        System.out.println("tryTransfer(e,timeout,timeUnit)返回的时间 " + new Date() + "，返回的结果：" + transferQueue.tryTransfer("c", 1, TimeUnit.SECONDS));

        System.out.println(transferQueue.getWaitingConsumerCount());
        System.out.println(transferQueue.hasWaitingConsumer());
    }

}
