package org.rone.core.algorithm;

import java.util.Arrays;
import java.util.Stack;

/**
 * 数学计算相关算法
 * @author rone
 */
public class MathCalculate {

    public static void main(String[] args) {
        System.out.println(checkIntegerIsAnIntegerPowerOfTwo(1));
        System.out.println(checkIntegerIsAnIntegerPowerOfTwo(2));
        System.out.println(checkIntegerIsAnIntegerPowerOfTwo(3));
        System.out.println(checkIntegerIsAnIntegerPowerOfTwo(4));
        System.out.println(checkIntegerIsAnIntegerPowerOfTwo(1024));
        System.out.println(checkIntegerIsAnIntegerPowerOfTwo(1025));
        System.out.println(checkIntegerIsAnIntegerPowerOfTwo(100));

        System.out.println();
        int[] num1 = new int[]{1, 2, 3, 4, 5, 6, 7, 8, 9, 0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 0, 1, 2, 3, 4, 5, 6, 7, 8};
        int[] num2 = new int[]{1, 2, 3, 4, 5, 6, 7, 8, 9, 0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 0, 1, 2, 3, 4, 5, 6, 7, 8, 9};
        int[] result = bigNumberAddition(num1, num2);
        Arrays.stream(result).forEach(System.out::print);
        System.out.println();

        System.out.println();
        System.out.println(removeDigits(3, 1234567));

        System.out.println();
        int[] nums = new int[]{1, 1, 2, 3, 3, 4, 4, 4, 4};
        System.out.println(findLostNum(nums));

        System.out.println();
        int[] nums1 = new int[]{1, 1, 2, 3, 3, 4, 4, 4, 4, 5};
        int[] result2 = findLostNum2(nums1);
        System.out.println(result2[0]);
        System.out.println(result2[1]);

        System.out.println();
        System.out.println(getGreatestCommonDivisor(25, 5));
        System.out.println(getGreatestCommonDivisor(100, 80));
        System.out.println(getGreatestCommonDivisor(27, 14));
    }

    /**
     * 判断一个整数是否是 2 的整数次幂
     * 实现1：暴力枚举
     *  从1开始*2，去和目标数比较，若等于则是2的整数次幂，若小于则继续，若大于则不是
     * 实现2：通过二进制的与运算判断，代码基于此实现
     *  2的整数次幂的数转换成二进制后只有最高位是1其余都是0，减去1后除了最高位为0其余都是1。
     *  基于此特性，将i和i-1做与运算，若结果为0则i为2的整数次幂，否则不是。
     */
    public static boolean checkIntegerIsAnIntegerPowerOfTwo(Integer i) {
        if (i == null) {
            return false;
        }
        return (i & (i - 1)) == 0;
    }

    /**
     * 大整数相加
     * 模拟人工计算加法的方式。
     * 优化的思路：
     *  Java支持Long类型的直接加法，所以可按照 Long.MAX_VALUE 位数 -1 来截取大整数后计算。
     *  没必要一位一位的截取计算。
     * @param num1  采用int数组来表示大整数
     * @param num2  采用int数组来表示大整数
     */
    public static int[] bigNumberAddition(int[] num1, int[] num2) {
        int[] bigNum = num1.length > num2.length ? num1 : num2;
        int[] smallNum = num1.length > num2.length ? num2 : num1;

        int digitsDifference = bigNum.length - smallNum.length;
        int[] result = new int[bigNum.length + 1];
        int carry = 0;
        for (int i = (bigNum.length - 1); i >= 0; i--) {
            int n = carry + bigNum[i];
            int smallNumIndex = i - digitsDifference;
            if (smallNumIndex >= 0) {
                n += smallNum[smallNumIndex];
            }
            result[i + 1] = n % 10;
            carry = n / 10;
        }
        result[0] = carry;

        return result;
    }

    /**
     * 一个整数删除 k 个任意数字后尽可能的小。
     * 优先移除高位中数值【较大】的数字，其中较大的含义为比后一位大。
     * 从高位开始遍历，当该数字比后一位数字大的时候移除。
     */
    public static int removeDigits(int k, int num) {
        String[] numStrings = String.valueOf(num).split("");
        int[] nums = new int[numStrings.length];
        for (int i = 0; i < numStrings.length; i++) {
            nums[i] = Integer.parseInt(numStrings[i]);
        }
        if (k >= nums.length) {
            return 0;
        }
        Stack<Integer> resultStack = new Stack<>();
        for (int j : nums) {
            while (k > 0 && !resultStack.isEmpty() && resultStack.peek() > j) {
                resultStack.pop();
                k--;
            }
            resultStack.push(j);
        }
        while (k > 0) {
            resultStack.pop();
            k--;
        }
        StringBuilder resultStr = new StringBuilder();
        while (!resultStack.isEmpty()) {
            resultStr.append(resultStack.pop());
        }
        return Integer.parseInt(resultStr.reverse().toString());
    }

