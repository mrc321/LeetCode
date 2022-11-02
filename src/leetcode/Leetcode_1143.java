package leetcode;

public class Leetcode_1143 {

    public int longestCommonSubsequence(String text1, String text2) {
        int len1 = text1.length();
        int len2 = text2.length();
        char[] str1 = text1.toCharArray();
        char[] str2 = text2.toCharArray();
        //申请一个dp表
        int[][] dp = new int[len1][len2];
        //给00位置填数
        dp[0][0] = str1[0] == str2[0] ? 1 : 0;
        //给第一行填数
        for (int index1 = 1; index1 < len1; index1++) {
            dp[index1][0] = str1[index1] == str2[0] ? 1 : dp[index1 - 1][0];
        }
        //给第一列填数
        for (int index2 = 1; index2 < len2; index2++) {
            dp[0][index2] = str1[0] == str2[index2] ? 1 : dp[0][index2 - 1];
        }
        //给中间部分填数
        for (int index1 = 1; index1 < len1; index1++) {
            for (int index2 = 1; index2 < len2; index2++) {
                //在这里总共也分为三种情况：
                //1、考虑text1的末尾，排除text2的末尾
                int p1 = dp[index1][index2 - 1];
                //2、排除text1的末尾，考虑text2的末尾
                int p2 = dp[index1 - 1][index2];
                //3、排除text1的末尾，排除text2的末尾
                int p3 = (str1[index1] == str2[index2] ? 1 : 0) + dp[index1 - 1][index2 - 1];
                dp[index1][index2] = Math.max(Math.max(p1, p2), p3);
            }
        }
        return dp[len1 - 1][len2 - 1];
    }

}
