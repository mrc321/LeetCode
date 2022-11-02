package zuo.class2;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
//import zuo.structure.HeapGreater;

public class WhosYourDaddyProblem {

    public static class Customer {
        public int id;
        public int buy;
        public int enterTime;

        public Customer(int v, int b, int o) {
            id = v;
            buy = b;
            enterTime = 0;
        }

        @Override
        public String toString() {
            return "Customer{" +
                    "id=" + id +
                    ", buy=" + buy +
                    ", enterTime=" + enterTime +
                    '}';
        }
    }

    //候选区排序规则
    public static class CandidateComparator implements Comparator<Customer> {
        @Override
        public int compare(Customer o1, Customer o2) {
            //购买数相同时，进入时间早的排前面
            //购买数不同时，购买数多的在前面
            //方便将排在最前面的移入到中奖区
            return o1.buy != o2.buy ? (o2.buy - o1.buy) : (o1.enterTime - o2.enterTime);
        }

    }

    //中奖区排序规则
    public static class DaddyComparator implements Comparator<Customer> {
        @Override
        public int compare(Customer o1, Customer o2) {
            //购买数相同时，进入时间早的排前面
            //购买数不同时，购买数少的在前面
            //随时准备将排在最前面的移出
            return o1.buy != o2.buy ? (o1.buy - o2.buy) : (o1.enterTime - o2.enterTime);
        }

    }

    public static class WhosYourDaddy {
        //存放所有购买数大于0的客户
        private HashMap<Integer, Customer> customers;
        //候选区堆
        private HeapGreater<Customer> candHeap;
        //中奖区堆
        private HeapGreater<Customer> daddyHeap;
        //中奖区中元素个数上限
        private final int daddyLimit;

        public WhosYourDaddy(int limit) {
            customers = new HashMap<Integer, Customer>();
            candHeap = new HeapGreater<>(new CandidateComparator());
            daddyHeap = new HeapGreater<>(new DaddyComparator());
            daddyLimit = limit;
        }

        // 当前处理i号事件，arr[i] -> id,  buyOrRefund
        public void operate(int time, int id, boolean buyOrRefund) {
            //如果客户是退货且查询不到该客户之前的购买信息，则直接忽略该客户
            //所以该操作直接将：【用户购买数为0并且又退货了】这种情况直接排除
            //还剩下下面三种情况：
            // 1、用户之前购买数是0，此时买货
            // 2、用户之前购买数>0，此时买货
            // 3、用户之前购买数>0, 此时退货
            if (!buyOrRefund && !customers.containsKey(id)) {
                return;
            }
            //如果查询不到该用户，说明该用户是新用户，且行为是买货，属于情况1
            if (!customers.containsKey(id)) {
                //将该用户添加到map中
                customers.put(id, new Customer(id, 0, 0));
            }
            // 买、卖
            Customer c = customers.get(id);
            if (buyOrRefund) {
                c.buy++;
            } else {
                c.buy--;
            }
            //如果此时用户的购买数为0了，直接从用户表中剔除
            if (c.buy == 0) {
                customers.remove(id);
            }
            // 如果候选区和中奖区中都不存在该用户，则将其添加到对应的区中
            if (!candHeap.contains(c) && !daddyHeap.contains(c)) {
                //此时中奖区已添加用户的数量小于K，则直接添加用户到中奖区中
                if (daddyHeap.size() < daddyLimit) {
                    //设置添加的时间点为当前时间
                    c.enterTime = time;
                    daddyHeap.push(c);
                } else {
                    c.enterTime = time;
                    candHeap.push(c);
                }
                //如果该元素已经存在在候选区，做清零工作
            } else if (candHeap.contains(c)) {
                if (c.buy == 0) {
                    candHeap.remove(c);
                } else {
                    candHeap.resign(c);
                }
            } else {
                if (c.buy == 0) {
                    daddyHeap.remove(c);
                } else {
                    daddyHeap.resign(c);
                }
            }
            //开始尝试让候选区元素往中奖区中移动
            daddyMove(time);
        }

