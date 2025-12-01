package analyzer;

import java.io.*;
import java.nio.file.*;
import java.time.*;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.*;
import java.util.stream.Collectors;

// 尝试记录类
class AttemptRecord {
    public LocalDateTime timestamp;
    public String operation;
    public int sequence;

    public AttemptRecord(LocalDateTime timestamp, String operation, int sequence) {
        this.timestamp = timestamp;
        this.operation = operation;
        this.sequence = sequence;
    }
}

// 题目记录类
class ProblemRecord {
    public String problemId;
    public String title;
    public String difficulty;
    public List<AttemptRecord> attempts;

    public ProblemRecord(String problemId, String title, String difficulty) {
        this.problemId = problemId;
        this.title = title;
        this.difficulty = difficulty;
        this.attempts = new ArrayList<>();
    }

    public void addAttempt(LocalDateTime timestamp, String operation, int sequence) {
        attempts.add(new AttemptRecord(timestamp, operation, sequence));
        // 按时间排序
        attempts.sort((a1, a2) -> a1.timestamp.compareTo(a2.timestamp));
    }

    public Map<String, Object> getStats() {
        if (attempts.isEmpty()) {
            return new HashMap<>();
        }

        int runCount = 0;
        int debugCount = 0;
        for (AttemptRecord attempt : attempts) {
            if ("RUN".equals(attempt.operation)) {
                runCount++;
            } else {
                debugCount++;
            }
        }
        int totalAttempts = attempts.size();

        // 计算时间跨度（分钟）
        long timeSpanMinutes = ChronoUnit.MINUTES.between(
                attempts.get(0).timestamp,
                attempts.get(attempts.size() - 1).timestamp
        );
        timeSpanMinutes = Math.max(timeSpanMinutes, 1); // 避免除以0

        // 计算最大连续尝试次数
        int maxSequence = attempts.stream()
                .mapToInt(a -> a.sequence)
                .max()
                .orElse(1);

        // 计算难缠指数
        double timeDensity = 1 + (totalAttempts / (double)timeSpanMinutes) * 0.1;
        double sequencePenalty = maxSequence * 0.2;
        double troubleIndex = (debugCount * 3 + runCount * 1) * timeDensity * sequencePenalty;

        // 是否一次通过
        boolean onePass = totalAttempts == 1 && "RUN".equals(attempts.get(0).operation);

        Map<String, Object> stats = new HashMap<>();
        stats.put("runCount", runCount);
        stats.put("debugCount", debugCount);
        stats.put("totalAttempts", totalAttempts);
        stats.put("timeSpanMinutes", (double)timeSpanMinutes);
        stats.put("maxSequence", maxSequence);
        stats.put("troubleIndex", troubleIndex);
        stats.put("onePass", onePass);
        stats.put("firstAttempt", attempts.get(0).timestamp);
        stats.put("lastAttempt", attempts.get(attempts.size() - 1).timestamp);

        return stats;
    }
}

// 统计分析器
class LeetCodeAnalyzer {
    public Map<String, ProblemRecord> problems;
    public Map<String, Object> stats;

    public LeetCodeAnalyzer() {
        this.problems = new HashMap<>();
        this.stats = new HashMap<>();
        initializeStats();
    }

    private void initializeStats() {
        stats.put("totalProblems", 0);
        stats.put("totalAttempts", 0);
        stats.put("runCount", 0);
        stats.put("debugCount", 0);
        stats.put("onePassCount", 0);
        stats.put("onePassRate", 0.0);
        stats.put("difficultyDist", new HashMap<String, Integer>());
        stats.put("dailyActivity", new HashMap<LocalDate, Integer>());
    }

    public void parseData(List<String> dataLines) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

