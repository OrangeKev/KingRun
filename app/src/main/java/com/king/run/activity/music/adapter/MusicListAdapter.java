package com.king.run.activity.music.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.king.run.R;
import com.king.run.activity.music.model.MusicDetails;

import org.xutils.view.annotation.ViewInject;
import org.xutils.x;

import java.util.ArrayList;
import java.util.List;

/**
 * 作者：水子wade on 2018/1/29 22:24
 * 邮箱：674618016@qq.com
 */
public class MusicListAdapter extends BaseAdapter {
    private Context context;

    private List<MusicDetails> list = new ArrayList<>();

    public MusicListAdapter(Context context, List<MusicDetails> list) {
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
            view = LayoutInflater.from(context).inflate(R.layout.activity_music_list_teim_row, null);
            x.view().inject(holder, view);
            view.setTag(holder);
        } else holder = (Holder) view.getTag();
        holder.tv_music_name.setText(list.get(i).getMusicName());
        holder.tv_music_content.setText(list.get(i).getMusicSpecial());
        return view;
    }

    private class Holder {
        @ViewInject(R.id.tv_music_name)
        TextView tv_music_name;
        @ViewInject(R.id.tv_music_content)
        TextView tv_music_content;
        @ViewInject(R.id.iv_music_status)
        ImageView iv_music_status;
    }
}
