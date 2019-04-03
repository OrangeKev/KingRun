package com.king.run.view;

import android.content.Context;
import android.graphics.Color;
import android.support.v4.content.ContextCompat;
import android.view.MotionEvent;
import android.view.View;

import com.github.mikephil.charting.animation.Easing;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.listener.ChartTouchListener;
import com.king.run.R;

import java.util.ArrayList;

/**
 * 作者：shuizi_wade on 2017/12/19 18:09
 * 邮箱：674618016@qq.com
 */
public class LineChartManager {
    private static String lineName = null;
    private static String lineName1 = null;

    /**
     * @param context    上下文
     * @param mLineChart 折线图控件
     * @param xValues    折线在x轴的值
     * @param yValue     折线在y轴的值
     * @Description:创建两条折线
     */
    public static void initSingleLineChart(Context context, LineChart mLineChart, ArrayList<String> xValues,
                                           ArrayList<Entry> yValue) {
        initDataStyle(context, mLineChart);
        //设置折线的样式
        LineDataSet dataSet = new LineDataSet(yValue, lineName);
        dataSet.setColor(Color.RED);
        dataSet.setCircleColor(Color.RED);
        dataSet.setDrawValues(false);
//        dataSet.setValueFormatter(new PercentFormatter(new DecimalFormat("%").format()));

        ArrayList<LineDataSet> dataSets = new ArrayList<>();
        dataSets.add(dataSet);

        //构建一个LineData  将dataSets放入
        LineData lineData = new LineData(xValues, dataSets);
        //将数据插入
        mLineChart.setData(lineData);

        //设置动画效果
        mLineChart.animateY(2000, Easing.EasingOption.Linear);
        mLineChart.animateX(2000, Easing.EasingOption.Linear);
        mLineChart.invalidate();
    }

    /**
     * @param context    上下文
     * @param mLineChart 折线图控件
     * @param xValues    折线在x轴的值
     * @param yValue     折线在y轴的值
     * @param yValue1    另一条折线在y轴的值
     * @Description:创建两条折线
     */
    public static void initDoubleLineChart(Context context, LineChart mLineChart, ArrayList<String> xValues,
                                           ArrayList<Entry> yValue, ArrayList<Entry> yValue1) {

        initDataStyle(context, mLineChart);

        LineDataSet dataSet = new LineDataSet(yValue, lineName);
        int[] ints = new int[]{R.color.green_00ff22, R.color.yellow_f4ff24, R.color.red_ff1607};
        dataSet.setColors(ints, context);
//        dataSet.setColor(Color.GREEN);
        dataSet.setLineWidth(3);
        dataSet.setDrawCircles(false);
//        dataSet.setCircleColor(Color.RED);
        dataSet.setDrawValues(false);
        dataSet.setDrawCubic(true);

//        LineDataSet dataSet1 = new LineDataSet(yValue1, lineName1);
//        dataSet.enableDashedLine(0f, 50f, 0f);//将折线设置为曲线
//        dataSet1.setColor(Color.parseColor("#66CDAA"));
//        dataSet1.setCircleColor(Color.parseColor("#66CDAA"));
//        dataSet1.setDrawValues(false);
//        dataSet1.setDrawCubic(true);

        //构建一个类型为LineDataSet的ArrayList 用来存放所有 y的LineDataSet   他是构建最终加入LineChart数据集所需要的参数
        ArrayList<LineDataSet> dataSets = new ArrayList<>();

        //将数据加入dataSets
        dataSets.add(dataSet);
//        dataSets.add(dataSet1);

        //构建一个LineData  将dataSets放入
        LineData lineData = new LineData(xValues, dataSets);
        //将数据插入
        mLineChart.setData(lineData);
        //设置动画效果
        mLineChart.animateY(2000, Easing.EasingOption.Linear);
        mLineChart.animateX(2000, Easing.EasingOption.Linear);
        mLineChart.invalidate();
    }

    /**
     * @param context
     * @param mLineChart
     * @Description:初始化图表的样式
     */
    private static void initDataStyle(Context context, LineChart mLineChart) {
        //设置图表是否支持触控操作
        mLineChart.setTouchEnabled(true);
        mLineChart.setScaleEnabled(false);
        mLineChart.setHighlightPerTapEnabled(false);
        mLineChart.setGridBackgroundColor(ContextCompat.getColor(context, R.color.bg_262626));
        //设置点击折线点时，显示其数值
//        MyMakerView mv = new MyMakerView(context, R.layout.item_mark_layout);
//        mLineChart.setMarkerView(mv);
        //设置折线的描述的样式（默认在图表的左下角）
        Legend title = mLineChart.getLegend();
//        title.setForm(Legend.LegendForm.LINE);
//        title.setWordWrapEnabled(false);
        title.setEnabled(false);
        //设置x轴的样式
        XAxis xAxis = mLineChart.getXAxis();
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        xAxis.setAxisLineColor(ContextCompat.getColor(context, R.color.white));
        xAxis.setAxisLineWidth(0);
        xAxis.setDrawGridLines(true);
        xAxis.setGridColor(ContextCompat.getColor(context, R.color.white));
        //设置是否显示x轴
        xAxis.setEnabled(true);
        xAxis.setTextColor(ContextCompat.getColor(context, R.color.white_color));
        xAxis.setSpaceBetweenLabels(5);

        //设置左边y轴的样式
        YAxis yAxisLeft = mLineChart.getAxisLeft();
        yAxisLeft.setAxisLineColor(Color.parseColor("#66CDAA"));
        yAxisLeft.setAxisLineWidth(5);
        yAxisLeft.setDrawGridLines(false);
        yAxisLeft.setEnabled(false);
        //设置右边y轴的样式
        YAxis yAxisRight = mLineChart.getAxisRight();
        yAxisRight.setEnabled(false);

    }

    /**
     * @param name
     * @Description:设置折线的名称
     */
    public static void setLineName(String name) {
        lineName = name;
    }

    /**
     * @param name
     * @Description:设置另一条折线的名称
     */
    public static void setLineName1(String name) {
        lineName1 = name;
    }
}
