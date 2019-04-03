package com.king.run.activity.circle;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.view.KeyEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.king.run.R;
import com.king.run.activity.circle.adapter.CommentAdapter;
import com.king.run.activity.circle.adapter.GvCirclePicAdapter;
import com.king.run.activity.circle.adapter.PraiseAdapter;
import com.king.run.activity.circle.model.Comment;
import com.king.run.activity.circle.model.CommentResult;
import com.king.run.activity.circle.model.LikeUser;
import com.king.run.activity.circle.model.Moment;
import com.king.run.activity.circle.model.StateDetails;
import com.king.run.activity.mine.adapter.MinePicAdapter;
import com.king.run.activity.mine.model.Albums;
import com.king.run.base.BaseActivity;
import com.king.run.base.ImagePagerActivity;
import com.king.run.model.BaseResult;
import com.king.run.util.PicassoUtil;
import com.king.run.util.PrefName;
import com.king.run.util.StringUtil;
import com.king.run.util.TimeUtil;
import com.king.run.util.Url;
import com.king.run.util.Utils;
import com.king.run.view.CircleImageView;
import com.king.run.view.GridViewForScrollView;
import com.king.run.view.ListViewForScrollView;

import org.xutils.common.util.DensityUtil;
import org.xutils.http.RequestParams;
import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.Event;
import org.xutils.view.annotation.ViewInject;

