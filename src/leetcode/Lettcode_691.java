package leetcode;

import java.util.HashMap;
import java.util.Map;

public class Lettcode_691 {
    public static void main(String[] args) {
        String[] stickers = {"heavy", "claim", "seven", "set", "had", "it", "dead", "jump", "design", "question", "sugar", "dress", "any", "special", "ground", "huge", "use", "busy", "prove", "there", "lone", "window", "trip", "also", "hot", "choose", "tie", "several", "be", "that", "corn", "after", "excite", "insect", "cat", "cook", "glad", "like", "wont", "gray", "especially", "level", "when", "cover", "ocean", "try", "clean", "property", "root", "wing"};
        String target = "travelbell";
        System.out.println(new Lettcode_691().minStickers(stickers, target));
    }

    public int minStickers(String[] stickers, String target) {
        int[][] stickersCount = new int[stickers.length][26];
        //关键优化（用词频表替代贴纸数组）
        for (int i = 0; i < stickers.length; i++) {
            char[] stickerArray = stickers[i].toCharArray();
            for (char c : stickerArray) {
                stickersCount[i][c - 'a']++;
            }
        }
        Map<String, Integer> dp = new HashMap<>();
        int kind = process(stickersCount, target, dp);
        return kind == Integer.MAX_VALUE ? -1 : kind;
    }

    private int process(int[][] stickers, String target, Map<String, Integer> dp) {
        if (dp.containsKey(target)) {
            return dp.get(target);
        }
        //表示目标字符串已经被拼接完毕了
        if (target.length() == 0) {
            return 0;
        }
        //将target也转成字符表
        char[] targetArray = target.toCharArray();
        int[] targetCount = new int[26];
        for (char c : targetArray) {
            targetCount[c - 'a']++;
        }
        //min存放的是当前层，每个有效分支的使用贴纸数的最小数
        int min = Integer.MAX_VALUE;
        //每一张贴纸都作为第一个开始试
        for (int i = 0; i < stickers.length; i++) {
            int[] sticker = stickers[i];
            //如果target的第一个字符在该贴纸上有，则开始对其进行递归
            //最关键的优化（重要的剪枝），可以直接挑选出包含target字符的贴纸，而不需要逐一去尝试
            if (sticker[targetArray[0] - 'a'] > 0) {
                StringBuilder builder = new StringBuilder();
                //开始互减,并得出剩余字符串
                for (int j = 0; j < 26; j++) {
                    //这里一定要记得不能直接修改原数组的值，因为之后的遍历会用到
                    int nums = targetCount[j] - sticker[j];
                    if (nums > 0) {
                        for (int k = 0; k < nums; k++) {
                            builder.append((char) (j + 'a'));
                        }
                    }
                }
                String rest = builder.toString();
                min = Math.min(min, process(stickers, rest, dp));
            }
        }
        min += (min == Integer.MAX_VALUE ? 0 : 1);
        dp.put(target, min);
        //如果此时min仍然为Integer.MAX_VALUE,则表示所有分支都是无效的，反则该层有一个贴纸被选取
        return min;
    }


}
