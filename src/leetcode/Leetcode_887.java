package leetcode;

import java.net.InetAddress;

public class Leetcode_887 {
    /**
     * @param k 表示还剩下k个棋子
     * @param n 表示还剩下多少楼层
     * @return
     */
    public static int superEggDrop(int k, int n) {
        //还剩下一层楼时，则只要扔一次即可。
        if (n <= 1) {
            return n;
        }
        //只剩下一枚蛋时，只能从第一层开始仍绕n层一个一个试。
        if (k == 1) {
            return n;
        }
        //开始尝试多种扔法
        int ans = Integer.MAX_VALUE;
        for (int level = 1; level < n; level++) {
            int yes = superEggDrop(k - 1, level - 1);
            int no = superEggDrop(k, n - level);
            ans = Math.min(Math.max(yes, no) + 1, ans);
        }
        return ans;
    }

    public static int superEggDrop1(int k, int n) {
        //dp[i][j]表示使用j个鸡蛋从i层楼中准确识别出来的最小操作数
        int[][] dp = new int[n + 1][k + 1];
        //将边界条件补齐
        //只剩一层时，所有情况都只需要试一遍
        for (int restK = 1; restK <= k; restK++) {
            dp[1][restK] = 1;
        }
        //只剩一颗蛋时，所有情况都只需要试剩余楼层的个数
        for (int restN = 1; restN <= n; restN++) {
            dp[restN][1] = restN;
        }
        //从上往下，从右往左
        for (int restN = 2; restN <= n; restN++) {
            for (int restK = k; restK >= 2; restK--) {

                //开始枚举从哪一层开始试是最优
                int ans = Integer.MAX_VALUE;
                for (int level = 1; level <= restN; level++) {
                    int yes = dp[level - 1][restK - 1];
                    int no = dp[restN - level][restK];
                    ans = Math.min(Math.max(yes, no) + 1, ans);
                }
                dp[restN][restK] = ans;
            }
        }
        return dp[n][k];
    }

    public static int superEggDrop2(int k, int n) {
        //dp[i][j]表示使用j个鸡蛋从i层楼中准确识别出来的最小操作数
        int[][] dp = new int[n + 1][k + 1];
        int[][] bestLevel = new int[n + 1][k + 1];
        //将边界条件补齐
        //只剩一层时，所有情况都只需要试一遍
        for (int restK = 1; restK <= k; restK++) {
            dp[1][restK] = 1;
            bestLevel[1][restK] = 1;
        }
        //只剩一颗蛋时，所有情况都只需要试剩余楼层的个数
        for (int restN = 1; restN <= n; restN++) {
            dp[restN][1] = restN;
        }
        //从上往下，从右往左
        for (int restN = 2; restN <= n; restN++) {
            for (int restK = k; restK >= 2; restK--) {

                //开始枚举从哪一层开始试是最优
                int ans = Integer.MAX_VALUE;
                int down = bestLevel[restN - 1][restK];
                int up = restK != k ? bestLevel[restN][restK + 1] : restN;
                int choice = 0;
                for (int level = down; level <= up; level++) {
                    int yes = dp[level - 1][restK - 1];
                    int no = dp[restN - level][restK];
                    int curAns = Math.max(yes, no) + 1;
                    if (curAns <= ans) {
                        ans = curAns;
                        choice = level;
                    }

                }
                dp[restN][restK] = ans;
                bestLevel[restN][restK] = choice;
            }
        }
        return dp[n][k];
    }

    public static int superEggDrop3(int k, int n) {
        if (n == 1) {
            return 1;
        }
        if (k == 1) {
            return n;
        }
        int[] dp = new int[k + 1];
        for (int i = 1; i <= k; i++) {
            dp[i] = 1;
        }
        int times = 2;
        while (true) {
            //pre存放restK的上一个值
            int pre = dp[1];
            for (int restK = 2; restK <= k; restK++) {
                int tmp = dp[restK];
                dp[restK] += pre + 1;
                if (dp[restK] >= n) {
                    return times;
                }
                pre = tmp;
            }
            dp[1] = times;
            times++;
        }
    }

    public static void main(String[] args) {
        int maxN = 10000;
        int maxK = 100;
        int timeTest = 100;
        System.out.println("start");
        for (int i = 0; i < timeTest; i++) {
            int n = (int) (Math.random() * maxN) + 1;
            int k = (int) (Math.random() * maxK) + 1;
//            int n = 10;
//            int k = 1;
            int ans1 = superEggDrop2(k, n);
            int ans2 = superEggDrop3(k, n);
            if (ans1 != ans2) {
                System.out.println("oops!");
                System.out.println(n + " : " + k);
                System.out.println(ans1);
                System.out.println(ans2);
                break;
            }
        }
        System.out.println("end");
    }
}
