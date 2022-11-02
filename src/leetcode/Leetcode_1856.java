package leetcode;

import java.util.Deque;
import java.util.LinkedList;

public class Leetcode_1856 {
    //计算出每个以nums[i]为最小值的数组的累加和 * nums[i]
    public int maxSumMinProduct(int[] nums) {
        int length = nums.length;
        //使用一个数组记录累加和，因此对于nums数组求任意区间[i,j]的累加和可以直接通过sum[j] - sum[i - 1]得到
        long[] sum = new long[length];
        sum[0] = nums[0];
        for (int i = 1; i < length; i++) {
            sum[i] += sum[i - 1] + nums[i];
        }
        long max = -1;
        Deque<Integer> stack = new LinkedList<>();
        for (int i = 0; i < length; i++) {
            while (!stack.isEmpty() && nums[stack.peek()] >= nums[i]) {
                //表示在区间[left + 1,right]内，curMin是最小值
                int curMin = nums[stack.pop()];
                int right = i - 1;
                int left = stack.isEmpty() ? -1 : stack.peek();
                max = Math.max(max, (sum[right] - (left == -1 ? 0 : sum[left])) * curMin);
            }
            stack.push(i);
        }

        while (!stack.isEmpty()) {
            int curMin = nums[stack.pop()];
            int right = length - 1;
            int left = stack.isEmpty() ? -1 : stack.peek();
            max = Math.max(max, (sum[right] - (left == -1 ? 0 : sum[left])) * curMin);
        }
        return (int) (max % (1000000007));
    }
}
