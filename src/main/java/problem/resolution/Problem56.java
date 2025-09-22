package problem.resolution;

import java.util.*;

public class Problem56 extends LeetCodeProblem{

    public int[][] merge(int[][] intervals) {

        if (intervals.length == 0) {
            return new int[0][0];
        }

        Arrays.sort(intervals, (pre, cur) -> pre[0] != cur[0] ? Integer.compare(pre[0], cur[0]) : Integer.compare(pre[1], cur[1]));

        int num = 0;
        List<int[]> res = new ArrayList<>();
        res.add(intervals[0]);

        for (int i = 1 ; i < intervals.length; i++) {

            int curStart = intervals[i][0];
            int curEnd = intervals[i][1];
            int lastEnd = res.get(num)[1];

            if (curStart <= lastEnd) {
                if (curEnd > lastEnd) {
                    res.get(num)[1] = curEnd;
                }
            } else {
                num++;
                res.add(intervals[i]);
            }
        }

        return res.toArray(new int[num][2]);
    }

    @Override
    public void run() {
        int[][] intervals = {
                {2, 3},
                {4, 5},
                {6, 7},
                {8, 9},
                {1, 10}
        };

        int[][] merge = merge(intervals);

        System.out.println(Arrays.deepToString(merge));
    }

    @Override
    protected String getTitle() {
        return "合并区间";
    }

    @Override
    protected Difficulty getDifficulty() {
        return Difficulty.MEDIUM;
    }
}