        for (String line : dataLines) {
            line = line.trim();
            if (line.isEmpty()) continue;

            String[] parts = line.split("\\|");
            if (parts.length != 6) continue;

            String problemId = parts[0];
            String difficulty = parts[1];
            String title = parts[2];
            String timestampStr = parts[3];
            String operation = parts[4];
            int sequence = Integer.parseInt(parts[5]);

            // 解析时间
            try {
                LocalDateTime timestamp = LocalDateTime.parse(timestampStr, formatter);

                // 创建或获取题目记录
                ProblemRecord problem = problems.computeIfAbsent(problemId,
                        k -> new ProblemRecord(problemId, title, difficulty));

                // 添加尝试记录
                problem.addAttempt(timestamp, operation, sequence);

                // 更新统计
                stats.put("totalAttempts", (int)stats.get("totalAttempts") + 1);
                if ("RUN".equals(operation)) {
                    stats.put("runCount", (int)stats.get("runCount") + 1);
                } else {
                    stats.put("debugCount", (int)stats.get("debugCount") + 1);
                }

                // 记录日期活动
                LocalDate dateKey = timestamp.toLocalDate();
                Map<LocalDate, Integer> dailyActivity = (Map<LocalDate, Integer>) stats.get("dailyActivity");
                dailyActivity.put(dateKey, dailyActivity.getOrDefault(dateKey, 0) + 1);

            } catch (Exception e) {
                System.err.println("解析时间错误: " + timestampStr);
            }
        }
    }

    public void calculateStats() {
        stats.put("totalProblems", problems.size());

        // 计算一次通过数量
        int onePassCount = 0;
        Map<String, Integer> difficultyDist = (Map<String, Integer>) stats.get("difficultyDist");

        for (ProblemRecord problem : problems.values()) {
            Map<String, Object> problemStats = problem.getStats();
            if ((boolean)problemStats.get("onePass")) {
                onePassCount++;
            }
            difficultyDist.put(problem.difficulty,
                    difficultyDist.getOrDefault(problem.difficulty, 0) + 1);
        }

        stats.put("onePassCount", onePassCount);
        double onePassRate = problems.size() > 0 ? (onePassCount * 100.0) / problems.size() : 0;
        stats.put("onePassRate", onePassRate);

        // 计算日期范围
        List<LocalDateTime> allDates = new ArrayList<>();
        for (ProblemRecord problem : problems.values()) {
            for (AttemptRecord attempt : problem.attempts) {
                allDates.add(attempt.timestamp);
            }
        }

        if (!allDates.isEmpty()) {
            LocalDateTime minDate = Collections.min(allDates);
            LocalDateTime maxDate = Collections.max(allDates);
            stats.put("dateRange", new LocalDateTime[]{minDate, maxDate});
        }
    }

    public List<Map.Entry<ProblemRecord, Double>> getTroubleRanking(int topN) {
        List<Map.Entry<ProblemRecord, Double>> ranked = new ArrayList<>();

        for (ProblemRecord problem : problems.values()) {
            Map<String, Object> problemStats = problem.getStats();
            double troubleIndex = (double) problemStats.get("troubleIndex");
            ranked.add(new AbstractMap.SimpleEntry<>(problem, troubleIndex));
        }

        // 按难缠指数降序排序
        ranked.sort((e1, e2) -> Double.compare(e2.getValue(), e1.getValue()));

        return ranked.subList(0, Math.min(topN, ranked.size()));
    }

    public Map<String, Integer> getDifficultyStats() {
        return (Map<String, Integer>) stats.get("difficultyDist");
    }

    public Map<Integer, Integer> getTimeAnalysis() {
        Map<Integer, Integer> hourDist = new HashMap<>();
        for (ProblemRecord problem : problems.values()) {
            for (AttemptRecord attempt : problem.attempts) {
                int hour = attempt.timestamp.getHour();
                hourDist.put(hour, hourDist.getOrDefault(hour, 0) + 1);
            }
        }
        return hourDist;
    }

    public double getEfficiencyScore() {
        int totalProblems = (int) stats.get("totalProblems");
        if (totalProblems == 0) return 0;

        // 基础分：一次通过率（权重40%）
        double onePassRate = (double) stats.get("onePassRate");
        double passRateScore = onePassRate * 0.4;

        // 难度分：完成题目难度分布（权重30%）
        Map<String, Integer> diffDist = getDifficultyStats();
        double diffScore = 0;
        Map<String, Integer> diffWeights = Map.of("EASY", 1, "MEDIUM", 2, "HARD", 3);
        int totalWeight = 0;

        for (Map.Entry<String, Integer> entry : diffDist.entrySet()) {
            String diff = entry.getKey();
            int count = entry.getValue();
            int weight = diffWeights.getOrDefault(diff, 1);
            diffScore += count * weight;
            totalWeight += count;
        }

        if (totalWeight > 0) {
            diffScore = (diffScore / totalWeight / 3) * 100 * 0.3;
        }

        // 效率分：调试比例（权重30%）
        int totalAttempts = (int) stats.get("totalAttempts");
        int debugCount = (int) stats.get("debugCount");
        double debugRatio = totalAttempts > 0 ? (double) debugCount / totalAttempts : 0;
        double efficiencyScore = (1 - debugRatio) * 100 * 0.3;

        return Math.min(100, passRateScore + diffScore + efficiencyScore);
    }
}

// 控制台渲染器

class ConsoleRenderer {
    private LeetCodeAnalyzer analyzer;
    private int terminalWidth = 80;

    public ConsoleRenderer(LeetCodeAnalyzer analyzer) {
        this.analyzer = analyzer;
    }

