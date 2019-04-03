package com.king.run.activity.statistics.model;

import com.king.run.model.BaseResult;
import com.king.run.model.http.res.ExerciseDetailInfo;

import java.util.ArrayList;
import java.util.List;

/**
 * 作者：shuizi_wade on 2018/1/12 16:51
 * 邮箱：674618016@qq.com
 */
public class StatisListResult extends BaseResult {
    private List<ExerciseDetailInfo> data = new ArrayList<>();

    public List<ExerciseDetailInfo> getData() {
        return data;
    }

    public void setData(List<ExerciseDetailInfo> data) {
        this.data = data;
    }
}
