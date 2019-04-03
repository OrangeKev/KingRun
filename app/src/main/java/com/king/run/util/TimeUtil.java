package com.king.run.util;

import android.content.Context;


import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Locale;
import java.util.TimeZone;

/**
 * Created with IntelliJ IDEA. User: weiguo.ren Date: 13-10-30 Time: 下午3:39 To
 * change this template use File | Settings | File Templates.
 */
public class TimeUtil {
    /**
     * @return 该毫秒数转换为 * days * hours * minutes * seconds 后的格式
     * @author fy.zhang
     */
    public static String formatDuring(long mss) {
        long days = mss / (1000 * 60 * 60 * 24);
        long hours = (mss % (1000 * 60 * 60 * 24)) / (1000 * 60 * 60);
        long minutes = (mss % (1000 * 60 * 60)) / (1000 * 60);
        long seconds = (mss % (1000 * 60)) / 1000;
        String time;
        if (days == 0 && hours == 0 && minutes == 0)
            time = seconds + "秒";
        else if (days == 0 && hours == 0)
            time = minutes + "分";
        else if (days == 0)
            time = hours + "小时" + minutes + "分";
        else
            time = days + "天" + hours + "小时" + minutes + "分";
        return time;
    }

    public static String getYearMonth() {
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy年MM月 ",
                Locale.getDefault());
        Date curDate = new Date(System.currentTimeMillis());// 获取当前时间
        String str = formatter.format(curDate);
        return str;
    }

    public static String getDateTheStr(Calendar calendar) {
        String formatStr = "yyyy-MM-dd";
        SimpleDateFormat df = new SimpleDateFormat(formatStr,
                Locale.getDefault());
        String dateString = df.format(calendar.getTime());
        return dateString;
    }

    /*
     * 获取每月第一天 00:00:00
     */
    public static long getMonthFirstDay() {
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd",
                Locale.getDefault());
        Calendar cal = Calendar.getInstance();
        cal.set(GregorianCalendar.DAY_OF_MONTH, 1);
        Date beginTime = cal.getTime();
        String startTime = formatter.format(beginTime) + " 00:00:00";
        System.out.println(startTime);
        return getStringToDate(startTime);
    }

    public static long getToDay() {
        Long toDayTime = System.currentTimeMillis();
        return toDayTime;
    }

    public static Long getStringToDate(String tiemString) {
        SimpleDateFormat sf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss",
                Locale.getDefault());
        Date s = null;
        try {
            s = sf.parse(tiemString);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return s.getTime();
    }

    public static String getDateToString(long time) {
        SimpleDateFormat sf = new SimpleDateFormat("yyyy年MM月dd日 HH:mm");
        return sf.format(time);
    }

    public static String getIsToday(String time) {
        Date date = new Date(Long.parseLong(time.trim()));
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd",
                Locale.getDefault());
        SimpleDateFormat sfH = new SimpleDateFormat("HH:mm",
                Locale.getDefault());
        SimpleDateFormat sfY = new SimpleDateFormat("MM-dd HH:mm",
                Locale.getDefault());
        String dateString = formatter.format(date);
        if (getDateTime().equals(dateString)) {
            return sfH.format(date);
        } else {
            return sfY.format(date);
        }
    }

    public static String getDateTime() {
        SimpleDateFormat sDateFormat = new SimpleDateFormat("yyyy-MM-dd",
                Locale.getDefault());
        String date = sDateFormat.format(new Date());
        return date;
    }

    /**
     * 判断是否为合法的日期时间字符串
     *
     * @param str_input
     * @param str_input
     * @return boolean;符合为true,不符合为false
     */
    public static boolean isDate(String str_input, String rDateFormat) {
        if (!isNull(str_input)) {
            SimpleDateFormat formatter = new SimpleDateFormat(rDateFormat,
                    Locale.getDefault());
            formatter.setLenient(false);
            try {
                formatter.format(formatter.parse(str_input));
            } catch (Exception e) {
                return false;
            }
            return true;
        }
        return false;
    }

    public static boolean isNull(String str) {
        return str == null;
    }

    public static long getDateFormat(String time, int status) {

        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd",
                Locale.getDefault());

        SimpleDateFormat fm = new SimpleDateFormat("yyyy-MM-dd HH:mm",
                Locale.getDefault());
        long times = 0;

        try {
            switch (status) {
                case 0:
                    times = formatter.parse(time).getTime();
                    break;
                case 1:
                    times = fm.parse(time).getTime();
                    break;
            }
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return times;
    }

    /**
     * 计算出离beginDate日期datas天的日期, 若datas小于0表示当前日期之前datas天，若datas大于0表当前日期之后datas天
     *
     * @param //要计算的天数
     * @return 得到日期
     */
    public static String getDate(Date beginDate) {
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd",
                Locale.getDefault()); // 制定日期格式
        Calendar c = Calendar.getInstance();
        c.setTime(beginDate);
        c.add(Calendar.MONTH, 1); // 将当前日期加一个月
        GregorianCalendar gc = new GregorianCalendar();
        gc.setTime(c.getTime());
        gc.add(Calendar.DATE, -1);
        String validityDate = df.format(gc.getTime());
        return validityDate;
    }

    public static String getDateYear(Date date) {
        Calendar c = Calendar.getInstance(
                TimeZone.getDefault(), Locale.CHINA);
        SimpleDateFormat formatter = new SimpleDateFormat(
                "yyyy-MM-dd", Locale.getDefault());
        // String beginDate = formatter.format(date);
        c.add(Calendar.YEAR, 1);
        Date newdate = c.getTime();
        String endDate = formatter.format(newdate);
        return endDate;
    }

    public static String getNextDate(String time, boolean isYear) {
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd",
                Locale.getDefault());
        String monthDate = "";
        try {
            Date beginDate = formatter.parse(time);
            if (isYear) {
                monthDate = getDateYear(beginDate);
            } else {
                monthDate = getDate(beginDate);
            }
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return monthDate;

    }

