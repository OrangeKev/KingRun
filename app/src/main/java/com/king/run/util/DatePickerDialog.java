package com.king.run.util;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.res.Resources;
import android.os.Build;
import android.view.Gravity;
import android.view.View;
import android.widget.DatePicker;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;


import com.king.run.R;

import java.lang.reflect.Field;
import java.util.Calendar;

public class DatePickerDialog extends AlertDialog implements
        OnClickListener {

    private ValueSetCallback valueSetCallback;

    public interface ValueSetCallback {
        void setValue(Calendar calendar);
    }

    protected DatePickerDialog(Context context) {
        super(context);
    }

    protected DatePickerDialog(Context context, int theme) {
        super(context, theme);
    }

    protected DatePickerDialog(Context context, boolean cancelable,
                               OnCancelListener cancelListener) {
        super(context, cancelable, cancelListener);
    }

    private DatePicker datePicker;

    public DatePickerDialog(Context context, Calendar calendar,
                            ValueSetCallback valueSetCallback) {
        super(context);
        if (calendar != null) {
            this.valueSetCallback = valueSetCallback;
            LinearLayout layout = new LinearLayout(context);
            layout.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT,
                    LayoutParams.MATCH_PARENT));
            layout.setOrientation(LinearLayout.VERTICAL);
            layout.setGravity(Gravity.CENTER_HORIZONTAL);
            LayoutParams params = new LayoutParams(LayoutParams.WRAP_CONTENT,
                    LayoutParams.WRAP_CONTENT);
            datePicker = (DatePicker) View.inflate(context, R.layout.datepicker_layout, null);
            datePicker.init(calendar.get(Calendar.YEAR),
                    calendar.get(Calendar.MONTH),
                    calendar.get(Calendar.DAY_OF_MONTH), null);
            datePicker.setLayoutParams(params);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
                datePicker.setCalendarViewShown(false);
            }
            layout.addView(datePicker, 0);
            hidePicker(datePicker);
            setView(layout);
            setButton(BUTTON_POSITIVE,
                    context.getString(android.R.string.cancel),
                    (OnClickListener) null);
            setButton(BUTTON_NEGATIVE, context.getString(android.R.string.ok),
                    this);
        }
    }

    private Calendar getValue() {
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.YEAR, datePicker.getYear());
        calendar.set(Calendar.MONTH, datePicker.getMonth());
        calendar.set(Calendar.DAY_OF_MONTH, datePicker.getDayOfMonth());
        calendar.set(calendar.HOUR_OF_DAY, 0);
        calendar.set(calendar.MINUTE, 0);
        calendar.set(calendar.SECOND, 0);
        calendar.set(calendar.MILLISECOND, 0);
        return calendar;
    }

    @Override
    public void onClick(DialogInterface dialog, int which) {
        if (valueSetCallback != null) {
            valueSetCallback.setValue(getValue());
        }
    }

    public DatePickerDialog(Context context, Calendar calendar, boolean isTime,
                            ValueSetCallback valueSetCallback) {
        super(context);
        if (calendar != null) {
            this.valueSetCallback = valueSetCallback;
            LinearLayout layout = new LinearLayout(context);
            layout.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT,
                    LayoutParams.MATCH_PARENT));
            layout.setOrientation(LinearLayout.VERTICAL);
            layout.setGravity(Gravity.CENTER_HORIZONTAL);
            LayoutParams params = new LayoutParams(LayoutParams.WRAP_CONTENT,
                    LayoutParams.WRAP_CONTENT);
            datePicker = (DatePicker) View.inflate(context, R.layout.datepicker_layout, null);
            datePicker.init(calendar.get(Calendar.YEAR),
                    calendar.get(Calendar.MONTH),
                    calendar.get(Calendar.DAY_OF_MONTH), null);
            datePicker.setLayoutParams(params);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
                datePicker.setCalendarViewShown(false);
            }
            layout.addView(datePicker, 0);

            setView(layout);
            setButton(BUTTON_POSITIVE,
                    context.getString(android.R.string.cancel),
                    (OnClickListener) null);
            setButton(BUTTON_NEGATIVE, context.getString(android.R.string.ok),
                    this);
        }
    }

    public static void hidePicker(DatePicker picker) {
        // 利用java反射技术得到picker内部的属性，并对其进行操作
        Class<? extends DatePicker> c = picker.getClass();
        try {
            Field fd = null, fm = null, fy = null;
            // 系统版本大于5.0
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                int daySpinnerId = Resources.getSystem().getIdentifier("day", "id", "android");
                if (daySpinnerId != 0) {
                    View daySpinner = picker.findViewById(daySpinnerId);
                    if (daySpinner != null) {
                        daySpinner.setVisibility(View.GONE);
                        return;
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
            View vd = (View) fd.get(picker);
            View vm = (View) fm.get(picker);
            View vy = (View) fy.get(picker);

            vd.setVisibility(View.VISIBLE);
            vm.setVisibility(View.VISIBLE);
            vy.setVisibility(View.VISIBLE);
        } catch (NoSuchFieldException e) {
            e.printStackTrace();
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
    }
}
