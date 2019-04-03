package com.king.run.activity.circle.model;

import java.io.Serializable;

/**
 * 作者：水子wade on 2018/3/1 21:00
 * 邮箱：674618016@qq.com
 */
public class LikeUser implements Serializable {
    private String userId;
    private String avatar;

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }
}
