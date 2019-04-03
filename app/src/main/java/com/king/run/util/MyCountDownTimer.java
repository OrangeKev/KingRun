package com.king.run.util;

import android.content.Context;
import android.os.CountDownTimer;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.king.run.R;


/**
 * 作者：shuizi_wade on 2017/1/16 15:55
 * 邮箱：674618016@qq.com
 */
public class MyCountDownTimer extends CountDownTimer {
    private Button btn_djs;
    private TextView tv_count;
    private Context context;

    public MyCountDownTimer(Context context, long millisInFuture, long countDownInterval, Button btn, TextView tv_count) {
        super(millisInFuture, countDownInterval);
        btn_djs = btn;
        this.tv_count = tv_count;
        this.context = context;
    }

    //计时过程
    @Override
    public void onTick(long l) {
        //防止计时过程中重复点击
        btn_djs.setClickable(false);
        btn_djs.setVisibility(View.GONE);
        tv_count.setText(String.format(context.getResources().getString(R.string.code_send), l / 1000));
    }

    //计时完毕的方法
    @Override
    public void onFinish() {
        //重新给Button设置文字
        tv_count.setVisibility(View.GONE);
        btn_djs.setVisibility(View.VISIBLE);
        btn_djs.setText("重新获取");
        btn_djs.setBackgroundResource(R.drawable.btn_get_code);
        //设置可点击
        btn_djs.setClickable(true);
    }
}