package com.king.run.base;

import android.Manifest;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.NonNull;
import android.support.annotation.StringRes;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.text.Layout;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.ForegroundColorSpan;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.dd.plist.NSArray;
import com.dd.plist.NSDictionary;
import com.dd.plist.PropertyListParser;
import com.king.run.R;
import com.king.run.activity.music.model.MusicDetails;
import com.king.run.activity.music.model.MusicInfo;
import com.king.run.model.BaseResult;
import com.king.run.util.ActivityManager;
import com.king.run.util.CheckNetwork;
import com.king.run.util.MainHandlerConstant;
import com.king.run.util.OSUtil;
import com.king.run.util.PrefName;
import com.king.run.util.PreferencesUtils;
import com.king.run.util.QQConstants;
import com.king.run.util.StringUtil;
import com.king.run.util.Utils;
import com.king.run.util.WxConstants;
import com.king.run.util.XUtil;
import com.orhanobut.logger.Logger;
import com.sina.weibo.sdk.share.WbShareHandler;
import com.tencent.mm.opensdk.modelmsg.SendMessageToWX;
import com.tencent.mm.opensdk.openapi.IWXAPI;
import com.tencent.mm.opensdk.openapi.WXAPIFactory;
import com.tencent.tauth.Tencent;

import org.json.JSONObject;
import org.xutils.common.Callback;
import org.xutils.http.HttpMethod;
import org.xutils.http.RequestParams;
import org.xutils.x;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;


public class BaseActivity extends AppCompatActivity implements MainHandlerConstant {
    public Context context;
    private ProgressDialog progressDialog;
    public ImageView title_iv_back;
    public TextView title_tv_title, title_tv_right, title_tv_back;
    public RelativeLayout title_relate_layout, ly_iv_back, ly_iv_right;
    public ImageView title_iv_right;
    private String type;
    public static Activity topActivity;
    public boolean isToobarHide = true;
    protected Handler mainHandler;
    public Tencent mTencent;
    public IWXAPI api;
    public int mTargetScene = SendMessageToWX.Req.WXSceneSession;
    public WbShareHandler wbShareHandler;
    public List<MusicInfo> musicList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mainHandler = new Handler() {
            /*
             * @param msg
             */
            @Override
            public void handleMessage(Message msg) {
                super.handleMessage(msg);
                handle(msg);
            }

        };
        if (getSupportActionBar() != null){
            getSupportActionBar().hide();
        }
//        if (Utils.isPad(BaseActivity.this))
//            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
//        else
//            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
//        requestWindowFeature(Window.FEATURE_NO_TITLE);
        x.view().inject(this);
        ActivityManager.getInstance().pushOneActivity(this);
        context = this;
//        setStatusBarDarkMode(true);
//        initTitleBar();
        Log.e("width", Utils.getDisplayWidth((Activity) context) + "");
        Log.e("height", Utils.getDisplayHeight((Activity) context) + "");
        regToQQ();
        regToWx();
        regToWb();
    }

    private void regToWb() {
        wbShareHandler = new WbShareHandler(this);
        wbShareHandler.registerApp();
    }

    private void regToWx() {
        api = WXAPIFactory.createWXAPI(context, WxConstants.APP_ID, true);
        api.registerApp(WxConstants.APP_ID);
    }

    private void regToQQ() {
        mTencent = Tencent.createInstance(QQConstants.APP_ID, this.getApplicationContext());
    }

    @Override
    protected void onResume() {
        topActivity = this;
        super.onResume();
    }

    @Override
    protected void onDestroy() {
        ActivityManager.getInstance().popOneActivity(this);
        super.onDestroy();
    }

    protected void handle(Message msg) {
        int what = msg.what;
        switch (what) {
            case PRINT:
//                print(msg);
                break;
            case UI_CHANGE_INPUT_TEXT_SELECTION:
//                if (msg.arg1 <= mInput.getText().length()) {
//                    mInput.setSelection(0, msg.arg1);
//                }
                break;
            case UI_CHANGE_SYNTHES_TEXT_SELECTION:
//                SpannableString colorfulText = new SpannableString(mInput.getText().toString());
//                if (msg.arg1 <= colorfulText.toString().length()) {
//                    colorfulText.setSpan(new ForegroundColorSpan(Color.GRAY), 0, msg.arg1, Spannable
//                            .SPAN_EXCLUSIVE_EXCLUSIVE);
//                    mInput.setText(colorfulText);
//                }
                break;
            default:
                break;
        }
    }

    //    private void print(Message msg) {
