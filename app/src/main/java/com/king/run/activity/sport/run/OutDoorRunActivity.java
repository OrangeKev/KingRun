package com.king.run.activity.sport.run;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.graphics.Bitmap;
import android.graphics.Typeface;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.SystemClock;
import android.support.v4.view.MotionEventCompat;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Chronometer;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.BitmapDescriptor;
import com.baidu.mapapi.map.BitmapDescriptorFactory;
import com.baidu.mapapi.map.MapStatus;
import com.baidu.mapapi.map.MapStatusUpdateFactory;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.map.MyLocationConfiguration;
import com.baidu.mapapi.map.MyLocationData;
import com.baidu.mapapi.map.OverlayOptions;
import com.baidu.mapapi.map.Polyline;
import com.baidu.mapapi.map.PolylineOptions;
import com.baidu.mapapi.map.TextureMapView;
import com.baidu.mapapi.model.LatLng;
import com.baidu.mapapi.utils.DistanceUtil;
import com.github.mikephil.charting.data.Entry;
import com.king.run.R;
import com.king.run.activity.circle.PublishActivity;
import com.king.run.activity.statistics.SpeedDetailsActivity;
import com.king.run.baidumap.LocationService;
import com.king.run.base.BaseActivity;
import com.king.run.intface.GoalListener;
import com.king.run.intface.http.request.SportReq;
import com.king.run.model.BaseResult;
import com.king.run.receiver.MediaService;
import com.king.run.util.PrefName;
import com.king.run.util.StepAlgorithm;
import com.king.run.util.ThirdShare;
import com.king.run.util.Url;
import com.king.run.util.Utils;
import com.king.run.util.image.FileUtil;
import com.king.run.view.FinishDialog;
import com.king.run.view.MusicSettingDialog;
import com.king.run.view.ObservableScrollView;
import com.king.run.view.ShareToDialog;
import com.orhanobut.logger.Logger;

import net.arvin.pictureselector.entities.ImageEntity;

import org.json.JSONException;
import org.json.JSONObject;
import org.xutils.http.RequestParams;
import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.Event;
import org.xutils.view.annotation.ViewInject;

import java.io.File;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@ContentView(R.layout.activity_run_out)
public class OutDoorRunActivity extends BaseActivity {
    @ViewInject(R.id.tv_run_km)
    private TextView tvKm;
    @ViewInject(R.id.tv_speed_value)
    private TextView tvSpeed;
    @ViewInject(R.id.tv_kcal_value)
    private TextView tvKcal;

    @ViewInject(R.id.tv_km_1)
    private TextView tvKm1;
    @ViewInject(R.id.tv_time_1)
    private TextView tvTime1;
    @ViewInject(R.id.tv_speed_1)
    private TextView tvSpeed1;
    @ViewInject(R.id.tv_kcal_1)
    private TextView tvKcal1;

    @ViewInject(R.id.iv_refresh)
    private ImageButton ivRefresh;
    @ViewInject(R.id.chronometer_run_time)
    private Chronometer chronometerRunTime;
    @ViewInject(R.id.rl_running)
    private RelativeLayout rlRunning;
    @ViewInject(R.id.rl_finish)
    private RelativeLayout rlFinish;
    @ViewInject(R.id.iv_back)
    private ImageView ivBack;
    @ViewInject(R.id.iv_share)
    private ImageView ivShare;
    @ViewInject(R.id.ly_title)
    RelativeLayout ly_title;
    @ViewInject(R.id.bmapView)
    MapView mapView;
    private double latUp;
    private double lngUp;
    private static final double LOCATION_CHANGE_DISTINCTION_LAT = 0.00009797;//10米
    private static final double LOCATION_CHANGE_DISTINCTION_LNG = 0.00009000;//10米
    @ViewInject(R.id.hor_scr)
    ObservableScrollView hor_scr;
    private int width;
    @ViewInject(R.id.ly_go_on)
    LinearLayout ly_go_on;
    @ViewInject(R.id.ly_right_to_suspend)
    RelativeLayout ly_right_to_suspend;
    private float startX, startY, endX, endY;
    private boolean isStart = true;
    private LocationService locService;
    private BaiduMap mBaiduMap;
    private BitmapDescriptor mCurrentMarker;
    private double runDistance;
    private DecimalFormat df = new DecimalFormat("#.##");
    private List<LatLng> latLngList = new ArrayList<>();//记录覆盖物的经纬度
    private Polyline mColorfulPolyline;

