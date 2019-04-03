package com.king.run.activity.statistics.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.king.run.R;
import com.king.run.base.BaseFragment;
import com.king.run.intface.http.ReqBack;
import com.king.run.intface.http.iml.SportIml;
import com.king.run.model.http.req.StatisticsBarReq;
import com.king.run.model.http.res.StatisticsBarRes;
import com.king.run.model.http.res.StatisticsItem;
import com.king.run.util.DateUtils;
import com.king.run.util.PrefName;
import com.king.run.view.barchart.BarStaView;
import com.orhanobut.logger.Logger;

import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.Event;
import org.xutils.view.annotation.ViewInject;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Random;

/**
 * @author xwk
 */
@ContentView(R.layout.fragment_week)
public class MonthBarFg extends BaseFragment implements ReqBack {

    @ViewInject(R.id.my_single_chart_view)
    private BarStaView mMySingleChartView;
    private LinearLayout llSingle;
    @ViewInject(R.id.rl_single)
    private RelativeLayout rlSingle;
    @ViewInject(R.id.btn_share_record)
    private Button btnAchievement;
    @ViewInject(R.id.tv_date_bar)
    private TextView tvDate;
    @ViewInject(R.id.iv_date_left)
    private ImageView ivPreDate;
    @ViewInject(R.id.iv_date_right)
    private ImageView ivNextDate;
    @ViewInject(R.id.tv_value_km)
    private TextView tvValueKm;
    @ViewInject(R.id.tv_value_step)
    private TextView tvValueStep;
    @ViewInject(R.id.tv_value_kcal)
    private TextView tvValueKcacl;
    @ViewInject(R.id.tv_value_time)
    private TextView tvValueTime;
    private int[] singlelist;
    private String[] xValue;
    private Calendar cal;
    private long start, end;

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        cal = Calendar.getInstance();
        initViews();
        initData();
    }

    private void initViews() {
        end = cal.getTimeInMillis();
        cal.add(Calendar.DATE, -6);
        start = cal.getTimeInMillis();
    }

    private void initData() {
        tvDate.setText(cal.get(Calendar.YEAR) + "年" + (cal.get(Calendar.MONTH) + 1) + "月");
        initBar();
        StatisticsBarReq req = new StatisticsBarReq();
        req.setStartTime(start + "");
        req.setEndTime(end + "");
        req.setType("1");
        req.setToken(PrefName.getToken(getActivity()));
        SportIml.getInstance(getActivity()).statisticsBarData(req, this);
    }

    @Event(value = {R.id.iv_date_left, R.id.iv_date_right, R.id.iv_left_seven, R.id.iv_right_seven})
    private void getEvent(View view) {
        switch (view.getId()) {
            case R.id.iv_date_left:
                cal.set(Calendar.DAY_OF_MONTH, 1);
                cal.add(Calendar.DAY_OF_MONTH, -1);
                end = cal.getTimeInMillis();
                initViews();
                initData();
                break;
            case R.id.iv_date_right:
                cal.add(Calendar.MONTH, +1);
                cal.set(Calendar.DAY_OF_MONTH, 1);
                start = cal.getTimeInMillis();
                cal.add(Calendar.DATE, +6);
                end = cal.getTimeInMillis();
                initData();
                break;
            case R.id.iv_left_seven:
                getLeft();
                initData();
                break;
            case R.id.iv_right_seven:
                getRight();
                initData();
                break;
        }
    }

    private void getLeft() {
        cal.setTimeInMillis(start);
        cal.add(Calendar.DATE, -1);
        initViews();
    }

    private void getRight() {
        cal.setTimeInMillis(end);
        cal.add(Calendar.DATE, +1);
        start = cal.getTimeInMillis();
        cal.add(Calendar.DATE, +6);
        end = cal.getTimeInMillis();
    }

    /**
     * 初始化单柱柱状图
     */
    private void initBar() {
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(start);
        singlelist = new int[7];
        xValue = new String[7];
        for (int i = 0; i < singlelist.length; i++) {
            singlelist[i] = 0;
            xValue[i] = (calendar.get(Calendar.DAY_OF_MONTH)) + "";
            calendar.add(Calendar.DATE, +1);
        }
        mMySingleChartView.upData(singlelist);
        mMySingleChartView.upDateTextForX(xValue);
    }


    @Override
    public void onSuccess(String result) {
        if (!TextUtils.isEmpty(result)) {
            Log.e("xwk", "-------" + result);
            StatisticsBarRes res = JSON.parseObject(result, StatisticsBarRes.class);
            //加载grideview数据
            tvValueKcacl.setText(res.getAvgCalorie());
            tvValueKm.setText(res.getAvgKilometre());
            tvValueStep.setText(res.getAvgStep());
            tvValueTime.setText(DateUtils.formatTime(Long.valueOf(res.getAvgSecond())));
            //加载柱状图
            ArrayList<StatisticsItem> barData = res.getBarData();

            for (int i = 0; i < 7; i++) {
                if (i < barData.size())
                    singlelist[i] = Integer.parseInt(barData.get(i).getValue());
                else
                    singlelist[i] = 0;
            }
            mMySingleChartView.upData(singlelist);
            mMySingleChartView.upDateTextForX(xValue);

        }
    }

    @Override
    public void onError(String error) {

    }
}