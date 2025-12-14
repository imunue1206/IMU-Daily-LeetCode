package problem.resolution;

public class Problem11 extends LeetCodeProblem{

    /**
     * 给定一个长度为 n 的整数数组 height 。有 n 条垂线，第 i 条线的两个端点是 (i, 0) 和 (i, height[i]) 。
     *
     * 找出其中的两条线，使得它们与 x 轴共同构成的容器可以容纳最多的水。
     *
     * 返回容器可以储存的最大水量。
     *
     * 说明：你不能倾斜容器。
     * @param height
     * @return
     */
    public int maxArea(int[] height) {

        int left = 0, right = height.length - 1;
        int bestArea = 0;

        while (left < right) {
            int currentLeftHeight = height[left];
            int currentRightHeight = height[right];
            int currentArea  = (right - left) * Math.min(currentLeftHeight, currentRightHeight);

            if (currentArea > bestArea) {
                bestArea = currentArea;
            } else {
                if (currentLeftHeight < currentRightHeight) left ++;
                else right--;
            }
        }

        return bestArea;
    }

    @Override
    public String note() {

        return """
                1.感觉可以直接双指针秒杀啊
                就是分界，左右两侧寻找各自最高的，然后再对比中间边界，
                但是这样是有问题的，问题在于，如果这个边界就是在左右怎么办？，所以这个还是要老老实实头尾双指针来一遍，
                很简单，就是计算最好结果，一直寻找就可以了
                
                通过了，3ms 94%
                但是我感觉这个题是不是过于简单了？这个不就是获取 间隔尽量最远，同时两边尽量最高？
                """;
    }

    @Override
    public void run() {
        int height[] = new int[]{1,8,6,2,5,4,8,3,7};
        int i = maxArea(height);
        System.out.println(i);
    }

    @Override
    protected String getTitle() {
        return "盛最多水的容器";
    }

    @Override
    protected Difficulty getDifficulty() {
        return Difficulty.MEDIUM;
    }
}
