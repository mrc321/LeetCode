package leetcode;

public class Leetcode_333 {

    //用于存放最大BST的节点数
    int max = 0;
    public int largestBSTSubtree(TreeNode head) {
        process(head);
        return max;
    }

    private Info process(TreeNode root) {
        if (root == null) {
            return new Info(true, Long.MIN_VALUE, Long.MAX_VALUE, 0);
        }
        Info leftInfo = process(root.left);
        Info rightInfo = process(root.right);
        Info curInfo = new Info();
        //如果是BST数，则更新当前子树的信息
        if (leftInfo.isBST && rightInfo.isBST && root.val > leftInfo.max && root.val < rightInfo.min) {
            curInfo.isBST = true;
            curInfo.max = Math.max(rightInfo.max, root.val);
            curInfo.min = Math.min(leftInfo.min, root.val);
            curInfo.number = leftInfo.number + rightInfo.number + 1;
            max = Math.max(max, curInfo.number);
        }
        return curInfo;
    }

    class Info {
        public boolean isBST;
        public long max;
        public long min;
        public int number;

        public Info() {
        }

        public Info(boolean isBST, long max, long min, int number) {
            this.isBST = isBST;
            this.max = max;
            this.min = min;
            this.number = number;
        }
    }
}
