package problem.resolution;

public class Problem392 extends LeetCodeProblem{

    /**
     * 给定字符串 s 和 t ，判断 s 是否为 t 的子序列。
     *
     * 字符串的一个子序列是原始字符串删除一些（也可以不删除）字符而不改变剩余字符相对位置形成的新字符串。（例如，"ace"是"abcde"的一个子序列，而"aec"不是）。
     *
     * 进阶：
     *
     * 如果有大量输入的 S，称作 S1, S2, ... , Sk 其中 k >= 10亿，你需要依次检查它们是否为 T 的子序列。在这种情况下，你会怎样改变代码？
     *
     *
     * 示例 1：
     *
     * 输入：s = "abc", t = "ahbgdc"
     * 输出：true
     * 示例 2：
     *
     * 输入：s = "axc", t = "ahbgdc"
     * 输出：false
     *
     *
     * 提示：
     *
     * 0 <= s.length <= 100
     * 0 <= t.length <= 10^4
     * 两个字符串都只由小写字符组成。
     * @param s
     * @param t
     * @return
     */
    public boolean isSubsequence(String s, String t) {
        char[] subCharArray = s.toCharArray();
        char[] targetCharArray = t.toCharArray();
        int subLength = subCharArray.length;
        int targetLength = targetCharArray.length;
        if (subLength == 0 && targetLength == 0) return true;
        if (subLength == 0 && targetLength > 0) return true;
        if (subLength > 0 && targetLength == 0) return false;
        if (subLength > targetLength) return false;

        int subIndex = 0;
        for (int i = 0; i < targetLength; i++) {
           if (targetCharArray[i] == subCharArray[subIndex]) {
               subIndex++;
           }

           if (subIndex == subLength) return true;
        }

        return false;
    }

    @Override
    public void run() {
        System.out.println(isSubsequence("abc", "ahbgdc"));
    }

    @Override
    protected String getTitle() {
        return "判断子序列";
    }

    @Override
    protected Difficulty getDifficulty() {
        return Difficulty.EASY;
    }

    @Override
    protected String note() {
        return """
                这太简单了，但是还是有很多边界条件没有考虑到，核心逻辑还是非常简单，
                就是循环就是forij这种嵌套循环
                但是，这个进阶，我在想是否能用布隆过滤器？
                
                也就是sub先插入，然后target插入，如果有碰撞，那就sub下一个插入，然后一次类推，好像，都是一样的，没有任何变化
                还没有其他头绪，
                然后我还去看了String.charAt()源码，是通过byte去做的，我不太了解，这是更底层的编码机制，这个我有空还是需要去了解一下
                """;
    }
}
