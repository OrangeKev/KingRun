package com.king.run.intface.http.request;

import com.king.run.intface.http.Api;
import com.king.run.intface.http.ReqBack;
import com.king.run.model.http.req.ExerciseReq;
import com.king.run.model.http.req.StatisticsBarReq;

import java.util.Map;

/**
 * 运动数据请求接口
 */

public interface SportReq extends Api {

    String TYPE_WALK = "0";
    String TYPE_RUN = "1";
    String TYPE_EXERCISE = "2";
    String TYPE_RIDE = "3";

    void uploadData(ExerciseReq req, ReqBack reqBack);

    void exerciseData(String token, ReqBack reqBack);

    void statisticsData(ReqBack reqBack);

    void statisticsListData(int page, ReqBack reqBack);

    void statisticsBarData(StatisticsBarReq statisticsBarReq, ReqBack reqBack);

    void praise(ReqBack reqBack, int momentId);

    void weather(String city, ReqBack reqBack);
}