    /**
     * 一个无序整数数组中只有一个数出现了奇数次其余都是偶数次，求该数。
     * 二进制异或运算 ^ ，相同锝0不同锝1。基于异或运算可以直接解决上述问题。
     */
    public static int findLostNum(int[] nums) {
        int result = 0;
        for (int num : nums) {
            result = result ^ num;
        }
        return result;
    }

    /**
     * 一个无序整数数组中只有两个个数出现了奇数次其余都是偶数次，求这两个数。
     * 采用分治法思想，将数组拆分为两块，各自包含一个出现奇数次的数。
     * 实现：
     *  假设两个奇数次的狮子为 n 和 m 。
     *  首先将所有数做异或运算，即为 n ^ m ，最终会得到一个不为0的数，即其二进制数中肯定有一位为1。
     *  而 n、m 的二进制数肯定在这一位上的数字肯定是不同的。
     *  按照这一位的数字作为标准，将原数组拆分为两个数组。
     *  这两个数组肯定分别满足：仅有一个数出现奇数次，则回归到上一个问题。
     */
    public static int[] findLostNum2(int[] nums) {
        int[] result = new int[2];
        int xorResult = 0;
        for (int num : nums) {
            xorResult = xorResult ^ num;
        }
        if (xorResult == 0) {
            return null;
        }
        int separator = 1;
        while (0 == (separator & xorResult)) {
            separator = separator << 1;
        }
        for (int num : nums) {
            if (0 == (separator & num)) {
                result[0] = result[0] ^ num;
            } else {
                result[1] = result[1] ^ num;
            }
        }
        return result;
    }

    /**
     * 求最大公约数
     * 算法1：暴力枚举
     *  将两者中较小的数每次除以2，的到的商去尝试作为最大公约数，一直到找到这个数。
     *  时间复杂度：O(min(a, b))
     * 算法2：辗转相除法(欧几里得算法)
     *  两个正整数 a 和 b (a>b)，它们的醉倒公约数等于 a 除以 b 的余数 c 和 b 之间的最大公约数。
     *  时间复杂度：O(log(max(a, b)))
     *  缺点：两个大整数下，取模运算性能较差。
     * 算法3：更相减损法(出自《九章算术》)
     *  两个正整数 a 和 b (a>b)，它们的最大公约数等于 a-b 的差值 c 和 b 之间的最大公约数。
     *  时间复杂度：O(max(a, b))
     *  缺点：两个数相差很大时，运算次数太多性能很差
     * 算法4：辗转相除法 + 更相减损法，下面的代码实现即为此算法
     *  当 a 和 b 皆为偶数时，最大公约数(a, b) = 2 * 最大公约数(a/2, b/2) = 2 * 最大公约数(a>>1, b>>1)
     *  当 a 为偶数，b 为奇数时，最大公约数(a, b) = 最大公约数(a/2, b) = 最大公约数(a>>1, b)
     *  当 a 为奇数，b 为偶数时，最大公约数(a, b) = 最大公约数(a, b/2) = 最大公约数(a, b>>1)
     *  当 a 和 b 皆为奇数时，先利用更相减损法运算一次，最大公约数(a, b) = 最大公约数(a - b, b)，此时 a-b 必为偶数，然后又可以继续上面的移位运算
     *  时间复杂度：O(log(max(a, b)))
     */
    public static int getGreatestCommonDivisor(int a, int b) {
        if (a == b) {
            return a;
        }
        // 利用与1进行 与 运算来判断是否为偶数。二进制中1除了最后一位为1其余为0，偶数最后一位为0，奇数为1，与运算为 同为 1&1 = 1。
        if ((a & 1) == 0 && (b & 1) == 0) {
            return getGreatestCommonDivisor(a>>1, b>>1)<<1;
        } else if ((a & 1) == 0 && (b & 1) != 0) {
            return getGreatestCommonDivisor(a>>1, b);
        } else if ((a & 1) != 0 && (b & 1) == 0) {
            return getGreatestCommonDivisor(a, b>>1);
        } else {
            int big = Math.max(a, b);
            int small = Math.min(a, b);
            return getGreatestCommonDivisor(big - small, a - b);
        }
    }
}
