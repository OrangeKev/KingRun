package com.king.run.view;

import android.view.View;

import com.king.run.R;


/**
 * 作者：shuizi_wade on 2016/11/29 15:09
 * 邮箱：674618016@qq.com
 */

public class BindFootView {
    //上拉加载更多
    public static final int PULLUP_LOAD_MORE = 0;
    //正在加载中
    public static final int LOADING_MORE = 1;
    //没有加载更多 隐藏
    public static final int NO_LOAD_MORE = 2;

    public static void onBindFootView(FootViewHolder footViewHolder, int load_more_status) {
        switch (load_more_status) {
            case PULLUP_LOAD_MORE:
                footViewHolder.mLoadLayout.setVisibility(View.VISIBLE);
                footViewHolder.pro.setVisibility(View.INVISIBLE);
                footViewHolder.foot_view_item_tv.setText(R.string.idoc_pull_loading_more);
                break;
            case LOADING_MORE:
                footViewHolder.mLoadLayout.setVisibility(View.VISIBLE);
                footViewHolder.pro.setVisibility(View.VISIBLE);
                footViewHolder.foot_view_item_tv.setText(R.string.idoc_loading);
                break;
            case NO_LOAD_MORE:
                //隐藏加载更多
                footViewHolder.mLoadLayout.setVisibility(View.GONE);
                break;
        }
    }
}
