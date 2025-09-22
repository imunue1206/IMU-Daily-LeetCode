package problem.resolution;

import java.util.Set;

public abstract class LeetCodeProblem {

    public abstract void run();

    protected abstract String getTitle();

    protected abstract Difficulty getDifficulty();

    public enum Difficulty {
        EASY,
        MEDIUM,
        HARD
    }

    @Override
    public String toString() {
        return getDifficulty().toString() + " - " + getTitle();
    }

    public static class ListNode {
        int val;
        ListNode next;
        ListNode() {}
        ListNode(int val) { this.val = val; }
        ListNode(int val, ListNode next) { this.val = val; this.next = next; }
    }
}
