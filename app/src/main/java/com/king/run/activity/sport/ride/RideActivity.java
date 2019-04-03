package com.king.run.activity.sport.ride;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.view.MotionEventCompat;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.baidu.location.BDLocation;
import com.baidu.mapapi.map.MapView;
import com.king.run.R;
import com.king.run.baidumap.LocManage;
import com.king.run.baidumap.MapManage;
import com.king.run.base.BaseActivity;
import com.king.run.util.PrefName;
import com.king.run.util.PreferencesUtils;
import com.king.run.util.Utils;
import com.king.run.view.FinishDialog;
import com.king.run.view.ObservableScrollView;

import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.Event;
import org.xutils.view.annotation.ViewInject;

@ContentView(R.layout.activity_ride)
public class RideActivity extends BaseActivity {
    private LocManage mLocManage;
    private String locationAddr;
    @ViewInject(R.id.bmapView)
    private MapView mapView;
    private double latUp;
    private double lngUp;
    private MapManage mMapManage;
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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initTitleBar();
        initViews();
        initSign();
    }

    private void initViews() {
        width = Utils.getDisplayWidth(RideActivity.this);
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
        }, 10);
        hor_scr.setOnTouchListener(touchListener);
    }

    @Event(value = {R.id.iv_refresh, R.id.btn_finish})
    private void getEvent(View view) {
        switch (view.getId()) {
            case R.id.iv_refresh:
                getLocation();
                new FinishDialog(context, new FinishDialog.ClickListener() {
                }, true).show();
                break;
            case R.id.btn_finish:
                new FinishDialog(context, new FinishDialog.ClickListener() {
                }, true).show();
                break;
        }
    }

    private void initSign() {
        mMapManage = new MapManage(context);
        mLocManage = new LocManage(context);
        if (checkLocationPermission()) {
            initByGotLocationPermission();
        }
    }

    /**
     * 获取定位权限后的初始化
     */
    public void initByGotLocationPermission() {
        initLocation();
        getLocation();
    }

    /**
     * 查看定位信息
     */
    private void initLocation() {
        mMapManage.initMap((Activity) context, mapView);
        mMapManage.setMyLocationEnabled();
        //显示上次显示的位置
        try {
            double lastLat = Double.parseDouble(PreferencesUtils.getAppPref().getString(PrefName.PREF_LAST_LAT, "0"));
            double lastLng = Double.parseDouble(PreferencesUtils.getAppPref().getString(PrefName.PREF_LAST_LNG, "0"));
            mMapManage.initLocation(lastLat, lastLng);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void gotLocationPermissionResult(boolean isGrant) {
        super.gotLocationPermissionResult(isGrant);
        if (isGrant) {
            initByGotLocationPermission();
        } else {
            senToa(R.string.location_permission_denied);
        }
    }

    /**
     * 自动获取定位信息
     */
    private void getLocation() {
        //重新初始化最后一次的位置，防止通过isLocationChange检测认为地址没有变化
        latUp = 0.0d;
        lngUp = 0.0d;
        mLocManage.getLocation(gotLocCallBack);
    }

    /**
     * 获取百度经纬度回调
     */
    private LocManage.GotLocCallBackShort gotLocCallBack = new LocManage.GotLocCallBackShort() {

        @Override
        public void onGotLocShort(double lat, double lng, BDLocation location) {
            if (lat == 0.0d || lng == 0.0d) {
                return;
            } else {
//                mMapManage.showInfo((Activity) context, location.getLatitude(), location.getLongitude(), "");
                saveLastPosition(lat, lng);
                location.setLatitude(lat);
                location.setLongitude(lng);
                mMapManage.showMyLocationPoint(location);
                mMapManage.animToLocation(lat, lng);
                if (isLocationChange(lat, lng)) {
                    //此处返回的是百度坐标，用于上传到后台作为考勤数据
                    latUp = lat;
                    lngUp = lng;
                }
            }
        }

        @Override
        public void onGotLocShortFailed(String location) {
            //目前不会执行到这里
        }
    };

    /**
     * 保存最后一次的位置，下次打开时地图默认显示该位置
     *
     * @param lat
     * @param lng
     */
    private void saveLastPosition(double lat, double lng) {
        PreferencesUtils.putString(context, PrefName.PREF_LAST_LAT, lat + "");
        PreferencesUtils.putString(context, PrefName.PREF_LAST_LNG, lng + "");
    }

    /**
     * 判断位置是否发生变化，如果没有变化则不更新地址信息，防止选择地址后，地址又变化的问题
     *
     * @param lat1
     * @param lng1
     * @return
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
                        } else {
                            //开始
                            scroll(2 * width);
                        }
                    } else {
                        if ((startX - endX) > startX / 2) {
                            //开始
                            scroll(2 * width);
                            isStart = true;
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
}
