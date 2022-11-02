package leetcode;

public class Leetcode_28 {
    public int strStr(String str1, String str2) {
        //如果主串比字串还短，直接返回-1表示无效
        int len1 = str1.length();
        int len2 = str2.length();
        if (len1 < len2) {
            return -1;
        }
        //将两个串都转成字符数组用于比较
        char[] str1Chars = str1.toCharArray();
        char[] str2Chars = str2.toCharArray();

        //获取next数组
        int[] next = getNextArray(str2Chars);
        //两个字符串都从头开始比较
        int index1 = 0;
        int index2 = 0;
        //设置的条件就是两个字符串的下标都不能越界
        while (index1 < len1 && index2 < len2) {
            //两个字符串对应字符相同，就共通都向后移动一位
            if (str1Chars[index1] == str2Chars[index2]) {
                index1++;
                index2++;
            } else if (index2 > 0) {
                //对应字符不匹配，则让index2重定位到最长前缀之后的位置
                index2 = next[index2];
            } else {
                //此时表示index2指向的是0位置，并且str1Chars[index1] != str2Chars[0]
                //则index1要向后移动
                index1++;
            }
        }
        //只有匹配成功时，index2才会越界
        return index2 == len2 ? index1 - len2 : -1;
    }

    private int[] getNextArray(char[] match) {
        int length = match.length;
        if (match.length == 1) {
            return new int[]{-1};
        }
        int[] next = new int[length];
        next[0] = -1;
        next[1] = 0;
        //p始终表示表示match[i - 1]位置的最长前后缀的长度，即next[i - 1]的值，同时也是最长前缀的下一个字符的位置
        int p = 0;
        //next[i] 表示正在求match[i]位置的最长前后缀的长度
        int i = 2;
        while (i < length) {
            //如果此时
            if (match[i - 1] == match[p]) {
                next[i++] = ++p;
            } else if (p > 0) {
                //必须保证p要大于等，因为p等于0的话，执行这里会导致p = -1；
                //意义为p一直寻找能够匹配的最长前缀
                p = next[p];
            } else {
                //如果p等于0，match[i-1]仍然不等于match[0]，则说明无最长前缀，直接赋值为 0
                next[i++] = 0;
            }
        }
        return next;
    }
}
