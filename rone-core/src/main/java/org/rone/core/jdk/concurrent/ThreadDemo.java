package org.rone.core.jdk.concurrent;

import java.util.concurrent.*;

/**
 * 多线程
 * ● 进程、线程
 *     进程：一个程序就是一个进程。每个进程都有独立的代码和数据空间（进程上下文），进程间的切换会有较大的开销，一个进程包含1--n个线程。（进程是资源分配的最小单位）
 *     线程：一个程序可包含多个线程。同一类线程共享代码和数据空间，每个线程有独立的运行栈和程序计数器(PC)，线程切换开销小。（线程是cpu调度的最小单位）
 * ● 新建一个线程的方法
 *     继承 Thread
 *     实现 Runnable
 *     实现 Callable<V>，需要用线程池的 submit 方法去调用执行
 * ● Thread、Runnable、Callable<V> 区别
 *     Thread：
 *         提供了很多的方法供使用。例如下面
 *             currentThread()：静态⽅法，返回对当前正在执⾏的线程对象的引⽤。
 *             start()：开始执⾏线程的⽅法，java虚拟机会调⽤线程内的run()⽅法。
 *             yield()：当前线程愿意让出对当前处理器的占⽤，由CPU重新分配资源。
 *             sleep()：静态⽅法，使当前线程睡眠⼀段时间。
 *             join()：使当前线程等待另⼀个线程执⾏完毕之后再继续执⾏，内部调⽤的是Object类的wait⽅法实现的。
 *     Runnable：
 *         由于Java“单继承，多实现”的特性，Runnable接⼝使⽤起来⽐Thread更灵活。
 *         降低了线程对象和线程任务的耦合性。
 *         线程池只能放入Runnable或callable接口的实现类，不能直接放入继承Thread的类。
 *     Callable:
 *         支持有返回值的异步线程，而且是泛型的返回值。
 *         支持试图取消任务，Future接口的cancel方法，并不⼀定能取消成功。
 *     Tips：
 *         一般而言我们会使用Runnable接口配合线程池来实现多线程任务。
 *         当线程需要支持返回值、中途取消的话改用Callable接口。
 * ● 常用API
 *     Thread.sleep(N): 强迫一个线程睡眠N毫秒。
 *         Thread.Sleep(0)：触发操作系统立刻重新进行一次CPU竞争
 *             在大循环里面经常会写一句Thread.Sleep(0) ，因为这样就给了其他线程比如Paint线程获得CPU控制权的权力，这样界面就不会假死在那里。
 *     thread.join(): 等待线程终止。 在A线程中调用了B线程的join()方法，A一定在B死后才销毁。
 *     Thread.yield(): 暂停当前正在执行的线程对象，让出CPU资源。将线程从运行状态转到可运行状态，接下来可能还是继续运行该线程。
 *     thread.setPriority(): 更改线程的优先级，该配置只是建议操作系统的执行顺序，但具体的优先级还是由系统自行按照线程调度算法决定。
 *         供10级，默认为5，建议使用如下的层级MIN_PRIORITY = 1、NORM_PRIORITY = 5、MAX_PRIORITY = 10。
 *     Object.wait(): 强迫一个线程等待。wait()会释放所拥有的对象锁。
 *     Object.notify(): 通知一个线程继续运行。
 *         Obj.wait(),Obj.notify必须在synchronized(Obj){...}语句块内。
 *     Thread.currentThread(): 得到当前线程。
 *     thread.setDaemon(): 设置一个线程为守护线程。
 *     Thread.activeCount(): 获取当前线程所在线程组中活跃的线程数，一旦线程start后，即为活跃状态，直到手动终止或自然死亡。
 *     Thread.interrupt(): 设置线程中断 。
 *         中断机制：不会使目标线程立即退出(但此时相关的线程的API可能会报错 java.lang.InterruptedException)，
 *             而是给线程发送一个通知，告知目标线程有人希望你退出。至于目标线程接收到通知后如何处理，则完全由目标线程自行决定。
 *             在run方法中可通过可以isInterrupted()判断出当前线程的中断情况，后续的处理可通过抛出异常退出、return退出，或者无视。
 *     thread.isInterrupted();判断线程是否中断。
 *     Thread.interrupted():判断是否中断，并清除当前中断状态。
 * ● 守护线程
 *     在Java中有两类线程：User Thread(用户线程)、Daemon Thread(守护线程)
 *     Daemon的作用是为其他线程的运行提供服务。
 *         其实User Thread线程和Daemon Thread守护线程本质上来说去没啥区别的，
 *         唯一的区别之处就在虚拟机的离开：如果User Thread全部撤离，
 *         那么Daemon Thread也就没啥线程好服务的了，所以虚拟机也就退出了。
 *     用户可以自行的设定守护线程，方法：public final void setDaemon(boolean on);
 *     有几点需要注意：
 *     1）、thread.setDaemon(true)必须在thread.start()之前设置，否则会跑出一个IllegalThreadStateException异常。
 *         你不能把正在运行的常规线程设置为守护线程。
 *     2）、在Daemon线程中产生的新线程也是Daemon的。
 *     3）、不是所有的应用都可以分配给Daemon线程来进行服务，比如读写操作或者计算逻辑。
 *         因为在Daemon Thread还没来的及进行操作时，虚拟机可能已经退出了。
 * ● 线程组
 *     每个Thread必然存在于⼀个ThreadGroup中，Thread不能独⽴于ThreadGroup存在。
 *     如果在new Thread时没有显式指定，那么默认将⽗线程（当前执⾏new Thread的线程）线程组设置为⾃⼰的线程组。
 *     线程的优先级不能高于线程所属线程组的优先级。
 *     常用的API
 *         获取当前的线程组名字  Thread.currentThread().getThreadGroup().getName();
 *         线程组统⼀异常处理   在线程成员抛出unchecked exception会执⾏下面的方法
 *             自定义线程组继承ThreadGroup并重新定义 uncaughtException(Thread t, Throwable e) 方法
 * ● Java中六种线程状态 Thread.State 枚举
 *     NEW 线程还未开始执行，即还没有调用start()方法。
 *         线程调用start()方法时会判断线程的状态只有状态为 NEW 时才会执行线程，所以 Thread 无法多次start()且结束后不能再次start().
 *     RUNNABLE    处于执行中或执行就绪状态。包括了传统操作系统线程的ready和running两个状态的。
 *     BLOCKED 阻塞状态，处于BLOCKED状态的线程正等待锁的释放以进⼊同步区。
 *     WAITING 等待状态，处于等待状态的线程变成RUNNABLE状态需要其他线程唤醒。
 *         一般使用 Object.wait() 使当前线程进入等待状态，直到另⼀个线程唤醒它。
 *         其他线程调⽤notify()或notifyAll()可唤醒等待状态中线程。
 *             调⽤notify()⽅法只会唤醒单个等待锁的线程，如有有多个线程都在等待这个锁的话不⼀定会唤醒到之前调⽤wait()⽅法的线程。
 *             调⽤notifyAll()⽅法唤醒所有等待锁的线程之后，也不⼀定会⻢上把时间⽚分给刚才放弃锁的那个线程，具体要看系统的调度。
 *     TIMED_WAITING   超时等待状态，线程等待⼀个具体的时间，时间到后会被⾃动唤醒。
 *         Thread.sleep(long millis) 使当前线程睡眠指定时间。只是暂时使线程停⽌执⾏，并不会释放锁。
 *         Object.wait(long) 使线程进⼊TIMED_WAITING状态。可以通过其他线程调⽤notify()或notifyAll()⽅法来唤醒，或者经过指定时间long之后它会⾃动唤醒
 *     TERMINATED  终⽌状态，此时线程已执⾏完毕。
 * ● 坑
 *     ● Thread调用start()后、自然死亡之后，无法再次start()，因为start()时会检查线程的状态值，仅NEW状态的才能start()。
 * @author rone
 */
