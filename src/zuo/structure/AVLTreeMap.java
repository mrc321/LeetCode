package zuo.structure;

public class AVLTreeMap<K extends Comparable<K>, V> {
    /**
     * AVL树中的节点，每个节点包含key和value，且key的类型必须是可比较的
     *
     * @param <K> key类型
     * @param <V> value类型
     */
    private static class Node<K extends Comparable<K>, V> {
        public K key;
        public V value;
        public Node<K, V> left;
        public Node<K, V> right;
        public int hight;

        public Node(K key, V value) {
            this.key = key;
            this.value = value;
            this.hight = 1;
        }
    }

    private Node<K, V> root;
    private int size;

    /**
     * 右旋以head为头节点的树
     *
     * @param head
     * @return 返回新的头节点
     */
    private Node<K, V> rightRotate(Node<K, V> head) {
        //head的左孩子做新的头节点
        Node<K, V> newHead = head.left;
        //右旋
        head.left = newHead.right;
        newHead.right = head;
        //调整newHead和head的高度：选出左右两侧的最大高度再加 1
        head.hight = Math.max(head.left != null ? head.left.hight : 0, head.right != null ? head.right.hight : 0) + 1;
        newHead.hight = Math.max(newHead.left != null ? newHead.left.hight : 0, head.hight) + 1;
        return newHead;
    }

    /**
     * 左旋以head为头节点的树
     *
     * @param head
     * @return 返回新的头节点
     */
    private Node<K, V> leftRotate(Node<K, V> head) {
        Node<K, V> newHead = head.right;

        head.right = newHead.left;
        newHead.left = head;

        head.hight = Math.max(head.left != null ? head.left.hight : 0, head.right != null ? head.right.hight : 0) + 1;
        newHead.hight = Math.max(head.hight, newHead.right != null ? newHead.right.hight : 0) + 1;
        return newHead;
    }

    /**
     * 检查以head为头节点的树是否已经失衡，如果失衡了则做相应的调整
     *
     * @param head
     * @return 调整后新的头节点
     */
    private Node<K, V> maintain(Node<K, V> head) {
        if (head == null) {
            return null;
        }
        Node<K, V> newHead = head;

        //取出head树的左右两侧子树的高度
        int leftHight = head.left != null ? head.left.hight : 0;
        int rightHight = head.right != null ? head.right.hight : 0;

        //如果左右两侧的高度差超过1，则失衡，进而做出调整
        if (Math.abs(leftHight - rightHight) > 1) {
            //取出树头节点的左，右孩子节点
            Node<K, V> leftChild = head.left;
            Node<K, V> rightChild = head.right;
            //判断类型
            if (leftHight > rightHight) {
                //LL或LR
                int leftLeftHight = leftChild.left != null ? leftChild.left.hight : 0;
                int leftRightHight = leftChild.right != null ? leftChild.right.hight : 0;
                if (leftLeftHight >= leftRightHight) {
                    //LL型或者同时是LL型和LR型：做一次整体右旋调整即可
                    newHead = rightRotate(head);
                } else {
                    //LR型：先做一次局部左旋，再做一次整体右旋
                    head.left = leftRotate(leftChild);
                    newHead = rightRotate(head);
                }
            } else {
                //RR或RL
                int rightLeftHight = rightChild.left != null ? rightChild.left.hight : 0;
                int rightRightHight = rightChild.right != null ? rightChild.right.hight : 0;
                if (rightRightHight >= rightLeftHight) {
                    //RR型或同时是RL和RR型：做一次整体左旋
                    newHead = leftRotate(head);
                } else {
                    //RL型：先局部右旋，再整体左旋
                    head.right = rightRotate(rightChild);
                    newHead = leftRotate(head);
                }
            }
        }
        return newHead;
    }

    /**
     * 往以head为头的树中添加新节点
     *
     * @param head
     * @param key
     * @param value
     * @return
     */
    private Node<K, V> add(Node<K, V> head, K key, V value) {
        //判空，如果为空，直接创建节点
        if (head == null) {
            return new Node<>(key, value);
        }
        //无需判断key相等的情况，因为上游调用时已经处理了相等的情况
        if (head.key.compareTo(key) > 0) {
            //key比头节点的key小，则往左子树中加入
            head.left = add(head.left, key, value);
        } else {
            //key比头节点的key大，则往右子树中加入
            head.right = add(head.right, key, value);
        }
        //添加成功后，调整head的高度
        head.hight = Math.max(head.left != null ? head.left.hight : 0, head.right != null ? head.right.hight : 0) + 1;
        //最后维护head树的平衡性
        return maintain(head);
    }

