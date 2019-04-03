package com.king.run.activity.posture.adapter;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.king.run.R;
import com.king.run.activity.posture.ChangeNameActivity;
import com.king.run.activity.posture.model.ShoeInfo;
import com.king.run.activity.posture.model.iBeaconClass;

import org.xutils.view.annotation.ViewInject;
import org.xutils.x;

import java.util.ArrayList;
import java.util.List;

/**
 * 作者：水子wade on 2018/1/1 20:07
 * 邮箱：674618016@qq.com
 */
public class MyShoesAdapter extends BaseAdapter {
    private Context mContext;
    private List<iBeaconClass.iBeacon> list = new ArrayList<>();

    public void setList(List<iBeaconClass.iBeacon> list) {
        this.list = list;
    }

    private OnEditNameClickListener editNameClickListener;

    public MyShoesAdapter(Context context, List<iBeaconClass.iBeacon> list) {
        this.mContext = context;
        this.list = list;
    }

    public onRelieveClickListener listener;

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
    public View getView(final int i, View view, ViewGroup viewGroup) {
        Holder holder;
        if (view == null) {
            holder = new Holder();
            view = LayoutInflater.from(mContext).inflate(R.layout.activity_my_shoes_list_row, null);
            x.view().inject(holder, view);
            view.setTag(holder);
        } else holder = (Holder) view.getTag();
        if (list.get(i).isConnect) {
            holder.btn_remove_again_bind.setBackgroundResource(R.drawable.common_btn_click);
            holder.tv_type.setTextColor(ContextCompat.getColor(mContext, R.color.text_color_black_common));
            holder.tv_type.setText("绑定中");
        } else {
            holder.btn_remove_again_bind.setBackgroundResource(R.drawable.btn_remove_bind);
            holder.tv_type.setTextColor(ContextCompat.getColor(mContext, R.color.color_62000000));
            holder.tv_type.setText("未绑定");
        }
        holder.tv_name.setText(list.get(i).getName());
        holder.btn_change_name.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                editNameClickListener.editClick(i);
            }
        });
        holder.btn_remove_again_bind.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                listener.onClickListener(i);
            }
        });
        return view;
    }

    public interface OnEditNameClickListener {
        void editClick(int position);
    }

    public void setOnEditNameClickListener(OnEditNameClickListener listener) {
        editNameClickListener = listener;
    }

    private class Holder {
        @ViewInject(R.id.iv_shoe)
        ImageView iv_shoe;
        @ViewInject(R.id.tv_name)
        TextView tv_name;
        @ViewInject(R.id.tv_num)
        TextView tv_num;
        @ViewInject(R.id.tv_type)
        TextView tv_type;
        @ViewInject(R.id.btn_remove_again_bind)
        Button btn_remove_again_bind;
        @ViewInject(R.id.btn_change_name)
        Button btn_change_name;
    }

    public interface onRelieveClickListener {
        void onClickListener(int position);
    }

    public void setOnRelieveClickListener(onRelieveClickListener listener) {
        this.listener = listener;
    }
}
