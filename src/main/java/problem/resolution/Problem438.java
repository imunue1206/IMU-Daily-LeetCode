package problem.resolution;

import java.util.*;

public class Problem438 extends LeetCodeProblem{

    public List<Integer> findAnagrams(final String s, final String p) {

        int sLen = s.length(), pLen = p.length();

        if (sLen < pLen) {
            return new ArrayList<>();
        }

        List<Integer> ans = new ArrayList<>();
        int[] sCount = new int[123];
        int[] pCount = new int[123];

        for (int i = 0; i < pLen; ++i) { // 相当于初始化异位词
            ++pCount[p.charAt(i)];
            ++sCount[s.charAt(i)];
        }

        if (Arrays.equals(pCount, sCount)) {
            ans.add(0);
        }

        for (int i = 0; i < sLen - pLen; i++) { // 也就是左指针的探寻范围

            // 移动窗口, 但此处会丢失第一次的窗口，所以在上方去做这件事情
            --sCount[s.charAt(i)]; // 移动左指针
            ++sCount[s.charAt(i + pLen)]; // 移动右指针

            if (Arrays.equals(pCount, sCount)) {
                ans.add(i + 1); // 因为上方的移动窗口操作是下一步的
            }
        }

        return ans;
    }


    @Override
    public void run() {
        String s = "cbaebabacd";
        String p = "abc";

        System.out.println(findAnagrams(s, p));
    }

    @Override
    protected String getTitle() {

        return "找到字符串中所有字母异位词";
    }

    @Override
    protected Difficulty getDifficulty() {
        return Difficulty.MEDIUM;
    }
}
