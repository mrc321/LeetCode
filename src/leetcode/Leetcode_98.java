package leetcode;

public class Leetcode_98 {
    public boolean isValidBST(TreeNode root) {
        return process(root).isBST;
    }

    private Info process(TreeNode root) {
        if (root == null) {
            return new Info(true, Long.MIN_VALUE, Long.MAX_VALUE);
        }
        Info leftInfo = process(root.left);
        Info rightInfo = process(root.right);

        //满足四个条件，则当前子树为二叉搜索树
        boolean isBST = leftInfo.isBST && rightInfo.isBST && root.val > leftInfo.max && root.val < rightInfo.min;
        long max = 0;
        long min = 0;
        //如果当前树为二叉搜索树，则更新max和min，否则max和min对于非二叉搜索树无用，默认值即可
        if (isBST) {
            max = Math.max(rightInfo.max, root.val);
            min = Math.min(leftInfo.min, root.val);
        }

        return new Info(isBST, max, min);
    }

    /**
     * 此时需要三个成员变量记录信息：
     * 1、布尔值记录是否是搜索树
     * 2、子树的最大值
     * 3、子树的最小值
     */
    class Info {
        public boolean isBST;
        public long max;
        public long min;

        public Info(boolean isBST, long max, long min) {
            this.isBST = isBST;
            this.max = max;
            this.min = min;
        }
    }

    long  pre = Long.MIN_VALUE;

    public boolean isValidBST2(TreeNode root) {
        if (root == null) {
            return true;
        }
        return isValidBST2(root.left) && (root.val > pre && (pre = root.val) == pre) && isValidBST2(root.right);
    }

    public static void main(String[] args) {
        TreeNode root = new TreeNode(2147483647);
//        root.left = new TreeNode(1);
//        root.right = new TreeNode(4);
//        root.right.left = new TreeNode(3);
//        root.right.right = new TreeNode(6);
        System.out.println(new Leetcode_98().isValidBST(root));
    }
}
