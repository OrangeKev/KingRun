package com.king.run.activity.posture;

import android.graphics.Typeface;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.king.run.R;
import com.king.run.base.BaseActivity;

import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.Event;
import org.xutils.view.annotation.ViewInject;

@ContentView(R.layout.activity_run_posture)
public class RunPostureActivity extends BaseActivity {
    @ViewInject(R.id.tv_percent_half_sole)
    TextView tv_percent_half_sole;
    @ViewInject(R.id.tv_percent_mid_leg)
    TextView tv_percent_mid_leg;
    @ViewInject(R.id.tv_percent_heelpiece)
    TextView tv_percent_heelpiece;
    @ViewInject(R.id.tv_percent_foot_in)
    TextView tv_percent_foot_in;
    @ViewInject(R.id.tv_percent_foot_out)
    TextView tv_percent_foot_out;
    @ViewInject(R.id.tv_percent_normal)
    TextView tv_percent_normal;
    @ViewInject(R.id.tv_high_cm)
    TextView tv_high_cm;
    @ViewInject(R.id.tv_newton)
    TextView tv_newton;
    @ViewInject(R.id.tv_bei)
    TextView tv_bei;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        init();
        initViews();
    }

    private void initViews() {
        Typeface typeface = Typeface.createFromAsset(getAssets(), "fonts/ReductoCondSSK.ttf");
        tv_percent_half_sole.setTypeface(typeface);
        tv_percent_mid_leg.setTypeface(typeface);
        tv_percent_heelpiece.setTypeface(typeface);
        tv_percent_foot_in.setTypeface(typeface);
        tv_percent_foot_out.setTypeface(typeface);
        tv_percent_normal.setTypeface(typeface);
        tv_high_cm.setTypeface(typeface);
        tv_newton.setTypeface(typeface);
        tv_bei.setTypeface(typeface);
        title_tv_title.setText(R.string.normal_posture);
    }

    @Event(value = {R.id.ly_land_mode, R.id.ly_flip_type})
    private void getEvent(View view) {
        switch (view.getId()) {
            case R.id.ly_land_mode:
                jumpActvity(FootLandModeActivity.class);
                break;
            case R.id.ly_flip_type:
                jumpActvity(FootRotationTypeActivity.class);
                break;
        }
    }
}
