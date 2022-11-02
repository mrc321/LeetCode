package leetcode;

public class Leetcode_64 {
    public static void main(String[] args) {
        int[][] grid = {
                {1, 3, 1},
                {1, 5, 1},
                {4, 2, 1}};
        grid = new int[][]{
                {1, 2, 3},
                {4, 5, 6}};


        System.out.println(new Leetcode_64().minPathSumDp3(grid));
    }

    public int minPathSum(int[][] grid) {
        int rowSize = grid.length;
        int colSize = grid[0].length;
        System.out.println(rowSize);
        System.out.println(colSize);
        return process(grid, rowSize - 1, colSize - 1, 0, 0);
    }

    private int process(int[][] grid, int a, int b, int row, int col) {
        //越界
        if (row == a && col == b) {
            return grid[a][b];
        }
        int p1 = Integer.MAX_VALUE;
        int p2 = Integer.MAX_VALUE;
        //向右边走
        if (col + 1 <= b) {
            p1 = grid[row][col] + process(grid, a, b, row, col + 1);
        }

        //向下走
        if (row + 1 <= a) {
            p2 = grid[row][col] + process(grid, a, b, row + 1, col);
        }
        return Math.min(p1, p2);
    }

    public int minPathSumDp(int[][] grid) {
        //x的范围[0,M-1] y的范围在[0,N-1]
        int M = grid.length;
        int N = grid[0].length;
        int[][] dp = new int[M][N];

        for (int x = M - 1; x >= 0; x--) {
            for (int y = N - 1; y >= 0; y--) {

                if (x == M - 1 && y == N - 1) {
                    dp[M - 1][N - 1] = grid[M - 1][N - 1];
                    continue;
                }

                int p1 = Integer.MAX_VALUE;
                int p2 = Integer.MAX_VALUE;
                //向右边走
                if (y + 1 < N) {
                    p1 = grid[x][y] + dp[x][y + 1];
                }

                //向下走
                if (x + 1 < M) {
                    p2 = grid[x][y] + dp[x + 1][y];
                }
                dp[x][y] = Math.min(p1, p2);
            }
        }
        return dp[0][0];
    }

    public int minPathSumDp2(int[][] grid) {
        //x的范围[0,M-1] y的范围在[0,N-1]
        int M = grid.length;
        int N = grid[0].length;

        for (int y = N - 2; y >= 0; y--) {
            grid[M - 1][y] += grid[M - 1][y + 1];
        }
        for (int x = M - 2; x >= 0; x--) {
            grid[x][N - 1] += grid[x + 1][N - 1];
        }
        for (int x = M - 2; x >= 0; x--) {
            for (int y = N - 2; y >= 0; y--) {
                grid[x][y] += Math.min(grid[x][y + 1], grid[x + 1][y]);
            }
        }
        return grid[0][0];
    }

    public int minPathSumDp3(int[][] grid) {
        //x的范围[0,row-1] y的范围在[0,colSize-1]
        int rowSize= grid.length;
        int colSize = grid[0].length;
        int[] dp = new int[colSize];

        //计算出第一行，一直往右累加
        dp[0] = grid[0][0];
        for (int col = 1; col < colSize; col++) {
            dp[col] = grid[0][col] + dp[col - 1];
        }
        for (int row = 1; row < rowSize; row++) {
            //第一列的直接加上一行的值
            dp[0] += grid[row][0];
            for (int col = 1; col < colSize; col++) {
                //此时dp[col-1]是左边路线的值，dp[col]是上面路线的值
                dp[col] = grid[row][col] + Math.min(dp[col - 1], dp[col]);
            }
        }
        return dp[colSize - 1];
    }
}
