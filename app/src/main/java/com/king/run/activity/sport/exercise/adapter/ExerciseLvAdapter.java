package com.king.run.activity.sport.exercise.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.king.run.R;

import org.xutils.view.annotation.ViewInject;
import org.xutils.x;

/**
 * 作者：shuizi_wade on 2017/9/27 11:20
 * 邮箱：674618016@qq.com
 */
public class ExerciseLvAdapter extends BaseAdapter {
    private Context context;

    public ExerciseLvAdapter(Context context) {
        this.context = context;
    }

    @Override
    public int getCount() {
        return 6;
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
            view = LayoutInflater.from(context).inflate(R.layout.activity_exercise_lv_list_row, null);
            x.view().inject(holder, view);
            view.setTag(holder);
        } else holder = (Holder) view.getTag();
        return view;
    }

    private class Holder {
        @ViewInject(R.id.tv_name)
        TextView tv_name;
    }
}
