package leetcode;

public class Leetcode_327 {
    public static int countRangeSum(int[] nums, int lower, int upper) {

        SBTSet sbtSet = new SBTSet();
        sbtSet.add(0);

        //前缀和
        long prefixSum = 0;
        int result = 0;

        for (int i = 0; i < nums.length; i++) {
            prefixSum += nums[i];
            int rightNum = sbtSet.lessKey(prefixSum - lower + 1);
            int leftNum = sbtSet.lessKey(prefixSum - upper);
            result += rightNum - leftNum;
            //System.out.println(i + ": " + (rightNum - leftNum) + " || 0: " + sbtSet.getNumByKey(0) + " 3: " + sbtSet.getNumByKey(3) + " 7: " + sbtSet.getNumByKey(7));
            sbtSet.add(prefixSum);
        }
        return result;
    }

    public static void main(String[] args) {
        int[] num = {3, 0, 4, 4};
        System.out.println(countRangeSum(num, 4, 10));
    }

}

class SBTSet {
    //每个节点需要包含
    class Node {
        public long key;
        //这个树上总的节点个数（不包含重复的值）（平衡因子）
        public int size;
        public Node left;
        public Node right;
        //这颗树上加入进来的数的总个数（包括重复的值）
        public int num;

        public Node(long key) {
            this.key = key;
            this.size = 1;
            this.num = 1;
        }
    }

    public int getNumByKey(int key) {
        Node head = root;
        while (head != null) {
            if (head.key == key) {
                return head.num;
            } else if (key < head.key) {
                head = head.left;
            } else if (head.key < key) {
                head = head.right;
            }
        }
        return 0;
    }


    Node root;

    //右旋
    private Node rightRotate(Node head) {
        //首先记录head左侧子树的数字个数，目的是为了计算head去掉左子树后剩余的数字个数
        int leftNum = head.left.num;

        Node newHead = head.left;
        head.left = newHead.right;
        newHead.right = head;

        newHead.size = head.size;
        newHead.num = head.num;

        head.size = (head.left != null ? head.left.size : 0) + (head.right != null ? head.right.size : 0) + 1;
        head.num = head.num - leftNum + (head.left != null ? head.left.num : 0);
        return newHead;
    }

    //左旋
    private Node leftRotate(Node head) {
        //首先记录head左侧子树的数字个数，目的是为了计算 head 去掉左子树后剩余的数字个数
        int rightNum = head.right.num;

        Node newHead = head.right;
        head.right = newHead.left;
        newHead.left = head;

        newHead.size = head.size;
        newHead.num = head.num;

        head.size = (head.left != null ? head.left.size : 0) + (head.right != null ? head.right.size : 0) + 1;
        head.num = head.num - rightNum + (head.right != null ? head.right.num : 0);
        return newHead;
    }

    //维护
    private Node maintain(Node head) {
        //把这些节点的个数都取出来
        int leftSize = head.left != null ? head.left.size : 0;
        int leftLeftSize = head.left != null && head.left.left != null ? head.left.left.size : 0;
        int leftRightSize = head.left != null && head.left.right != null ? head.left.right.size : 0;

        int rightSize = head.right != null ? head.right.size : 0;
        int rightRightSize = head.right != null && head.right.right != null ? head.right.right.size : 0;
        int rightLeftSize = head.right != null && head.right.left != null ? head.right.left.size : 0;
        //开始判断三种失衡状况，并做出对应的对策
        //对策就是，旋转后，再针对子节点发生变化的做递归检测调整
        if (rightSize < leftLeftSize) {
            // LL型
            head = rightRotate(head);
            head.right = maintain(head.right);
            head = maintain(head);
        } else if (rightSize < leftRightSize) {
            //LR型
            head.left = leftRotate(head.left);
            head = rightRotate(head);

            head.left = maintain(head.left);
            head.right = maintain(head.right);
            head = maintain(head);
        } else if (leftSize < rightLeftSize) {
            //RL型
            head.right = rightRotate(head.right);
            head = leftRotate(head);

            head.left = maintain(head.left);
            head.right = maintain(head.right);
            head = maintain(head);
        } else if (leftSize < rightRightSize) {
            //RR型
            head = leftRotate(head);

            head.left = maintain(head.left);
            head = maintain(head);
        }
        return head;
    }

    //添加
    private Node add(Node head, long key, boolean isContains) {
        if (head == null) {
            return new Node(key);
        }
        head.num++;

        if (head.key == key) {
            return head;
        } else {
            //不包含该节点，size还会加一
            if (!isContains) {
                head.size++;
            }

            if (key < head.key) {
                head.left = add(head.left, key, isContains);
            } else {
                head.right = add(head.right, key, isContains);
            }
            return maintain(head);
        }
    }

    private Boolean contains(Node head, long key) {
        if (head == null) {
            return false;
        }
        if (head.key == key) {
            return true;
        } else if (key < head.key) {
            return contains(head.left, key);
        } else {
            return contains(head.right, key);
        }
    }

    public void add(long key) {
        boolean isContains = contains(root, key);
        root = add(root, key, isContains);

    }


    private int lessKey(Node head, long key) {
        if (head == null) {
            return 0;
        }

        if (head.key < key) {
            return head.num - (head.right != null ? head.right.num : 0) + lessKey(head.right, key);
        } else {
            return lessKey(head.left, key);
        }
    }

    /**
     * 返回比key小的数总共有多少个
     *
     * @param key
     * @return
     */
    public int lessKey(long key) {
        return lessKey(root, key);
    }
}



