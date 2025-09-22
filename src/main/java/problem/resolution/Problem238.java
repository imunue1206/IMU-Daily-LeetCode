package problem.resolution;

import java.util.Arrays;

public class Problem238 extends LeetCodeProblem {

    /**
     * 给你一个整数数组 nums，返回 数组 answer ，其中 answer[i] 等于 nums 中除 nums[i] 之外其余各元素的乘积 。
     * 题目数据 保证 数组 nums之中任意元素的全部前缀元素和后缀的乘积都在  32 位 整数范围内。
     * 请 不要使用除法，且在 O(n) 时间复杂度内完成此题。
     *
     * @param nums
     * @return
     */
    public int[] productExceptSelf(int[] nums) {

        int len = nums.length;

        // 定义两个计算结果存储列表

        int[] suffix = new int[len];
        int lastIndex = len - 1;
        suffix[lastIndex] = nums[lastIndex];

        for (int i = len - 2; i >= 0; i--) {
            suffix[i] = suffix[i + 1] * nums[i];
        }

        int pre = 1;

        for (int i = 0; i < len; i++) {
            int sufIndex = i + 1;

            if (i >= 1) {
                pre *= nums[i - 1];
            }
            int res = pre;

            if (sufIndex < len) {
                res *= suffix[sufIndex];
            }

            suffix[i] = res;
        }

        return suffix;
    }

    public int[] productExceptSelf2(int[] nums) {

        int len = nums.length - 1;

        // this is for suffix calculate cache
        int[] suffix = new int[len + 1];
        suffix[len] = nums[len];
        for (int i = len - 1; i >= 0; i--) {
            suffix[i] = suffix[i + 1] * nums[i];
        }

        int[] res = new int[len + 1];

        int preRes = 1;
        for (int i = 0; i <= len; i++) {

            int tmp = 1;

            int preIndex = i - 1;
            if (preIndex >= 0) {
                preRes *= nums[preIndex];
                tmp = preRes;
            }

            int sufIndex = i + 1;
            if (sufIndex <= len) tmp *= suffix[sufIndex];

            res[i] = tmp;
        }

        return res;
    }

    @Override
    public void run() {

        int[] nums = {1,2,0,3,4};

        System.out.println("origin array:" + Arrays.toString(nums));
        System.out.println(Arrays.toString(productExceptSelf2(nums)));
    }

    @Override
    protected String getTitle() {
        return "除自身以外数组的乘积";
    }

    @Override
    protected Difficulty getDifficulty() {
        return Difficulty.MEDIUM;
    }
}
