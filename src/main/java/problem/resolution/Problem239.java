package problem.resolution;

import java.util.*;

public class Problem239 extends LeetCodeProblem {

    public int[] maxSlidingWindow(int[] nums, int k) {

        // fast return
        int len = nums.length;
        if (len == 1 || k == 1) return nums;

        // init first window
        final PriorityQueue<int[]> windowQueue = new PriorityQueue<>(
                (Comparator<int[]>) (o1, o2) -> {
                    int i = o2[0] - o1[0];
                    if (i != 0) return i;
                    return o2[1] - o1[1];
                });

        for (int i = 0; i < k; i++) {
            windowQueue.offer(new int[]{nums[i], i});
        }

        final int[] res = new int[len + 1 - k];
        res[0] = windowQueue.peek()[0];

        for (int i = k; i < len; i++) {

            int lastIndex = i - k + 1;

            windowQueue.offer(new int[]{nums[i], i});

            while (windowQueue.peek()[1] < lastIndex) windowQueue.poll();

            res[lastIndex] = windowQueue.peek()[0];
        }

        return res;
    }

    public int[] maxSlidingWindow1(int[] nums, int k) {

        int len = nums.length;

        if (len == 1 || k == 1) return nums;

        Deque<Integer> window = new ArrayDeque<>();
        window.offer(nums[0]);

        int[] res = new int[len];

        for (int i = 1; i < len; i++) {

            int lastIndex = i - k + 1;
            int curr = nums[i];

            while (window.peekLast() != null && window.peekLast() < curr) {
                window.pollLast();
            }

            window.offerLast(curr);

            while (window.peekFirst() < lastIndex) {
                window.pollFirst();
            }

            res[i] = window.peekLast();
        }

        return Arrays.copyOfRange(res, k, len);
    }

    @Override
    public void run() {
        int[] nums = new int[]{9,10,9,-7,-4,-8,2,-6};
        int k = 5;

        System.out.println(Arrays.toString(maxSlidingWindow1(nums, k)));
    }

    @Override
    protected String getTitle() {
        return "滑动窗口最大值";
    }

    @Override
    protected Difficulty getDifficulty() {
        return Difficulty.HARD;
    }
}
