package leetcode;

public class Leetcode_5 {
    public String longestPalindrome(String s) {
        //转成处理串 "12132" -> "#1#2#1#3#2#"
        char[] strArray = tranString(s);
        //存放每个位置的回文半径
        int[] radius = new int[strArray.length];
        int C = 0;
        int R = -1;
        int maxIndex = 0;
        for (int i = 0; i < strArray.length; i++) {
            //默认半径为1(即自己)
            //i' = 2 * C - i
            radius[i] = i < R ? Math.min(radius[2 * C - i], R - i) : 1;
            //开始向两边扩,循环条件为两边不越界
            while (i - radius[i] > -1 && i + radius[i] < strArray.length) {
                if (strArray[i - radius[i]] == strArray[i + radius[i]]) {
                    radius[i]++;
                } else {
                    //只有两边字符相等时才会向两边扩，否则直接终止
                    break;
                }
            }
            //查看是否需要更新R，C
            if (R < i + radius[i]) {
                R = i + radius[i];
                C = i;
            }
            //记录最长回文字子串的i位置
            if (radius[i] > radius[maxIndex]) {
                maxIndex = i;
            }
        }
        StringBuilder builder = new StringBuilder();
        //拼接最长回文字串
        for (int i = maxIndex - radius[maxIndex] + 2; i < maxIndex + radius[maxIndex]; i += 2) {
                builder.append(strArray[i]);
        }
        return builder.toString();
    }

    //转成处理串
    private char[] tranString(String str) {
        char[] charArray = str.toCharArray();
        char[] res = new char[2 * str.length() + 1];
        int index = 0;
        for (int i = 0; i < res.length; i++) {
            res[i] = (i & 1) == 0 ? '#' : charArray[index++];
        }
        return res;
    }
}
