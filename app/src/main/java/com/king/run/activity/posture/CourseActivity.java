package com.king.run.activity.posture;

import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.king.run.R;
import com.king.run.activity.posture.adapter.CourseVideoListAdapter;
import com.king.run.base.BaseActivity;

import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.ViewInject;

import java.util.ArrayList;
import java.util.List;

@ContentView(R.layout.activity_course)
public class CourseActivity extends BaseActivity {
    private CourseVideoListAdapter adapter;
    private List<String> list = new ArrayList<>();
    @ViewInject(R.id.swipRefresh_circle)
    SwipeRefreshLayout swipRefresh_circle;
    @ViewInject(R.id.recyclerView_circle)
    RecyclerView recyclerView_circle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        init();
        initViews();
    }

    private void initViews() {
        title_tv_title.setText(R.string.teach);
        for (int i = 0; i < 6; i++) {
            list.add("2");
        }
        LinearLayoutManager layoutManager = new LinearLayoutManager(context);
        adapter = new CourseVideoListAdapter(context, list);
        recyclerView_circle.setLayoutManager(layoutManager);
        adapter.enableLoadMore(false);
        adapter.enableItemShowingAnim(true);
        recyclerView_circle.setAdapter(adapter);
        swipRefresh_circle.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                swipRefresh_circle.setRefreshing(false);
            }
        });
    }
}
