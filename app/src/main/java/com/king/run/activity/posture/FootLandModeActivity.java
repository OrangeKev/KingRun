package com.king.run.activity.posture;

import android.app.Activity;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.king.run.R;
import com.king.run.base.BaseActivity;
import com.king.run.util.Utils;

import org.xutils.common.util.DensityUtil;
import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.ViewInject;

@ContentView(R.layout.activity_foot_land_mode)
public class FootLandModeActivity extends BaseActivity {
    @ViewInject(R.id.iv_mode)
    ImageView iv_mode;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        init();
        initViews();
    }

    private void initViews() {
        title_tv_title.setText("足落地方式");
        int width = Utils.getDisplayWidth((Activity) context);
        RelativeLayout.LayoutParams p = (RelativeLayout.LayoutParams) iv_mode.getLayoutParams();
        p.height = (int) ((width - DensityUtil.dip2px(15) * 2) * 0.28);
        iv_mode.setLayoutParams(p);
    }
}
