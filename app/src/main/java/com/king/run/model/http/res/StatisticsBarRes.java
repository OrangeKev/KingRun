package com.king.run.model.http.res;


import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;

/**
 * @author xwk
 */
public class StatisticsBarRes {
    private String avgStep;
    private String avgKilometre;
    private String avgSecond;
    private String avgCalorie;
    private String avgNumber;
    private ArrayList<StatisticsItem> barData;

    public String getAvgStep() {
        return avgStep;
    }

    public void setAvgStep(String avgStep) {
        this.avgStep = avgStep;
    }

    public String getAvgKilometre() {
        return avgKilometre;
    }

    public void setAvgKilometre(String avgKilometre) {
        this.avgKilometre = avgKilometre;
    }

    public String getAvgSecond() {
        return avgSecond;
    }

    public void setAvgSecond(String avgSecond) {
        this.avgSecond = avgSecond;
    }

    public String getAvgCalorie() {
        return avgCalorie;
    }

    public void setAvgCalorie(String avgCalorie) {
        this.avgCalorie = avgCalorie;
    }

    public String getAvgNumber() {
        return avgNumber;
    }

    public void setAvgNumber(String avgNumber) {
        this.avgNumber = avgNumber;
    }

    public ArrayList<StatisticsItem> getBarData() {
        return barData;
    }

    public void setBarData(ArrayList<StatisticsItem> barData) {
        this.barData = barData;
    }
}
