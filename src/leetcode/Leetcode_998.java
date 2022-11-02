package leetcode;

public class Leetcode_998 {
    public TreeNode insertIntoMaxTree(TreeNode root, int val) {
        //因为每次新加入的元素都在数组的最后一位，因此:
        // 如果val大于某个结点，则该结点必然为val的左孩子
        // 如果val小于某个结点，则val必然为该结点的右孩子
        if (root == null || val > root.val) {
            TreeNode newNode = new TreeNode(val);
            newNode.left = root;
            return newNode;
        }
        root.right = insertIntoMaxTree(root.right, val);
        return root;
    }

}
