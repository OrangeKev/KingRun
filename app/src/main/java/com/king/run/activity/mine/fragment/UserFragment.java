package com.king.run.activity.mine.fragment;

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
@ContentView(R.layout.fragment_user)
public class UserFragment extends BaseFragment {
    @ViewInject(R.id.swipRefresh_user)
    SwipeRefreshLayout swipRefresh_user;
    @ViewInject(R.id.recyclerView_user)
    RecyclerView recyclerView_user;
    private FansAdapter fansAdapter;
    private List<FansInfo> listFans = new ArrayList<>();
    private int lastVisibleItemFans;
    private static final int USER_INFO_DETAILS = 121;
    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initFansAdapter();
        getListInfo();
    }

    private void getListInfo() {
        RequestParams params = new RequestParams(Url.MY_ATTENTION_USER_URL);
        httpGet(params, "list");
    }

    private void initFansAdapter() {
        fansAdapter = new FansAdapter(getActivity());
        fansAdapter.changeMoreStatus(BindFootView.NO_LOAD_MORE);
        SwipRecycSetting.setting(swipRefresh_user, getActivity());
        final LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity());
        recyclerView_user.setLayoutManager(linearLayoutManager);
        recyclerView_user.setHasFixedSize(true);
        recyclerView_user.setAdapter(fansAdapter);
        fansAdapter.setFans(false);
        //设置监听
        swipRefresh_user.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                getListInfo();
            }
        });
        recyclerView_user.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                int pos = linearLayoutManager.findLastVisibleItemPosition() + 1;
                int itemcount = fansAdapter.getItemCount();
                if (null != listFans && listFans.size() >= 10 && listFans.size() % 10 == 0 && (pos + 1) >= listFans.size()) {
                    fansAdapter.changeMoreStatus(BindFootView.PULLUP_LOAD_MORE);
                    //&& lastVisibleItemFans + 1 == itemcount
                    if (newState == RecyclerView.SCROLL_STATE_IDLE) {
                        fansAdapter.changeMoreStatus(BindFootView.LOADING_MORE);
                        new Handler().postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                fansAdapter.changeMoreStatus(BindFootView.NO_LOAD_MORE);
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
        fansAdapter.setOnItemClickListener(new MyItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                Bundle bundle = new Bundle();
                bundle.putString("userId", listFans.get(position).getUserId());
                jumpBundleActvityforResult(UserInfoDetailsActivity.class, bundle, USER_INFO_DETAILS);
            }
        });
    }

    @Override
    public void success(String result, String type) {
        super.success(result, type);
        switch (type) {
            case "list":
                FansInfoResult fansInfoResult = JSON.parseObject(result, FansInfoResult.class);
                listFans = fansInfoResult.getData();
                fansAdapter.setList(listFans);
                fansAdapter.notifyDataSetChanged();
                swipRefresh_user.setRefreshing(false);
                break;
        }
    }
}
