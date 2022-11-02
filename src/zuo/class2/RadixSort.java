package zuo.class2;

import java.util.Arrays;

/**
 * @author lab307
 */
public class RadixSort {

    /**
     * 取出数组中最大数的位数
     * @param arr
     * @return
     */
    public static int maxBits(int[] arr) {
        int max = Integer.MIN_VALUE;
        for (int num : arr) {
            max = Math.max(max, num);
        }
        int bits = 0;
        while (max != 0) {
            bits++;
            max /= 10;
        }
        return bits;
    }

    /**
     * 获取指定数的position位的单个数
     * @param number
     * @param position
     * @return
     */
    public static int getDigit(int number, int position) {
        return (number / (int) (Math.pow(10, position - 1))) % 10;
    }

    public static void radixSort(int[] arr) {
        final int radix = 10;
        //最大值的十进制位数digit
        int maxDigit = maxBits(arr);
        // 有多少个数准备多少个辅助空间
        int[] help = new int[arr.length];
        //从个位开始一直遍历到每个数的maxDigit位
        for (int i = 1; i <= maxDigit; i++) {
            //用于存放 0-9 这 10 个数出现的次数
            int[] count = new int[radix];
            //统计每个数第 i 位的数出现的次数
            for (int num : arr) {
                count[getDigit(num, i)]++;
            }
            //循环完毕后count[j]表示有count[j]个在i位小于等于j的数
            for (int j = 1; j < count.length; j++) {
                count[j] += count[j - 1];
            }
            for (int r = arr.length - 1; r >= 0; r--) {
                int digit = getDigit(arr[r], i);
                help[--count[digit]] = arr[r];
            }
            for (int l = 0; l < arr.length; l++) {
                arr[l] = help[l];
            }
        }
    }

    // for test
    public static void comparator(int[] arr) {
        Arrays.sort(arr);
    }

    // for test
    public static int[] generateRandomArray(int maxSize, int maxValue) {
        int[] arr = new int[(int) ((maxSize + 1) * Math.random())];
        for (int i = 0; i < arr.length; i++) {
            arr[i] = (int) ((maxValue + 1) * Math.random());
        }
        return arr;
    }

    // for test
    public static int[] copyArray(int[] arr) {
        if (arr == null) {
            return null;
        }
        int[] res = new int[arr.length];
        for (int i = 0; i < arr.length; i++) {
            res[i] = arr[i];
        }
        return res;
    }

    // for test
    public static boolean isEqual(int[] arr1, int[] arr2) {
        if ((arr1 == null && arr2 != null) || (arr1 != null && arr2 == null)) {
            return false;
        }
        if (arr1 == null && arr2 == null) {
            return true;
        }
        if (arr1.length != arr2.length) {
            return false;
        }
        for (int i = 0; i < arr1.length; i++) {
            if (arr1[i] != arr2[i]) {
                return false;
            }
        }
        return true;
    }

    // for test
    public static void printArray(int[] arr) {
        if (arr == null) {
            return;
        }
        for (int i = 0; i < arr.length; i++) {
            System.out.print(arr[i] + " ");
        }
        System.out.println();
    }

    // for test
    public static void main(String[] args) {
        int testTime = 500000;
        int maxSize = 10;
        int maxValue = 10;
        boolean succeed = true;
        for (int i = 0; i < testTime; i++) {
            int[] arr1 = generateRandomArray(maxSize, maxValue);
            int[] arr2 = copyArray(arr1);
            int[] tmp = arr1;
            radixSort(arr1);
            comparator(arr2);
            if (!isEqual(arr1, arr2)) {
                succeed = false;
                printArray(tmp);
                printArray(arr1);
                printArray(arr2);
                break;
            }
        }
        System.out.println(succeed ? "Nice!" : "Fucking fucked!");
    }
}
