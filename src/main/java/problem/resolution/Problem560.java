package problem.resolution;

import java.util.HashMap;

public class Problem560 extends LeetCodeProblem{

    public int subarraySum(int[] nums, int k) {

        int target , preSum = 0, res = 0;

        HashMap<Integer, Integer> preSumMap = new HashMap<>();
        preSumMap.put(0, 1);

        for (int curr : nums) {

            preSum += curr;
            target = preSum - k;

            if (preSumMap.containsKey(target)) res += preSumMap.get(target);

            preSumMap.put(preSum, preSumMap.getOrDefault(preSum, 0) + 1);
        }

        return res;
    }

    @Override
    public void run() {
        int[] ints = {1,3,2,1,1,1,2,-2};
        int k = 5;

        System.out.println(subarraySum(ints, k));
    }

    @Override
    protected String getTitle() {
        return "合为K的子数组";
    }

    @Override
    protected Difficulty getDifficulty() {
        return Difficulty.MEDIUM;
    }
}
