package com.king.run.intface.iml;

import com.king.run.commen.Constants;
import com.king.run.intface.GoalListener;

/**
 * 目标设置实现类
 */

public class GoalIml implements GoalListener {

    private static class SingletonHolder {
        public static GoalIml instance = new GoalIml();
    }

    private GoalIml() {
    }

    public static GoalIml getInstance() {
        return SingletonHolder.instance;
    }

    /**
     * 获取目标设置的类型
     */
    @Override
    public int getGoalType(int options) {

        switch (options) {
            case 0:
                return GOAL_TYPE_DISTANCE;
            case 1:
                return GOAL_TYPE_TIME;
            case 2:
                return GOAL_TYPE_KCAL;
        }
        return -1;
    }

    @Override
    public double getGoalValue(int type, String string) {
        double value = 0;
        switch (type) {
            case GOAL_TYPE_DISTANCE:
                if (string.equals("半程马拉松")) {
                    value = HALF_MARATHON;
                } else if (string.equals("全程马拉松")) {
                    value = FULL_MARATHON;
                } else {
                    value = Double.valueOf(string.replace("公里", "")) * 1000;
                }
                break;
            case GOAL_TYPE_TIME:
                value = Double.valueOf(string.replace("分钟", "")) * 60;
                break;
            case GOAL_TYPE_KCAL:
                value = Double.valueOf(string.replace("大卡", ""));
                break;
            default:
                value = 0;
        }
        return value;
    }

    @Override
    public String getGoalUnit(int type) {
        String unit = "";
        switch (type) {
            case GOAL_TYPE_DISTANCE:
                unit = "米";
                break;
            case GOAL_TYPE_TIME:
                unit = "分钟";
                break;
            case GOAL_TYPE_KCAL:
                unit = "大卡";
                break;
            default:
                break;
        }
        return unit;
    }
}