//    public static String getWeekOfDate(String time) {
//        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd",
//                Locale.getDefault());
//        try {
//            Date date = formatter.parse(time);
//            return getWeekOfDate(date.getTime());
//        } catch (ParseException e) {
//            e.printStackTrace();
//            return null;
//        }
//    }

    public static String getExamineTime(String time) {
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy年MM月dd日",
                Locale.getDefault());
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd",
                Locale.getDefault());
        try {
            Date date = formatter.parse(time);
            String t = sdf.format(date);
            return t;
        } catch (ParseException e) {
            e.printStackTrace();
            return null;
        }
    }

    public static String getExamineTime2(String time) {
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy年MM月dd日 HH:mm",
                Locale.getDefault());
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm",
                Locale.getDefault());
        try {
            Date date = formatter.parse(time);
            String t = sdf.format(date);
            return t;
        } catch (ParseException e) {
            e.printStackTrace();
            return null;
        }
    }

    public static long getExamineTimeLong(String time) {
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy年MM月dd日",
                Locale.getDefault());
        try {
            Date date = formatter.parse(time);
            return date.getTime();
        } catch (ParseException e) {
            e.printStackTrace();
            return 0;
        }
    }

    public static String getChuChaiTimeLong(String time) {
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy年MM月dd日",
                Locale.getDefault());
        try {
            Date date = formatter.parse(time);

            return getDateStr(date.getTime());
        } catch (ParseException e) {
            e.printStackTrace();
            return null;
        }
    }

    public static long getExamineTimeLong2(String time) {
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy年MM月dd日",
                Locale.getDefault());
        try {
            Date date = formatter.parse(time);
            return date.getTime();
        } catch (ParseException e) {
            e.printStackTrace();
            return 0;
        }
    }

//    public static String getWeekOfDate(long timeStamp) {
//        // String[] weekDays = { "星期日", "星期一", "星期二", "星期三", "星期四", "星期五", "星期六"
//        // };
//        String[] weekDays = BaseApplication.getContext().getResources()
//                .getStringArray(R.array.week_days);
//        int w = 0;
//        Calendar cal = Calendar.getInstance();
//        cal.setTimeInMillis(timeStamp);
//        w = cal.get(Calendar.DAY_OF_WEEK) - 1;
//        if (w < 0)
//            w = 0;
//
//        return weekDays[w];
//    }

    /*
     * 2014年1月27日 10:28
     */
