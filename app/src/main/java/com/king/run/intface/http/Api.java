package com.king.run.intface.http;

import com.king.run.util.Url;

/**
 * 接口
 */

public interface Api {
    //    String BSAE_URL = "http://182.254.149.193:8080/";
    String BSAE_URL = Url.BASE_URL;
    String UPLOAD_DATA = "v1/api/user/exerciseData";//上传运动信息
    String EXERCISE_DATE = "v1/api/user/exerciseData";//获取运动数据,最近一次上传的数据
    String STATISTICAL_DATA = "v1/api/user/statisticsData";//获取数据统计
    String STATISTICAL_LIST_DATA = "/v1/api/user/exerciseDetailData?page=";//获取数据列表统计
    String HISTOGRAMGRAM_DATA = "v1/api/user/statisticsBarData";//获取柱状图数据

}
