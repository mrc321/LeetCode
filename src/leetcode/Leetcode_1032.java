package leetcode;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

public class Leetcode_1032 {
    class StreamChecker {
        private Node root;
        private Node cur;

        public StreamChecker(String[] words) {
            root = new Node();
            cur = root;
            for (String word : words) {
                insert(word);
            }
            build();
        }

        public boolean query(char letter) {
            int index = letter - 'a';
            //使用这个while循环，一直搜索能够成功匹配的路径
            while (cur.nexts[index] == null && cur != root) {
                cur = cur.fail;
            }
            if (cur.nexts[index] != null) {
                cur = cur.nexts[index];
                Node endNode = cur;
                while (endNode != root) {
                    if (endNode.end != null) {
                        return true;
                    }
                    endNode = endNode.fail;
                }
            }
            return false;
        }

        class Node {
            //记录以本节点结尾的字符串是否已经被识别
            private boolean endUse;
            //存放以本节点结尾的字符串
            private String end;
            //存放子节点引用，长度总共为26，每一个下标代表对应的字母（本节点到子节点的边上的值）
            private Node[] nexts;
            //存放匹配失败时，应当跳转到的节点的指针
            private Node fail;

            public Node() {
                endUse = false;
                end = null;
                nexts = new Node[26];
                fail = null;
            }
        }

        /**
         * 构建前缀树
         *
         * @param s 要加入前缀树的字符串s
         */
        public void insert(String s) {
            char[] charArray = s.toCharArray();
            Node cur = root;
            int index = 0;
            for (int i = 0; i < charArray.length; i++) {
                index = charArray[i] - 'a';
                //查看当前节点到 charArray[i] 的边是否存在，不存在就创建以 charArray[i] 边连接的子节点
                if (cur.nexts[index] == null) {
                    cur.nexts[index] = new Node();
                }
                cur = cur.nexts[index];
            }
            cur.end = s;
        }

        /**
         * 必须要前缀树构造完毕后，才能够调用本方法
         * 将前缀树改造成一颗AC树，主要是让每个节点的fail指针都所有指向
         */
        public void build() {
            //采用宽度优先比例，因此适用一个队列
            Queue<Node> queue = new LinkedList<>();
            queue.offer(root);
            while (!queue.isEmpty()) {
                Node cur = queue.poll();
                //设置每个cur每个孩子节点的fail指针
                for (int index = 0; index < 26; index++) {
                    if (cur.nexts[index] != null) {
                        Node child = cur.nexts[index];
                        //默认指向root
                        child.fail = root;
                        //child的父节点的fail指针所指向的节点
                        Node failNode = cur.fail;
                        while (failNode != null) {
                            //查看faileNode是否有与cur 到 child相同的边 index
                            if (failNode.nexts[index] != null) {
                                child.fail = failNode.nexts[index];
                                break;
                            }
                            failNode = failNode.fail;
                        }
                        queue.offer(child);
                    }
                }
            }
        }
    }
}
