package leetcode;

import java.util.HashMap;
import java.util.HashSet;

public class Leetcode_494 {
    public int findTargetSumWays(int[] nums, int target) {
        return process(nums, 0, target);
    }

    //返回[index...]范围上，能够计算得出rest的方法数
    private int process(int[] nums, int index, int rest) {
        if (index == nums.length) {
            return rest == 0 ? 1 : 0;
        }
        //加上nums[index]，则剩余还要凑的数为rest - nums[index]
        int plus = process(nums, index + 1, rest - nums[index]);
        int sub = process(nums, index + 1, rest + nums[index]);
        return plus + sub;
    }

    private String getKey(int index, int rest) {
        return String.format("%d_%d", index, rest);
    }


    public int findTargetSumWays3(int[] nums, int target) {
        return process(nums, 0, target, new HashMap<Integer, HashMap<Integer, Integer>>());
    }

    //返回[index...]范围上，能够计算得出rest的方法数
    private int process(int[] nums, int index, int rest, HashMap<Integer, HashMap<Integer, Integer>> dp) {

        if (!dp.containsKey(index) || !dp.get(index).containsKey(rest)) {
            if (index == nums.length) {
                return rest == 0 ? 1 : 0;
            }
            //加上nums[index]，则剩余还要凑的数为rest - nums[index]
            int plus = process(nums, index + 1, rest - nums[index], dp);
            int sub = process(nums, index + 1, rest + nums[index], dp);
            if (!dp.containsKey(index)) {
                dp.put(index, new HashMap<>());
            }
            dp.get(index).put(rest, plus + sub);
        }
        return dp.get(index).get(rest);
    }

    public int findTargetSumWays4(int[] nums, int target) {
        int sum = 0;
        //优化一
        for (int i = 0; i < nums.length; i++) {
            nums[i] = Math.abs(nums[i]);
            sum += nums[i];
        }
        //优化二三四
        if (target > sum || target < -sum || ((target & 1) ^ (sum & 1)) == 1) {
            return 0;
        }
        //新的目标值，而且是只算加上去的数
        target = (target + sum) / 2;
        return process1(nums, 0, target);
    }

    //返回[index...]范围上，有多少种数的累加和加上去等于rest
    private int process1(int[] nums, int index, int rest) {

        if (index == nums.length) {
            return rest == 0 ? 1 : 0;
        }
        int yes = process1(nums, index + 1, rest - nums[index]);
        int no = process1(nums, index + 1, rest);
        return yes + no;
    }

    public int findTargetSumWays5(int[] nums, int target) {
        int sum = 0;
        //优化一
        for (int i = 0; i < nums.length; i++) {
            nums[i] = Math.abs(nums[i]);
            sum += nums[i];
        }
        //优化二三四
        if (target > sum || target < -sum || ((target & 1) ^ (sum & 1)) == 1) {
            return 0;
        }
        //新的目标值，而且是只算加上去的数
        target = (target + sum) / 2;

        //优化五：空间压缩，因为当前位置只依赖下一行的数据
        int[] dp = new int[target + 1];
        dp[0] = 1;

        //从下往上，从左往右
        for (int index = nums.length - 1; index >= 0; index--) {
            for (int rest = target; rest >= 0; rest--) {
                int yes = rest - nums[index] >= 0 ? dp[rest - nums[index]] : 0;
                int no = dp[rest];
                dp[rest] = yes + no;
            }
        }
        return dp[target];
    }

}
