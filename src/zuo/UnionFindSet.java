package zuo;

/**
 * 并查集
 */
public class UnionFindSet {
    /**
     * parents[i] = k : 表示i的父亲所在索引为k
     */
    public int[] parents;
    /**
     * size[i] = k : 如果i是代表结点，k才有意义，表示代表结点i所在集合的结点个数
     */
    public int[] size;
    /**
     * sets : 记录此并查集中有多少集合
     */
    public int setSize;

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
            size[i] = i;
        }
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
        int parent;
        if (parents[node] != (parent = find(node))) {
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
