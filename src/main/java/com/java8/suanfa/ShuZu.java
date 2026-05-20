package com.java8.suanfa;

import java.time.*;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;

public class ShuZu {
    private static final DateTimeFormatter DATE_TIME_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    public static void main(String[] args) {

        // 调用方法并打印结果
        List<String> dateList = getYesterdayAndTodayWithZeroTime();
        System.out.println("今天（00:00:00）：" + dateList.get(0));
        System.out.println("昨天（00:00:00）：" + dateList.get(1));
        String collected = dateList.stream()
                .map(s -> String.format("'%s'", s))
                .collect(Collectors.joining(",", "(", ")"));
        System.out.println(collected);


        // 1. 定义时间范围：2026-01-14 00:00:00 到 2026-01-20 23:59:59
        LocalDateTime start = LocalDateTime.of(2026, 1, 14, 0, 0, 0);
        LocalDateTime end = LocalDateTime.of(2026, 1, 20, 23, 59, 59);

        // 2. 生成100个随机时间点
        List<LocalDateTime> randomTimeList = generateRandomLocalDateTimes(start, end, 20);

        // 3. 输出结果（按时间排序，便于查看）
        randomTimeList.sort(LocalDateTime::compareTo);
        System.out.println("生成的100个随机时间点（2026-01-14 ~ 2026-01-20）：");
        for (int i = 0; i < randomTimeList.size(); i++) {
            System.out.printf("%d: %s%n", i + 1, randomTimeList.get(i));
        }

        // 验证数量和范围
        System.out.println("\n验证：");
        System.out.println("生成数量：" + randomTimeList.size());
        LocalDateTime minTime = randomTimeList.stream().min(LocalDateTime::compareTo).get();
        LocalDateTime maxTime = randomTimeList.stream().max(LocalDateTime::compareTo).get();
        System.out.println("最小时间：" + minTime);
        System.out.println("最大时间：" + maxTime);

        System.out.println("最近一个过去时间：" + findNearestPastTimeOptimize(randomTimeList));

    }

    // 随机数生成器（复用实例，避免多次创建）
    private static final Random RANDOM = new Random();

    /**
     * 生成指定范围内的随机LocalDateTime列表
     *
     * @param start 起始时间（包含）
     * @param end   结束时间（包含）
     * @param count 生成数量
     * @return 随机时间列表
     */
    public static List<LocalDateTime> generateRandomLocalDateTimes(
            LocalDateTime start,
            LocalDateTime end,
            int count
    ) {
        // 1. 校验参数：起始时间不能晚于结束时间，数量需大于0
        if (start.isAfter(end)) {
            throw new IllegalArgumentException("起始时间不能晚于结束时间");
        }
        if (count <= 0) {
            throw new IllegalArgumentException("生成数量必须大于0");
        }

        // 2. 将LocalDateTime转为带时区的Instant（默认系统时区，也可指定UTC）
        ZoneId zoneId = ZoneId.systemDefault();
        long startMillis = start.atZone(zoneId).toInstant().toEpochMilli();
        long endMillis = end.atZone(zoneId).toInstant().toEpochMilli();

        // 3. 生成随机时间（用Set去重，避免重复时间点）
        Set<LocalDateTime> timeSet = new HashSet<>();
        while (timeSet.size() < count) {
            // 生成介于startMillis和endMillis之间的随机毫秒数
            long randomMillis = startMillis + (long) (RANDOM.nextDouble() * (endMillis - startMillis + 1));
            // 将随机毫秒数转回LocalDateTime
            LocalDateTime randomTime = Instant.ofEpochMilli(randomMillis)
                    .atZone(zoneId)
                    .toLocalDateTime();
            timeSet.add(randomTime);
        }

        // 4. 转换为List返回
        return new ArrayList<>(timeSet);
    }

    // 优化版：遍历一次找最小值（避免排序，O(n) 复杂度）
    public static LocalDateTime findNearestPastTimeOptimize(List<LocalDateTime> timeList) {
        if (timeList == null || timeList.isEmpty()) {
            return null;
        }
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime nearest = null;
        long minDiff = Long.MAX_VALUE;

        for (LocalDateTime time : timeList) {
            if (time.isBefore(now)) {
                long diff = Duration.between(time, now).getSeconds();
                if (diff < minDiff) {
                    minDiff = diff;
                    nearest = time;
                }
            }
        }
        return nearest;
    }

    // 定义日期时间格式：yyyy-MM-dd HH:mm:ss（和你的数据库dt字段格式匹配）

    /**
     * 获取昨天和今天的日期（时间部分为00:00:00），封装为字符串列表
     *
     * @return 列表顺序：[今天 00:00:00, 昨天 00:00:00]（也可根据需求调整顺序）
     */
    public static List<String> getYesterdayAndTodayWithZeroTime() {
        List<String> dateList = new ArrayList<>(2);

        // 1. 获取今天的日期，拼接00:00:00
        LocalDate today = LocalDate.now();
        LocalDateTime todayZero = LocalDateTime.of(today, LocalTime.MIDNIGHT); // 午夜（00:00:00）
        String todayStr = todayZero.format(DATE_TIME_FORMATTER);

        // 2. 获取昨天的日期，拼接00:00:00
        LocalDate yesterday = today.minusDays(1); // 今天减1天 = 昨天
        LocalDateTime yesterdayZero = LocalDateTime.of(yesterday, LocalTime.MIDNIGHT);
        String yesterdayStr = yesterdayZero.format(DATE_TIME_FORMATTER);

        // 3. 添加到列表（顺序可按需调整，比如先昨天后今天）
        dateList.add(todayStr);
        dateList.add(yesterdayStr);

        return dateList;
    }
}
