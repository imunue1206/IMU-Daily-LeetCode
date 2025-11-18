package problem.util;

import problem.resolution.LeetCodeProblem;

import java.lang.reflect.InvocationTargetException;

public class ExecuteUtil {

    public static void runWithNumber(int suffix) {
        // 记录执行统计
        StatisticsUtil.recordExecution(suffix);
        
        String classPrefix = "problem.resolution.Problem";  // 固定前缀
        String className = classPrefix + suffix;  // 生成完整的类名

        try {
            // 通过反射查找类
            Class<?> problemClass = Class.forName(className);
            // 创建类的实例
            LeetCodeProblem leetCodeProblem = (LeetCodeProblem) problemClass.getDeclaredConstructor().newInstance();
            // 调用实例的 run() 方法
            leetCodeProblem.run();
        } catch (ClassNotFoundException e) {
            System.out.println("类 " + className + " 未找到.");
        } catch (InstantiationException | IllegalAccessException | NoSuchMethodException | InvocationTargetException e) {
            e.printStackTrace();
        }
    }

}
