package com.king.run.activity.sport.exercise.adapter;

import android.content.Context;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.king.run.R;
import com.king.run.activity.sport.exercise.model.Strategy;

import org.xutils.view.annotation.ViewInject;
import org.xutils.x;

import java.util.ArrayList;
import java.util.List;

/**
 * 作者：shuizi_wade on 2017/9/28 10:45
 * 邮箱：674618016@qq.com
 */
public class StrategyAdapter extends BaseAdapter {
    private Context context;

    public void setList(List<Strategy> list) {
        this.list = list;
    }

    private List<Strategy> list = new ArrayList<>();

    public StrategyAdapter(Context context, List<Strategy> list) {
        this.context = context;
        this.list = list;
    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public Object getItem(int i) {
        return list.get(i);
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
            view = LayoutInflater.from(context).inflate(R.layout.activity_strategy_list_row, null);
            x.view().inject(holder, view);
            view.setTag(holder);
        } else holder = (Holder) view.getTag();
        if (list.get(i).isChecked())
            holder.tv_name.setBackgroundColor(ContextCompat.getColor(context, R.color.common_bg));
        else
            holder.tv_name.setBackgroundColor(ContextCompat.getColor(context, R.color.color_e7));
        holder.tv_name.setText(list.get(i).getName());
        return view;
    }

    private class Holder {
        @ViewInject(R.id.tv_name)
        TextView tv_name;
    }
}
