package org.rone.core.jdk;

import java.math.BigDecimal;
import java.text.DecimalFormat;

/**
 * @author rone
 */
public class NumberDemo {

    public static void main(String[] args) {
        dividedZero();
    }

    /**
     * 格式化数字类型数据
     */
    private static void formatNumber() {
        // 小数部分需要用00来表示
        DecimalFormat decimalFormat = new DecimalFormat("###,###.00");
        // 2,400.52
        System.out.println(decimalFormat.format(Double.parseDouble("2400.515860")));
        // 2,400.00
        System.out.println(decimalFormat.format(Double.parseDouble("2400")));
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

    private static void bigDecimalDemo() {
        BigDecimal num1 = new BigDecimal(100.34);
        BigDecimal num2 = new BigDecimal(33.908);
        // 比较大小，返回结果0(相等),1(大于),-1(小于)。Tips: 不要使用 equals() 方法，equals()的比较可能因为精度的问题导致结果不符合预期(具体要看BigDecimal的实例方式)
        if (num1.compareTo(num2) > 1) {
            System.out.println("num1 大于 num2");
        }

        // BigDecimal转String，第一个参数是小数位数，参数变量是取舍方法(四舍五入)
        num2.setScale(2, BigDecimal.ROUND_HALF_UP).toString();
        // String转BigDecimal
        BigDecimal num3 = new BigDecimal("78548794");
        // divide方法结果为无限小数问题，结果保留有scale个小数位，roundingMode表示的就是保留模式是什么，是四舍五入啊还是其它的
        num3.divide(new BigDecimal(3), 2, BigDecimal.ROUND_HALF_UP);
        // 小数点处理
        // 保留一位小数，默认采用四舍五入方式
        num2.setScale(1);
        //直接删除多余的小数位，如2.35->2.3
        num2.setScale(1, BigDecimal.ROUND_DOWN);
        //进位处理，2.35->2.4
        num2.setScale(1, BigDecimal.ROUND_UP);
        //四舍五入，2.35->2.4
        num2.setScale(1, BigDecimal.ROUND_HALF_UP);
        //四舍五入，2.35->2.3，如果是5则向下舍
        num2.setScale(1, BigDecimal.ROUND_HALF_DOWN);
    }

    private static void initDemo() {
        // 整型数据在Java程序中有3种表示形式，分别为十进制、八进制和十六进制
        // 十进制：除了数字0，不能以0作为其他十进制数的开头。如120、0、-127。
        int int10 = 120;
        // 八进制：必须以0开头。如0123(转换为十进制数为83)、-0123(转换为十进制数为-83)。tips: 血与泪的教训，没事不要在给int赋值的时候带上0，会疯的。
        int int8 = 0123;
        // 十六进制：必须以0X或0x开头。如0x25(转换为十进制数是37)、0Xb01e(转换为十进制数是45086)。
        int int16 = 0Xb01e;

        // 无论如何，Integer与new Integer不会相等。不会经历拆箱过程，i1的引用指向堆，而i2指向专门存放他的内存（常量池），他们的内存地址不一样，所以为false
        Integer i1 = new Integer(127);
        Integer i2 = 127;
        // false
        System.out.println(i1 == i2);

        // 两个都是非new出来的Integer，如果数在-128到127之间，则是true,否则为false
        // java在编译Integer i2 = 128的时候,被翻译成-> Integer i2 = Integer.valueOf(128);而valueOf()函数会对-128到127之间的数进行缓存
        Integer i3 = 127;
        Integer i4 = 127;
        // true
        System.out.println(i3 == i4);
        Integer i5 = 128;
        Integer i6 = 128;
        // false
        System.out.println(i5 == i6);
        // 两个都是new出来的,都为false
        Integer i7 = new Integer(127);
        Integer i8 = new Integer(127);
        // false
        System.out.println(i7 == i8);
        // int和integer(无论new否)比，都为true，因为会把Integer自动拆箱为int再去比
        Integer i9 = new Integer(128);
        int i10 = 128;
        // true
        System.out.println(i9 == i10);
    }

    private static void mathDemo() {
        // 向上取整用
        Math.ceil(123.56f);
        // 向下取整用
        Math.floor(123.56f);
        // 基于系统时间生成一个[0,1.0)区间的随机小数。
        Math.random();
    }

    private static void doubleDemo() {
        // double类型数据过大时会自动转换成指数形式
        // 1.234567890002E9
        System.out.println(1234567890.002);
        // 1.23456789002E8
        System.out.println(123456789.002);
        // 1.2345678002E7
        System.out.println(12345678.002);
        // 1234567.002
        System.out.println(1234567.002);
        // 1234567.123456789
        System.out.println(1234567.123456789);
        // 123456.002
        System.out.println(123456.002);

        // 0.1+0.2 != 0.3
        // float、double浮点数采用二进制存储数据，能用2的指数表达的十进制数可准确的存储其数值，而其他数据存储的只是近似值。
        // float用1位表示符号，8位表示指数，32小数；double用1位表示小数，11位指数，52位小数。
        //  例如：0.1F: 0 01111011 1001100110011001101 = 0.100000001490116119384765625
        if ((0.1 + 0.2) == 0.3) {
            System.out.println("0.1+0.2 != 0.3");
        }
    }
}
