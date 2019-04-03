package com.king.run.activity.circle;

import android.os.Bundle;
import android.os.Handler;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.alibaba.fastjson.JSON;
import com.king.run.R;
import com.king.run.activity.circle.model.CircleInfo;
import com.king.run.activity.circle.model.CircleInfoResult;
import com.king.run.activity.mine.adapter.CircleAdapter;
import com.king.run.activity.mine.model.FansInfo;
import com.king.run.base.BaseActivity;
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
 * 作者：shuizi_wade on 2017/11/10 14:32
 * 邮箱：674618016@qq.com
 */
@ContentView(R.layout.activity_hot_circle)
public class HotCircleActivity extends BaseActivity {
    @ViewInject(R.id.swipRefresh_circle)
    SwipeRefreshLayout swipRefresh_circle;
    @ViewInject(R.id.recyclerView_circle)
    RecyclerView recyclerView_circle;
    private CircleAdapter circleAdapter;
    private List<CircleInfo> list = new ArrayList<>();
    private int pos;
    private String action;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        init();
        initViews();
        initCircleAdapter();

    }

    private void initViews() {
        action = getIntent().getAction();

    }

    private void initCircleAdapter() {
        circleAdapter = new CircleAdapter(context);
        switch (action) {
            case "hot":
                title_tv_title.setText(R.string.hot_circle);
                getInfo();
                break;
            case "attention":
                title_tv_title.setText(R.string.my_attention_circle);
                getAttentionCircle();
                circleAdapter.setMyAttention(true);
                break;
        }
        circleAdapter.changeMoreStatus(BindFootView.NO_LOAD_MORE);
        SwipRecycSetting.setting(swipRefresh_circle, context);
        final LinearLayoutManager linearLayoutManager = new LinearLayoutManager(context);
        recyclerView_circle.setLayoutManager(linearLayoutManager);
        recyclerView_circle.setHasFixedSize(true);
        recyclerView_circle.setAdapter(circleAdapter);
        //设置监听
        swipRefresh_circle.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
//                getList();
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
        circleAdapter.setOnClickListener(new CircleAdapter.OnClickListener() {
            @Override
            public void onClick(int position) {
                RequestParams params = new RequestParams(Url.ATTENTION_URL + list.get(position).getId());
                httpPost("attention", params);
                pos = position;
            }
        });
    }

    /**
     * 热门
     */
    private void getInfo() {
        RequestParams params = new RequestParams(Url.GET_HOT_CIRCLE_URL);
        httpGet(params, "getHotCircle");
    }

    /**
     * 关注
     */
    private void getAttentionCircle() {
        RequestParams params = new RequestParams(Url.USER_ATTENTION_URL);
        httpGet(params, "getHotCircle");
    }

    @Override
    public void success(String result, String type) {
        super.success(result, type);
        switch (type) {
            case "getHotCircle":
                CircleInfoResult circleInfoResult = JSON.parseObject(result, CircleInfoResult.class);
                list = circleInfoResult.getData();
                circleAdapter.setList(list);
                circleAdapter.notifyDataSetChanged();
                break;
            case "attention":
                if (list.get(pos).getIsFollowed() == 1)
                    list.get(pos).setIsFollowed(0);
                else
                    list.get(pos).setIsFollowed(1);
                circleAdapter.setList(list);
                circleAdapter.notifyDataSetChanged();
                break;
        }
    }
}
