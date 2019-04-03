package com.king.run.activity.music.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * 作者：水子wade on 2018/2/5 20:37
 * 邮箱：674618016@qq.com
 */
public class MusicInfo implements Serializable {
    private String image;
    private String type;
    private String title;
    private String step;
    private String desc;
    private String isuse;
    private String isdownload;
    private List<MusicDetails> musics = new ArrayList<>();

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getStep() {
        return step;
    }

    public void setStep(String step) {
        this.step = step;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public String getIsuse() {
        return isuse;
    }

    public void setIsuse(String isuse) {
        this.isuse = isuse;
    }

    public String getIsdownload() {
        return isdownload;
    }

    public void setIsdownload(String isdownload) {
        this.isdownload = isdownload;
    }

    public List<MusicDetails> getMusics() {
        return musics;
    }

    public void setMusics(List<MusicDetails> musics) {
        this.musics = musics;
    }
}
