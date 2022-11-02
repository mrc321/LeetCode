package leetcode;

import java.util.Deque;
import java.util.LinkedList;

public class Leetcode_1504 {
    public static void main(String[] args) {
        int[] q = {0, 1, 1, 0};
        int[] w = {0, 2, 2, 1};
        int[] e = {1, 3, 3, 0};
        System.out.println(getSum(q));
        System.out.println(getSum(w));
        System.out.println(getSum(e));
    }

    public int numSubmat(int[][] mat) {
        int N = mat.length;
        int M = mat[0].length;
        int[] line = new int[M];
        int sum = 0;
        for (int row = 0; row < N; row++) {
            for (int col = 0; col < M; col++) {
                line[col] = mat[row][col] == 0 ? 0 : line[col] + 1;
            }
            sum += getSum(line);
        }
        return sum;
    }

    //获取以每一层为底的子矩阵数量
    private static int getSum(int[] heights) {
        int length = heights.length;
        Deque<Integer> stack = new LinkedList<>();
        int sum = 0;
        for (int right = 0; right < length; right++) {
            while (!stack.isEmpty()) {
                //栈顶大于新加入元素
                if (heights[stack.peek()] > heights[right]) {
                    int height = heights[stack.pop()];
                    int left = stack.isEmpty() ? 0 : stack.peek() + 1;
                    int L = right - left;
                    //差额
                    int num = height - (stack.isEmpty() ? heights[right] : Math.max(heights[stack.peek()], heights[right]));
                    sum += num * ((L * (L + 1) / 2));
                } else if (heights[stack.peek()] == heights[right]) {
                    //栈顶等于新加入的元素时，直接弹出栈顶
                    stack.pop();
                } else {
                    break;
                }
            }
            stack.push(right);
        }
        while (!stack.isEmpty()) {
            int height = heights[stack.pop()];
            int left = stack.isEmpty() ? 0 : stack.peek() + 1;
            int L = length - left;
            int num = height - (stack.isEmpty() ? 0 : heights[stack.peek()]);
            sum += num * ((L * (L + 1) / 2));
        }
        return sum;
    }
}
