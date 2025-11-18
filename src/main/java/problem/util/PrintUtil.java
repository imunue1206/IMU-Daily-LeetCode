package problem.util;

import java.util.ArrayList;
import java.util.List;

public class PrintUtil {

    public static void nums(int[] nums) {
        List<Integer> list = new ArrayList<>();
        for (int num : nums) {
            list.add(num);
        }
        System.out.println(list); // [1, 2, 3, 4, 5]
    }
}
