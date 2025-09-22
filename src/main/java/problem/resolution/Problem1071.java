package problem.resolution;

public class Problem1071 extends LeetCodeProblem{

    public String gcdOfStrings(String str1, String str2) {

        boolean hasAnswer = str1.concat(str2).equals(str2.concat(str1));

        if (hasAnswer) return str1.substring(0, gcd(str1.length(), str2.length()));
        else return "";
    }

    public int gcd(int a, int b) {
        int remainder = a % b;
        while (remainder != 0) {
            a = b;
            b = remainder;
            remainder = a % b;
        }

        return b;
    }

    @Override
    public void run() {

        String s = gcdOfStrings("abab", "ab");
        System.out.println(s);
    }

    @Override
    protected String getTitle() {
        return "字符串的最大公因子";
    }

    @Override
    protected Difficulty getDifficulty() {
        return Difficulty.EASY;
    }
}
