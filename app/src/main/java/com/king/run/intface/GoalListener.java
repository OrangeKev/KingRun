package com.king.run.intface;

/**
 * 目标设置接口
 */

public interface GoalListener {

    //目标设置
    String GOAL_VALUE = "GoalValue";
    String GOAL_TYPE = "GoalSettings";
    int GOAL_TYPE_DISTANCE = 0;
    int GOAL_TYPE_TIME = 1;
    int GOAL_TYPE_KCAL = 2;
    double HALF_MARATHON = 21097.5;//半程马拉松，单位m
    double FULL_MARATHON = 42195;//全程马拉松，单位m

    int getGoalType(int options);

    double getGoalValue(int type, String string);

    String getGoalUnit(int type);
}