    public void printHeader(String title) {
        // 计算可用宽度（减去左右边框字符）
        int availableWidth = terminalWidth - 2;

        // 确保标题不超过可用宽度
        String displayTitle = truncateToWidth(title, availableWidth);

        // 计算实际需要的填充
        int titleDisplayWidth = calculateDisplayWidth(displayTitle);
        int leftPadding = (availableWidth - titleDisplayWidth) / 2;
        int rightPadding = availableWidth - titleDisplayWidth - leftPadding;

        String border = "═".repeat(availableWidth);
        String centeredTitle = " ".repeat(leftPadding) + displayTitle + " ".repeat(rightPadding);

        System.out.println("╔" + border + "╗");
        System.out.println("║" + centeredTitle + "║");
        System.out.println("╚" + border + "╝");
        System.out.println();
    }

    private String truncateToWidth(String s, int maxWidth) {
        int currentWidth = 0;
        StringBuilder result = new StringBuilder();

        for (int i = 0; i < s.length(); i++) {
            char c = s.charAt(i);
            int charWidth = isFullWidthChar(c) ? 2 : 1;

            if (currentWidth + charWidth > maxWidth) {
                // 如果添加这个字符会超出宽度，检查是否可以添加省略号
                if (currentWidth + 2 <= maxWidth && i < s.length() - 1) {
                    result.append("..");
                }
                break;
            }

            result.append(c);
            currentWidth += charWidth;
        }

        return result.toString();
    }

    private boolean isFullWidthChar(char c) {
        // 中文字符范围
        if (c >= 0x4E00 && c <= 0x9FFF) return true;
        // 全角字符范围
        if (c >= 0xFF00 && c <= 0xFFEF) return true;
        // 一些常见的全角标点
        if (c == '＇' || c == '＂' || c == '＃' || c == '＄' || c == '％' ||
                c == '＆' || c == '＇' || c == '（' || c == '）' || c == '＊' ||
                c == '＋' || c == '，' || c == '－' || c == '．' || c == '／' ||
                c == '：' || c == '；' || c == '＜' || c == '＝' || c == '＞' ||
                c == '？' || c == '＠' || c == '［' || c == '＼' || c == '］' ||
                c == '＾' || c == '＿' || c == '｀' || c == '｛' || c == '｜' ||
                c == '｝' || c == '～') return true;

        return false;
    }


    public String progressBar(double value, double maxValue, int width) {
        if (maxValue == 0) return "▢".repeat(width);

        int filled = (int) ((value / maxValue) * width);
        return "█".repeat(filled) + "▢".repeat(width - filled);
    }

    public String difficultyColor(String difficulty) {
        switch (difficulty) {
            case "EASY": return "🟢";
            case "MEDIUM": return "🟡";
            case "HARD": return "🔴";
            default: return "⚪";
        }
    }

    public String troubleStars(double index) {
        if (index < 2) return "⭐";
        else if (index < 5) return "⭐⭐";
        else if (index < 8) return "⭐⭐⭐";
        else if (index < 12) return "⭐⭐⭐⭐";
        else return "⭐⭐⭐⭐⭐";
    }

    public void printDashboard() {
        printHeader("LeetCode 刷题分析系统");

        // 数据范围
        LocalDateTime[] dateRange = (LocalDateTime[]) analyzer.stats.get("dateRange");
        String dateRangeStr;
        if (dateRange != null) {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
            dateRangeStr = dateRange[0].format(formatter) + " 至 " + dateRange[1].format(formatter);
        } else {
            dateRangeStr = "无数据";
        }

        System.out.println("📅 数据范围: " + dateRangeStr);
        System.out.println();

        // 概要统计
        Map<String, Object> stats = analyzer.stats;
        System.out.println("📊 实时数据概览");
        System.out.println("┌─────────────────┬─────────────────┬─────────────────┐");
        System.out.printf("│   总刷题数: %-2d  │  总尝试次数: %-2d │  一次通过率: %3.0f%% │\n",
                stats.get("totalProblems"), stats.get("totalAttempts"), stats.get("onePassRate"));
        System.out.println("├─────────────────┼─────────────────┼─────────────────┤");
        System.out.printf("│  RUN操作: %-4d │ DEBUG操作: %-3d │ 效率评分: %3.0f/100 │\n",
                stats.get("runCount"), stats.get("debugCount"), analyzer.getEfficiencyScore());
        System.out.println("└─────────────────┴─────────────────┴─────────────────┘");
        System.out.println();

        // 难度分布
        Map<String, Integer> diffStats = analyzer.getDifficultyStats();
        int total = diffStats.values().stream().mapToInt(Integer::intValue).sum();

        System.out.println("🎯 难度分布");
        if (total > 0) {
            for (String diff : Arrays.asList("EASY", "MEDIUM", "HARD")) {
                int count = diffStats.getOrDefault(diff, 0);
                double percent = (count * 100.0) / total;
                String bar = progressBar(count, total, 20);
                System.out.printf("%s %-6s: %s %d题(%.0f%%)\n",
                        difficultyColor(diff), diff, bar, count, percent);
            }
        }
        System.out.println();
    }

