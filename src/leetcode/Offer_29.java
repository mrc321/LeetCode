package leetcode;

import java.util.Arrays;

public class Offer_29 {
    public static void main(String[] args) {
        int[][] matrix = {{6, 9, 7}};
        System.out.println(Arrays.toString(new Offer_29().spiralOrder(matrix)));
        matrix = new int[][]{{1, 2, 3}, {4, 5, 6}, {7, 8, 9}};
        System.out.println(Arrays.toString(new Offer_29().spiralOrder(matrix)));
        matrix = new int[][]{{1, 2, 3, 4}, {5, 6, 7, 8}, {9, 10, 11, 12}};
        System.out.println(Arrays.toString(new Offer_29().spiralOrder(matrix)));
        matrix = new int[][]{{3}, {5}, {6}, {7}};
        System.out.println(Arrays.toString(new Offer_29().spiralOrder(matrix)));
        matrix = new int[][]{{3, 5}};
        System.out.println(Arrays.toString(new Offer_29().spiralOrder(matrix)));
        matrix = new int[][]{{5}};
        System.out.println(Arrays.toString(new Offer_29().spiralOrder(matrix)));
    }

    public int[] spiralOrder(int[][] matrix) {
        //判空
        if (matrix == null || matrix.length == 0 || matrix[0].length == 0) {
            return new int[]{};
        }
        //新建一个新数组用来存放结果
        int[] result = new int[matrix.length * matrix[0].length];
        //左上节点坐标
        int lx = 0;
        int ly = 0;
        //右下角节点坐标
        int rx = matrix.length - 1;
        int ry = matrix[0].length - 1;
        int index = 0;
        //两对角节点是一个左上，一个右下，不能错位，否则会导致越界
        while (lx <= rx && ly <= ry) {
            index = printCircle(matrix, result, index, lx++, ly++, rx--, ry--);
        }
        return result;
    }

    //打印一圈
    private int printCircle(int[][] matrix, int[] result, int index, int lx, int ly, int rx, int ry) {
        //上边
        for (int col = ly; col <= ry; col++) {
            result[index++] = matrix[lx][col];
        }
        //右边
        for (int row = lx + 1; row <= rx; row++) {
            result[index++] = matrix[row][ry];
        }
        //下边，不能与上边是同一条边
        for (int col = ry - 1; lx != rx && col >= ly; col--) {
            result[index++] = matrix[rx][col];
        }
        //左边，不能与右边是同一条边
        for (int row = rx - 1; ly != ry && row > lx; row--) {
            result[index++] = matrix[row][ly];
        }
        return index;
    }
}
