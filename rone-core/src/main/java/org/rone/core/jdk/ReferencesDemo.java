package org.rone.core.jdk;

import java.lang.ref.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Java中有四种引用类型：强引用、软引用、弱引用、虚引用
 * @author rone
 */
public class ReferencesDemo {

    public static void main(String[] args) {
        // strongReference();
        // softReference();
        // weakReference();
        phantomReference();
    }

    /**
     * 强引用
     * 在不主动取消引用下，否则无论是 GC 还是内存不足(宁可 OOM )都不会回收强引用引用的对象。
     */
    private static void strongReference() {
        Demo demo = new Demo();
        demo = null;
        System.gc();
    }
    /**
     * 软引用
     * 在不主动取消引用下，GC 后内存还是不足就会回收软引用引用的对象
     * tips：执行该 Demo 方法时 VM options 加上 -Xmx20M 参数
     */
    private static void softReference() {
        // 软引用实例化的时候用 SoftReference 包装一下，通过 get() 方法即可获取
        SoftReference<byte[]> softReference = new SoftReference<>(new byte[1024 * 1024 * 10]);
        System.out.println(softReference.get());
        System.gc();
        System.out.println(softReference.get());
        byte[] bytes = new byte[1024 * 1024 * 10];
        System.out.println(softReference.get());
    }
    /**
     * 弱引用
     * 在不主动取消引用下，一旦 GC 就会回收弱引用引用的对象
     */
    private static void weakReference() {
        // 弱引用实例化的时候用 WeakReference 包装一下，通过 get() 方法即可获取
        WeakReference<Demo> weakReference = new WeakReference<>(new Demo());
        System.out.println(weakReference.get());
        System.gc();
        System.out.println(weakReference.get());
    }
    /**
     * 虚引用
     * 无法通过虚引用来获取对一个对象的真实引用，需要配合 ReferenceQueue 一起使用。
     *     当GC准备回收一个对象，如果发现它还有虚引用，就会在回收之前，把这个虚引用加入到与之关联的ReferenceQueue中。
     */
    private static void phantomReference() {
        ReferenceQueue queue = new ReferenceQueue();
        PhantomReference<Demo> phantomReference = new PhantomReference<>(new Demo(), queue);

        // 触发 GC
        List<byte[]> bytesList = new ArrayList<>();
        for (int i = 0; i < 1000; i++) {
            bytesList.add(new byte[1024 * 1024]);
        }
        // 手动 GC 无法达到效果，原因不清楚后续研究
        // System.gc();

        while (true) {
            Reference reference = queue.poll();
            if (reference != null) {
                System.out.println("虚引用已被回收：" + reference);
                break;
            }
        }
    }

    private static class Demo{
        @Override
        protected void finalize() throws Throwable {
            super.finalize();
            System.out.println("Demo 被回收了");
        }
    }
}
