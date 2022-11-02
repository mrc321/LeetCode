package leetcode;

import java.util.Arrays;
import java.util.Deque;
import java.util.LinkedList;

public class Leetcode_239 {
    public static void main(String[] args) {
        int[] nums = {1,3,-1,-3,5,3,6,7};
        System.out.println(Arrays.toString(new Leetcode_239().maxSlidingWindow(nums, 3)));
    }
    public int[] maxSlidingWindow(int[] nums, int k) {
        int N = nums.length;
        int[] result = new int[N - k + 1];
        int L = 0;
        //需要设置一个双端队列：始终存放的是当前区间内从大到小的数据的下标
        Deque<Integer> maxQue = new LinkedList<>();
        for (int R = 0; R < N; R++) {
            //往队列末尾添加元素时，需要始终保证队列末尾的元素大于新添加的元素，否则将末尾元素一直出队，直到满足要求
            while (!maxQue.isEmpty() && nums[maxQue.peekLast()] <= nums[R]) {
                maxQue.pollLast();
            }
            //将新元素的下标加入到队列中
            maxQue.offerLast(R);
            //必须保证此时区间内的数满 K 个数
            if (R + 1 < k) {
                continue;
            }
            //获取区间内的最大元素
            result[L] = nums[maxQue.peekFirst()];
            //L向右移动，并判断队列队首元素是否过期
            if (maxQue.peekFirst() == L) {
                maxQue.pollFirst();
            }
            L++;
        }
        return result;
    }
}
