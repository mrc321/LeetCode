package leetcode;

public class Leetcode_209 {
    public int minSubArrayLen(int target, int[] nums) {
        int leftIndex = 0;
        int rightIndex = 0;
        long sum = nums[0];
        int minLen = Integer.MAX_VALUE;
        while (rightIndex < nums.length) {
            if (sum >= target) {
                minLen = Math.min(minLen, rightIndex - leftIndex + 1);
                sum -= nums[leftIndex++];
            } else if (sum < target) {
                rightIndex++;
                if (rightIndex < nums.length) {
                    sum += nums[rightIndex];
                }
            }
        }
        return minLen != Integer.MAX_VALUE ? minLen : 0;
    }
}
