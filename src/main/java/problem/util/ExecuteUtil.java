package problem.util;

import problem.resolution.LeetCodeProblem;

public class ExecuteUtil {

    public static void runWithNumber(int suffix) throws Exception {
        
        String classPrefix = "problem.resolution.Problem";  // 固定前缀
        String className = classPrefix + suffix;  // 生成完整的类名

        StatisticsRecordUtil.recordProblemExecute(suffix);

        Class<?> problemClass = Class.forName(className);
        // 创建类的实例
        LeetCodeProblem leetCodeProblem = (LeetCodeProblem) problemClass.getDeclaredConstructor().newInstance();
        // 调用实例的 run() 方法
        leetCodeProblem.run();
    }

}
