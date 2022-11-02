package leetcode;

import java.util.Arrays;

public class Leetcode_480 {
    public double[] medianSlidingWindow(int[] nums, int k) {
        SBTSet sbtSet = new SBTSet();
        double[] result = new double[nums.length - k + 1];
        int index = 0;

        for (int i = 0; i < k - 1; i++) {
            sbtSet.add(nums[i]);
        }

        for (int i = k - 1; i < nums.length; i++) {
            sbtSet.add(nums[i]);
            result[index++] = sbtSet.getMiddleNum();
            sbtSet.remove(nums[i - k + 1]);
        }
        return result;
    }

    class SBTSet {
        class Node {
            public int key;
            public int size;
            public int num;
            public Node left;
            public Node right;

            public Node(int key) {
                this.key = key;
                this.size = 1;
                this.num = 1;
            }
        }

        Node root;

        private Node rightRotate(Node head) {
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

        private Node leftRotate(Node head) {
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

        private Boolean contains(int key) {
            Node cur = root;
            while (cur != null) {
                if (cur.key == key) {
                    return true;
                } else if (cur.key < key) {
                    cur = cur.right;
                } else {
                    cur = cur.left;
                }
            }
            return false;
        }

        private Boolean reallyDelete(int key) {
            Node cur = root;
            while (cur != null) {
                if (cur.key == key) {
                    int curNum = cur.num - (cur.left != null ? cur.left.num : 0) - (cur.right != null ? cur.right.num : 0);
                    return curNum == 1;
                } else if (cur.key < key) {
                    cur = cur.right;
                } else {
                    cur = cur.left;
                }
            }
            return false;
        }

        private Node add(Node head, int key, Boolean isContains) {
            if (head == null) {
                return new Node(key);
            }
            head.num++;
            if (head.key == key) {
                return head;
            } else {
                if (!isContains) {
                    head.size++;
                }
                if (head.key < key) {
                    head.right = add(head.right, key, isContains);
                } else {
                    head.left = add(head.left, key, isContains);
                }
                return maintain(head);
            }
        }

        private Node delete(Node head, int key, Boolean isDelete) {
            head.num--;
            if (isDelete) {
                head.size--;
            }
            if (head.key < key) {
                head.right = delete(head.right, key, isDelete);
            } else if (head.key > key) {
                head.left = delete(head.left, key, isDelete);
            } else {
                //num为0了，则表示要将该节点删除掉
                if (isDelete) {
                    //三种情况:没有孩子，只有一个孩子，有双孩子
                    if (head.left == null && head.right == null) {
                        return null;
                    } else if (head.left != null && head.right != null) {
                        //找后继节点
                        Node pre = null;
                        Node cur = head.right;
                        while (cur.left != null) {
                            cur.size--;

                            pre = cur;
                            cur = cur.left;
                        }
                        if (pre != null) {
                            cur.num -= (cur.right != null ? cur.right.num : 0);
                            //把从head.right到pre的所有节点的num都减 cur.num
                            Node start = head.right;
                            while (start != cur) {
                                start.num -= cur.num;
                                start = start.left;
                            }


                            pre.left = cur.right;

                            cur.left = head.left;
                            cur.right = head.right;
                            cur.size = cur.left.size + cur.right.size + 1;
                            cur.num += cur.left.num + cur.right.num;
                        } else {
                            cur.left = head.left;
                            cur.size += cur.left.size;
                            cur.num += cur.left.num;
                        }
                        return cur;
                    } else if (head.left != null) {
                        return head.left;
                    } else {
                        return head.right;
                    }
                }
            }
            return head;
        }

        private double findNodeByIndex(int index) {
            Node cur = root;
            while (cur != null) {
                int leftNum = cur.left != null ? cur.left.num : 0;
                int rightNum = cur.right != null ? cur.right.num : 0;
                int curNum = cur.num - rightNum;

                if (index <= leftNum) {
                    cur = cur.left;
                } else if (index <= curNum) {
                    return cur.key;
                } else {
                    index -= curNum;
                    cur = cur.right;
                }
            }
            return 0;
        }

        public void add(int key) {
            root = add(root, key, contains(key));
        }

        public void remove(int key) {
            if (contains(key)) {
                root = delete(root, key, reallyDelete(key));
            }
        }

        public double getMiddleNum() {
            int k = root.num;
            int mid = (k + 1) / 2;
            if (k % 2 == 1) {
                return findNodeByIndex(mid);
            } else {
                return (findNodeByIndex(mid) + findNodeByIndex(mid + 1)) / 2.0;
            }
        }

    }

    public static void main(String[] args) {
        int[] nums = {7, 0, 3, 9, 9, 9, 1, 7, 2, 3};
        double[] doubles = new Leetcode_480().medianSlidingWindow(nums, 6);
        System.out.println(Arrays.toString(doubles));
    }

}

