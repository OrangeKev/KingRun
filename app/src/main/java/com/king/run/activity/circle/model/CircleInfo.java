package com.king.run.activity.circle.model;

import java.io.Serializable;

/**
 * 作者：shuizi_wade on 2017/11/10 16:38
 * 邮箱：674618016@qq.com
 */
public class CircleInfo implements Serializable {
    /**
     * "id": 1,
     * "name": "跑友圈",
     * "introduce": "这是一个跑友圈",
     * "pic": null,    //背景图
     * "avatar": null, //头像  ,目前为空 后续加上
     * "isFollowed": 1,      是否已关注 1是 0不是
     * "followers": 1322
     */
    private int id;
    private int isFollowed;
    private int followers;
    private String introduce;
    private String pic;
    private String avatar;
    private String name;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getIsFollowed() {
        return isFollowed;
    }

    public void setIsFollowed(int isFollowed) {
        this.isFollowed = isFollowed;
    }

    public int getFollowers() {
        return followers;
    }

    public void setFollowers(int followers) {
        this.followers = followers;
    }

    public String getIntroduce() {
        return introduce;
    }

    public void setIntroduce(String introduce) {
        this.introduce = introduce;
    }

    public String getPic() {
        return pic;
    }

    public void setPic(String pic) {
        this.pic = pic;
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
