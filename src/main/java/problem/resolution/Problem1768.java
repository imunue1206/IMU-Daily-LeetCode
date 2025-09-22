package problem.resolution;

public class Problem1768 extends LeetCodeProblem{

    public String mergeAlternately(String word1, String word2) {

        int len1 = word1.length();
        int len2 = word2.length();

        StringBuilder sb = new StringBuilder();

        int x = 0, y = 0;

        while(x < len1 || y <len2) {
            if (x < len1) sb.append(word1.charAt(x++));
            if (y < len2) sb.append(word2.charAt(y++));
        }

        return sb.toString();
    }

    @Override
    public void run() {

        String word1 = "lwy";
        String word2 = "a";

        System.out.println(mergeAlternately(word1, word2));
    }

    @Override
    protected String getTitle() {
        return "交替合并字符串";
    }

    @Override
    protected Difficulty getDifficulty() {
        return Difficulty.EASY;
    }
}
