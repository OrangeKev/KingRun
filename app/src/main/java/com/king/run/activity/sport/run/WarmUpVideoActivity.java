package com.king.run.activity.sport.run;

import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;

import com.king.run.R;
import com.king.run.activity.sport.walk.adapter.VideoInfoListAdapter;
import com.king.run.base.BaseActivity;
import com.king.run.util.Utils;

import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.ViewInject;

@ContentView(R.layout.activity_warm_up_video)
public class WarmUpVideoActivity extends BaseActivity {
    @ViewInject(R.id.iv_top_bg)
    ImageView iv_top_bg;
    @ViewInject(R.id.lv_video)
    ListView lv_video;
    VideoInfoListAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        init();
        initViews();
    }

    private void initViews() {
        title_iv_back.setBackgroundResource(R.mipmap.common_icon_back_white);
        title_relate_layout.setBackgroundColor(ContextCompat.getColor(context, R.color.bg_filly_transparent));
        RelativeLayout.LayoutParams layoutParams1 = (RelativeLayout.LayoutParams) iv_top_bg.getLayoutParams();
        layoutParams1.width = (Utils.getDisplayWidth(WarmUpVideoActivity.this));
        layoutParams1.height = Utils.getDisplayWidth(WarmUpVideoActivity.this) * 5 / 9;
        iv_top_bg.setLayoutParams(layoutParams1);
        iv_top_bg.setBackgroundResource(R.mipmap.train_run_img_warmup);
        adapter = new VideoInfoListAdapter(context);
        lv_video.setAdapter(adapter);
    }
}
