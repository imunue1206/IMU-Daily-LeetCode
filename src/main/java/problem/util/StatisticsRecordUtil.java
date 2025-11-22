package problem.util;

import problem.resolution.LeetCodeProblem;

import java.io.*;
import java.lang.management.ManagementFactory;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

public class StatisticsRecordUtil {
    
    private static final String STATISTICS_FILE = "execution_statistics.txt";
    private static final DateTimeFormatter DATE_FORMAT = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
    private static final String RECORD_FORMAT_TEMPLATE = "%s|%s|%s|%s|%s|%s";

    public static void recordProblemExecute(int problemNo) throws Exception {

        // 具体的类
        String classPrefix = "problem.resolution.Problem";  // 固定前缀
        String className = classPrefix + problemNo;
        Class<?> problemClass = Class.forName(className);
        LeetCodeProblem problem = (LeetCodeProblem) problemClass.getDeclaredConstructor().newInstance();

        // 格式化  题目编号｜难易度｜题目名称｜记录时间｜执行类型（RUN/DEBUG）｜连续执行次数
        String record = String.format(RECORD_FORMAT_TEMPLATE,
                problemNo,
                problem.getProblemInfo().get(0),
                problem.getProblemInfo().get(1),
                LocalDateTime.now().format(DATE_FORMAT),
                getRunMode(),
                getConsecutiveRuns(problemNo) + 1
        );

        // 追加到文件，没有则创建
        writeToFile(record);
    }

    private static String getRunMode() {
        List<String> jvmArgs = ManagementFactory.getRuntimeMXBean().getInputArguments();
        String argsString = jvmArgs.toString().toLowerCase();

        if (argsString.contains("-agentlib:jdwp") || argsString.contains("-xdebug")) {
            return "DEBUG";
        } else {
            return "RUN";
        }
    }

    private static int getConsecutiveRuns(int problemNo) throws IOException {
        Path filePath = Paths.get(STATISTICS_FILE);
        if (!Files.exists(filePath)) {
            return 0;
        }

        List<String> lines = Files.readAllLines(filePath);
        if (lines.isEmpty()) {
            return 0;
        }

        int count = 0;
        for (int i = lines.size() - 1; i >= 0; i--) {
            String line = lines.get(i);
            if (line.startsWith(problemNo + "|")) {
                count++;
            } else if (!line.trim().isEmpty()) {
                // 遇到其他类的记录，停止计数
                break;
            }
        }

        return count;
    }

    private static void writeToFile(String record) throws IOException {
        Path filePath = Paths.get(STATISTICS_FILE);

        if (!Files.exists(filePath)) {
            Files.writeString(filePath, record, StandardOpenOption.CREATE);
        } else {
            Files.writeString(filePath, System.lineSeparator() + record, StandardOpenOption.APPEND);
        }
    }

}