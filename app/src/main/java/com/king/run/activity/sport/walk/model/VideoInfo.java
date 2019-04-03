package com.king.run.activity.sport.walk.model;

import java.io.Serializable;

/**
 * 作者：水子wade on 2017/9/20 22:49
 * 邮箱：674618016@qq.com
 */
public class VideoInfo implements Serializable{
    private String imgUrl;
    private String name;
    private String content;

    public String getImgUrl() {
        return imgUrl;
    }

    public void setImgUrl(String imgUrl) {
        this.imgUrl = imgUrl;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }
}
