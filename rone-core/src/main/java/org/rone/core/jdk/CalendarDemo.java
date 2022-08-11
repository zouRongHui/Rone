package org.rone.core.jdk;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * 日历
 * @author rone
 */
public class CalendarDemo {

    public static void main(String[] args) throws ParseException {
        Calendar now = Calendar.getInstance();
        // 设置/获取，年份
        now.set(Calendar.YEAR, now.get(Calendar.YEAR));
        // 设置/获取，月份，默认一月为0
        now.set(Calendar.MONTH, now.get(Calendar.MONTH));
        // 设置/获取，这个月的几号
        now.set(Calendar.DAY_OF_MONTH, now.get(Calendar.DAY_OF_MONTH));
        // 设置/获取，今年的第几天
        now.set(Calendar.DAY_OF_YEAR, now.get(Calendar.DAY_OF_YEAR));
        // 设置/获取，小时
        // Calendar.HOUR：十二小时制, Calendar.HOUR_OF_DAY：二十四小时制
        now.set(Calendar.HOUR_OF_DAY, now.get(Calendar.HOUR_OF_DAY));
        // 设置/获取，分钟
        now.set(Calendar.MINUTE, now.get(Calendar.MINUTE));
        // 设置/获取，秒
        now.set(Calendar.SECOND, now.get(Calendar.SECOND));
        // 设置/获取，毫秒
        now.set(Calendar.MILLISECOND, now.get(Calendar.MILLISECOND));
        // 获取星期，1:周日，7:周六
        now.get(Calendar.DAY_OF_WEEK);
        // 设置星期，设置为当前周的周四(当前周为 周日-周六)
        now.set(Calendar.DAY_OF_WEEK, Calendar.THURSDAY);
        // 获取某个月的天数最大值
        now.getActualMaximum(Calendar.DAY_OF_MONTH);
        // 日期增减
        // 月份减一
        now.add(Calendar.MONTH, -1);
        // 月份加一
        now.add(Calendar.MONTH, 1);
        // 天数减一
        now.add(Calendar.DAY_OF_MONTH, -1);


        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        // Calendar 转 String
        String dateStr = sdf.format(now.getTime());
        // String 转 Calendar
        String str = "2010-5-27";
        Date date = sdf.parse(str);
        now.setTime(date);
        // Date 转 String
        String dateStr1 = sdf.format(new Date());
        // String 转 Date
        String str1 = "2010-5-27";
        Date date1 = sdf.parse(str1);
        // Date 转 Calendar
        now.setTime(new Date());
        // Calendar 转 Date
        Calendar calendar = Calendar.getInstance();
        Date date2 = calendar.getTime();

    }

}
