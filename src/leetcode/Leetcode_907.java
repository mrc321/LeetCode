package leetcode;

import java.util.Deque;
import java.util.LinkedList;

public class Leetcode_907 {

    public int sumSubarrayMins(int[] arr) {
        //使用单调栈求解，所有以栈顶元素为最小值的数组min之和
        int length = arr.length;
        long sum = 0;
        Deque<Integer> stack = new LinkedList<>();
        for (int right = 0; right < length; right++) {
            while (!stack.isEmpty() && arr[stack.peek()] >= arr[right]) {
                int curMinIndex = stack.pop();
                int left = stack.isEmpty() ? -1 : stack.peek();
                sum += (long) arr[curMinIndex] * (curMinIndex - left) * (right - curMinIndex);
            }
            stack.push(right);
        }

        while (!stack.isEmpty()){
            int curMinIndex = stack.pop();
            int left = stack.isEmpty() ? -1 : stack.peek();
            sum += (long) arr[curMinIndex] * (curMinIndex - left) * (length - curMinIndex);
        }
        return (int) (sum % (1000000007));
    }
}
