package com.king.run.activity.circle.adapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.v4.content.ContextCompat;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.king.run.R;
import com.king.run.activity.circle.model.Moment;
import com.king.run.activity.mine.adapter.MinePicAdapter;
import com.king.run.activity.mine.model.Albums;
import com.king.run.base.BaseActivity;
import com.king.run.base.ImagePagerActivity;
import com.king.run.model.BaseResult;
import com.king.run.util.CheckNetwork;
import com.king.run.util.PicassoUtil;
import com.king.run.util.PrefName;
import com.king.run.util.StringUtil;
import com.king.run.util.Url;
import com.king.run.util.Utils;
import com.lwkandroid.rcvadapter.RcvSingleAdapter;
import com.lwkandroid.rcvadapter.holder.RcvHolder;
import com.orhanobut.logger.Logger;

import org.xutils.common.Callback;
import org.xutils.common.util.DensityUtil;
import org.xutils.http.RequestParams;
import org.xutils.x;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

/**
 * 作者：水子wade on 2017/11/5 21:37
 * 邮箱：674618016@qq.com
 */
public class CircleInfoAdapter extends RcvSingleAdapter<Moment> {
    private Context context;
    private DeleteInterface deleteInterface;
    private boolean isFocus;
    private ImageView iv_praise;
    private Moment moment;
    private TextView tv_praise_num;
    private List<Moment> moments = new ArrayList<>();
    private ItemCommentClickInerface itemCommentClickInerface;
    private ItemAvatarClickInterface ItemAvatarClickInterface;

    public CircleInfoAdapter(Context context, List<Moment> datas) {
        super(context, R.layout.fragment_circle_list_row, datas);
        this.context = context;
        moments = datas;
    }

    @Override
    public void onBindView(RcvHolder holder, final Moment itemData, final int position) {
        moment = itemData;
        tv_praise_num = holder.findView(R.id.tv_praise_num);
        RelativeLayout ly_sex_age = holder.findView(R.id.ly_sex_age);
        RelativeLayout ly_share = holder.findView(R.id.ly_share);
        if (itemData.getExerciseTime() != 0) {
            ly_share.setVisibility(View.VISIBLE);
            holder.setTvText(R.id.tv_km_speed, "跑了" + itemData.getKilometer() + "km，配速：" + itemData.getPace());
            String timeUse;
            long time = itemData.getExerciseTime();
            if (time > 60 * 60 * 24) {
                int day = (int) (time / 60 * 60 * 24);
                int hour = (int) ((time % 60 * 60 * 24) / 60 * 60);
                int min = (int) ((time % 60 * 60 * 24) % (60 * 60)) / 60;
                int sec = (int) ((time % 60 * 60 * 24) % (60 * 60)) % 60;
                timeUse = day + "天" + hour + "小时" + min + "分钟" + sec + "秒";
            } else if (time > 60 * 60) {
                int hour = (int) ((time % 60 * 60 * 24) / 60 * 60);
                int min = (int) ((time % 60 * 60 * 24) % (60 * 60)) / 60;
                int sec = (int) ((time % 60 * 60 * 24) % (60 * 60)) % 60;
                timeUse = hour + "小时" + min + "分钟" + sec + "秒";
            } else if (time > 60) {
                int min = (int) ((time % 60 * 60 * 24) % (60 * 60)) / 60;
                int sec = (int) ((time % 60 * 60 * 24) % (60 * 60)) % 60;
                timeUse = min + "分钟" + sec + "秒";
            } else {
                timeUse = time + "秒";
            }
            holder.setTvText(R.id.tv_time_use, "用时：" + timeUse);
        } else
            ly_share.setVisibility(View.GONE);
        holder.setTvText(R.id.tv_user_name, itemData.getName());
        holder.setTvText(R.id.tv_content, itemData.getContent());
        holder.setTvText(R.id.tv_age, itemData.getAge());
        long time = System.currentTimeMillis();
        long between = (time - itemData.getDate()) / 1000;
        String showTime;
        int betweenDay = getTimeDistance(itemData.getDate(), time);
        if (betweenDay < 1) {
            if (between < 3600) {
                if (between / 60 < 1) {
                    showTime = "刚刚";
                } else
                    showTime = between / 60 + "分钟前";
            } else {
                showTime = between / 3600 + "小时前";
            }
        } else if (betweenDay > 30) {
            showTime = betweenDay / 30 + "月前";
        } else {
            showTime = betweenDay + "天前";
        }
//        TimeUtil.getSimpleFullDateStr(itemData.getDate())
        holder.setTvText(R.id.tv_time, showTime);
        ListView listview = holder.findView(R.id.lv);
        listview.setFocusableInTouchMode(false);
        ImageView iv_avatar = holder.findView(R.id.iv_user_avatar);
        iv_avatar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ItemAvatarClickInterface.onAvatarClick(position);
            }
        });
        ImageView iv_circle_pic = holder.findView(R.id.iv_circle_pic);
        final GridView gv = holder.findView(R.id.gv_pic);
        gv.setFocusableInTouchMode(false);
        RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) iv_circle_pic.getLayoutParams();
        int width = Utils.getDisplayWidth((Activity) context);
        params.width = (int) (width * 0.311);
        params.height = (int) (width * 0.311 * 1.786);
        RelativeLayout ly_img = holder.findView(R.id.ly_img);
        RelativeLayout.LayoutParams layoutParams = (RelativeLayout.LayoutParams) iv_circle_pic.getLayoutParams();
        layoutParams.width = (int) (width * 0.311);
        layoutParams.height = (int) (width * 0.311 * 1.786);
        RelativeLayout.LayoutParams gvParams = (RelativeLayout.LayoutParams) gv.getLayoutParams();
        gvParams.width = (int) (width * 0.666 + DensityUtil.dip2px(12));
        gv.setLayoutParams(gvParams);
        ly_img.setLayoutParams(layoutParams);
        iv_circle_pic.setLayoutParams(params);
        ImageView iv_play = holder.findView(R.id.iv_play);
        iv_praise = holder.findView(R.id.iv_praise);
        PicassoUtil.displayImage(iv_avatar, itemData.getAvatar(), context);
        ImageView iv_sex = holder.findView(R.id.iv_sex);
        if (itemData.isLiked()) {
            iv_praise.setImageResource(R.mipmap.social_icon_like);
        } else {
            iv_praise.setImageResource(R.mipmap.social_icon_unlike);
        }
        switch (itemData.getSex()) {
            case 0:
                iv_sex.setBackgroundResource(R.mipmap.common_icon_gender_male);
                ly_sex_age.setBackgroundResource(R.drawable.circle_pink_bg);
                break;
            case 1:
                iv_sex.setBackgroundResource(R.mipmap.common_icon_gender_female);
                ly_sex_age.setBackgroundResource(R.drawable.circle_blue_bg);
                break;
        }
        DecimalFormat df = new DecimalFormat("0.00");
        String result = df.format((float) itemData.getDistance() / 1000);
