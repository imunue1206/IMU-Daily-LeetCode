package problem.resolution;

import java.util.*;

public class Problem345 extends LeetCodeProblem{

    /**
     * 给你一个字符串 s ，仅反转字符串中的所有元音字母，并返回结果字符串。
     *
     * 元音字母包括 'a'、'e'、'i'、'o'、'u'，且可能以大小写两种形式出现不止一次。
     *
     *
     *
     * 示例 1：
     *
     * 输入：s = "IceCreAm"
     *
     * 输出："AceCreIm"
     *
     * 解释：
     *
     * s 中的元音是 ['I', 'e', 'e', 'A']。反转这些元音，s 变为 "AceCreIm".
     *
     * 示例 2：
     *
     * 输入：s = "leetcode"
     *
     * 输出："leotcede"
     *
     *
     *
     * 提示：
     *
     * 1 <= s.length <= 3 * 105
     * s 由 可打印的 ASCII 字符组成
     */
    public String reverseVowels(String s) {

        boolean[] isVowel = new boolean[128];
        for (char c : "aeiouAEIOU".toCharArray()) {
            isVowel[c] = true;
        }

        int len = s.length();
        char[] charArray = s.toCharArray();

        int l = 0, r = len - 1;
        boolean lb = false, rb = false;

        while(l < r) {
            if (isVowel[charArray[l]]) {
                lb = true;
            }
            if (isVowel[charArray[r]]) {
                rb = true;
            }
            if (lb && rb) {
                char temp = charArray[l];
                charArray[l] = charArray[r];
                charArray[r] = temp;
                lb = false;
                rb = false;
            }
            if (!lb) l++;
            if (!rb) r--;
        }

        return new String(charArray);
    }

    @Override
    public void run() {

        System.out.println(reverseVowels("IceCreAm"));
    }

    @Override
    protected String getTitle() {

        return "反转字符串中的元音字母";
    }

    @Override
    protected Difficulty getDifficulty() {
        return Difficulty.EASY;
    }
}
