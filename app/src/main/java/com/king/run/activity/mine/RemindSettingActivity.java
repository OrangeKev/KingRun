package com.king.run.activity.mine;

import android.app.TimePickerDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;
import android.widget.TimePicker;

import com.king.run.R;
import com.king.run.activity.mine.model.RemindData;
import com.king.run.base.BaseActivity;
import com.king.run.util.StartRemind;
import com.king.run.util.StringUtil;

import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.Event;
import org.xutils.view.annotation.ViewInject;

import java.util.Calendar;

import steps.teller.step.utils.DbUtils;

@ContentView(R.layout.activity_remind_setting)
public class RemindSettingActivity extends BaseActivity {
    private String weekRepDays = "1";
    private TimePickerDialog timePickerDialog;
    @ViewInject(R.id.tv_remind_time)
    TextView tv_remind_time;
    private int mHour, mMin;
    private String action;
    @ViewInject(R.id.btn_delete)
    Button btn_delete;
    private RemindData remindData;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        init();
        initViews();
    }

    private void initViews() {
        action = getIntent().getAction();
        switch (action) {
            case "add":
                Calendar calendar = Calendar.getInstance();
                mHour = calendar.get(Calendar.HOUR);
                mMin = calendar.get(Calendar.MINUTE);
                btn_delete.setVisibility(View.GONE);
                title_tv_right.setText(R.string.save);
                break;
            case "edit":
                remindData = (RemindData) getIntent().getExtras().getSerializable("remindData");
                mHour = remindData.getHour();
                mMin = remindData.getMin();
                weekRepDays = remindData.getRepet();
                btn_delete.setVisibility(View.VISIBLE);
                title_tv_right.setText(R.string.edit);
                break;
        }
        String h = mHour + "";
        String m = mMin + "";
        if (mHour < 10)
            h = "0" + h;
        if (mMin < 10)
            m = "0" + m;
        tv_remind_time.setText(h + ":" + m);
        title_tv_right.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (null != remindData)
                    DbUtils.delete(remindData);
                RemindData remindData = new RemindData();
                remindData.setRepet(getWeekRepDays());
                remindData.setHour(mHour);
                remindData.setMin(mMin);
                remindData.setRepetStr(getWeekRepDaysStr());
                DbUtils.insert(remindData);
                Bundle bundle = new Bundle();
                bundle.putSerializable("remindData", remindData);
                setResultAct(bundle);
                finish();
            }
        });
        title_tv_title.setText(R.string.remind_setting);
        for (int i = 1; i <= 7; i++) {
            int id = getResources().getIdentifier("week_day_" + i, "id",
                    getPackageName());
            CheckBox weekDayCheckBox = (CheckBox) findViewById(id);
            weekDayCheckBox.setOnClickListener(onWeekDayClick);
            weekDayCheckBox.setOnCheckedChangeListener(checkChange);
            if (StringUtil.isNotBlank(weekRepDays) && weekRepDays.contains(i + "")) {
                weekDayCheckBox.setChecked(true);
            }
        }
    }

    @Event(value = {R.id.ly_remind_time, R.id.btn_delete})
    private void getEvent(View view) {
        switch (view.getId()) {
            case R.id.ly_remind_time:
                TimeDialogShow();
                break;
            case R.id.btn_delete:
                AlertDialog.Builder builder = new AlertDialog.Builder(context);
                builder.setTitle(R.string.notify).setMessage(R.string.sure_to_delete).setPositiveButton(R.string.sure, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int position) {
                        DbUtils.delete(remindData);
                        StartRemind.cancel(context, remindData);
                        setResult(RESULT_OK);
                        finish();
                    }
                }).setNegativeButton(R.string.cancel, null).create().show();
                break;
        }
    }

    private void TimeDialogShow() {
        Calendar calendar = Calendar.getInstance();
        int hour = calendar.get(Calendar.HOUR_OF_DAY);
        int minute = calendar.get(Calendar.MINUTE);
        timePickerDialog = new TimePickerDialog(this, setting, hour, minute, true);
        timePickerDialog.show();
    }

    //当点击TimePickerDialog控件的设置按钮时，调用该方法
    TimePickerDialog.OnTimeSetListener setting = new TimePickerDialog.OnTimeSetListener() {
        @Override
        public void onTimeSet(TimePicker view, int hour, int minute) {
            mHour = hour;
            mMin = minute;
            String h = hour + "";
            String m = minute + "";
            if (hour < 10)
                h = "0" + h;
            if (minute < 10)
                m = "0" + m;
            tv_remind_time.setText(h + ":" + m);
        }

    };
    private CompoundButton.OnCheckedChangeListener checkChange = new CompoundButton.OnCheckedChangeListener() {// 显示隐藏选择状态的icon，因为drawableright，不能使用selector
        @Override
        public void onCheckedChanged(CompoundButton buttonView,
                                     boolean isChecked) {
            if (isChecked) {
                buttonView.setCompoundDrawablesWithIntrinsicBounds(null, null,
                        getResources().getDrawable(R.mipmap.common_icon_check_selected), null);
            } else {
                buttonView.setCompoundDrawables(null, null, null, null);
            }
//            setTV_Str();
        }
    };
    private View.OnClickListener onWeekDayClick = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            boolean isChecked = ((CheckBox) v).isChecked();
            if (!isChecked) {// 当某天取消时，自动取消week_repeatd check状态
                if (isAllWeekDayNotCheck()) {// 如果只剩最后一天，则不能取消选中状态
                    ((CheckBox) v).setChecked(true);
                    senToa(R.string.task_at_last_one_day);
                }
            }
        }
    };

    protected boolean isAllWeekDayNotCheck() {
        for (int i = 1; i <= 7; i++) {
            int id = getResources().getIdentifier("week_day_" + i, "id",
                    getPackageName());
            if (((CheckBox) findViewById(id)).isChecked()) {
                return false;
            }
        }
        return true;
    }

    private String getWeekRepDays() {
        StringBuffer sb = new StringBuffer();
        for (int i = 1; i <= 7; i++) {
            int id = getResources().getIdentifier("week_day_" + i, "id",
                    getPackageName());
            if (((CheckBox) findViewById(id)).isChecked()) {
                sb.append(i).append(",");
            }
        }
        if (sb.length() < 2) {
            return "";
        }
        return sb.substring(0, sb.length() - 1);
    }

    private String getWeekRepDaysStr() {
        StringBuffer sb = new StringBuffer();
        for (int i = 1; i <= 7; i++) {
            int id = getResources().getIdentifier("week_day_" + i, "id",
                    getPackageName());
            if (((CheckBox) findViewById(id)).isChecked()) {
                sb.append(((CheckBox) findViewById(id)).getText()).append(",");
            }
        }
        if (sb.length() < 2) {
            return "";
        }
        return sb.substring(0, sb.length() - 1);
    }
}
