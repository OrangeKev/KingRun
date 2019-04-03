package com.king.run.util;

import android.app.Activity;

import com.king.run.activity.other.HomeActivity;

import java.util.Stack;


public class ActivityManager {
    private static ActivityManager instance;
    private Stack<Activity> activityStack;// activity�?

    // 单例模式
    public static ActivityManager getInstance() {
        if (instance == null) {
            instance = new ActivityManager();
        }
        return instance;
    }

    // 把一个activity压入栈中
    public void pushOneActivity(Activity actvity) {
        if (activityStack == null) {
            activityStack = new Stack<Activity>();
        }
        activityStack.add(actvity);
    }

    // 查找栈中是否存在指定的activity
    public boolean checkActivity(Class<?> cls) {
        if (activityStack != null) {
            for (Activity activity : activityStack) {
                if (activity.getClass().equals(cls)) {
                    return true;
                }
            }
        }
        return false;
    }

    // 获取栈顶的activity，先进后出原�?
    public Activity getLastActivity() {
        return activityStack.lastElement();
    }

    // 移除�?个activity
    public void popOneActivity(Activity activity) {
        if (activityStack != null && activityStack.size() > 0) {
            if (activity != null) {
                activity.finish();
                activityStack.remove(activity);
                activity = null;
            }

        }
    }

    // �?出所有activity
    public void finishAllActivity() {
        if (activityStack != null) {
            while (activityStack.size() > 0) {
                Activity activity = getLastActivity();
                if (activity == null)
                    break;
                popOneActivity(activity);
            }
        }
    }

    public Activity getTopActivity() {
        if (activityStack != null && !activityStack.isEmpty()) {
            return activityStack.get(activityStack.size() - 1);
        }
        return null;
    }
}
