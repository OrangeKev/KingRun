package com.king.run.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * 作者：shuizi_wade on 2018/1/8 16:34
 * 邮箱：674618016@qq.com
 */
public class Weather implements Serializable {
    /**
     * date: "20180108",
     * message: "Success !",
     * status: 200,
     * city: "北京",
     * count: 1374,
     */
    private String date;
    private String message;
    private int status;
    private String city;
    private int count;

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    private WeatherData data;

    public WeatherData getData() {
        return data;
    }

    public void setData(WeatherData data) {
        this.data = data;
    }
}

