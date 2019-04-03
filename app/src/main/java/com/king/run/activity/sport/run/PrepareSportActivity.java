package com.king.run.activity.sport.run;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.res.AssetFileDescriptor;
import android.content.res.AssetManager;
import android.graphics.Typeface;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.content.ContextCompat;
import android.view.View;
import android.widget.TextView;

import com.king.run.R;
import com.king.run.activity.sport.walk.VoiceSettingActivity;
import com.king.run.base.BaseActivity;
import com.king.run.intface.GoalListener;
import com.king.run.util.PrefName;

import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.Event;
import org.xutils.view.annotation.ViewInject;

import java.io.IOException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@ContentView(R.layout.activity_prepare_sport)
public class PrepareSportActivity extends BaseActivity {
    private int refreshTime = 3;
    private final static int HANDLE_UPDATE_TIME = 1;
    private final static int HANDLE_CHECK_SIGN_TYPE = 2;
    private final static int VOICE_SETTING_CODE = 22;
    private ExecutorService singleThreadExecutor = Executors.newSingleThreadExecutor();
    private boolean flag;
    @SuppressLint("HandlerLeak")
    private Handler mhandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case HANDLE_UPDATE_TIME:
                    if (refreshTime == 0) {
                        tv_second.setText("GO");
                        Bundle bundle = new Bundle();
                        if (hasGoal) {
                            bundle.putInt(GoalListener.GOAL_TYPE, goalType);
                            bundle.putDouble(GoalListener.GOAL_VALUE, goalValue);
                            if (runWay == 0) {
                                jumpBundleActvity(IndoorRunActivity.class, bundle);
                            } else {
                                bundle.putInt("run_way", runWay);
                                jumpBundleActvity(OutDoorRunActivity.class, bundle);
                            }
                        } else {
                            if (runWay == 0) {
                                jumpActvity(IndoorRunActivity.class);
                            } else {
                                bundle.putInt("run_way", runWay);
                                jumpBundleActvity(OutDoorRunActivity.class, bundle);
                            }
                        }
                        finish();
                    } else
                        tv_second.setText(refreshTime + "");
                    break;
                case HANDLE_CHECK_SIGN_TYPE:
//                    if (runWay == 0) {
//                        jumpActvity(IndoorRunActivity.class);
//                    } else {
//                        jumpActvity(OutDoorRunActivity.class);
//                    }
//                    tv_second.setText("0");
//                    mflag = false;
//                    finish();
                    break;
            }
        }
    };
    @ViewInject(R.id.tv_second)
    TextView tv_second;

    private boolean hasGoal;
    private int goalType = -1;
    private double goalValue = 0;
    private int runWay = 0;//1为室外跑//2为骑行

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        init();
        initViews();
    }

    private void initViews() {
        Typeface typeface = Typeface.createFromAsset(getAssets(), "fonts/ReductoCondSSK.ttf");
        tv_second.setTypeface(typeface);
        title_relate_layout.setBackgroundColor(ContextCompat.getColor(context, R.color.common_black_bg));
        title_iv_back.setVisibility(View.GONE);
        title_iv_right.setBackgroundResource(R.mipmap.train_icon_setting);
        tv_second.setText(refreshTime + "");
        Bundle bundle = getIntent().getExtras();
        runWay = bundle.getInt("run_way", 0);
        if (!getIntent().hasExtra(GoalListener.GOAL_TYPE)) {
            hasGoal = false;
        } else {
            goalType = bundle.getInt(GoalListener.GOAL_TYPE, -1);
            if (goalType != -1) {
                hasGoal = true;
                goalValue = bundle.getDouble(GoalListener.GOAL_VALUE, 0);
            }
        }
    }

    @Event(value = {R.id.btn_start_soon, R.id.btn_cancel, R.id.ly_advertisement, R.id.title_right})
    private void getEvent(View view) {
        switch (view.getId()) {
            case R.id.btn_start_soon:
                playMusic();
                findViewById(R.id.btn_start_soon).setClickable(false);
                break;
            case R.id.btn_cancel:
                finish();
                break;
            case R.id.ly_advertisement:
//                jumpActvity(WarmUpVideoActivity.class);
                break;
            case R.id.title_right:
                jumpBundleActvityforResult(VoiceSettingActivity.class, null, VOICE_SETTING_CODE);
                break;
        }
    }

    private void playMusic() {
        AssetManager assetManager;
        MediaPlayer player;
        try {
            player = new MediaPlayer();
            assetManager = getAssets();
            String fileName;
            if (PrefName.getVoiceSetting(context) == 0)
                fileName = "voice_man.m4a";
            else
                fileName = "voice_woman.m4a";
            AssetFileDescriptor fileDescriptor = assetManager.openFd(fileName);
            player.setDataSource(fileDescriptor.getFileDescriptor(), fileDescriptor.getStartOffset(),
                    fileDescriptor.getStartOffset());
            player.prepare();
            player.start();
            Thread.sleep(1000);
            TimeListener();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    //新建一个线程 每秒发送一次消息更新显示的时间
    private void TimeListener() {
        flag = true;
        refreshTime = 4;
        singleThreadExecutor.execute(new Runnable() {
            @Override
            public void run() {
                while (flag) {
                    refreshTime--;
                    int mWhat = HANDLE_UPDATE_TIME;
                    if (refreshTime < 0) {
                        mWhat = HANDLE_CHECK_SIGN_TYPE;
                        mhandler.sendEmptyMessage(mWhat);
                        break;
                    }
                    mhandler.sendEmptyMessage(mWhat);
                    try {
                        Thread.sleep(1000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        flag = false;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode != RESULT_OK) return;
//        if (requestCode == VOICE_SETTING_CODE) {
//            TimeListener();
//        }
    }
}
