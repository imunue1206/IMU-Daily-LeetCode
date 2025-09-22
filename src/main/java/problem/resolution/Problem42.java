package problem.resolution;

import java.util.ArrayDeque;
import java.util.Deque;
import java.util.LinkedList;
import java.util.Queue;
import java.util.concurrent.CountDownLatch;

public class Problem42 extends LeetCodeProblem {

    public int trap(int[] height) {

        int len = height.length;

        Deque<Integer> stack = new ArrayDeque<>();

        int res = 0;

        for (int i = 0; i < len; i++) {

            int right = height[i];

            while (!stack.isEmpty() && right > height[stack.peek()]) {

                int mid = stack.pop();
                if (stack.isEmpty()) break;
                int left = stack.peek();

                res += (Math.min(height[left], right) - height[mid]) * (i - left - 1);
            }

            stack.push(i);
        }

        return res;
    }

    public int trap2(int[] height) {
        int n = height.length;
        if (n == 0) {
            return 0;
        }

        CountDownLatch countDownLatch = new CountDownLatch(2);

        int[] leftMax = new int[n];
        new Thread(() -> {
            leftMax[0] = height[0];
            for (int i = 1; i < n; ++i) {
                leftMax[i] = Math.max(leftMax[i - 1], height[i]);
            }
            countDownLatch.countDown();
        }).start();

        int[] rightMax = new int[n];
        new Thread(() -> {
            rightMax[n - 1] = height[n - 1];
            for (int i = n - 2; i >= 0; --i) {
                rightMax[i] = Math.max(rightMax[i + 1], height[i]);
            }
            countDownLatch.countDown();
        }).start();

        int ans = 0;
        try {
            countDownLatch.await();
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
        for (int i = 0; i < n; ++i) {
            ans += Math.min(leftMax[i], rightMax[i]) - height[i];
        }
        return ans;
    }

    public int trap3(int[] height) {

        int res = 0;
        int lm = 0, rm = 0;
        int l = 0, r = height.length - 1;

        while (l < r) {

            int lh = height[l], rh = height[r];

            lm = Math.max(lm, lh);
            rm = Math.max(rm, rh);

            if (lm < rm) res += lm - height[l++];
            else res += rm - height[r--];
        }

        return res;
    }

    @Override
    public void run() {

        int[] heights = new int[]{0,1,0,2,1,0,1,3,2,1,2,1};

        System.out.println(trap3(heights));
    }

    @Override
    protected String getTitle() {
        return "接雨水";
    }

    @Override
    protected Difficulty getDifficulty() {
        return Difficulty.HARD;
    }
}
