package com.king.run.model.http.req;

/**
 * 上传运动数据请求参数
 */

public class ExerciseReq {

    private String type;//0=漫步 1：跑步 2锻炼 3，骑行
    private String kilometre;//公里数 小数点后保留两位
    private String calorie;//卡路里
    private String second;//时长秒数
    private String step;//步数
    private String target;//目标
    private String progress;//进度，此处是百分比，只需传分子值 30%，传30
    private String pace;//配速，传字符串值
    private String token;//token
    private String time;//上传数据当前时间


    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
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

    public String getSecond() {
        return second;
    }

    public void setSecond(String second) {
        this.second = second;
    }

    public String getStep() {
        return step;
    }

    public void setStep(String step) {
        this.step = step;
    }

    public String getTarget() {
        return target;
    }

    public void setTarget(String target) {
        this.target = target;
    }

    public String getProgress() {
        return progress;
    }

    public void setProgress(String progress) {
        this.progress = progress;
    }

    public String getPace() {
        return pace;
    }

    public void setPace(String pace) {
        this.pace = pace;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }


}
