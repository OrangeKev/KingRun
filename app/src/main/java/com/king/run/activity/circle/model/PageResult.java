package com.king.run.activity.circle.model;

import com.king.run.model.BaseResult;

import java.util.ArrayList;
import java.util.List;

/**
 * 作者：水子wade on 2017/11/26 21:21
 * 邮箱：674618016@qq.com
 */
public class PageResult extends BaseResult {
    private List<Moment> data = new ArrayList<>();

    public List<Moment> getData() {
        return data;
    }

    public void setData(List<Moment> data) {
        this.data = data;
    }
}
