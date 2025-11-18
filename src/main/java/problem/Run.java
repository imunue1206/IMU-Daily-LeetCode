package problem;

import problem.util.ExecuteUtil;

public class Run {

    public static void main(String[] args) {
        
        // 如果有参数，尝试将参数作为题目编号执行
        if (args.length > 0) {
            try {
                int problemNumber = Integer.parseInt(args[0]);
                ExecuteUtil.runWithNumber(problemNumber);
            } catch (NumberFormatException e) {
                System.out.println("请输入有效的题目编号，例如: java Run 283");
            }
            return;
        }
        
        // 默认执行题目283
        ExecuteUtil.runWithNumber(283);
    }

}