    public void printProblemAnalysis() {
        printHeader("题目详细分析报告");

        // 难缠指数排名
        List<Map.Entry<ProblemRecord, Double>> ranked = analyzer.getTroubleRanking(10);

        // 动态计算各列最大宽度
        int rankWidth = 3;      // 排名列宽固定为3
        int idWidth = 4;        // 编号最小宽度
        int titleWidth = 12;    // 标题最小宽度
        int difficultyWidth = 8; // 难度列宽固定
        int attemptsWidth = 6;  // 尝试次数列宽固定
        int troubleWidth = 8;   // 难缠指数列宽固定

        // 计算实际需要的宽度
        for (Map.Entry<ProblemRecord, Double> entry : ranked) {
            ProblemRecord problem = entry.getKey();
            idWidth = Math.max(idWidth, problem.problemId.length());
            titleWidth = Math.max(titleWidth, Math.min(problem.title.length(), 20));
        }

        // 确保宽度为偶数，便于对齐
        idWidth = (idWidth % 2 == 0) ? idWidth : idWidth + 1;
        titleWidth = (titleWidth % 2 == 0) ? titleWidth : titleWidth + 1;

        // 构建表格框架
        int totalWidth = rankWidth + idWidth + titleWidth + difficultyWidth + attemptsWidth + troubleWidth + 13; // 13是边框和分隔线

        // 表头
        System.out.println("🔢 题目尝试排名（按难缠指数）");
        printTableLine("┌", "┬", "┐", rankWidth, idWidth, titleWidth, difficultyWidth, attemptsWidth, troubleWidth);

        // 标题行
        printTableRow("│", "│", "│",
                centerString("排名", rankWidth),
                centerString("编号", idWidth),
                centerString("题目名称", titleWidth),
                centerString("难度", difficultyWidth),
                centerString("尝试次数", attemptsWidth),
                centerString("难缠指数", troubleWidth));

        printTableLine("├", "┼", "┤", rankWidth, idWidth, titleWidth, difficultyWidth, attemptsWidth, troubleWidth);

        // 数据行
        for (int i = 0; i < ranked.size(); i++) {
            Map.Entry<ProblemRecord, Double> entry = ranked.get(i);
            ProblemRecord problem = entry.getKey();
            double troubleIndex = entry.getValue();
            Map<String, Object> pStats = problem.getStats();
            int attemptCount = (int) pStats.get("totalAttempts");

            // 处理标题长度
            String title = problem.title;
            if (title.length() > titleWidth) {
                title = title.substring(0, titleWidth - 2) + "..";
            }

            String rankStr = centerString(String.valueOf(i + 1), rankWidth);
            String idStr = centerString(problem.problemId, idWidth);
            String titleStr = String.format("%-" + titleWidth + "s", title);
            String difficultyStr = centerString(difficultyColor(problem.difficulty) + " " + problem.difficulty, difficultyWidth);
            String attemptsStr = centerString(String.valueOf(attemptCount), attemptsWidth);
            String troubleStr = centerString(troubleStars(troubleIndex), troubleWidth);

            printTableRow("│", "│", "│", rankStr, idStr, titleStr, difficultyStr, attemptsStr, troubleStr);
        }

        printTableLine("└", "┴", "┘", rankWidth, idWidth, titleWidth, difficultyWidth, attemptsWidth, troubleWidth);
        System.out.println();

        // 时间线分析
        printTimelineAnalysis(ranked);
    }

    // 打印表格横线
    private void printTableLine(String left, String middle, String right,
                                int rankWidth, int idWidth, int titleWidth,
                                int difficultyWidth, int attemptsWidth, int troubleWidth) {
        System.out.print(left);
        System.out.print("─".repeat(rankWidth + 2));
        System.out.print(middle);
        System.out.print("─".repeat(idWidth + 2));
        System.out.print(middle);
        System.out.print("─".repeat(titleWidth + 2));
        System.out.print(middle);
        System.out.print("─".repeat(difficultyWidth + 2));
        System.out.print(middle);
        System.out.print("─".repeat(attemptsWidth + 2));
        System.out.print(middle);
        System.out.print("─".repeat(troubleWidth + 2));
        System.out.println(right);
    }

