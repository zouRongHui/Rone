package org.rone.core.designMode;

import java.io.*;
import java.lang.reflect.Constructor;

/**
 * 单例模式
 * ●.在整个系统中只有一个实例
 * ●.实现方案
 *     懒汉式，在使用时才去创建实例，线程不安全
 *     饿汉式，在加载时就创建实例，线程安全，存在浪费内存的情况
 *     双重检查式，在懒汉式的基础上加上了synchronized同步代码块，并实现双重检查实现线程安全
 *     静态内部类，线程安全，延迟加载
 *         静态内部类的V2版，添加一个标识，在构造器中阻止二次实例
 *         静态内部类的V3版，通过创建一个private Object readResolve() { return getInstance(); } 方法，维护其单例性。
 *     枚举类，线程安全，防止反序列化重新创建新的对象
 * ●.反射机制对单例的影响
 *     针对静态内部类，当获取一个单例的实例后，仍可通过反射机制创建第二个实例，两个hash值是不同的。
 *     eg.
 *         System.out.println(Singleton4.getInstance().hashCode());
 *         Constructor<Singleton4> constructor = Singleton4.class.getDeclaredConstructor();
 *         constructor.setAccessible(true);
 *         System.out.println(constructor.newInstance().hashCode());
 *     解决方案：添加一个标识在构造器中阻止二次实例
 *     eg.
 *         private static boolean initialized = false;
 *         private Singleton4() {
 *             synchronized (Singleton4.class) {
 *                 if (initialized) {
 *                     throw new RuntimeException("This class has his single instance..")
 *                 } else {
 *                     initialized = true;
 *                 }
 *             }
 *         }
 *     虽然该方案仍然存在缺陷，例如可通过反射修改 initialized 的值，来实现多次实例。
 *     但反射机制一般开发人员不会直接使用，通常时一些框架去使用，所以上述的情况也基本不会发生。
 * ●.序列化对单例的影响
 *     普通的 Java 类的反序列化过程中，会通过反射调用类的默认构造函数来初始化对象。所以，即使单例中构造函数是私有的，也会被反射给破坏掉。由于反序列化后的对象是重新 new 出来的，所以这就破坏了单例。
 *     解决方案：创建一个private Object readResolve() { return getInstance(); } 方法，
 *         反序列化底层会通过判断是否拥有该方法，来维护其单例特性。
 * @author rone
 */
public class SingletonPattern {

    public static void main(String[] args) throws Exception {
        test0();
        test1();
        test2();
        test3();
    }

    /**
     * 静态内部类，反射破坏单例
     * @throws Exception
     */
    public static void test0() throws Exception {
        Singleton4 singletonInstance = Singleton4.getInstance();
        Singleton4 anotherInstance;
        Class<Singleton4> clazz = Singleton4.class;
        Constructor<Singleton4> constructor = clazz.getDeclaredConstructor();
        constructor.setAccessible(true);
        anotherInstance = constructor.newInstance();
        System.out.println(singletonInstance.hashCode());
        System.out.println(anotherInstance.hashCode());
    }

    /**
     * 静态内部类V2版，反射不会破坏单例
     * @throws Exception
     */
    public static void test1() throws Exception {
        Singleton4V2 singletonInstance = Singleton4V2.getInstance();
        Singleton4V2 anotherInstance;
        Class<Singleton4V2> clazz = Singleton4V2.class;
        Constructor<Singleton4V2> constructor = clazz.getDeclaredConstructor();
        constructor.setAccessible(true);
        anotherInstance = constructor.newInstance();
        System.out.println(singletonInstance.hashCode());
        System.out.println(anotherInstance.hashCode());
    }

    /**
     * 静态内部类V2版，序列化会破坏单例
     * @throws Exception
     */
    public static void test2() throws Exception {
        Singleton4V2 instance1 = Singleton4V2.getInstance();
        ObjectOutput out = new ObjectOutputStream(new FileOutputStream("filename.ser"));
        out.writeObject(instance1);
        out.close();
        ObjectInput in = new ObjectInputStream(new FileInputStream("filename.ser"));
        Singleton4V2 instance2 = (Singleton4V2) in.readObject();
        in.close();
        System.out.println("INSTANCE1 hashCode=" + instance1.hashCode());
        System.out.println("INSTANCE2 hashCode=" + instance2.hashCode());
    }

