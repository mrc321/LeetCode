package leetcode;

import jdk.nashorn.internal.ir.EmptyNode;
import zuo.class2.Test;

import java.net.InetAddress;

public class Leetcode_111 {
    public static void main(String[] args) {
        TreeNode root = new TreeNode(1);
        root.right = new TreeNode(3);
        root.left = new TreeNode(2);
        root.left.left = new TreeNode(4);
        root.left.left.left = new TreeNode(5);
        System.out.println(minDepth(root));

    }

    public static int minDepth(TreeNode root) {
        if (root == null) {
            return 0;
        }
        //新申请一个头节点，使其做指针指向root
        TreeNode newRoot = new TreeNode();
        newRoot.left = root;
        TreeNode cur = newRoot;
        //新头节点所在层为0层，整个过程中新头节点总共会被遍历两次，开始一次，最后结束一次
        int curLevel = 0;
        int minLevel = Integer.MAX_VALUE;

        while (cur != null) {
            if (cur.left != null) {
                TreeNode mostRight = cur.left;
                //leftDepth记录cur到mostRight的距离，注意目的是为了当从MostRight返回到cur时，恢复curLevel
                int leftDepth = 1;
                while (mostRight.right != null && mostRight.right != cur) {
                    leftDepth++;
                    mostRight = mostRight.right;
                }
                if (mostRight.right == null) {
                    mostRight.right = cur;
                    cur = cur.left;
                    //向左边移动，就加一
                    curLevel++;
                    continue;
                } else {
                    //因为从mostRight到cur多加了一个1，所以在这里需要减掉这个1
                    curLevel--;
                    //mostRight左指针为null，说明mostRight是叶子节点，考虑其Level是否为最小深度
                    if (mostRight.left == null) {
                        minLevel = Math.min(curLevel, minLevel);
                    }
                    //恢复curLevel
                    curLevel -= leftDepth;
                    mostRight.right = null;
                    if (curLevel == 0) {
                        //当curLevel第二次等于0，说明正颗树已经遍历完毕，时候
                        break;
                    }
                }
            }
            curLevel++;
            cur = cur.right;
        }
        return minLevel;
    }
}
