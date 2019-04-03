package com.king.run.intface.http.iml;

import android.app.Activity;
import android.content.Context;

import com.king.run.intface.http.BaseHttp;
import com.king.run.intface.http.ReqBack;
import com.king.run.intface.http.request.SportReq;
import com.king.run.model.http.req.ExerciseReq;
import com.king.run.model.http.req.StatisticsBarReq;
import com.king.run.util.PrefName;
import com.king.run.util.Url;
import com.orhanobut.logger.Logger;

import org.xutils.http.RequestParams;

import java.util.HashMap;
import java.util.Map;

/**
 * 运动数据接口实现
 */

public class SportIml extends BaseHttp implements SportReq {

    private static Activity activity;

    private static class SingletonHolder {
        public static SportIml instance = new SportIml();
    }

    private SportIml() {
    }

    public static SportIml getInstance(Activity context) {
        activity = context;
        return SingletonHolder.instance;
    }

    /**
     * 上传运动数据
     */
    @Override
    public void uploadData(ExerciseReq req, ReqBack reqBack) {
        String url = BSAE_URL + UPLOAD_DATA;
        RequestParams params = new RequestParams(url);
        params.addHeader("token", req.getToken());
        Map<String, String> map = new HashMap<>();
        map.put("type", req.getType());
        map.put("step", req.getStep());
        map.put("target", req.getTarget());
        map.put("time", req.getTime());
        map.put("kilometre", req.getKilometre());
        map.put("calorie", req.getCalorie());
        map.put("pace", req.getPace());
        map.put("progress", req.getProgress());
        map.put("second", req.getSecond());
        map.put("token", req.getToken());
        reqJson(activity, POST, params, map, reqBack);
    }

    /**
     * 获取运动数据
     */
    @Override
    public void exerciseData(String token, ReqBack reqBack) {
        String url = BSAE_URL + EXERCISE_DATE;
        RequestParams params = new RequestParams(url);
        params.addHeader("token", token);
        params.addBodyParameter("token", token);
        get(activity, params, reqBack);
    }

    /**
     * 获取数据统计
     */
    @Override
    public void statisticsData(ReqBack reqBack) {
        String url = BSAE_URL + STATISTICAL_DATA;
        RequestParams params = new RequestParams(url);
        params.addHeader("token", PrefName.getToken(activity));
        params.addBodyParameter("token", PrefName.getToken(activity));
        Logger.d(params);
        get(activity, params, reqBack);
    }

    /**
     * 获取数据列表统计
     */
    @Override
    public void statisticsListData(int page, ReqBack reqBack) {
        String url = BSAE_URL + STATISTICAL_LIST_DATA + page;
        RequestParams params = new RequestParams(url);
        params.addHeader("token", PrefName.getToken(activity));
        params.addBodyParameter("token", PrefName.getToken(activity));
        Logger.d(params);
        get(activity, params, reqBack);
    }

    /**
     * 获取柱状图统计
     */
    @Override
    public void statisticsBarData(StatisticsBarReq req, ReqBack reqBack) {
        String url = BSAE_URL + HISTOGRAMGRAM_DATA;
        RequestParams params = new RequestParams(url);
        Logger.d(params);
        params.addHeader("token", req.getToken());
        params.addBodyParameter("token", req.getToken());
        params.addBodyParameter("type", req.getType());
        params.addBodyParameter("startTime", req.getStartTime());
        params.addBodyParameter("endTime", req.getEndTime());
        get(activity, params, reqBack);
    }

    @Override
    public void praise(ReqBack reqBack, int momentId) {
        RequestParams params = new RequestParams(Url.PRAISE_URL + momentId);
        params.addHeader("token", PrefName.getToken(activity));
        params.addBodyParameter("token", PrefName.getToken(activity));
        post(activity, params, reqBack);
    }

    @Override
    public void weather(String city, ReqBack reqBack) {
        RequestParams params = new RequestParams(Url.WEATHER + city);
        get(activity, params, reqBack);
    }
}
