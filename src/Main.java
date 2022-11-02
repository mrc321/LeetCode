import zuo.structure.DC3;

public class Main {
    public static void main(String[] args) {
        System.out.println(lastSubstring("leetcode"));
    }

    public static String lastSubstring(String s) {
        if (s.length() == 1) {
            return s;
        }

        int min = Integer.MAX_VALUE;
        int max = Integer.MIN_VALUE;
        char[] chars = s.toCharArray();

        for (char ch : chars) {
            min = Math.min(min, ch);
            max = Math.max(max, ch);
        }

        int[] nums = new int[s.length()];
        for (int i = 0; i < nums.length; i++) {
            //最低都要取1
            nums[i] = chars[i] - min + 1;
        }

        //第一个参数传原数组，第二个参数传每个元素最大能取到的值
        int[] sa = new DC3(nums, max - min + 1).sa;
        return s.substring(sa[nums.length - 1]);
    }
}