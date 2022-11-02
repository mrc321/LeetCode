package zuo.class2;

public class OnlyKTimes {
    public static int onlyTimes(int[] nums, int k, int m) {
        byte[] bits = new byte[32];// 用于存放每一个数bit位为1的数
        for (int num : nums) {
            for (int i = 0; i < 32; i++) {
                bits[i] += (num >> i) & 1; //对每一个数的每一个位进行判别
            }
        }
        int ans = 0; //用来存放出现了k次的数
        for (int i = 0; i < bits.length; i++) {
            if (bits[i] % m == 0) continue; //这一位对m取模为0，说名出现k次的数在这一位不为1
            if (bits[i] % m == k) ans |= (1 << i);
            else return -1;
        }
        if (ans != 0) return ans;
        //如果ans = 0，上面的循环无法判别，因此还需要再次循环一次看0是否出现k次
        int count = 0;
        for (int num : nums) {
            if (num == 0) count++; //统计0出现了多少次
        }
        return count == k ? 0 : -1;
    }

    public static void main(String[] args) {
        int[] arr = {1,1,1,2,2,2,3,3,3,0,0,0};
        System.out.println(onlyTimes(arr, 2, 3));
    }
}
