package com.king.run.activity.mine.model;

import java.io.Serializable;

/**
 * 作者：shuizi_wade on 2017/8/24 14:46
 * 邮箱：674618016@qq.com
 */
public class FansInfo implements Serializable {
    /**
     * "userId": "20171125qjguy06n",
     * "avatar": "img/avator/7c5l10nuckhk9mjp3bt90nmu.jpg",
     * "userName": "20171125qjguy06n"
     */
    private String avatar;
    private String userName;
    private String userId;
    private int type;
    private boolean isFollowing;

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public boolean isFollowing() {
        return isFollowing;
    }

    public void setFollowing(boolean following) {
        isFollowing = following;
    }
}
