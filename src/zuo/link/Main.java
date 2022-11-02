package zuo.link;

import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        Scanner in = new Scanner(System.in);
        int n = in.nextInt();
        int pivot = in.nextInt();

        ListNode head = inputLink(n,in);
        printNode(head);
        printNode(head);
    }

    public boolean isPalindrome(ListNode head) {
        if (head == null) {
            return false;
        }
        //1、找到中点
        ListNode fast = head;
        ListNode slow = head;
        while (fast.next != null && fast.next.next != null){
            slow = slow.next;
            fast = fast.next.next;
        }
        ListNode mid = slow;
        //2、反转后半部分
        ListNode pre = mid;
        ListNode cur = mid.next;
        ListNode next = null;
        while (cur != null){
            next = cur.next;
            cur.next = pre;
            pre = cur;
            cur = next;
        }
        //3、开始一一遍历比较
        //后半部分的头指针
        cur = pre;
        while (cur != mid){
            if (cur.val != head.val){
                return false;
            }
            cur = cur.next;
            head = head.next;
        }
        return true;
    }

    public ListNode reverseList(ListNode head) {
        //反转链表
        ListNode pre = null;
        ListNode next = null;
        while (head != null){
            //记录下一个结点
            next = head.next;
            //让当前结点指向前一个结点
            head.next = pre;
            //两个指针同时向后移动
            pre = head;
            head = next;
        }
        return pre;
    }

    private static void arrPartition(ListNode[] arr, int pivot) {
        int left = -1;
        int right = arr.length;
        int index = 0;
        while (index < right) {
            if (arr[index].val < pivot) {
                swap(arr, ++left, index++);
            } else if (arr[index].val > pivot) {
                swap(arr, --right, index);
            } else {
                index++;
            }
        }
    }

    private static void swap(ListNode[] arr, int i, int j) {
        ListNode tmp = arr[i];
        arr[i] = arr[j];
        arr[j] = tmp;
    }

    public static void printNode(ListNode head) {
        while (head != null) {
            System.out.print(head.val + " ");
            head = head.next;
        }
        System.out.println();
    }

    public static ListNode inputLink(int n,Scanner in){
        ListNode head = null;
        ListNode p = null;
        for (int i = 0; i < n; i++) {
            ListNode listNode = new ListNode(in.nextInt());
            if (i == 0) {
                head = listNode;
                p = listNode;
            } else {
                p.next = listNode;
                p = p.next;
            }
        }
        return head;
    }
}
class ListNode {
    public int val;
    public ListNode next;

    public ListNode() {
    }

    public ListNode(int val) {
        this.val = val;
    }

    public ListNode(int val, ListNode next) {
        this.val = val;
        this.next = next;
    }
}
