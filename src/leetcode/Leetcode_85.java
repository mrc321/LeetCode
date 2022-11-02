package leetcode;

import java.util.Deque;
import java.util.LinkedList;

public class Leetcode_85 {
    public int maximalRectangle(char[][] matrix) {
        //行数
        int N = matrix.length;
        //列数
        int M = matrix[0].length;
        int[] line = new int[M];
        int maxArea = 0;
        for (int row = 0; row < N; row++) {
            for (int col = 0; col < M; col++) {
                line[col] = matrix[row][col] == '0' ? 0 : line[col] + 1;
            }
            maxArea = Math.max(maxArea, largestRectangleArea(line));
        }
        return maxArea;
    }

    public int largestRectangleArea(int[] heights) {
        //使用的就是单调栈确定以当前栈顶元素为高的矩阵的左右边界
        Deque<Integer> stack = new LinkedList<>();
        int length = heights.length;
        int maxArea = -1;
        for (int i = 0; i < length; i++) {
            while (!stack.isEmpty() && heights[stack.peek()] >= heights[i]) {
                //求以topHeight为高的矩阵的面积 [left,i - 1]
                int topHeight = heights[stack.pop()];
                //左边界需要加一
                int left = stack.isEmpty() ? 0 : stack.peek() + 1;
                //右边界就是当前i
                maxArea = Math.max(maxArea, (i - left) * topHeight);
            }
            stack.push(i);
        }

        while (!stack.isEmpty()) {
            int topHeight = heights[stack.pop()];
            int left = stack.isEmpty() ? 0 : stack.peek() + 1;
            maxArea = Math.max(maxArea, (length - left) * topHeight);
        }
        return maxArea;
    }
}
