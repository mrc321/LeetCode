package leetcode;

import java.util.Arrays;
import java.util.Comparator;

public class Leetcode_322 {

    //暴力递归
    public int coinChange(int[] coins, int amount) {
        Arrays.sort(coins);
        for (int i = 0, j = coins.length - 1; i < j; i++, j--) {
            int tmp = coins[i];
            coins[i] = coins[j];
            coins[j] = tmp;
        }
        int result = process(coins, 0, amount);
        return result == Integer.MAX_VALUE ? -1 : result;
    }

    //求coins[index....]范围上组成rest所需要的最少硬币数
    private int process(int[] coins, int index, int rest) {
        if (index == coins.length) {
            return rest == 0 ? 0 : Integer.MAX_VALUE;
        }
        //最少硬币个数，那么返回的就是所需最少硬币数
        int min = Integer.MAX_VALUE;
        //total表示最多能够选择coins[index]硬币的数量
        int total = rest / coins[index];
        for (int num = 0; num <= total; num++) {
            int later = process(coins, index + 1, rest - coins[index] * num);
            if (later != Integer.MAX_VALUE) {
                min = Math.min(min, num + later);
            }
        }
        return min;
    }

    //三层for
    public int coinChangeDp(int[] coins, int amount) {
        //index:[0,N]  rest:[0,amount]
        int N = coins.length;
        int[][] dp = new int[N + 1][amount + 1];

        for (int rest = 1; rest <= amount; rest++) {
            dp[N][rest] = Integer.MAX_VALUE;
        }

        for (int index = N - 1; index >= 0; index--) {
            for (int rest = 0; rest <= amount; rest++) {
                int min = Integer.MAX_VALUE;
                int total = rest / coins[index];
                for (int num = 0; num <= total; num++) {
                    int later = dp[index + 1][rest - coins[index] * num];
                    if (later != Integer.MAX_VALUE) {
                        min = Math.min(min, num + later);
                    }
                }
                dp[index][rest] = min ;
            }
        }
        return dp[0][amount] == Integer.MAX_VALUE ? -1 : dp[0][amount];
    }

    public static void main(String[] args) {
        int[] coins = {1, 2, 5};
        System.out.println(coinChangeDp2(coins, 11));

    }
    //不带空间压缩
    public static int coinChangeDp2(int[] coins, int amount) {
        //index:[0,N]  rest:[0,amount]
        int N = coins.length;
        int[][] dp = new int[N + 1][amount + 1];

        for (int rest = 1; rest <= amount; rest++) {
            dp[N][rest] = Integer.MAX_VALUE;
        }

        for (int index = N - 1; index >= 0; index--) {
            for (int rest = 0; rest <= amount; rest++) {
                int min = Integer.MAX_VALUE;

                if (dp[index + 1][rest] != Integer.MAX_VALUE) {
                    min = dp[index + 1][rest];
                }
                if (rest / coins[index] > 0) {
                    int later = dp[index][rest - coins[index]];
                    if (later != Integer.MAX_VALUE) {
                        min = Math.min(min, 1 + later);
                    }
                }
                dp[index][rest] = min ;
            }
        }
        return dp[0][amount] == Integer.MAX_VALUE ? -1 : dp[0][amount];
    }
    //带空间压缩
    public static int coinChangeDp3(int[] coins, int amount) {
        //index:[0,N]  rest:[0,amount]
        int N = coins.length;
        int[] dp = new int[amount + 1];

        //值为Integer.MAX_VALUE的表示不可达，无效组合
        for (int rest = 1; rest <= amount; rest++) {
            dp[rest] = Integer.MAX_VALUE;
        }

        for (int index = N - 1; index >= 0; index--) {
            for (int rest = 0; rest <= amount; rest++) {
                int min = Integer.MAX_VALUE;
                //表示coins[index]的num为0时，所需使用的硬币数
                if (dp[rest] != Integer.MAX_VALUE) {
                    min = dp[rest];
                }
                if (rest / coins[index] > 0) {
                    int later = dp[rest - coins[index]];
                    if (later != Integer.MAX_VALUE) {
                        //later不为Integer.MAX_VALUE，则表示当前可以使用一枚硬币，所以加上一
                        min = Math.min(min, 1 + later);
                    }
                }
                //min仍然为Integer.MAX_VALUE，则表示当前rest余额，不能选择使用coins[index]硬币
                dp[rest] = min;
            }
        }
        return dp[amount]== Integer.MAX_VALUE ? -1 : dp[amount];
    }
}