//    public static String getFullDateStr(Calendar calendar) {
//        Context context = BaseApplication.getContext();
//        String formatStr = "yyyy"
//                + context
//                .getString(R.string.year)
//                + "M"
//                + context
//                .getString(R.string.month)
//                + "dd"
//                + context.getString(R.string.day)
//                + " HH:mm";
//        SimpleDateFormat df = new SimpleDateFormat(formatStr,
//                Locale.getDefault());
//        String dateString = df.format(calendar.getTime());
//        return dateString;
//    }

    /*
     * 201401271028
     */
    public static String getSimpleFullTimeStr(long time) {
        String formatStr = "yyyyMMddHHmm";
        SimpleDateFormat df = new SimpleDateFormat(formatStr,
                Locale.getDefault());
        String dateString = df.format(time);
        return dateString;
    }

    /*
     * 2014-01-27 10:28
     */
    public static String getSimpleFullDateStr(long time) {
        String formatStr = "yyyy-MM-dd HH:mm";
        SimpleDateFormat df = new SimpleDateFormat(formatStr,
                Locale.getDefault());
        String dateString = df.format(time);
        return dateString;
    }

    public static String getSimpleExamineStr(long time) {
//		String formatStr = "MM-dd HH:mm";
        String formatStr = "MM-dd";
        SimpleDateFormat df = new SimpleDateFormat(formatStr,
                Locale.getDefault());
        String dateString = df.format(time);
        return dateString;
    }

    public static String getSimpleExamineStrCreate(long time) {
        String formatStr = "MM-dd HH:mm";
        SimpleDateFormat df = new SimpleDateFormat(formatStr,
                Locale.getDefault());
        String dateString = df.format(time);
        return dateString;
    }

    public static String getSimplDateStr(long time) {
        String formatStr = "HH:mm";
        SimpleDateFormat df = new SimpleDateFormat(formatStr,
                Locale.getDefault());
        String dateString = df.format(time);
        return dateString;
    }

    public static String getSimpleFullDateStrSign(long time) {
        String formatStr = "MM月dd日";
        SimpleDateFormat df = new SimpleDateFormat(formatStr,
                Locale.getDefault());
        String dateString = df.format(time);
        return dateString;
    }

    public static String getCurWeek(long time) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(time);
        String week = getCurWeek(calendar.get(Calendar.DAY_OF_WEEK));
        return week;
    }

    public static String getCurWeek(int curWeek) {
        String week = "";
        switch (curWeek) {
            case 1:
                week = "星期日";
                break;
            case 2:
                week = "星期一";
                break;
            case 3:
                week = "星期二";
                break;
            case 4:
                week = "星期三";
                break;
            case 5:
                week = "星期四";
                break;
            case 6:
                week = "星期五";
                break;
            case 7:
                week = "星期六";
                break;
        }
        return week;
    }

    /*
     * 2014-01-27 10:28
     */
    public static String getSimpleFullDateStr(Calendar calendar) {
        return getSimpleFullDateStr(calendar.getTimeInMillis());
    }

    /*
     * 2014-01-27
     */
    public static String getDateStr(Calendar calendar) {
        return getDateStr(calendar.getTimeInMillis());
    }

    /*
     * 2014-01-27
     */
    public static String getDateStr(long time) {
        String formatStr = "yyyy-MM-dd";
        SimpleDateFormat df = new SimpleDateFormat(formatStr,
                Locale.getDefault());
        String dateString = df.format(time);
        return dateString;
    }

    /*
     * return like: 11:46
     */
    public static String getTimeStrByLong(long startTime) {
        Date date = new Date(startTime);
        SimpleDateFormat df = new SimpleDateFormat("HH:mm", Locale.getDefault());
        return df.format(date);
    }


    public static String getDateByLong(long startTime) {
        Date date = new Date(startTime);
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd",
                Locale.getDefault());
        SimpleDateFormat sfH = new SimpleDateFormat("HH:mm",
                Locale.getDefault());
        SimpleDateFormat sfY = new SimpleDateFormat("MM-dd HH:mm",
                Locale.getDefault());
        String dateString = formatter.format(date);
        if (getDateTime().equals(dateString)) {
            return sfH.format(date);
        } else {
            return sfY.format(date);
        }
    }

	/*
     * 当天返回时间 如23:23 昨天返回昨天 当前周返回周几 其他返回日期 2014-04-18
	 */

    public static CharSequence getDisplayDate(long timestamp) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(timestamp);
        if (isToday(calendar)) {
            return getTimeStrByLong(timestamp);
//        } else if (isYestoday(timestamp)) {
//            return BaseApplication.getContext().getString(R.string.yestoday);
//        } else if (isBeforYestoday(timestamp)) {
//            return BaseApplication.getContext().getString(
//                    R.string.before_yestoday);
//        } else if (isThisWeek(timestamp)) {
//            return getWeekOfDate(timestamp);
        } else {
            return getDateStr(timestamp);
        }
    }

    /*
     * 当天返回今天 + 时间 如23:23 昨天返回昨天 当前周返回周几 其他返回日期 2014-04-18，所有日期后都跟时间
     */
