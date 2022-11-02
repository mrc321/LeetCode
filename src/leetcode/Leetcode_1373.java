package leetcode;

public class Leetcode_1373 {
    //只用一个全局变量存放结果
    static int  ans = 0;

    public static int maxSumBST(TreeNode root) {
        process(root);
        return ans;
    }

    private static Info process(TreeNode root) {
        //如果是空节点，返回一个默认值，其中max为最小，min为最大
        if (root == null) {
            return new Info(true, Integer.MIN_VALUE, Integer.MAX_VALUE, 0);
        }
        Info leftInfo = process(root.left);
        Info rightInfo = process(root.right);
        //给当前节点的属性全部设置为默认值
        boolean isBST = false;
        int max = 0;
        int min = 0;
        int sum = 0;
        //说明当前子树为二叉搜索树
        if (leftInfo.isBST && rightInfo.isBST && root.val > leftInfo.max && root.val < rightInfo.min) {
            isBST = true;
            max = Math.max(root.val, rightInfo.max);
            min = Math.min(root.val, leftInfo.min);
            //求出当前二叉搜索子树的节点和
            sum = leftInfo.sum + rightInfo.sum + root.val;
            //与之前的答案进行对比，取最大值
            ans = Math.max(sum, ans);
        }
        //对于如果当前不是二叉搜索树，则直接返回默认值，因为之后都用不上了
        return new Info(isBST, max, min, sum);
    }

    private static class Info {
        public boolean isBST;
        public int max;
        public int min;
        public int sum;

        public Info(boolean isBST, int max, int min, int maxSum) {
            this.isBST = isBST;
            this.max = max;
            this.min = min;
            this.sum = maxSum;
        }
    }

    public static void main(String[] args) {
        TreeNode root = new TreeNode(1);
        root.left = new TreeNode(-5);
        root.left.right = new TreeNode(-3);
        root.right = new TreeNode(4);
        root.right.right = new TreeNode(10);
        System.out.println(maxSumBST(root));
    }
}

class Solution {
    static int ans = 0;

    public static int maxSumBST(TreeNode root) {
        process(root);
        return ans;
    }

    private static int[] process(TreeNode root) {
        if (root == null) {
            return new int[]{1, Integer.MIN_VALUE, Integer.MAX_VALUE, 0};
        }
        int[] leftInfo = process(root.left);
        int[] rightInfo = process(root.right);

        int[] info = new int[4];
        //说明当前树为二叉搜索树
        if (leftInfo[0] == 1 && rightInfo[0] == 1 && root.val > leftInfo[1] && root.val < rightInfo[2]) {
            info[0] = 1;
            info[1] = Math.max(root.val, rightInfo[1]);
            info[2] = Math.min(root.val, leftInfo[2]);
            info[3] = leftInfo[3] + rightInfo[3] + root.val;

            ans = Math.max(info[3], ans);
        }
        return info;
    }

    public static void main(String[] args) {
        TreeNode root = new TreeNode(1);
        root.left = new TreeNode(-5);
        root.left.right = new TreeNode(-3);
        root.right = new TreeNode(4);
        root.right.right = new TreeNode(10);
        System.out.println(maxSumBST(root));
    }
}

