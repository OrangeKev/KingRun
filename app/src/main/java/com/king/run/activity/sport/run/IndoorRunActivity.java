package com.king.run.activity.sport.run;

import android.annotation.SuppressLint;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.ServiceConnection;
import android.graphics.Typeface;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.os.SystemClock;
import android.support.v4.view.MotionEventCompat;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Chronometer;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.king.run.R;
import com.king.run.base.BaseActivity;
import com.king.run.intface.GoalListener;
import com.king.run.intface.MyBroadcastListener;
import com.king.run.intface.http.ReqBack;
import com.king.run.intface.http.iml.SportIml;
import com.king.run.intface.http.request.SportReq;
import com.king.run.intface.iml.GoalIml;
import com.king.run.intface.iml.MyBroadListenerIml;
import com.king.run.model.http.req.ExerciseReq;
import com.king.run.receiver.MediaService;
import com.king.run.util.PrefName;
import com.king.run.util.StepAlgorithm;
import com.king.run.util.Utils;
import com.king.run.view.MusicSettingDialog;
import com.king.run.view.ObservableScrollView;
import com.orhanobut.logger.Logger;

import org.json.JSONException;
import org.json.JSONObject;
import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.Event;
import org.xutils.view.annotation.ViewInject;

import steps.teller.step.utils.SharedPreferencesUtils;

@ContentView(R.layout.activity_indoor_run)
public class IndoorRunActivity extends BaseActivity implements ReqBack {
    private int refreshTime = 4;
    private final static int HANDLE_UPDATE_TIME = 1;
    private final static int HANDLE_CHECK_SIGN_TYPE = 2;
    private boolean mflag;
    @ViewInject(R.id.hor_scr)
    ObservableScrollView hor_scr;
    @ViewInject(R.id.ly_go_on)
    LinearLayout ly_go_on;
    @ViewInject(R.id.ly_right_to_suspend)
    RelativeLayout ly_right_to_suspend;
    private float startX, startY, endX, endY;
    private boolean isStart = true;
    @ViewInject(R.id.tv_second_indoor)
    TextView tv_second_indoor;
    @ViewInject(R.id.tv_goal_km)
    TextView tvGoalKm;
    @ViewInject(R.id.ly_sport_target)
    RelativeLayout rlGole;
    @ViewInject(R.id.pb_run_goal)
    ProgressBar pb_run_finish;
    @ViewInject(R.id.chronometer_run_time)
    Chronometer chronometerRunTime;
    @ViewInject(R.id.tv_run_speed)
    private TextView tvRunSpeed;
    @ViewInject(R.id.tv_run_km)
    private TextView tvRunKm;
    @ViewInject(R.id.tv_run_kcal)
    private TextView tvRunKcal;
    @ViewInject(R.id.tv_percent)
    private TextView tv_percent;
    @ViewInject(R.id.iv_music)
    ImageView iv_music;