    // 打印表格行
    private void printTableRow(String left, String middle, String right,
                               String rank, String id, String title,
                               String difficulty, String attempts, String trouble) {
        System.out.printf("%s %s %s %s %s %s %s %s %s %s %s %s %s%n",
                left, rank, middle,
                id, middle,
                title, middle,
                difficulty, middle,
                attempts, middle,
                trouble, right);
    }

    // 字符串居中
    private String centerString(String s, int width) {
        if (s == null || s.isEmpty()) {
            return " ".repeat(width);
        }

        // 计算字符串的实际显示宽度（考虑中文字符）
        int displayWidth = calculateDisplayWidth(s);

        if (displayWidth >= width) {
            // 字符串太长，需要截断
            return truncateString(s, width);
        }

        int leftPadding = (width - displayWidth) / 2;
        int rightPadding = width - displayWidth - leftPadding;

        return " ".repeat(leftPadding) + s + " ".repeat(rightPadding);
    }

    private int calculateDisplayWidth(String s) {
        int width = 0;
        for (char c : s.toCharArray()) {
            // 中文字符和全角字符算2个宽度，英文字符算1个宽度
            if (c >= 0x4E00 && c <= 0x9FFF) { // 中文字符范围
                width += 2;
            } else if (c >= 0xFF00 && c <= 0xFFEF) { // 全角字符范围
                width += 2;
            } else {
                width += 1;
            }
        }
        return width;
    }

    private String truncateString(String s, int maxWidth) {
        int currentWidth = 0;
        StringBuilder result = new StringBuilder();

        for (char c : s.toCharArray()) {
            int charWidth = (c >= 0x4E00 && c <= 0x9FFF) || (c >= 0xFF00 && c <= 0xFFEF) ? 2 : 1;

            if (currentWidth + charWidth > maxWidth) {
                break;
            }

            result.append(c);
            currentWidth += charWidth;
        }

        // 如果还有空间，可以添加省略号
        if (currentWidth + 2 <= maxWidth && s.length() > result.length()) {
            result.append("..");
        }

        return result.toString();
    }

    // 时间线分析
    private void printTimelineAnalysis(List<Map.Entry<ProblemRecord, Double>> ranked) {
        System.out.println("📈 尝试模式时间线分析");

        for (int i = 0; i < Math.min(3, ranked.size()); i++) {
            Map.Entry<ProblemRecord, Double> entry = ranked.get(i);
            ProblemRecord problem = entry.getKey();
            Map<String, Object> pStats = problem.getStats();

            System.out.printf("%s. %s [%s] - 难缠指数: %.1f%n",
                    problem.problemId, problem.title, problem.difficulty, entry.getValue());

            if (!problem.attempts.isEmpty()) {
                DateTimeFormatter timeFormat = DateTimeFormatter.ofPattern("HH:mm:ss");

                // 提取所有连续序列
                List<List<AttemptRecord>> sequences = extractSequences(problem.attempts);

                for (int seqIndex = 0; seqIndex < sequences.size(); seqIndex++) {
                    List<AttemptRecord> sequence = sequences.get(seqIndex);

                    // 构建时间线
                    StringBuilder timeline = new StringBuilder();
                    for (int j = 0; j < sequence.size(); j++) {
                        AttemptRecord attempt = sequence.get(j);
                        String opSymbol = "RUN".equals(attempt.operation) ? "▶️" : "🔧";
                        timeline.append(String.format("%s(%d)", opSymbol, attempt.sequence));

                        if (j < sequence.size() - 1) {
                            timeline.append(" → ");
                        }
                    }

                    // 显示时间范围
                    String startTime = sequence.get(0).timestamp.format(timeFormat);
                    String endTime = sequence.get(sequence.size() - 1).timestamp.format(timeFormat);
                    long duration = ChronoUnit.SECONDS.between(
                            sequence.get(0).timestamp,
                            sequence.get(sequence.size() - 1).timestamp
                    );

                    String durationStr = duration < 60 ?
                            String.format("%d秒", duration) :
                            String.format("%d分%d秒", duration / 60, duration % 60);

                    System.out.printf("  %d. %s | %s-%s (%s)%n",
                            seqIndex + 1, timeline.toString(), startTime, endTime, durationStr);
                }

                // 总体统计
                double spanMinutes = (double)pStats.get("timeSpanMinutes");
                String spanText = spanMinutes < 120 ?
                        String.format("%.0f分钟", spanMinutes) :
                        String.format("%.1f小时", spanMinutes / 60);

                int debugCount = (int)pStats.get("debugCount");
                int runCount = (int)pStats.get("runCount");
                String debugDensity = debugCount > runCount ? "高" :
                        debugCount > 0 ? "中" : "低";

                System.out.printf("  总时间: %s | 调试密度: %s | 序列数: %d%n%n",
                        spanText, debugDensity, sequences.size());
            }
        }
    }

