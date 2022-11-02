package leetcode;

import java.util.PriorityQueue;

public class Leetcode_502 {

    public int findMaximizedCapital(int k, int w, int[] profits, int[] capital) {
        //第一个堆启动资本小则优先级高；
        PriorityQueue<Project> lowCapitals = new PriorityQueue<>((o1, o2) -> o1.capital - o2.capital);
        //第二个堆为利润大优先级高
        PriorityQueue<Project> highProfits = new PriorityQueue<>((o1, o2) -> o2.profit - o1.profit);

        //给第一个建堆
        for (int i = 0; i < profits.length; i++) {
            lowCapitals.add(new Project(profits[i], capital[i]));
        }
        //剩余项目大于0
        while (k > 0) {
            while (!lowCapitals.isEmpty() && lowCapitals.peek().capital <= w) {
                //将花费最少且花费小于w的项目添加到highProfits堆中
                highProfits.add(lowCapitals.poll());
            }
            //highProfits为空，说明已经没有满足启动资金的项目了
            if (highProfits.isEmpty()) {
                return w;
            }
            //highProfits堆的堆顶肯定是利润最高的项目
            w += highProfits.poll().profit;
            //完成了一个项目减一
            k--;
        }
        return w;
    }

    static class Project {
        public int profit;
        public int capital;

        public Project(int profit, int capital) {
            this.profit = profit;
            this.capital = capital;
        }
    }
}
