package zuo.structure;

public class IndexTree {
    private int[] tree;
    private int N;

    public IndexTree(int size) {
        N = size;
        tree = new int[N + 1];
    }

    /**
     * 在index位置上加个value
     *
     * @param index
     * @param value
     */
    public void add(int index, int value) {
        while (index <= N) {
            tree[index] += value;
            index += index & -index;
        }
    }

    /**
     * 求出[1,index]上的累加和
     *
     * @param index
     * @return
     */
    public int sum(int index) {
        int sum = 0;
        while (index > 0) {
            sum += tree[index];
            index -= index & -index;
        }
        return sum;
    }


    public static class Right {
        private int[] nums;
        private int N;

        public Right(int size) {
            N = size + 1;
            nums = new int[N + 1];
        }

        public int sum(int index) {
            int ret = 0;
            for (int i = 1; i <= index; i++) {
                ret += nums[i];
            }
            return ret;
        }

        public void add(int index, int d) {
            nums[index] += d;
        }

    }

    public static void main(String[] args) {
        int N = 100;
        int V = 100;
        int testTime = 2000000;
        IndexTree tree = new IndexTree(N);
        Right test = new Right(N);
        System.out.println("test begin");
        for (int i = 0; i < testTime; i++) {
            int index = (int) (Math.random() * N) + 1;
            if (Math.random() <= 0.5) {
                int add = (int) (Math.random() * V);
                tree.add(index, add);
                test.add(index, add);
            } else {
                if (tree.sum(index) != test.sum(index)) {
                    System.out.println("Oops!");
                }
            }
        }
        System.out.println("test finish");
    }
}
