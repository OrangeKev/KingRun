package com.king.run.activity.music;

import android.app.Activity;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.dd.plist.NSArray;
import com.dd.plist.NSDictionary;
import com.dd.plist.PropertyListParser;
import com.king.run.R;
import com.king.run.activity.ViewPagerAdapter;
import com.king.run.activity.music.model.MusicDetails;
import com.king.run.activity.music.model.MusicInfo;
import com.king.run.base.BaseActivity;
import com.king.run.util.PicassoUtil;
import com.king.run.util.Utils;
import com.orhanobut.logger.Logger;

import org.xutils.common.util.DensityUtil;
import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.Event;
import org.xutils.view.annotation.ViewInject;

import java.util.ArrayList;
import java.util.List;

@ContentView(R.layout.activity_play_music)
public class PlayMusicActivity extends BaseActivity {
    @ViewInject(R.id.gallery_viewpager)
    private ViewPager viewPager;
    private ViewPagerAdapter adapter;
    private int img[] = {R.mipmap.train_share_pic_1, R.mipmap.train_share_pic_2, R.mipmap.train_share_pic_3, R.mipmap.train_share_pic_4, R.mipmap.train_share_pic_5};
    @ViewInject(R.id.relativelayout)
    LinearLayout linearLayout;
    private int pagerWidth;
    @ViewInject(R.id.tv_slow_run_name)
    TextView tv_slow_run_name;
    @ViewInject(R.id.tv_pace_name)
    TextView tv_pace_name;
    @ViewInject(R.id.tv_type_name)
    TextView tv_type_name;
    @ViewInject(R.id.tv_in_use)
    TextView tv_in_use;
    @ViewInject(R.id.tv_pace_music)
    TextView tv_pace_music;
    @ViewInject(R.id.tv_set_model)
    TextView tv_set_model;
    @ViewInject(R.id.tv_model_type)
    TextView tv_model_type;
    private int pos = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        init();
        getMusicInfo();
        initViews();
    }

    private void initViews() {
        int width = Utils.getDisplayWidth((Activity) context);
        Typeface typeRegular = Typeface.createFromAsset(getAssets(), "fonts/PingFang-SC-Regular.otf");
        Typeface typeSemibold = Typeface.createFromAsset(getAssets(), "fonts/PingFang-SC-Semibold.ttf");
        tv_slow_run_name.setTypeface(typeSemibold);
        tv_pace_name.setTypeface(typeSemibold);
        tv_type_name.setTypeface(typeRegular);
        tv_in_use.setTypeface(typeRegular);
        tv_pace_music.setTypeface(typeRegular);
        tv_set_model.setTypeface(typeRegular);
        tv_model_type.setTypeface(typeRegular);
        title_iv_back.setImageResource(R.mipmap.common_icon_back_white);
        title_relate_layout.setBackgroundColor(ContextCompat.getColor(context, R.color.transparency_color));
        title_tv_title.setText(R.string.walk_music);
        title_tv_title.setTextColor(ContextCompat.getColor(context, R.color.white_color));
        adapter = new ViewPagerAdapter(context, musicList);
        pagerWidth = (int) (width * 0.611);
        viewPager.measure(0, 0);
        setText();
        ViewGroup.LayoutParams lp = viewPager.getLayoutParams();
        if (lp == null) {
            lp = new ViewGroup.LayoutParams(pagerWidth, pagerWidth);
        } else {
            lp.width = pagerWidth;
            lp.height = pagerWidth;
        }
        viewPager.setLayoutParams(lp);//设置页面宽度为屏幕的3/5
        viewPager.setOffscreenPageLimit(img.length);  //设置ViewPager至多缓存4个Pager页面，防止多次加载
        viewPager.setPageMargin((int) (width * 0.1));  //设置Pager之间的间距
        viewPager.setAdapter(adapter);
        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                if (viewPager != null) {
                    viewPager.invalidate();  //更新超出区域页面，否则会出现页面缓存，导致页面效果不佳
                }
            }

            @Override
            public void onPageSelected(int position) {
                pos = position;
                setText();
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }

    private void setText() {
        tv_slow_run_name.setText(musicList.get(pos).getTitle());
        tv_pace_name.setText(musicList.get(pos).getStep());
        tv_type_name.setText(musicList.get(pos).getDesc());
    }

    @Event(value = {R.id.ly_mode})
    private void getEvent(View view) {
        switch (view.getId()) {
            case R.id.ly_mode:
                jumpActvity(MusicModeSettingActivity.class);
                break;
        }
    }
}
