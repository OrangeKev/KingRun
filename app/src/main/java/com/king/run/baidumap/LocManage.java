package com.king.run.baidumap;

import android.annotation.SuppressLint;
import android.content.Context;
import android.util.Log;

import com.baidu.location.Address;
import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;

public class LocManage {
    public final static String LOG_TAG = "locationMap";

    public interface GotLocCallBackShort {
        void onGotLocShort(double lat, double lng, BDLocation location);

        void onGotLocShortFailed(String location);
    }

    private GotLocCallBackShort mGotLocCallBack;

    public LocationClient mLocationClient = null;
    public MyLocationListener myListener = new MyLocationListener();
//    private int getLocationCount = 0;

    public LocManage(Context context) {
        mLocationClient = new LocationClient(context); // 声明LocationClient类
        mLocationClient.registerLocationListener(myListener); // 注册监听函数
    }
    /**
     * @param gotLocCallBack
     */
    public void getLocation(GotLocCallBackShort gotLocCallBack) {
        mGotLocCallBack = gotLocCallBack;
        // 定位
        LocationClientOption option = new LocationClientOption();
        option.setOpenGps(true);
//		option.setLocationMode(LocationClientOption.LocationMode.Hight_Accuracy);
        option.setAddrType("all");// 返回的定位结果包含地址信息
        option.setCoorType("bd09ll");// 返回的定位结果是百度经纬度,默认值bd09ll,gcj02是火星坐标
        option.setScanSpan(30 * 1000);// 设置发起定位请求的间隔时间为5000ms
        option.setIsNeedAddress(true);
//        option.setPoiNumber(5); // 最多返回POI个数
//        option.setPoiDistance(1000); // poi查询距离
//        option.setPoiExtraInfo(true); // 是否需要POI的电话和地址等详细信息
        option.setProdName("wsoh");
        mLocationClient.setLocOption(option);
        mLocationClient.start();
        if (mLocationClient.isStarted()) {
//            getLocationCount = 0;
            mLocationClient.requestLocation();
            Log.i(LOG_TAG, "launchApp get location");
        } else {
            Log.i(LOG_TAG, "LocationClient not launchApp");
        }
    }

    public void stopLocation() {
        if (mLocationClient != null) {
            mLocationClient.stop();
        }
    }

    public void startLocation() {
        if (mLocationClient != null) {
            mLocationClient.start();
        }
    }

    public class MyLocationListener implements BDLocationListener {
        @SuppressLint("NewApi")
        @Override
        public void onReceiveLocation(BDLocation location) {
            Log.i(LOG_TAG, "receive location");
            if (location == null) {
                mGotLocCallBack.onGotLocShortFailed(null);
                return;
            }
            String addr = location.getAddrStr();
            Address address = location.getAddress();
            if (addr != null) {
                double lat = location.getLatitude();
                double lng = location.getLongitude();
                if (location.getLocType() == 65 || location.getLocType() == 61
                        || location.getLocType() == 161) {
                    Log.i(LOG_TAG, "address:" + location.getAddrStr());
                    if (mGotLocCallBack != null) {
                        mGotLocCallBack.onGotLocShort(lat, lng, location);
                    }
                }
//                mLocationClient.stop();
//                Log.i(LOG_TAG, "receive valide location: " + lat + " ... "
//                        + lng);
            } else {
//                getLocationCount++;
                mGotLocCallBack.onGotLocShortFailed(null);
            }
//            if (getLocationCount > 10) {
//            	if(mGotLocCallBack!=null){
//            		mGotLocCallBack.onGotLocShortFailed(null);
//            	}
//                mLocationClient.stop();
//                Log.i(LOG_TAG, "receive location 10 times, failed.");
//            }
        }

//        @Override
//        public void onConnectHotSpotMessage(String s, int i) {
//
//        }
    }
}
