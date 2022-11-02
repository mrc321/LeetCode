package zuo.class2;

import java.util.Arrays;
import java.util.HashMap;

public class Test {
    public static void main(String[] args) {
        //3 3 4 9
        int[] arr = {4,2,8,3,3};
        RadixSort.radixSort(arr);
        System.out.println(Arrays.toString(arr));
    }
}