import java.io.Serializable;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@ContentView(R.layout.activity_info_details)
public class InfoDetailsActivity extends BaseActivity {
    private Moment moment;
    @ViewInject(R.id.iv_user_avatar)
    CircleImageView iv_user_avatar;
    @ViewInject(R.id.tv_user_name)
    TextView tv_user_name;
    @ViewInject(R.id.iv_sex)
    ImageView iv_sex;
    @ViewInject(R.id.gv_praise)
    GridViewForScrollView gv_praise;
    @ViewInject(R.id.tv_sex)
    TextView tv_sex;
    @ViewInject(R.id.btn_attention)
    Button btn_attention;
    @ViewInject(R.id.tv_content)
    TextView tv_content;
    @ViewInject(R.id.iv_circle_pic)
    ImageView iv_circle_pic;
    @ViewInject(R.id.gv_pic)
    GridViewForScrollView gv_pic;
    @ViewInject(R.id.iv_play)
    ImageView iv_play;
    @ViewInject(R.id.tv_km)
    TextView tv_km;
    @ViewInject(R.id.tv_time)
    TextView tv_time;
    @ViewInject(R.id.tv_delete)
    TextView tv_delete;
    @ViewInject(R.id.tv_circle_name)
    TextView tv_circle_name;
    @ViewInject(R.id.iv_praise)
    ImageView iv_praise;
    @ViewInject(R.id.tv_praise_num)
    TextView tv_praise_num;
    @ViewInject(R.id.tv_praise_num_name)
    TextView tv_praise_num_name;
    @ViewInject(R.id.tv_comment_num)
    TextView tv_comment_num;
    @ViewInject(R.id.tv_comment_num_name)
    TextView tv_comment_num_name;
    @ViewInject(R.id.lv)
    ListViewForScrollView lv;
    private CommentAdapter commentAdapter;
    @ViewInject(R.id.et_comment)
    EditText et_comment;
    private int commentClickPosition = -1;
    @ViewInject(R.id.ly_comment)
    LinearLayout ly_comment;
    @ViewInject(R.id.title_title)
    TextView title_tv_title;
    private boolean hasComment = false;
    private PraiseAdapter praiseAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        initTitleBar();
        initViews();
        getInfo();
    }

    private void getInfo() {
        String url = Url.STATE_DETAIL_URL + "?momentId=" + moment.getId() + "&lat=" + PrefName.getPrefLastLat(context) + "&lng=" + PrefName.getPrefLastLat(context);
        RequestParams params = new RequestParams(url);
        httpGet(params, "info");
    }

    private void initViews() {
        praiseAdapter = new PraiseAdapter(context);
        gv_praise.setAdapter(praiseAdapter);
        gv_praise.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
//                if (moment.getLikeUser().size() > 7 && i == 6) {
                    Bundle bundle = new Bundle();
                    bundle.putSerializable("list", (Serializable) moment.getLikeUser());
                    jumpBundleActvity(AllPraiseActivity.class, bundle);
//                }
            }
        });
        moment = (Moment) getIntent().getExtras().getSerializable("moment");
        title_tv_title.setText(R.string.details);
        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, final int position, long l) {
                commentClickPosition = position;
                if (moment.getComments().get(position).isCanDeleted()) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(context);
                    builder.setTitle(R.string.hint).setMessage(R.string.sure_to_delete).setPositiveButton(R.string.sure, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            int id = moment.getComments().get(position).getId();
                            RequestParams params = new RequestParams(Url.DELETE_COMMENT_URL + id);
                            httpPost("deleteComment", params);
                        }
                    }).setNegativeButton(R.string.cancel, null).create().show();
                } else {
                    ly_comment.setVisibility(View.VISIBLE);
                    et_comment.requestFocus();
                    et_comment.setFocusable(true);
                    Utils.showKeyboard(et_comment, context);
                    et_comment.setHint("回复:" + moment.getComments().get(position).getReviewerName());
                }
            }
        });
    }

    private void setInfo() {
        List<LikeUser> likeUsers = moment.getLikeUser();
        if (moment.getLikeUser().size() > 7)
            likeUsers = moment.getLikeUser().subList(0, 7);
        praiseAdapter.setAlbums(likeUsers);
        praiseAdapter.notifyDataSetChanged();
        tv_comment_num_name.setText(moment.getCommentCount() + "条评论");
        tv_praise_num_name.setText(moment.getLikeCount() + "赞");
        tv_user_name.setText(moment.getName());
        tv_content.setText(moment.getContent());
        tv_time.setText(TimeUtil.getSimpleFullDateStr(moment.getDate()));
        long time = System.currentTimeMillis();
        long between = (time - moment.getDate()) / 1000;
        String showTime;
        int betweenDay = Utils.getTimeDistance(moment.getDate(), time);
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
        tv_time.setText(showTime);
        RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) iv_circle_pic.getLayoutParams();
        int width = Utils.getDisplayWidth((Activity) context);
        params.width = (int) (width * 0.311);
        params.height = (int) (width * 0.311 * 1.786);
        RelativeLayout ly_img = (RelativeLayout) findViewById(R.id.ly_img);
        RelativeLayout.LayoutParams layoutParams = (RelativeLayout.LayoutParams) iv_circle_pic.getLayoutParams();
        layoutParams.width = (int) (width * 0.311);
        layoutParams.height = (int) (width * 0.311 * 1.786);
        ly_img.setLayoutParams(layoutParams);
        iv_circle_pic.setLayoutParams(params);
        PicassoUtil.displayImage(iv_user_avatar, moment.getAvatar(), context);
        if (moment.isLiked()) {
            iv_praise.setImageResource(R.mipmap.social_icon_like);
        } else {
            iv_praise.setImageResource(R.mipmap.social_icon_unlike);
        }
        switch (moment.getSex()) {
            case 0:
                iv_sex.setBackgroundResource(R.mipmap.common_icon_gender_male);
                break;
            case 1:
                iv_sex.setBackgroundResource(R.mipmap.common_icon_gender_female);
                break;
        }
        DecimalFormat df = new DecimalFormat("0.00");
        String result = df.format((float) moment.getDistance() / 1000);
        tv_km.setText(result + "km");
        if (StringUtil.isNotBlank(moment.getCircleName())) {
            tv_circle_name.setText(moment.getCircleName());
        }
        //自己发布的状态显示和隐藏按钮
        if (moment.isFollowed()) {
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
        if (moment.isCanDeleted()) {
            tv_delete.setVisibility(View.VISIBLE);
        } else {
            tv_delete.setVisibility(View.GONE);
        }
        tv_praise_num.setText(moment.getLikeCount() + "");
        tv_comment_num.setText(moment.getCommentCount() + "");
        List<String> picList = moment.getAlbum();
        RelativeLayout.LayoutParams gvParams = (RelativeLayout.LayoutParams) gv_pic.getLayoutParams();
        gvParams.width = (int) (width * 0.666 + DensityUtil.dip2px(12));
        gv_pic.setLayoutParams(gvParams);
        final Moment.Video video = moment.getVideo();
        if (null != picList && !picList.isEmpty()) {
            iv_play.setVisibility(View.GONE);
            if (picList.size() > 1) {
                gv_pic.setVisibility(View.VISIBLE);
                findViewById(R.id.ly_img).setVisibility(View.GONE);
                iv_circle_pic.setVisibility(View.GONE);
                GvCirclePicAdapter adapter = new GvCirclePicAdapter(context);
                gv_pic.setAdapter(adapter);
                List<Albums> list = new ArrayList<>();
                for (int i = 0; i < picList.size(); i++) {
                    Albums al = new Albums();
                    al.setUrlPre(picList.get(i));
                    list.add(al);
                }
                adapter.setAlbums(list);
                adapter.notifyDataSetChanged();
            } else {
                findViewById(R.id.ly_img).setVisibility(View.VISIBLE);
                iv_circle_pic.setVisibility(View.VISIBLE);
                PicassoUtil.displayImage(iv_circle_pic, picList.get(0), context);
                iv_circle_pic.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        ArrayList<String> list = (ArrayList<String>) moment.getAlbum();
                        ImagePagerActivity.showPics((Activity) context, list, null, 0);
                    }
                });
            }
        } else if (null != video) {
            findViewById(R.id.ly_img).setVisibility(View.VISIBLE);
            findViewById(R.id.ly_img).setVisibility(View.VISIBLE);
            gv_pic.setVisibility(View.GONE);
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
                    Uri uri = Uri.parse(video.getVideoUrl());
                    //调用系统自带的播放器
                    Intent intent = new Intent(Intent.ACTION_VIEW);
                    intent.setDataAndType(uri, "video/mp4");
                    context.startActivity(intent);
                }
            });
        } else {
            iv_play.setVisibility(View.GONE);
        }
        gv_pic.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                ArrayList<String> list = (ArrayList<String>) moment.getAlbum();
                ImagePagerActivity.showPics((Activity) context, list, null, i);
            }
        });
