package com.king.run.view;

import android.content.Context;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.SwipeRefreshLayout;

import com.king.run.R;


/**
 * 作者：shuizi_wade on 2017/7/13 16:14
 * 邮箱：674618016@qq.com
 */
public class SwipRecycSetting {
    public static void setting(SwipeRefreshLayout swipeRefresh, Context context) {
//        recyclerView.addItemDecoration(new DividerItemDecoration(context, DividerItemDecoration.VERTICAL_LIST));
        //改变加载显示的颜色
        swipeRefresh.setColorSchemeColors(ContextCompat.getColor(context, R.color.blue_text_color)
                , ContextCompat.getColor(context, R.color.brand_text_color));
        //设置初始时的大小
        swipeRefresh.setSize(SwipeRefreshLayout.DEFAULT);
        //设置向下拉多少出现刷新
        swipeRefresh.setDistanceToTriggerSync(100);
        //设置刷新出现的位置
        swipeRefresh.setProgressViewEndTarget(false, 200);
    }
}
