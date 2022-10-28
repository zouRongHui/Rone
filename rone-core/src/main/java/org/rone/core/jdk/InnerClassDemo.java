package org.rone.core.jdk;

/**
 * Java中的内部类(嵌套类)共分为四种：
 * 　　静态内部类，在其他类中使用InnerClass inner = new OuterClass.InnerClass();
 * 　　成员内部类，在其他类中使用(new Outerclass()).new Innerclass();
 * 　　局部内部类，局部内部类在方法中定义，所以只能在方法中使用
 * 　　匿名内部类，就是没有名字的局部内部类。
 * @author rone
 */
public class InnerClassDemo {

    public static void main(String[] args) {
        // 静态内部类 使用主类.内部类名可在其他类中访问
        InnerClassDemo.StaticInnerClass staticInnerClass = new InnerClassDemo.StaticInnerClass();

        // 成员内部类 需要使用主类的实例对象来访问
        StaticInnerClass.MemberInnerClass memberInnerClass = staticInnerClass.new MemberInnerClass();

        // 局部内部类 一般定义在方法中且只能在该方法中访问
        staticInnerClass.accessLocalInnerClass();

        // 匿名内部类 不会定义一个类名，一般是一次性的
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                System.out.println("匿名内部类");
            }
        });
        thread.start();
    }

    /**
     * 静态内部类
     */
    public static class StaticInnerClass {

        /**
         * 成员内部类
         */
        public class MemberInnerClass {
            public void test() {
                System.out.println("成员内部类");
            }
        }

        /**
         * 局部内部类
         */
        public void accessLocalInnerClass() {
            class LocalInnerClass {
                public void test() {
                    System.out.println("局部内部类");
                }
            }
            new LocalInnerClass().test();
        }
    }
}
