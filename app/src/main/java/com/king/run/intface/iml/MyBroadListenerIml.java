package com.king.run.intface.iml;

import android.content.Context;
import android.content.Intent;

import com.king.run.intface.MyBroadcastListener;

/**
 * 广播接口实现类
 */

public class MyBroadListenerIml implements MyBroadcastListener {
    private static MyBroadListenerIml instance = null;
    private static Context context;

    private MyBroadListenerIml() {

    }

    public static MyBroadListenerIml getInstance(Context mContext) {
        synchronized (MyBroadListenerIml.class) {
            if (instance == null) {
                context = mContext;
                instance = new MyBroadListenerIml();
            }
        }
        return instance;
    }

    @Override
    public void startRun() {
        sendBroadCast(ACTION_RUN_START);
    }

    @Override
    public void finishRun() {
        sendBroadCast(ACTION_RUN_END);
    }

    @Override
    public void pauseRun() {
        sendBroadCast(ACTION_RUN_PARSE);
    }

    @Override
    public void onResume() {
        sendBroadCast(ACTION_RUN_RESUME);
    }

    @Override
    public void onStepChanged(int steps, long seconds) {
        Intent intent = new Intent(ACTION_RUN_CHANGED);
        intent.putExtra(EXTRA_RUNNING_STEPS, steps);
        intent.putExtra(EXTRA_RUNNING_SECONDS, seconds);
        context.sendBroadcast(intent);
    }

    private void sendBroadCast(String mAction) {
        Intent intent = new Intent(mAction);
        context.sendBroadcast(intent);
    }
}
