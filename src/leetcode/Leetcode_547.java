package leetcode;

public class Leetcode_547 {
    public int findCircleNum(int[][] isConnected) {
        //表示有N个城市
        int cityNumber = isConnected.length;
        UnionFindSet unionFindSet = new UnionFindSet(cityNumber);
        //开始遍历每个城市的联通状态
        for (int i = 0; i < isConnected.length; i++) {
            for (int j = i + 1; j < isConnected[i].length; j++) {
                if (isConnected[i][j] == 1) {
                    unionFindSet.union(i, j);
                }
            }
        }
        return unionFindSet.getSetSize();
    }

    public static void main(String[] args) {
        Leetcode_547 solve = new Leetcode_547();
        int[][] data = {{1,1,1},{1,1,1},{1,1,1}};
        System.out.println(solve.findCircleNum(data));
    }
}

class UnionFindSet {
    /**
     * parents[i] = k : 表示i的父亲所在索引为k
     */
    private int[] parents;
    /**
     * size[i] = k : 如果i是代表结点，k才有意义，表示代表结点i所在集合的结点个数
     */
    private int[] size;
    /**
     * sets : 记录此并查集中有多少集合
     */
    private int setSize;

    /**
     * 初始化
     *
     * @param N
     */
    public UnionFindSet(int N) {
        parents = new int[N];
        size = new int[N];
        for (int i = 0; i < N; i++) {
            parents[i] = i;
            size[i] = 1;
        }
        setSize = N;
    }

    /**
     * 查找其最上面的祖先结点 即为代表结点
     * 并且将node到代表结点链路上的所有节点的父节点都设置为代表结点
     * 目的是了为了之后查询越来越快
     *
     * @param node
     * @return node所在集合的代表结点
     */
    public int find(int node) {
        int parent = parents[node];
        if (parent != node) {
            parent = find(parent);
            parents[node] = parent;
        }
        return parent;
    }

    /**
     * 将a结点和b结点所在结合合并
     *
     * @param a
     * @param b
     */
    public void union(int a, int b) {
        //分别找到两个集合的代表结点
        int aHead = find(a);
        int bHead = find(b);

        //如果a和b已经在一个集合中了,就直接返回
        if (aHead == bHead){
            return;
        }

        //获取两个集合的元素个数
        int aSetSize = size[a];
        int bSetSize = size[b];

        //分别指向元素多的集合和元素少的集合的代表结点
        int big = aSetSize >= bSetSize ? aHead : bHead;
        int small = big == aHead ? bHead : aHead;
        //让小集合的代表结点指向大集合的代表节点：目的是为了让之后find的迭代次数尽可能小
        parents[small] = big;
        //大集合的元素个数进行调整
        size[big] = aSetSize + bSetSize;
        //将集合的元素个数从sizeMap中移除，目的是为了让sizeMap的个数始终为集合个数
        setSize--;
    }

    /**
     * @param a
     * @param b
     * @return 判断两个结点是否在同一个集合中
     */
    public boolean isSameSet(int a, int b) {
        return find(a) == find(b);
    }

    /**
     * @return 并查集中集合的个数
     */
    public int getSetSize() {
        return setSize;
    }
}
