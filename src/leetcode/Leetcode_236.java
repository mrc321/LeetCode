package leetcode;

public class Leetcode_236 {
    public TreeNode lowestCommonAncestor(TreeNode root, TreeNode p, TreeNode q) {
        //包含了父节点就是其中之一的情况
        if (root == null || root == p || root == q) {
            return root;
        }

        TreeNode left = lowestCommonAncestor(root.left, p, q);
        TreeNode right = lowestCommonAncestor(root.right, p, q);

        //包括了父节点就是自己的情况
        if (left != null && right != null) {
            return root;
        }
        //包括了父节点在左树或右树之一的情况
        return left != null ? left : right;
    }
}