//        double f1 = new BigDecimal((float) (itemData.getDistance()) / 1000).setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue();
        holder.setTvText(R.id.tv_km, result + "km");
        if (StringUtil.isNotBlank(itemData.getCircleName())) {
            holder.setTvText(R.id.tv_circle_name, itemData.getCircleName());
        }
        TextView tv_delete = holder.findView(R.id.tv_delete);
        String name = PrefName.getNickName(context);
        final Button btn_attention = holder.findView(R.id.btn_attention);
        //是否是热门和最新
        if (isFocus) {
            btn_attention.setVisibility(View.GONE);
        } else {
            //自己发布的状态显示和隐藏按钮
            if (itemData.isCanDeleted()) {
                btn_attention.setVisibility(View.GONE);
            } else {
                btn_attention.setVisibility(View.VISIBLE);
                if (itemData.isFollowed()) {
                    btn_attention.setVisibility(View.GONE);
                    btn_attention.setBackgroundResource(R.drawable.btn_attention_true);
                    btn_attention.setText(R.string.attention_on);
                    btn_attention.setTextColor(ContextCompat.getColor(context, R.color.sub_class_content_text_color));
                } else {
                    btn_attention.setVisibility(View.VISIBLE);
                    btn_attention.setBackgroundResource(R.drawable.btn_attention_false);
                    btn_attention.setText(R.string.attention);
                    btn_attention.setTextColor(ContextCompat.getColor(context, R.color.red));
                }
            }
        }
        if (itemData.isCanDeleted()) {
            tv_delete.setVisibility(View.VISIBLE);
            tv_delete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    deleteInterface.delete(itemData, position);
                }
            });
        } else {
            tv_delete.setVisibility(View.GONE);
        }
        holder.findView(R.id.ly_comment_num).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                itemCommentClickInerface.onItemCommentClick(itemData, position);
            }
        });
        btn_attention.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                RequestParams params = new RequestParams(Url.ATTENTION_USER_URL + itemData.getUserId());
                if (CheckNetwork.isNetworkAvailable((Activity) context)) {
                    ((BaseActivity) context).showDia("正在加载中...");
                    com.orhanobut.logger.Logger.d(params);
                    x.http().post(params, new Callback.CommonCallback<String>() {
                        @Override
                        public void onSuccess(String s) {
                            BaseResult result = JSON.parseObject(s, BaseResult.class);
                            if (result.getCode() == 0) {
                                if (itemData.isFollowed()) {
                                    btn_attention.setBackgroundResource(R.drawable.btn_attention_false);
                                    btn_attention.setText(R.string.attention);
                                    btn_attention.setTextColor(ContextCompat.getColor(context, R.color.red));
                                } else {
                                    btn_attention.setVisibility(View.GONE);
                                    btn_attention.setBackgroundResource(R.drawable.btn_attention_true);
                                    btn_attention.setText(R.string.attention_on);
                                    btn_attention.setTextColor(ContextCompat.getColor(context, R.color.sub_class_content_text_color));
                                }
                            } else {
                                ((BaseActivity) context).senToa(result.getMsg());
                            }
                            ((BaseActivity) context).hideDia();
                            Logger.d(s);
                        }

                        @Override
                        public void onError(Throwable ex, boolean isOnCallback) {
                            ((BaseActivity) context).hideDia();
                            ((BaseActivity) context).senToa(ex.getLocalizedMessage());
                        }

                        @Override
                        public void onCancelled(CancelledException cex) {
                            ((BaseActivity) context).hideDia();
                        }

                        @Override
                        public void onFinished() {
                            ((BaseActivity) context).hideDia();
                        }
                    });
                } else {
                    ((BaseActivity) context).senToa("网络异常");
                }
            }
        });

        holder.setTvText(R.id.tv_praise_num, itemData.getLikeCount() + "");
        holder.setClickListener(R.id.ly_praise, new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ((BaseActivity) context).showDia("正在加载中...");
//                SportIml.getInstance((Activity) context).praise(CircleInfoAdapter.this, itemData.getId());
                RequestParams params = new RequestParams(Url.PRAISE_URL + itemData.getId());
                params.addHeader("token", PrefName.getToken(context));
                if (CheckNetwork.isNetworkAvailable((Activity) context)) {
                    ((BaseActivity) context).showDia("正在加载中...");
                    com.orhanobut.logger.Logger.d(params);
                    x.http().post(params, new Callback.CommonCallback<String>() {
                        @Override
                        public void onSuccess(String s) {
                            BaseResult result = JSON.parseObject(s, BaseResult.class);
                            if (result.getCode() == 0) {
                                if (itemData.isLiked()) {
//                                    iv_praise.setBackgroundResource(R.mipmap.social_icon_unlike);
                                    itemData.setLiked(false);
                                    int num = itemData.getLikeCount() - 1;
                                    itemData.setLikeCount(num);
//                                    tv_praise_num.setText(num + "");
                                } else {
                                    itemData.setLiked(true);
                                    int num = itemData.getLikeCount() + 1;
//                                    iv_praise.setBackgroundResource(R.mipmap.social_icon_like);
//                                    tv_praise_num.setText(num + "");
                                    itemData.setLikeCount(num);
                                }
                                notifyDataSetChanged();
                            } else {
                                ((BaseActivity) context).senToa(result.getMsg());
                            }
                            ((BaseActivity) context).hideDia();
                            Logger.d(s);
                        }

                        @Override
                        public void onError(Throwable ex, boolean isOnCallback) {
                            ((BaseActivity) context).hideDia();
                            ((BaseActivity) context).senToa(ex.getLocalizedMessage());
                        }

                        @Override
                        public void onCancelled(CancelledException cex) {
                            ((BaseActivity) context).hideDia();
                        }

                        @Override
                        public void onFinished() {
                            ((BaseActivity) context).hideDia();
                        }
                    });
                } else {
                    ((BaseActivity) context).senToa("网络异常");
                }

            }
        });
        holder.setTvText(R.id.tv_comment_num, itemData.getCommentCount() + "");
        List<String> picList = itemData.getAlbum();
        final Moment.Video video = itemData.getVideo();
        if (null != picList && !picList.isEmpty()) {
            iv_play.setVisibility(View.GONE);
            if (picList.size() > 1) {
                holder.findView(R.id.ly_img).setVisibility(View.GONE);
                gv.setVisibility(View.VISIBLE);
                iv_circle_pic.setVisibility(View.GONE);
                GvCirclePicAdapter adapter = new GvCirclePicAdapter(context);
                gv.setAdapter(adapter);
                gv.setTag(position);
                List<Albums> list = new ArrayList<>();
                for (int i = 0; i < picList.size(); i++) {
                    Albums al = new Albums();
                    al.setUrlPre(picList.get(i));
                    list.add(al);
                }
                adapter.setAlbums(list);
                adapter.notifyDataSetChanged();
            } else {
                gv.setVisibility(View.GONE);
                holder.findView(R.id.ly_img).setVisibility(View.VISIBLE);
                iv_circle_pic.setVisibility(View.VISIBLE);
                PicassoUtil.displayImage(iv_circle_pic, picList.get(0), context);
                iv_circle_pic.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        ArrayList<String> list = (ArrayList<String>) itemData.getAlbum();
                        ImagePagerActivity.showPics((Activity) context, list, null, 0);
                    }
                });
            }
        } else if (null != video) {
            holder.findView(R.id.ly_img).setVisibility(View.VISIBLE);
            holder.findView(R.id.ly_img).setVisibility(View.VISIBLE);
            gv.setVisibility(View.GONE);
            iv_circle_pic.setVisibility(View.VISIBLE);
            PicassoUtil.displayImage(iv_circle_pic, video.getFramePic(), context);
            iv_play.setVisibility(View.VISIBLE);
            iv_play.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Uri uri = Uri.parse(video.getVideoUrl());
                    //调用系统自带的播放器
                    Intent intent = new Intent(Intent.ACTION_VIEW);
                    intent.setDataAndType(uri, "video/mp4");
                    context.startActivity(intent);
                }
            });
            iv_circle_pic.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (StringUtil.isNotBlank(video.getVideoUrl())) {
                        Uri uri = Uri.parse(video.getVideoUrl());
                        //调用系统自带的播放器
                        Intent intent = new Intent(Intent.ACTION_VIEW);
                        intent.setDataAndType(uri, "video/mp4");
                        context.startActivity(intent);
                    } else {
                        ArrayList<String> list = (ArrayList<String>) itemData.getAlbum();
                        ImagePagerActivity.showPics((Activity) context, list, null, 0);
                    }
                }
            });
        } else {
            gv.setVisibility(View.GONE);
            holder.findView(R.id.ly_img).setVisibility(View.GONE);
            iv_play.setVisibility(View.GONE);
            iv_circle_pic.setVisibility(View.GONE);
        }

        gv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                ArrayList<String> list = (ArrayList<String>) itemData.getAlbum();
                ImagePagerActivity.showPics((Activity) context, list, null, i);
            }
        });
