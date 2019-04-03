package com.king.run.activity.music.model;

import java.io.Serializable;

/**
 * 作者：水子wade on 2018/2/5 20:40
 * 邮箱：674618016@qq.com
 */
public class MusicDetails implements Serializable{
    private String musicName;
    private String musicSpecial;
    private String musicUrl;

    public String getMusicName() {
        return musicName;
    }

    public void setMusicName(String musicName) {
        this.musicName = musicName;
    }

    public String getMusicSpecial() {
        return musicSpecial;
    }

    public void setMusicSpecial(String musicSpecial) {
        this.musicSpecial = musicSpecial;
    }

    public String getMusicUrl() {
        return musicUrl;
    }

    public void setMusicUrl(String musicUrl) {
        this.musicUrl = musicUrl;
    }
}
