package com.king.run.activity.sport.exercise.model;

import java.io.Serializable;

/**
 * 作者：shuizi_wade on 2017/9/28 10:36
 * 邮箱：674618016@qq.com
 */
public class Strategy implements Serializable {
    private String name;
    private boolean isChecked;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public boolean isChecked() {
        return isChecked;
    }

    public void setChecked(boolean checked) {
        isChecked = checked;
    }
}