//    public static CharSequence getDisplayDateWithTime(long timestamp) {
//        Calendar calendar = Calendar.getInstance();
//        calendar.setTimeInMillis(timestamp);
//        if (isToday(calendar)) {
//            return BaseApplication.getContext().getString(R.string.today) + " "
//                    + getTimeStrByLong(timestamp);
//        } else if (isYestoday(timestamp)) {
//            return BaseApplication.getContext().getString(R.string.yestoday)
//                    + " " + getTimeStrByLong(timestamp);
//        } else if (isBeforYestoday(timestamp)) {
//            return BaseApplication.getContext().getString(
//                    R.string.before_yestoday)
//                    + " " + getTimeStrByLong(timestamp);
//        } else if (isThisWeek(timestamp)) {
//            return getWeekOfDate(timestamp) + " " + getTimeStrByLong(timestamp);
//        } else {
//            return getDateStr(timestamp) + " " + getTimeStrByLong(timestamp);
//        }
//    }

    private static boolean isYestoday(long timestamp) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(timestamp);
        calendar.add(Calendar.DAY_OF_YEAR, 1);
        return isToday(calendar);
    }

    private static boolean isBeforYestoday(long timestamp) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(timestamp);
        calendar.add(Calendar.DAY_OF_YEAR, 2);
        return isToday(calendar);
    }

    private static boolean isThisWeek(long timestamp) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(timestamp);
        Calendar today = Calendar.getInstance();
        return today.get(Calendar.YEAR) == calendar.get(Calendar.YEAR)
                && today.get(Calendar.WEEK_OF_YEAR) == calendar
                .get(Calendar.WEEK_OF_YEAR) && calendar.before(today);
    }

    public static boolean isToday(long time) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(time);
        return isToday(calendar);
    }

    public static boolean isToday(Calendar calendar) {
        Calendar today = Calendar.getInstance();
        return isOneDay(calendar.getTimeInMillis(), today.getTimeInMillis());
    }

    public static long getDayStart(long timeStamp) {
        return getDayStart(getCalendarByLong(timeStamp));
    }

    public static long getDayEnd(long timeStamp) {
        return getDayEnd(getCalendarByLong(timeStamp));
    }

    public static long getDayStart(Calendar calendar) {
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);
        return calendar.getTimeInMillis();
    }

    public static long getDayEnd(Calendar calendar) {
        calendar.set(Calendar.HOUR_OF_DAY, 23);
        calendar.set(Calendar.MINUTE, 59);
        calendar.set(Calendar.SECOND, 59);
        calendar.set(Calendar.MILLISECOND, 999);
        return calendar.getTimeInMillis();
    }

    public static long getMonthStart(Calendar calendar) {
        Calendar cal = (Calendar) calendar.clone();
        cal.set(Calendar.DAY_OF_MONTH, 1);
        return getDayStart(cal);
    }

    public static long getMonthEnd(Calendar calendar) {
        Calendar cal = (Calendar) calendar.clone();
        cal.set(Calendar.DAY_OF_MONTH, cal.getMaximum(Calendar.DAY_OF_MONTH));
        return getDayEnd(cal);
    }

    public static long getWeekStart(Calendar calendar) {
        Calendar cal = (Calendar) calendar.clone();
        cal.set(Calendar.DAY_OF_WEEK, Calendar.SUNDAY);
        return getDayStart(cal);
    }

    public static long getWeekEnd(Calendar calendar) {
        Calendar cal = (Calendar) calendar.clone();
        cal.set(Calendar.DAY_OF_WEEK, Calendar.SATURDAY);
        return getDayEnd(cal);
    }

    public static Calendar getCalendarByLong(long timeStamp) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(timeStamp);
        return calendar;
    }

