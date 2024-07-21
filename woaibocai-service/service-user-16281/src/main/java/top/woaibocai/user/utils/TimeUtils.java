package top.woaibocai.user.utils;

import java.time.Duration;
import java.time.LocalDateTime;
import java.time.Period;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * @program: woaibocai-parent
 * @description: 日期的计算
 * @author: LikeBocai
 * @create: 2024/7/21 10:10
 **/

public class TimeUtils {

    // LocalDateTime 之差 转化 时分秒
    public static String localDateTimeBetween(LocalDateTime start, LocalDateTime end) {
        // 计算年、月、日的差值
        Period period = Period.between(start.toLocalDate(), end.toLocalDate());
        long years = period.getYears();
        long months = period.getMonths();
        long days = period.getDays();

        // 计算时、分的差值
        Duration duration = Duration.between(start, end);
        long totalHours = duration.toHours();
        long hours = totalHours % 24;
        long minutes = duration.toMinutes() % 60;

        // 使用 LinkedHashMap 保持顺序
        Map<String, Long> timeUnits = new LinkedHashMap<>();
        timeUnits.put("年", years);
        timeUnits.put("月", months);
        timeUnits.put("日", days);
        timeUnits.put("时", hours);
        timeUnits.put("分", minutes);

        // 构建输出字符串
        StringBuilder formattedDifference = new StringBuilder();
        for (Map.Entry<String, Long> entry : timeUnits.entrySet()) {
            if (entry.getValue() > 0) {
                formattedDifference.append(entry.getValue()).append(entry.getKey());
            }
        }

        String result = formattedDifference.toString();
        if (result.isEmpty()) {
            result = "0分";  // 当所有单位都为0时，输出“0分”
        }
        return result;
    }
}
