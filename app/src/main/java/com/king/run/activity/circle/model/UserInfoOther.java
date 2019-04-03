package com.king.run.activity.circle.model;

import com.king.run.activity.mine.model.Albums;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * 作者：shuizi_wade on 2017/12/5 13:02
 * 邮箱：674618016@qq.com
 */
public class UserInfoOther implements Serializable{
    private String avator;
    private String nickName;
    private int follow;
    private int follower;
    private int collect;
    private int sex;
    private int age;
    private boolean isFollowed;
    private List<Albums> albums = new ArrayList<>();

    public int getSex() {
        return sex;
    }

    public void setSex(int sex) {
        this.sex = sex;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public boolean isFollowed() {
        return isFollowed;
    }

    public void setFollowed(boolean followed) {
        isFollowed = followed;
    }

    public String getAvator() {
        return avator;
    }

    public void setAvator(String avator) {
        this.avator = avator;
    }

    public String getNickName() {
        return nickName;
    }

    public void setNickName(String nickName) {
        this.nickName = nickName;
    }

    public int getFollow() {
        return follow;
    }

    public void setFollow(int follow) {
        this.follow = follow;
    }

    public int getFollower() {
        return follower;
    }

    public void setFollower(int follower) {
        this.follower = follower;
    }

    public int getCollect() {
        return collect;
    }

    public void setCollect(int collect) {
        this.collect = collect;
    }

    public List<Albums> getAlbums() {
        return albums;
    }

    public void setAlbums(List<Albums> albums) {
        this.albums = albums;
    }
}
