package leetcode;

public class Leetcode_200 {
    //行和列长
    int rLen;
    int cLen;
    //并查集
    UnionFindSet ufSet;

    //使用并查集
    public int numIslands(char[][] grid) {
        rLen = grid.length;
        cLen = grid[0].length;
        ufSet = new UnionFindSet(grid);
        //开始遍历每个网格,只要找到一个网格为1,说明是一个新的岛屿
        for (int i = 0; i < grid.length; i++) {
            for (int j = 0; j < grid[i].length; j++) {
                if (grid[i][j] == '1') {
                    unionWithNeighbour(grid, i, j);
                }
            }
        }
        return ufSet.getSetSize();
    }

    /**
     * 需要将grid[i][j]格子的上下左右格子都加入到一个集合中
     *
     * @param i
     * @param j
     */
    private void unionWithNeighbour(char[][] grid, int i, int j) {
        //当前格如果不是陆地,直接返回
        if (grid[i][j] != '1') {
            return;
        }
        //四个方向:上下左右
        int[][] dire = {{1, 0}, {0, 1}};
        for (int k = 0; k < dire.length; k++) {
            //邻居结点的二维坐标
            int nbI = i + dire[k][0];
            int nbJ = j + dire[k][1];

            //查看二维坐标是否在范围内
            if (0 <= nbI && nbI < rLen && 0 <= nbJ && nbJ < cLen) {
                //查看邻居结点是否是陆地 以及 是否已经添加到当前陆地所在岛屿集合中去
                if (grid[nbI][nbJ] == '1') {
                    //满足上面条件,则添加邻居结点到岛屿中去
                    ufSet.union(i, j, nbI, nbJ);
                }
            }
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
         * 初始总集合数为所有为 1 的表格数量
         *
         * @param grid
         */
        public UnionFindSet(char[][] grid) {
            //总格子数
            int N = rLen * cLen;
            parents = new int[N];
            size = new int[N];


            for (int i = 0; i < grid.length; i++) {
                for (int j = 0; j < grid[i].length; j++) {
                    if (grid[i][j] == '1') {
                        setSize++;
                    }
                    int index = i * cLen + j;
                    parents[index] = index;
                    size[index] = 1;
                }
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
            int parent = parents[node];
            if (parent != node) {
                parent = find(parent);
                parents[node] = parent;
            }
            return parent;
        }

        /**
         * 将a结点和b结点所在结合合并
         */
        public void union(int i, int j, int ni, int nj) {
            int a = i * cLen + j;
            int b = ni * cLen + nj;
            //分别找到两个集合的代表结点
            int aHead = find(a);
            int bHead = find(b);


            //如果a和b已经在一个集合中了,就直接返回
            if (aHead == bHead) {
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
//            System.out.println(setSize + " : " + a + " " + b);
        }

        /**
         * @return 并查集中集合的个数
         */
        public int getSetSize() {
            return setSize;
        }
    }
}