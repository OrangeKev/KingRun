package com.king.run.util;

import java.text.DecimalFormat;

public class StepAlgorithm {
    public static String getKcal(double weight, Double distance) {

        // 跑步热量（kcal）＝体重（kg）×距离（公里）×1.036，换算一下
        return (int) Math.ceil(weight * distance * 1.036) + "";
    }

    /**
     * 计算行走的距离
     */
    public static double getDistance(int step, int step_length) {
        double distance;
        if (step % 2 == 0) {
            distance = (step / 2) * 3 * step_length * 0.01;
        } else {
            distance = ((step / 2) * 3 + 1) * step_length * 0.01;
        }
        return distance;
    }


    public static String getSpeedByDis(double distance, long seconds) {
        double speed = distance * 100 / seconds * 0.01;
        int needTime = (int) (1000 * 100 / speed * 0.01);

        int hour;
        int min;
        int s;
        String time;
        hour = needTime / 60 / 60;
        min = (needTime - hour * 60 * 60) / 60;
        s = (needTime - hour * 60 * 60) % 60;
        min = min + hour * 60;
//        if (hour == 0) {
        time = min + "'" + s;
//        } else {
//            time = hour + "'" + min + "'" + s;
//        }
        return time + "\"";
    }

    public static Float getSpeedTime(double distance, long seconds) {
        double speed = distance * 100 / seconds * 0.01;
        int needTime = (int) (1000 * 100 / speed * 0.01);

        int hour;
        int min;
        int s;
        String time;
        hour = needTime / 60 / 60;
        min = (needTime - hour * 60 * 60) / 60;
        s = (needTime - hour * 60 * 60) % 60;
//        if (hour == 0) {
        time = min + "." + s;
//        } else {
//            time = hour + "'" + min + "'" + s;
//        }
        return Float.parseFloat(String.valueOf(needTime));
    }

    public static String getSpeed(int stepCount, long seconds) {
        if (stepCount <= 0) {
            return "0";
        }
        double distance = stepCount * 0.45;
        double speed = distance * 100 / seconds * 0.01;
        int needTime = (int) (1000 * 100 / speed * 0.01);
        return getSpeedStr(needTime);
    }

    public static String getSpeedStr(int needTime) {
        int hour;
        int min;
        int s;
        String time;
        hour = needTime / 60 / 60;
        min = (needTime - hour * 60 * 60) / 60;
        s = (needTime - hour * 60 * 60) % 60;
        if (hour == 0) {
            time = min + "'" + s + "\"";
        } else {
            time = hour + "'" + min + "'" + s + "\"";
        }
        return time;
    }

    public static String getSpeed(double distance, long seconds) {
        double speed = distance * 100 / seconds * 0.01;
        int needTime = (int) (1000 * 100 / speed * 0.01);

        int hour;
        int min;
        int s;
        String time;
        hour = needTime / 60 / 60;
        min = (needTime - hour * 60 * 60) / 60;
        s = (needTime - hour * 60 * 60) % 60;
        if (hour == 0) {
            time = min + "'" + s + "\"";
        } else {
            time = hour + "'" + min + "'" + s + "\"";
        }
        return time;
    }

    public static String getDisString(int stepCount) {
        return getDfStr(stepCount * 0.45 / 1000);
    }

    public static double getDisDouble(int stepCount) {
        return stepCount * 0.45 / 1000;
    }

    private static String getDfStr(double d) {
        DecimalFormat df = new DecimalFormat("#.##");
        return df.format(d);
    }
}