        public List<Integer> getDaddies() {
            List<Customer> customers = daddyHeap.getAllElements();
            List<Integer> ans = new ArrayList<>();
            for (Customer c : customers) {
                ans.add(c.id);
            }
            return ans;
        }

        private void daddyMove(int time) {
            //候选区为空的话直接返回就行，不用移动
            if (candHeap.isEmpty()) {
                return;
            }
            if (daddyHeap.size() < daddyLimit) {
                Customer p = candHeap.pop();
                p.enterTime = time;
                daddyHeap.push(p);
            } else {
                if (candHeap.peek().buy > daddyHeap.peek().buy) {
                    Customer oldDaddy = daddyHeap.pop();
                    Customer newDaddy = candHeap.pop();
                    oldDaddy.enterTime = time;
                    newDaddy.enterTime = time;
                    daddyHeap.push(newDaddy);
                    candHeap.push(oldDaddy);
                }
            }
        }

    }

    public static List<List<Integer>> topK(int[] arr, boolean[] op, int k) {
        List<List<Integer>> ans = new ArrayList<>();
        WhosYourDaddy whoDaddies = new WhosYourDaddy(k);
        for (int i = 0; i < arr.length; i++) {
            whoDaddies.operate(i, arr[i], op[i]);
            ans.add(whoDaddies.getDaddies());
        }
        return ans;
    }

    // 干完所有的事，模拟，不优化
    public static List<List<Integer>> compare(int[] arr, boolean[] op, int k) {
        //存放所有购买数大于0的客户
        HashMap<Integer, Customer> map = new HashMap<>();
        //候选区列表
        ArrayList<Customer> cands = new ArrayList<>();
        //中奖区列表
        ArrayList<Customer> daddy = new ArrayList<>();
        //存放每个时间点的中奖区
        List<List<Integer>> ans = new ArrayList<>();
        //遍历每个事件
        for (int i = 0; i < arr.length; i++) {
            //获取客户id
            int id = arr[i];
            //获取客户的操作
            boolean buyOrRefund = op[i];
            //如果客户是退货且查询不到该客户之前的购买信息，则直接忽略该客户
            //所以该操作直接将：【用户购买数为0并且又退货了】这种情况直接排除
            //还剩下下面三种情况：
            // 1、用户之前购买数是0，此时买货
            // 2、用户之前购买数>0，此时买货
            // 3、用户之前购买数>0, 此时退货
            if (!buyOrRefund && !map.containsKey(id)) {
                //直接将中奖区添加到ans中
                ans.add(getCurAns(daddy));
                continue;
            }
            //如果查询不到该用户，说明该用户是新用户，且行为是买货，属于情况1
            if (!map.containsKey(id)) {
                //将该用户添加到map中
                map.put(id, new Customer(id, 0, 0));
            }
            // 买、卖
            Customer c = map.get(id);
            if (buyOrRefund) {
                c.buy++;
            } else {
                c.buy--;
            }
            //如果此时用户的购买数为0了，直接从用户表中剔除
            if (c.buy == 0) {
                map.remove(id);
            }
            // 如果候选区和中奖区中都不存在该用户，则将其添加到对应的区中
            if (!cands.contains(c) && !daddy.contains(c)) {
                //此时中奖区已添加用户的数量小于K，则直接添加用户到中奖区中
                if (daddy.size() < k) {
                    //设置添加的时间点为当前时间
                    c.enterTime = i;
                    daddy.add(c);
                } else {
                    c.enterTime = i;
                    cands.add(c);
                }
            }
            //下面两个方法就是清除出所有购买数已经为0的用户
            cleanZeroBuy(cands);
            cleanZeroBuy(daddy);
            cands.sort(new CandidateComparator());
            daddy.sort(new DaddyComparator());
            move(cands, daddy, k, i);
            ans.add(getCurAns(daddy));
        }
        return ans;
    }

