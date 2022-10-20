package org.rone.core.jdk;

/**
 * @author rone
 */
public class NumberDemo {

    public static void main(String[] args) {
        dividedZero();
    }

    /**
     * java中浮点数float和double都是基于IEEE 754标准
     *  而这个标准规定：浮点数除以0等于正无穷或负无穷
     *  所以在java中也就如此来实现了。
     *  可详见java.lang.Double类中POSITIVE_INFINITY、NEGATIVE_INFINITY、NaN这几个常量
     * @author Rone
     */
    private static void dividedZero() {
        // Infinity 正无穷
        System.out.println(1.0/0);
        // -Infinity 负无穷
        System.out.println(-1.0/0);
        // NaN 非数字
        System.out.println(0.0/0);
        // java.lang.ArithmeticException: / by zero
        System.out.println(1/0);
    }
}
