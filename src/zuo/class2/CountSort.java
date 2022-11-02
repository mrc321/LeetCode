package zuo.class2;

import java.util.ArrayList;
import java.util.Arrays;

public class CountSort {
    public static void main(String[] args) {
    }

    public void countSort(int[] arr) {
        if (arr == null || arr.length < 2) {
            return;
        }
        //找出该数组中最大的数
        int maxNum = Integer.MIN_VALUE;
        for (int i : arr) {
            maxNum = Math.max(i, maxNum);
        }
        //创建桶数组用于计数
        int[] bucket = new int[maxNum + 1];
        for (int i : arr) {
            bucket[i]++;
        }
        //开始创建数组
        int index = 0;
        for (int i = 0; i < bucket.length; i++) {
            //表示此时有 bucket[i] 个 i 数需要加入到数组中
            while (bucket[i]-- > 0) {
                arr[index++] = i;
            }
        }
    }

    public static void compare(int[] arr) {
        Arrays.sort(arr);
    }

    /**
     * 自动生成随机数组
     * @param maxSize 数组最大长度
     * @param maxValue 数组最大值
     * @return 生成的数组
     */
//    public static int[] generateRandomArray(int maxSize, int maxValue){
//
//    }
}