    // 提取连续序列
    private List<List<AttemptRecord>> extractSequences(List<AttemptRecord> attempts) {
        List<List<AttemptRecord>> sequences = new ArrayList<>();

        if (attempts.isEmpty()) {
            return sequences;
        }

        List<AttemptRecord> currentSequence = new ArrayList<>();
        currentSequence.add(attempts.get(0));

        for (int i = 1; i < attempts.size(); i++) {
            AttemptRecord current = attempts.get(i);
            AttemptRecord previous = attempts.get(i - 1);

            // 如果当前序列号是1，或者与上一个不连续，则开始新序列
            if (current.sequence == 1 || current.sequence != previous.sequence + 1) {
                sequences.add(new ArrayList<>(currentSequence));
                currentSequence.clear();
            }
            currentSequence.add(current);
        }

        // 添加最后一个序列
        if (!currentSequence.isEmpty()) {
            sequences.add(currentSequence);
        }

        return sequences;
    }

    public void printTimeAnalysis() {
        printHeader("时间效率分析报告");

        // 时间段分析
        Map<Integer, Integer> hourDist = analyzer.getTimeAnalysis();
        int totalAttempts = (int) analyzer.stats.get("totalAttempts");

        System.out.println("🕐 时间段活跃度分析");
        System.out.println("┌──────────────┬────────────────────────────────────────┬────────┐");
        System.out.println("│   时间段     │              活跃度热力图              │ 百分比 │");
        System.out.println("├──────────────┼────────────────────────────────────────┼────────┤");

        int[][] timeRanges = {{23, 6}, {9, 12}, {14, 18}, {19, 22}};
        String[] labels = {"夜间(23-6)", "上午(9-12)", "下午(14-18)", "晚间(19-22)"};

        for (int i = 0; i < timeRanges.length; i++) {
            int start = timeRanges[i][0];
            int end = timeRanges[i][1];
            String label = labels[i];

            // 计算该时间段内的尝试次数
            int count = 0;
            for (Map.Entry<Integer, Integer> entry : hourDist.entrySet()) {
                int hour = entry.getKey();
                if ((start <= hour && hour < end) ||
                        (start > end && (hour >= start || hour < end))) {
                    count += entry.getValue();
                }
            }

            double percent = totalAttempts > 0 ? (count * 100.0) / totalAttempts : 0;
            String bar = progressBar(count, totalAttempts, 30);

            System.out.printf("│ %-12s │ %s (%2d次)     │ %5.1f%% │\n",
                    label, bar, count, percent);
        }

        System.out.println("└──────────────┴────────────────────────────────────────┴────────┘");
        System.out.println();

        // 每日活动
        Map<LocalDate, Integer> dailyActivity = (Map<LocalDate, Integer>) analyzer.stats.get("dailyActivity");
        if (!dailyActivity.isEmpty()) {
            System.out.println("📅 每日活动趋势");
            int maxActivity = Collections.max(dailyActivity.values());

            List<LocalDate> dates = new ArrayList<>(dailyActivity.keySet());
            Collections.sort(dates);

            for (LocalDate date : dates) {
                int count = dailyActivity.get(date);
                String bar = progressBar(count, maxActivity, 40);
                System.out.printf("%s : %s %d次\n", date, bar, count);
            }
            System.out.println();
        }
    }

