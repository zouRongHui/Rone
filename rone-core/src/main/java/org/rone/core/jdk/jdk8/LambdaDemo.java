package org.rone.core.jdk.jdk8;

/**
 * java8 lambda表达式
 * @author rone
 */
public class LambdaDemo {

    public static void main(String[] args) {
        // 无参数
        Thread thread = new Thread(() -> System.out.println("hello lambda..."));
        thread.start();

        String publicParam = "lambda";
        // 单参数
        test(str -> System.out.println(str + publicParam));
        // 多参数
        test((num, count) -> System.out.println(num + " , " + count));
        test((num, count) -> {
            num = num + 1;
            System.out.println(num * count);
        });
    }

    public static void test(Demoable demo) {
        demo.doAction(3);
    }

    public static void test(Snowable snow) {
        snow.doAction(3, 5);
    }

    public interface Demoable {
        void doAction(Integer num);
    }

    public interface Snowable {
        void doAction(Integer num, Integer count);
    }
}