    /**
     * 静态内部类V3版，序列化也不会破坏单例
     * @throws Exception
     */
    public static void test3() throws Exception {
        Singleton4V3 instance1 = Singleton4V3.getInstance();
        ObjectOutput out = new ObjectOutputStream(new FileOutputStream("filename.txt"));
        out.writeObject(instance1);
        out.close();
        ObjectInput in = new ObjectInputStream(new FileInputStream("filename.txt"));
        Singleton4V3 instance2 = (Singleton4V3) in.readObject();
        in.close();
        System.out.println("INSTANCE1 hashCode=" + instance1.hashCode());
        System.out.println("INSTANCE2 hashCode=" + instance2.hashCode());
    }

    /**
     * 懒汉式，线程不安全
     * @author Rone
     */
    static class Singleton1 {
        private static Singleton1 INSTANCE;

        private Singleton1() {}

        public static Singleton1 getInstance() {
            if (INSTANCE == null) {
                INSTANCE = new Singleton1();
            }
            return INSTANCE;
        }
    }

    /**
     * 饿汉式，在加载时就创建实例，线程安全，存在浪费内存的情况
     * @author Rone
     */
    static class Singleton2 {
        private final static Singleton2 INSTANCE = new Singleton2();

        private Singleton2() {}

        public static Singleton2 getInstance() {
            return INSTANCE;
        }
    }

    /**
     * 双重检查式，在懒汉式的基础上加上了synchronized同步代码块，并通过双重检查实现线程安全
     * @author Rone
     */
    static class Singleton3 {
        private static Singleton3 INSTANCE;

        private Singleton3() {}

        public static Singleton3 getInstance() {
            if (INSTANCE == null) {
                synchronized (Singleton3.class) {
                    if (INSTANCE == null) {
                        INSTANCE = new Singleton3();
                    }
                }
            }
            return INSTANCE;
        }
    }

    /**
     * 静态内部类，线程安全，延迟加载
     * @author Rone
     */
    static class Singleton4 {
        private static class Singleton4Instance {
            private final static Singleton4 INSTANCE = new Singleton4();
        }

        private Singleton4() {}

        public static Singleton4 getInstance() {
            return Singleton4Instance.INSTANCE;
        }

    }

    /**
     * 枚举类，线程安全，防止反序列化重新创建新的对象
     * 线程安全：反编译class文件后会发现每个枚举实例是形如 public final class T extends Enum 的实现，static 类型的属性会在类被加载之后被初始化。而
     *      当一个 Java 类第一次被真正使用到的时候静态资源被初始化、Java 类的加载和初始化过程都是线程安全的（因为虚拟机在加载枚举的类的时候，
     *      会使用 ClassLoader 的 loadClass 方法，而这个方法使用同步代码块保证了线程安全）。所以，创建一个 enum 类型是线程安全的。
     * 防止反序列化破单例：枚举类在序列化的时候 Java 仅仅是将枚举对象的 name 属性输出到结果中，反序列化的时候则是通过 java.lang.Enum 的 valueOf 方法来根据名字查找枚举对象。
     *      同时，编译器是不允许任何对这种序列化机制的定制的，因此禁用了 writeObject、readObject、readObjectNoData、writeReplace 和 readResolve 等方法。
     * @author Rone
     */
    enum Singleton5 {
        /**
         * 枚举单例
         */
        INSTANCE
    }

    /**
     * 静态内部类的V2版，添加一个标识，在构造器中阻止二次实例
     * @author Rone
     */
    static class Singleton4V2 implements Serializable {
        private static boolean initialized = false;
        private Singleton4V2() {
            synchronized (Singleton4V2.class) {
                if (initialized) {
                    throw new RuntimeException("the singletonInstance has destroy.");
                } else {
                    initialized = true;
                }
            }
        }
        private static class SingletonHolder {
            private static final Singleton4V2 INSTANCE = new Singleton4V2();
        }
        public static Singleton4V2 getInstance() {
            return SingletonHolder.INSTANCE;
        }
    }

    /**
     * 静态内部类的V3版，通过 readResolve() 方法，维护其单例性。
     * 原理是：反序列化底层会通过判断是否拥有该方法
     * @author Rone
     */
    static class Singleton4V3 implements Serializable {
        private static boolean initialized = false;
        private Singleton4V3() {
            synchronized (Singleton4V3.class) {
                if (initialized) {
                    throw new RuntimeException("the singletonInstance has destroy.");
                } else {
                    initialized = true;
                }
            }
        }
        private static class SingletonHolder {
            private static final Singleton4V3 INSTANCE = new Singleton4V3();
        }
        public static Singleton4V3 getInstance() {
            return SingletonHolder.INSTANCE;
        }
        //序列化源码中，通过反射机制来反序列化的，也会判断实例对象是否有 readResolve() 方法
        private Object readResolve() {
            return getInstance();
        }
    }
}
