package com.king.run.activity.sport.walk.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.bumptech.glide.Glide;
import com.king.run.R;

import org.xutils.view.annotation.ViewInject;
import org.xutils.x;

import jp.wasabeef.glide.transformations.RoundedCornersTransformation;

/**
 * 作者：idmin on 2017/9/12 22:46
 * 邮箱：674618016@qq.com
 */

public class ShareRecordAdapter extends BaseAdapter {
    private int[] icons;
    private Context context;

    public ShareRecordAdapter(Context context, int[] icons) {
        this.context = context;
        this.icons = icons;
    }

    @Override
    public int getCount() {
        return icons.length + 1;
    }

    @Override
    public Object getItem(int i) {
        return icons[i];
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
            view = LayoutInflater.from(context).inflate(R.layout.activity_share_record_list_row, null);
            x.view().inject(holder, view);
            view.setTag(holder);
        } else holder = (Holder) view.getTag();
        if (i != 0) {
            holder.ly_share_record.setVisibility(View.GONE);
            holder.iv_share_record.setBackgroundResource(icons[i - 1]);
        } else {
            holder.ly_share_record.setVisibility(View.VISIBLE);
            holder.iv_share_record.setBackgroundResource(R.drawable.iv_bg_share_record);
        }
        return view;
    }

    private class Holder {
        @ViewInject(R.id.iv_share_record)
        ImageView iv_share_record;
        @ViewInject(R.id.ly_share_record)
        LinearLayout ly_share_record;
    }
}
