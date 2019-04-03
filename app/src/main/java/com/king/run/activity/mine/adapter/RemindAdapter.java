package com.king.run.activity.mine.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.king.run.R;
import com.king.run.activity.mine.model.RemindData;

import org.xutils.view.annotation.ViewInject;
import org.xutils.x;

import java.util.List;

/**
 * 作者：shuizi_wade on 2018/1/5 11:00
 * 邮箱：674618016@qq.com
 */
public class RemindAdapter extends BaseAdapter {
    public Context context;
    List<RemindData> list;

    public void setList(List<RemindData> list) {
        this.list = list;
    }

    public RemindAdapter(Context context) {
        this.context = context;
    }

    public RemindAdapter(Context context, List<RemindData> list) {
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
            view = LayoutInflater.from(context).inflate(R.layout.activity_train_remind_list_row, null);
            x.view().inject(holder, view);
            view.setTag(holder);
        } else holder = (Holder) view.getTag();
        int hour = list.get(i).getHour();
        int minute = list.get(i).getMin();
        String h = hour + "";
        String m = minute + "";
        if (hour < 10)
            h = "0" + h;
        if (minute < 10)
            m = "0" + m;
        holder.tv_time.setText(h + ":" + m);
        holder.tv_repet.setText(list.get(i).getRepetStr());
        return view;
    }

    private class Holder {
        @ViewInject(R.id.tv_repet)
        TextView tv_repet;
        @ViewInject(R.id.tv_time)
        TextView tv_time;
    }
}
