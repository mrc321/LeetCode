package leetcode;

import java.util.Deque;
import java.util.LinkedList;

public class Leetcode_134 {
    public static void main(String[] args) {
        int[] gas = {4, 5, 2, 6, 5, 3};
        int[] cost = {3, 2, 7, 3, 2, 9};
        System.out.println(new Leetcode_134().canCompleteCircuit(gas, cost));
    }

    public int canCompleteCircuit(int[] gas, int[] cost) {
        int N = gas.length;
        //sumRemainOil[i]表示是从0号加油站出发到i % N 号加油站出发后剩余的油量
        int[] sumRemainOil = new int[N * 2];
        sumRemainOil[0] += gas[0] - cost[0];
        for (int i = 1; i < N * 2; i++) {
            //gas[i % N] - cost[i % N]表示从i % N号加油站出发到达下一个加油站时，所剩余的油量
            sumRemainOil[i] += sumRemainOil[i - 1] + gas[i % N] - cost[i % N];
        }
        //窗口大小为N的滑动窗口，始终保持一个窗口（一圈）内的一个最小值，因此维护一个单调递增队列
        Deque<Integer> minQue = new LinkedList<>();
        int R = 0;
        int last = 0;
        //遍历N个加油站为起始加油站
        for (int L = 0; L < N; L++) {
            //维护一个窗口内的最小数据队列
            while (R < L + N) {
                while (!minQue.isEmpty() && sumRemainOil[minQue.peekLast()] >= sumRemainOil[R]) {
                    minQue.pollLast();
                }
                minQue.offerLast(R);
                R++;
            }
            //取出队列中的最小值，与前一个值相间，如果仍然大于等于0，则表示从L号加油站触发，全程汽车的油量都不会小于0，因此可以直接返回
            if (sumRemainOil[minQue.peekFirst()] - last >= 0) {
                return L;
            }
            //清除过期下标
            if (L == minQue.peekFirst()) {
                minQue.pollFirst();
            }
            last = sumRemainOil[L];
        }
        return -1;
    }
}
