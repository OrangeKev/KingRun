package com.king.run.activity.statistics.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.king.run.R;
import com.king.run.base.BaseFragment;
import com.king.run.commen.CommonRecyclerViewAdapter;
import com.king.run.commen.CommonRecyclerViewHolder;
import com.king.run.commen.MyItemDecoration;
import com.king.run.intface.http.ReqBack;
import com.king.run.intface.http.iml.SportIml;
import com.king.run.model.http.res.StatisticsItem;
import com.king.run.model.http.res.StatisticsRes;
import com.king.run.util.PrefName;
import com.king.run.util.StepAlgorithm;
import com.king.run.view.linechart.LineChart;
import com.king.run.view.linechart.LineChartData;

import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.ViewInject;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import steps.teller.step.bean.StepData;

/**
 * 消耗统计Fragment
 */
@ContentView(R.layout.fragment_kcal_statistics)
public class KcalLineFg extends BaseFragment {

    @ViewInject(R.id.line_chart_week_steps)
    private LineChart lineChart;
    private String[] mDayItems = new String[]{"5.1", "5.2", "5.3", "5.4", "5.5", "5.6", "今天"};
    private double[] mDayPoints = new double[]{0, 0, 0, 0, 0, 0, 0};
    private List<LineChartData> dataList = new ArrayList<>();
    private List<StatisticsItem> items = new ArrayList<>();
//    private List<StepData> datas = new ArrayList<>();

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        drawLineChart();
        initRecycleView();
    }

    private void drawLineChart() {
        dataList.clear();
        for (int i = 0; i < mDayItems.length; i++) {
            LineChartData data = new LineChartData();
            data.setItem(mDayItems[i]);
            data.setPoint(mDayPoints[i]);
            dataList.add(data);
        }
        if (lineChart != null && dataList != null)
            lineChart.setData(dataList);
    }

    //
//    @ViewInject(R.id.rc_view)
//    private RecyclerView rcView;
    private CommonRecyclerViewAdapter<StatisticsItem> rcAdapter;

    private void initRecycleView() {
        //创建一个线性的布局管理器并设置
//        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
//
//        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
//        rcView.setHasFixedSize(true);
//        rcView.setLayoutManager(layoutManager);
//        rcView.addItemDecoration(new MyItemDecoration());
//
//        rcAdapter = new CommonRecyclerViewAdapter<StatisticsItem>(getActivity(), items) {
//            @Override
//            public void convert(CommonRecyclerViewHolder h, StatisticsItem entity, int position) {
//
////                String steps = entity.getStep();
////                String time = entity.getSecond();
////                h.setText(R.id.tv_time, time);
////                h.setText(R.id.tv_unit, "大卡");
////                h.setText(R.id.tv_unit_value, StepAlgorithm.getKcal(65,
////                        StepAlgorithm.getDisDouble(Integer.valueOf(steps))));
////                h.setText(R.id.tv_date, entity.getToday());
//                h.setText(R.id.tv_time, "");
//                h.setText(R.id.tv_unit, "大卡");
//                h.setText(R.id.tv_unit_value, entity.getValue());
//                h.setText(R.id.tv_date, entity.getDate());
//
//            }
//
//            @Override
//            public int getLayoutViewId(int viewType) {
//                return R.layout.item_line_statistic;
//            }
//        };
//        rcView.setAdapter(rcAdapter);
    }


    public void setDatas(List<StepData> datas, double[] mDayPoints, String[] mDayItems) {
//        this.datas = datas;
//        this.mDayItems = mDayItems;
//        this.mDayPoints = mDayPoints;
//        if (rcAdapter != null) {
//            rcAdapter.notifyDataSetChanged();
//        }
//        drawLineChart();
    }

    public void setDatasChanged(ArrayList<StatisticsItem> calorie) {
        items.clear();
        items.addAll(calorie);
        Collections.reverse(items);
        for (int i = 0; i < items.size(); i++) {
            mDayItems[i] = items.get(i).getDate();
            mDayPoints[i] = Double.valueOf(items.get(i).getValue());
        }
        if (rcAdapter != null) {
            rcAdapter.notifyDataSetChanged();
        }
        drawLineChart();
    }
}
