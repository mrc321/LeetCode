package leetcode;

public class Leetcode_464 {
    public static boolean canIWin(int maxChoosableInteger, int desiredTotal) {
        //题目规定的最一开始为 0 的时候先手获胜
        if (desiredTotal == 0) {
            return true;
        }
        //如果当所有值的累加和都小于desiredTotal，则先手直接返回false
        if (maxChoosableInteger * (1 + maxChoosableInteger) >> 1 < desiredTotal) {
            return false;
        }
        int[] isExist = new int[maxChoosableInteger + 1];
        return process(isExist, desiredTotal);
    }

    /**
     * @param isExist 待选的数字，如果数字已被选，则标记为-1
     * @param rest 剩余累加和
     * @return
     */
    private static boolean process(int[] isExist, int rest) {
        //如果一开始的时候，rest就小于等于0，说明当前玩家输了
        if (rest <= 0) {
            return false;
        }
        //每个数字都尝试一下，看看有没有赢的可能，并且此时运行此方法的为先手
        for (int curChoice = 1; curChoice < isExist.length; curChoice++) {
            if (isExist[curChoice] != -1) {
                //isExist[choiceNum]为-1，说明choiceNum这个数已经在前面被选走了
                isExist[curChoice] = -1;
                boolean backhand = process(isExist, rest - curChoice);
                //由于是深度优先便利，因此结果递归回来后，必须马上恢复现场，避免影响后续的深度遍历
                isExist[curChoice] = 0;
                if (!backhand) {
                    //后手输了，说明我赢了
                    return true;
                }
            }
        }
        return false;
    }


    public static boolean canIWin1(int maxChoosableInteger, int desiredTotal) {
        //题目规定的最一开始为0的时候先手获胜
        if (desiredTotal == 0) {
            return true;
        }
        if (maxChoosableInteger * (1 + maxChoosableInteger) >> 1 < desiredTotal) {
            return false;
        }
        return process(maxChoosableInteger, 0, desiredTotal);
    }

    /**
     * @param maxChoice
     * @param status    i位如果为1，说明i这个数已经被选择了
     * @param rest
     * @return
     */
    private static boolean process(int maxChoice, int status, int rest) {
        //如果一开始的时候，rest就小于等于0，说明当前玩家输了
        if (rest <= 0) {
            return false;
        }
        for (int curChoice = 1; curChoice <= maxChoice; curChoice++) {
            //首先判断当前值是否经被选择了
            if (((1 << curChoice) & status) == 0) {
                if (!process(maxChoice, status | (1 << curChoice), rest - curChoice)) {
                    return true;
                }
            }
        }
        return false;
    }

    public static boolean canIWin2(int maxChoosableInteger, int desiredTotal) {
        //题目规定的最一开始为0的时候先手获胜
        if (desiredTotal == 0) {
            return true;
        }
        if (maxChoosableInteger * (1 + maxChoosableInteger) >> 1 < desiredTotal) {
            return false;
        }
        int maxLen = 1 << (maxChoosableInteger + 1);
        //dp[i] == 0表示未选
        //dp[i] == -1 表示失败
        //dp[i] == 1  表示成功
        int[] dp = new int[maxLen];
        return process(maxChoosableInteger, 0, desiredTotal, dp);
    }

    /**
     * @param maxChoice
     * @param status    i位如果为1，说明i这个数已经被选择了
     * @param rest
     * @return
     */
    private static boolean process(int maxChoice, int status, int rest, int[] dp) {
        //如果一开始的时候，rest就小于等于0，说明当前玩家输了
        if (dp[status] != 0) {
            return dp[status] == 1 ? true : false;
        }
        if (rest > 0) {
            for (int curChoice = 1; curChoice <= maxChoice; curChoice++) {
                //首先判断当前值是否经被选择了
                if (((1 << curChoice) & status) == 0) {
                    if (!process(maxChoice, status | (1 << curChoice), rest - curChoice, dp)) {
                        dp[status] = 1;
                        return true;
                    }
                }
            }
        }
        dp[status] = -1;
        return false;
    }

    public static boolean canIWin3(int maxChoosableInteger, int desiredTotal) {
        //题目规定的最一开始为0的时候先手获胜
        if (desiredTotal == 0) {
            return true;
        }
        if (maxChoosableInteger * (1 + maxChoosableInteger) >> 1 < desiredTotal) {
            return false;
        }
        int maxLen = 1 << (maxChoosableInteger + 1);
        //dp[i] == 0表示未选
        //dp[i] == -1 表示失败
        //dp[i] == 1  表示成功
        int[] dp = new int[maxLen];
        return process(maxChoosableInteger, 0, desiredTotal, dp);
    }

    public static void main(String[] args) {
//        int maxChoosableInteger = 20;
//        int desiredTotal = 300;
//        int testTime = 1000;
//        System.out.println("start");
//        for (int i = 0; i < testTime; i++) {
//            int maxChoice = (int) (Math.random() * maxChoosableInteger) + 1;
//            int total = (int) (Math.random() * desiredTotal) + 1;
//            boolean ans1 = canIWin2(maxChoice, total);
//            boolean ans2 = canIWin3(maxChoice, total);
//            if (ans1 != ans2) {
//                System.out.println("oops!!");
//                System.out.println(maxChoice + " | " + total + ":");
//                System.out.println(ans1);
//                System.out.println(ans2);
//                break;
//            }
//        }
//        System.out.println("end");
        int maxLen = 0;
        for (int i = 1; i <= 20; i++) {
            maxLen |= (1 << i);
            //111111111111111111110
        }
        System.out.println(maxLen);
        System.out.println((1 << (20 + 1) )- 2);
    }
}
