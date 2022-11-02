package leetcode;

public class Leetcode_654 {
    public TreeNode constructMaximumBinaryTree(int[] nums) {
        return constructMaximumBinaryTree(nums, 0, nums.length - 1);
    }

    private TreeNode constructMaximumBinaryTree(int[] nums, int start, int end) {
        if (start > end) {
            return null;
        }
        //找出最大数的下标
        int maxIndex = start;
        for (int i = start; i <= end; i++) {
            if (nums[i] > nums[maxIndex]) {
                maxIndex = i;
            }
        }
        TreeNode root = new TreeNode(nums[maxIndex]);
        root.left = constructMaximumBinaryTree(nums, start, maxIndex - 1);
        root.right = constructMaximumBinaryTree(nums, maxIndex + 1, end);
        return root;
    }
}
