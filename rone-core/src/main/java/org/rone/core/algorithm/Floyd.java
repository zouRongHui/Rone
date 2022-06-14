package org.rone.core.algorithm;

/**
 * Floyd算法
 * 是一种利用动态规划的思想寻找给定的加权图中多源点之间最短路径的算法
 * @author rone
 */
public class Floyd {

    public static void main(String[] args) {
        int[][] matrix = {
                {0, 5, 2, M, M, M, M},
                {5, 0, M, 1, 6, M, M},
                {2, M, 0, 6, M, 8, M},
                {M, 1, 6, 0, 1, 2, M},
                {M, 6, M, 1, 0, M, 7},
                {M, M, 8, 2, M, 0, 3},
                {M, M, M, M, 7, 3, 0}
        };
        floyd(matrix);
    }

    public static final int M = Integer.MAX_VALUE;

    public static void floyd(int[][] matrix) {
        //循环更新矩阵值，每次循环更新的是j到k的距离，i作为中继点
        for (int i = 0; i < matrix.length; i++) {
            for (int j = 0; j < matrix.length; j++) {
                for (int k = 0; k < matrix.length; k++) {
                    //matrix[j][i]为j点到i点的最短距离
                    //若j点到中继点i或k点到中继点i为无限，则忽略此时更新
                    if (matrix[j][i] == M || matrix[i][k] == M) {
                        continue;
                    }
                    //将当前的距离和通过中继点绕一下的距离之和取两者最小值
                    matrix[j][k] = Math.min(matrix[j][k], matrix[j][i] + matrix[i][k]);
                }
            }
        }
        System.out.println("最短路径矩阵：");
        System.out.println("   A   B   C   D   E   F   G");
        for (int i = 0; i < matrix.length; i++) {
            int ascii = 65 + i;
            System.out.print((char)ascii);
            for (int j = 0; j < matrix.length; j++) {
                System.out.printf("%3d ", matrix[i][j]);
            }
            System.out.println();
        }
    }
}