    /**
     * 在head这颗树上，删掉key为key的节点
     *
     * @param head
     * @param key
     * @return
     */
    private Node<K, V> delete(Node<K, V> head, K key) {
        if (head == null) {
            return null;
        }
        //先提取出head的左、右孩子节点
        Node<K, V> leftChild = head.left;
        Node<K, V> rightChild = head.right;

        if (head.key.compareTo(key) > 0) {
            //key在左子树
            head.left = delete(leftChild, key);
        } else if (head.key.compareTo(key) < 0) {
            //key在右子树
            head.right = delete(rightChild, key);
        } else if (head.key.compareTo(key) == 0) {
            //要删除的就是head节点
            if (leftChild == null && rightChild == null) {
                //情况1：key所在节点head没有左右子树，则直接删除即可
                return null;
            } else if (leftChild != null && rightChild == null) {
                //情况2：key所在节点head只有其中一个子树，则直接使用该子树的头节点顶替head即可
                head = leftChild;
            } else if (leftChild == null && rightChild != null) {
                head = rightChild;
            } else if (leftChild != null && rightChild != null) {
                //情况3：key所在节点head左右子树都存在，则需要使用head的后继节点来顶替head，并且要将后继节点从原位置删除
                //找到后继节点
                Node<K, V> target = rightChild;

                while (target.left != null) {
                    target = target.left;
                }
                //找到后继节点后，要将该后继节点从rightChild这颗树中删除掉
                rightChild = delete(rightChild, target.key);
                target.left = leftChild;
                target.right = rightChild;
                head = target;
            }
        }
        //删除完成后必须要调整当前树的高度
        head.hight = Math.max(head.left != null ? head.left.hight : 0, head.right != null ? head.right.hight : 0) + 1;
        //并且时刻保证树的平衡
        return maintain(head);
    }

    /**
     * 找到key所在的节点，如果key不存在，返回key插入时的父节点
     *
     * @param key
     * @return
     */
    private Node<K, V> findLastIndex(K key) {
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
     * 返回最后一个不小于key的节点
     *
     * @param key
     * @return
     */
    private Node<K, V> findLastNoSmallIndex(K key) {
        Node<K, V> ans = null;
        Node<K, V> cur = root;
        while (cur != null) {
            if (cur.key.compareTo(key) == 0) {
                ans = cur;
                break;
            } else if (cur.key.compareTo(key) > 0) {
                //凡是比key大的节点都使用cur记录
                ans = cur;
                cur = cur.left;
            } else {
                cur = cur.right;
            }
        }
        return ans;
    }

    /**
     * 返回最后一个不大于key的节点
     *
     * @param key
     * @return
     */
    private Node<K, V> findLastNoBigIndex(K key) {
        Node<K, V> ans = null;
        Node<K, V> cur = root;
        while (cur != null) {
            if (cur.key.compareTo(key) == 0) {
                ans = cur;
                break;
            } else if (cur.key.compareTo(key) < 0) {
                //凡是比key小的节点，都使用ans记录
                ans = cur;
                cur = cur.right;
            } else {
                cur = cur.left;
            }
        }
        return ans;
    }

    /**
     * @return 元素个数
     */
    public int size() {
        return size;
    }

    /**
     * 查看是否包含key
     *
     * @param key
     * @return True or False
     */
    public boolean containsKey(K key) {
        if (key == null) {
            return false;
        }
        Node<K, V> lastNode = findLastIndex(key);
        return lastNode != null && lastNode.key.compareTo(key) == 0;
    }

    /**
     * 往里面添加键值对
     *
     * @param key
     * @param value
     */
    public void put(K key, V value) {
        //不允许添加为null的key
        if (key == null) {
            return;
        }
        //找到key或者key应该插入位置的父节点
        Node<K, V> lastIndex = findLastIndex(key);
        //相等时，直接覆盖掉值
        if (lastIndex != null && lastIndex.key.compareTo(key) == 0) {
            lastIndex.value = value;
        } else {
            size++;
            //添加节点
            root = add(root, key, value);
        }
    }

    /**
     * 删除key
     *
     * @param key
     */
    public void remove(K key) {
        if (key == null) {
            return;
        }
        if (containsKey(key)) {
            root = delete(root, key);
            size--;
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
