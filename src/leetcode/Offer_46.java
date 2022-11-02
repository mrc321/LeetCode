package leetcode;

public class Offer_46 {
    public static void main(String[] args) {
        System.out.println(translateNum(200000));
        System.out.println(translateNumDp(200000));
    }

    public static int translateNum(int num) {
        int[] numArray = toArray(num);
        return process(numArray, 0);
    }

    private static int process(int[] numArray, int index) {
        if (index == numArray.length) {
            return 1;
        }

        //转一个位
        int ways = process(numArray, index + 1);
        //转两个位
        //转两个数字时不能第一个数字不能为0，且两位数不能大于25
        if (numArray[index] != 0 && index + 1 < numArray.length && (numArray[index] * 10 + numArray[index + 1]) < 26) {
            ways += process(numArray, index + 2);
        }
        return ways;
    }

    public static int translateNumDp(int num) {
        int[] numArray = toArray(num);
        int N = numArray.length;
        int[] table = new int[N + 1];
        table[N] = 1;
        for (int index = N - 1; index >= 0; index--) {
            int ways = table[index + 1];
            //转两个数字时不能第一个数字不能为0，且两位数不能大于25
            if (numArray[index] != 0 && index + 1 < N && (numArray[index] * 10 + numArray[index + 1]) < 26) {
                ways += table[index + 2];
            }
            table[index] = ways;
        }
        return table[0];
    }


    private static int[] toArray(int num) {
        int length = 1;
        int tmp = num;
        while ((tmp /= 10) != 0) {
            length++;
        }
        int[] array = new int[length];
        for (int i = length - 1; i >= 0; i--) {
            array[i] = num % 10;
            num /= 10;
        }
        return array;
    }
}
