package problem.resolution;

import java.util.ArrayList;
import java.util.List;
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

    protected String note() {
        return """
                write something in here
                """;
    }

    public List<String> getProblemInfo() {
        List<String> infos = new ArrayList<>();
        infos.add(getDifficulty().name());
        infos.add(getTitle());
        return infos;
    }

    public static class ListNode {
        int val;
        ListNode next;
        ListNode() {}
        ListNode(int val) { this.val = val; }
        ListNode(int val, ListNode next) { this.val = val; this.next = next; }
    }
}
