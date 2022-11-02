package leetcode;

import java.util.*;

public class Leetcode_699 {
    public static void main(String[] args) {
        int[][] position = {{2, 1}, {2, 9}, {1, 8}};
        System.out.println(new Leetcode_699().fallingSquares(position));
    }


    public List<Integer> fallingSquares(int[][] positions) {
        //坐标压缩
        Map<Integer, Integer> newIndex = tranNewIndex(positions);
        int N = newIndex.size();
        FallSquares fallSquares = new FallSquares(N);

        List<Integer> maxHightList = new LinkedList<>();
        //遍历每个方块
        for (int[] position : positions) {
            //获得每个方块的左右区间范围
            int L = newIndex.get(position[0]);
            int R = newIndex.get(position[0] + position[1] - 1);
            //此方块落下去后 区间[L,R] 更新之后的值
            int newValue = position[1] + fallSquares.query(L, R, 1, N, 1);
            //开始更新
            fallSquares.update(L, R, newValue, 1, N, 1);
            //将结果添加集合中去
            maxHightList.add(fallSquares.query(1, N, 1, N, 1));
        }
        return maxHightList;
    }

    private Map<Integer, Integer> tranNewIndex(int[][] positions) {
        Set<Integer> treeSet = new TreeSet<>();
        for (int[] position : positions) {
            treeSet.add(position[0]);
            treeSet.add(position[0] + position[1] - 1);
        }
        Map<Integer, Integer> newIndex = new HashMap<>();
        int count = 0;
        for (Integer xIndex : treeSet) {
            newIndex.put(xIndex, ++count);
        }
        return newIndex;
    }


    public class FallSquares {
        /**
         * 下标从 1 开始的数组的长度
         */
        private int MAXN;

        /**
         * 存放最大高度的数组
         */
        private int[] maxHight;
        /**
         * 如果当前节点全落在范围内，则将更新任务全存放在lazy数组中
         */
        private int[] change;
        /**
         * 记录当前节点是否有更新任务
         */
        private boolean[] isUpdate;

        public FallSquares(int N) {
            MAXN = N;
            //以下四个数组的长度都是 4N
            maxHight = new int[MAXN << 2];
            change = new int[MAXN << 2];
            isUpdate = new boolean[MAXN << 2];
        }


        /**
         * 将范围在[L,R]上的所有值都更新为newValue
         *
         * @param L        任务范围左侧边界
         * @param R        任务范围右侧边界
         * @param newValue 更新后的新值
         * @param l        当前节点左侧边界
         * @param r        当前节点右侧边界
         * @param index    当前节点在maxHight、change、isUpdate数组中的下标
         */
        public void update(int L, int R, int newValue, int l, int r, int index) {
            //依旧判断[l,r]是否完全在任务[L,R]的范围内
            if (L <= l && r <= R) {
                maxHight[index] = newValue;
                change[index] = newValue;
                isUpdate[index] = true;
                return;
            }
            int mid = l + ((r - l) >> 1);
            pushDown(index);
            if (mid >= L) {
                update(L, R, newValue, l, mid, index << 1);
            }
            if (mid + 1 <= R) {
                update(L, R, newValue, mid + 1, r, index << 1 | 1);
            }
            pushUp(index);
        }

        public int query(int L, int R, int l, int r, int index) {
            if (L <= l && r <= R) {
                return maxHight[index];
            }
            int mid = l + ((r - l) >> 1);
            pushDown(index);
            int querymaxHight = 0;

            if (mid >= L) {
                querymaxHight = Math.max(querymaxHight, query(L, R, l, mid, index << 1));
            }
            if (mid + 1 <= R) {
                querymaxHight = Math.max(querymaxHight, query(L, R, mid + 1, r, index << 1 | 1));
            }
            return querymaxHight;
        }

        private void pushUp(int index) {
            maxHight[index] = Math.max(maxHight[index << 1], maxHight[index << 1 | 1]);
        }

        private void pushDown(int index) {
            //表示本层上次有更新任务还没有下发，开始下发
            if (isUpdate[index]) {
                maxHight[index << 1] = change[index];
                change[index << 1] = change[index];
                isUpdate[index << 1] = true;

                maxHight[index << 1 | 1] = change[index];
                change[index << 1 | 1] = change[index];
                isUpdate[index << 1 | 1] = true;

                isUpdate[index] = false;
            }
        }
    }
}
