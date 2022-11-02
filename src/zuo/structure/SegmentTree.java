package zuo.structure;

import java.util.Arrays;

public class SegmentTree {
    /**
     * 下标从 1 开始的数组的长度
     */
    private int MAXN;
    /**
     * 新数组
     */
    private int[] arr;
    /**
     * 存放累加和的数组
     */
    private int[] sum;
    /**
     * 如果当前节点全落在范围内，则将累加任务全存放在lazy数组中
     */
    private int[] lazy;
    /**
     * 如果当前节点全落在范围内，则将更新任务全存放在lazy数组中
     */
    private int[] change;
    /**
     * 记录当前节点是否有更新任务
     */
    private boolean[] isUpdate;

    /**
     * 初始化
     * @param origin 原数组
     */
    public SegmentTree(int[] origin) {
        MAXN = origin.length + 1;
        arr = new int[MAXN];
        //以下四个数组的长度都是 4N
        sum = new int[MAXN << 2];
        lazy = new int[MAXN << 2];
        change = new int[MAXN << 2];
        isUpdate = new boolean[MAXN << 2];

        //由于下标从1开始，需要将原数组的数据转移到新数组，方便后续的操作
        for (int i = 1; i < MAXN; i++) {
            arr[i] = origin[i - 1];
        }
    }

    /**
     * 表示将范围[L,R]上所有的数都加上一个addValue
     * @param L 任务范围左侧边界
     * @param R 任务范围右侧边界
     * @param addValue 区间上要加上的值
     * @param l 当前节点的左侧范围 l
     * @param r 当前节点的右侧范围 r
     * @param index 当前节点在sum、lazy中的下标
     */
    public void add(int L, int R, int addValue, int l, int r, int index) {
        //首先查看当前节点[l,r]能够全部承接住任务
        if (L <= l && r <= R) {
            sum[index] += (r - l + 1) * addValue;
            lazy[index] += addValue;
            return;
        }
        //如果不能完全承接住任务，则要将任务下发到下一层
        int mid = l + ((r - l) >> 1);
        ;
        //将本层上一次的累加任务下发给下一层
        pushDown(index, mid - l + 1, r - mid);
        //正式将这次的任务下发,两个if语句就是判断任务[L,R]是否落在了左右子树的求和范围内
        if (mid >= L) {
            //左子树求的累加和范围为[l,mid]
            add(L, R, addValue, l, mid, index << 1);
        }
        if (mid + 1 <= R) {
            //右子树求的累加和范围为[mid+1,r]
            add(L, R, addValue, mid + 1, r, index << 1 | 1);
        }
        //更新本层的累加和信息
        pushUp(index);
    }

    /**
     * 将范围在[L,R]上的所有值都更新为newValue
     * @param L 任务范围左侧边界
     * @param R 任务范围右侧边界
     * @param newValue 更新后的新值
     * @param l 当前节点左侧边界
     * @param r 当前节点右侧边界
     * @param index 当前节点在sum、change、isUpdate数组中的下标
     */
    public void update(int L, int R, int newValue, int l, int r, int index) {
        //依旧判断[l,r]是否完全在任务[L,R]的范围内
        if (L <= l && r <= R) {
            sum[index] = newValue * (r - l + 1);
            change[index] = newValue;
            isUpdate[index] = true;
            //注意每次更新都需要将lazy置为0
            lazy[index] = 0;
            return;
        }
        int mid = l + ((r - l) >> 1);
        pushDown(index, mid - l + 1, r - mid);
        if (mid >= L) {
            update(L, R, newValue, l, mid, index << 1);
        }
        if (mid + 1 <= R) {
            update(L, R, newValue, mid + 1, r, index << 1 | 1);
        }
        pushUp(index);
    }

    /**
     * 查询[L,R]范围上的累加和
     * @param L 查询任务左侧范围
     * @param R 查询任务右侧范围
     * @param l 当前节点左侧范围
     * @param r 当前节点右侧范围
     * @param index 当前节点在sum数组中的下标
     * @return 范围[L,R]上的累加和
     */
    public int query(int L, int R, int l, int r, int index) {
        if (L <= l && r <= R) {
            return sum[index];
        }
        int mid = l + ((r - l) >> 1);
        ;
        pushDown(index, mid - l + 1, r - mid);
        int querySum = 0;
        if (mid >= L) {
            querySum += query(L, R, l, mid, index << 1);
        }
        if (mid + 1 <= R) {
            querySum += query(L, R, mid + 1, r, index << 1 | 1);
        }
        return querySum;
    }

    /**
     * 将[l,r]范围上的数建立一颗二叉树
     * @param l
     * @param r
     * @param index
     */
    private void buildTree(int l, int r, int index) {
        //l == r表示到达了叶节点
        if (l == r) {
            sum[index] = arr[l];
            return;
        }
        int mid = l + ((r - l) >> 1);
        //建左子树
        buildTree(l, mid, index << 1);
        //建右子树
        buildTree(mid + 1, r, index << 1 | 1);
        //左右两边都建好后，最后建本层
        pushUp(index);
    }

