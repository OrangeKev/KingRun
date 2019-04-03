package com.king.run.activity.statistics.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.king.run.R;
import com.king.run.activity.statistics.model.ItemBar;

import java.util.ArrayList;
import java.util.List;
import java.util.zip.Inflater;


/**
 * @author xwk
 */
public class GridAdapter extends BaseAdapter {

    private Context context;
    private List<ItemBar> datas = new ArrayList<>();

    public GridAdapter(Context context, List<ItemBar> datas) {
        this.context = context;
        this.datas = datas;
    }

    @Override
    public int getCount() {
        return datas.size();
    }

    @Override
    public Object getItem(int i) {
        return datas.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        Holder holder;
        if (view == null) {
            view = LayoutInflater.from(context).inflate(R.layout.item_gv_bar, viewGroup, false);
            holder = new Holder();
            holder.value = view.findViewById(R.id.tv_value);
            holder.unit = view.findViewById(R.id.tv_unit);
            holder.describe = view.findViewById(R.id.tv_describe);
            view.setTag(holder);
        } else {
            holder = (Holder) view.getTag();
        }
        holder.describe.setText(datas.get(i).getDescribe());
        holder.unit.setText(datas.get(i).getUnit());
        holder.value.setText(datas.get(i).getValue());
        return view;
    }

    private class Holder {
        TextView value;
        TextView unit;
        TextView describe;
    }
}
