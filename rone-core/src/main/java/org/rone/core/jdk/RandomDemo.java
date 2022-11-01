package org.rone.core.jdk;

import java.util.Random;

/**
 * Random类中实现的随机算法是伪随机，也就是有规则的随机。在进行随机时，随机算法的起源数字称为种子数(seed)，在种子数的基础上进行一定的变换，从而产生需要的随机数字。
 * 	相同种子数的Random对象，相同次数生成的随机数字是完全相同的。也就是说，两个种子数相同的Random对象，第一次生成的随机数字完全相同，第二次生成的随机数字也完全相同。
 * @author rone
 */
public class RandomDemo {

    public static void main(String[] args) {

    }

    public static void demo() {
        // 该构造方法使用一个和当前系统时间对应的相对时间有关的数字作为种子数，然后使用这个种子数构造Random对象。种子数只是随机算法的起源数字，和生成的随机数字的区间无关。
        Random random = new Random();
        // 该构造方法可以通过自定一个种子数进行创建。
        random = new Random(System.currentTimeMillis());

        // 该方法的作用是生成一个随机的boolean值，生成true和false的值几率相等，也就是都是50%的几率。
        System.out.println(random.nextBoolean());
        // 该方法的作用是生成一个随机的double值，数值介于[0,1.0)之间，这里中括号代表包含区间端点，小括号代表不包含区间端点，也就是0到1之间的随机小数，包含0而不包含1.0。
        System.out.println(random.nextDouble());
        // 该方法的作用是生成一个随机的int值，该值介于int的区间，也就是-231到231-1之间。
        System.out.println(random.nextInt());
        // 该方法的作用是生成一个随机的int值，该值介于[0,n)的区间，也就是0到n之间的随机int值，包含0而不包含n。
        System.out.println(random.nextInt(100));
        // 该方法的作用是重新设置Random对象中的种子数。设置完种子数以后的Random对象和相同种子数使用new关键字创建出的Random对象相同。
        random.setSeed(System.currentTimeMillis());
    }
}
