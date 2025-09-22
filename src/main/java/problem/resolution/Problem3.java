package problem.resolution;

import java.util.HashSet;

public class Problem3 extends LeetCodeProblem{

    public int lengthOfLongestSubstring(final String s) {
        int maxDiffCharSize = 0;
        int len = s.length();

        // 用于跟踪字符是否在窗口中
        int[] charIndex = new int[128]; // ASCII字符总共有128个

        int leftPoint = 0;

        for (int rightPoint = 0; rightPoint < len; rightPoint++) {
            char currentChar = s.charAt(rightPoint);

            // 更新左指针，当遇到重复字符时，移动左指针到重复字符的下一个位置
            leftPoint = Math.max(charIndex[currentChar], leftPoint);

            // 计算当前窗口的长度并更新最大长度
            maxDiffCharSize = Math.max(maxDiffCharSize, rightPoint - leftPoint + 1);

            // 更新字符的位置为其下一个索引
            charIndex[currentChar] = rightPoint + 1;  // 保证是下一个位置
        }

        return maxDiffCharSize;
    }

    @Override
    public void run() {

        int size = lengthOfLongestSubstring("ababac");

        System.out.println(size);
    }

    @Override
    protected String getTitle() {
        return "无重复字符的最长子串";
    }

    @Override
    protected Difficulty getDifficulty() {
        return Difficulty.MEDIUM;
    }
}
