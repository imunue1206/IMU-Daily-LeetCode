package problem.resolution;

public class Problem643 extends LeetCodeProblem {

    /**
     * 给你一个由 n 个元素组成的整数数组 nums 和一个整数 k 。
     *
     * 请你找出平均数最大且 长度为 k 的连续子数组，并输出该最大平均数。
     *
     * 任何误差小于 10-5 的答案都将被视为正确答案。
     *
     *
     *
     * 示例 1：
     *
     * 输入：nums = [1,12,-5,-6,50,3], k = 4
     * 输出：12.75
     * 解释：最大平均数 (12-5-6+50)/4 = 51/4 = 12.75
     * 示例 2：
     *
     * 输入：nums = [5], k = 1
     * 输出：5.00000
     *
     *
     * 提示：
     *
     * n == nums.length
     * 1 <= k <= n <= 105
     * -104 <= nums[i] <= 104
     *
     * @param nums
     * @param k
     * @return
     */
    public double findMaxAverage(int[] nums, int k) {

        int maxSum = 0, currentSum = 0;
        int left = 0, right = 0, lastIndex = nums.length - 1;

        while(right <= lastIndex) {
            if (right - left < k) {
                maxSum += nums[right];
                currentSum = maxSum;
                right++;
                continue;
            }
            int nextNum = nums[right];
            int lastNum = nums[left];
            currentSum -= lastNum;
            currentSum += nextNum;
            maxSum = Math.max(currentSum, maxSum);

            left++;
            right++;
        }

        return maxSum / (k * 1.0);
    }

    public String note() {
        return """
                居然一个double和int的数据类型的区别导致实际运算性能差距这么大，因为会频繁拆装箱
                拆装箱的性能损耗还是有一些的
                """;
    }

    @Override
    public void run() {

        int[] nums = {6,8,6,8,0,4,1,2,9,9};
        double maxAverage = findMaxAverage(nums, 2);
        System.out.println(maxAverage);
    }

    @Override
    protected String getTitle() {
        return "子数组最大平均数 I";
    }

    @Override
    protected Difficulty getDifficulty() {
        return Difficulty.EASY;
    }
}
