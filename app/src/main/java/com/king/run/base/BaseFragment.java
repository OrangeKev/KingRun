package com.king.run.base;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.StringRes;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.king.run.R;
import com.king.run.model.BaseResult;
import com.king.run.util.CheckNetwork;
import com.king.run.util.PrefName;
import com.king.run.util.StringUtil;
import com.orhanobut.logger.Logger;

import org.json.JSONObject;
import org.xutils.common.Callback;
import org.xutils.http.HttpMethod;
import org.xutils.http.RequestParams;
import org.xutils.x;

import java.util.Map;

/**
 * Email 674618016@qq.com
 * Created by shuiz_wade on 2017/8/16.
 */

public class BaseFragment extends Fragment {
    private ProgressDialog progressDialog;
    private String type;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return x.view().inject(this, inflater, container);
    }

    public void httpGet(RequestParams params, final String type) {
        params.addHeader("token", PrefName.getToken(getActivity()));
        Logger.d(params);
        this.type = type;
        if (CheckNetwork.isNetworkAvailable(getActivity())) {
            showDia("正在加载中...");
            x.http().get(params, callback);
        } else {
            senToa("网络异常");
        }
    }
    public void httpGetNoDia(RequestParams params, final String type) {
        params.addHeader("token", PrefName.getToken(getActivity()));
        Logger.d(params);
        this.type = type;
        if (CheckNetwork.isNetworkAvailable(getActivity())) {
//            showDia("正在加载中...");
            x.http().get(params, callback);
        } else {
            senToa("网络异常");
        }
    }

    public void httpGetJSON(final String type, RequestParams params, Map map) {
        params.addHeader("token", PrefName.getToken(getActivity()));
        this.type = type;
        JSONObject json = new JSONObject(map);
        params.setAsJsonContent(true);
        params.setBodyContent(json.toString());
        httpGet(params, type);
    }

    public void httpPostJSON(final String type, RequestParams params, Map map) {
        params.addHeader("token", PrefName.getToken(getActivity()));
        JSONObject json = new JSONObject(map);
        params.setAsJsonContent(true);
        params.setBodyContent(json.toString());
        http(params, type);
    }

    public void httpPutJSON(final String type, RequestParams params, Map map) {
        params.addHeader("token", PrefName.getToken(getActivity()));
        this.type = type;
        if (CheckNetwork.isNetworkAvailable(getActivity())) {
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
        params.addHeader("token", PrefName.getToken(getActivity()));
        this.type = type;
        if (CheckNetwork.isNetworkAvailable(getActivity())) {
            showDia("正在加载中...");
            x.http().request(HttpMethod.PUT, params, callback);
        } else {
            senToa("网络异常");
        }
    }

    public void httpDelete(final String type, RequestParams params) {
        params.addHeader("token", PrefName.getToken(getActivity()));
        this.type = type;
        if (CheckNetwork.isNetworkAvailable(getActivity())) {
            showDia("正在加载中...");
            x.http().request(HttpMethod.DELETE, params, callback);
        } else {
            senToa("网络异常");
        }
    }

    public void httpPost(final String type, RequestParams params) {
        params.addHeader("token", PrefName.getToken(getActivity()));
        http(params, type);
    }

    private void http(RequestParams params, final String type) {
        this.type = type;
        if (CheckNetwork.isNetworkAvailable(getActivity())) {
            showDia("正在加载中...");
            x.http().post(params, callback);
        } else {
            senToa("网络异常");
        }
    }

    public void httpGetJSONNoDia(final String type, RequestParams params, Map map) {
        params.addHeader("token", PrefName.getToken(getActivity()));
        this.type = type;
        JSONObject json = new JSONObject(map);
        params.setAsJsonContent(true);
        params.setBodyContent(json.toString());
        httpGetNoDia(params, type);
    }

    public void httpPostJSONNoDia(final String type, RequestParams params, Map map) {
        params.addHeader("token", PrefName.getToken(getActivity()));
        JSONObject json = new JSONObject(map);
        params.setAsJsonContent(true);
        params.setBodyContent(json.toString());
        httpNoDia(params, type);
    }

    public void httpPutJSONNoDia(final String type, RequestParams params, Map map) {
        params.addHeader("token", PrefName.getToken(getActivity()));
        this.type = type;
        if (CheckNetwork.isNetworkAvailable(getActivity())) {
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
        params.addHeader("token", PrefName.getToken(getActivity()));
        this.type = type;
        if (CheckNetwork.isNetworkAvailable(getActivity())) {
//            showDia("正在加载中...");
            x.http().request(HttpMethod.PUT, params, callback);
        } else {
            senToa("网络异常");
        }
    }

    public void httpDeleteNoDia(final String type, RequestParams params) {
        params.addHeader("token", PrefName.getToken(getActivity()));
        this.type = type;
        if (CheckNetwork.isNetworkAvailable(getActivity())) {
//            showDia("正在加载中...");
            x.http().request(HttpMethod.DELETE, params, callback);
        } else {
            senToa("网络异常");
        }
    }

    public void httpPostNoDia(final String type, RequestParams params) {
        params.addHeader("token", PrefName.getToken(getActivity()));
        httpNoDia(params, type);
    }

    private void httpNoDia(RequestParams params, final String type) {
        this.type = type;
        if (CheckNetwork.isNetworkAvailable(getActivity())) {
//            showDia("正在加载中...");
            x.http().post(params, callback);
        } else {
            senToa("网络异常");
        }
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
            if (baseResult.getCode() == 0)
                success(result, type);
            else
                senToa(baseResult.getMsg());
            hideDia();
        }

        @Override
        public void onError(Throwable ex, boolean isOnCallback) {
            error(ex.getMessage(), type);
            Logger.d("", ex);
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
            mToast = new Toast(getActivity());
        }
        mToast.makeText(getActivity(), text, Toast.LENGTH_LONG).show();
    }

    public void senToa(@StringRes int id) {
        Toast mToast = null;
        if (mToast == null) {
            mToast = new Toast(getActivity());
        }
        mToast.makeText(getActivity(), id, Toast.LENGTH_LONG).show();
    }

    public void showDia(String str) {
        if (progressDialog == null) {
            progressDialog = new ProgressDialog(getActivity());
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
        if (bundle != null)
            intent.putExtras(bundle);
        intent.setClass(getActivity(), cls);
        startActivity(intent);
    }

    public void jumpBundleActvity(Class cls, Bundle bundle, String action) {
        Intent intent = new Intent();
        intent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);// 设置跳转标志为如此Activity存在则把其从任务堆栈中取出放到最上方
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);// 在跳转之前情况当前任务堆栈中此Activity的顶部任务
        if (bundle != null)
            intent.putExtras(bundle);
        if (StringUtil.isNotBlank(action))
            intent.setAction(action);
        intent.setClass(getActivity(), cls);
        startActivity(intent);
    }

    public void jumpActvity(Class cls) {
        Intent intent = new Intent();
        intent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);// 设置跳转标志为如此Activity存在则把其从任务堆栈中取出放到最上方
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);// 在跳转之前情况当前任务堆栈中此Activity的顶部任务
        intent.setClass(getActivity(), cls);
        startActivity(intent);
    }

    public void jumpActvity(Class cls, String action) {
        Intent intent = new Intent();
        intent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);// 设置跳转标志为如此Activity存在则把其从任务堆栈中取出放到最上方
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);// 在跳转之前情况当前任务堆栈中此Activity的顶部任务
        if (StringUtil.isNotBlank(action))
            intent.setAction(action);
        intent.setClass(getActivity(), cls);
        startActivity(intent);
    }

    public void jumpBundleActvityforResult(Class cls, Bundle bundle, int code) {
        Intent intent = new Intent();
        intent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);// 设置跳转标志为如此Activity存在则把其从任务堆栈中取出放到最上方
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);// 在跳转之前情况当前任务堆栈中此Activity的顶部任务
        if (null != bundle)
            intent.putExtras(bundle);
        intent.setClass(getActivity(), cls);
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
        intent.setClass(getActivity(), cls);
        startActivityForResult(intent, code);
    }

    public void setResultAct(Bundle bundle) {
        Intent intent = new Intent();
        if (bundle != null)
            intent.putExtras(bundle);
        getActivity().setResult(getActivity().RESULT_OK, intent);
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
            case PermissionRequestCode.CAMERASTRAGEAUDIO:
                gotCameraStorageAudioPermissionResult(grantResult);
                break;
        }
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
    protected void gotCameraStorageAudioPermissionResult(boolean isGrant) {
    }

    protected boolean checkPhonePermission() {
        if (ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.READ_PHONE_STATE)
                != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(getActivity(),
                    new String[]{Manifest.permission.READ_PHONE_STATE},
                    BaseActivity.PermissionRequestCode.PHONE);
            return false;
        } else {
            return true;
        }
    }

    protected boolean checkCameraPermission() {
        if (ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.CAMERA)
                != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(getActivity(),
                    new String[]{Manifest.permission.CAMERA},
                    BaseActivity.PermissionRequestCode.CAMERA);
            return false;
        } else {
            return true;
        }
    }

    protected boolean checkCameraStoragePermission() {
        if (ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.WRITE_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED
                || ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.READ_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED
                || ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.CAMERA)
                != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(getActivity(),
                    new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE,
                            Manifest.permission.READ_EXTERNAL_STORAGE,
                            Manifest.permission.CAMERA},
                    BaseActivity.PermissionRequestCode.CAMERASTRAGE);
            return false;
        } else {
            return true;
        }
    }

    protected boolean checkCameraStorageAudioPermission() {
        if (ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.WRITE_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED
                || ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.READ_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED
                || ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.CAMERA)
                != PackageManager.PERMISSION_GRANTED
                || ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.RECORD_AUDIO)
                != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(getActivity(),
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

    protected boolean checkStoragePermission() {
        if (ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.WRITE_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED
                || ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.READ_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(getActivity(),
                    new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE,
                            Manifest.permission.READ_EXTERNAL_STORAGE},
                    BaseActivity.PermissionRequestCode.STORAGE);
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
        if (ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_COARSE_LOCATION)
                != PackageManager.PERMISSION_GRANTED
                || ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(getActivity(),
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION,
                            Manifest.permission.ACCESS_COARSE_LOCATION},
                    BaseActivity.PermissionRequestCode.LOCATION);
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
        public final static int CAMERASTRAGEAUDIO = 70;
    }
}
