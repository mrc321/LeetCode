package zuo.structure;

import java.util.ArrayList;

public class SkipListMap<K extends Comparable<K>, V> {
    public static class Node<K extends Comparable<K>, V> {
        public K key;
        public V value;
        public ArrayList<Node<K, V>> nextNodes;

        public Node(K key, V value) {
            this.key = key;
            this.value = value;
            nextNodes = new ArrayList<>();
        }

        /**
         * 当前结点小于otherKey，则返回true
         *
         * @param otherKey
         * @return
         */
        public boolean isKeyLess(K otherKey) {
            //对于key为null的结点是最小的（例如头结点）
            return otherKey != null && (key == null || key.compareTo(otherKey) < 0);
        }

        /**
         * 判断与本结点的key是否相等
         *
         * @param otherKey
         * @return
         */
        public boolean isKeyEqual(K otherKey) {
            //要么全为null，要么两者值相等
            return (key == null && otherKey == null) ||
                    (key != null && otherKey != null && key.compareTo(otherKey) == 0);
        }
    }

    private static final double PROBABILITY = 0.5;
    public final Node<K, V> head;
    private int size;
    public int maxLevel;

    public SkipListMap() {
        //头节点不存值，因此key和value全为null，并且表示最小的一个结点
        head = new Node<>(null, null);
        //初始时，next的第0层指向null
        head.nextNodes.add(null);
        size = 0;
        //层数从0开始
        maxLevel = 0;
    }

    /**
     * 在level层中，以cur结点的next结点为起始位置，找到小于key的最右侧结点
     *
     * @param key
     * @param cur
     * @param level
     * @return
     */
    private Node<K, V> mostRightLessNodeInLevel(K key, Node<K, V> cur, int level) {
        Node<K, V> next = cur.nextNodes.get(level);
        //next如果比key小，则一直在level层中右移
        while (next != null && next.isKeyLess(key)) {
            //cur保存比key小的结点
            cur = next;
            next = next.nextNodes.get(level);
        }
        return cur;
    }

    /**
     * 在整个跳表中 找到小于key的最右侧结点
     * 从高层往底层搜索，从左侧往右侧搜索
     *
     * @param key
     * @return
     */
    private Node<K, V> mostRightLessNodeInTree(K key) {
        if (key == null) {
            return null;
        }
        Node<K, V> pre = head;
        int level = maxLevel;
        while (level >= 0) {
            //在每一层找到最右侧 < key 的结点
            pre = mostRightLessNodeInLevel(key, pre, level);
            level--;
        }
        return pre;
    }

    /**
     * 找到key所在的节点，如果key不存在，返回key插入时左侧节点
     *
     * @param key
     * @return
     */
    private Node<K, V> findLastIndex(K key) {
        Node<K, V> less = mostRightLessNodeInTree(key);
        Node<K, V> next = less.nextNodes.get(0);
        return next != null && next.isKeyEqual(key) ? next : less;
    }

    /**
     * 查询调表中是否含有 key
     *
     * @param key
     * @return
     */
    public boolean containsKey(K key) {
        if (key == null) {
            return false;
        }
        return findLastIndex(key).isKeyEqual(key);
    }

    /**
     * 加入键值对
     *
     * @param key
     * @param value
     */
    public void put(K key, V value) {
        if (key == null) {
            return;
        }
        Node<K, V> less = mostRightLessNodeInTree(key);
        Node<K, V> next = less.nextNodes.get(0);
        if (next != null && next.isKeyEqual(key)) {
            //如果在跳表中已经存在了，则直接修改value即可
            next.value = value;
        } else
        {
            size++;
            //确定此结点的最大层高,50%的概率往上层跳
            int newNodeLevel = 0;
            while (Math.random() < PROBABILITY) {
                newNodeLevel++;
            }
            //将头节点的层数同步
            while (newNodeLevel > maxLevel) {
                head.nextNodes.add(null);
                maxLevel++;
            }
            //创建新节点,并初始化每一层的后继为null
            Node<K, V> newNode = new Node<>(key, value);
            for (int i = 0; i <= newNodeLevel; i++) {
                newNode.nextNodes.add(null);
            }
            //开始正式将新结点插入到跳表中,采用和 mostRightLessNodeInTree 方法一样的逐层搜索的方法
            int level = maxLevel;
            Node<K, V> pre = head;
            while (level >= 0) {
                //在level层找到最右 < key 的节点
                pre = mostRightLessNodeInLevel(key, pre, level);
                if (level <= newNodeLevel) {
                    //只有level的高度到达了新节点的高度，才开始变更新节点的指针
                    newNode.nextNodes.set(level, pre.nextNodes.get(level));
                    pre.nextNodes.set(level, newNode);
                }
                level--;
            }
        }
    }

    /**
     * 获取key对应的value
     *
     * @param key
     * @return
     */
    public V get(K key) {
        if (key == null) {
            return null;
        }
        Node<K, V> next = findLastIndex(key);
        return next.isKeyEqual(key) ? next.value : null;
    }

    /**
     * 删除key所在节点
     *
     * @param key
     */
    public void remove(K key) {
        if (containsKey(key)) {
            size--;
            //同样的开始逐层遍历查找
            int level = maxLevel;
            Node<K, V> pre = head;
            while (level >= 0) {
                pre = mostRightLessNodeInLevel(key, pre, level);
                //next为pre后面的第一个比pre大的节点，因此有可能为要移除的节点
                Node<K, V> next = pre.nextNodes.get(level);
                //如果next不为空，且刚好next就是要删除的节点
                if (next != null && next.isKeyEqual(key)) {
                    //pre节点的level层的next指针指向cur在level层后面的节点
                    pre.nextNodes.set(level, next.nextNodes.get(level));
                }
                //如果head头部，0层以上的位置都没有后继节点，则删除该层，并且最大高度减1
                if (level != 0 && pre == head && head.nextNodes.get(level) == null) {
                    head.nextNodes.remove(level);
                    maxLevel--;
                }
                //向下一层搜索
                level--;
            }
        }
    }

    /**
     * 找到第一个节点的key
     *
     * @return
     */
    public K firstKey() {
        //直接在第0层找
        Node<K, V> firstKeyNode = head.nextNodes.get(0);
        return firstKeyNode != null ? firstKeyNode.key : null;
    }

    /**
     * 找到最后一个节点的key
     *
     * @return
     */
    public K lastKey() {
        Node<K, V> cur = head;
        int level = maxLevel;
        while (level >= 0) {
            Node<K, V> next = cur.nextNodes.get(level);
            while (next != null) {
                cur = next;
                next = next.nextNodes.get(level);
            }
            level--;
        }
        return cur.key;
    }

    /**
     * 返回key或key右侧的节点key
     *
     * @param key
     * @return
     */
    public K ceilingKey(K key) {
        if (key == null) {
            return null;
        }
        Node<K, V> left = mostRightLessNodeInTree(key);
        Node<K, V> right = left.nextNodes.get(0);
        return right != null ? right.key : null;
    }

    /**
     * 返回key或key左侧的节点key
     *
     * @param key
     * @return
     */
    public K floorKey(K key) {
        if (key == null) {
            return null;
        }
        Node<K, V> left = mostRightLessNodeInTree(key);
        Node<K, V> right = left.nextNodes.get(0);
        return right != null && right.isKeyEqual(key) ? right.key : left.key;
    }

    public int size() {
        return size;
    }
}
