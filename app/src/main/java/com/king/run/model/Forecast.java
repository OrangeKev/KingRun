package com.king.run.model;

import java.io.Serializable;

/**
 * 作者：shuizi_wade on 2018/1/8 17:23
 * 邮箱：674618016@qq.com
 */
public class Forecast implements Serializable {
    /**
     * date: "08日星期一",
     * sunrise: "08:02",
     * high: "高温 8.0℃",
     * low: "低温 -2.0℃",
     * sunset: "18:18",
     * aqi: 104,
     * fx: "无持续风向",
     * fl: "<3级",
     * type: "多云",
     * notice: "绵绵的云朵，形状千变万化"
     */
    private String type;
    private String fx;
    private String fl;
    private String notice;
    private String date;
    private String sunrise;
    private String high;
    private String low;
    private String sunset;
    private int aqi;

    public String getFx() {
        return fx;
    }

    public void setFx(String fx) {
        this.fx = fx;
    }

    public String getFl() {
        return fl;
    }

    public void setFl(String fl) {
        this.fl = fl;
    }

    public String getNotice() {
        return notice;
    }

    public void setNotice(String notice) {
        this.notice = notice;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getSunrise() {
        return sunrise;
    }

    public void setSunrise(String sunrise) {
        this.sunrise = sunrise;
    }

    public String getHigh() {
        return high;
    }

    public void setHigh(String high) {
        this.high = high;
    }

    public String getLow() {
        return low;
    }

    public void setLow(String low) {
        this.low = low;
    }

    public String getSunset() {
        return sunset;
    }

    public void setSunset(String sunset) {
        this.sunset = sunset;
    }

    public int getAqi() {
        return aqi;
    }

    public void setAqi(int aqi) {
        this.aqi = aqi;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}

