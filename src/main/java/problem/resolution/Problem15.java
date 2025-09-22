package problem.resolution;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Problem15 extends LeetCodeProblem{

    public List<List<Integer>> threeSum(int[] nums) {

        Arrays.sort(nums);
        int n = nums.length;
        List<List<Integer>> ans = new ArrayList<List<Integer>>();

        // 此处的循环是用于压缩边界的
        for (int first = 0; first < n; first++) {

            // 因为是压缩边界，所以需要需要给出后面双指针的范围的求解信息

            // 防止重复
            if (first > 0 && nums[first] == nums[first - 1]) continue;

            subTwoSum(ans, nums, first, n);
        }

        return ans;
    }

    private void subTwoSum(List<List<Integer>> ans, int[] nums, int first, int n) {

        int start = first + 1;
        int end = n - 1;

        int l = start;
        int r = end;

        while (l < r) {

            if (l > start && nums[l] == nums[l - 1]) {
                l++;
                continue;
            }

            if (r < end && nums[r] == nums[r + 1]) {

                r--;
                continue;
            }

            int sum = nums[first] + nums[l] + nums[r];

            if (sum == 0) {
                ans.add(Arrays.asList(nums[first], nums[l], nums[r]));

                // 跳过重复的第二个数
                while (l < r && nums[l] == nums[l + 1]) {
                    l++;
                }

                // 跳过重复的第三个数
                while (l < r && nums[r] == nums[r - 1]) {
                    r--;
                }

                // 找到一个结果后，移动双指针
                l++;
                r--;
            } else if (sum < 0) {
                // 如果和小于0，说明需要更大的数，把l指针右移
                l++;
            } else {
                // 如果和大于0，说明需要更小的数，把r指针左移
                r--;
            }
        }
    }


    @Override
    public void run() {
        int[] nums = new int[]{-1,0,1,2,-1,-4};

        System.out.println(threeSum(nums));
    }

    @Override
    protected String getTitle() {

        return "三数之和";
    }

    @Override
    protected Difficulty getDifficulty() {

        return Difficulty.MEDIUM;
    }
}
