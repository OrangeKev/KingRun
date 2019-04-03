package com.king.run.model.http.res;

import java.util.ArrayList;

/**
 * 数据统计
 */

public class StatisticsRes {

    private ArrayList<StatisticsItem> calorie;
    private ArrayList<ExerciseDetailInfo> detailKilometre;
    private ArrayList<StatisticsItem> kilometre;
    private ArrayList<StatisticsItem> step;

    public ArrayList<StatisticsItem> getCalorie() {
        return calorie;
    }

    public void setCalorie(ArrayList<StatisticsItem> calorie) {
        this.calorie = calorie;
    }

    public ArrayList<ExerciseDetailInfo> getDetailKilometre() {
        return detailKilometre;
    }

    public void setDetailKilometre(ArrayList<ExerciseDetailInfo> detailKilometre) {
        this.detailKilometre = detailKilometre;
    }

    public ArrayList<StatisticsItem> getKilometre() {
        return kilometre;
    }

    public void setKilometre(ArrayList<StatisticsItem> kilometre) {
        this.kilometre = kilometre;
    }

    public ArrayList<StatisticsItem> getStep() {
        return step;
    }

    public void setStep(ArrayList<StatisticsItem> step) {
        this.step = step;
    }
}
