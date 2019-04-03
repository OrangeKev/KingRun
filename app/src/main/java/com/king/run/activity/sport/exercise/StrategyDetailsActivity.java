package com.king.run.activity.sport.exercise;

import android.os.Bundle;
import android.support.v4.content.ContextCompat;

import com.king.run.R;
import com.king.run.base.BaseActivity;

import org.xutils.view.annotation.ContentView;

@ContentView(R.layout.activity_strategy_details)
public class StrategyDetailsActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        init();
        initViews();
    }

    private void initViews() {
        title_iv_back.setBackgroundResource(R.mipmap.common_icon_back_white);
        title_relate_layout.setBackgroundColor(ContextCompat.getColor(context, R.color.bg_filly_transparent));
    }
}
