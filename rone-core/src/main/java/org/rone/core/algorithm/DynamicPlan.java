package org.rone.core.algorithm;

/**
 * 动态规划算法
 * ●.动态规划算法、背包规划问题
 *     解法思路：求最优子解、确定全局最优解和最优子结构之间的关系，以及问题的边界，又叫作确定状态转移方程式。
 *
 * ●.示例：有五座金矿，储量分别为{400, 500, 200, 300, 350}，开采这些矿分别需要工人{5, 5, 3, 4, 3}。
 *     现在有10名工人，要这么分配产值最大？
 *     解法思路：
 *         每一个金矿都存在着“挖”和“不挖”两种选择。
 *         如果最后一个金矿肯定会挖，则问题变成4座矿7个人挖的最大收益。
 *         如果最后一个金矿肯定不挖，则问题变成4座矿10个人挖的最大收益。
 *         上述的两种情况即为全局问题的两个最优子解。
 *         至于最后一个矿挖不挖需要看，如果不挖和挖了哪个收益更大。
 *         即10个人外前面四个的收益 和 最后一个矿收益+7个人挖前面4个矿的收益 哪个更大。
 *         至于第4个矿的选择也可参考此逻辑来处理。
 *         当矿为0个或者工人为0时收益肯定一直为0，这就是边界了。
 * @author rone
 */
public class DynamicPlan {

    public static void main(String[] args) {
        int goldNum = 5;
        int peopleNum = 10;
        int[] golds = new int[]{400, 500, 200, 300, 350};
        int[] goldPeoples = new int[]{5, 5, 3, 4, 3};
        System.out.println(goldRush(goldNum, peopleNum, golds, goldPeoples));
        System.out.println(goldRush(peopleNum, golds, goldPeoples));
        System.out.println(goldRush2(peopleNum, golds, goldPeoples));
    }

    /**
     * 动态规划之挖矿问题
     * 有五座金矿，储量分别为{400, 500, 200, 300, 350}，
     *  开采这些矿分别需要工人{5, 5, 3, 4, 3}。
     *  现在有10名工人，要这么分配产值最大？
     * 核心思想：拆分为求最优子解
     *  确定全局最优解和最优子结构之间的关系，以及问题的边界，又叫作确定状态转移方程式。
     * 该问题的状态转移方程式：
     *  金矿数量 n，工人数量 w，金矿含量 g[]，金矿开采人数 p[]。
     *  F（n，w）为n个金矿、w个工人时的最优收益函数。
     *  当n=0 或 w=0 时触发边界
     *      F(n,w) = 0
     *  当 n≥1,w<p[n-1]
     *      F(n,w) = F(n-1,w)
     *  当 n≥1,w≥p[n-1]
     *      F(n,w) = max(F(n-1,w), F(n-1,w-p[n-1])+g[n-1])
     * 时间复杂度 O(2^n)
     * 空间复杂度 O(1)
     * @param goldNum 金矿数量
     * @param peopleNum 总工人数
     * @param golds 金矿产值数组
     * @param goldPeoples 挖矿所需工人数组
     */
    public static int goldRush(int goldNum, int peopleNum, int[] golds, int[] goldPeoples) {
        if (goldNum == 0 || peopleNum == 0) {
            return 0;
        }
        if (peopleNum < goldPeoples[goldNum - 1]) {
            return goldRush(goldNum - 1, peopleNum, golds, goldPeoples);
        }
        return Math.max(goldRush(goldNum - 1, peopleNum, golds, goldPeoples),
                goldRush(goldNum - 1, peopleNum - goldPeoples[goldNum - 1], golds, goldPeoples) + golds[goldNum - 1]);
    }

    /**
     * 挖矿问题 - 优化解法
     * 自底向上求解
     * 时间复杂度 O(nw)
     * 空间复杂度 O(nw)
     */
    public static int goldRush(int peopleNum, int[] golds, int[] goldPeoples) {
        int[][] result = new int[golds.length + 1][peopleNum + 1];
        for (int i = 1; i <= golds.length; i++) {
            for (int j = 1; j <= peopleNum; j++) {
                if (j < goldPeoples[i-1]) {
                    result[i][j] = result[i-1][j];
                } else {
                    result[i][j] = Math.max(result[i-1][j], result[i-1][j-goldPeoples[i-1]] + golds[i-1]);
                }
            }
        }
        return result[golds.length][peopleNum];
    }

    /**
     * 挖矿问题 - 优化解法2
     * 优化空间复杂度
     * 时间复杂度 O(nw)
     * 空间复杂度 O(w)
     */
    public static int goldRush2(int peopleNum, int[] golds, int[] goldPeoples) {
        int[] result = new int[peopleNum + 1];
        for (int i = 1; i <= golds.length; i++) {
            for (int j = peopleNum; j >= 1; j--) {
                if (j >= goldPeoples[i-1]) {
                    result[j] = Math.max(result[j], result[j-goldPeoples[i-1]]+ golds[i-1]);
                }
            }
        }
        return result[peopleNum];
    }
}
