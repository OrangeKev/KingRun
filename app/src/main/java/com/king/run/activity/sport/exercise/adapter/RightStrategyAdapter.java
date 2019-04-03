package com.king.run.activity.sport.exercise.adapter;

import android.app.Activity;
import android.content.Context;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.king.run.R;
import com.king.run.activity.sport.exercise.model.Strategy;
import com.king.run.util.Utils;

import org.xutils.common.util.DensityUtil;
import org.xutils.view.annotation.ViewInject;
import org.xutils.x;

import java.util.ArrayList;
import java.util.List;

/**
 * 作者：shuizi_wade on 2017/9/28 10:45
 * 邮箱：674618016@qq.com
 */
public class RightStrategyAdapter extends BaseAdapter {
    private Context context;

    public RightStrategyAdapter(Context context) {
        this.context = context;
    }

    @Override
    public int getCount() {
        return 5;
    }

    @Override
    public Object getItem(int i) {
        return i;
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        Holder holder;
        if (view == null) {
            holder = new Holder();
            view = LayoutInflater.from(context).inflate(R.layout.activity_recommended_strategy_right_list_row, null);
            x.view().inject(holder, view);
            view.setTag(holder);
        } else holder = (Holder) view.getTag();
        int width = Utils.getDisplayWidth((Activity) context);
        RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) holder.ly_bg.getLayoutParams();
        int lyWidth = (width - DensityUtil.dip2px(90 + 24 + 4)) / 2;
        params.width = lyWidth;
        params.height = lyWidth;
        holder.ly_bg.setLayoutParams(params);
        return view;
    }

    private class Holder {
        @ViewInject(R.id.tv_name)
        TextView tv_name;
        @ViewInject(R.id.ly_bg)
        RelativeLayout ly_bg;
    }
}