    public static void move(ArrayList<Customer> cands, ArrayList<Customer> daddy, int k, int time) {
        //候选区如果为空，不用发生移动，直接返回
        if (cands.isEmpty()) {
            return;
        }
        // 候选区不为空
        //如果中奖区当前已有元素个数小于k，则直接将候选区的第一个元素添加到中奖区中
        if (daddy.size() < k) {
            Customer c = cands.get(0);
            c.enterTime = time;
            daddy.add(c);
            cands.remove(0);
        } else { // 等奖区满了，候选区有东西，就需要进行比较，才能进行交换
            if (cands.get(0).buy > daddy.get(0).buy) {
                Customer oldDaddy = daddy.get(0);
                daddy.remove(0);
                Customer newDaddy = cands.get(0);
                cands.remove(0);
                newDaddy.enterTime = time;
                oldDaddy.enterTime = time;
                daddy.add(newDaddy);
                cands.add(oldDaddy);
            }
        }
    }

    public static void cleanZeroBuy(ArrayList<Customer> arr) {
        List<Customer> noZero = new ArrayList<Customer>();
        for (Customer c : arr) {
            if (c.buy != 0) {
                noZero.add(c);
            }
        }
        arr.clear();
        for (Customer c : noZero) {
            arr.add(c);
        }
    }

    public static List<Integer> getCurAns(ArrayList<Customer> daddy) {
        List<Integer> ans = new ArrayList<>();
        for (Customer c : daddy) {
            ans.add(c.id);
        }
        return ans;
    }

    // 为了测试
    public static class Data {
        public int[] arr;
        public boolean[] op;

        public Data(int[] a, boolean[] o) {
            arr = a;
            op = o;
        }
    }

    // 为了测试
    public static Data randomData(int maxValue, int maxLen) {
        int len = (int) (Math.random() * maxLen) + 1;
        int[] arr = new int[len];
        boolean[] op = new boolean[len];
        for (int i = 0; i < len; i++) {
            arr[i] = (int) (Math.random() * maxValue);
            op[i] = Math.random() < 0.5 ? true : false;
        }
        return new Data(arr, op);
    }

    // 为了测试
    public static boolean sameAnswer(List<List<Integer>> ans1, List<List<Integer>> ans2) {
        if (ans1.size() != ans2.size()) {
            return false;
        }
        for (int i = 0; i < ans1.size(); i++) {
            List<Integer> cur1 = ans1.get(i);
            List<Integer> cur2 = ans2.get(i);
            if (cur1.size() != cur2.size()) {
                return false;
            }
            cur1.sort((a, b) -> a - b);
            cur2.sort((a, b) -> a - b);
            for (int j = 0; j < cur1.size(); j++) {
                if (!cur1.get(j).equals(cur2.get(j))) {
                    return false;
                }
            }
        }
        return true;
    }

    public static void main(String[] args) {
        int maxValue = 10;
        int maxLen = 10;
        int maxK = 5;
        int testTimes = 100000000;
        System.out.println("测试开始");
        for (int i = 0; i < testTimes; i++) {
            Data testData = randomData(maxValue, maxLen);
            int k = (int) (Math.random() * maxK) + 1;
            int[] arr = testData.arr;
            boolean[] op = testData.op;
            List<List<Integer>> ans1 = topK(arr, op, k);
            List<List<Integer>> ans2 = compare(arr, op, k);
            if (!sameAnswer(ans1, ans2)) {
                for (int j = 0; j < arr.length; j++) {
                    System.out.println(arr[j] + " , " + op[j]);
                }
                System.out.println(k);
                System.out.println(ans1);
                System.out.println(ans2);
                System.out.println("出错了！");
                break;
            }
        }
        System.out.println("测试结束");
    }

}
