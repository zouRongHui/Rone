package org.rone.core.jdk.jdk8;

import java.time.*;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;

/**
 * jdk8的日期
 * @author rone
 */
public class DateDemo {

    public static void main(String[] args) {
        // 日期的对象，不包含具体的时间
        LocalDate today = LocalDate.now();
        System.out.println("当前日期: " + today);
        System.out.println("当前日期年: " + today.getYear());
        System.out.println("当前日期月: " + today.getMonthValue());
        System.out.println("当前日期日: " + today.getDayOfMonth());
        // 修改日期
        System.out.println("修改日期,一周后: " + today.plusWeeks(1));
        System.out.println("修改日期,一年前: " + today.minus(1, ChronoUnit.YEARS));
        // 指定日期
        LocalDate newYear = LocalDate.of(2023, 1, 1);
        System.out.println("指定日期: " + newYear);
        // 日期比较
        if (today.equals(newYear)) {
            System.out.println("今天是2023年1月1日");
        }
        if (today.isAfter(newYear)) {
            System.out.println("2023年1月1日之后");
        }
        if (today.isBefore(newYear)) {
            System.out.println("快到2023年1月1日了");
        }
        // 日期与字符串互转
        System.out.println("字符串转日期: " + LocalDate.parse("20220701", DateTimeFormatter.BASIC_ISO_DATE));
        System.out.println("日期转字符串: " + today.format(DateTimeFormatter.BASIC_ISO_DATE));
        // 闰年判断
        if (today.isLeapYear()) {
            System.out.println("今年是闰年");
        } else {
            System.out.println("今年不是闰年");
        }
        // 计算两个日期的间隔
        Period period = today.until(newYear);
        System.out.println(String.format("当前和2023年1月1日相差: %s年%s月%s日", period.getYears(), period.getMonths(), period.getDays()));
        System.out.println();

        // 时间对象，不包含年月日
        LocalTime time = LocalTime.now();
        System.out.println("当前时间: " + time);
        // 动态修改时间
        System.out.println("当前时间,一小时后: " + time.plusHours(1));
        System.out.println("当前时间,一分钟前: " + time.minus(1, ChronoUnit.MINUTES));
        // 指定时间
        LocalTime diyLocalTime = LocalTime.of(8, 30, 0);
        System.out.println("指定时间: " + diyLocalTime);
        // 时间比较
        if (time.equals(diyLocalTime)) {
            System.out.println("现在是上班时间点了");
        }
        if (time.isAfter(diyLocalTime)) {
            System.out.println("已经迟到了");
        }
        if (time.isBefore(diyLocalTime)) {
            System.out.println("早到了");
        }
        // 时间与字符串互转
        System.out.println("时间转字符串: " + time.format(DateTimeFormatter.ofPattern("HH mm ss")));
        System.out.println("字符串转时间: " + LocalTime.parse("12 23 34", DateTimeFormatter.ofPattern("HH mm ss")));
        System.out.println();

        // 日期时间对象
        LocalDateTime todayTime = LocalDateTime.now();
        System.out.println("当前日期时间: " + todayTime);
        // 修改日期时间
        System.out.println("当前日期时间,一天后: " + todayTime.plusDays(1));
        System.out.println("当前日期时间,一小时前: " + todayTime.minus(1, ChronoUnit.HOURS));
        // 指定日期时间
        LocalDateTime diyLocalDateTime = LocalDateTime.of(2022, 10, 1, 0, 0, 0);
        System.out.println("指定日期时间: " + diyLocalDateTime);
        // 日期时间比较
        if (todayTime.equals(diyLocalDateTime)) {
            System.out.println("若干年前此时新中国成立了");
        }
        if (todayTime.isAfter(diyLocalDateTime)) {
            System.out.println("国庆过了");
        }
        if (todayTime.isBefore(diyLocalDateTime)) {
            System.out.println("等着国庆吧");
        }
        // 日期时间与字符串互转
        System.out.println("日期时间转字符串: " + todayTime.format(DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss")));
        System.out.println("字符串转日期时间: " + LocalDateTime.parse("20220701 12:23:34", DateTimeFormatter.ofPattern("yyyyMMdd HH:mm:ss")));
        System.out.println();

        // 年月
        YearMonth yearMonth = YearMonth.now();
        System.out.println("当前年月: " + yearMonth);
        System.out.println("当前年总天数: " + yearMonth.lengthOfYear());
        System.out.println("当前月总天数: " + yearMonth.lengthOfMonth());
        // 指定年月
        YearMonth diyYearMonth = YearMonth.of(2022, Month.FEBRUARY);
        System.out.println("指定年月: " + diyYearMonth);
        // 就年月比较
        if (yearMonth.equals(diyYearMonth)) {
            System.out.println("2月份还没到");
        }
        if (yearMonth.isBefore(diyYearMonth)) {
            System.out.println("还没到春天");
        }
        if (yearMonth.isAfter(diyYearMonth)) {
            System.out.println("春天已经来了");
        }
        System.out.println();

        // 月日
        MonthDay monthDay = MonthDay.now();
        System.out.println("当前月日: " + monthDay);
        System.out.println("当前月: " + monthDay.getMonthValue());
        // 指定月日
        MonthDay diyMonthDay = MonthDay.of(10, 1);
        System.out.println("指定月日: " + diyMonthDay);
        // 就月日比较
        if (monthDay.equals(diyMonthDay)) {
            System.out.println("今天不是是10月1日");
        }
        if (monthDay.isBefore(diyMonthDay)) {
            System.out.println("国庆节还没到");
        }
        if (monthDay.isAfter(diyMonthDay)) {
            System.out.println("下一个法定节日是元旦了");
        }
        System.out.println();

        // 时间戳
        Clock clock = Clock.systemUTC();
        System.out.println("当前时间戳: " + clock.millis());
        Clock defaultClock = Clock.systemDefaultZone();
        System.out.println("当前默认时区的时间戳: " + defaultClock.millis());
        Instant timestamp = Instant.now();
        System.out.println("当前时间戳: " + timestamp.toEpochMilli());
        System.out.println();

        // 时区
        System.out.println("默认时区的日期时间: " + ZonedDateTime.now());
        ZoneId newYork = ZoneId.of("America/New_York");
        System.out.println("美国纽约的日期时间: " + ZonedDateTime.now(newYork));
        // 将当前时间转换为指定时区的时间
        System.out.println("将当前时间转换为指定时区的时间: " + ZonedDateTime.now().withZoneSameInstant(newYork));
        System.out.println("指定时间并切换到指定时区; " + ZonedDateTime.of(today.atTime(12, 0), newYork));
        System.out.println();
    }
}
