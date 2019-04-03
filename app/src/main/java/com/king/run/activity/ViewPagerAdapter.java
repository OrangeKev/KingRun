package com.king.run.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.v4.view.PagerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.king.run.R;
import com.king.run.activity.music.MusicListActivity;
import com.king.run.activity.music.model.MusicInfo;
import com.king.run.util.Utils;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

/**
 * 作者：水子wade on 2018/1/24 23:13
 * 邮箱：674618016@qq.com
 */
public class ViewPagerAdapter extends PagerAdapter {
    private Context context;
    private List<MusicInfo> list = new ArrayList<>();

    public ViewPagerAdapter(Context context, List<MusicInfo> list) {
        this.context = context;
        this.list = list;
    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == object;
    }

    @Override
    public Object instantiateItem(final ViewGroup container, final int position) {
        View view = null;
        Holder holder;
        if (null == view) {
            view = LayoutInflater.from(context).inflate(R.layout.viewpager_item, null);
            holder = new Holder();
            holder.iv_bg = view.findViewById(R.id.iv_bg);
            holder.iv_center = view.findViewById(R.id.iv_center);
            holder.item_root = view.findViewById(R.id.item_root);
            view.setTag(holder);
        } else
            holder = (Holder) view.getTag();
        int width = Utils.getDisplayWidth((Activity) context);
        ViewGroup.LayoutParams lp = holder.iv_bg.getLayoutParams();
        lp.width = (int) (width * 0.611);
        lp.height = (int) (width * 0.611);
        holder.iv_bg.setLayoutParams(lp);
        int resId = Utils.getResource(list.get(position).getImage().split("\\.")[0]);
        holder.iv_bg.setImageResource(resId);
        ViewGroup.LayoutParams params = holder.iv_center.getLayoutParams();
        params.width = (int) (width * 0.1);
        params.height = (int) (width * 0.1);
        holder.iv_center.setLayoutParams(params);
        holder.iv_center.setBackgroundResource(R.drawable.iv_bg_center_music);
        container.addView(view, position);
        holder.item_root.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent();
                Bundle bundle = new Bundle();
                bundle.putSerializable("detail", list.get(position));
                intent.putExtras(bundle);
                intent.setClass(context, MusicListActivity.class);
                context.startActivity(intent);
            }
        });
        return view;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((View) object);
    }

    public ImageView createImageView(int resId) {
        int pagerWidth = (int) (Utils.getDisplayWidth((Activity) context) / 5.0f);
        ImageView iv = new ImageView(context);
        ViewGroup.LayoutParams lp = iv.getLayoutParams();
        if (lp == null) {
            lp = new ViewGroup.LayoutParams(pagerWidth, ViewGroup.LayoutParams.MATCH_PARENT);
        } else {
            lp.width = pagerWidth;
            lp.height = ViewGroup.LayoutParams.MATCH_PARENT;
        }
        iv.setScaleType(ImageView.ScaleType.FIT_CENTER);
        iv.setImageResource(resId);
        iv.setLayoutParams(lp);
        return iv;
    }

    class Holder {
        ImageView iv_bg;
        ImageView iv_center;
        RelativeLayout item_root;
    }
}
