package com.king.run.activity.posture;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ListView;

import com.king.run.R;
import com.king.run.activity.posture.adapter.MyShoesAdapter;
import com.king.run.activity.posture.model.ShoeInfo;
import com.king.run.activity.posture.model.iBeaconClass;
import com.king.run.base.BaseActivity;
import com.king.run.view.RelieveDialog;

import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.Event;
import org.xutils.view.annotation.ViewInject;

import java.util.ArrayList;
import java.util.List;

import steps.teller.step.utils.DbUtils;

@ContentView(R.layout.activity_my_shoes)
public class MyShoesActivity extends BaseActivity {
    private MyShoesAdapter adapter;
    @ViewInject(R.id.lv)
    ListView lv;
    private List<iBeaconClass.iBeacon> list = new ArrayList<>();
    private static final int EDIT_NAME_CODE = 123;
    private static final int ADD_CODE = 111;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        init();
        initViews();
    }

    private void initViews() {
        title_tv_title.setText(R.string.my_shoes);
        list = DbUtils.getQueryAll(iBeaconClass.iBeacon.class);
        adapter = new MyShoesAdapter(context, list);
        lv.setAdapter(adapter);
        adapter.setOnRelieveClickListener(new MyShoesAdapter.onRelieveClickListener() {
            @Override
            public void onClickListener(final int position) {
                new RelieveDialog(context, new RelieveDialog.ClickListener() {
                    @Override
                    public void relieve() {
                        list.get(position).setConnect(false);
                        adapter.setList(list);
                        adapter.notifyDataSetChanged();
                        DbUtils.updateALL(list);
                    }
                }).show();
            }
        });
        adapter.setOnEditNameClickListener(new MyShoesAdapter.OnEditNameClickListener() {
            @Override
            public void editClick(int position) {
                Bundle bundle = new Bundle();
                bundle.putSerializable("iBeacon", list.get(position));
                jumpBundleActvityforResult(ChangeNameActivity.class, bundle, EDIT_NAME_CODE);
            }
        });
    }

    @Event(value = {R.id.btn_add})
    private void getEvent(View view) {
        switch (view.getId()) {
            case R.id.btn_add:
                jumpBundleActvityforResult(ConnectDeviceActivity.class, null, ADD_CODE, "list");
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode != RESULT_OK) return;
        switch (requestCode) {
            case ADD_CODE:
                list.clear();
                list = DbUtils.getQueryAll(iBeaconClass.iBeacon.class);
                adapter.setList(list);
                adapter.notifyDataSetChanged();
                break;
            case EDIT_NAME_CODE:
                iBeaconClass.iBeacon iBeacon = (iBeaconClass.iBeacon) data.getExtras().getSerializable("iBeacon");
                DbUtils.update(iBeacon);
                list.clear();
                list = DbUtils.getQueryAll(iBeaconClass.iBeacon.class);
                adapter.setList(list);
                adapter.notifyDataSetChanged();
                break;
        }
    }
}
