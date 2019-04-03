package com.king.run.base;

import android.content.Context;
import android.os.Handler;
import android.os.Looper;
import android.support.multidex.MultiDexApplication;

import com.baidu.mapapi.SDKInitializer;
import com.king.run.baidumap.LocManage;
import com.king.run.baidumap.MapManage;
import com.sina.weibo.sdk.WbSdk;
import com.sina.weibo.sdk.auth.AuthInfo;
import com.sina.weibo.sdk.constant.WBConstants;
import com.tencent.bugly.crashreport.CrashReport;

import org.xutils.x;

/**
 * Email 674618016@qq.com
 * Created by shuiz_wade on 2017/1/11.
 */

public class MyApplication extends MultiDexApplication {
    private static MyApplication application;
    private Handler wxHandler;
    private static Context context;
    private static Handler mHandler;
    private static MapManage mapManage;
    private static LocManage locManage;

    public static MapManage getMapManage() {
        return mapManage;
    }

    public static LocManage getLocManage() {
        return locManage;
    }

    public static MyApplication getInstance() {
        if (application == null) {
            application = new MyApplication();
        }
        return application;
    }

    public static void doInMain(Runnable runnable) {
        mHandler.post(runnable);
    }

    public static void doInMain(Runnable runnable, long delay) {
        mHandler.postDelayed(runnable, delay);
    }


    @Override
    public void onCreate() {
        super.onCreate();
        context = this.getApplicationContext();
        initBaiduMap();
        CrashReport.initCrashReport(getApplicationContext(), "676e03396a", true);
        x.Ext.init(this);
        WbSdk.install(getApplicationContext(), new AuthInfo(getApplicationContext(), WBConstants.SSO_APP_KEY, WBConstants.SSO_REDIRECT_URL, WBConstants.SSO_USER_SCOPE));
        mHandler = new Handler(Looper.getMainLooper());
    }

    private void initBaiduMap() {
        try {
            SDKInitializer.initialize(this);
            mapManage = new MapManage(getApplicationContext());
            locManage = new LocManage(getApplicationContext());
        } catch (Exception e) {
        }
    }

    public Handler getWxHandler() {
        return wxHandler;
    }

    public void setWxHandler(Handler wxHandler) {
        this.wxHandler = wxHandler;
    }

    public static Context getContext() {
        return context;
    }

}
