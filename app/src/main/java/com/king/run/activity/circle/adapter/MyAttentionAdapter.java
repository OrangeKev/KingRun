package com.king.run.activity.circle.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.king.run.R;
import com.king.run.activity.circle.model.CircleInfo;
import com.king.run.activity.mine.model.FansInfo;
import com.king.run.util.PicassoUtil;

import org.xutils.view.annotation.ViewInject;
import org.xutils.x;

import java.util.ArrayList;
import java.util.List;

/**
 * 作者：水子wade on 2017/11/12 22:05
 * 邮箱：674618016@qq.com
 */
public class MyAttentionAdapter extends BaseAdapter {
    private Context context;

    public MyAttentionAdapter(Context context) {
        this.context = context;
    }

    List<CircleInfo> circleInfos = new ArrayList<>();

    public void setList(List<CircleInfo> list) {
        this.circleInfos = list;
    }

    @Override
    public int getCount() {
        return circleInfos.size() > 3 ? 3 : circleInfos.size();
    }

    @Override
    public Object getItem(int i) {
        return circleInfos.get(i);
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
            view = LayoutInflater.from(context).inflate(R.layout.activity_attention_circle_list_row, null);
            x.view().inject(holder, view);
            view.setTag(holder);
        } else holder = (Holder) view.getTag();
        holder.btn_attention.setVisibility(View.GONE);
        PicassoUtil.displayImageSquare(holder.iv_avatar, circleInfos.get(i).getAvatar(), context);
        holder.tv_user_name.setText(circleInfos.get(i).getName());
        holder.tv_join_size.setText(circleInfos.get(i).getFollowers() + "人参与");
        return view;
    }

    private class Holder {
        @ViewInject(R.id.tv_user_name)
        TextView tv_user_name;
        @ViewInject(R.id.iv_avatar)
        ImageView iv_avatar;
        @ViewInject(R.id.tv_join_size)
        TextView tv_join_size;
        @ViewInject(R.id.btn_attention)
        Button btn_attention;
    }
}
