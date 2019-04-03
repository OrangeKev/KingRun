package com.king.run.activity.mine.model;

import com.king.run.model.BaseResult;

import java.util.ArrayList;
import java.util.List;

/**
 * 作者：shuizi_wade on 2017/11/27 15:20
 * 邮箱：674618016@qq.com
 */
public class FansInfoResult extends BaseResult{
    private List<FansInfo> data = new ArrayList<>();

    public List<FansInfo> getData() {
        return data;
    }

    public void setData(List<FansInfo> data) {
        this.data = data;
    }
}