//        if (null != moment.getComments() && !moment.getComments().isEmpty()) {
//            findViewById(R.id.lv_comment_bottom_view).setVisibility(View.VISIBLE);
//            findViewById(R.id.lv_comment_top_view).setVisibility(View.VISIBLE);
//            commentAdapter = new CommentAdapter(context, moment.getComments());
//            lv.setAdapter(commentAdapter);
//        } else {
        findViewById(R.id.lv_comment_bottom_view).setVisibility(View.GONE);
        findViewById(R.id.lv_comment_top_view).setVisibility(View.GONE);
//        }
        commentAdapter = new CommentAdapter(context, moment.getComments());
        lv.setAdapter(commentAdapter);
        tv_sex.setText(moment.getAge());
        title_tv_title.requestFocus();
        title_tv_title.setFocusableInTouchMode(true);
    }

    @Event(value = {R.id.ly_comment_num, R.id.ly_praise, R.id.tv_delete, R.id.btn_attention, R.id.btn_comment
            , R.id.ly_iv_back})
    private void getEvent(View view) {
        RequestParams params;
        switch (view.getId()) {
            case R.id.ly_comment_num:
                ly_comment.setVisibility(View.VISIBLE);
                et_comment.requestFocus();
                et_comment.setFocusable(true);
                Utils.showKeyboard(et_comment, context);
                et_comment.setHint(R.string.pub_comment);
                break;
            case R.id.ly_praise:
                params = new RequestParams(Url.PRAISE_URL + moment.getId());
                params.addHeader("token", PrefName.getToken(context));
                httpPost("praise", params);
                break;
            case R.id.tv_delete:
                AlertDialog.Builder builder = new AlertDialog.Builder(context);
                builder.setTitle(R.string.hint).setMessage(R.string.sure_to_delete).setPositiveButton(R.string.sure, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        RequestParams params = new RequestParams(Url.DELETE_MOMENT_URL + moment.getId());
                        httpPost("delete", params);
                    }
                }).setNegativeButton(R.string.cancel, null).create().show();
                break;
            case R.id.btn_attention:
                params = new RequestParams(Url.ATTENTION_USER_URL + moment.getUserId());
                httpPost("attention", params);
                break;
            case R.id.btn_comment:
                String content = et_comment.getText().toString().trim();
                if (StringUtil.isBlank(content)) {
                    et_comment.setError("请输入回复内容");
                    return;
                }
                params = new RequestParams(Url.COMMENT_URL);
                Map<String, Object> map = new HashMap<>();
                map.put("id", moment.getId());
                map.put("content", content);
                map.put("userId", moment.getUserId());
                if (commentClickPosition != -1)
                    map.put("toUserId", moment.getComments().get(commentClickPosition).getReviewer());
                httpPostJSON("comment", params, map);
                break;
            case R.id.ly_iv_back:
                if (hasComment)
                    setResultAct(null);
                finish();
                break;
        }
    }

    @Override
    public void success(String result, String type) {
        super.success(result, type);
        switch (type) {
            case "info":
                StateDetails stateDetails = JSON.parseObject(result, StateDetails.class);
                moment = stateDetails.getData();
                setInfo();
                break;
            case "delete":
                finish();
                setResult(RESULT_OK);
                break;
            case "attention":
                BaseResult baseResult = JSON.parseObject(result, BaseResult.class);
                if (baseResult.getCode() == 0) {
                    if (moment.isFollowed()) {
                        btn_attention.setVisibility(View.VISIBLE);
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
                    senToa(baseResult.getMsg());
                }
                break;
            case "praise":
                hasComment = true;
                BaseResult baseResult1 = JSON.parseObject(result, BaseResult.class);
                if (baseResult1.getCode() == 0) {
                    if (moment.isLiked()) {
                        iv_praise.setBackgroundResource(R.mipmap.social_icon_unlike);
                        int num = moment.getLikeCount() - 1;
                        tv_praise_num.setText(num + "");
                    } else {
                        int num = moment.getLikeCount() + 1;
                        iv_praise.setBackgroundResource(R.mipmap.social_icon_like);
                        tv_praise_num.setText(num + "");
                    }
                } else {
                    senToa(baseResult1.getMsg());
                }
                break;
            case "comment":
                hasComment = true;
                CommentResult commentResult = JSON.parseObject(result, CommentResult.class);
                List<Comment> list = moment.getComments();
                list.add(commentResult.getData());
                commentAdapter.setComments(list);
                commentAdapter.notifyDataSetChanged();
                ly_comment.setVisibility(View.GONE);
                findViewById(R.id.lv_comment_bottom_view).setVisibility(View.VISIBLE);
                findViewById(R.id.lv_comment_top_view).setVisibility(View.VISIBLE);
                Utils.hideInput(context, et_comment);
                break;
            case "deleteComment":
                moment.getComments().remove(commentClickPosition);
                if (!(null != moment.getComments() && !moment.getComments().isEmpty())) {
                    findViewById(R.id.lv_comment_bottom_view).setVisibility(View.GONE);
                    findViewById(R.id.lv_comment_top_view).setVisibility(View.GONE);
                }
                commentAdapter.setComments(moment.getComments());
                commentAdapter.notifyDataSetChanged();
                break;
        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        super.onKeyDown(keyCode, event);
        if (hasComment)
            setResultAct(null);
        return true;
    }
}
