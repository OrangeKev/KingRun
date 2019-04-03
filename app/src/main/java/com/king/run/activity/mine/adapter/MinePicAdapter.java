package com.king.run.activity.mine.adapter;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.king.run.R;
import com.king.run.activity.mine.model.Albums;
import com.king.run.activity.mine.model.UserInfo;
import com.king.run.util.PicassoUtil;
import com.king.run.util.Url;
import com.king.run.util.Utils;
import com.king.run.view.SquareLayout;

import org.xutils.view.annotation.ViewInject;
import org.xutils.x;

import java.util.ArrayList;
import java.util.List;

/**
 * 作者：idmin on 2017/9/12 22:46
 * 邮箱：674618016@qq.com
 */

public class MinePicAdapter extends BaseAdapter {
    private Context context;
    private List<Albums> albums = new ArrayList<>();

    public MinePicAdapter(Context context) {
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
            view = LayoutInflater.from(context).inflate(R.layout.activity_share_record_list_row, null);
            x.view().inject(holder, view);
            view.setTag(holder);
        } else holder = (Holder) view.getTag();
        holder.ly_share_record.setVisibility(View.GONE);
//        if (albums.get(i).getUrlPre().contains("http")) {
        int width = Utils.getDisplayWidth((Activity) context);
        RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) holder.iv_share_record.getLayoutParams();
        RelativeLayout.LayoutParams params2 = (RelativeLayout.LayoutParams) holder.sq.getLayoutParams();
        params.width = width / 4;
        params.height = width / 4;
        params2.width = width / 4;
        params2.height = width / 4;
        holder.iv_share_record.setLayoutParams(params);
        holder.sq.setLayoutParams(params2);
        PicassoUtil.displayImageSquare(holder.iv_share_record, albums.get(i).getUrlPre(), context);
//        } else {
//            Bitmap bitmap = BitmapFactory.decodeStream(getClass().getResourceAsStream(albums.get(i).getUrlPre()));
//            holder.iv_share_record.setImageBitmap(bitmap);
//        }
        return view;
    }

    public void setAlbums(List<Albums> albums) {
        this.albums = albums;
    }

    private class Holder {
        @ViewInject(R.id.iv_share_record)
        ImageView iv_share_record;
        @ViewInject(R.id.ly_share_record)
        LinearLayout ly_share_record;
        @ViewInject(R.id.sq)
        SquareLayout sq;
    }
}
