package leetcode;

import java.util.ArrayList;
import java.util.List;

public class Leetcode_305 {
    //行和列长
    int rLen;
    int cLen;
    //并查集
    UnionFindSet ufSet;

    public List<Integer> numIslands2(int m, int n, int[][] positions) {
        rLen = m;
        cLen = n;
        ufSet = new UnionFindSet(m * n);


        ArrayList<Integer> result = new ArrayList<>();

        for (int[] position : positions) {
            result.add(ufSet.unionWithNeighbour(position[0],position[1]));
        }
        return result;
    }

    class UnionFindSet {
        private int[] parents;
        private int[] size;
        private int setSize;

        public UnionFindSet(int N) {
            //总格子数
            parents = new int[N];
            size = new int[N];
        }

        public int find(int node) {
            int parent = parents[node];
            if (parent != node) {
                parent = find(parent);
                parents[node] = parent;
            }
            return parent;
        }

        public void union(int i, int j, int ni, int nj) {
//            if (i < 0 || j < 0 || ni < 0 || nj < 0 || i == rLen || ni == rLen || j == cLen || nj == cLen || size[index(ni, nj)] == 0) {
//                return;
//            }
            if (ni < 0 || nj < 0 || ni == rLen || nj == cLen || size[index(ni, nj)] == 0) {
                return;
            }
            int a = index(i, j);
            int b = index(ni, nj);
            int aHead = find(a);
            int bHead = find(b);

            if (!(aHead == bHead)) {
                int aSetSize = size[a];
                int bSetSize = size[b];

                int big = aSetSize >= bSetSize ? aHead : bHead;
                int small = big == aHead ? bHead : aHead;
                parents[small] = big;
                size[big] = aSetSize + bSetSize;
                setSize--;
            }
        }

        private int index(int i, int j) {
            return i * cLen + j;
        }

        public int unionWithNeighbour(int i, int j) {
            int curIndex = index(i, j);
            if (size[curIndex] == 0) {
                setSize++;
                size[curIndex] = 1;
                parents[curIndex] = curIndex;
                union(i, j, i - 1, j);
                union(i, j, i + 1, j);
                union(i, j, i, j - 1);
                union(i, j, i, j + 1);
            }
            return setSize;
        }
    }
}