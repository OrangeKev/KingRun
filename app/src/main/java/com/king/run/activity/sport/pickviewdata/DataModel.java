package com.king.run.activity.sport.pickviewdata;

import java.util.ArrayList;

/**
 * PickView 的数据
 **/
public class DataModel {

    /**
     * 初始化三个选项卡数据。
     */
    public static void initData(ArrayList<GoalBean> options1Items, ArrayList<ArrayList<String>> options2Items) {

        //选项1
        options1Items.add(new GoalBean(0, "距离目标", "用户设置的距离目标", ""));
        options1Items.add(new GoalBean(1, "时间目标", "用户设置的时间目标", ""));
        options1Items.add(new GoalBean(2, "卡路里目标", "用户设置的卡路里目标", ""));

        //选项2
        ArrayList<String> options2Items_01 = new ArrayList<>();
        options2Items_01.add("1公里");
        options2Items_01.add("3公里");
        options2Items_01.add("5公里");
        options2Items_01.add("10公里");
        options2Items_01.add("半程马拉松");
        options2Items_01.add("全程马拉松");

        ArrayList<String> options2Items_02 = new ArrayList<String>();
        String unitMin = "分钟";
        for (int i = 10; i <= 300; i += 10) {
            options2Items_02.add(i + unitMin);
        }

        ArrayList<String> options2Items_03 = new ArrayList<String>();
        String unitLcal = "大卡";
        for (int i = 100; i <= 4500; i += 200) {
            options2Items_03.add(i + unitLcal);
        }
        options2Items.add(options2Items_01);
        options2Items.add(options2Items_02);
        options2Items.add(options2Items_03);


    }
}
