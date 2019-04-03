package com.king.run.activity.circle.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.king.run.R;
import com.king.run.activity.circle.model.CircleInfo;

import org.xutils.view.annotation.ViewInject;
import org.xutils.x;

import java.util.ArrayList;
import java.util.List;

/**
 * 作者：水子wade on 2017/11/23 21:53
 * 邮箱：674618016@qq.com
 */
public class CircleNameListAdapter extends BaseAdapter {
    private Context context;
    private List<CircleInfo> list = new ArrayList<>();

    public CircleNameListAdapter(Context context, List<CircleInfo> list) {
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
            view = LayoutInflater.from(context).inflate(R.layout.activity_circle_name_list_row, null);
            x.view().inject(holder, view);
            view.setTag(holder);
        } else holder = (Holder) view.getTag();
        holder.tv_name.setText(list.get(i).getName());
        return view;
    }


    private class Holder {
        @ViewInject(R.id.tv_name)
        TextView tv_name;
    }
}
