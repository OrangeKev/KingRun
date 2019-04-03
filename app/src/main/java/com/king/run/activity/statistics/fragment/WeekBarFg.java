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

import com.alibaba.fastjson.JSON;
import com.king.run.R;
import com.king.run.activity.mine.model.UserInfo;
import com.king.run.activity.statistics.adapter.GridAdapter;
import com.king.run.activity.statistics.model.ItemBar;
import com.king.run.base.BaseFragment;
import com.king.run.intface.http.ReqBack;
import com.king.run.intface.http.iml.SportIml;
import com.king.run.model.http.req.StatisticsBarReq;
import com.king.run.model.http.res.StatisticsBarRes;
import com.king.run.model.http.res.StatisticsItem;
import com.king.run.model.http.res.StatisticsRes;
import com.king.run.util.DateUtils;
import com.king.run.util.PicassoUtil;
import com.king.run.util.PrefName;
import com.king.run.util.TimeUtil;
import com.king.run.view.barchart.BarStaView;
import com.orhanobut.logger.Logger;

import org.xutils.http.RequestParams;
import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.Event;
import org.xutils.view.annotation.ViewInject;

import java.text.SimpleDateFormat;
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
public class WeekBarFg extends BaseFragment {

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
    @ViewInject(R.id.iv_right_seven)
    ImageView iv_right_seven;
    @ViewInject(R.id.iv_left_seven)
    ImageView iv_left_seven;
    private int[] singlelist = new int[7];
    private String[] xValue;
    private List<Long> sevenDay = new ArrayList<>();
    private int startMonth, startDay, endMonth, endDay;

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initView();
        initData();
        initBar();
    }

    private void initView() {
        iv_left_seven.setVisibility(View.GONE);
        iv_right_seven.setVisibility(View.GONE);
        Calendar cal = Calendar.getInstance();
        cal.set(Calendar.DAY_OF_WEEK, cal.getFirstDayOfWeek());
        cal.add(Calendar.DATE, +1);
        long start = cal.getTimeInMillis();
        cal.add(Calendar.DATE, +6);
        long end = cal.getTimeInMillis();
        sevenDay.add(start);
        sevenDay.add(end);
    }

    private void initData() {
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(sevenDay.get(0));
        startMonth = calendar.get(Calendar.MONTH) + 1;
        startDay = calendar.get(Calendar.DAY_OF_MONTH);
        calendar.add(Calendar.DATE, +6);
        endMonth = calendar.get(Calendar.MONTH) + 1;
        endDay = calendar.get(Calendar.DAY_OF_MONTH);
        tvDate.setText(startMonth + "月" + startDay + "号~" + endMonth + "月" + endDay + "号");


        String url = SportIml.BSAE_URL + SportIml.HISTOGRAMGRAM_DATA;
        RequestParams params = new RequestParams(url);
        params.addBodyParameter("token", PrefName.getToken(getActivity()));
        params.addBodyParameter("type", "0");
        params.addBodyParameter("startTime", sevenDay.get(0) + "");
        params.addBodyParameter("endTime", sevenDay.get(1) + "");
        httpGet(params, "bar");
    }

    @Event(value = {R.id.iv_date_left, R.id.iv_date_right})
    private void getEvent(View view) {
        switch (view.getId()) {
            case R.id.iv_date_left:
                getLeft();
                initData();
                break;
            case R.id.iv_date_right:
                long time = sevenDay.get(1);
                Calendar ca = Calendar.getInstance();
                ca.setTimeInMillis(time);
                ca.set(Calendar.MINUTE, 0);
                ca.set(Calendar.HOUR_OF_DAY, 0);
                ca.set(Calendar.SECOND, 0);
                ca.set(Calendar.MILLISECOND, 0);
                time = ca.getTimeInMillis();

                Calendar calendar = Calendar.getInstance();
                calendar.set(Calendar.MINUTE, 0);
                calendar.set(Calendar.HOUR_OF_DAY, 0);
                calendar.set(Calendar.SECOND, 0);
                calendar.set(Calendar.MILLISECOND, 0);
                long curTime = calendar.getTimeInMillis();
                if (curTime > time) {
                    getRight();
                    initData();
                }
                break;
        }
    }

    private void getLeft() {
        long start = sevenDay.get(0);
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(start);
        sevenDay.clear();
        calendar.add(Calendar.DATE, -1);
        sevenDay.add(calendar.getTimeInMillis());
        calendar.add(Calendar.DATE, -6);
        sevenDay.add(calendar.getTimeInMillis());
        Collections.reverse(sevenDay);
    }

    private void getRight() {
        long time = sevenDay.get(1);
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(time);
        sevenDay.clear();
        calendar.add(Calendar.DATE, +1);
        sevenDay.add(calendar.getTimeInMillis());
        calendar.add(Calendar.DATE, +6);
        sevenDay.add(calendar.getTimeInMillis());

    }

    @Override
    public void success(String result, String type) {
        super.success(result, type);
        switch (type) {
            case "bar":
                if (!TextUtils.isEmpty(result)) {
                    String datas = JSON.parseObject(result).get("data").toString();
                    StatisticsBarRes res = JSON.parseObject(datas, StatisticsBarRes.class);
                    //加载grideview数据
                    tvValueKcacl.setText(res.getAvgCalorie());
                    tvValueKm.setText(res.getAvgKilometre());
                    tvValueStep.setText(res.getAvgStep());
                    tvValueTime.setText(DateUtils.formatTime(Long.valueOf(res.getAvgSecond())));

                    //加载柱状图
                    ArrayList<StatisticsItem> barData = res.getBarData();
                    for (int i = 0; i < singlelist.length; i++) {
                        if (i < barData.size())
                            singlelist[i] = Integer.parseInt(barData.get(i).getValue());
                        else
                            singlelist[i] = 0;
                    }
//            xValue = new String[]{"一", "二", "三", "四", "五", "六", "日"};
                    mMySingleChartView.upData(singlelist);
                    mMySingleChartView.upDateTextForX(xValue);

                }
                break;
            default:
                break;
        }
    }

    /**
     * 初始化单柱柱状图
     */
    private void initBar() {
        for (int i = 0; i < singlelist.length; i++) {
            singlelist[i] = 0;
        }
        xValue = new String[]{"一", "二", "三", "四", "五", "六", "日"};
        mMySingleChartView.upData(singlelist);
        mMySingleChartView.upDateTextForX(xValue);
    }


}
