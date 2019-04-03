package com.king.run.activity.statistics;

import android.app.Activity;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.constraint.ConstraintLayout;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.king.run.R;
import com.king.run.activity.sport.run.RunFinishDetailsActivity;
import com.king.run.activity.statistics.adapter.MyFgAdapter;
import com.king.run.activity.statistics.adapter.StatisticsListAdapter;
import com.king.run.activity.statistics.fragment.KcalLineFg;
import com.king.run.activity.statistics.fragment.KmLineFg;
import com.king.run.activity.statistics.fragment.StepsLinesFg;
import com.king.run.activity.statistics.model.StatisListResult;
import com.king.run.base.BaseActivity;
import com.king.run.commen.CommonRecyclerViewAdapter;
import com.king.run.commen.CommonRecyclerViewHolder;
import com.king.run.commen.MyItemDecoration;
import com.king.run.intface.http.ReqBack;
import com.king.run.intface.http.iml.SportIml;
import com.king.run.model.http.res.ExerciseDetailInfo;
import com.king.run.model.http.res.StatisticsRes;
import com.king.run.util.DateUtils;
import com.king.run.util.PrefName;
import com.king.run.util.StepAlgorithm;
import com.king.run.util.StringUtil;
import com.king.run.util.TimeUtil;
import com.king.run.util.Url;
import com.king.run.util.Utils;
import com.king.run.util.ViewRefUtil;
import com.lwkandroid.rcvadapter.holder.RcvHolder;
import com.lwkandroid.rcvadapter.listener.RcvItemViewClickListener;
import com.lwkandroid.rcvadapter.listener.RcvLoadMoreListener;
import com.orhanobut.logger.Logger;

import org.xutils.http.RequestParams;
import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.ViewInject;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import steps.teller.step.bean.StepData;
import steps.teller.step.utils.DbUtils;

/**
 * 折线统计界面
 */
@ContentView(R.layout.activity_line_chart)
public class LineChartActivity extends BaseActivity implements ReqBack {

    @ViewInject(R.id.tab_layout)
    private TabLayout tabLayout;
    @ViewInject(R.id.view_pager)
    private ViewPager viewPager;

    private StepsLinesFg stepsFg;
    private KcalLineFg kcalFg;
    private KmLineFg kmFg;
    private List<Fragment> frags;
    private String[] tabNames;
    private List<ExerciseDetailInfo> items = new ArrayList<>();
    @ViewInject(R.id.swipRefresh_circle)
    SwipeRefreshLayout swipRefresh_circle;
    @ViewInject(R.id.recyclerView_circle)
    RecyclerView recyclerView_circle;
    private StatisticsListAdapter statisticsListAdapter;
    private int page = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        init();
        initViews();
        initAdapter();
        SportIml.getInstance(this).statisticsData(this);
        loadMore();
    }

    private void initAdapter() {
        LinearLayoutManager layoutManager = new LinearLayoutManager(context);
        statisticsListAdapter = new StatisticsListAdapter(context, items);
        recyclerView_circle.setLayoutManager(layoutManager);
        recyclerView_circle.setAdapter(statisticsListAdapter);
        statisticsListAdapter.enableLoadMore(false);
        swipRefresh_circle.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                page = 1;
                loadMore();
            }
        });
        statisticsListAdapter.setOnItemClickListener(new RcvItemViewClickListener<ExerciseDetailInfo>() {
            @Override
            public void onItemViewClicked(RcvHolder holder, ExerciseDetailInfo exerciseDetailInfo, int position) {
                Bundle bundle = new Bundle();
                bundle.putSerializable("detailInfo", exerciseDetailInfo);
                jumpBundleActvity(RunFinishDetailsActivity.class, bundle);
            }
        });
    }

    private void initViews() {
        int height = Utils.getDisplayHeight((Activity) context);
        ConstraintLayout.LayoutParams params = (ConstraintLayout.LayoutParams) viewPager.getLayoutParams();
        params.height = (int) (height * 0.5);
        viewPager.setLayoutParams(params);
        title_tv_title.setText(getString(R.string.data_statistics));
        title_iv_right.setImageResource(R.mipmap.record_icon_statistics);
        ly_iv_right.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                jumpActvity(BarChartActivity.class);
            }
        });
        stepsFg = new StepsLinesFg();
        kcalFg = new KcalLineFg();
        kmFg = new KmLineFg();

        frags = new ArrayList<>();
        frags.add(kmFg);
        frags.add(stepsFg);
        frags.add(kcalFg);

        tabNames = new String[]{getString(R.string.km), getString(R.string.steps), getString(R.string.kcal)};
        MyFgAdapter fgAdapter = new MyFgAdapter(
                getSupportFragmentManager(), frags, tabNames);
        viewPager.setAdapter(fgAdapter);
        tabLayout.setupWithViewPager(viewPager);
        ViewRefUtil.reflex(tabLayout);
    }

    private void loadMore() {
        RequestParams params = new RequestParams(Url.STATISTICAL_LIST_DATA + page);
        httpGet(params, "page");
        page++;
    }

    @Override
    public void success(String result, String type) {
        super.success(result, type);
        switch (type) {
            case "page":
                StatisListResult statisListResult = JSON.parseObject(result, StatisListResult.class);
                if (page == 2) {
                    items.clear();
                    items = statisListResult.getData();
                    statisticsListAdapter.addDatas(items);
                    statisticsListAdapter.notifyDataSetChanged();
                    statisticsListAdapter.enableLoadMore(true, new RcvLoadMoreListener() {
                        @Override
                        public void onLoadMoreRequest() {
                            loadMore();
                        }
                    });
                    swipRefresh_circle.setRefreshing(false);
                } else {
                    if (null != statisListResult.getData() && !statisListResult.getData().isEmpty()) {
                        items.addAll(statisListResult.getData());
                        if (statisListResult.getData().size() < 10) {
                            statisticsListAdapter.notifyLoadMoreSuccess(statisListResult.getData(), false);
                            page = 1;
                        } else {
                            statisticsListAdapter.notifyLoadMoreSuccess(statisListResult.getData(), true);
                        }
                    } else {
                        statisticsListAdapter.notifyLoadMoreSuccess(new ArrayList<ExerciseDetailInfo>(), false);
                        page = 1;
                    }
                }
                break;
        }
    }

    @Override
    public void onSuccess(String result) {
        Logger.json(result);
        StatisticsRes res = JSON.parseObject(result, StatisticsRes.class);
        kcalFg.setDatasChanged(res.getCalorie());
        kmFg.setDatasChanged(res.getKilometre());
        stepsFg.setDatasChanged(res.getStep());
    }

    @Override
    public void onError(String error) {
        Toast.makeText(this, "获取数据失败", Toast.LENGTH_SHORT).show();
    }
}
