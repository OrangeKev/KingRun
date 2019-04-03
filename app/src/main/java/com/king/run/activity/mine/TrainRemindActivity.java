package com.king.run.activity.mine;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.SwitchCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.CompoundButton;
import android.widget.ListView;

import com.king.run.R;
import com.king.run.activity.mine.adapter.RemindAdapter;
import com.king.run.activity.mine.model.RemindData;
import com.king.run.base.BaseActivity;
import com.king.run.util.PrefName;
import com.king.run.util.PreferencesUtils;
import com.king.run.util.StartRemind;

import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.Event;
import org.xutils.view.annotation.ViewInject;

import java.util.Calendar;
import java.util.List;

import steps.teller.step.utils.DbUtils;

@ContentView(R.layout.activity_train_remind)
public class TrainRemindActivity extends BaseActivity {
    @ViewInject(R.id.switch_train)
    SwitchCompat switch_train;
    private RemindAdapter adapter;
    @ViewInject(R.id.lv)
    ListView lv;
    private static final int ADD_CODE = 111;
    private static final int EDIT_CODE = 112;
    private List<RemindData> list;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        init();
        initViews();
    }

    private void initViews() {
        switch_train.setChecked(PrefName.getTrainSwitch(context));
        list = DbUtils.getQueryAll(RemindData.class);
        title_tv_title.setText(R.string.train_remind);
        adapter = new RemindAdapter(context, list);
        View footerView = LayoutInflater.from(context).inflate(R.layout.activity_train_remind_footer_view, null);
        footerView.findViewById(R.id.btn_add).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                jumpBundleActvityforResult(RemindSettingActivity.class, null, ADD_CODE, "add");
            }
        });
        lv.addFooterView(footerView);
        lv.setAdapter(adapter);
        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                if (i < list.size()) {
                    Bundle bundle = new Bundle();
                    bundle.putSerializable("remindData", list.get(i));
                    jumpBundleActvityforResult(RemindSettingActivity.class, bundle, EDIT_CODE, "edit");
                }
            }
        });
        switch_train.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                PreferencesUtils.putBoolean(context, PrefName.TRAIN_SWITCH, b);
            }
        });
    }

    @Event(value = {R.id.ly_remind_type})
    private void getEvent(View view) {
        switch (view.getId()) {
            case R.id.ly_remind_type:
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode != RESULT_OK) return;
        switch (requestCode) {
            case ADD_CODE:
                RemindData remindData = (RemindData) data.getExtras().getSerializable("remindData");
                list.clear();
                list = DbUtils.getQueryAll(RemindData.class);
                adapter.setList(list);
                adapter.notifyDataSetChanged();
                Calendar calendar = Calendar.getInstance();
                int week = calendar.get(Calendar.DAY_OF_WEEK);
                if (remindData.getRepet().contains(week + "") && PrefName.getTrainSwitch(context)) {
                    StartRemind.start(context, remindData);
                }
                break;
            case EDIT_CODE:
                list.clear();
                list = DbUtils.getQueryAll(RemindData.class);
                adapter.setList(list);
                adapter.notifyDataSetChanged();
                break;
        }
    }


}
