package com.king.run.activity.posture;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;

import com.king.run.R;
import com.king.run.activity.posture.model.iBeaconClass;
import com.king.run.base.BaseActivity;
import com.king.run.util.StringUtil;

import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.Event;
import org.xutils.view.annotation.ViewInject;

@ContentView(R.layout.activity_change_name)
public class ChangeNameActivity extends BaseActivity {
    @ViewInject(R.id.et_name)
    EditText et_name;
    @ViewInject(R.id.iv_del)
    ImageView iv_del;
    private iBeaconClass.iBeacon iBeacon;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        init();
        initViews();
    }

    private void initViews() {
        iBeacon = (iBeaconClass.iBeacon) getIntent().getExtras().getSerializable("iBeacon");
        title_tv_right.setText(R.string.save);
        title_tv_title.setText(R.string.change_name);
        et_name.setText(iBeacon.getName());
        title_iv_right.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String name = et_name.getText().toString().trim();
                if (StringUtil.isBlank(name)) {
                    et_name.setError("名称不能为空！");
                    return;
                }
                iBeacon.setName(name);
                Bundle bundle = new Bundle();
                bundle.putSerializable("iBeacon", iBeacon);
                setResultAct(bundle);
                finish();
            }
        });
    }

    @Event(value = {R.id.iv_del})
    private void getEvent(View view) {
        switch (view.getId()) {
            case R.id.iv_del:
                et_name.setText("");
                break;
        }
    }
}
