package leetcode;

public class Leetcode_509 {
    //使用矩阵乘法，可以优化到 O(logN)
    public int fib(int n) {
        if (n == 0 || n == 1) {
            return n;
        }
        int[][] base = {
                {1, 1},
                {1, 0}
        };
        int[][] res = matrixPower(base, n - 1);
        return res[0][0];
    }

    //开始计算矩阵base的power次方
    private int[][] matrixPower(int[][] base, int power) {
        int N = base.length;
        int M = base[0].length;
        int[][] res = new int[N][M];
        int[][] tmp = base;
        //将res初始化为单位矩阵
        for (int i = 0; i < N; i++) {
            res[i][i] = 1;
        }
        //根据power的每一位来计算
        for (; power != 0; power = power >>> 1) {
            //根据最右侧是否为1，来选择res是否跟tmp相乘
            if ((power & 1) == 1) {
                res = muliMatrix(res, tmp);
            }
            //每一次遍历tmp都需要自乘一次
            tmp = muliMatrix(tmp,tmp);
        }
        return res;
    }

    //两矩阵乘法
    private int[][] muliMatrix(int[][] left, int[][] right) {
        //结果矩阵的行数为左侧矩阵的行数，列数为右侧矩阵的列数
        int[][] res = new int[left.length][right[0].length];
        //遍历每一行
        for (int i = 0; i < left.length; i++) {
            //遍历每一列
            for (int j = 0; j < right[0].length; j++) {
                //前行乘后列，然后累加
                for (int k = 0; k < left[0].length; k++) {
                    res[i][j] += left[i][k] * right[k][j];
                }
            }
        }
        return res;
    }
}
