package problem.resolution;

import problem.util.PrintUtil;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Problem283 extends LeetCodeProblem{

    public void moveZeroes(int[] nums) {
        int lp = 0, rp = 0, len = nums.length;

        while (rp < len) {
            if (nums[lp] != 0) {
                lp++;
                rp = lp;
            } else if (nums[rp] != 0) {
                nums[lp] = nums[rp];
                nums[rp] = 0;
                lp++;
            } else {
                rp++;
            }
        }
    }

    @Override
    public void run() {

        int[] nums = {1,0,0,1,0,3,12,0,0};

        moveZeroes(nums);

        PrintUtil.nums(nums);
    }

    @Override
    public String note() {

        return """
                这道题，我一开始想错了，我想的是“批处理”，
                也就是先找非0的下标，然后固定
                接着，去找0的下标，且有可能0是连续的，所以还需要记录又多少的0（moveSize），直到碰到非零为止，
                然后按照这个moveSize个数，进行循环，替换
                哦，在我写这个的时候，发现其实，我一开始的思路已经很接近了
                先接着说，我发现我的第一思路有问题，因为不能循环，也就是不能用批处理去做，就是批处理本身就是错误
                所以我在想怎么做，然后，还是之前的思路
                就是先找个0，再找个非0，替换，然后左边往前移，右边不用动，
                但是左边的下一个坐标可能不是0，所以左边需要找0，找到就重复，
                我这个和官方题解还不太一样，官方题解我没能理解代码是什么意思，
                我这个效率以及可读性，逻辑清晰度，理解难易度都是上乘，所以我用我的想法解出来，且做到了1ms超过100%就够了
                """;
    }

    @Override
    protected String getTitle() {
        return "移动零";
    }

    @Override
    protected Difficulty getDifficulty() {
        return Difficulty.EASY;
    }
}
