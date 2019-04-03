package com.king.run.activity.mine.fragment;

import android.os.Bundle;
import android.os.Handler;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.LinearLayout;

import com.alibaba.fastjson.JSON;
import com.king.run.R;
import com.king.run.activity.circle.CircleDetailsActivity;
import com.king.run.activity.circle.model.CircleInfo;
import com.king.run.activity.circle.model.CircleInfoResult;
import com.king.run.activity.mine.adapter.CircleAdapter;
import com.king.run.base.BaseFragment;
import com.king.run.intface.MyItemClickListener;
import com.king.run.util.Url;
import com.king.run.view.BindFootView;
import com.king.run.view.SwipRecycSetting;

import org.xutils.http.RequestParams;
import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.ViewInject;

import java.util.ArrayList;
import java.util.List;

/**
 * 作者：shuizi_wade on 2017/8/24 17:25
 * 邮箱：674618016@qq.com
 */
@ContentView(R.layout.fragment_attention_circle)
public class AttentionCircleFragment extends BaseFragment {
    @ViewInject(R.id.swipRefresh_circle)
    SwipeRefreshLayout swipRefresh_circle;
    @ViewInject(R.id.recyclerView_circle)
    RecyclerView recyclerView_circle;
    private CircleAdapter circleAdapter;
    private List<CircleInfo> list = new ArrayList<>();
    private int lastVisibleItemFans;
    @ViewInject(R.id.ly_no_circle)
    LinearLayout ly_no_circle;

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initCircleAdapter();
        getAttentionCircle();
    }

    private void getAttentionCircle() {
        RequestParams params = new RequestParams(Url.USER_ATTENTION_URL);
        httpGet(params, "userAttention");
    }

    private void initCircleAdapter() {
        circleAdapter = new CircleAdapter(getActivity());
        circleAdapter.changeMoreStatus(BindFootView.NO_LOAD_MORE);
        SwipRecycSetting.setting(swipRefresh_circle, getActivity());
        final LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity());
        recyclerView_circle.setLayoutManager(linearLayoutManager);
        recyclerView_circle.setHasFixedSize(true);
        recyclerView_circle.setAdapter(circleAdapter);
        //设置监听
        swipRefresh_circle.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        swipRefresh_circle.setRefreshing(false);
                    }
                }, 1200);
            }
        });
        recyclerView_circle.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                int pos = linearLayoutManager.findLastVisibleItemPosition() + 1;
                int itemcount = circleAdapter.getItemCount();
                if (list.size() >= 10 && null != list && list.size() % 10 == 0 && (pos + 1) >= list.size()) {
                    circleAdapter.changeMoreStatus(BindFootView.PULLUP_LOAD_MORE);
                    //&& lastVisibleItemFans + 1 == itemcount
                    if (newState == RecyclerView.SCROLL_STATE_IDLE) {
                        circleAdapter.changeMoreStatus(BindFootView.LOADING_MORE);
                        new Handler().postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                circleAdapter.changeMoreStatus(BindFootView.NO_LOAD_MORE);
                            }
                        }, 1200);
                    }
                }
            }

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                lastVisibleItemFans = linearLayoutManager.findLastVisibleItemPosition();
            }
        });
        circleAdapter.setOnItemClickListener(new MyItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                Bundle bundle = new Bundle();
                bundle.putInt("id", list.get(position).getId());
                jumpBundleActvity(CircleDetailsActivity.class, bundle);
            }
        });
    }

    @Override
    public void success(String result, String type) {
        super.success(result, type);
        switch (type) {
            case "userAttention":
                circleAdapter.setMyAttention(true);
                CircleInfoResult circleInfoResult = JSON.parseObject(result, CircleInfoResult.class);
                list = circleInfoResult.getData();
                if (null != list && !list.isEmpty()) {
                    ly_no_circle.setVisibility(View.INVISIBLE);
                    circleAdapter.setList(list);
                    circleAdapter.notifyDataSetChanged();
                } else {
                    ly_no_circle.setVisibility(View.VISIBLE);
                }
                break;
        }
    }
}
