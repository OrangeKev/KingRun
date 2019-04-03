package com.king.run.activity.mine.adapter;

import android.app.Activity;
import android.content.Context;
import android.support.v4.content.ContextCompat;
import android.util.DisplayMetrics;
import android.util.Log;
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
import com.king.run.util.StringUtil;
import com.king.run.util.Url;

import java.util.ArrayList;
import java.util.List;

/**
 * 作者：shuizi_wade on 2017/8/22 17:05
 * 邮箱：674618016@qq.com
 */
public class EditInfoPicAdapter extends BaseAdapter {
    private Context context;
    private List<Albums> albums = new ArrayList<>();

    public EditInfoPicAdapter(Context context) {
        this.context = context;
    }

    @Override
    public int getCount() {
        int size = albums.size();
        if (size < 8) size = size + 1;
        return size;
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
        ViewHolder holder;
        if (view == null) {
            view = LayoutInflater.from(context).inflate(R.layout.activity_edit_info_pic_list_row, null);
            holder = new ViewHolder();
            holder.iv_pic = (ImageView) view.findViewById(R.id.iv_pic);
            holder.iv_add = (ImageView) view.findViewById(R.id.iv_add);
            holder.ly_re = (RelativeLayout) view.findViewById(R.id.ly_re);
            view.setTag(holder);
        } else holder = (ViewHolder) view.getTag();
        // 通过WindowManager获取
        DisplayMetrics dm = new DisplayMetrics();
        ((Activity) context).getWindowManager().getDefaultDisplay().getMetrics(dm);
        LinearLayout.LayoutParams layoutParams = (LinearLayout.LayoutParams) holder.ly_re.getLayoutParams();
        layoutParams.height = (dm.widthPixels - 10) / 4;
        layoutParams.width = (dm.widthPixels - 10) / 4;
        holder.ly_re.setLayoutParams(layoutParams);
        if (albums.size() >= 8) {
            String url = albums.get(i).getUrlPre();
            PicassoUtil.displayImage(holder.iv_pic, url, context);
            holder.iv_pic.setVisibility(View.VISIBLE);
            holder.iv_add.setVisibility(View.GONE);
        } else {
            if (i == albums.size()) {
                holder.iv_pic.setVisibility(View.GONE);
                holder.iv_add.setVisibility(View.VISIBLE);
                holder.ly_re.setBackgroundColor(ContextCompat.getColor(context, R.color.view_color));
                holder.iv_add.setImageResource(R.mipmap.mine_icon_addphoto);
            } else {
                holder.iv_pic.setVisibility(View.VISIBLE);
                holder.iv_add.setVisibility(View.GONE);
                holder.ly_re.setBackgroundColor(ContextCompat.getColor(context, R.color.white_color));
                String url = albums.get(i).getUrlPre();
                PicassoUtil.displayImage(holder.iv_pic, url, context);

            }
        }
        return view;
    }

    public void setAlbums(List<Albums> albums) {
        this.albums = albums;
    }

    class ViewHolder {
        ImageView iv_pic;
        RelativeLayout ly_re;
        ImageView iv_add;
    }
}