    private void pushUp(int index) {
        sum[index] = sum[index << 1] + sum[index << 1 | 1];
    }

    private void pushDown(int index, int lAmount, int rAmount) {
        //表示本层上次有更新任务还没有下发，开始下发
        if (isUpdate[index]) {
            sum[index << 1] = change[index] * lAmount;
            change[index << 1] = change[index];
            isUpdate[index << 1] = true;
            lazy[index << 1] = 0;

            sum[index << 1 | 1] = change[index] * rAmount;
            change[index << 1 | 1] = change[index];
            isUpdate[index << 1 | 1] = true;
            lazy[index << 1 | 1] = 0;

            isUpdate[index] = false;
        }

        if (lazy[index] != 0) {
            //表示此时 index节点还有上一次的任务没有下发，开始下发
            //主要就是左侧和右侧的累加和都加上，还有lazy任务也下发
            sum[index << 1] += lazy[index] * lAmount;
            lazy[index << 1] += lazy[index];

            sum[index << 1 | 1] += lazy[index] * rAmount;
            lazy[index << 1 | 1] += lazy[index];

            lazy[index] = 0;
        }
    }


    public static class Right {
        public int[] arr;

        public Right(int[] origin) {
            arr = new int[origin.length + 1];
            for (int i = 0; i < origin.length; i++) {
                arr[i + 1] = origin[i];
            }
        }

        public void update(int L, int R, int C) {
            for (int i = L; i <= R; i++) {
                arr[i] = C;
            }
        }

        public void add(int L, int R, int C) {
            for (int i = L; i <= R; i++) {
                arr[i] += C;
            }
        }

        public long query(int L, int R) {
            long ans = 0;
            for (int i = L; i <= R; i++) {
                ans += arr[i];
            }
            return ans;
        }

    }

    public static int[] genarateRandomArray(int len, int max) {
        int size = (int) (Math.random() * len) + 1;
        int[] origin = new int[size];
        for (int i = 0; i < size; i++) {
            origin[i] = (int) (Math.random() * max) - (int) (Math.random() * max);
        }
        return origin;
    }

    public static boolean test() {
        int len = 100;
        int max = 1000;
        int testTimes = 5000;
        int addOrUpdateTimes = 1000;
        int queryTimes = 500;
        for (int i = 0; i < testTimes; i++) {
            int[] origin = genarateRandomArray(len, max);
            SegmentTree seg = new SegmentTree(origin);
            int S = 1;
            int N = origin.length;
            int root = 1;
            seg.buildTree(S, N, root);
            Right rig = new Right(origin);
            for (int j = 0; j < addOrUpdateTimes; j++) {
                int num1 = (int) (Math.random() * N) + 1;
                int num2 = (int) (Math.random() * N) + 1;
                int L = Math.min(num1, num2);
                int R = Math.max(num1, num2);
                int C = (int) (Math.random() * max) - (int) (Math.random() * max);
                if (Math.random() < 0.5) {
                    seg.add(L, R, C, S, N, root);
                    rig.add(L, R, C);
                } else {
                    seg.update(L, R, C, S, N, root);
                    rig.update(L, R, C);
                }
            }
            for (int k = 0; k < queryTimes; k++) {
                int num1 = (int) (Math.random() * N) + 1;
                int num2 = (int) (Math.random() * N) + 1;
                int L = Math.min(num1, num2);
                int R = Math.max(num1, num2);
                long ans1 = seg.query(L, R, S, N, root);
                long ans2 = rig.query(L, R);
                if (ans1 != ans2) {
                    System.out.println(Arrays.toString(origin));
                    System.out.println(ans1);
                    System.out.println(ans2);
                    return false;
                }
            }
        }
        return true;
    }

    public static void main(String[] args) {
        int[] origin = {2, 1, 1, 2, 3, 4, 5};
        SegmentTree seg = new SegmentTree(origin);
        int S = 1; // 整个区间的开始位置，规定从1开始，不从0开始 -> 固定
        int N = origin.length; // 整个区间的结束位置，规定能到N，不是N-1 -> 固定
        int root = 1; // 整棵树的头节点位置，规定是1，不是0 -> 固定
        int L = 2; // 操作区间的开始位置 -> 可变
        int R = 5; // 操作区间的结束位置 -> 可变
        int C = 4; // 要加的数字或者要更新的数字 -> 可变
        // 区间生成，必须在[S,N]整个范围上build
        seg.buildTree(S, N, root);
        // 区间修改，可以改变L、R和C的值，其他值不可改变
        seg.add(L, R, C, S, N, root);
        // 区间更新，可以改变L、R和C的值，其他值不可改变
        seg.update(L, R, C, S, N, root);
        // 区间查询，可以改变L和R的值，其他值不可改变
        long sum = seg.query(L, R, S, N, root);
        System.out.println(sum);

        System.out.println("对数器测试开始...");
        System.out.println("测试结果 : " + (test() ? "通过" : "未通过"));

    }

}
