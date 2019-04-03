package com.king.run.activity.mine;

import android.os.Bundle;
import android.os.Handler;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.king.run.R;
import com.king.run.activity.mine.adapter.CollectAdapter;
import com.king.run.activity.mine.model.FansInfo;
import com.king.run.base.BaseActivity;
import com.king.run.intface.MyItemClickListener;
import com.king.run.view.BindFootView;
import com.king.run.view.SwipRecycSetting;

import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.ViewInject;

import java.util.ArrayList;
import java.util.List;

/**
 * 作者：shuizi_wade on 2017/8/22 15:27
 * 邮箱：674618016@qq.com
 */
@ContentView(R.layout.activity_collect)
public class CollectActivity extends BaseActivity {
    @ViewInject(R.id.swipRefresh)
    SwipeRefreshLayout swipeRefresh;
    @ViewInject(R.id.recyclerView)
    RecyclerView recyclerView;
    private List<FansInfo> list = new ArrayList<>();
    private CollectAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        init();
        initViews();
        initAdapter();
    }

    private void initViews() {
        title_tv_title.setText(R.string.collect);
    }

    private void initAdapter() {
        for (int i = 0; i < 10; i++) {
            FansInfo f = new FansInfo();
            list.add(f);
        }
        adapter = new CollectAdapter(context);
        adapter.changeMoreStatus(BindFootView.NO_LOAD_MORE);
        SwipRecycSetting.setting(swipeRefresh, context);
        final LinearLayoutManager linearLayoutManager = new LinearLayoutManager(context);
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setHasFixedSize(true);
        recyclerView.setAdapter(adapter);
        adapter.setList(list);
        adapter.notifyDataSetChanged();
        //设置监听
        swipeRefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
//                getList();
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        swipeRefresh.setRefreshing(false);
                    }
                }, 1200);
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
//                lastVisibleItem = linearLayoutManager.findLastVisibleItemPosition();
            }
        });
        adapter.setOnItemClickListener(new MyItemClickListener() {
            @Override
            public void onItemClick(View view, int postion) {

            }
        });
    }
}
