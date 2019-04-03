package com.king.run.activity.sport.walk.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.king.run.R;
import com.king.run.activity.sport.walk.model.VideoInfo;

import org.xutils.view.annotation.ViewInject;
import org.xutils.x;

import java.util.ArrayList;
import java.util.List;

/**
 * 作者：水子wade on 2017/9/20 22:50
 * 邮箱：674618016@qq.com
 */
public class VideoInfoListAdapter extends BaseAdapter {
    private Context context;
    private List<VideoInfo> mList = new ArrayList<>();

    public VideoInfoListAdapter(Context context) {
        this.context = context;
    }

    @Override
    public int getCount() {
        return 6;
    }

    @Override
    public Object getItem(int i) {
        return mList.get(i);
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
            view = LayoutInflater.from(context).inflate(R.layout.activity_warm_up_video_list_roe, null);
            x.view().inject(holder, view);
            view.setTag(holder);
        } else {
            holder = (Holder) view.getTag();
        }
        return view;
    }

    public void setmList(List<VideoInfo> mList) {
        this.mList = mList;
    }

    private class Holder {
        @ViewInject(R.id.iv_img)
        ImageView iv_img;
        @ViewInject(R.id.tv_name)
        TextView tv_name;
        @ViewInject(R.id.tv_content)
        TextView tv_content;
    }
}
