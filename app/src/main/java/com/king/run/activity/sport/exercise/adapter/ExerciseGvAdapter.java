package com.king.run.activity.sport.exercise.adapter;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.king.run.R;
import com.king.run.util.Utils;

import org.xutils.common.util.DensityUtil;
import org.xutils.view.annotation.ViewInject;
import org.xutils.x;

/**
 * 作者：shuizi_wade on 2017/9/25 16:49
 * 邮箱：674618016@qq.com
 */
public class ExerciseGvAdapter extends BaseAdapter {
    private Context context;

    public ExerciseGvAdapter(Context context) {
        this.context = context;
    }

    @Override
    public int getCount() {
        return 4;
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
            view = LayoutInflater.from(context).inflate(R.layout.activity_exercise_gv_list_row, null);
            x.view().inject(holder, view);
            view.setTag(holder);
        } else holder = (Holder) view.getTag();
        int widthPx = Utils.getDisplayWidth((Activity) context);
//        int widthDp = DensityUtil.px2dip(widthPx);
        int ivWidth = (widthPx - DensityUtil.dip2px(16) * 2 - DensityUtil.dip2px(6)) / 2;
        RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) holder.iv_bg.getLayoutParams();
        params.width = ivWidth;
        params.height = (int) (ivWidth * 0.67);
        holder.iv_bg.setLayoutParams(params);
        holder.iv_bg.setBackgroundResource(R.mipmap.train_share_pic_1);
        holder.tv_name.setText("瑜伽入门");
        return view;
    }

    private class Holder {
        @ViewInject(R.id.iv_bg)
        ImageView iv_bg;
        @ViewInject(R.id.tv_name)
        TextView tv_name;
    }
}
