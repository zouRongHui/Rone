package org.rone.core.jdk;

/**
 * 通过一个label来指定break/continue的范围，一般用在多重循环的转跳上。
 * 当需要转跳多重循环时使用 break label、continue label
 * label:
 * while() {
 *     while() {
 *         ...
 *         break label;
 *         //continue label;
 *         ...
 *     }
 * }
 * 其实一重循环的转跳也是如此
 * @author rone
 */
public class LabelDemo {

    public static void main(String[] args) {
        int length = 3;
        for (int i = 0; i < length; i++) {
            label:
            for (int j = 0; j < length; j++) {
                for (int k = 0; k < length; k++) {
                    System.out.println(i + "," + j + "," + k);
                    if (k == 1) {
                        break label;
//						continue second;
                    }
                }
            }
        }
    }
	/*
	 输出：
	 0,0,0
	 0,0,1
	 1,0,0
	 1,0,1
	 2,0,0
	 2,0,1
	 */
}
