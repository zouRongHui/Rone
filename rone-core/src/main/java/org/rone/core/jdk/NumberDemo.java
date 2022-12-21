package org.rone.core.jdk;

import java.math.BigDecimal;
import java.math.RoundingMode;
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

    /**
     * BigDecimal 的坑
     */
    private static void bigDecimalTips() {
        // new BigDecimal()还是BigDecimal#valueOf()
        BigDecimal bd1 = new BigDecimal(0.01);
        BigDecimal bd2 = BigDecimal.valueOf(0.01);
        // bd1 = 0.01000000000000000020816681711721685132943093776702880859375
        System.out.println("bd1 = " + bd1);
        // bd2 = 0.01
        System.out.println("bd2 = " + bd2);
        // 造成这种差异的原因是0.1这个数字计算机是无法精确表示的，送给BigDecimal的时候就已经丢精度了，而BigDecimal#valueOf的实现却完全不同
        // 它使用了浮点数相应的字符串来构造BigDecimal对象，因此避免了精度问题。所以大家要尽量要使用字符串而不是浮点数去构造BigDecimal对象，如果实在不行，就使用BigDecimal#valueOf()方法吧。

        // 等值比较
        bd1 = new BigDecimal("1.0");
        bd2 = new BigDecimal("1.00");
        // false
        System.out.println(bd1.equals(bd2));
        // 0
        System.out.println(bd1.compareTo(bd2));

        // BigDecimal并不代表无限精度
        BigDecimal a = new BigDecimal("1.0");
        BigDecimal b = new BigDecimal("3.0");
        // java.lang.ArithmeticException: Non-terminating decimal expansion; no exact representable decimal result.
        a.divide(b);
        // 像这种无限精度的算法，需要制定四舍五入的格式
        a.divide(b, 2, BigDecimal.ROUND_HALF_UP);

        // BigDecimal转回String要小心
        BigDecimal d = BigDecimal.valueOf(12334535345456700.12345634534534578901);
        // 1.23345353454567E+16
        System.out.println(d.toString());
        // 可以看到结果已经被转换成了科学计数法，可能这个并不是预期的结果BigDecimal有三个方法可以转为相应的字符串类型，切记不要用错：
        // toString();     // 有必要时使用科学计数法
        // toPlainString();   // 不使用科学计数法
        // toEngineeringString();  // 工程计算中经常使用的记录数字的方法，与科学计数法类似，但要求10的幂必须是3的倍数

        // 执行顺序不能调换（乘法交换律失效）。乘法满足交换律是一个常识，但是在计算机的世界里，会出现不满足乘法交换律的情况
        a = BigDecimal.valueOf(1.0);
        b = BigDecimal.valueOf(3.0);
        BigDecimal c = BigDecimal.valueOf(3.0);
        System.out.println(a.divide(b, 2, RoundingMode.HALF_UP).multiply(c)); // 0.990
        System.out.println(a.multiply(c).divide(b, 2, RoundingMode.HALF_UP)); // 1.00


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
