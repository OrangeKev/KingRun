package com.king.run.model.http.res;

import java.io.Serializable;

/**
 * 作者：水子wade on 2017/12/20 21:26
 * 邮箱：674618016@qq.com
 */
public class ExerciseDetailInfo implements Serializable {
    /**
     * avator: null,
     * kilometre: 0,
     * calorie: 0,
     * step: 0,
     * second: 8,
     * pace: "0",
     * progress: "0",
     * target: null,
     * date: 1513775288000
     */
    private String avator;
    private String kilometre;
    private String calorie;
    private String step;
    private String pace;
    private String progress;
    private String target;
    private long second;
    private long date;

    public String getAvator() {
        return avator;
    }

    public void setAvator(String avator) {
        this.avator = avator;
    }

    public String getKilometre() {
        return kilometre;
    }

    public void setKilometre(String kilometre) {
        this.kilometre = kilometre;
    }

    public String getCalorie() {
        return calorie;
    }

    public void setCalorie(String calorie) {
        this.calorie = calorie;
    }

    public String getStep() {
        return step;
    }

    public void setStep(String step) {
        this.step = step;
    }

    public String getPace() {
        return pace;
    }

    public void setPace(String pace) {
        this.pace = pace;
    }

    public String getProgress() {
        return progress;
    }

    public void setProgress(String progress) {
        this.progress = progress;
    }

    public String getTarget() {
        return target;
    }

    public void setTarget(String target) {
        this.target = target;
    }

    public long getSecond() {
        return second;
    }

    public void setSecond(long second) {
        this.second = second;
    }

    public long getDate() {
        return date;
    }

    public void setDate(long date) {
        this.date = date;
    }
}