//    public static String getDateWeekTime(long timeStamp) {
//        String dateWeek = getDateWeek(timeStamp);
//        String time = getTimeStrByLong(timeStamp);
//        return dateWeek + " " + time;
//    }

//    public static String getDateWeek(long timeStamp) {
//        String date = getDateStr(getCalendarByLong(timeStamp));
//        String weekDay = getWeekOfDate(timeStamp);
//        return date + " " + weekDay;
//    }

    public static String getTodayTime(String time) {
        // Date date = new Date(Long.parseLong(time.trim()));
        SimpleDateFormat formatter1 = new SimpleDateFormat("yyyy-MM-dd HH:mm",
                Locale.getDefault());
        SimpleDateFormat sfH = new SimpleDateFormat("HH:mm",
                Locale.getDefault());

        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd",
                Locale.getDefault());
        Date times;
        String date = "";
        try {
            times = formatter.parse(time);
            String dateString = formatter.format(times);
            if (getDateTime().equals(dateString)) {
                date = sfH.format(formatter1.parse(time));
            } else {
                date = formatter.format(formatter1.parse(time));
            }
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return date;
    }

    /*
     * 返回日期格式： 06月25日 15:13
     */
    public static String getDateTimeZh(long time) {
        Date now = new Date(time);
        SimpleDateFormat f = new SimpleDateFormat("MM月dd日 HH:mm",
                Locale.getDefault());
        return f.format(now);
    }

    public static long getLongFromeDate(String start) {
        if (StringUtil.isBlank(start)) {
            return 0;
        }
        String formatStr = "yyyy-MM-dd";
        SimpleDateFormat df = new SimpleDateFormat(formatStr,
                Locale.getDefault());
        try {
            Date date = df.parse(start);
            return date.getTime();
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return 0;
    }

    public static Date getFromeDate(String start) {
        if (StringUtil.isBlank(start)) {
            return null;
        }
        String formatStr = "yyyy-MM-dd";
        SimpleDateFormat df = new SimpleDateFormat(formatStr,
                Locale.getDefault());
        try {
            Date date = df.parse(start);
            return date;
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static String getDay(long time) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(time);
        return calendar.get(Calendar.DAY_OF_MONTH) + "";
    }

    public static boolean isOneDay(long time1, long time2) {
        Calendar cal1 = Calendar.getInstance();
        cal1.setTimeInMillis(time1);
        Calendar cal2 = Calendar.getInstance();
        cal2.setTimeInMillis(time2);
        return isOneDay(cal1, cal2);
    }

    public static boolean isOneDay(Calendar cal1, Calendar cal2) {
        if (cal1.get(Calendar.YEAR) == cal2.get(Calendar.YEAR)) {
            if (cal1.get(Calendar.DAY_OF_YEAR) == cal2
                    .get(Calendar.DAY_OF_YEAR)) {
                return true;
            }
        }
        return false;
    }

    /*
     * 时间转换
     */
    public static long getDateConvert(long time) {
        SimpleDateFormat sDateFormat = new SimpleDateFormat("ss");
        String date = sDateFormat.format(new Date(time));
        int ss = Integer.parseInt(date);
        Long newTime;
        if (ss > 30) {
            newTime = time + ((60 - ss) * 1000);
        } else {
            newTime = time;
        }
        return newTime;
    }

    public static int[] getPerviousWeekSunday() {
        Calendar cal = Calendar.getInstance();
        int[] time = {cal.get(Calendar.YEAR), cal.get(Calendar.MONTH) + 1,
                cal.get(Calendar.DATE), cal.get(Calendar.DAY_OF_WEEK) - 1};
        return time;
    }

    public static String getDateData(long timeStamp) {
        String date = getDateStr(getCalendarByLong(timeStamp));
        return date;
    }

    public static String getNextYeardate(int i) {
        Calendar cal = new GregorianCalendar();
        int year = cal.get(Calendar.YEAR) + i;
        int month = cal.get(Calendar.MONTH) + 1;
        int day = cal.get(Calendar.DATE);
        return year + "-" + month + "-" + day;
    }

    public static String getNextYeardate(long l, int i) {
        Calendar cal = new GregorianCalendar();
        int year = cal.get(Calendar.YEAR) + i;
        int month = cal.get(Calendar.MONTH) + 1;
        int day = cal.get(Calendar.DATE);
        return year + "-" + month + "-" + day;
    }

    /*
     * 返回日期格式：06月25日 15:13   6/25 15:13
     */
//    public static String getDateMDHM(long time) {
//        Date now = new Date(time);
//        SimpleDateFormat f = new SimpleDateFormat("M/d HH:mm",
//                Locale.getDefault());
//        if (LanguageSetting.isCurrentChinese())
//            f = new SimpleDateFormat("M月d日 HH:mm",
//                    Locale.getDefault());
//        return f.format(now);
//    }

    public static String getSimpleDateStr(long time) {
        String formatStr = "yyyy-MM-dd";
        SimpleDateFormat df = new SimpleDateFormat(formatStr,
                Locale.getDefault());
        String dateString = df.format(time);
        return dateString;
    }

    public static String getKaoNameDate(long time) {
        String formatStr = "MM-dd";
        SimpleDateFormat df = new SimpleDateFormat(formatStr,
                Locale.getDefault());
        String dateString = df.format(time);
        return dateString;
    }

    public static long getFirstDayOfMonth(int year, int month) {
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd",
                Locale.getDefault());
        Calendar cal = Calendar.getInstance();
        cal.set(Calendar.YEAR, year);
        cal.set(Calendar.MONTH, month - 1);
        cal.set(Calendar.DAY_OF_MONTH, cal.getMinimum(Calendar.DATE));
        String startTime = formatter.format(cal.getTime()) + " 00:00:00";
        System.out.println(startTime);
        return getStringToDate(startTime);
    }

    public static long getDaySeconds(long time) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(new Date(time));
        cal.set(Calendar.HOUR_OF_DAY, 0);
        cal.set(Calendar.SECOND, 0);
        cal.set(Calendar.MINUTE, 0);
        return cal.getTime().getTime();
    }

    public static long getMonthLastDay(int year, int month) {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        Calendar cal = Calendar.getInstance();
        cal.set(Calendar.YEAR, year);
        cal.set(Calendar.MONTH, month - 1);
        cal.set(Calendar.DAY_OF_MONTH, cal.getActualMaximum(Calendar.DATE));
        String last = format.format(cal.getTime());
        return getStringToDate(last + " 23:59:59");
    }

    public static long getLongDate(String time) {
        SimpleDateFormat format1 = new SimpleDateFormat("yyyy-M-d HH:mm:ss");
        Date date = null;// 有异常要捕获
        try {
            date = format1.parse(time);
            format1 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            String newD = format1.format(date);
            return getStringToDate(newD);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return 0;
    }

//    public static CharSequence milliToMinute(long milli) {
//        int seconds = (int) (milli / 1000);
//        if (seconds < 60) {
//            return seconds
//                    + BaseApplication.getContext().getString(
//                    R.string.time_unit_second);
//        } else {
//            int minute = seconds / 60;
//            int sec = seconds % 60;
//            return minute
//                    + BaseApplication.getContext().getString(
//                    R.string.time_unit_minute)
//                    + sec
//                    + BaseApplication.getContext().getString(
//                    R.string.time_unit_second);
//        }
//    }

    /*
     * 返回日期格式： 2014年06月25日
     */
    public static String getTimeZh(long time) {
        Date now = new Date(time);
        SimpleDateFormat f = new SimpleDateFormat("yyyy年MM月dd日",
                Locale.getDefault());
        return f.format(now);
    }

    public static String getTimeEn(long time) {
        Date now = new Date(time);
        SimpleDateFormat f = new SimpleDateFormat("yyyy-MM-dd",
                Locale.getDefault());
        return f.format(now);
    }

    public static String getTimeDay(long time) {
        Date now = new Date(time);
        SimpleDateFormat f = new SimpleDateFormat("dd",
                Locale.getDefault());
        return f.format(now);
    }
//
//    public static String getTime(long time) {
//        if (LanguageSetting.isCurrentChinese())
//            return getTimeZh(time);
//        else
//            return getTimeEn(time);
//    }

    public static String getHourMin(long time) {
        Date now = new Date(time);
        SimpleDateFormat f = new SimpleDateFormat("HH:mm",
                Locale.getDefault());
        return f.format(now);
    }

    public static String getHourMinSce(long time) {
        Date now = new Date(time);
        SimpleDateFormat f = new SimpleDateFormat("HH:mm:ss",
                Locale.getDefault());
        return f.format(now);
    }

    public static String getMonthTime(long time) {
        Date now = new Date(time);
        SimpleDateFormat f = new SimpleDateFormat("yyyy-MM",
                Locale.getDefault());
        return f.format(now);
    }

    public static long getMonthToMseconds(String time) {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM");
        Date date = null;
        try {
            date = format.parse(time);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return date.getTime();
    }

    public static long getDayToMseconds(String time) {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        Date date = null;
        try {
            date = format.parse(time);
            return date.getTime();
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return 0;
    }

    public static String getTimeZheng(long time) {
        Date now = new Date(time);
        SimpleDateFormat f = new SimpleDateFormat("yyyy年MM月dd日 HH:mm",
                Locale.getDefault());
        return f.format(now);
    }

    public static String getDateStrNoSplite(long time) {
        SimpleDateFormat f = new SimpleDateFormat("yyyyMMdd",
                Locale.getDefault());
        return f.format(new Date(time));
    }

    public static final int SECONDS_IN_DAY = 60 * 60 * 24;
    public static final long MILLIS_IN_DAY = 1000L * SECONDS_IN_DAY;

    public static boolean isSameDayOfMillis(final long ms1, final long ms2) {
        final long interval = ms1 - ms2;
        return interval < MILLIS_IN_DAY
                && interval > -1L * MILLIS_IN_DAY
                && toDay(ms1) == toDay(ms2);
    }

    private static long toDay(long millis) {
        return (millis + TimeZone.getDefault().getOffset(millis)) / MILLIS_IN_DAY;
    }

    public static int getBetweenDays(long startTime, long endTime) {
        Calendar startTimeCal = Calendar.getInstance();
        startTimeCal.setTimeInMillis(startTime);
        Calendar endTimeCal = Calendar.getInstance();
        endTimeCal.setTimeInMillis(endTime);
        return getBetweenDays(startTimeCal, endTimeCal);
    }

    public static int getBetweenDays(Calendar startTimeCal, Calendar endTimeCal) {
        long startTime = getDayStart(startTimeCal);
        long endTime = getDayStart(endTimeCal);
        long ongDay = 24 * 60 * 60 * 1000;
        return (int) ((Math.abs(endTime - startTime) / ongDay));
    }

    public static String getDayOfWeekStr(String dayOfWeek) {
        String[] strs = dayOfWeek.split(",");
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < strs.length; i++) {
            sb.append(TimeUtil.getCurWeek(Integer.parseInt(strs[i])) + ",");
        }
        return sb.substring(0, sb.length() - 1);
    }

    public static String getSceondsToTime(long seconds) {
        int hour = (int) (seconds / 3600);
        int min = (int) ((seconds % 3600) / 60);
        int second = (int) ((seconds % 3600) % 60);
        String ho, m, s;
        if (hour < 10) {
            ho = "0" + hour;
        } else ho = hour + "";
        if (min < 10) {
            m = "0" + min;
        } else m = min + "";
        if (second < 10) {
            s = "0" + second;
        } else s = second + "";
        return ho + ":" + m + ":" + s;
    }
}
