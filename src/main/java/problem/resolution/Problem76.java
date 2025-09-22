package problem.resolution;

import java.lang.reflect.Array;
import java.util.Arrays;

public class Problem76 extends LeetCodeProblem{

    public String minWindow(String s, String t) {

        int[] indexMap = getIndexMapForStr(t);

        int length = s.length();
        int lp = 0, rp = 0;
        int tempLp = 0, tempRp = 0, tempMatchCount = 0;

        int[] tempMap = new int[128];

        while(rp < length && lp < rp) {

            char c = s.charAt(rp);
            int charCount = indexMap[c];
            int tempCount = tempMap[c];
            
            if (charCount > 0 && tempCount <= charCount) {
                tempMatchCount++;
                tempMap[c] = tempCount + 1;
                if (tempMatchCount == length) {
                    // 如果匹配成功，得记录双指针位置（同时得判断间隔，如果比当前的小，就更新最小区段下标），并移动左指针（还得执行else的移动逻辑？）
                }
                rp++;
            } else {
                rp++;
                char outChar = s.charAt(lp);
                int tempOutChar = tempMap[outChar];
                if (tempOutChar > 0) {
                    tempMap[outChar] = tempOutChar - 1;
                    tempMatchCount--;
                }
                lp++;
            }

        }

        return ""; // tempLp和tempRp得出最小区段
    }

    private int[] getIndexMapForStr(String t) {
        int[] indexMapForT = new int[128];

        char[] charArray = t.toCharArray();
        int length = charArray.length;

        for (int i = 0; i < length; i++) {

            indexMapForT[charArray[i]] += 1;
        }

        return indexMapForT;
    }

    @Override
    public void run() {
        String s = "ADOBECODEBANC", t = "ABC";
        minWindow(s, t);


        int[] indexMapForT = new int[59];

        char[] charArray = t.toCharArray();
        int length = charArray.length;

        for (int i = 0; i < length; i++) {

            indexMapForT[t.charAt(i) - 65] += 1;
        }

        System.out.println(Arrays.toString(indexMapForT));
    }

    @Override
    protected String getTitle() {
        return "最小覆盖子串";
    }

    @Override
    protected Difficulty getDifficulty() {
        return Difficulty.HARD;
    }
}
