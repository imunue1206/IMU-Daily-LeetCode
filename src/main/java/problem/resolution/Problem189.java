package problem.resolution;

import java.util.Arrays;

public class Problem189 extends LeetCodeProblem{

    public void rotate(int[] nums, int k) {

        int len = nums.length;
        int kk = k > len ? k % len : k;
        int[] extraArr = new int[kk];

        System.arraycopy(nums, len-kk, extraArr, 0, kk);


    }

    @Override
    public void run() {

        int[] arr = new int[]{1,2,3,4,5,6,7};

        rotate(arr, 2);

        System.out.println(Arrays.toString(arr));
    }

    @Override
    protected String getTitle() {
        return "轮转数组";
    }

    @Override
    protected Difficulty getDifficulty() {
        return Difficulty.MEDIUM ;
    }
}
