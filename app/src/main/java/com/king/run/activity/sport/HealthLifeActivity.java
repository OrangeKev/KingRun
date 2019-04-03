package com.king.run.activity.sport;

import android.os.Bundle;

import com.king.run.R;
import com.king.run.base.BaseActivity;

import org.xutils.view.annotation.ContentView;

@ContentView(R.layout.activity_health_life)
public class HealthLifeActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        init();
        initViews();
    }

    private void initViews() {
        title_iv_right.setBackgroundResource(R.mipmap.train_health_icon_collect_on);
    }
}
