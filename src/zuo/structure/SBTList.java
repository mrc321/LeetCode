package zuo.structure;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

public class SBTList<V> {
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
     * ??????head?????????????????????index????????????cur??????
     *
     * @param head
     * @param index ?????????0??????
     * @param cur
     * @return
     */
    private Node insert(Node head, int index, Node cur) {
        if (head == null) {
            return cur;
        }
        head.size++;
        //??????????????????????????????????????????
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
            //???????????????,???????????????
            if (head.left == null && head.right == null) {
                head = null;
            } else if (head.left != null && head.right != null) {
                //???????????????
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
     * ???index???????????????
     *
     * @param index
     * @param value
     */
    public void insert(int index, V value) {
        Node node = new Node(value);
        //???????????????????????????????????????????????????????????????????????????
        if (index >= 0 && index <= size()) {
            root = insert(root, index, node);
        }
    }

    /**
     * ???????????????
     *
     * @param value
     */
    public void add(V value) {
        int index = size();
        insert(index, value);
    }

    /**
     * ??????index????????????
     *
     * @param index
     * @return
     */
    public V get(int index) {
        if (index >= 0 && index < size()) {
            return get(root, index).value;
        } else {
            throw new ArrayIndexOutOfBoundsException("??????");
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

    // ???????????????????????????
    // ????????????????????????LinkedList?????????????????????get????????????SbtList
    // LinkedList????????????index???????????????????????????????????????????????????????????????O(N)
    // SbtList????????????????????????????????????????????????????????????????????????O(logN)
    public static void main(String[] args) {
        // ????????????
        int test = 50000;
        int max = 1000000;
        boolean pass = true;
        String name = "ArrayList";
        List<Integer> list = new ArrayList<>();
        SBTList sbtList = new SBTList();
        for (int i = 0; i < test; i++) {
            if (list.size() != sbtList.size()) {
                pass = false;
                break;
            }
            if (list.size() > 1 && Math.random() < 0.5) {
                int removeIndex = (int) (Math.random() * list.size());
                list.remove(removeIndex);
                sbtList.remove(removeIndex);
            } else {
                int randomIndex = (int) (Math.random() * (list.size() + 1));
                int randomValue = (int) (Math.random() * (max + 1));
                list.add(randomIndex, randomValue);
                sbtList.insert(randomIndex, randomValue);
            }
        }
        for (int i = 0; i < list.size(); i++) {
            if (!list.get(i).equals(sbtList.get(i))) {
                pass = false;
                break;
            }
        }
        System.out.println("???????????????????????? : " + pass);

        // ????????????
        test = 200000;
        list = new ArrayList<>();
        sbtList = new SBTList();
        long start = 0;
        long end = 0;

        testTime(new ArrayList<>(),test,max);
        testTime(new LinkedList<>(),test,max);

        start = System.currentTimeMillis();
        for (int i = 0; i < test; i++) {
            int randomIndex = (int) (Math.random() * (sbtList.size() + 1));
            int randomValue = (int) (Math.random() * (max + 1));
            sbtList.insert(randomIndex, randomValue);
        }
        end = System.currentTimeMillis();
        System.out.println("SbtList???????????????(??????) : " + (end - start));

        start = System.currentTimeMillis();
        for (int i = 0; i < test; i++) {
            int randomIndex = (int) (Math.random() * (i + 1));
            sbtList.get(randomIndex);
        }
        end = System.currentTimeMillis();
        System.out.println("SbtList???????????????(??????) :  " + (end - start));

        start = System.currentTimeMillis();
        for (int i = 0; i < test; i++) {
            int randomIndex = (int) (Math.random() * sbtList.size());
            sbtList.remove(randomIndex);
        }
        end = System.currentTimeMillis();
        System.out.println("SbtList???????????????(??????) :  " + (end - start));
    }


    private static void testTime(List<Integer> list, int test, int max) {
        long start = 0;
        long end = 0;
        String name = list.getClass().getSimpleName();
        start = System.currentTimeMillis();
        for (int i = 0; i < test; i++) {
            int randomIndex = (int) (Math.random() * (list.size() + 1));
            int randomValue = (int) (Math.random() * (max + 1));
            list.add(randomIndex, randomValue);
//            if (i % 10000 == 0){
//                System.out.println(i + ": " + name + "???????????????(??????) ??? " + (System.currentTimeMillis() - start));
//            }
        }
        end = System.currentTimeMillis();
        System.out.println(name + "???????????????(??????) ??? " + (end - start));

        start = System.currentTimeMillis();
        for (int i = 0; i < test; i++) {
            int randomIndex = (int) (Math.random() * (i + 1));
            list.get(randomIndex);
        }
        end = System.currentTimeMillis();
        System.out.println(name + "???????????????(??????) : " + (end - start));

        start = System.currentTimeMillis();
        for (int i = 0; i < test; i++) {
            int randomIndex = (int) (Math.random() * list.size());
            list.remove(randomIndex);
        }
        end = System.currentTimeMillis();
        System.out.println(name + "???????????????(??????) : " + (end - start));
    }
}
