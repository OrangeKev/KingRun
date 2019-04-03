package com.king.run.activity.circle.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * 作者：水子wade on 2017/11/19 16:21
 * 邮箱：674618016@qq.com
 */
public class CircleInfoResult implements Serializable {
    private List<CircleInfo> data = new ArrayList<>();

    public List<CircleInfo> getData() {
        return data;
    }

    public void setData(List<CircleInfo> data) {
        this.data = data;
    }
}
