package org.rone.core.util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * 日期相关工具
 * @author rone
 */
public class DateUtils {

    private DateUtils() {
        throw new IllegalStateException("Utility class");
    }

    /**
     * 将日期格式的字符串根据指定的格式转换成Date对象
     * @param dateStr   日期格式的字符串
     * @param format    指定的日期格式
     * @return
     * @throws ParseException
     */
    public static Date str2Date(String dateStr, String format) throws ParseException {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(format);
        return simpleDateFormat.parse(dateStr);
    }

    /**
     * 将日期转换成指定格式的字符串
     * @param date
     * @param format
     * @return
     */
    public static String date2String(Date date, String format) {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(format);
        return simpleDateFormat.format(date);
    }

    /**
     * 格式化日期的显示格式，
     * 规则：
     * 	今天：只显示具体时间到分，如：08:31
     * 	昨天、前天
     * 	超过3天显示具体的日期，不显示时间，如：04-21（今年），2017-04-21（去年）
     *
     * @param targetDate
     * @return
     */
    public static String parseDate(Date targetDate) {
        Calendar nowTime = Calendar.getInstance();
        Calendar targetTime = Calendar.getInstance();
        targetTime.setTime(targetDate);
        int nowYear = nowTime.get(Calendar.YEAR);
        int targetYear = targetTime.get(Calendar.YEAR);

        //当前日期和目标日期的天数差，只考虑三天内的情况
        int days = 3;
        if (nowYear == targetYear) {
            //相同年份
            days = nowTime.get(Calendar.DAY_OF_YEAR) - targetTime.get(Calendar.DAY_OF_YEAR);
        } else if (nowTime.get(Calendar.YEAR) - targetTime.get(Calendar.YEAR) == 1) {
            //上一个年份
            days = nowTime.get(Calendar.DAY_OF_YEAR) + targetTime.getActualMaximum(Calendar.DAY_OF_YEAR)
                    - targetTime.get(Calendar.DAY_OF_YEAR);
        }
        if (days == 0) {
            //同一天，返回 "时:分"
            return new SimpleDateFormat("HH:mm").format(targetDate);
        } else if (days == 1) {
            //前一天，返回 "昨天"
            return "昨天";
        } else if (days == 2) {
            //两天前，返回 "前天"
            return "前天";
        }
        if (nowYear == targetYear) {
            //超过三天，但同年，返回 "月-日"
            return new SimpleDateFormat("MM-dd").format(targetDate);
        } else {
            //上一年，返回 "年-月-日"
            return new SimpleDateFormat("yyyy-MM-dd").format(targetDate);
        }
    }

    /**
     * 根据两天日期获取相差天数
     * @param can1
     * @param can2
     * @return
     */
    public static int getDaysByDatas(Calendar can1, Calendar can2) {
        //区别时间的先后
        Calendar before;
        Calendar after;
        if (can1.before(can2)) {
            before = can1;
            after = can2;
        } else {
            before = can2;
            after = can1;
        }
        //相差天数 = after的年天数 - before的年天数 + 两年之间的年总天数
        int years = after.get(Calendar.YEAR) - before.get(Calendar.YEAR);
        int days = after.get(Calendar.DAY_OF_YEAR) - before.get(Calendar.DAY_OF_YEAR);
        for (int i = 0; i < years; i++) {
            days += before.getActualMaximum(Calendar.DAY_OF_YEAR);
            before.set(Calendar.YEAR, -1);
        }
        return days;
    }

    /**
     * 获取两个时间之间间隔的月数和天数
     * @param beginDate 之前的时间
     * @param endDate   靠后的时间
     * @return  XX月XX天（月数为0时不返回月份信息，仅当月数不为0且天数为0时不返回天数信息）
     */
    public static String getIntervalMonthsAndDays(Date beginDate, Date endDate) {
        if (beginDate == null || endDate == null) {
            return "";
        }
        Calendar beginCalendar = Calendar.getInstance();
        beginCalendar.setTime(beginDate);
        Calendar endCalendar = Calendar.getInstance();
        endCalendar.setTime(endDate);

        beginCalendar.add(Calendar.DAY_OF_YEAR, -endCalendar.get(Calendar.DAY_OF_MONTH));
        endCalendar.set(Calendar.DAY_OF_MONTH, 0);

        int day = beginCalendar.getActualMaximum(Calendar.DAY_OF_MONTH) - beginCalendar.get(Calendar.DAY_OF_MONTH);
        int month = 12 * (endCalendar.get(Calendar.YEAR) - beginCalendar.get(Calendar.YEAR)) + endCalendar.get(Calendar.MONTH) - beginCalendar.get(Calendar.MONTH);
        StringBuilder result = new StringBuilder();
        if (month != 0) {
            result.append(month).append("月");
        }
        //仅当月数不为0且天数为0时不返回天数信息，其余都需要返回天数
        if (!(month != 0 && day == 0)) {
            result.append(day).append("天");
        }
        return result.toString();
    }
}
