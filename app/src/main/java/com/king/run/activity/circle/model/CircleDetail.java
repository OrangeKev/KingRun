package com.king.run.activity.circle.model;

import java.io.Serializable;

/**
 * 作者：shuizi_wade on 2017/12/6 11:42
 * 邮箱：674618016@qq.com
 */
public class CircleDetail implements Serializable{
    /**
     * id: 2,
     name: "晚上一起跑步",
     introduce: "晚上相约一起跑步",
     pic: "img/avator/b23bvk7pt8etid8j5c0jitgo.jpg",
     avatar: "img/avator/b23bvk7pt8etid8j5c0jitgo.jpg",
     isFollowed: 1,
     followers: 2
     */
    private int id;
    private String name;
    private String introduce;
    private String avatar;
    private int isFollowed;
    private int followers;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getIntroduce() {
        return introduce;
    }

    public void setIntroduce(String introduce) {
        this.introduce = introduce;
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
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
}
