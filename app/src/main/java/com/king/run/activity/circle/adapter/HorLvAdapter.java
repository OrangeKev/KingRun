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
import com.king.run.util.PicassoUtil;

import org.xutils.view.annotation.ViewInject;
import org.xutils.x;

import java.util.ArrayList;
import java.util.List;

/**
 * 作者：水子wade on 2017/11/5 17:05
 * 邮箱：674618016@qq.com
 */
public class HorLvAdapter extends BaseAdapter {
    private Context context;
    private List<CircleInfo> list = new ArrayList<>();

    public HorLvAdapter(Context context) {
        this.context = context;
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
            view = LayoutInflater.from(context).inflate(R.layout.fragment_hot_hor_list_row, null);
            x.view().inject(holder, view);
            view.setTag(holder);
        } else holder = (Holder) view.getTag();
        PicassoUtil.displayImageSquare(holder.iv_img, list.get(i).getAvatar(), context);
        holder.tv_name.setText(list.get(i).getName());
        holder.tv_num.setText(list.get(i).getFollowers() + "人参与");
        return view;
    }

    public void setList(List<CircleInfo> list) {
        this.list = list;
    }

    private class Holder {
        @ViewInject(R.id.iv_img)
        ImageView iv_img;
        @ViewInject(R.id.tv_name)
        TextView tv_name;
        @ViewInject(R.id.tv_num)
        TextView tv_num;
    }
}