    private int width;
    private SharedPreferencesUtils sp;
    private RunBroadcast runBroadcast;
    private boolean hasGoal;
    private int goalType;
    private double goalValue;
    private int steps;
    private int progress;
    private MusicSettingDialog musicSettingDialog;
    private int id = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getIntent() == null || getIntent().getExtras() == null) {
            hasGoal = false;
        } else {
            hasGoal = true;
            Bundle bundle = getIntent().getExtras();
            goalType = bundle.getInt(GoalListener.GOAL_TYPE, -1);
            goalValue = bundle.getDouble(GoalListener.GOAL_VALUE, 0);
        }
        initTitleBar();
        initViews();
        initBroadcast();
        MyBroadListenerIml.getInstance(this).startRun();
        getMusicInfo();
        initMusicService();
    }

    private void initMusicService() {
        Intent intent = new Intent();
        intent.setClass(context, MediaService.class);
        Bundle bundle = new Bundle();
        bundle.putSerializable("detail", musicList.get(0));
        intent.putExtras(bundle);
        startService(intent);
        bindService(intent, mServiceConnection, Context.BIND_AUTO_CREATE);
    }

    private void initBroadcast() {
        runBroadcast = new RunBroadcast();
        IntentFilter filter = new IntentFilter();
        filter.addAction(MyBroadcastListener.ACTION_RUN_CHANGED);
        registerReceiver(runBroadcast, filter);
    }


    private void initViews() {
        Typeface typeface = Typeface.createFromAsset(getAssets(), "fonts/ReductoCondSSK.ttf");
        tv_second_indoor.setTypeface(typeface);
        tvRunKm.setTypeface(typeface);
        chronometerRunTime.setTypeface(typeface);
        tvRunSpeed.setTypeface(typeface);
        tvRunKcal.setTypeface(typeface);
        tvRunSpeed.setText("0");
        width = Utils.getDisplayWidth(IndoorRunActivity.this);
        LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) ly_go_on.getLayoutParams();
        params.width = width;
        ly_go_on.setLayoutParams(params);
        LinearLayout.LayoutParams params1 = (LinearLayout.LayoutParams) ly_right_to_suspend.getLayoutParams();
        params1.width = width;
        ly_right_to_suspend.setLayoutParams(params1);
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                // 水平直接滚动800px，如果想效果更平滑可以使用smoothScrollTo(int x, int y)
                hor_scr.smoothScrollTo(2 * width, 0);
            }
        }, 500);
        hor_scr.setOnTouchListener(touchListener);
        sp = new SharedPreferencesUtils(this);
        if (!hasGoal) {
            rlGole.setVisibility(View.GONE);
            pb_run_finish.setVisibility(View.GONE);
        } else {
            rlGole.setVisibility(View.VISIBLE);
            pb_run_finish.setVisibility(View.VISIBLE);
            tvGoalKm.setText("运动目标:" + goalValue + GoalIml.getInstance().getGoalUnit(goalType));
        }
        chronometerRunTime.setFormat("%s");
