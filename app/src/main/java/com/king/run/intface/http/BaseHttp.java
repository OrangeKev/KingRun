package com.king.run.intface.http;

import android.app.Activity;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.util.Log;

import com.alibaba.fastjson.JSON;
import com.king.run.util.CheckNetwork;

import org.json.JSONObject;
import org.xutils.common.Callback;
import org.xutils.http.RequestParams;
import org.xutils.x;

import java.lang.ref.WeakReference;
import java.util.Map;

/**
 * Created by Administrator on 2017/11/4.
 */

public class BaseHttp implements Callback.CommonCallback<String> {

    private ReqBack reqBack;
    protected static final int GET = 0;
    protected static final int POST = 1;

    protected void reqJson(Activity activity, int method, RequestParams params, Map map, ReqBack reqBack) {
        this.reqBack = reqBack;
        JSONObject json = new JSONObject(map);
        params.setAsJsonContent(true);
        params.setBodyContent(json.toString());
        if (method == POST) {
            post(activity, params, reqBack);
        } else {
            get(activity, params, reqBack);
        }
    }


    protected void get(Activity activity, RequestParams params, ReqBack reqBack) {
        this.reqBack = reqBack;
        if (CheckNetwork.isNetworkAvailable(activity)) {
            x.http().get(params, this);
        } else {
            reqBack.onError("网络异常");
        }
    }

    protected void post(Activity activity, RequestParams params, ReqBack reqBack) {
        this.reqBack = reqBack;
        if (CheckNetwork.isNetworkAvailable(activity)) {
            x.http().post(params, this);
        } else {
            reqBack.onError("网络异常");
        }
    }


    @Override
    public void onSuccess(String result) {
        Log.e("xwk", "result=" + result);
        Result baseResult = JSON.parseObject(result, Result.class);
        if (baseResult.getCode() == 0) {
            reqBack.onSuccess(baseResult.getData());
        } else {
            reqBack.onError(baseResult.getMessage());
        }
    }

    @Override
    public void onError(Throwable ex, boolean isOnCallback) {
        Log.e("xwk", "error=" + ex.getMessage());
        if (reqBack != null) {
            reqBack.onError(ex.getMessage());
        }
    }

    @Override
    public void onCancelled(CancelledException cex) {

    }

    @Override
    public void onFinished() {

    }


}
