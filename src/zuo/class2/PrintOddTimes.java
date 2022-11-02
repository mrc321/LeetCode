package zuo.class2;

public class PrintOddTimes {
    public static void printOddTimes(int[] nums){
        int eor = 0;
        for (int num : nums) {
            eor ^= num;
        }

        int rightOne = eor & -eor; //提取出最右的1
        int a = eor;
        for (int num : nums) {
            if ((num & rightOne) != 0) {
                a ^= num;
            }
        }
        int b = eor ^ a;
        System.out.println(a + ":" + b);
    }

    public static void main(String[] args) {
        int[] arr = {1,2,3,4,8,1,2,3,4,9};
        printOddTimes(arr);
    }
}
