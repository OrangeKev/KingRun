package com.king.run.activity.mine;

import android.os.Bundle;
import android.os.Handler;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.alibaba.fastjson.JSON;
import com.king.run.R;
import com.king.run.activity.circle.UserInfoDetailsActivity;
import com.king.run.activity.mine.adapter.FansAdapter;
import com.king.run.activity.mine.model.FansInfo;
import com.king.run.activity.mine.model.FansInfoResult;
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

@ContentView(R.layout.activity_fans)
public class FansActivity extends BaseActivity {
    @ViewInject(R.id.swipRefresh)
    SwipeRefreshLayout swipeRefresh;
    @ViewInject(R.id.recyclerView)
    RecyclerView recyclerView;
    private FansAdapter adapter;
    private List<FansInfo> list = new ArrayList<>();
    private int lastVisibleItem;
    private static final int USER_INFO_DETAILS = 121;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        init();
        initViews();
        initAdapter();
        getListInfo();
    }

    private void getListInfo() {
        RequestParams params = new RequestParams(Url.FANS_USER_URL);
        httpGet(params, "list");
    }

    private void initViews() {
        title_tv_title.setText(R.string.fans);
    }

    private void initAdapter() {
        adapter = new FansAdapter(context);
        adapter.changeMoreStatus(BindFootView.NO_LOAD_MORE);
        SwipRecycSetting.setting(swipeRefresh, context);
        final LinearLayoutManager linearLayoutManager = new LinearLayoutManager(context);
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setHasFixedSize(true);
        recyclerView.setAdapter(adapter);
        adapter.setFans(true);
        //设置监听
        swipeRefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                getListInfo();
            }
        });
        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                int pos = linearLayoutManager.findLastVisibleItemPosition() + 1;
                int itemcount = adapter.getItemCount();
                if (list.size() >= 10 && null != list && list.size() % 10 == 0 && (pos + 1) >= list.size()) {
                    adapter.changeMoreStatus(BindFootView.PULLUP_LOAD_MORE);
                    //&& lastVisibleItem + 1 == itemcount
                    if (newState == RecyclerView.SCROLL_STATE_IDLE) {
                        adapter.changeMoreStatus(BindFootView.LOADING_MORE);
                        new Handler().postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                adapter.changeMoreStatus(BindFootView.NO_LOAD_MORE);
                            }
                        }, 1200);
                    }
                }
            }

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                lastVisibleItem = linearLayoutManager.findLastVisibleItemPosition();
            }
        });
        adapter.setOnItemClickListener(new MyItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                Bundle bundle = new Bundle();
                bundle.putString("userId", list.get(position).getUserId());
                jumpBundleActvityforResult(UserInfoDetailsActivity.class, bundle, USER_INFO_DETAILS);
            }
        });
        adapter.setOnItemAttentionClickListener(new FansAdapter.AttentionClick() {
            @Override
            public void attentionClick(int position) {
                RequestParams params = new RequestParams(Url.ATTENTION_USER_URL + list.get(position).getUserId());
                httpPost("attention", params);
            }
        });
    }

    @Override
    public void success(String result, String type) {
        super.success(result, type);
        switch (type) {
            case "list":
                FansInfoResult fansInfoResult = JSON.parseObject(result, FansInfoResult.class);
                list = fansInfoResult.getData();
                adapter.setList(list);
                adapter.notifyDataSetChanged();
                swipeRefresh.setRefreshing(false);
                break;
            case "attention":
                getListInfo();
//                if (moment.isFollowed()) {
//                    btn_attention.setBackgroundResource(R.drawable.btn_attention_false);
//                    btn_attention.setText(R.string.attention);
//                    btn_attention.setTextColor(ContextCompat.getColor(context, R.color.red));
//                } else {
//                    btn_attention.setBackgroundResource(R.drawable.btn_attention_true);
//                    btn_attention.setText(R.string.attention_on);
//                    btn_attention.setTextColor(ContextCompat.getColor(context, R.color.sub_class_content_text_color));
//                }
                break;
        }
    }
}
