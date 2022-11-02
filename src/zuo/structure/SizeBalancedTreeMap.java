package zuo.structure;

public class SizeBalancedTreeMap<K extends Comparable<K>, V> {
    private static class Node<K extends Comparable<K>, V> {
        public K key;
        public V value;
        public Node<K, V> left;
        public Node<K, V> right;
        public int size;

        public Node(K key, V value) {
            this.key = key;
            this.value = value;
            size = 1;
        }
    }

    private Node<K, V> root;
    private int size;

    /**
     * 右旋
     *
     * @param head
     * @return
     */
    private Node<K, V> rightRotate(Node<K, V> head) {
        Node<K, V> newHead = head.left;
        head.left = newHead.right;
        newHead.right = head;

        //新头节点的个数就等于原来头节点的个数，因为右旋不改变整颗树的结点个数
        newHead.size = head.size;
        //原来头节点所在树的大小要重新计算
        head.size = (head.left != null ? head.left.size : 0) + (head.right != null ? head.right.size : 0) + 1;

        return newHead;
    }

    /**
     * 左旋
     *
     * @param head
     * @return
     */
    private Node<K, V> leftRotate(Node<K, V> head) {
        Node<K, V> newHead = head.right;
        head.right = newHead.left;
        newHead.left = head;

        newHead.size = head.size;

        //原来头节点所在树的大小要重新计算
        head.size = (head.left != null ? head.left.size : 0) + (head.right != null ? head.right.size : 0) + 1;
        return newHead;
    }

