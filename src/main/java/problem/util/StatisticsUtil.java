package problem.util;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

public class StatisticsUtil {
    
    private static final String STATISTICS_FILE = "execution_statistics.txt";
    private static final DateTimeFormatter DATE_FORMAT = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
    
    /**
     * 记录执行信息
     */
    public static void recordExecution(int problemNumber) {
        try {
            // 获取项目根目录
            Path projectRoot = Paths.get(System.getProperty("user.dir"));
            Path statsFile = projectRoot.resolve(STATISTICS_FILE);
            
            // 如果文件不存在，创建文件
            if (!Files.exists(statsFile)) {
                Files.createFile(statsFile);
            }
            
            // 追加写入执行记录
            try (BufferedWriter writer = new BufferedWriter(new FileWriter(statsFile.toFile(), true))) {
                String timestamp = LocalDateTime.now().format(DATE_FORMAT);
                writer.write(String.format("Problem-%d, %s", problemNumber, timestamp));
                writer.newLine();
            }
            
        } catch (IOException e) {
            System.err.println("记录执行统计信息时出错: " + e.getMessage());
        }
    }
    
    /**
     * 统计并打印执行信息
     */
    public static void printStatistics() {
        try {
            Path projectRoot = Paths.get(System.getProperty("user.dir"));
            Path statsFile = projectRoot.resolve(STATISTICS_FILE);
            
            if (!Files.exists(statsFile)) {
                System.out.println("还没有执行记录！");
                return;
            }
            
            // 读取所有执行记录
            List<String> records = Files.readAllLines(statsFile);
            
            if (records.isEmpty()) {
                System.out.println("执行记录为空！");
                return;
            }
            
            // 统计信息
            int totalExecutions = records.size();
            int[] problemCounts = new int[2000]; // 假设题目编号最大为2000
            List<String> recentExecutions = new ArrayList<>();
            
            // 解析每条记录
            for (String record : records) {
                if (record.trim().isEmpty()) continue;
                
                String[] parts = record.split(", ");
                if (parts.length >= 2) {
                    String problemPart = parts[0];
                    int problemNumber = Integer.parseInt(problemPart.replace("Problem-", ""));
                    problemCounts[problemNumber]++;
                    
                    recentExecutions.add(record);
                }
            }
            
            // 打印统计信息
            System.out.println("\n========== 执行统计 ==========");
            System.out.println("总执行次数: " + totalExecutions);
            System.out.println("\n题目执行次数统计:");
            
            // 找出执行次数最多的题目
            int maxCount = 0;
            int mostExecutedProblem = 0;
            for (int i = 0; i < problemCounts.length; i++) {
                if (problemCounts[i] > maxCount) {
                    maxCount = problemCounts[i];
                    mostExecutedProblem = i;
                }
            }
            
            if (mostExecutedProblem > 0) {
                System.out.println("执行次数最多的题目: Problem-" + mostExecutedProblem + " (" + maxCount + "次)");
            }
            
            // 显示最近5次执行
            System.out.println("\n最近5次执行:");
            int startIndex = Math.max(0, recentExecutions.size() - 5);
            for (int i = startIndex; i < recentExecutions.size(); i++) {
                System.out.println("  " + recentExecutions.get(i));
            }
            
            // 显示每个题目的执行次数（只显示有执行的）
            System.out.println("\n各题目执行详情:");
            boolean hasExecutedProblems = false;
            for (int i = 0; i < problemCounts.length; i++) {
                if (problemCounts[i] > 0) {
                    System.out.println(String.format("  Problem-%d: %d次", i, problemCounts[i]));
                    hasExecutedProblems = true;
                }
            }
            
            if (!hasExecutedProblems) {
                System.out.println("  暂无执行记录");
            }
            
            System.out.println("================================\n");
            
        } catch (IOException e) {
            System.err.println("读取统计信息时出错: " + e.getMessage());
        }
    }
    
    /**
     * 清空统计记录
     */
    public static void clearStatistics() {
        try {
            Path projectRoot = Paths.get(System.getProperty("user.dir"));
            Path statsFile = projectRoot.resolve(STATISTICS_FILE);
            
            if (Files.exists(statsFile)) {
                Files.delete(statsFile);
                System.out.println("统计记录已清空！");
            } else {
                System.out.println("没有统计记录需要清空！");
            }
        } catch (IOException e) {
            System.err.println("清空统计记录时出错: " + e.getMessage());
        }
    }
}