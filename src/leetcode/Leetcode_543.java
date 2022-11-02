package leetcode;

public class Leetcode_543 {
    int max = Integer.MIN_VALUE;

    public int diameterOfBinaryTree(TreeNode root) {
        getDepth(root);
        //由于max为最大路径上的结点个数，因此需要减1
        return max - 1;
    }

    private int getDepth(TreeNode root) {
        if (root == null) {
            return 0;
        }
        //获得左右子树的深度
        int leftDepth = getDepth(root.left);
        int rightDepth = getDepth(root.right);
        //获取当前子树的最大路径
        max = Math.max(leftDepth + rightDepth + 1, max);
        return Math.max(leftDepth, rightDepth) + 1;
    }
}
