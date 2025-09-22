package problem.resolution;

public class Problem53 extends LeetCodeProblem {

    public int maxSubArray(int[] nums) {

        int pre = 0, max = nums[0];

        for (int num : nums) {
            pre = Math.max(num, pre + num);
            max = Math.max(max, pre);
        }

        return max;
    }

    @Override
    public void run() {

        int[] nums = new int[]{-2,1,-3,4,-1,2,1,-5,4};

        System.out.println(maxSubArray(nums));
    }

    @Override
    protected String getTitle() {
        return "最大子数组和";
    }

    @Override
    protected Difficulty getDifficulty() {
        return Difficulty.MEDIUM;
    }
}
