package org.rone.core.jdk;

import java.io.Closeable;
import java.io.IOException;

/**
 * 泛型
 * @author rone
 */
public class GenericityDemo {

    public static void main(String[] args) {
        Demo demo = new Demo<MyThread>();
        demo.setT(new MyThread());
        Thread thread = demo.test(new GenericityDemo(), Integer.valueOf(1));
        thread.start();
    }

    /**
     * 泛型在类/接口上的使用。
     * 泛型的上界，泛型只能是指定类/接口的子类/实现类。
     *      通过 extends 指定泛型的上界(基于Java的继承、实现的特性)。
     *      通过 & 来指定多重上界，此时 & 后面的指定的只能是接口，因为Java单继承多实现。
     *      当指定上界时，类中返回值为泛型的方法，只能用第一个上界的对象来接收。
     *          不清楚这里为什么这么设计，明明我指定了具体的一个类还是只能用子一个上界来接收。可能原因是Java的泛型擦除机制，后续研究。
     *      泛型还有下界，后续研究。
     * @param <T>
     */
    static class Demo<T extends Thread & Runnable & Closeable> {
        private T t;

        /**
         * 泛型在方法上的使用。
         * 多泛型的使用。
         * 泛型的擦除。
         * @param u
         * @param v
         * @param <U>
         * @param <V>
         */
        public <U, V> T test(U u, V v) {
            System.out.println("U.class : " + u.getClass().getSimpleName());
            System.out.println("V.class : " + v.getClass().getSimpleName());
            /*
            由于泛型的擦除，会导致下面几种情况编译报错。
            Java语言的泛型采用的是擦除法实现的伪泛型，泛型信息（类型变量、参数化类型）编译之后通通被除掉了。
            解决的方法
            U.class;
            Integer.valueOf("1") instanceof V; //可通过传进来一个泛型的Class对象然后通过Class对象的isInstance方法来判断
            u = new U(); //可通过传进来一个泛型的Class对象来实例化，或者通过工厂模式等设计模式来实现
            v.compareTo(Integer.valueOf("0")); //可通过指定泛型的上界来实现，当指定上界后可以指定调用上界中声明的方法
            V[] vs = new V[3];//可通过ArrayList来曲线救国
            */
            return this.getT();
        }

        public T getT() {
            return t;
        }

        public void setT(T t) {
            this.t = t;
        }
    }

    static class MyThread extends Thread implements Runnable,Closeable {

        @Override
        public void run() {
            System.out.println("hello world");
        }

        @Override
        public void close() throws IOException {

        }
    }
}
