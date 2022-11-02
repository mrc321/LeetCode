package leetcode;


/**
 * @author lab307
 */
public class Leetcode_110 {
    public boolean isBalanced(TreeNode root) {
        if (root == null) {
            return true;
        }
        int leftDepth = getDepth(root.left);
        int rightDepth = getDepth(root.right);

        boolean isBalanced = Math.abs(leftDepth - rightDepth) <= 1;
        return isBalanced && isBalanced(root.left) && isBalanced(root.right);
    }


    //返回当前子树的高度
    private int getDepth(TreeNode root) {
        if (root == null) {
            return 0;
        }
        return Math.max(getDepth(root.left), getDepth(root.right)) + 1;
    }

    class Info {
        public boolean isBalanced;
        public int depth;

        public Info(boolean isBalanced, int depth) {
            this.isBalanced = isBalanced;
            this.depth = depth;
        }
    }

    public static void main(String[] args) {
        TreeNode root = new TreeNode(3);
        root.left = new TreeNode(9);
        root.right = new TreeNode(20);
        root.right.right = new TreeNode(7);
        root.right.left = new TreeNode(15);
        System.out.println(new Leetcode_110().isBalanced(root));
    }


//    private Info check(TreeNode root) {
//        if (root == null) {
//            return new Info(true, 0);
//        }
//        Info left = check(root.left);
//        Info right = check(root.right);
//        //判断左右两侧是否为平衡二叉树
//        boolean balance = left.balance && right.balance && Math.abs(left.layer - right.layer) <= 1;
//        int layer = Math.max(left.layer,right.layer) + 1;
//        return new Info(balance,layer);
//    }
//    private class Info{
//        public boolean balance;
//        public int layer;
//
//        public Info(boolean balance,int layer){
//            this.balance = balance;
//            this.layer = layer;
//        }
//    }
}
