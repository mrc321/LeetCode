package leetcode;

public class Leetcode_410 {
    public static void main(String[] args) {
        int[] nums = {4, 1, 10, 20, 1, 3, 6, 8, 69, 1, 56, 4, 9, 7};
        int k = 6;
        System.out.println(splitArray_(nums, k));
        System.out.println(splitArray_last(nums, k));
    }

    public static int splitArray(int[] nums, int k) {
        int[] preSumArray = new int[nums.length + 1];
        preSumArray[0] = 0;
        for (int i = 0; i < nums.length; i++) {
            preSumArray[i + 1] = preSumArray[i] + nums[i];
        }
        return process(nums, nums.length, k, preSumArray);
    }

    /**
     * 将len个数字连续划分到arrayNum组中，求出所有组中的最大累加和 的最小值
     *
     * @param nums     原数组
     * @param len      从0位置开始，要划分的数字的个数
     * @param arrayNum 在此区间上，要划分成的数组的个数
     * @return
     */
    private static int process(int[] nums, int len, int arrayNum, int[] preSumArray) {
        if (arrayNum == 1) {
            return preSumArray[len];
        }
        int ans = Integer.MAX_VALUE;
        //对于len个数字，arrayNum个数组分，可以化成一下情况:
        //前len - 1个数，arrayNum-1个数组分；前len - 2个数，arrayNum-1个数组分；.... 前len - 2个数，arrayNum-1个数组分
        //且数字的个数不得低于划分成的数组的个数，因为必须保证每个组中都至少有一个数
        int up = len - 1;
        int down = arrayNum - 1;
        for (int leftEnd = up; leftEnd >= down; leftEnd--) {
            //前leftEnd个数被arrayNum - 1个数组分，最后获得到的最小累加和
            int leftMinSum = process(nums, leftEnd, arrayNum - 1, preSumArray);
            //剩下的数全部被最后一个数组收下
            int curSum = preSumArray[len] - preSumArray[leftEnd];
            ans = Math.min(Math.max(leftMinSum, curSum), ans);
        }
        return ans;
    }

    public static int splitArray_(int[] nums, int k) {
        //前缀和数组，prepreSumArray[i]，表示[0,i-1]区间上的累加和
        int[] preSumArray = new int[nums.length + 1];
        preSumArray[0] = 0;
        for (int i = 0; i < nums.length; i++) {
            preSumArray[i + 1] = preSumArray[i] + nums[i];
        }
        //第0行 和 第0列都用不上
        int[][] dp = new int[nums.length + 1][k + 1];
        //dp[i][j]表示在[0,i)区间上，划分成j个数组后，其中最大累加和的最小值
        for (int i = 0; i < nums.length; i++) {
            //第一列相当于只有一个数组，因此直接就是累加和
            dp[i + 1][1] = dp[i][1] + nums[i];
        }
        //arrayNum表示要将number个数划分成arrayNum组
        //从第二列开始计算
        for (int arrayNum = 2; arrayNum <= k; arrayNum++) {
            //len表示当前从0开头的区间内中数字的总个数
            //为了让每个划分的数组至少都有一个数，因此数字的个数不小于划分数组的个数
            for (int len = nums.length; len >= arrayNum; len--) {
                int ans = Integer.MAX_VALUE;
                int up = len - 1;
                int down = arrayNum - 1;
                //leftEnd是划给前面 arrayNum - 1 个数组的数字个数，len - leftEnd是留给最后一个数组的数字个数
                for (int leftEnd = up; leftEnd >= down; leftEnd--) {
                    //这是最后一个数组的累加和
                    int curArraySum = preSumArray[len] - preSumArray[leftEnd];
                    ans = Math.min(Math.max(dp[leftEnd][arrayNum - 1], curArraySum), ans);
                }
                dp[len][arrayNum] = ans;
            }
        }
        return dp[nums.length][k];
    }

    public static int splitArray_2(int[] nums, int k) {
        //前缀和数组，prepreSumArray[i]，表示[0,i-1]区间上的累加和
        int[] preSumArray = new int[nums.length + 1];
        preSumArray[0] = 0;
        for (int i = 0; i < nums.length; i++) {
            preSumArray[i + 1] = preSumArray[i] + nums[i];
        }

        int[][] dp = new int[nums.length + 1][k + 1];
        int[][] bestEnd = new int[nums.length + 1][k + 1];
        //dp[i][j]表示在[0,i)区间上，j个数组中最大累加和的最小值
        for (int i = 0; i < nums.length; i++) {
            dp[i + 1][1] = dp[i][1] + nums[i];
            bestEnd[i + 1][1] = -1;
        }
        //arrayNum表示要将number个数划分成arrayNum组
        for (int arrayNum = 2; arrayNum <= k; arrayNum++) {
            //len表示当前从0开头的区间内中数字的总个数
            //为了让每个划分的数组至少都有一个数，因此数字的个数不小于划分数组的个数
            for (int len = nums.length; len >= arrayNum; len--) {
                int ans = Integer.MAX_VALUE;

                int down = Math.max(arrayNum - 1, bestEnd[len][arrayNum - 1]);
                int up = len == nums.length ? len - 1 : bestEnd[len + 1][arrayNum];

                int bestChoice = 0;
                //leftEnd是划给前面 arrayNum - 1 个数组的数字个数，len - leftEnd是留给最后一个数组的数字个数
                for (int leftEnd = up; leftEnd >= down; leftEnd--) {
//                for (int leftEnd = down; leftEnd <= up; leftEnd++) {
                    //这是最后一个数组的累加和
                    int curArraySum = preSumArray[len] - preSumArray[leftEnd];
                    int isAns = Math.max(dp[leftEnd][arrayNum - 1], curArraySum);
                    if (isAns <= ans) {
                        ans = isAns;
                        bestChoice = leftEnd;
                    }
                }
                dp[len][arrayNum] = ans;
                bestEnd[len][arrayNum] = bestChoice;
            }
        }
        return dp[nums.length][k];
    }

    public static int splitArray_last(int[] nums, int k) {
        //求整个数组的累加和
        int allSum = 0;
        for (int i = 0; i < nums.length; i++) {
            allSum += nums[i];
        }
        int L = 0;
        //求整个数组的累加和并且作为右边界
        int R = allSum;
        while (L <= R) {
            int mid = L + (R - L) / 2;
            //获取如果取mid作为V的话，看看能分成多少个子数组
            int result = getM(nums, mid);

            if (result > k) {
                //如果大于K，说明V太小了，需要让V大一点
                L = mid + 1;
            } else {
                //如果小于等于K，说明V太大了，需要让V小一点
                R = mid - 1;
            }
        }
        return L;
    }

    private static int getM(int[] nums, int mostLessSum) {
        int sum = 0;
        int count = 1;
        for (int i = 0; i < nums.length; i++) {
            //如果单个数就已经超过了mostLessSum，直接返回最大值，表示需要提高V的值
            if (nums[i] > mostLessSum) {
                return Integer.MAX_VALUE;
            }
            //每次加之前先判断，当前数组的累加和是否超过了
            if (sum + nums[i] > mostLessSum) {
                sum = 0;
                count++;
            }
            sum += nums[i];
        }
        return count;
    }
}
