package com.king.run.util;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;

import com.king.run.activity.mine.model.RemindData;
import com.king.run.receiver.AlarmReceiver;
import com.orhanobut.logger.Logger;

import java.util.Calendar;
import java.util.TimeZone;

/**
 * 作者：shuizi_wade on 2018/1/5 10:58
 * 邮箱：674618016@qq.com
 */
public class StartRemind {
    public static void MondayRemind(Context context, int hour, int min) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeZone(TimeZone.getTimeZone("GMT+8"));
        calendar.set(Calendar.DAY_OF_WEEK, calendar.getFirstDayOfWeek());
        calendar.add(Calendar.DATE, +1);
    }

    public static void start(Context context, RemindData remindData) {
        //得到日历实例，主要是为了下面的获取时间
        Calendar mCalendar = Calendar.getInstance();
        long curTime = mCalendar.getTimeInMillis();
        // 这里时区需要设置一下，不然可能个别手机会有8个小时的时间差
        mCalendar.setTimeZone(TimeZone.getTimeZone("GMT+8"));
        mCalendar.set(Calendar.HOUR_OF_DAY, remindData.getHour());
        mCalendar.set(Calendar.MINUTE, remindData.getMin());
        mCalendar.set(Calendar.SECOND, 0);
        mCalendar.set(Calendar.MILLISECOND, 0);
        //AlarmReceiver.class为广播接受者
        Intent intent = new Intent(context, AlarmReceiver.class);
        PendingIntent pi = PendingIntent.getBroadcast(context, remindData.getId(), intent, PendingIntent.FLAG_UPDATE_CURRENT);
        //得到AlarmManager实例
        AlarmManager am = (AlarmManager) context.getSystemService(context.ALARM_SERVICE);
        if (curTime < mCalendar.getTimeInMillis())
            am.set(AlarmManager.RTC_WAKEUP, mCalendar.getTimeInMillis(), pi);
    }

    public static void cancel(Context context, RemindData remindData) {
        Intent intent = new Intent(context, AlarmReceiver.class);
        // 创建PendingIntent对象
        AlarmManager am = (AlarmManager) context.getSystemService(context.ALARM_SERVICE);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(context, remindData.getId(), intent, PendingIntent.FLAG_UPDATE_CURRENT);
        am.cancel(pendingIntent);
    }
}