    private boolean hasGoal;
    private int goalType;//0-dis,1-time,2-kcal
    private double goalValue;
    private boolean isFinished;
    @ViewInject(R.id.pb_run_finish)
    ProgressBar pb_run_finish;
    private int progress;
    private int run_way;//1是室外跑  2骑行
    private int yValueInt = 0;
    private ArrayList<String> xValues = new ArrayList<>();
    private ArrayList<Entry> yValue = new ArrayList<>();
    @ViewInject(R.id.pb_run_finish_details)
    ProgressBar pb_run_finish_details;
    private double lastDistance;
    private long lastRecordTime, totalTime;
    private int id = 0;

    private MusicSettingDialog musicSettingDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initTitleBar();
        initViews();
        setUI(true);
        checkLocationPermission();
        startLoc();
        checkStoragePermission();
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

    @Override
    protected void gotStoragePermissionResult(boolean isGrant) {
        super.gotStoragePermissionResult(isGrant);
        if (!isGrant) senToa(R.string.permission_phone_storage);
    }

    private void initViews() {
        mapView.removeViewAt(2);
        Typeface typeface = Typeface.createFromAsset(getAssets(), "fonts/ReductoCondSSK.ttf");
        tvKm.setTypeface(typeface);
        chronometerRunTime.setTypeface(typeface);
        tvSpeed.setTypeface(typeface);
        tvKcal.setTypeface(typeface);
        tvKm1.setTypeface(typeface);
        tvTime1.setTypeface(typeface);
        tvSpeed1.setTypeface(typeface);
        tvKcal1.setTypeface(typeface);
        Bundle bundle = getIntent().getExtras();
        if (!getIntent().hasExtra(GoalListener.GOAL_TYPE)) {
            hasGoal = false;
            pb_run_finish.setVisibility(View.GONE);
        } else {
            hasGoal = true;
            goalType = bundle.getInt(GoalListener.GOAL_TYPE, -1);
            goalValue = bundle.getDouble(GoalListener.GOAL_VALUE, 0);
            pb_run_finish.setVisibility(View.VISIBLE);
        }
        run_way = bundle.getInt("run_way");
        width = Utils.getDisplayWidth(OutDoorRunActivity.this);
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
        }, 100);
        hor_scr.setOnTouchListener(touchListener);
        chronometerRunTime.setOnChronometerTickListener(new Chronometer.OnChronometerTickListener() {
            @Override
            public void onChronometerTick(Chronometer chronometer) {
                recordingTime = SystemClock.elapsedRealtime() - chronometer.getBase();
                if (recordingTime != 0 && runDistance != 0) {
                    tvSpeed.setText(StepAlgorithm.getSpeedByDis(runDistance, recordingTime / 1000));
                    tvSpeed1.setText(StepAlgorithm.getSpeedByDis(runDistance, recordingTime / 1000));
                }
                if (goalValue != 0 && goalType == 1) {
                    progress = (int) ((recordingTime * 100) / (goalValue * 1000));
                    pb_run_finish.setProgress(progress);
                    if (progress >= 100) {
                        finishRunRide();
                        new FinishDialog(context, new FinishDialog.ClickListener() {
                        }, true).show();
                    }
                }
            }
        });
    }

    @Override
    protected void onStop() {
        stopLoc();
        super.onStop();
    }

    private void setUI(boolean isRunning) {
        if (isRunning) {
            rlRunning.setVisibility(View.VISIBLE);
            ivShare.setVisibility(View.GONE);
            rlFinish.setVisibility(View.GONE);
            ivBack.setVisibility(View.GONE);
            ivRefresh.setVisibility(View.GONE);
        } else {
            rlRunning.setVisibility(View.GONE);
            hor_scr.setVisibility(View.GONE);
            ivRefresh.setVisibility(View.GONE);

            rlFinish.setVisibility(View.VISIBLE);
            ivBack.setVisibility(View.VISIBLE);
            ivShare.setVisibility(View.VISIBLE);
            if (hasGoal) {
                pb_run_finish_details.setVisibility(View.VISIBLE);
                pb_run_finish_details.setProgress(progress);
            } else {
                pb_run_finish_details.setVisibility(View.INVISIBLE);
            }
        }
    }

    private void startLoc() {
        mBaiduMap = mapView.getMap();
        mBaiduMap.setMapStatus(MapStatusUpdateFactory.zoomTo(20));
        mBaiduMap.getUiSettings().setAllGesturesEnabled(false);
        mBaiduMap.setMyLocationEnabled(true);
        // 修改为自定义marker
        mCurrentMarker = BitmapDescriptorFactory
                .fromResource(R.mipmap.icon_geo);
        mBaiduMap.setMyLocationConfiguration(new MyLocationConfiguration(
                MyLocationConfiguration.LocationMode.NORMAL, true, mCurrentMarker,
                0xAAFFCC00, 0xAAFFCC00));
        locService = new LocationService(context);
        if (locService.registerListener(mListener)) {
            locService.start();
        }

    }

    @Override
    public void onBackPressed() {
        if (isFinished) {
            super.onBackPressed();
        } else {
            Toast.makeText(this, "请结束运动", Toast.LENGTH_SHORT).show();
        }
    }

    private void stopLoc() {
        if (locService != null && mListener != null) {
            locService.unregisterListener(mListener); //注销掉监听
            locService.stop(); //停止定位服务
        }
    }

    private MyLocationData locData;
    private boolean firstLoc = true;
    /*****
     *
     * 定位结果回调，重写onReceiveLocation方法，可以直接拷贝如下代码到自己工程中修改
     *
     */
    private BDLocationListener mListener = new BDLocationListener() {

        @Override
        public void onReceiveLocation(BDLocation location) {
            int locType = location.getLocType();
            Log.e("xwk", "locType = " + locType);
            if (locType != BDLocation.TypeServerError) {
                double curLat = location.getLatitude();
                double curLng = location.getLongitude();
                if (isLocationChange(curLat, curLng)) {
                    showMyLoc(location);
                    latUp = curLat;
                    lngUp = curLng;
                    if (runDistance > 0 && runDistance != 0) {
                        tvSpeed.setText(StepAlgorithm.getSpeedByDis(runDistance, recordingTime / 1000));
                        tvSpeed1.setText(StepAlgorithm.getSpeedByDis(runDistance, recordingTime / 1000));
                    }
                }
                if (firstLoc) {
                    chronometerRunTime.setFormat("%s");
//                    chronometerRunTime.start();
                    onRecordStart();
                    firstLoc = false;
                }
            } else {
                Toast.makeText(OutDoorRunActivity.this, "定位失败-", Toast.LENGTH_SHORT).show();
            }
        }

    };

    private void showMyLoc(BDLocation location) {
        //显示定位图标
        locData = new MyLocationData.Builder()
                .accuracy(location.getRadius())
                // 此处设置开发者获取到的方向信息，顺时针0-360
                .direction(location.getRadius()).latitude(location.getLatitude())
                .longitude(location.getLongitude()).build();
        mBaiduMap.setMyLocationData(locData);

        //设置地图中心为当前定位的位置
        LatLng curLatLng = new LatLng(location.getLatitude(),
                location.getLongitude());
        MapStatus.Builder builder = new MapStatus.Builder();
        builder.target(curLatLng).zoom(18.0f);
        mBaiduMap.animateMapStatus(MapStatusUpdateFactory.newMapStatus(builder.build()));

        latLngList.add(curLatLng);

        if (latUp != 0 && lngUp != 0) {
            LatLng upLatLng = new LatLng(latUp, lngUp);
            //计算p1、p2两点之间的直线距离，单位：米
            runDistance += DistanceUtil.getDistance(curLatLng, upLatLng);
            if (runDistance >= ((yValueInt + 1) * 100)) {
                lastDistance = runDistance - lastDistance;
                lastRecordTime = recordingTime - lastRecordTime;
                float num = (float) runDistance / 1000;
                DecimalFormat df = new DecimalFormat("0.0");
                xValues.add(df.format(num) + "");
                Float speed = StepAlgorithm.getSpeedTime(lastDistance, lastRecordTime / 1000);
//                String speedStr = StepAlgorithm.getSpeedByDis(last, lastRecordTime / 1000);
//                yValueStr.add(speedStr);
                yValue.add(new Entry(speed, yValueInt));
                yValueInt++;
            }
            String kcal = Integer.parseInt(StepAlgorithm.getKcal(Double.parseDouble(PrefName.getWeight(context)), runDistance)) / 1000 + "";
            if (goalValue != 0 && goalType == 0) {
                progress = (int) ((runDistance * 100) / (goalValue / 1000));
                pb_run_finish.setProgress(progress);
            } else if (goalValue != 0 && goalType == 2) {
                progress = (int) ((Double.parseDouble(kcal) * 100) / goalValue);
                pb_run_finish.setProgress(progress);
            }
            tvKm.setText(df.format(runDistance * 0.001) + "");
            tvKcal.setText(kcal);

            tvKm1.setText(df.format(runDistance * 0.001) + "");
            tvKcal1.setText(kcal);

            if (progress >= 100) {
                finishRunRide();
                new FinishDialog(context, new FinishDialog.ClickListener() {
                }, true).show();
            }
        }
    }

    private void drawLine() {
        if (latLngList.size() > 1) {
            List<Integer> colorValue = new ArrayList<>();
            int kindsColor = latLngList.size() / 4;//将所有的点分为4部分，为每部分点加相同的颜色
            for (int i = 0; i < kindsColor; i++) {
                colorValue.add(0xAAff5600);
            }
            for (int i = 0; i < kindsColor; i++) {
                colorValue.add(0xAAff8e11);
            }
            for (int i = 0; i < kindsColor; i++) {
                colorValue.add(0xAAffed0c);
            }
            for (int i = 0; i < kindsColor; i++) {
                colorValue.add(0xAA00ff35);
            }

            OverlayOptions ooPolyline1 = new PolylineOptions().width(10)
                    .color(0xAA00ff35).points(latLngList).colorsValues(colorValue);
            mColorfulPolyline = (Polyline) mBaiduMap.addOverlay(ooPolyline1);
        }
    }

    @Event(value = {R.id.iv_refresh, R.id.btn_finish, R.id.btn_go_on, R.id.ly_back, R.id.ly_share, R.id.ly_title, R.id.iv_music})
    private void getEvent(View view) {
        switch (view.getId()) {
            case R.id.iv_refresh:
                new FinishDialog(context, new FinishDialog.ClickListener() {
                }, true).show();
                break;
            case R.id.btn_finish:
                if (hasGoal) {
                    new FinishDialog(context, new FinishDialog.ClickListener() {
                    }, false).show();
                }
                finishRunRide();
                break;
            case R.id.btn_go_on:
//                chronometerRunTime.start();
                isStart = true;
                onRecordStart();
                locService.start();
                scroll(2 * width);
                break;
            case R.id.ly_back:
                finish();
                break;
            case R.id.ly_share:
                share();
                break;
            case R.id.ly_title:
                Bundle bundle = new Bundle();
                bundle.putStringArrayList("xValues", xValues);
                bundle.putParcelableArrayList("yValue", yValue);
                bundle.putLong("time", totalTime);
                bundle.putDouble("distance", runDistance);
                jumpBundleActvity(SpeedDetailsActivity.class, bundle);
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

    /**
     * 结束运动
     */
    private void finishRunRide() {
        mBaiduMap.getUiSettings().setAllGesturesEnabled(true);
        isFinished = true;
        if (hasGoal) {
            boolean hasFinished = false;
            switch (goalType) {
                case 0:
                    if (Integer.valueOf(tvKm.getText().toString()) >= goalValue) {
                        hasFinished = true;
                    }
                    break;
                case 1:
                    int min = (int) (getMin() * 10 / 6 * 0.1);
                    if (min >= goalValue) {
                        hasFinished = true;
                    }
                    break;
                case 2:
                    if (Integer.valueOf(tvKcal.getText().toString()) >= goalValue) {
                        hasFinished = true;
                    }
                    break;
                default:
                    hasFinished = false;
                    break;
            }
            new FinishDialog(context, new FinishDialog.ClickListener() {
            }, hasFinished).show();
        }
        tvTime1.setText(chronometerRunTime.getText().toString());
        stopLoc();
//                chronometerRunTime.stop();
        drawLine();
        setUI(false);
        if (runDistance != 0)
            uploadData();
        onRecordStop();
    }

    private void uploadData() {
        RequestParams params = new RequestParams(Url.UPLOAD_DATA_URL);
        Map<String, Object> map = new HashMap<>();
        map.put("kilometre", tvKm1.getText().toString());
        map.put("calorie", tvKcal1.getText().toString());
        map.put("second", recordingTime / 1000);
        if (hasGoal) {
            map.put("target", goalValue);
            map.put("progress", progress);
        } else
            map.put("target", "");
        switch (run_way) {
            case 2:
                map.put("type", SportReq.TYPE_RIDE);
                break;
            case 1:
                map.put("type", SportReq.TYPE_RUN);
                break;
        }
        map.put("pace", tvSpeed1.getText().toString());
        map.put("time", System.currentTimeMillis());
        map.put("token", PrefName.getToken(context));
        httpPostJSON("upload", params, map);
    }

    @Override
    public void success(String result, String type) {
        super.success(result, type);
        switch (type) {
            case "upload":
                try {
                    JSONObject jsonObject = new JSONObject(result);
                    id = jsonObject.getInt("id");
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                break;
        }
    }

    /**
     * 判断位置是否发生变化，如果没有变化则不更新地址信息，防止选择地址后，地址又变化的问题
     */
    private boolean isLocationChange(double lat1, double lng1) {
        return Math.abs(lat1 - latUp) > LOCATION_CHANGE_DISTINCTION_LAT
                || Math.abs(lng1 - lngUp) > LOCATION_CHANGE_DISTINCTION_LNG;
    }

    private void scroll(final int x) {
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                // 水平直接滚动800px，如果想效果更平滑可以使用smoothScrollTo(int x, int y)
                hor_scr.smoothScrollTo(x, 0);
            }
        }, 10);
    }

    View.OnTouchListener touchListener = new View.OnTouchListener() {
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
                        if ((endX - startX) > width / 2) {
                            //暂停
                            scroll(0);
                            isStart = false;
//                            chronometerRunTime.stop();
                            onRecordPause();
                            locService.stop();
                        } else {
                            //开始
                            scroll(2 * width);
                            Logger.d("开始");
                        }
                    } else {
                        if ((startX - endX) > startX / 2) {
                            //开始
                            scroll(2 * width);
//                            chronometerRunTime.start();
                            onRecordStart();
                            locService.start();
                            isStart = true;
                        } else {
                            //暂停
                            scroll(0);
                            Logger.d("暂停");
                        }
                    }
                    break;
            }
            return false;
        }
    };

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

    public int getMin() {
        int min = 0;
        if (chronometerRunTime != null) {
            String mText = chronometerRunTime.getText().toString();
            String[] s = mText.split(":");
            if (s.length == 3) {
                min = Integer.valueOf(s[0]) * 60 + Integer.valueOf(s[1]);
            } else if (s.length == 2) {
                min = Integer.valueOf(s[1]);
            }
        }
        return min;
    }

    /**
     * 进行分享
     */
    private void share() {
        final Bitmap bitmap = Utils.activityShot((Activity) context);
        final File file = FileUtil.saveBitmapFile(bitmap);
        new ShareToDialog(context, new ShareToDialog.ClickListener() {
            @Override
            public void shareToWeixin() {
                ThirdShare.shareToWx(bitmap, api);
            }

            @Override
            public void shareToCircle() {
                Bundle bundle = new Bundle();
                if (id != -1)
                    bundle.putInt("id", id);
                jumpBundleActvity(PublishActivity.class, bundle);
//                ThirdShare.shareToCircle(file.getAbsolutePath(), context);
            }

            @Override
            public void shareToWeibo() {
                ThirdShare.shareToWb(bitmap, wbShareHandler);
            }

            @Override
            public void shareToQQ() {
                ThirdShare.onClickShareQQ(file.getAbsolutePath(), context, mTencent);
            }
        }).show();
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
        totalTime = recordingTime;
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
