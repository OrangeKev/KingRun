package com.king.run.util;

import android.app.Dialog;
import android.content.Context;
import android.content.res.Resources;
import android.os.Build;
import android.view.View;
import android.widget.DatePicker;

import com.king.run.R;

import java.lang.reflect.Field;
import java.util.Calendar;

/**
 * Email 674618016@qq.com
 * Created by shuiz_wade on 2017/9/3.
 */

public class TimeDialog extends Dialog {

    private DatePicker datePicker;
    private int type = 3;  //1表示只显示年； 2表示显示年月 ;3表示显示年月日

    //设置监听 时间
    public interface myTimeListener {
        void timeListener(String time);
    }

    public myTimeListener iTimeListener = null;

    public void setTimeListener(myTimeListener listener) {
        iTimeListener = listener;
    }

    /*
    1表示只显示年； 2表示显示年月 ;3表示显示年月日
     */
    public TimeDialog(Context context, int year) {
        super(context);
        // TODO Auto-generated constructor stub
        setContentView(R.layout.datepicker_layout);
        initView(year);

    }

    /*
    1表示只显示年； 2表示显示年月 ;3表示显示年月日
     */
    public TimeDialog(Context context, int type, int year, myTimeListener myTimeListener) {
        super(context);
        // TODO Auto-generated constructor stub
        setContentView(R.layout.datepicker_layout);
        this.type = type;
        this.iTimeListener = myTimeListener;
        initView(year);

    }

    private void initView(int year) {
        datePicker = (DatePicker) findViewById(R.id.dialog_date_datePicker);
        Calendar calendar = Calendar.getInstance();
        datePicker.init(year, calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH), new DatePicker.OnDateChangedListener() {
            @Override
            public void onDateChanged(DatePicker datePicker, int i, int i1, int i2) {

            }
        });
        if (type != 3) {
            hidePicker();
        }
        findViewById(R.id.tv_sure).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // 回调  日期
                iTimeListener.timeListener(datePicker.getYear() + "");
                dismiss();
            }
        });
        findViewById(R.id.tv_cancel).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dismiss();
            }
        });
    }

    /**
     * 传入一个DatePicker对象,隐藏或者显示相应的时间项
     */
    private void hidePicker() {
        // 利用java反射技术得到picker内部的属性，并对其进行操作
        Class<? extends DatePicker> c = datePicker.getClass();
        try {
            Field fd = null, fm = null, fy = null;
            // 系统版本大于5.0
            if (Build.VERSION.SDK_INT >= 5) {
                int daySpinnerId = Resources.getSystem().getIdentifier("day", "id", "android");
                int monthSpinnerId = Resources.getSystem().getIdentifier("month", "id", "android");
                //      int yearSpinnerId = Resources.getSystem().getIdentifier("year", "id", "android");
                //隐藏日
                if (daySpinnerId != 0) {
                    View daySpinner = datePicker.findViewById(daySpinnerId);
                    View monthSpinner = datePicker.findViewById(monthSpinnerId);
                    if (daySpinner != null && monthSpinner != null) {
                        daySpinner.setVisibility(View.GONE);
                        monthSpinner.setVisibility(View.GONE);
                        return;
                    }
                }
                //隐藏月
                if (type == 1) {
                    if (daySpinnerId != 0) {
                        View monthSpinner = datePicker.findViewById(monthSpinnerId);
                        if (monthSpinner != null) {
                            monthSpinner.setVisibility(View.GONE);
                            return;
                        }
                    }
                }

            } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.ICE_CREAM_SANDWICH) {
                // 系统版本大于4.0
                fd = c.getDeclaredField("mDaySpinner");
                fm = c.getDeclaredField("mMonthSpinner");
                fy = c.getDeclaredField("mYearSpinner");
            } else {
                fd = c.getDeclaredField("mDayPicker");
                fm = c.getDeclaredField("mMonthPicker");
                fy = c.getDeclaredField("mYearPicker");
            }
            // 对字段获取设置权限
            fd.setAccessible(true);
            fm.setAccessible(true);
            fy.setAccessible(true);
            // 得到对应的控件
            View vd = (View) fd.get(datePicker);
            View vm = (View) fm.get(datePicker);
            View vy = (View) fy.get(datePicker);

            if (type == 2) {
                vd.setVisibility(View.GONE);
                vm.setVisibility(View.VISIBLE);
                vy.setVisibility(View.VISIBLE);
            } else if (type == 1) {
                vd.setVisibility(View.GONE);
                vm.setVisibility(View.GONE);
                vy.setVisibility(View.VISIBLE);
            } else {
                vd.setVisibility(View.VISIBLE);
                vm.setVisibility(View.VISIBLE);
                vy.setVisibility(View.VISIBLE);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
