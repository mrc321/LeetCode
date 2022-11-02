package leetcode;

import zuo.structure.SBTList;

import java.util.Arrays;

public class Leetcode_406 {
    public static void main(String[] args) {
        Leetcode_406 code_406 = new Leetcode_406();
        int[][] people = {{7,0},{4,4},{7,1},{5,0},{6,1},{5,2}};
        int[][] ints = code_406.reconstructQueue(people);
        System.out.println(Arrays.deepToString(ints));
    }
    public int[][] reconstructQueue(int[][] people) {
        int[][] queue = new int[people.length][2];
        SBTList<Unit> sbtList = new SBTList<>();
        Unit[] peopleUnit = new Unit[people.length];
        //全部包装成对象，方便排序
        for (int i = 0; i < people.length; i++) {
            peopleUnit[i] = new Unit(people[i][0], people[i][1]);
        }
        //按照指定规则排序
        Arrays.sort(peopleUnit);
        //开始依次插入队列，以k为下标，以h为value
        for (int i = 0; i < people.length; i++) {
            sbtList.insert(peopleUnit[i].k, peopleUnit[i]);
        }
        //全部一个一个按序取出来
        for (int i = 0; i < people.length; i++) {
            Unit curPeople = sbtList.get(i);
            queue[i][0] = curPeople.h;
            queue[i][1] = curPeople.k;
        }
        return queue;
    }

    class Unit implements Comparable<Unit> {
        public int h;
        public int k;

        public Unit(int h, int k) {
            this.h = h;
            this.k = k;
        }

        @Override
        public int compareTo(Unit other) {
            //身高按照从大到小的顺序排列，如果身高相同，按照k从小到大排列
            return this.h != other.h ? other.h - this.h : this.k - other.k;
        }

    }

    class SBTList<V> {
        public class Node {
            public V value;
            public int size;
            public Node left;
            public Node right;

            public Node(V value) {
                this.value = value;
                this.size = 1;
            }
        }

        private Node root;

        private Node leftRotate(Node head) {
            Node rightNode = head.right;
            head.right = rightNode.left;
            rightNode.left = head;

            rightNode.size = head.size;

            head.size = (head.left != null ? head.left.size : 0) + (head.right != null ? head.right.size : 0) + 1;
            return rightNode;
        }

        private Node rightRotate(Node head) {
            Node leftNode = head.left;
            head.left = leftNode.right;
            leftNode.right = head;

            leftNode.size = head.size;
            head.size = (head.left != null ? head.left.size : 0) + (head.right != null ? head.right.size : 0) + 1;
            return leftNode;
        }

        private Node maintain(Node head) {
            int leftSize = head.left != null ? head.left.size : 0;
            int leftLeftSize = head.left != null && head.left.left != null ? head.left.left.size : 0;
            int leftRightSize = head.left != null && head.left.right != null ? head.left.right.size : 0;
            int rightSize = head.right != null ? head.right.size : 0;
            int rightLeftSize = head.right != null && head.right.left != null ? head.right.left.size : 0;
            int rightRightSize = head.right != null && head.right.right != null ? head.right.right.size : 0;
            if (rightSize < leftLeftSize) {
                head = rightRotate(head);

                head.right = maintain(head.right);
                head = maintain(head);
            } else if (rightSize < leftRightSize) {
                head.left = leftRotate(head.left);
                head = rightRotate(head);

                head.left = maintain(head.left);
                head.right = maintain(head.right);
                head = maintain(head);
            } else if (leftSize < rightLeftSize) {
                head.right = rightRotate(head.right);
                head = leftRotate(head);

                head.left = maintain(head.left);
                head.right = maintain(head.right);
                head = maintain(head);
            } else if (leftSize < rightRightSize) {
                head = leftRotate(head);

                head.left = maintain(head.left);
                head = maintain(head);
            }
            return head;
        }

        /**
         * 在以head为头的树上，向index位置插入cur节点
         *
         * @param head
         * @param index 范围从0开始
         * @param cur
         * @return
         */
        private Node insert(Node head, int index, Node cur) {
            if (head == null) {
                return cur;
            }
            head.size++;
            //首先确定头节点目前在第几个数
            int headIndex = head.left != null ? head.left.size : 0;
            if (index <= headIndex) {
                head.left = insert(head.left, index, cur);
            } else {
                head.right = insert(head.right, index - headIndex - 1, cur);
            }
            return maintain(head);
        }

        /**
         * @param head
         * @param index
         * @return
         */
        private Node remove(Node head, int index) {
            if (head == null) {
                return null;
            }
            head.size--;
            int headIndex = head.left != null ? head.left.size : 0;
            if (index < headIndex) {
                head.left = remove(head.left, index);
            } else if (index > headIndex) {
                head.right = remove(head.right, index - headIndex - 1);
            } else {
                //删除头节点,分三种情况
                if (head.left == null && head.right == null) {
                    head = null;
                } else if (head.left != null && head.right != null) {
                    //找后继节点
                    Node pre = null;
                    Node cur = head.right;
                    while (cur.left != null) {
                        pre = cur;
                        cur.size--;
                        cur = cur.left;
                    }
                    if (cur != head.right) {
                        pre.left = cur.right;

                        cur.right = head.right;
                    }
                    cur.left = head.left;
                    cur.size = head.size;
                    head = cur;
                } else if (head.left != null) {
                    head = head.left;
                } else {
                    head = head.right;
                }
            }
            return head;
        }

        private Node get(Node head, int index) {
            if (head == null) {
                return null;
            }
            int headIndex = head.left != null ? head.left.size : 0;
            if (headIndex == index) {
                return head;
            } else if (index < headIndex) {
                return get(head.left, index);
            } else {
                return get(head.right, index - headIndex - 1);
            }
        }

        /**
         * 往index位置插入值
         *
         * @param index
         * @param value
         */
        public void insert(int index, V value) {
            Node node = new Node(value);
            //可以看到，插入节点的下标不能大于当前整个数组的个数
            if (index >= 0 && index <= size()) {
                root = insert(root, index, node);
            }
        }

        /**
         * 往末尾添加
         *
         * @param value
         */
        public void add(V value) {
            int index = size();
            insert(index, value);
        }

        /**
         * 获取index位置的数
         *
         * @param index
         * @return
         */
        public V get(int index) {
            if (index >= 0 && index < size()) {
                return get(root, index).value;
            } else {
                throw new ArrayIndexOutOfBoundsException("越界");
            }
        }

        public void remove(int index) {
            if (index >= 0 && index < size()) {
                root = remove(root, index);
            }
        }

        public int size() {
            return root != null ? root.size : 0;
        }
    }
}
