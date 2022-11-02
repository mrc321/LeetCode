package zuo.utils;


import zuo.ListNode;

import java.util.Scanner;

public class linkUtil {
    public static void printNode(ListNode head) {
        while (head != null) {
            System.out.print(head.val + " ");
            head = head.next;
        }
        System.out.println();
    }

    public static ListNode generate() {
        int[] arr = {1, 2, 2, 3, 3, 1};
        ListNode head = null;
        ListNode p = null;
        for (int i = 0; i < arr.length; i++) {
            ListNode listNode = new ListNode(arr[i]);
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
