package com.king.run.activity.circle.adapter;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.king.run.R;
import com.king.run.activity.mine.model.Albums;
import com.king.run.util.PicassoUtil;
import com.king.run.util.Utils;

import org.xutils.view.annotation.ViewInject;
import org.xutils.x;

import java.util.ArrayList;
import java.util.List;

/**
 * 作者：idmin on 2017/9/12 22:46
 * 邮箱：674618016@qq.com
 */

public class GvCirclePicAdapter extends BaseAdapter {
    private Context context;
    private List<Albums> albums = new ArrayList<>();

    public GvCirclePicAdapter(Context context) {
        this.context = context;
    }

    @Override
    public int getCount() {
        return albums.size();
    }

    @Override
    public Object getItem(int i) {
        return albums.get(i);
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
            view = LayoutInflater.from(context).inflate(R.layout.circle_gv_pic_list_row, null);
            x.view().inject(holder, view);
            view.setTag(holder);
        } else holder = (Holder) view.getTag();
        int width = Utils.getDisplayWidth((Activity) context);
        RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) holder.iv_pic.getLayoutParams();
        params.width = (int) (width * 0.222);
        params.height = (int) (width * 0.222);
        holder.iv_pic.setLayoutParams(params);
        PicassoUtil.displayImageSquare(holder.iv_pic, albums.get(i).getUrlPre(), context);
        return view;
    }

    public void setAlbums(List<Albums> albums) {
        this.albums = albums;
    }

    private class Holder {
        @ViewInject(R.id.iv_pic)
        ImageView iv_pic;
    }
}
