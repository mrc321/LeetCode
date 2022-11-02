package leetcode;

import java.util.HashMap;

public class Leetcode_325 {
    public int maxSubArrayLen(int[] nums, int k) {
        HashMap<Integer, Integer> preSumMap = new HashMap<>();
        preSumMap.put(0, -1);
        int preSum = 0;
        int maxLen = 0;
        for (int i = 0; i < nums.length; i++) {
            //以当前i以为的区间[0,i]的累加和
            preSum += nums[i];
            Integer leftIndex = preSumMap.get(preSum - k);
            //如果存在，则更新长度
            if (leftIndex != null) {
                maxLen = Math.max(maxLen, i - leftIndex);
            }
            //只有不存在再存，因为后面出现的前缀和的下标肯定没有之前出现的下标小
            if (!preSumMap.containsKey(preSum)) {
                preSumMap.put(preSum, i);
            }
        }
        return maxLen;
    }
}
