package com.king.run.activity.circle.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import com.king.run.R;
import com.king.run.activity.circle.model.LikeUser;
import com.king.run.activity.mine.model.Albums;
import com.king.run.util.PicassoUtil;
import com.king.run.util.PrefName;
import com.king.run.util.Url;

import org.xutils.view.annotation.ViewInject;
import org.xutils.x;

import java.util.ArrayList;
import java.util.List;

/**
 * 作者：idmin on 2017/9/12 22:46
 * 邮箱：674618016@qq.com
 */

public class PraiseAdapter extends BaseAdapter {
    private Context context;
    private List<LikeUser> likeUsers = new ArrayList<>();

    public PraiseAdapter(Context context) {
        this.context = context;
    }

    @Override
    public int getCount() {
        if (likeUsers.size() < 7)
            return likeUsers.size() + 1;
        else
            return 7;
    }

    @Override
    public Object getItem(int i) {
        return likeUsers.get(i);
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
            view = LayoutInflater.from(context).inflate(R.layout.layout_praise_avatar, null);
            x.view().inject(holder, view);
            view.setTag(holder);
        } else holder = (Holder) view.getTag();
        String url = likeUsers.get(i).getAvatar();
        if (i == 6 && likeUsers.size() > 7) {
            holder.iv_avatar.setImageResource(R.mipmap.social_pic_like_more);
        } else {
            if (!likeUsers.get(i).getAvatar().contains("http:"))
                url = Url.BASE_URL + url;
            PicassoUtil.displayImageSquare(holder.iv_avatar, url, context);
        }
        return view;
    }

    public void setAlbums(List<LikeUser> albums) {
        this.likeUsers = albums;
    }

    private class Holder {
        @ViewInject(R.id.iv_avatar)
        ImageView iv_avatar;
    }
}
