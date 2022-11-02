package leetcode;

import java.util.HashMap;
import java.util.HashSet;

public class Leetcode_329 {


    public static int longestIncreasingPath(int[][] matrix) {
        int max = 0;
        for (int i = 0; i < matrix.length; i++) {
            for (int j = 0; j < matrix[0].length; j++) {
                max = Math.max(max, process(matrix, i, j));
            }
        }
        return max;
    }

    //以坐标x、y为起点，返回最长递增路径长度
    private static int process(int[][] matrix, int x, int y) {
        //上下左右走
        int cur = matrix[x][y];
        //上走
        int p1 = x > 0 && matrix[x - 1][y] > cur ? process(matrix, x - 1, y) : 0;
        int p2 = x < matrix.length - 1 && matrix[x + 1][y] > cur ? process(matrix, x + 1, y) : 0;
        int p3 = y > 0 && matrix[x][y - 1] > cur ? process(matrix, x, y - 1) : 0;
        int p4 = y < matrix[0].length - 1 && matrix[x][y + 1] > cur ? process(matrix, x, y + 1) : 0;

        return Math.max(Math.max(p1, p2), Math.max(p3, p4)) + 1;
    }

    public static int longestIncreasingPath1(int[][] matrix) {
        int max = 0;
        int[][] dp = new int[matrix.length][matrix[0].length];
        for (int i = 0; i < matrix.length; i++) {
            for (int j = 0; j < matrix[0].length; j++) {
                max = Math.max(max, process(matrix, i, j, dp));
            }
        }
        return max;
    }

    //以坐标x、y为起点，返回最长递增路径长度
    private static int process(int[][] matrix, int x, int y, int[][] dp) {
        if (dp[x][y] == 0) {
            //上下左右走
            int cur = matrix[x][y];
            //上走
            int p1 = x > 0 && matrix[x - 1][y] > cur ? process(matrix, x - 1, y, dp) : 0;
            int p2 = x < matrix.length - 1 && matrix[x + 1][y] > cur ? process(matrix, x + 1, y, dp) : 0;
            int p3 = y > 0 && matrix[x][y - 1] > cur ? process(matrix, x, y - 1, dp) : 0;
            int p4 = y < matrix[0].length - 1 && matrix[x][y + 1] > cur ? process(matrix, x, y + 1, dp) : 0;
            dp[x][y] = Math.max(Math.max(p1, p2), Math.max(p3, p4)) + 1;
        }
        return dp[x][y];
    }

    public static void main(String[] args) {
        int[][] matrix = {{9, 9, 4}, {6, 6, 8}, {2, 1, 1}};
        System.out.println(longestIncreasingPath(matrix));
    }
}

