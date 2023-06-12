package org.rone.core.jdk.jdk8;

/**
 * java8 lambda表达式
 * 普通的函数或方法通常有 4 个元素：
 *  一个名称、返回类型、参数列表、主体
 *  而lambda表达式只有后两个元素。
 * ●.语法：会根据参数个数去匹配接口中声明的方法，所以lambda表达式实现的接口中需要实现的方法的参数个数要唯一
 *     (参数) -> {方法体}
 * tips: 一种简化的 lambda 表达式的语法，方法引用，使用格式为 [类名/接口/对象]::方法名，其中方法名可以是静态方法、实例方法、对象方法、构造函数等等
 *      eg. Integer::parseInt、System.out::println、String::length、ArrayList::new、int[]::new
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

    public static void test(DemoAble demo) {
        demo.doAction(3);
    }

    public static void test(SnowAble snow) {
        snow.doAction(3, 5);
    }

    // 用于标识一个接口是函数式接口，可以确保接口只有一个抽象方法，它可以被 Lambda 表达式或方法引用所使用。如果接口中有多个抽象方法，编译器会报错。这可以帮助开发者更好地使用 Lambda 表达式和方法引用，以及更好地理解 Java 8 中引入的函数式编程概念。
    @FunctionalInterface
    public interface DemoAble {
        void doAction(Integer num);
    }

    @FunctionalInterface
    public interface SnowAble {
        void doAction(Integer num, Integer count);
    }
}
