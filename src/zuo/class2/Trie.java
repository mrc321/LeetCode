package zuo.class2;

import java.util.HashMap;

/**
 * @author lab307
 */
public class Trie {
    private class Node {
        public int pass;
        public int end;
        //这个map的key存放的是边上的值（字符串），value存放的是这条边所指向的下一个结点
        public HashMap<Character, Node> nexts;

        public Node() {
            pass = 0;
            end = 0;
            nexts = new HashMap<>();
        }
    }

    private final Node root;

    public Trie() {
        root = new Node();
    }

    /**
     * 往树中插入一条字符串
     * @param word
     */
    public void insert(String word) {
        if (word == null || word.length() == 0) {
            return;
        }
        //先将字符串转成字符数组
        char[] chars = word.toCharArray();
        Node node = root;
        node.pass++;
        //开始遍历数组中每一个字符，并开始构建这个树
        for (char ch : chars) {
            //先查看当前结点所连接的边中存在当前字符吗
            if (!node.nexts.containsKey(ch)) {
                //如果不存在则创建一个新的边和与之相连的结点
                node.nexts.put(ch, new Node());
            }
            //取出与该字符相连的结点
            node = node.nexts.get(ch);
            //相当于有边经过这条结点，所以要对该边加一
            node.pass++;
        }
        node.end++;
    }

    public void delete(String word){
        //先查一下看有没有，没有的话直接返回不做删除操作
        if (search(word) == 0) {
            return;
        }
        Node node = root;
        node.pass--;
        char[] chars = word.toCharArray();
        //遍历字符串的每一个字符
        for (char ch : chars) {
            //让下一个结点的pass先自减，如果减为了0，直接将该结点删除，并返回
            if (--node.nexts.get(ch).pass == 0){
                node.nexts.remove(ch);
                return;
            }
            //开始操作下一个结点
            node = node.nexts.get(ch);
        }
        //如果执行到了这里，说明要删除的字符串不止一个，所以要对end做减一操作
        node.end--;
    }

    /**
     * @param word
     * @return word之前已经加入了的次数
     */
    public int search(String word) {
        return find(word, true);
    }

    /**
     * @param pre
     * @return 返回前缀pre出现的次数
     */
    public int prefixNumber(String pre) {
        return find(pre, false);
    }

    private int find(String word, boolean flag) {
        if (word == null || word.length() == 0) {
            return 0;
        }
        //从根节点开始往下查
        Node node = root;
        char[] chars = word.toCharArray();
        for (char ch : chars) {
            //如果不包含当前字符，说明word在该树中不存在
            if (!node.nexts.containsKey(ch)) {
                return 0;
            }
            node = node.nexts.get(ch);
        }
        return flag ? node.end : node.pass;
    }
}
