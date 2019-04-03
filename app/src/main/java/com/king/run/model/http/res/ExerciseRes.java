package com.king.run.model.http.res;

import java.util.ArrayList;

/**
 * 上传运动数据返回参数
 */

public class ExerciseRes {

    private int type;
    private double kilometre=0;
    private int second;
    private int calorie;

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public double getKilometre() {
        return kilometre;
    }

    public void setKilometre(double kilometre) {
        this.kilometre = kilometre;
    }

    public int getSecond() {
        return second;
    }

    public void setSecond(int second) {
        this.second = second;
    }

    public int getCalorie() {
        return calorie;
    }

    public void setCalorie(int calorie) {
        this.calorie = calorie;
    }

}
