package leetcode;

public class Leetcode_516 {
    public int longestPalindromeSubseq(String s) {
        char[] str = s.toCharArray();
        int N = str.length;
        //start和end的范围都为[0,N-1]
        int[][] dp = new int[N][N];
        //根据递归逻辑填表
        //填写对角线
        for (int i = 0; i < N; i++) {
            dp[i][i] = 1;
            if (i + 1 < N) {
                dp[i][i + 1] = str[i] == str[i + 1] ? 2 : 1;
            }
        }
        //从下往上，从左往由填
        for (int start = N - 3; start >= 0; start--) {
            for (int end = start + 2; end < N; end++) {
                int p1 = dp[start + 1][end - 1] + (str[start] == str[end] ? 2 : 0);
                int p2 = dp[start + 1][end];
                int p3 = dp[start][end - 1];
                dp[start][end] = Math.max(p1, Math.max(p2, p3));
            }
        }
        return dp[0][N - 1];
    }

    private int process(char[] str, int start, int end) {
        //考虑边界条件
        if (start == end) {
            return 1;
        }
        if (start + 1 == end) {
            return str[start] == str[end] ? 2 : 1;
        }

        //1、不考虑start，不考虑end
        int p1 = process(str, start + 1, end - 1);
        //2、考虑start，不考虑end
        int p2 = process(str, start, end - 1);
        //3、不考虑start，考虑end
        int p3 = process(str, start + 1, end);
        //4、判断当前start和end是否相等
        if (str[start] == str[end]) {
            p1 += 2;
        }
        return Math.max(p1, Math.max(p2, p3));
    }
}
