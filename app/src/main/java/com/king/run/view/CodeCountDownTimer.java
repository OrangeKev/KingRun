package com.king.run.view;

import android.content.Context;
import android.os.CountDownTimer;
import android.widget.TextView;

import com.king.run.R;

/**
 * 作者：shuizi_wade on 2017/10/23 13:19
 * 邮箱：674618016@qq.com
 */
public class CodeCountDownTimer extends CountDownTimer {
    private TextView textView;
    private Context context;

    public CodeCountDownTimer(long millisInFuture, long countDownInterval, TextView textView, Context context) {
        super(millisInFuture, countDownInterval);
        this.textView = textView;
        this.context = context;
    }

    @Override
    public void onTick(long l) {
        textView.setClickable(false);
        textView.setText(String.format(context.getResources().getString(R.string.seconds_send_again), l / 1000));
    }

    @Override
    public void onFinish() {
        textView.setClickable(true);
        textView.setText("重新获取");
    }
}