    public void printAbilityAssessment() {
        printHeader("个人能力矩阵评估");

        Map<String, Object> stats = analyzer.stats;
        double efficiencyScore = analyzer.getEfficiencyScore();

        // 算法理解 - 基于一次通过率和难度分布
        double onePassRate = (double) stats.get("onePassRate");
        double algoScore = Math.min(100, onePassRate * 1.2 + 20);

        // 调试能力
        int totalAttempts = (int) stats.get("totalAttempts");
        int debugCount = (int) stats.get("debugCount");
        double debugRatio = totalAttempts > 0 ? (double) debugCount / totalAttempts : 0;
        double debugScore = Math.max(30, 100 - debugRatio * 70);
        String debugComment = debugRatio > 0.3 ? "能从DEBUG恢复，但依赖度较高" : "调试效率良好";

        // 编码效率
        int totalProblems = (int) stats.get("totalProblems");
        double avgAttempts = totalProblems > 0 ? (double) totalAttempts / totalProblems : 0;
        double codingScore = Math.max(40, 100 - (avgAttempts - 1) * 20);
        String codingComment = avgAttempts > 1.5 ? "存在多次尝试，一次通过率待提升" : "编码效率良好";

        // 难题韧性
        Map<String, Integer> diffDist = analyzer.getDifficultyStats();
        int hardCount = diffDist.getOrDefault("HARD", 0);
        double persistenceScore = Math.min(100, 60 + hardCount * 20);
        String persistenceComment = hardCount > 0 ? "坚持解决Hard题目，表现良好" : "可尝试更多难题";

        // 时间管理
        Map<Integer, Integer> hourDist = analyzer.getTimeAnalysis();
        int nightCount = hourDist.entrySet().stream()
                .filter(e -> e.getKey() >= 23 || e.getKey() < 6)
                .mapToInt(Map.Entry::getValue)
                .sum();
        double nightRatio = totalAttempts > 0 ? (double) nightCount / totalAttempts : 0;
        double timeScore = 100 - nightRatio * 40;
        String timeComment = nightRatio > 0.5 ? "集中在晚间，规律性待改善" : "时间安排合理";

        System.out.println("🎯 核心能力评分（基于刷题行为分析）");
        System.out.println("┌─────────────────┬──────────┬─────────────────────────────────────┐");
        System.out.printf("│ 算法理解       │ 🟢 %3.0f/100 │ %-35s │\n", algoScore, "中等题一次通过，基础扎实");
        System.out.printf("│ 调试能力       │ 🟡 %3.0f/100 │ %-35s │\n", debugScore, debugComment);
        System.out.printf("│ 编码效率       │ 🟡 %3.0f/100 │ %-35s │\n", codingScore, codingComment);
        System.out.printf("│ 难题韧性       │ 🟢 %3.0f/100 │ %-35s │\n", persistenceScore, persistenceComment);
        System.out.printf("│ 时间管理       │ 🟡 %3.0f/100 │ %-35s │\n", timeScore, timeComment);
        System.out.println("└─────────────────┴──────────┴─────────────────────────────────────┘");
        System.out.println();

        // 能力雷达图（文本版）
        System.out.println("📊 技能掌握度雷达图");
        double[] scores = {algoScore, debugScore, codingScore, persistenceScore, timeScore};
        String[] labels = {"算法理解", "调试能力", "编码效率", "难题韧性", "时间管理"};

        for (int i = 0; i < labels.length; i++) {
            String bar = progressBar(scores[i], 100, 20);
            System.out.printf("%-8s(%3.0f): %s\n", labels[i], scores[i], bar);
        }
        System.out.println();

        // 成长预测
        System.out.println("📈 能力成长曲线（预测）");
        String currentGrade = efficiencyScore < 60 ? "初级" : efficiencyScore < 80 ? "中级" : "高级";
        System.out.printf("当前等级: 算法%s开发者 🟡\n", currentGrade);

        if (totalProblems >= 5) {
            System.out.println("30天预测: 高级开发者 🟢 (如保持训练)");
            System.out.println("突破关键: 减少20%调试时间，效率提升35%");
        } else {
            System.out.println("建议: 完成更多题目以获得准确预测");
        }
        System.out.println();
    }

