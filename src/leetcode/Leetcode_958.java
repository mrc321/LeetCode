package leetcode;

import java.util.LinkedList;
import java.util.Queue;

public class Leetcode_958 {
    public boolean isCompleteTree(TreeNode root) {
        //1、按层遍历，设置一个标志为，只要检测某个结点没有孩子，那么之后所有的结点都不允许有孩子
        Queue<TreeNode> queue = new LinkedList<>();

        if (root != null) {
            queue.offer(root);
        }
        boolean isEnd = false;
        while (!queue.isEmpty()) {
            TreeNode cur = queue.poll();
            //检测到某个结点只有右孩子没有左孩子直接返回
            if (cur.left == null && cur.right != null) {
                return false;
            }
            //此时如果isEnd为true了，任然还有孩子结点，说明不是完全二叉树
            if (isEnd && cur.left != null) {
                return false;
            }

            if (cur.left != null) {
                queue.offer(cur.left);
            } else {
                isEnd = true;
            }
            if (cur.right != null) {
                queue.offer(cur.right);
            } else {
                isEnd = true;
            }
        }
        return true;
    }

    public boolean isCompleteTree2(TreeNode root) {
        return process(root).isCBT;
    }

    private Info process(TreeNode root) {
        if (root == null) {
            return new Info(true, true, 0);
        }
        Info leftInfo = process(root.left);
        Info rightInfo = process(root.right);
        Info curInfo = new Info();
        curInfo.height = Math.max(leftInfo.height,rightInfo.height) + 1;
        //只有左子树和右子树都是满二叉树且高度相等，当前树才为满二叉树
        curInfo.isFull = leftInfo.isFull && rightInfo.isFull && leftInfo.height == rightInfo.height;
        if (curInfo.isFull == true) {
            curInfo.isCBT = true;
        }else {
            //左完，右满
            if (leftInfo.isCBT && rightInfo.isFull && leftInfo.height == rightInfo.height + 1){
                curInfo.isCBT = true;
            }else if (leftInfo.isFull && rightInfo.isCBT && leftInfo.height == rightInfo.height){
                //左满，右完
                curInfo.isCBT = true;
            } else if (leftInfo.isFull && rightInfo.isFull && leftInfo.height == rightInfo.height + 1) {
                //左满，右满
                curInfo.isCBT = true;
            }
        }
        return curInfo;
    }

    class Info {
        public boolean isFull;
        public boolean isCBT = false;
        public int height;

        public Info(boolean isFull, boolean isCBT, int height) {
            this.isFull = isFull;
            this.isCBT = isCBT;
            this.height = height;
        }
        public Info() {
        }
    }


    public static void main(String[] args) {
        TreeNode root = new TreeNode(1);
        root.right = new TreeNode(3);
        root.right.left = new TreeNode(6);

        root.left = new TreeNode(2);
        root.left.left = new TreeNode(4);
//        root.left.right = new TreeNode(5);
        System.out.println(new Leetcode_958().isCompleteTree(root));

    }
}
