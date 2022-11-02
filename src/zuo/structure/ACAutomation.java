package zuo.structure;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

public class ACAutomation {
    static class Node {
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

    private Node root;

    public ACAutomation() {
        root = new Node();
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

    /**
     * 正式匹配文章包含的候选串
     *
     * @param content 文章内容
     * @return 候选串集合
     */
    public List<String> containWords(String content) {
        int index = 0;
        Node cur = root;
        List<String> stringList = new ArrayList<>();
        for (int i = 0; i < content.length(); i++) {
            index = content.charAt(i) - 'a';

            //使用这个while循环，通过fail指针一直搜索能够成功匹配该字符的路径
            while (cur.nexts[index] == null && cur != root) {
                cur = cur.fail;
            }
            //运行到此处有两种情况：
            //1、现在来到的路径是可以继续匹配成功的：cur 跳到匹配成功后的下一个节点，
            //2、现在来到的节点是根节点root ： 说明匹配不上此字符串，直接匹配下一个字符

            //能够匹配上该字符
            if (cur.nexts[index] != null) {
                cur = cur.nexts[index];

                Node endNode = cur;
                //使用endNode以 fail指针转一圈，不错过任何一个能够匹配上的候选串
                while (endNode != root) {
                    if (!endNode.endUse && endNode.end != null) {
                        stringList.add(endNode.end);
                        endNode.endUse = true;
                    }
                    endNode = endNode.fail;
                }
            }
        }
        return stringList;
    }
    public static void main(String[] args) {
        ACAutomation ac = new ACAutomation();
        ac.insert("dhe");
        ac.insert("he");
        ac.insert("abcdheks");
        // 设置fail指针
        ac.build();

        List<String> contains = ac.containWords("abekskdjfafhasldkflskdjdhewqaeruv");
        for (String word : contains) {
            System.out.println(word);
        }
    }
}
