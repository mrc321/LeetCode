package leetcode;

import sun.print.DialogOnTop;

public class Leetcode_796 {
    public boolean rotateString(String s, String goal) {
        if (s.length() != goal.length()) {
            return false;
        }

        return KMP(s + s, goal);
    }

    private boolean KMP(String str1, String str2) {
        char[] str1Array = str1.toCharArray();
        char[] str2Array = str2.toCharArray();
        int[] next = getNextArray(str2Array);
        int index1 = 0;
        int index2 = 0;
        while (index1 < str1.length() && index2 < str2.length()) {
            if (str1Array[index1] == str2Array[index2]) {
                index1++;
                index2++;
            } else if (index2 > 0) {
                index2 = next[index2];
            } else {
                index1++;
            }
        }
        return index2 == str2.length();
    }

    private int[] getNextArray(char[] match) {
        if (match.length == 1) {
            return new int[]{-1};
        }
        int[] next = new int[match.length];
        next[0] = -1;
        next[1] = 0;
        int index = 2;
        int p = 0;
        while (index < match.length) {
            if (match[index - 1] == match[p]) {
                next[index++] = ++p;
            } else if (p > 0) {
                p = next[p];
            } else {
                next[index++] = 0;
            }
        }
        return next;
    }
}