//        if (null != itemData.getComments() && !itemData.getComments().isEmpty()) {
//            listview.setVisibility(View.VISIBLE);
//            holder.findView(R.id.lv_comment_bottom_view).setVisibility(View.VISIBLE);
//            holder.findView(R.id.lv_comment_top_view).setVisibility(View.VISIBLE);
//            CommentAdapter commentAdapter = new CommentAdapter(context, itemData.getComments());
//            listview.setAdapter(commentAdapter);
//        } else {
//            listview.setVisibility(View.GONE);
//            holder.findView(R.id.lv_comment_bottom_view).setVisibility(View.GONE);
//            holder.findView(R.id.lv_comment_top_view).setVisibility(View.GONE);
//        }
        holder.findView(R.id.lv_comment_bottom_view).setVisibility(View.GONE);
        holder.findView(R.id.lv_comment_top_view).setVisibility(View.GONE);
        listview.setVisibility(View.GONE);
    }

    public void setDel(DeleteInterface deleteInterface) {
        this.deleteInterface = deleteInterface;
    }

    public void setFocus(boolean focus) {
        isFocus = focus;
    }

    public interface DeleteInterface {
        void delete(Moment circleInfo, int position);
    }

    public interface ItemCommentClickInerface {
        void onItemCommentClick(Moment moment, int position);
    }

    public void setOnItemCommentClickListener(ItemCommentClickInerface itemCommentClickListener) {
        this.itemCommentClickInerface = itemCommentClickListener;
    }

    public interface ItemAvatarClickInterface {
        void onAvatarClick(int position);
    }

    public void setOnItemAvatarClickListener(ItemAvatarClickInterface itemAvatarClickListener) {
        this.ItemAvatarClickInterface = itemAvatarClickListener;
    }

    public static int getTimeDistance(long beginDate, long endDate) {
        Calendar beginCalendar = Calendar.getInstance();
        beginCalendar.setTimeInMillis(beginDate);
        Calendar endCalendar = Calendar.getInstance();
        endCalendar.setTimeInMillis(endDate);
        long beginTime = beginCalendar.getTime().getTime();
        long endTime = endCalendar.getTime().getTime();
        int betweenDays = (int) ((endTime - beginTime) / (1000 * 60 * 60 * 24));//先算出两时间的毫秒数之差大于一天的天数

        endCalendar.add(Calendar.DAY_OF_MONTH, -betweenDays);//使endCalendar减去这些天数，将问题转换为两时间的毫秒数之差不足一天的情况
        endCalendar.add(Calendar.DAY_OF_MONTH, -1);//再使endCalendar减去1天
        if (beginCalendar.get(Calendar.DAY_OF_MONTH) == endCalendar.get(Calendar.DAY_OF_MONTH))//比较两日期的DAY_OF_MONTH是否相等
            return betweenDays + 1; //相等说明确实跨天了
        else
            return betweenDays; //不相等说明确实未跨天
    }

    public static void main(String args[]) {
        long start = Long.parseLong("1514826061000");
        long end = Long.parseLong("1515769501000");
        System.out.println(getTimeDistance(start, end));
    }
}
