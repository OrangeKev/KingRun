package com.king.run.activity.statistics;

import android.app.Activity;
import android.graphics.Matrix;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.king.run.R;
import com.king.run.base.BaseActivity;
import com.king.run.util.StepAlgorithm;
import com.king.run.util.TimeUtil;
import com.king.run.util.Utils;
import com.king.run.view.LineChartManager;

import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.ViewInject;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@ContentView(R.layout.activity_speed_details)
public class SpeedDetailsActivity extends BaseActivity {
    @ViewInject(R.id.chart)
    LineChart mLineChart;
    @ViewInject(R.id.tv_time)
    TextView tv_time;
    @ViewInject(R.id.tv_second)
    TextView tv_second;
    @ViewInject(R.id.tv_km)
    TextView tv_km;
    @ViewInject(R.id.tv_average_speed)
    TextView tv_average_speed;
    @ViewInject(R.id.tv_min_speed)
    TextView tv_min_speed;
    @ViewInject(R.id.tv_max_speed)
    TextView tv_max_speed;
    private ArrayList<String> xValues = new ArrayList<>();
    private ArrayList<Entry> yValue = new ArrayList<>();
    private ArrayList<Float> value = new ArrayList<>();
    private long time;
    private double distance;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        init();
        initViews();
    }

    private void initViews() {
        xValues = getIntent().getExtras().getStringArrayList("xValues");
        yValue = getIntent().getExtras().getParcelableArrayList("yValue");
        time = getIntent().getExtras().getLong("time");
        distance = getIntent().getExtras().getDouble("distance");
        title_relate_layout.setBackgroundColor(ContextCompat.getColor(context, R.color.bg_262626));
        title_iv_back.setImageResource(R.mipmap.common_icon_back_white);
        title_tv_title.setTextColor(ContextCompat.getColor(context, R.color.white_color));
        title_tv_title.setText(R.string.speed_details);
        int height = Utils.getDisplayHeight((Activity) context);
        RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) mLineChart.getLayoutParams();
        params.height = height / 2;
        mLineChart.setLayoutParams(params);
        //设置图表的描述
        mLineChart.setDescription("");
        //设置x轴的数据
        tv_time.setText(TimeUtil.getSimpleFullDateStr(System.currentTimeMillis()));
        tv_second.setText(TimeUtil.getSceondsToTime(time / 1000));
        float num = (float) distance / 1000;
        DecimalFormat df = new DecimalFormat("0.00");
        tv_km.setText(df.format(num));
        tv_average_speed.setText("平均:" + StepAlgorithm.getSpeedByDis(distance, time / 1000));

//        xValues.add("0.1");
//        xValues.add("0.2");
//        xValues.add("0.3");
//        xValues.add("0.4");
//        xValues.add("0.5");
//        xValues.add("0.6");
//        xValues.add("0.7");
//
//        yValue.add(new Entry((float) 583.0, 0));
//        yValue.add(new Entry((float) 651.0, 1));
//        yValue.add(new Entry((float) 622.0, 2));
//        yValue.add(new Entry((float) 653.0, 3));
//        yValue.add(new Entry((float) 640.0, 4));
//        yValue.add(new Entry((float) 653.0, 5));
//        yValue.add(new Entry((float) 626.0, 6));
//        xValues.add("0.8");
//        xValues.add("0.9");
//        xValues.add("0.10");
//        xValues.add("0.11");
//        xValues.add("0.12");
//        xValues.add("0.13");
//        xValues.add("0.14");
//        xValues.add("0.15");
//        xValues.add("0.16");
//        //设置y轴的数据
        //设置折线的名称
        LineChartManager.setLineName("");
        //创建两条折线的图表
        LineChartManager.initDoubleLineChart(context, mLineChart, xValues, yValue, null);
        if (yValue.size() > 5) {
            Matrix m = new Matrix();
            m.postScale(2f, 1f);//两个参数分别是x,y轴的缩放比例。例如：将x轴的数据放大为之前的1.5倍
            mLineChart.getViewPortHandler().refresh(m, mLineChart, false);//将图表动画显示之前进行缩放
            mLineChart.animateX(1000); // 立即执行的动画,x轴
            mLineChart.moveViewToX(0);
        }
        for (int i = 0; i < yValue.size(); i++) {
            value.add(yValue.get(i).getVal());
        }
        float max = Collections.max(value);
        float min = Collections.min(value);
        tv_max_speed.setText("最快:" + StepAlgorithm.getSpeedStr((int) min));
        tv_min_speed.setText("最慢:" + StepAlgorithm.getSpeedStr((int) max));

    }
}
