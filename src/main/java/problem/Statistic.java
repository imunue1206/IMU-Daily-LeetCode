package problem;

import problem.util.StatisticsUtil;

public class Statistic {
    
    public static void main(String[] args) {
        
        if (args.length == 0) {
            // 没有参数，显示统计信息
            StatisticsUtil.printStatistics();
            return;
        }
        
        String command = args[0].toLowerCase();
        switch (command) {
            case "stats":
            case "statistics":
                // 显示统计信息
                StatisticsUtil.printStatistics();
                break;
            case "clear":
                // 清空统计记录
                StatisticsUtil.clearStatistics();
                break;
            case "help":
                printHelp();
                break;
            default:
                System.out.println("未知命令: " + args[0]);
                printHelp();
                break;
        }
    }
    
    private static void printHelp() {
        System.out.println("\n统计工具使用方法:");
        System.out.println("  java Statistic               - 显示执行统计信息");
        System.out.println("  java Statistic stats         - 显示执行统计信息（同上）");
        System.out.println("  java Statistic clear         - 清空统计记录");
        System.out.println("  java Statistic help          - 显示帮助信息");
        System.out.println();
        System.out.println("示例:");
        System.out.println("  java Statistic               # 查看所有执行记录和统计");
        System.out.println("  java Statistic clear         # 清空所有统计记录");
        System.out.println();
    }
}
