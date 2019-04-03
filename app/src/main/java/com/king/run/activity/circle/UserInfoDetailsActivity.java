package com.king.run.activity.circle;

import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.king.run.R;
import com.king.run.activity.circle.adapter.CircleInfoAdapter;
import com.king.run.activity.circle.model.Moment;
import com.king.run.activity.circle.model.PageResult;
import com.king.run.activity.circle.model.UserInfoDetailResult;
import com.king.run.activity.circle.model.UserInfoOther;
import com.king.run.activity.mine.adapter.MinePicAdapter;
import com.king.run.activity.mine.model.Albums;
import com.king.run.base.BaseActivity;
import com.king.run.util.PicassoUtil;
import com.king.run.util.PrefName;
import com.king.run.util.Url;
import com.king.run.view.CircleImageView;
import com.king.run.view.HorizontalListView;
import com.lwkandroid.rcvadapter.listener.RcvLoadMoreListener;

import org.xutils.http.RequestParams;
import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.ViewInject;

import java.util.ArrayList;
import java.util.List;

@ContentView(R.layout.activity_user_info_details)
public class UserInfoDetailsActivity extends BaseActivity {
    @ViewInject(R.id.swipRefresh_circle)
    SwipeRefreshLayout swipRefresh_circle;
    @ViewInject(R.id.recyclerView_circle)
    RecyclerView recyclerView_circle;
    private CircleInfoAdapter mAdapter;
    private List<Moment> moments = new ArrayList<>();
    private View headerView;
    private int page = 2;
    private String userId;
    private Button btn_attention;
    private CircleImageView iv_avatar;
    private TextView tv_attention_num, tv_fans_num, tv_collect_num, tv_name_pic, tv_user_name;
    private HorizontalListView hor_lv;
    private MinePicAdapter adapter;
    private UserInfoOther userInfoOther;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        init();
        initHeaderView();
        initAdapter();
        getListInfo();
    }

    private void initHeaderView() {
        userId = getIntent().getStringExtra("userId");
        headerView = LayoutInflater.from(context).inflate(R.layout.header_user_info_detail, null);
        btn_attention = headerView.findViewById(R.id.btn_attention);
        iv_avatar = headerView.findViewById(R.id.iv_avatar);
        tv_attention_num = headerView.findViewById(R.id.tv_attention_num);
        tv_fans_num = headerView.findViewById(R.id.tv_fans_num);
        tv_collect_num = headerView.findViewById(R.id.tv_collect_num);
        tv_name_pic = headerView.findViewById(R.id.tv_name_pic);
        tv_user_name = headerView.findViewById(R.id.tv_user_name);
        hor_lv = headerView.findViewById(R.id.hor_lv);
        adapter = new MinePicAdapter(context);
        hor_lv.setAdapter(adapter);
        btn_attention.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                RequestParams params = new RequestParams(Url.ATTENTION_USER_URL + userId);
                httpPost("attention", params);
            }
        });
    }

    private void initAdapter() {
        LinearLayoutManager layoutManager = new LinearLayoutManager(context);
        mAdapter = new CircleInfoAdapter(context, moments);
        recyclerView_circle.setLayoutManager(layoutManager);
        mAdapter.addHeaderView(headerView);
        mAdapter.enableLoadMore(false);
        mAdapter.enableItemShowingAnim(true);
        recyclerView_circle.setAdapter(mAdapter);
        swipRefresh_circle.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                getListInfo();
                page = 2;
            }
        });
    }

    private void getListInfo() {
        RequestParams params = new RequestParams(Url.USER_INFO_DETAIL_URL);
        params.addBodyParameter("lng", PrefName.getPrefLastLng(context));
        params.addBodyParameter("lat", PrefName.getPrefLastLat(context));
        params.addBodyParameter("userId", userId);
        httpGet(params, "list");
        page = 2;
    }

    private void loadMoreDatas() {
        RequestParams params = new RequestParams(Url.PAGE_CIRCLE_URL);
        params.addBodyParameter("lng", PrefName.getPrefLastLng(context));
        params.addBodyParameter("lat", PrefName.getPrefLastLat(context));
        params.addBodyParameter("page", page + "");
        params.addBodyParameter("typeId", userId);
        params.addBodyParameter("type", 2 + "");
        httpGet(params, "page");
        page++;
    }

    @Override
    public void success(String result, String type) {
        super.success(result, type);
        switch (type) {
            case "list":
                UserInfoDetailResult userInfoDetailResult = JSON.parseObject(result, UserInfoDetailResult.class);
                moments = userInfoDetailResult.getData().getMoment();
                mAdapter.addDatas(moments);
                mAdapter.notifyDataSetChanged();
                mAdapter.enableLoadMore(true, new RcvLoadMoreListener() {
                    @Override
                    public void onLoadMoreRequest() {
                        loadMoreDatas();
                    }
                });
                userInfoOther = userInfoDetailResult.getData().getUserInfo();
                List<Albums> albums = userInfoOther.getAlbums();
                if (null != albums && !albums.isEmpty()) {
                    adapter.setAlbums(albums);
                    adapter.notifyDataSetChanged();
                } else {
                    hor_lv.setVisibility(View.GONE);
                }
                tv_user_name.setText(userInfoOther.getNickName());
                tv_attention_num.setText(userInfoOther.getFollow() + "");
                tv_fans_num.setText(userInfoOther.getFollower() + "");
                tv_collect_num.setText(userInfoOther.getCollect() + "");
                tv_name_pic.setText("我的相册(" + albums.size() + ")");
                if (userInfoOther.getNickName().equals(PrefName.getNickName(context))) {
                    btn_attention.setVisibility(View.GONE);
                } else if (userInfoOther.isFollowed()) {
                    btn_attention.setBackgroundResource(R.drawable.btn_attention_true);
                    btn_attention.setText(R.string.attention_on);
                    btn_attention.setTextColor(ContextCompat.getColor(context, R.color.sub_class_content_text_color));
                } else {
                    btn_attention.setBackgroundResource(R.drawable.btn_attention_false);
                    btn_attention.setText(R.string.attention);
                    btn_attention.setTextColor(ContextCompat.getColor(context, R.color.red));
                }
                PicassoUtil.displayImage(iv_avatar, userInfoOther.getAvator(), context);
                swipRefresh_circle.setRefreshing(false);
                break;
            case "page":
                PageResult pageResult = JSON.parseObject(result, PageResult.class);
                if (null != pageResult.getData() && !pageResult.getData().isEmpty()) {
                    moments.addAll(pageResult.getData());
                    if (pageResult.getData().size() < 10) {
                        mAdapter.notifyLoadMoreSuccess(pageResult.getData(), false);
                        page = 2;
                    } else
                        mAdapter.notifyLoadMoreSuccess(pageResult.getData(), true);
                } else {
                    mAdapter.notifyLoadMoreSuccess(new ArrayList<Moment>(), false);
                    page = 2;
                }
                break;
            case "attention":
                if (userInfoOther.isFollowed()) {
                    btn_attention.setBackgroundResource(R.drawable.btn_attention_false);
                    btn_attention.setText(R.string.attention);
                    btn_attention.setTextColor(ContextCompat.getColor(context, R.color.red));
                } else {
                    btn_attention.setBackgroundResource(R.drawable.btn_attention_true);
                    btn_attention.setText(R.string.attention_on);
                    btn_attention.setTextColor(ContextCompat.getColor(context, R.color.sub_class_content_text_color));
                }
                break;
        }
    }
}
