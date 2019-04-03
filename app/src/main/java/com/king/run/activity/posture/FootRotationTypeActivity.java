package com.king.run.activity.posture;

import android.app.Activity;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.king.run.R;
import com.king.run.base.BaseActivity;
import com.king.run.util.Utils;

import org.xutils.common.util.DensityUtil;
import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.ViewInject;

@ContentView(R.layout.activity_foot_rotation_type)
public class FootRotationTypeActivity extends BaseActivity {
    @ViewInject(R.id.iv_foot_in)
    ImageView iv_foot_in;
    @ViewInject(R.id.iv_foot_out)
    ImageView iv_foot_out;
    @ViewInject(R.id.iv_follow_ground)
    ImageView iv_follow_ground;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        init();
        initViews();
    }

    private void initViews() {
        title_tv_title.setText("足旋转类型");
        int width = Utils.getDisplayWidth((Activity) context);
        LinearLayout.LayoutParams p = (LinearLayout.LayoutParams) iv_foot_in.getLayoutParams();
        p.height = (int) ((width - DensityUtil.dip2px(15) * 2) * 0.28);
        iv_foot_in.setLayoutParams(p);
        LinearLayout.LayoutParams p1 = (LinearLayout.LayoutParams) iv_foot_out.getLayoutParams();
        p1.height = (int) ((width - DensityUtil.dip2px(15) * 2) * 0.28);
        iv_foot_out.setLayoutParams(p1);
        LinearLayout.LayoutParams p2 = (LinearLayout.LayoutParams) iv_follow_ground.getLayoutParams();
        p2.height = (int) ((width - DensityUtil.dip2px(15) * 2) * 0.28);
        iv_follow_ground.setLayoutParams(p2);
    }
}
