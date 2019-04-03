package com.king.run.activity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.king.run.R;
import com.king.run.activity.other.HomeActivity;
import com.king.run.util.ActivityManager;
import com.king.run.util.Utils;

public class AllActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_all);
        Class cls;
        try {
            cls = (ActivityManager.getInstance().getTopActivity().getClass());
        } catch (Exception e) {
            cls = HomeActivity.class;
        }
        Intent intent = new Intent();
//        intent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);// 设置跳转标志为如此Activity存在则把其从任务堆栈中取出放到最上方
//        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);// 在跳转之前情况当前任务堆栈中此Activity的顶部任务
        intent.addCategory(Intent.CATEGORY_LAUNCHER);
        intent.setAction(Intent.ACTION_MAIN);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_RESET_TASK_IF_NEEDED);
        intent.setClass(AllActivity.this, cls);
        startActivity(intent);
        finish();
    }
}