//        chronometerRunTime.start();
        onRecordStart();
        chronometerRunTime.setOnChronometerTickListener(new Chronometer.OnChronometerTickListener() {
            @Override
            public void onChronometerTick(Chronometer chronometer) {
                recordingTime = SystemClock.elapsedRealtime() - chronometer.getBase();
                Log.e("recordingTime:", recordingTime + "");
                if (goalValue != 0 && goalType == 1) {
                    progress = (int) ((recordingTime * 100) / (goalValue * 1000));
                    pb_run_finish.setProgress(progress);
                    if (progress >= 100)
                        finishRunRide();
                }
            }
        });
    }

    @Event(value = {R.id.btn_go_on, R.id.btn_finish, R.id.iv_music})
    private void getEvent(View view) {
        switch (view.getId()) {
            case R.id.btn_go_on:
                tv_second_indoor.setVisibility(View.VISIBLE);
                mflag = true;
                TimeListener();
                break;
            case R.id.btn_finish:
                finishRunRide();
                break;
            case R.id.iv_music:
                musicDialog();
                break;
        }
    }

    private void musicDialog() {
        musicSettingDialog = new MusicSettingDialog(context, new MusicSettingDialog.ClickListener() {
            @Override
            public void next() {
                mMyBinder.nextMusic();
                String name = mMyBinder.getCurrent();
                musicSettingDialog.tv_music.setText(name);
            }

            @Override
            public void pre() {
                mMyBinder.preciousMusic();
                String name = mMyBinder.getCurrent();
                musicSettingDialog.tv_music.setText(name);
            }
        });
        musicSettingDialog.show();
        String name = mMyBinder.getCurrent();
        musicSettingDialog.tv_music.setText(name);
    }

    private void finishRunRide() {
        showDia("加载中...");
        MyBroadListenerIml.getInstance(IndoorRunActivity.this).finishRun();
        uploadData();
    }


    @Override
    public void onBackPressed() {
        Toast.makeText(this, "请结束室内跑步", Toast.LENGTH_SHORT).show();
    }

    private void scroll(final int x) {
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                // 水平直接滚动800px，如果想效果更平滑可以使用smoothScrollTo(int x, int y)
                hor_scr.smoothScrollTo(x, 0);
            }
        }, 500);
    }

    private View.OnTouchListener touchListener = new View.OnTouchListener() {
        @Override
        public boolean onTouch(View view, MotionEvent motionEvent) {
            switch (motionEvent.getAction()) {
                case MotionEvent.ACTION_DOWN:
                    startX = MotionEventCompat.getX(motionEvent, motionEvent.getActionIndex());
                    startY = MotionEventCompat.getY(motionEvent, motionEvent.getActionIndex());
                    Log.d("sss", startX + ";" + startX);
                    break;
                case MotionEvent.ACTION_UP:
                    endX = MotionEventCompat.getX(motionEvent, motionEvent.getActionIndex());
                    endY = MotionEventCompat.getY(motionEvent, motionEvent.getActionIndex());
                    Log.d("sssddd", endX + ";" + endX);
                    if (isStart) {
                        if (Math.abs(endX - startX) > width / 4) {
                            //暂停
                            scroll(0);
                            isStart = false;
//                            chronometerRunTime.stop();
                            onRecordPause();
                            MyBroadListenerIml.getInstance(IndoorRunActivity.this).pauseRun();
                        } else {
                            //开始
                            scroll(2 * width);
                        }
                    } else {
                        if (Math.abs(startX - endX) > width / 4) {
                            //开始
                            scroll(2 * width);
                            isStart = true;
//                            chronometerRunTime.start();
                            onRecordStart();
                            MyBroadListenerIml.getInstance(IndoorRunActivity.this).startRun();
                        } else {
                            //暂停
                            scroll(0);
                        }
                    }
                    break;
            }
            return false;
        }
    };

    //新建一个线程 每秒发送一次消息更新显示的时间
    private void TimeListener() {
        new Thread() {
            @Override
            public void run() {
                refreshTime = 4;
                while (mflag) {
                    refreshTime--;
                    Message msg = new Message();
                    msg.what = HANDLE_UPDATE_TIME;
                    if (refreshTime == 0) {
                        msg.what = HANDLE_CHECK_SIGN_TYPE;
                    }
                    handler.sendMessage(msg);
                    try {
                        Thread.sleep(1000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        }.start();
    }


    private void uploadData() {
        ExerciseReq req = new ExerciseReq();
        req.setType(SportReq.TYPE_RUN);
        req.setKilometre(tvRunKm.getText().toString());
        req.setCalorie(tvRunKcal.getText().toString());
        req.setSecond((recordingTime / 1000) + "");
        req.setStep(steps + "");
        if (hasGoal) {
            req.setTarget(goalValue + "");
        }
        req.setProgress(progress + "");
        req.setPace(tvRunSpeed.getText().toString());
        req.setToken(PrefName.getToken(context));
        req.setTime(System.currentTimeMillis() + "");
        SportIml.getInstance(this).uploadData(req, this);
    }

    @Override
    public void onSuccess(String result) {
        Logger.json(result);
        try {
            JSONObject jsonObject = new JSONObject(result);
            id = jsonObject.getInt("id");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        hideDia();
        finishJump();
    }

    @Override
    public void onError(String error) {
        finishJump();
        hideDia();
        Toast.makeText(this, "上传失败", Toast.LENGTH_SHORT).show();
    }

    private void finishJump() {
        Bundle bundle = new Bundle();
        bundle.putString("km", tvRunKm.getText().toString());
        bundle.putString("time", (recordingTime / 1000) + "");
        bundle.putString("speed", tvRunSpeed.getText().toString());
        bundle.putString("kcal", tvRunKcal.getText().toString());
        bundle.putBoolean("hasGoal", hasGoal);
        bundle.putInt("id", id);
        if (hasGoal) {
            bundle.putString("goal", tvGoalKm.getText().toString());
            bundle.putInt("percent", progress);
        }
        jumpBundleActvity(RunFinishActivity.class, bundle);
        onRecordStop();
        finish();
    }

    class RunBroadcast extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            if (action.equals(MyBroadcastListener.ACTION_RUN_CHANGED)) {
                steps = intent.getIntExtra(MyBroadcastListener.EXTRA_RUNNING_STEPS, -1);
                long seconds = intent.getLongExtra(MyBroadcastListener.EXTRA_RUNNING_SECONDS, -1);
                tvRunKm.setText(StepAlgorithm.getDisString(steps));
                tvRunSpeed.setText(StepAlgorithm.getSpeed(steps, recordingTime / 1000));
                String curKca = StepAlgorithm.getKcal(Double.parseDouble(PrefName.getWeight(context)), StepAlgorithm.getDisDouble(steps));
                tvRunKcal.setText(curKca);
                if (hasGoal && goalType != -1) {
                    pb_run_finish.setVisibility(View.VISIBLE);
                    progress = 0;
                    switch (goalType) {
                        case GoalListener.GOAL_TYPE_DISTANCE:
                            double curDistance = StepAlgorithm.getDisDouble(steps) * 1000;
                            progress = Utils.getPercent(curDistance, goalValue);
                            break;
                        case GoalListener.GOAL_TYPE_TIME:
//                            double curTime = getSeconds();
//                            progress = Utils.getPercent(curTime, goalValue * 60);
                            break;
                        case GoalListener.GOAL_TYPE_KCAL:
                            progress = Utils.getPercent(Double.valueOf(curKca), goalValue * 60);
                            break;
                        default:
                            break;
                    }
                    pb_run_finish.setProgress(progress);
                    tv_percent.setText(progress + "%");
                    if (progress >= 100) {
                        finishRunRide();
                    }
                }
            }

        }

    }

    public int getSeconds() {
        int seconds = 0;
        if (chronometerRunTime != null) {
            String mText = chronometerRunTime.getText().toString();
            String[] s = mText.split(":");
            if (s.length == 3) {
                seconds = Integer.valueOf(s[0]) * 60 * 60 + Integer.valueOf(s[1]) * 60 + Integer.valueOf(s[2]);
            } else if (s.length == 2) {
                seconds = Integer.valueOf(s[0]) * 60 + Integer.valueOf(s[1]);
            }
        }
        return seconds;
    }

    @SuppressLint("HandlerLeak")
    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case HANDLE_UPDATE_TIME:
                    tv_second_indoor.setText(refreshTime + "");
                    break;
                case HANDLE_CHECK_SIGN_TYPE:
//                    chronometerRunTime.start();
                    onRecordStart();
                    isStart = true;
                    MyBroadListenerIml.getInstance(IndoorRunActivity.this).onResume();
                    tv_second_indoor.setText("0");
                    mflag = false;
                    scroll(2 * width);
                    tv_second_indoor.setVisibility(View.GONE);
                    break;
            }
        }
    };


    @Override
    protected void onDestroy() {
        if (runBroadcast != null) {
            unregisterReceiver(runBroadcast);
        }
        mMyBinder.closeMedia();
        unbindService(mServiceConnection);
        super.onDestroy();
    }

    private long recordingTime = 0;// 记录下来的总时间

    public void onRecordStart() {
        chronometerRunTime.setBase(SystemClock.elapsedRealtime() - recordingTime);// 跳过已经记录了的时间，起到继续计时的作用
        chronometerRunTime.start();
    }

    public void onRecordPause() {
        chronometerRunTime.stop();
        recordingTime = SystemClock.elapsedRealtime() - chronometerRunTime.getBase();// 保存这次记录了的时间
    }

    public void onRecordStop() {
        recordingTime = 0;
        chronometerRunTime.setBase(SystemClock.elapsedRealtime());
    }


    private MediaService.MyBinder mMyBinder;
    //“绑定”服务的intent
    Intent MediaServiceIntent;
    private ServiceConnection mServiceConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            mMyBinder = (MediaService.MyBinder) service;
            Log.d("", "Service与Activity已连接");
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {

        }
    };
}
