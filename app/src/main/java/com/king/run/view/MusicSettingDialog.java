package com.king.run.view;

import android.app.Activity;
import android.app.Dialog;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.media.AudioManager;
import android.os.Bundle;
import android.os.IBinder;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.LinearInterpolator;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;

import com.king.run.R;
import com.king.run.activity.music.model.MusicInfo;
import com.king.run.receiver.AudioService;
import com.king.run.util.Utils;

import java.util.ArrayList;
import java.util.List;

/**
 * 作者：shuizi_wade on 2017/9/13 13:51
 * 邮箱：674618016@qq.com
 */
public class MusicSettingDialog extends Dialog {
    private Context context;
    private ClickListener clickListener;
    public TextView tv_music;

    public MusicSettingDialog(Context context,  ClickListener listener) {
        //给dialog定制了一个主题（透明背景，无边框，无标题栏，浮在Activity上面，模糊）
        super(context, R.style.run_type_dialog);
        setContentView(R.layout.dialog_music_setting);
        this.context = context;
        clickListener = listener;
        initView();
    }

    private void initView() {
        findViewById(R.id.iv_next).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                clickListener.next();
            }
        });
        findViewById(R.id.iv_pre).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                clickListener.pre();
            }
        });
        tv_music = findViewById(R.id.tv_music);
        final AudioManager am = (AudioManager) context.getSystemService(Context.AUDIO_SERVICE);
        int max = am.getStreamMaxVolume(AudioManager.STREAM_MUSIC);// 3
        int current = am.getStreamVolume(AudioManager.STREAM_MUSIC);
        SeekBar seekbar = findViewById(R.id.seekbar);
        seekbar.setMax(max);
        seekbar.setProgress(current);
        seekbar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
                am.setStreamVolume(AudioManager.STREAM_MUSIC, i,
                        AudioManager.FLAG_PLAY_SOUND);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });
        Log.e("service", "音乐音量值：" + max + "-" + current);
        findViewById(R.id.iv_cancel).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });
        //点击空白区域可以取消dialog
        this.setCanceledOnTouchOutside(true);
        //点击back键可以取消dialog
        this.setCancelable(true);
        Window window = this.getWindow();
        //让Dialog显示在屏幕的底部
//        window.setGravity(Gravity.BOTTOM);
        //设置窗口出现和窗口隐藏的动画
        window.setWindowAnimations(R.style.ios_bottom_dialog_anim);
        //设置BottomDialog的宽高属性
        WindowManager.LayoutParams lp = window.getAttributes();
        //WindowManager.LayoutParams.MATCH_PARENT;
        lp.width = Utils.getDisplayWidth((Activity) context);
        lp.height = Utils.getDisplayHeight((Activity) context);
        window.setAttributes(lp);
    }

    public interface ClickListener {
        void next();

        void pre();

//        void voiceSet(int progress);
    }
}
