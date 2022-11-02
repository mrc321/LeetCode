package zuo;


import java.util.ArrayList;
import java.util.List;


class Employee {
    public int happy; // 这名员工可以带来的快乐值
    public List<Employee> subordinates; // 这名员工有哪些直接下级

    public Employee(int h) {
        happy = h;
        subordinates = new ArrayList<>();
    }
}

public class party {


    public static int maxHappy(Employee boss) {
        Info info = process(boss);
        return Math.max(info.yes, info.no);
    }

    private static Info process(Employee boss) {
        if (boss == null) {
            return new Info(0, 0);
        }
        Info curInfo = new Info(boss);
        List<Employee> subordinates = boss.subordinates;

        for (Employee subordinate : subordinates) {
            Info subordinateInfo = process(subordinate);
            curInfo.yes += subordinateInfo.no;
            curInfo.no += Math.max(subordinateInfo.yes, subordinateInfo.no);
        }

        return curInfo;
    }

    static class Info {
        public int yes;
        public int no;

        public Info(int no, int yes) {
            this.yes = yes;
            this.no = no;
        }

        public Info(Employee boss) {
            this.yes = boss.happy;
        }
    }


    public static int maxHappy1(Employee boss) {
        if (boss == null) {
            return 0;
        }
        return process1(boss, false);
    }

    // 当前来到的节点叫cur，
    // up表示cur的上级是否来，
    // 该函数含义：
    // 如果up为true，表示在cur上级已经确定来，的情况下，cur整棵树能够提供最大的快乐值是多少？
    // 如果up为false，表示在cur上级已经确定不来，的情况下，cur整棵树能够提供最大的快乐值是多少？
    public static int process1(Employee cur, boolean up) {
        if (up) { // 如果cur的上级来的话，cur没得选，只能不来
            int ans = 0;
            for (Employee next : cur.subordinates) {
                ans += process1(next, false);
            }
            return ans;
        } else { // 如果cur的上级不来的话，cur可以选，可以来也可以不来
            int p1 = cur.happy;
            int p2 = 0;
            for (Employee next : cur.subordinates) {
                p1 += process1(next, true);
                p2 += process1(next, false);
            }
            return Math.max(p1, p2);
        }
    }

    public static int maxHappy2(Employee head) {
        Info allInfo = process2(head);
        return Math.max(allInfo.no, allInfo.yes);
    }

    public static Info process2(Employee x) {
        if (x == null) {
            return new Info(0, 0);
        }
        int no = 0;
        int yes = x.happy;
        for (Employee next : x.subordinates) {
            Info nextInfo = process2(next);
            no += Math.max(nextInfo.no, nextInfo.yes);
            yes += nextInfo.no;

        }
        return new Info(no, yes);
    }

    // for test
    public static Employee genarateBoss(int maxLevel, int maxNexts, int maxHappy) {
        if (Math.random() < 0.02) {
            return null;
        }
        Employee boss = new Employee((int) (Math.random() * (maxHappy + 1)));
        genarateNexts(boss, 1, maxLevel, maxNexts, maxHappy);
        return boss;
    }

    // for test
    public static void genarateNexts(Employee e, int level, int maxLevel, int maxNexts, int maxHappy) {
        if (level > maxLevel) {
            return;
        }
        int nextsSize = (int) (Math.random() * (maxNexts + 1));
        for (int i = 0; i < nextsSize; i++) {
            Employee next = new Employee((int) (Math.random() * (maxHappy + 1)));
            e.subordinates.add(next);
            genarateNexts(next, level + 1, maxLevel, maxNexts, maxHappy);
        }
    }

    public static void main(String[] args) {
        int maxLevel = 4;
        int maxNexts = 7;
        int maxHappy = 100;
        int testTimes = 100000;
        for (int i = 0; i < testTimes; i++) {
            Employee boss = genarateBoss(maxLevel, maxNexts, maxHappy);
            if (maxHappy1(boss) != maxHappy(boss)) {
                System.out.println("Oops!");
            }
        }
        System.out.println("finish!");
    }
}
