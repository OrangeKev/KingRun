package com.king.run.activity.circle.adapter;

import android.content.Context;
import android.content.Intent;
import android.icu.math.BigDecimal;
import android.net.Uri;
import android.support.v4.content.ContextCompat;
import android.view.View;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.github.library.BaseRecyclerAdapter;
import com.github.library.BaseViewHolder;
import com.king.run.R;
import com.king.run.activity.circle.model.Moment;
import com.king.run.activity.mine.adapter.MinePicAdapter;
import com.king.run.activity.mine.model.Albums;
import com.king.run.util.PicassoUtil;
import com.king.run.util.PrefName;
import com.king.run.util.StringUtil;
import com.king.run.util.TimeUtil;

import java.util.ArrayList;
import java.util.List;

/**
 * 作者：水子wade on 2017/11/27 00:07
 * 邮箱：674618016@qq.com
 */
public class MAdapter extends BaseRecyclerAdapter<Moment> {
    private Context context;

    public MAdapter(Context context, List<Moment> data, int layoutResId) {
        super(context, data, layoutResId);
        this.context = context;
    }

    @Override
    protected void convert(BaseViewHolder holder, Moment itemData) {
        holder.setText(R.id.tv_user_name, itemData.getName());
        holder.setText(R.id.tv_content, itemData.getContent());
        holder.setText(R.id.tv_time, TimeUtil.getSimpleFullDateStr(itemData.getDate()));
        ListView listview = holder.getView(R.id.lv);
        GridView gv = holder.getView(R.id.gv_pic);
        ImageView iv_avatar = holder.getView(R.id.iv_user_avatar);
        ImageView iv_circle_pic = holder.getView(R.id.iv_circle_pic);
        ImageView iv_play = holder.getView(R.id.iv_play);
        ImageView iv_praise = holder.getView(R.id.iv_praise);
        PicassoUtil.displayImage(iv_avatar, itemData.getAvatar(), context);
        ImageView iv_sex = holder.getView(R.id.iv_sex);
        if (itemData.isLiked()) {
            iv_praise.setBackgroundResource(R.mipmap.social_icon_like);
        } else {
            iv_praise.setBackgroundResource(R.mipmap.social_icon_unlike);
        }
        switch (itemData.getSex()) {
            case 0:
                iv_sex.setBackgroundResource(R.mipmap.common_icon_gender_male);
                break;
            case 1:
                iv_sex.setBackgroundResource(R.mipmap.common_icon_gender_female);
                break;
        }
        double f1 = new BigDecimal((float) (itemData.getDistance()) / 1000).setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue();
        holder.setText(R.id.tv_km, f1 + "km");
        if (StringUtil.isNotBlank(itemData.getCircleName())) {
            holder.setText(R.id.tv_circle_name, itemData.getCircleName());
        }
        TextView tv_delete = holder.getView(R.id.tv_delete);
        String name = PrefName.getNickName(context);
        if (itemData.getName().equals(name)) {
            tv_delete.setVisibility(View.VISIBLE);
        } else {
            tv_delete.setVisibility(View.GONE);
        }
        Button btn_attention = holder.getView(R.id.btn_attention);
        if (itemData.isFollowed()) {
            btn_attention.setBackgroundResource(R.drawable.btn_attention_true);
            btn_attention.setText(R.string.attention_on);
            btn_attention.setTextColor(ContextCompat.getColor(context, R.color.sub_class_content_text_color));
        } else {
            btn_attention.setBackgroundResource(R.drawable.btn_attention_false);
            btn_attention.setText(R.string.attention);
            btn_attention.setTextColor(ContextCompat.getColor(context, R.color.red));
        }
        holder.setText(R.id.tv_praise_num, itemData.getLikeCount() + "");
        holder.setText(R.id.tv_comment_num, itemData.getCommentCount() + "");
        List<String> picList = itemData.getAlbum();
        Moment.Video video = itemData.getVideo();
        if (null != picList && !picList.isEmpty()) {
            iv_play.setVisibility(View.GONE);
            if (picList.size() > 1) {
                MinePicAdapter adapter = new MinePicAdapter(context);
                gv.setAdapter(adapter);
                List<Albums> list = new ArrayList<>();
                for (int i = 0; i < picList.size(); i++) {
                    Albums al = new Albums();
                    al.setUrlPre(picList.get(i));
                    list.add(al);
                }
                adapter.setAlbums(list);
                adapter.notifyDataSetChanged();
            } else {
                PicassoUtil.displayImage(iv_circle_pic, picList.get(0), context);
            }
        } else if (null != video) {
            PicassoUtil.displayImage(iv_circle_pic, video.getFramePic(), context);
            iv_play.setVisibility(View.VISIBLE);
            Uri uri = Uri.parse(video.getVideoUrl());
            //调用系统自带的播放器
            Intent intent = new Intent(Intent.ACTION_VIEW);
            intent.setDataAndType(uri, "video/mp4");
            context.startActivity(intent);
        } else {
            iv_play.setVisibility(View.GONE);
        }
//            MinePicAdapter adapter = new MinePicAdapter(context);
//            listview.setAdapter(adapter);
//            gv.setAdapter(adapter);
//            List<Albums> list = new ArrayList<>();
//            for (int i = 0; i < 5; i++) {
//                Albums al = new Albums();
//                list.add(al);
//            }
//            adapter.setAlbums(list);
//            adapter.notifyDataSetChanged();
//            adapter.setAlbums(list);
//            adapter.notifyDataSetChanged();
    }
}
