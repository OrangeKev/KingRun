package com.king.run.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * 作者：shuizi_wade on 2018/1/8 17:22
 * 邮箱：674618016@qq.com
 */
public class WeatherData implements Serializable {
    /**
     * shidu: "28%",
     * pm25: 15,
     * pm10: 35,
     * quality: "优",
     * wendu: "0",
     * ganmao: "各类人群可自由活动",
     */
    private int pm25;
    private int pm10;
    private String wendu;
    private String shidu;
    private String quality;
    private String ganmao;

    public int getPm10() {
        return pm10;
    }

    public void setPm10(int pm10) {
        this.pm10 = pm10;
    }

    public String getShidu() {
        return shidu;
    }

    public void setShidu(String shidu) {
        this.shidu = shidu;
    }

    public String getQuality() {
        return quality;
    }

    public void setQuality(String quality) {
        this.quality = quality;
    }

    public String getGanmao() {
        return ganmao;
    }

    public void setGanmao(String ganmao) {
        this.ganmao = ganmao;
    }

    private List<Forecast> forecast = new ArrayList<>();

    public int getPm25() {
        return pm25;
    }

    public void setPm25(int pm25) {
        this.pm25 = pm25;
    }

    public String getWendu() {
        return wendu;
    }

    public void setWendu(String wendu) {
        this.wendu = wendu;
    }

    public List<Forecast> getForecast() {
        return forecast;
    }

    public void setForecast(List<Forecast> forecast) {
        this.forecast = forecast;
    }
}
