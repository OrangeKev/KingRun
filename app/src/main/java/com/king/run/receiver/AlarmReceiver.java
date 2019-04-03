package com.king.run.receiver;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.support.v4.app.NotificationCompat;

import com.king.run.R;
import com.king.run.activity.mine.TrainRemindActivity;

/**
 * 作者：shuizi_wade on 2018/1/4 09:43
 * 邮箱：674618016@qq.com
 */
public class AlarmReceiver extends BroadcastReceiver {
    private NotificationCompat.Builder mBuilder;
    private Context context;
    /**
     * 通知管理对象
     */
    private NotificationManager mNotificationManager;

    @Override
    public void onReceive(Context context, Intent intent) {
        this.context = context;
        initNotifi();
    }

    private void initNotifi() {
        mBuilder = new NotificationCompat.Builder(context);
        mBuilder.setContentTitle(context.getResources().getString(R.string.app_name))
                .setContentText("您有一个训练提醒")
                .setContentIntent(getDefalutIntent(Notification.FLAG_ONGOING_EVENT))
                .setWhen(System.currentTimeMillis())//通知产生的时间，会在通知信息里显示
                .setPriority(Notification.PRIORITY_DEFAULT)//设置该通知优先级
                .setAutoCancel(true)//设置这个标志当用户单击面板就可以让通知将自动取消
//                .setOngoing(true)//ture，设置他为一个正在进行的通知。他们通常是用来表示一个后台任务,用户积极参与(如播放音乐)或以某种方式正在等待,因此占用设备(如一个文件下载,同步操作,主动网络连接)
                .setSmallIcon(R.mipmap.ic_launcher_app);
        mNotificationManager = (NotificationManager) context.getSystemService(context.NOTIFICATION_SERVICE);

        Intent hangIntent = new Intent(context, TrainRemindActivity.class);
        hangIntent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);// 设置跳转标志为如此Activity存在则把其从任务堆栈中取出放到最上方
        hangIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);// 在跳转之前情况当前任务堆栈中此Activity的顶部任务
        PendingIntent hangPendingIntent = PendingIntent.getActivity(context, 0, hangIntent, PendingIntent.FLAG_CANCEL_CURRENT);
        Notification notification = mBuilder.setContentTitle(context.getResources().getString(R.string.app_name))
                .setContentText("您有一个训练提醒，请点击查看")
                .setWhen(System.currentTimeMillis())//通知产生的时间，会在通知信息里显示
                .setContentIntent(hangPendingIntent)
                .build();
        mNotificationManager.notify(11, notification);
    }

    /**
     * @获取默认的pendingIntent,为了防止2.3及以下版本报错
     * @flags属性: 在顶部常驻:Notification.FLAG_ONGOING_EVENT
     * 点击去除： Notification.FLAG_AUTO_CANCEL
     */
    public PendingIntent getDefalutIntent(int flags) {
        PendingIntent pendingIntent = PendingIntent.getActivity(context, 1, new Intent(), flags);
        return pendingIntent;
    }
}
