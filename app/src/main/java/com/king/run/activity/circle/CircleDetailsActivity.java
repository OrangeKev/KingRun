package com.king.run.activity.circle;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.CenterCrop;
import com.king.run.R;
import com.king.run.activity.circle.adapter.CircleInfoAdapter;
import com.king.run.activity.circle.model.CircleDetail;
import com.king.run.activity.circle.model.CircleDetailResult;
import com.king.run.activity.circle.model.Moment;
import com.king.run.activity.circle.model.PageResult;
import com.king.run.base.BaseActivity;
import com.king.run.util.PicassoUtil;
import com.king.run.util.PrefName;
import com.king.run.util.Url;
import com.king.run.util.Utils;
import com.lwkandroid.rcvadapter.holder.RcvHolder;
import com.lwkandroid.rcvadapter.listener.RcvItemViewClickListener;
import com.lwkandroid.rcvadapter.listener.RcvLoadMoreListener;
import com.orhanobut.logger.Logger;

import org.xutils.common.util.DensityUtil;
import org.xutils.http.RequestParams;
import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.ViewInject;

import java.util.ArrayList;
import java.util.List;

import jp.wasabeef.glide.transformations.BlurTransformation;

@ContentView(R.layout.activity_circle_details)
public class CircleDetailsActivity extends BaseActivity {
    RelativeLayout ly_top;
    ImageView iv_top;
    ImageView iv_circle_avatar, iv_add;
    RelativeLayout ly_add_attention;
    private View headerView;
    @ViewInject(R.id.swipRefresh_circle)
    SwipeRefreshLayout swipRefresh_circle;
    @ViewInject(R.id.recyclerView_circle)
    RecyclerView recyclerView_circle;
    private CircleInfoAdapter mAdapter;
    private List<Moment> moments = new ArrayList<>();
    private int page = 2;
    private int id;
    TextView tv_circle_name, tv_content, tv_num, tv_is_attention;
    private CircleDetail circleDetail;
    private static final int INFO_DETAILS = 111;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        init();
        initTitleBar();
        initViews();
        initAdapter();
        getListInfo();
    }

    private void initViews() {
        id = getIntent().getExtras().getInt("id");
        headerView = LayoutInflater.from(context).inflate(R.layout.header_circle_detail, null);
        ly_top = headerView.findViewById(R.id.ly_top);
        iv_top = headerView.findViewById(R.id.iv_top);
        iv_circle_avatar = headerView.findViewById(R.id.iv_circle_avatar);
        title_relate_layout = headerView.findViewById(R.id.title_layout);
        title_iv_back = headerView.findViewById(R.id.title_iv_back);
        tv_circle_name = headerView.findViewById(R.id.tv_circle_name);
        tv_content = headerView.findViewById(R.id.tv_content);
        iv_add = headerView.findViewById(R.id.iv_add);
        tv_num = headerView.findViewById(R.id.tv_num);
        tv_is_attention = headerView.findViewById(R.id.tv_is_attention);
        ly_add_attention = headerView.findViewById(R.id.ly_add_attention);
        title_relate_layout.setBackgroundColor(ContextCompat.getColor(context, R.color.transparency_color));
        int width = Utils.getDisplayWidth((Activity) context);
        RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) ly_top.getLayoutParams();
        params.width = width;
        params.height = (int) (width * 0.43);
        ly_top.setLayoutParams(params);
        RelativeLayout.LayoutParams layoutParams = (RelativeLayout.LayoutParams) iv_circle_avatar.getLayoutParams();
        layoutParams.width = width / 4;
        layoutParams.height = width / 4;
        layoutParams.setMargins(0, DensityUtil.dip2px(93), 0, 0);
        iv_circle_avatar.setLayoutParams(layoutParams);
        iv_circle_avatar.setBackgroundResource(R.mipmap.hot);
        title_iv_back.setImageResource(R.mipmap.common_icon_back_white);
        title_iv_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        ly_add_attention.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                if (circleDetail.getIsFollowed() == 1) return;
                RequestParams params = new RequestParams(Url.ATTENTION_URL + id);
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
        mAdapter.setOnItemClickListener(new RcvItemViewClickListener<Moment>() {
            @Override
            public void onItemViewClicked(RcvHolder holder, Moment moment, int position) {
                Bundle bundle = new Bundle();
                bundle.putSerializable("moment", moments.get(position - 1));
                jumpBundleActvityforResult(InfoDetailsActivity.class, bundle, INFO_DETAILS);
            }
        });
    }

    private void getListInfo() {
        RequestParams params = new RequestParams(Url.CIRCLE_DETAIL_URL);
        params.addBodyParameter("lng", PrefName.getPrefLastLng(context));
        params.addBodyParameter("lat", PrefName.getPrefLastLat(context));
        params.addBodyParameter("circleId", id + "");
        httpGet(params, "list");
        page = 2;
    }

    private void loadMoreDatas() {
        RequestParams params = new RequestParams(Url.PAGE_CIRCLE_URL);
        params.addBodyParameter("lng", PrefName.getPrefLastLng(context));
        params.addBodyParameter("lat", PrefName.getPrefLastLat(context));
        params.addBodyParameter("page", page + "");
        params.addBodyParameter("type", "3");
        params.addBodyParameter("typeId", id + "");
        httpGet(params, "page");
        page++;
    }

    @Override
    public void success(String result, String type) {
        super.success(result, type);
        switch (type) {
            case "list":
                CircleDetailResult circleDetailResult = JSON.parseObject(result, CircleDetailResult.class);
                moments = circleDetailResult.getData().getMoment();
                mAdapter.addDatas(moments);
                mAdapter.notifyDataSetChanged();
                mAdapter.enableLoadMore(true, new RcvLoadMoreListener() {
                    @Override
                    public void onLoadMoreRequest() {
                        loadMoreDatas();
                    }
                });
                circleDetail = circleDetailResult.getData().getCirclDetail();
                PicassoUtil.displayImageSquare(iv_circle_avatar, circleDetail.getAvatar(), context);
                PicassoUtil.displayImageDim(iv_top, circleDetail.getAvatar(), context);
                tv_circle_name.setText(circleDetail.getName());
                tv_content.setText(circleDetail.getIntroduce());
                tv_num.setText(circleDetail.getFollowers() + "人参与");
                swipRefresh_circle.setRefreshing(false);
                iv_top.setFocusableInTouchMode(true);
                iv_top.requestFocus();
                if (circleDetail.getIsFollowed() == 1) {
                    ly_add_attention.setBackgroundResource(R.drawable.ly_circle_detail_attention);
                    iv_add.setVisibility(View.GONE);
                    tv_is_attention.setVisibility(View.VISIBLE);
                } else {
                    ly_add_attention.setBackgroundResource(R.drawable.common_btn_click);
                    iv_add.setVisibility(View.VISIBLE);
                    tv_is_attention.setVisibility(View.GONE);
                }
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
                if (circleDetail.getIsFollowed() == 1) {
                    ly_add_attention.setBackgroundResource(R.drawable.common_btn_click);
                    iv_add.setVisibility(View.VISIBLE);
                    tv_is_attention.setVisibility(View.GONE);
                } else {
                    ly_add_attention.setBackgroundResource(R.drawable.ly_circle_detail_attention);
                    iv_add.setVisibility(View.GONE);
                    tv_is_attention.setVisibility(View.VISIBLE);
                }
                break;
        }
    }
}