    public void printCoachAdvice() {
        printHeader("AI教练个性化建议");

        List<Map.Entry<ProblemRecord, Double>> ranked = analyzer.getTroubleRanking(10);
        Map<String, Object> stats = analyzer.stats;

        System.out.println("🎯 重点改进领域（基于数据识别）");
        System.out.println();

        // 高优先级问题
        if (!ranked.isEmpty() && ranked.get(0).getValue() > 5) {
            Map.Entry<ProblemRecord, Double> entry = ranked.get(0);
            ProblemRecord problem = entry.getKey();
            Map<String, Object> pStats = problem.getStats();

            System.out.println("🔴 高优先级 - " + problem.title);
            System.out.printf("   • 难缠指数%.1f，尝试%d次\n", entry.getValue(), pStats.get("totalAttempts"));
            System.out.println("   • 建议: 专项练习类似题目，重点理解核心算法");
            System.out.println("   • 目标: 将此类题目难缠指数降至≤5.0");
            System.out.println();
        }

        // 中优先级问题
        List<ProblemRecord> mediumTrouble = ranked.stream()
                .filter(e -> e.getValue() > 2 && e.getValue() <= 5)
                .map(Map.Entry::getKey)
                .collect(Collectors.toList());

        if (!mediumTrouble.isEmpty()) {
            ProblemRecord problem = mediumTrouble.get(0);
            Map<String, Object> pStats = problem.getStats();

            System.out.println("🟡 中优先级 - " + problem.title);
            System.out.println("   • 基础题但多次尝试，存在理解盲区");
            System.out.println("   • 建议: 重新学习相关算法模板");
            System.out.println("   • 目标: 确保此类题目一次通过率100%");
            System.out.println();
        }

        // 良好保持
        long onePassCount = analyzer.problems.values().stream()
                .filter(p -> (boolean)p.getStats().get("onePass"))
                .count();

        if (onePassCount >= 2) {
            System.out.println("🟢 良好保持 - 已掌握" + onePassCount + "种题型");
            System.out.println("   • 一次通过率良好，算法基础扎实");
            System.out.println("   • 建议: 继续保持，可挑战更复杂变种题目");
            System.out.println();
        }

        // 训练计划
        System.out.println("📅 个性化训练计划");
        System.out.println("┌──────────┬──────────────────────────────┬──────────────┐");
        System.out.println("│  时间    │          训练重点            │   目标指标   │");

        int totalProblems = (int) stats.get("totalProblems");
        String[][] plans;

        if (totalProblems < 10) {
            plans = new String[][]{
                    {"本周", "完成5道新题", "题目数≥10"},
                    {"下周", "重点复习高难缠题目", "难缠指数↓30%"},
                    {"本月", "建立每日刷题习惯", "效率评分≥70"}
            };
        } else {
            plans = new String[][]{
                    {"本周", "专项练习薄弱环节", "难缠指数↓2.0"},
                    {"下周", "综合训练+新题", "一次通过率↑20%"},
                    {"本月", "减少调试依赖", "效率评分≥85"}
            };
        }

        for (String[] plan : plans) {
            System.out.println("├──────────┼──────────────────────────────┼──────────────┤");
            System.out.printf("│ %-8s │ %-28s │ %-12s │\n", plan[0], plan[1], plan[2]);
        }

        System.out.println("└──────────┴──────────────────────────────┴──────────────┘");
        System.out.println();

        // 习惯建议
        System.out.println("💡 行为习惯优化建议");
        String[] suggestions = {
                "1. 时间管理: 尝试分散练习时间，避免集中晚间",
                "2. 编码前: 先写伪代码，减少运行时调试",
                "3. 复盘: 对高难缠题目进行标记，定期复习",
                "4. 目标: 每周至少3题，Medium占比50%"
        };

        for (String suggestion : suggestions) {
            System.out.println(suggestion);
        }
        System.out.println();

        // 实时提醒
        System.out.println("🔔 实时提醒");
        if (!ranked.isEmpty() && ranked.get(0).getValue() > 5) {
            long highTroubleCount = ranked.stream()
                    .filter(e -> e.getValue() > 5)
                    .count();
            System.out.printf("• 您有%d道高难缠题目需要本周内复习\n", highTroubleCount);
        }

        // 推荐训练时间
        Map<Integer, Integer> hourDist = analyzer.getTimeAnalysis();
        if (!hourDist.isEmpty()) {
            int maxHour = Collections.max(hourDist.entrySet(), Map.Entry.comparingByValue()).getKey();
            String recTime = String.format("%02d:00-%02d:00", maxHour, maxHour + 2);
            System.out.printf("• 推荐训练时间: %s (您的效率高峰期)\n", recTime);
        }

        System.out.println("• 保持热情，您正在进步中！");
        System.out.println();
    }
}

// 主程序
public class LeetCodeAnalyzerApp {

    public static void main(String[] args) throws IOException {

        List<String> dataLines = new ArrayList<>();

        Path filePath = Paths.get("execution_statistics.txt");
        if (Files.exists(filePath)) dataLines = Files.readAllLines(filePath);;

        // 从文件读取或使用示例数据
        if (args.length > 0) {
            try {
                dataLines = Files.readAllLines(Paths.get(args[0]));
            } catch (IOException e) {
                System.out.println("无法读取文件");
            }
        }
        // 创建分析器并解析数据
        LeetCodeAnalyzer analyzer = new LeetCodeAnalyzer();
        analyzer.parseData(dataLines);
        analyzer.calculateStats();

        // 创建渲染器并显示报告
        ConsoleRenderer renderer = new ConsoleRenderer(analyzer);

        // 显示所有报告部分
        renderer.printDashboard();
        waitForEnter();

        renderer.printProblemAnalysis();
        waitForEnter();

        renderer.printTimeAnalysis();
        waitForEnter();

        renderer.printAbilityAssessment();
        waitForEnter();

        renderer.printCoachAdvice();
    }

    private static void waitForEnter() {
        System.out.println("按Enter键继续...");
        try {
            System.in.read();
            // 清空缓冲区
            while (System.in.available() > 0) {
                System.in.read();
            }
        } catch (IOException e) {
            // 忽略错误，继续执行
        }
        System.out.println("\n".repeat(3));
    }
}