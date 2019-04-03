package com.king.run.activity.circle;

import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.alibaba.fastjson.JSON;
import com.king.run.R;
import com.king.run.activity.circle.adapter.CircleInfoAdapter;
import com.king.run.activity.circle.model.Moment;
import com.king.run.activity.circle.model.PageResult;
import com.king.run.base.BaseFragment;
import com.king.run.util.PrefName;
import com.king.run.util.Url;
import com.lwkandroid.rcvadapter.holder.RcvHolder;
import com.lwkandroid.rcvadapter.listener.RcvItemViewClickListener;
import com.lwkandroid.rcvadapter.listener.RcvLoadMoreListener;

import org.xutils.http.RequestParams;
import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.ViewInject;

import java.util.ArrayList;
import java.util.List;

@ContentView(R.layout.fragment_latest)
public class LatestFragment extends BaseFragment {
    @ViewInject(R.id.swipRefresh_circle)
    SwipeRefreshLayout swipRefresh_circle;
    @ViewInject(R.id.recyclerView_circle)
    RecyclerView recyclerView_circle;
    private List<Moment> moments = new ArrayList<>();
    private CircleInfoAdapter mAdapter;
    private int page = 1;
    private static final int INFO_DETAILS = 111;
    private static final int USER_INFO_DETAILS = 121;

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initCircleAdapter();
        loadMoreDatas();
    }

    private void initCircleAdapter() {
        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
        mAdapter = new CircleInfoAdapter(getActivity(), moments);
        recyclerView_circle.setLayoutManager(layoutManager);
        recyclerView_circle.setAdapter(mAdapter);
        mAdapter.enableLoadMore(false);
        swipRefresh_circle.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                page = 1;
                loadMoreDatas();
            }
        });

        mAdapter.setOnItemClickListener(new RcvItemViewClickListener<Moment>() {
            @Override
            public void onItemViewClicked(RcvHolder holder, Moment circleInfo, int position) {
//                senToa(position + "");
                Bundle bundle = new Bundle();
                bundle.putSerializable("moment", moments.get(position));
                jumpBundleActvityforResult(InfoDetailsActivity.class, bundle, INFO_DETAILS);
            }
        });
        mAdapter.setOnItemAvatarClickListener(new CircleInfoAdapter.ItemAvatarClickInterface() {
            @Override
            public void onAvatarClick(int position) {
                Bundle bundle = new Bundle();
                bundle.putString("userId", moments.get(position - 1).getUserId());
                jumpBundleActvityforResult(UserInfoDetailsActivity.class, bundle, USER_INFO_DETAILS);
            }
        });
    }

    private void loadMoreDatas() {
        RequestParams params = new RequestParams(Url.PAGE_CIRCLE_URL);
        params.addBodyParameter("lng", PrefName.getPrefLastLng(getActivity()));
        params.addBodyParameter("lat", PrefName.getPrefLastLat(getActivity()));
        params.addBodyParameter("page", page + "");
        params.addBodyParameter("type", "1");
        httpGet(params, "page");
        page++;
    }

    @Override
    public void success(String result, String type) {
        super.success(result, type);
        switch (type) {
            case "page":
                PageResult pageResult = JSON.parseObject(result, PageResult.class);
                if (page == 2) {
                    moments.clear();
                    moments = pageResult.getData();
                    mAdapter.addDatas(moments);
                    mAdapter.notifyDataSetChanged();
                    mAdapter.enableLoadMore(true, new RcvLoadMoreListener() {
                        @Override
                        public void onLoadMoreRequest() {
                            loadMoreDatas();
                        }
                    });
                    swipRefresh_circle.setRefreshing(false);
                } else {
                    if (null != pageResult.getData() && !pageResult.getData().isEmpty()) {
                        moments.addAll(pageResult.getData());
                        if (pageResult.getData().size() < 10) {
                            mAdapter.notifyLoadMoreSuccess(pageResult.getData(), false);
                            page = 1;
                        } else {
                            mAdapter.notifyLoadMoreSuccess(pageResult.getData(), true);
                        }
                    } else {
                        mAdapter.notifyLoadMoreSuccess(new ArrayList<Moment>(), false);
                        page = 1;
                    }
                }
                break;
        }
    }
}