    /**
     * 检测四种失衡情况，并做出对应情况的旋转调整
     *
     * @param head
     * @return 调整后新的头节点
     */
    private Node<K, V> maintain(Node<K, V> head) {
        if (head == null) {
            return null;
        }
        int leftSize = head.left != null ? head.left.size : 0;
        int leftLeftSize = head.left != null && head.left.left != null ? head.left.left.size : 0;
        int leftRightSize = head.left != null && head.left.right != null ? head.left.right.size : 0;

        int rightSize = head.right != null ? head.right.size : 0;
        int rightLeftSize = head.right != null && head.right.left != null ? head.right.left.size : 0;
        int rightRightSize = head.right != null && head.right.right != null ? head.right.right.size : 0;

        //对应的失衡类型，做对应的失衡调整
        if (rightSize < leftLeftSize) {
            //LL型
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

    /**
     * 添加结点
     *
     * @param head
     * @param key
     * @param value
     * @return
     */
    private Node<K, V> add(Node<K, V> head, K key, V value) {
        if (head == null) {
            return new Node<K, V>(key, value);
        }

        head.size++;
        if (head.key.compareTo(key) > 0) {
            head.left = add(head.left, key, value);
        } else if (head.key.compareTo(key) < 0) {
            head.right = add(head.right, key, value);
        }

        return maintain(head);
    }

    /**
     * 在head这颗树上，删掉key为key的节点
     * 按照BST的方式删除，无关平衡关系
     *
     * @param head
     * @param key
     * @return
     */
    private Node<K, V> delete(Node<K, V> head, K key) {
        if (head == null) {
            return null;
        }
        //沿途 所有结点 都要减少1
        head.size--;

        if (head.key.compareTo(key) > 0) {
            head.left = delete(head.left, key);
        } else if (head.key.compareTo(key) < 0) {
            head.right = delete(head.right, key);
        } else if (head.key.compareTo(key) == 0) {
            Node<K, V> leftNode = head.left;
            Node<K, V> rightNode = head.right;
            //三种情况
            if (leftNode == null && rightNode == null) {
                head = null;
            } else if (leftNode != null && rightNode != null) {
                //找到后继结点
                Node<K, V> pre = null;
                Node<K, V> dest = rightNode;

                //一直往下找后继结点，pre是后继结点的父结点
                while (dest.left != null) {
                    pre = dest;
                    //如果后面还有结点，则沿途所有的结点都减一
                    pre.size--;
                    dest = dest.left;
                }

                if (pre != null) {
                    //pre不为null，表示此后继结点不是 rightNode
                    pre.left = dest.right;
                    dest.left = leftNode;
                    dest.right = rightNode;
                    dest.size = head.size;
                } else {
                    //表示此后继结点就是 rightNode
                    dest.left = leftNode;
                    dest.size += leftNode.size;
                }
                head = dest;
            } else if (leftNode != null) {
                head = leftNode;
            } else if (rightNode != null) {
                head = rightNode;
            }
        }
        return head;
    }

    /**
     * 找到cur所在树中按序排列第kth个结点
     *
     * @param cur
     * @param kth
     * @return
     */
    private Node<K, V> getIndex(Node<K, V> cur, int kth) {
        if (cur == null || kth < 1) {
            return null;
        }

        //当前cur结点所处的下标
        int curIndex = (cur.left != null ? cur.left.size : 0) + 1;
        if (kth == curIndex) {
            return cur;
        } else if (kth < curIndex) {
            return getIndex(cur.left, kth);
        } else {
            return getIndex(cur.right, kth - curIndex);
        }
    }

    /**
     * 找到key所在的节点，如果key不存在，返回key插入时的父节点
     *
     * @param key
     * @return
     */
    private Node<K, V> findLastIndex(K key) {
        if (key == null) {
            return null;
        }
        Node<K, V> pre = root;
        Node<K, V> cur = root;

        while (cur != null) {
            pre = cur;
            if (cur.key.compareTo(key) == 0) {
                break;
            } else if (cur.key.compareTo(key) > 0) {
                cur = cur.left;
            } else {
                cur = cur.right;
            }
        }
        return pre;
    }

    /**
     * 返回最接近key且不小于（大于等于）key的节点
     *
     * @param key
     * @return
     */
    private Node<K, V> findLastNoSmallIndex(K key) {
        if (key == null) {
            return null;
        }
        Node<K, V> ans = null;
        Node<K, V> cur = root;
        while (cur != null) {
            if (cur.key.compareTo(key) == 0) {
                ans = cur;
                break;
            } else if (cur.key.compareTo(key) > 0) {
                ans = cur;
                cur = cur.left;
            } else {
                cur = cur.right;
            }
        }
        return ans;
    }

    /**
     * 返回最接近key且不大于(小于等于)key的节点
     *
     * @param key
     * @return
     */
    private Node<K, V> findLastNoBigIndex(K key) {
        if (key == null) {
            return null;
        }
        Node<K, V> ans = null;
        Node<K, V> cur = root;

        while (cur != null) {
            if (cur.key.compareTo(key) == 0) {
                ans = cur;
                break;
            } else if (cur.key.compareTo(key) < 0) {
                ans = cur;
                cur = cur.right;
            } else {
                cur = cur.left;
            }
        }
        return ans;
    }

    public int size() {
        return root != null ? root.size : 0;
    }

    public boolean containsKey(K key) {
        if (key == null) {
            return false;
        }
        Node<K, V> lastIndex = findLastIndex(key);
        return lastIndex != null && lastIndex.key.compareTo(key) == 0;
    }

    public void put(K key, V value) {
        if (key == null) {
            return;
        }
        Node<K, V> lastIndex = findLastIndex(key);
        if (lastIndex != null && lastIndex.key.compareTo(key) == 0) {
            //加入前，先查看一下是否已经存在这个key
            lastIndex.value = value;
        } else {
            root = add(root, key, value);
        }
    }

    public void remove(K key) {
        if (key == null) {
            return;
        }
        if (containsKey(key)) {
            root = delete(root, key);
        }
    }

    /**
     * 获取对应key的value
     *
     * @param key
     * @return
     */
    public V get(K key) {
        if (key == null) {
            return null;
        }
        Node<K, V> lastIndex = findLastIndex(key);
        //找到了key才返回对应的value，其余情况全部返回null
        if (lastIndex != null && lastIndex.key.compareTo(key) == 0) {
            return lastIndex.value;
        }
        return null;
    }

    /**
     * @return 序列表第一个节点的key
     */
    public K firstKey() {
        if (root == null) {
            return null;
        }
        Node<K, V> cur = root;
        //最左边的那个节点就是第一个节点
        while (cur.left != null) {
            cur = cur.left;
        }
        return cur.key;
    }

    /**
     * @return 序列表最后一个节点的key
     */
    public K lastKey() {
        if (root == null) {
            return null;
        }
        Node<K, V> cur = root;
        //最右边的那个节点就是最后一个节点
        while (cur.right != null) {
            cur = cur.right;
        }
        return cur.key;
    }

    /**
     * @param key
     * @return 最接近于key且不大于key的key
     */
    public K floorKey(K key) {
        if (key == null) {
            return null;
        }
        Node<K, V> lastNoBigIndex = findLastNoBigIndex(key);
        return lastNoBigIndex != null ? lastNoBigIndex.key : null;
    }

    /**
     * @param key
     * @return 最接近于key且不小于key的key
     */
    public K ceilingKey(K key) {
        if (key == null) {
            return null;
        }
        Node<K, V> lastNoSmallIndex = findLastNoSmallIndex(key);
        return lastNoSmallIndex != null ? lastNoSmallIndex.key : null;
    }

}