public class ThreadDemo {

    public static void main(String[] args) throws ExecutionException, InterruptedException {
        // ThreadPoolExecutor threadPoolExecutor = new ScheduledThreadPoolExecutor(9, new BasicThreadFactory.Builder().namingPattern("example-schedule-pool-%d").daemon(true).build());
        ThreadPoolExecutor threadPoolExecutor = new ScheduledThreadPoolExecutor(9, Executors.defaultThreadFactory());
        Thread thread1 = new Thread1();
        Runnable thread2 = new Thread2();
        Future<String> futureResult = threadPoolExecutor.submit(new Thread3());
        FutureTask<String> futureTask = new FutureTask<>(new Thread3());
        System.out.println(Thread.currentThread().getName() + " : 主线程 - 开始");
        threadPoolExecutor.execute(thread1);
        threadPoolExecutor.execute(thread2);
        threadPoolExecutor.submit(futureTask);
        System.out.println(Thread.currentThread().getName() + " : 其余线程启动完毕");
        // 注意调⽤get⽅法会阻塞当前线程，直到得到结果。
        System.out.println(Thread.currentThread().getName() + " : " + futureResult.get());
        System.out.println(Thread.currentThread().getName() + " : " + futureTask.get());
        System.out.println(Thread.currentThread().getName() + " : 主线程 - 结束");
    }

    static class Thread1 extends Thread {
        @Override
        public void run() {
            System.out.println(Thread.currentThread().getName() + " : 继承自Thread类");
        }
    }

    static class Thread2 implements Runnable {
        @Override
        public void run() {
            System.out.println(Thread.currentThread().getName() + " : 实现Runnable接口");
        }
    }

    static class Thread3 implements Callable<String> {
        @Override
        public String call() throws Exception {
            System.out.println(Thread.currentThread().getName() + " : 实现Callable接口");
            Thread.sleep(5000);
            return "线程执行后的返回值";
        }
    }
}
