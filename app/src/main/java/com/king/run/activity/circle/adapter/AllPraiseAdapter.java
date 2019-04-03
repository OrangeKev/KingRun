package com.king.run.activity.circle.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.king.run.R;
import com.king.run.activity.circle.model.LikeUser;
import com.king.run.util.PicassoUtil;
import com.king.run.view.CircleImageView;

import org.xutils.view.annotation.ViewInject;
import org.xutils.x;

import java.util.List;

/**
 * 作者：水子wade on 2018/3/25 21:45
 * 邮箱：674618016@qq.com
 */
public class AllPraiseAdapter extends BaseAdapter {
    private Context context;
    private List<LikeUser> list;

    public AllPraiseAdapter(Context context, List<LikeUser> list) {
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
            view = LayoutInflater.from(context).inflate(R.layout.activity_fans_list_row, null);
            x.view().inject(holder, view);
            view.setTag(holder);
        } else holder = (Holder) view.getTag();
        PicassoUtil.displayImage(holder.iv_avatar, list.get(i).getAvatar(), context);
        return view;
    }

    private class Holder {
        @ViewInject(R.id.tv_user_name)
        TextView tv_user_name;
        @ViewInject(R.id.iv_avatar)
        CircleImageView iv_avatar;
    }
}