//        String message = (String) msg.obj;
//        if (message != null) {
//            scrollLog(message);
//        }
//    }
//    private void scrollLog(String message) {
//        Spannable colorMessage = new SpannableString(message + "\n");
//        colorMessage.setSpan(new ForegroundColorSpan(0xff0000ff), 0, message.length(),
//                Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
//        mShowText.append(colorMessage);
//        Layout layout = mShowText.getLayout();
//        if (layout != null) {
//            int scrollAmount = layout.getLineTop(mShowText.getLineCount()) - mShowText.getHeight();
//            if (scrollAmount > 0) {
//                mShowText.scrollTo(0, scrollAmount + mShowText.getCompoundPaddingBottom());
//            } else {
//                mShowText.scrollTo(0, 0);
//            }
//        }
//    }
    public void initTitleBar() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);//透明状态栏
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                window.getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                        | View.SYSTEM_UI_FLAG_LAYOUT_STABLE | View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
            } else {
                window.getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                        | View.SYSTEM_UI_FLAG_LAYOUT_STABLE);
            }
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS
                    | WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);
            window.setStatusBarColor(Color.TRANSPARENT);
        } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
//            Window window = getWindow();
//            window.addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        }
    }

    /**
     * 透明导航栏和状态栏
     */
    protected void transNavigationBarStatusBar() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = getWindow();
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS | WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);
            window.getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                    | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                    | View.SYSTEM_UI_FLAG_LAYOUT_STABLE);
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setNavigationBarColor(Color.TRANSPARENT);
            window.setStatusBarColor(Color.TRANSPARENT);
        } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);

        }
    }

    public void init() {
        if (isToobarHide)
            initTitleBar();
        DisplayMetrics dm = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(dm);
        int screenWidthDip = dm.widthPixels; // 屏幕宽
        int screenHeightDip = dm.heightPixels; // 屏幕宽
        PreferencesUtils.putInt(BaseActivity.this, PrefName.SCREEN_HEIGHT, screenHeightDip);
        PreferencesUtils.putInt(BaseActivity.this, PrefName.SCREEN_WIDTH, screenWidthDip);
        title_iv_back = (ImageView) findViewById(R.id.title_iv_back);
        title_relate_layout = (RelativeLayout) findViewById(R.id.title_layout);
        title_tv_title = (TextView) findViewById(R.id.title_title);
        title_iv_right = (ImageView) findViewById(R.id.title_right);
        title_tv_right = (TextView) findViewById(R.id.title_tv_right);
        title_tv_back = (TextView) findViewById(R.id.title_tv_back);
        ly_iv_back = (RelativeLayout) findViewById(R.id.ly_iv_back);
        ly_iv_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setFinish();
            }
        });
        title_tv_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setFinish();
            }
        });
        ly_iv_right = (RelativeLayout) findViewById(R.id.ly_iv_right);
    }

    /**
     * 针对miui android m以上系统设置浅色状态栏
     *
     * @param darkmode
     */
    public void setStatusBarDarkMode(boolean darkmode) {
        if (OSUtil.isMIUI() && Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            Class<? extends Window> clazz = getWindow().getClass();
            try {
                int darkModeFlag = 0;
                Class<?> layoutParams = Class.forName("android.view.MiuiWindowManager$LayoutParams");
                Field field = layoutParams.getField("EXTRA_FLAG_STATUS_BAR_DARK_MODE");
                darkModeFlag = field.getInt(layoutParams);
                Method extraFlagField = clazz.getMethod("setExtraFlags", int.class, int.class);
                extraFlagField.invoke(getWindow(), darkmode ? darkModeFlag : 0, darkModeFlag);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public void setFinish() {
        finish();
    }

    public void httpGet(RequestParams params, final String type) {
        params.addHeader("token", PrefName.getToken(context));
        Logger.d(params);
        this.type = type;
        if (CheckNetwork.isNetworkAvailable(BaseActivity.this)) {
            showDia("正在加载中...");
            x.http().get(params, callback);
        } else {
            senToa("网络异常");
        }
    }

    public void httpGetWx(RequestParams params, String type) {
        if (CheckNetwork.isNetworkAvailable((Activity) context)) {
            showDia("正在加载中...");
            x.http().get(params, callback);
            Logger.d("url=" + params);
        } else {
            senToa("网络异常");
        }
    }

    public void httpGetJSON(final String type, RequestParams params, Map map) {
        params.addHeader("token", PrefName.getToken(context));
        this.type = type;
        JSONObject json = new JSONObject(map);
        params.setAsJsonContent(true);
        params.setBodyContent(json.toString());
        httpGet(params, type);
    }

    public void httpPostJSON(final String type, RequestParams params, Map map) {
        params.addHeader("token", PrefName.getToken(context));
        JSONObject json = new JSONObject(map);
        params.setAsJsonContent(true);
        params.setBodyContent(json.toString());
        http(params, type);
    }

    public void httpPutJSON(final String type, RequestParams params, Map map) {
        params.addHeader("token", PrefName.getToken(context));
        this.type = type;
        if (CheckNetwork.isNetworkAvailable(BaseActivity.this)) {
            showDia("正在加载中...");
            JSONObject json = new JSONObject(map);
            params.setAsJsonContent(true);
            params.setBodyContent(json.toString());
            x.http().request(HttpMethod.PUT, params, callback);
        } else {
            senToa("网络异常");
        }
    }

    public void httpPut(final String type, RequestParams params) {
        params.addHeader("token", PrefName.getToken(context));
        this.type = type;
        if (CheckNetwork.isNetworkAvailable(BaseActivity.this)) {
            showDia("正在加载中...");
            x.http().request(HttpMethod.PUT, params, callback);
        } else {
            senToa("网络异常");
        }
    }

    public void httpDelete(final String type, RequestParams params) {
        params.addHeader("token", PrefName.getToken(context));
        this.type = type;
        if (CheckNetwork.isNetworkAvailable(BaseActivity.this)) {
            showDia("正在加载中...");
            x.http().request(HttpMethod.DELETE, params, callback);
        } else {
            senToa("网络异常");
        }
    }

    public void httpPost(final String type, RequestParams params) {
        params.addHeader("token", PrefName.getToken(context));
        http(params, type);
    }

    private void http(RequestParams params, final String type) {
        this.type = type;
        if (CheckNetwork.isNetworkAvailable(BaseActivity.this)) {
            showDia("正在加载中...");
            x.http().post(params, callback);
        } else {
            senToa("网络异常");
        }
    }


    public void httpGetNoDia(RequestParams params, final String type) {
        params.addHeader("token", PrefName.getToken(context));
        Logger.d(params);
        this.type = type;
        if (CheckNetwork.isNetworkAvailable(BaseActivity.this)) {
//            showDia("正在加载中...");
            x.http().get(params, callback);
        } else {
            senToa("网络异常");
        }
    }

    public void httpGetJSONNoDia(final String type, RequestParams params, Map map) {
        params.addHeader("token", PrefName.getToken(context));
        this.type = type;
        JSONObject json = new JSONObject(map);
        params.setAsJsonContent(true);
        params.setBodyContent(json.toString());
        httpGetNoDia(params, type);
    }

    public void httpPostJSONNoDia(final String type, RequestParams params, Map map) {
        params.addHeader("token", PrefName.getToken(context));
        JSONObject json = new JSONObject(map);
        params.setAsJsonContent(true);
        params.setBodyContent(json.toString());
        httpNoDia(params, type);
    }

    public void httpPutJSONNoDia(final String type, RequestParams params, Map map) {
        params.addHeader("token", PrefName.getToken(context));
        this.type = type;
        if (CheckNetwork.isNetworkAvailable(BaseActivity.this)) {
//            showDia("正在加载中...");
            JSONObject json = new JSONObject(map);
            params.setAsJsonContent(true);
            params.setBodyContent(json.toString());
            x.http().request(HttpMethod.PUT, params, callback);
        } else {
            senToa("网络异常");
        }
    }

    public void httpPutNoDia(final String type, RequestParams params) {
        params.addHeader("token", PrefName.getToken(context));
        this.type = type;
        if (CheckNetwork.isNetworkAvailable(BaseActivity.this)) {
//            showDia("正在加载中...");
            x.http().request(HttpMethod.PUT, params, callback);
        } else {
            senToa("网络异常");
        }
    }

    public void httpDeleteNoDia(final String type, RequestParams params) {
        params.addHeader("token", PrefName.getToken(context));
        this.type = type;
        if (CheckNetwork.isNetworkAvailable(BaseActivity.this)) {
//            showDia("正在加载中...");
            x.http().request(HttpMethod.DELETE, params, callback);
        } else {
            senToa("网络异常");
        }
    }

    public void httpPostNoDia(final String type, RequestParams params) {
        params.addHeader("token", PrefName.getToken(context));
        http(params, type);
    }

    private void httpNoDia(RequestParams params, final String type) {
        this.type = type;
        if (CheckNetwork.isNetworkAvailable(BaseActivity.this)) {
//            showDia("正在加载中...");
            x.http().post(params, callback);
        } else {
            senToa("网络异常");
        }
        XUtil.DownLoadFile("", "", new Callback.ProgressCallback<Object>() {
            @Override
            public void onSuccess(Object result) {

            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {

            }

            @Override
            public void onCancelled(CancelledException cex) {

            }

            @Override
            public void onFinished() {

            }

            @Override
            public void onWaiting() {

            }

            @Override
            public void onStarted() {

            }

            @Override
            public void onLoading(long total, long current, boolean isDownloading) {

            }
        });
    }

    Callback.CommonCallback<String>
            callback = new Callback.CacheCallback<String>() {
        @Override
        public boolean onCache(String result) {
            return false;
        }

        @Override
        public void onSuccess(String result) {
            Logger.d("result" + result);
            BaseResult baseResult = JSON.parseObject(result, BaseResult.class);
            if (baseResult.getCode() != 0) {
                senToa(baseResult.getMsg());
            }
            success(result, type);
            hideDia();
        }

        @Override
        public void onError(Throwable ex, boolean isOnCallback) {
            error(ex.getMessage(), type);
            Logger.d("error", ex.getMessage());
            if (ex.getMessage().contains("timeout")) {
                senToa("连接超时");
            } else
                senToa("error" + ex.getMessage());
            hideDia();
        }

        @Override
        public void onCancelled(CancelledException cex) {

        }

        @Override
        public void onFinished() {

        }
    };

    public void success(String result, String type) {

    }

    public void error(String result, String type) {

    }

    public void senToa(String text) {
        Toast mToast = null;
        if (mToast == null) {
            mToast.makeText(context, text, Toast.LENGTH_LONG).show();
        }
    }

    public void senToa(@StringRes int id) {
        Toast mToast = null;
        if (mToast == null) {
            mToast.makeText(context, id, Toast.LENGTH_LONG).show();
        }
    }

    public void showDia(String str) {
        if (progressDialog == null) {
            progressDialog = new ProgressDialog(this);
            progressDialog.setProgressStyle(R.style.CustomProgressDialog);
        }
        progressDialog.setMessage(str);
        progressDialog.show();
    }

    public void hideDia() {
        try {
            if (progressDialog != null && progressDialog.isShowing()) {
                progressDialog.dismiss();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void jumpBundleActvity(Class cls, Bundle bundle) {
        Intent intent = new Intent();
        intent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);// 设置跳转标志为如此Activity存在则把其从任务堆栈中取出放到最上方
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);// 在跳转之前情况当前任务堆栈中此Activity的顶部任务
        if (null != bundle)
            intent.putExtras(bundle);
        intent.setClass(context, cls);
        startActivity(intent);
    }

    public void jumpBundleActvity(Class cls, Bundle bundle, String action) {
        Intent intent = new Intent();
        intent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);// 设置跳转标志为如此Activity存在则把其从任务堆栈中取出放到最上方
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);// 在跳转之前情况当前任务堆栈中此Activity的顶部任务
        if (null != bundle)
            intent.putExtras(bundle);
        if (StringUtil.isNotBlank(action))
            intent.setAction(action);
        intent.setClass(context, cls);
        startActivity(intent);
    }

    public void jumpActvity(Class cls) {
        Intent intent = new Intent();
        intent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);// 设置跳转标志为如此Activity存在则把其从任务堆栈中取出放到最上方
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);// 在跳转之前情况当前任务堆栈中此Activity的顶部任务
        intent.setClass(context, cls);
        startActivity(intent);
    }

    public void jumpActvity(Class cls, String action) {
        Intent intent = new Intent();
        intent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);// 设置跳转标志为如此Activity存在则把其从任务堆栈中取出放到最上方
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);// 在跳转之前情况当前任务堆栈中此Activity的顶部任务
        if (StringUtil.isNotBlank(action))
            intent.setAction(action);
        intent.setClass(context, cls);
        startActivity(intent);
    }

    public void jumpBundleActvityforResult(Class cls, Bundle bundle, int code) {
        Intent intent = new Intent();
        intent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);// 设置跳转标志为如此Activity存在则把其从任务堆栈中取出放到最上方
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);// 在跳转之前情况当前任务堆栈中此Activity的顶部任务
        if (null != bundle)
            intent.putExtras(bundle);
        intent.setClass(context, cls);
        startActivityForResult(intent, code);
    }

    public void jumpBundleActvityforResult(Class cls, Bundle bundle, int code, String action) {
        Intent intent = new Intent();
        intent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);// 设置跳转标志为如此Activity存在则把其从任务堆栈中取出放到最上方
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);// 在跳转之前情况当前任务堆栈中此Activity的顶部任务
        if (StringUtil.isNotBlank(action))
            intent.setAction(action);
        if (null != bundle)
            intent.putExtras(bundle);
        intent.setClass(context, cls);
        startActivityForResult(intent, code);
    }

    public void setResultAct(Bundle bundle) {
        Intent intent = new Intent();
        if (null != bundle)
            intent.putExtras(bundle);
        setResult(RESULT_OK, intent);
    }

    protected boolean checkPhonePermission() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_PHONE_STATE)
                != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.READ_PHONE_STATE},
                    PermissionRequestCode.PHONE);
            return false;
        } else {
            return true;
        }
    }

    protected boolean checkCameraPermission() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA)
                != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.CAMERA},
                    PermissionRequestCode.CAMERA);
            return false;
        } else {
            return true;
        }
    }

    protected boolean checkCameraStoragePermission() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED
                || ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED
                || ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA)
                != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE,
                            Manifest.permission.READ_EXTERNAL_STORAGE,
                            Manifest.permission.CAMERA},
                    PermissionRequestCode.CAMERASTRAGE);
            return false;
        } else {
            return true;
        }
    }

    protected boolean checkStoragePhonePermission() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED
                || ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED
                || ContextCompat.checkSelfPermission(this, Manifest.permission.READ_PHONE_STATE)
                != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE,
                            Manifest.permission.READ_EXTERNAL_STORAGE,
                            Manifest.permission.READ_PHONE_STATE},
                    PermissionRequestCode.CAMERASTRAGE);
            return false;
        } else {
            return true;
        }
    }

    protected boolean checkRecordAudioPermission() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.RECORD_AUDIO) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.RECORD_AUDIO},
                    PermissionRequestCode.CAMERASTRAGE);
            return false;
        } else {
            return true;
        }
    }

    protected boolean checkStoragePermission() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED
                || ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE,
                            Manifest.permission.READ_EXTERNAL_STORAGE},
                    PermissionRequestCode.STORAGE);
            return false;
        } else {
            return true;
        }
    }

    /**
     * 检查是否有定位权限，如果没有 则获取
     *
     * @return
     */
    public boolean checkLocationPermission() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION)
                != PackageManager.PERMISSION_GRANTED
                || ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION,
                            Manifest.permission.ACCESS_COARSE_LOCATION},
                    PermissionRequestCode.LOCATION);
            return false;
        } else {
            return true;
        }
    }

    protected boolean checkCameraStorageAudioPermission() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED
                || ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED
                || ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA)
                != PackageManager.PERMISSION_GRANTED
                || ContextCompat.checkSelfPermission(this, Manifest.permission.RECORD_AUDIO)
                != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE,
                            Manifest.permission.READ_EXTERNAL_STORAGE,
                            Manifest.permission.CAMERA,
                            Manifest.permission.RECORD_AUDIO},
                    PermissionRequestCode.CAMERASTRAGEAUDIO);
            return false;
        } else {
            return true;
        }
    }

    protected class PermissionRequestCode {
        public final static int STORAGE = 20;
        public final static int PHONE = 30;
        public final static int CAMERA = 40;
        public final static int CAMERASTRAGE = 50;
        public final static int LOCATION = 60;
        public final static int RECORD_AUDIO = 70;
        public final static int PHONE_STORAGE = 80;
        public final static int CAMERASTRAGEAUDIO = 90;
    }

    /**
     * @param isGrant true 已授权，flase 已拒绝
     */
    protected void gotLocationPermissionResult(boolean isGrant) {
    }

    /**
     * @param isGrant true 已授权，flase 已拒绝
     */
    protected void gotStoragePermissionResult(boolean isGrant) {
    }

    /**
     * @param isGrant true 已授权，flase 已拒绝
     */
    protected void gotCameraPermissionResult(boolean isGrant) {
    }

    /**
     * @param isGrant true 已授权，flase 已拒绝
     */
    protected void gotMicroPhonePermissionResult(boolean isGrant) {
    }

    /**
     * @param isGrant true 已授权，flase 已拒绝
     */
    protected void gotCameraStoragePermissionResult(boolean isGrant) {
    }

    /**
     * @param isGrant true 已授权，flase 已拒绝
     */
    protected void gotRecordAudioPermissionResult(boolean isGrant) {
    }

    /**
     * @param isGrant true 已授权，flase 已拒绝
     */
    protected void gotPhoneStoragePermissionResult(boolean isGrant) {
    }

    /**
     * @param isGrant true 已授权，flase 已拒绝
     */
    protected void gotCameraStorageAudioPermissionResult(boolean isGrant) {
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        boolean grantResult = false;
        if (grantResults.length > 0) {
            //只要有一项通过，则认为已授权
            for (int i = 0; i < grantResults.length; i++) {
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED)
                    grantResult = true;
            }
        }
        switch (requestCode) {
            case PermissionRequestCode.STORAGE:
                gotStoragePermissionResult(grantResult);
                break;
            case PermissionRequestCode.CAMERA:
                gotCameraPermissionResult(grantResult);
                break;
            case PermissionRequestCode.PHONE:
                gotMicroPhonePermissionResult(grantResult);
                break;
            case PermissionRequestCode.CAMERASTRAGE:
                gotCameraStoragePermissionResult(grantResult);
                break;
            case PermissionRequestCode.LOCATION:
                gotLocationPermissionResult(grantResult);
                break;
            case PermissionRequestCode.RECORD_AUDIO:
                gotRecordAudioPermissionResult(grantResult);
                break;
            case PermissionRequestCode.PHONE_STORAGE:
                gotPhoneStoragePermissionResult(grantResult);
                break;
            case PermissionRequestCode.CAMERASTRAGEAUDIO:
                gotCameraStorageAudioPermissionResult(grantResult);
                break;
        }
    }

    public void getMusicInfo() {
        musicList = new ArrayList<>();
        try {
            NSArray ary = (NSArray) PropertyListParser.parse(getAssets().open("music.plist"));
            for (int i = 0; i < ary.count(); i++) {//遍历当前NSArray
                //将当前遍历的结果也是字典。将其存放入dic（字典）中保存
                NSDictionary dic = (NSDictionary) ary.objectAtIndex(i);
                MusicInfo musicInfo = new MusicInfo();
                //获取key：project的value值。
                String image = (dic.objectForKey("image")).toJavaObject().toString();
                String type = (dic.objectForKey("type")).toJavaObject().toString();
                String title = (dic.objectForKey("title")).toJavaObject().toString();
                String step = (dic.objectForKey("step")).toJavaObject().toString();
                String isuse = (dic.objectForKey("isuse")).toJavaObject().toString();
                String isdownload = (dic.objectForKey("isdownload")).toJavaObject().toString();
                String desc = (dic.objectForKey("desc")).toJavaObject().toString();
                musicInfo.setImage(image);
                musicInfo.setType(type);
                musicInfo.setTitle(title);
                musicInfo.setStep(step);
                musicInfo.setIsuse(isuse);
                musicInfo.setIsdownload(isdownload);
                musicInfo.setDesc(desc);
                //获取key：dataSource的value。
                // 分析plist文件得到value为array类型（每一个数据是NSDictionary类型）
                List<MusicDetails> lists = new ArrayList<>();
                NSArray tmpAry = (NSArray) dic.objectForKey("musics");
                for (int j = 0; j < tmpAry.count(); j++) {
                    NSDictionary secDic = (NSDictionary) tmpAry.objectAtIndex(j);
                    MusicDetails musicDetails = new MusicDetails();
                    String musicName = (secDic.objectForKey("musicName")).toJavaObject().toString();
                    String musicSpecial = (secDic.objectForKey("musicSpecial")).toJavaObject().toString();
                    String musicUrl = (secDic.objectForKey("musicUrl")).toJavaObject().toString();
                    musicDetails.setMusicName(musicName);
                    musicDetails.setMusicSpecial(musicSpecial);
                    musicDetails.setMusicUrl(musicUrl);
                    lists.add(musicDetails);
                }
                musicInfo.setMusics(lists);
                musicList.add(musicInfo);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
