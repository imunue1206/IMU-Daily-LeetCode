package problem.resolution;

import java.util.ArrayList;
import java.util.List;

public class Problem605 extends LeetCodeProblem{

    /**
     * 假设有一个很长的花坛，一部分地块种植了花，另一部分却没有。可是，花不能种植在相邻的地块上，它们会争夺水源，两者都会死去。
     *
     * 给你一个整数数组 flowerbed 表示花坛，由若干 0 和 1 组成，其中 0 表示没种植花，1 表示种植了花。另有一个数 n ，能否在不打破种植规则的情况下种入 n 朵花？能则返回 true ，不能则返回 false 。
     *
     *
     *
     * 示例 1：
     *
     * 输入：flowerbed = [1,0,0,0,1], n = 1
     * 输出：true
     * 示例 2：
     *
     * 输入：flowerbed = [1,0,0,0,1], n = 2
     * 输出：false
     *
     *
     * 提示：
     *
     * 1 <= flowerbed.length <= 2 * 104
     * flowerbed[i] 为 0 或 1
     * flowerbed 中不存在相邻的两朵花
     * 0 <= n <= flowerbed.length
     */
    public boolean canPlaceFlowers(int[] flowerbed, int n) {

        int maxIndex = flowerbed.length - 1;

        if (n == 0) return true;

        if (maxIndex <= 1) {
            if (n > 1) return false;
            int count = 0;
            for (int i = 0; i <= maxIndex; i++) {
                count += flowerbed[i];
            }

            return count == 0;
        }

        for (int i = 0; i <= maxIndex; i++) {

            if (i == 0) {
                if (flowerbed[0] == 0 && flowerbed[1] == 0) {
                    flowerbed[0] = 1;
                    n--;
                }
                continue;
            }

            if (i == maxIndex) {
                if (flowerbed[maxIndex] == 0 && flowerbed[maxIndex - 1] == 0) {
                    flowerbed[maxIndex] = 1;
                    n--;
                }
                continue;
            }

            if (flowerbed[i - 1] == 0 && flowerbed[i] == 0 && flowerbed[i + 1] == 0) {
                flowerbed[i] = 1;
                n--;
            }
        }

        return n <= 0;
    }


    @Override
    public void run() {

        int[] ints = {0, 0, 0, 0};

        System.out.println(canPlaceFlowers(ints, 3));
    }

    @Override
    protected String getTitle() {

        // todo 这个不能算作是简单问题，目前的边界判断可以简化，还有贪心算法，我用的是最笨的方式，需要回过头来重新写
        return "种花问题";
    }

    @Override
    protected Difficulty getDifficulty() {
        return Difficulty.EASY;
    }
}
