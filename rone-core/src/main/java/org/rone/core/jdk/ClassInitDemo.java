package org.rone.core.jdk;

/**
 * class对象初始化
 * 初始化的顺序(先父类后子类、先静态后私有)
 *  1.父类的静态成员变量和静态代码块
 *  2.子类的静态成员变量和静态代码块
 *  3.父类的普通成员变量和代码块，再执行父类的构造方法
 *  4.子类的普通成员变量和代码块，再执行子类的构造方法
 * @author rone
 */
public class ClassInitDemo {

    public static void main(String[] args) {
        test1();
    }

    /**
     * static{}静态代码块、{}构造代码块、构造器constructor 的执行顺序
     */
    public static void test1() {
        // Test1ClassA.test();
        // Test1ClassA.test2();
        // Test1ClassA.test();
        /* 输出
        static Test1ClassA
        静态方法 Test1ClassA
        静态方法2 Test1ClassA
        静态方法 Test1ClassA
        */

        Test1ClassB t1 = new Test1ClassB();
        System.out.println();
        Test1ClassB t2 = new Test1ClassB();
        /* 输出
        static Test1ClassA
        static Test1ClassB
        this is Test1ClassA.class.
        Test1ClassA 构造器。
        this is Test1ClassB.class.
        Test1ClassB 构造器。

        this is Test1ClassA.class.
        Test1ClassA 构造器。
        this is Test1ClassB.class.
        Test1ClassB 构造器。
        */
    }

    static class Test1ClassA {
        //构造器
        public Test1ClassA() {
            System.out.println("Test1ClassA 构造器。");
        }
        //构造代码块，在构造器执行之前执行，用于处理多个构造器情况下的共同抽象部分。
        {
            System.out.println("this is Test1ClassA.class.");
        }
        //静态代码块，仅执行一次
        static {
            System.out.println("static Test1ClassA");
        }

        public static void test() {
            System.out.println("静态方法 Test1ClassA");
        }

        public static void test2() {
            System.out.println("静态方法2 Test1ClassA");
        }
    }

    static class Test1ClassB extends Test1ClassA {
        public Test1ClassB() {
            System.out.println("Test1ClassB 构造器。");
        }
        {
            System.out.println("this is Test1ClassB.class.");
        }
        static {
            System.out.println("static Test1ClassB");
        }

        public static void test() {
            System.out.println("静态方法 Test1ClassB");
        }
    }
}
