package com.king.run.activity.sport;

import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.HorizontalScrollView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.baidu.location.BDLocation;
import com.king.run.R;
import com.king.run.activity.music.PlayMusicActivity;
import com.king.run.activity.other.HomeActivity;
import com.king.run.activity.sport.adapter.MyFragmentPagerAdapter;
import com.king.run.activity.sport.exercise.ExerciseActivity;
import com.king.run.activity.sport.exercise.ExerciseFragment;
import com.king.run.activity.sport.ride.RideFragment;
import com.king.run.activity.sport.run.RunFragment;
import com.king.run.activity.sport.walk.WalkFragment;
import com.king.run.baidumap.LocManage;
import com.king.run.model.Weather;
import com.king.run.model.http.res.ExerciseRes;
import com.king.run.util.Url;
import com.king.run.util.Utils;
import com.orhanobut.logger.Logger;

import org.xutils.common.Callback;
import org.xutils.common.util.DensityUtil;
import org.xutils.http.RequestParams;
import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.Event;
import org.xutils.view.annotation.ViewInject;
import org.xutils.x;

import java.util.ArrayList;
import java.util.List;

/**
 * 作者：shuizi_wade on 2017/8/18 16:23
 * 邮箱：674618016@qq.com
 */
@ContentView(R.layout.fragment_sport)
public class SportFragment extends BaseSportFragment {
    @ViewInject(R.id.scrollview)
    HorizontalScrollView scrollView;
    @ViewInject(R.id.scrollview_bottom)
    HorizontalScrollView scrollview_bottom;
    @ViewInject(R.id.pager)
    ViewPager viewPager;
    @ViewInject(R.id.iv_frag1)
    ImageView iv_frag1;
    @ViewInject(R.id.iv_frag2)
    ImageView iv_frag2;
    @ViewInject(R.id.iv_frag3)
    ImageView iv_frag3;
    @ViewInject(R.id.iv_frag4)
    ImageView iv_frag4;
    private List<Fragment> mFragments = new ArrayList<>();
    private ExerciseFragment exerciseFragment;
    private RideFragment rideFragment;
    private WalkFragment walkFragment;
    private RunFragment runFragment;
    @ViewInject(R.id.iv_first)
    ImageView iv_first;
    @ViewInject(R.id.iv_second)
    ImageView iv_second;
    @ViewInject(R.id.iv_three)
    ImageView iv_three;
    @ViewInject(R.id.iv_four)
    ImageView iv_four;
    @ViewInject(R.id.tv_first)
    TextView tv_first;
    @ViewInject(R.id.tv_second)
    TextView tv_second;
    @ViewInject(R.id.tv_three)
    TextView tv_three;
    @ViewInject(R.id.tv_four)
    TextView tv_four;
    @ViewInject(R.id.tv_weather)
    TextView tv_weather;
    @ViewInject(R.id.btn_bottom)
    Button btn_bottom;
    private int currentPage;
    private LocManage mLocManage;

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initData(view);
        ((HomeActivity) getActivity()).initLocaionSport("sport");
    }

    /**
     * 获取定位权限后的初始化
     */
    public void initByGotLocationPermission() {
        getLocation();
    }

    /**
     * 自动获取定位信息
     */
    private void getLocation() {
        mLocManage = new LocManage(getActivity());
        mLocManage.getLocation(gotLocCallBack);
    }

    /**
     * 获取百度经纬度回调
     */
    private LocManage.GotLocCallBackShort gotLocCallBack = new LocManage.GotLocCallBackShort() {

        @Override
        public void onGotLocShort(double latitude, double longitude, BDLocation location) {
            if (latitude == 0.0d || longitude == 0.0d) {
                return;
            } else {
                Logger.d(Url.WEATHER + location.getCity());
                RequestParams params = new RequestParams(Url.WEATHER + location.getCity());
//                httpGet(params,"");
                x.http().get(params, new Callback.CommonCallback<String>() {
                    @Override
                    public void onSuccess(String result) {
                        Logger.json(result);
                        Weather weather = JSON.parseObject(result, Weather.class);
                        tv_weather.setText(weather.getData().getForecast().get(0).getType() + "    " + weather.getData().getWendu() + "℃    PM2.5  " + weather.getData().getPm25());
                    }

                    @Override
                    public void onError(Throwable ex, boolean isOnCallback) {
                        Logger.d(ex.getMessage());
                    }

                    @Override
                    public void onCancelled(CancelledException cex) {

                    }

                    @Override
                    public void onFinished() {

                    }
                });
                mLocManage.stopLocation();
            }
        }

        @Override
        public void onGotLocShortFailed(String location) {
            //目前不会执行到这里
            hideDia();
        }
    };

    private void initData(View view) {
        RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) btn_bottom.getLayoutParams();
        int width = Utils.getDisplayWidth(getActivity());
        params.width = (int) (width * 0.611);
        params.height = (int) (width * 0.611) / 5;
        btn_bottom.setLayoutParams(params);
        Typeface typefaceRegular = Typeface.createFromAsset(getActivity().getAssets(), "fonts/SourceHanSansCN-Regular.ttf");
        btn_bottom.setTypeface(typefaceRegular);
        tv_weather.setTypeface(typefaceRegular);
        walkFragment = new WalkFragment();
        runFragment = new RunFragment();
        exerciseFragment = new ExerciseFragment();
        rideFragment = new RideFragment();
        mFragments.add(walkFragment);
        mFragments.add(runFragment);
        mFragments.add(exerciseFragment);
        mFragments.add(rideFragment);
        MyFragmentPagerAdapter tabPageAdapter = new MyFragmentPagerAdapter(
                getChildFragmentManager(), mFragments);
        viewPager.setAdapter(tabPageAdapter);
        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                selectPage(position);
                initFourScrollPic(position);
                initCirclePoint(position);
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
        viewPager.setCurrentItem(0);
        selectPage(0);
        initFourScrollPic(0);
        initCirclePoint(0);
        view.findViewById(R.id.ly_first).setOnClickListener(listener);
        view.findViewById(R.id.ly_second).setOnClickListener(listener);
        view.findViewById(R.id.ly_three).setOnClickListener(listener);
        view.findViewById(R.id.ly_four).setOnClickListener(listener);
        scrollView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                return true;
            }
        });
        view.findViewById(R.id.iv_music).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                jumpActvity(PlayMusicActivity.class);
            }
        });
    }

    /**
     * 初始化界面四个滑动图片
     */
    private void initFourScrollPic(int position) {
        RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) scrollView.getLayoutParams();
        params.setMargins(Utils.getDisplayWidth(getActivity()) / 2 - (DensityUtil.dip2px(60) * position) - DensityUtil.dip2px(24),
                0, 0, DensityUtil.dip2px(130));
        scrollView.setLayoutParams(params);
    }

    /**
     * 初始化界面四个滑动圆点
     */
    private void initCirclePoint(int position) {
        RelativeLayout.LayoutParams paramsBottom = (RelativeLayout.LayoutParams) scrollview_bottom.getLayoutParams();
        paramsBottom.setMargins(Utils.getDisplayWidth(getActivity()) / 2 - ((DensityUtil.dip2px(10) + 10) * position) - DensityUtil.dip2px(5)
                , 0, 0, DensityUtil.dip2px(110));
        scrollview_bottom.setLayoutParams(paramsBottom);
        LinearLayout.LayoutParams layoutParams1 = (LinearLayout.LayoutParams) iv_frag1.getLayoutParams();
        LinearLayout.LayoutParams layoutParams2 = (LinearLayout.LayoutParams) iv_frag2.getLayoutParams();
        LinearLayout.LayoutParams layoutParams3 = (LinearLayout.LayoutParams) iv_frag3.getLayoutParams();
        LinearLayout.LayoutParams layoutParams4 = (LinearLayout.LayoutParams) iv_frag4.getLayoutParams();
        LinearLayout.LayoutParams params1 = (LinearLayout.LayoutParams) tv_first.getLayoutParams();
        LinearLayout.LayoutParams params2 = (LinearLayout.LayoutParams) tv_second.getLayoutParams();
        LinearLayout.LayoutParams params3 = (LinearLayout.LayoutParams) tv_three.getLayoutParams();
        LinearLayout.LayoutParams params4 = (LinearLayout.LayoutParams) tv_four.getLayoutParams();
        switch (position) {
            case 0:
                layoutParams1.width = 20;
                layoutParams1.height = 10;
                iv_frag1.setLayoutParams(layoutParams1);
                layoutParams2.width = 10;
                layoutParams2.height = 10;
                iv_frag2.setLayoutParams(layoutParams2);
                layoutParams3.width = 10;
                layoutParams3.height = 10;
                iv_frag3.setLayoutParams(layoutParams3);
                layoutParams4.width = 10;
                layoutParams4.height = 10;
                iv_frag4.setLayoutParams(layoutParams4);
                iv_frag1.setBackgroundResource(R.drawable.iv_sport_fragment_black);
                iv_frag2.setBackgroundResource(R.drawable.iv_sport_fragment_gray);
                iv_frag3.setBackgroundResource(R.drawable.iv_sport_fragment_gray);
                iv_frag4.setBackgroundResource(R.drawable.iv_sport_fragment_gray);
                params1.setMargins(0, 0, 0, 0);
                params2.setMargins(0, 0, 0, DensityUtil.dip2px(10));
                params3.setMargins(0, 0, 0, DensityUtil.dip2px(20));
                params4.setMargins(0, 0, 0, DensityUtil.dip2px(30));
                tv_second.setLayoutParams(params2);
                tv_three.setLayoutParams(params3);
                tv_four.setLayoutParams(params4);
                break;
            case 1:
                layoutParams1.width = 10;
                layoutParams1.height = 10;
                iv_frag1.setLayoutParams(layoutParams1);
                layoutParams2.width = 20;
                layoutParams2.height = 10;
                iv_frag2.setLayoutParams(layoutParams2);
                layoutParams3.width = 10;
                layoutParams3.height = 10;
                iv_frag3.setLayoutParams(layoutParams3);
                layoutParams4.width = 10;
                layoutParams4.height = 10;
                iv_frag4.setLayoutParams(layoutParams4);
                iv_frag1.setBackgroundResource(R.drawable.iv_sport_fragment_gray);
                iv_frag2.setBackgroundResource(R.drawable.iv_sport_fragment_black);
                iv_frag3.setBackgroundResource(R.drawable.iv_sport_fragment_gray);
                iv_frag4.setBackgroundResource(R.drawable.iv_sport_fragment_gray);
                params1.setMargins(0, 0, 0, DensityUtil.dip2px(10));
                params2.setMargins(0, 0, 0, 0);
                params3.setMargins(0, 0, 0, DensityUtil.dip2px(10));
                params4.setMargins(0, 0, 0, DensityUtil.dip2px(20));
                tv_first.setLayoutParams(params1);
                tv_second.setLayoutParams(params2);
                tv_three.setLayoutParams(params3);
                tv_four.setLayoutParams(params4);
                break;
            case 2:
                layoutParams1.width = 10;
                layoutParams1.height = 10;
                iv_frag1.setLayoutParams(layoutParams1);
                layoutParams2.width = 10;
                layoutParams2.height = 10;
                iv_frag2.setLayoutParams(layoutParams2);
                layoutParams3.width = 20;
                layoutParams3.height = 10;
                iv_frag3.setLayoutParams(layoutParams3);
                layoutParams4.width = 10;
                layoutParams4.height = 10;
                iv_frag4.setLayoutParams(layoutParams4);
                iv_frag1.setBackgroundResource(R.drawable.iv_sport_fragment_gray);
                iv_frag2.setBackgroundResource(R.drawable.iv_sport_fragment_gray);
                iv_frag3.setBackgroundResource(R.drawable.iv_sport_fragment_black);
                iv_frag4.setBackgroundResource(R.drawable.iv_sport_fragment_gray);

                params1.setMargins(0, 0, 0, DensityUtil.dip2px(20));
                params2.setMargins(0, 0, 0, DensityUtil.dip2px(10));
                params3.setMargins(0, 0, 0, 0);
                params4.setMargins(0, 0, 0, 10);
                tv_first.setLayoutParams(params1);
                tv_second.setLayoutParams(params2);
                tv_three.setLayoutParams(params3);
                tv_four.setLayoutParams(params4);
                break;
            case 3:
                layoutParams1.width = 10;
                layoutParams1.height = 10;
                iv_frag1.setLayoutParams(layoutParams1);
                layoutParams2.width = 10;
                layoutParams2.height = 10;
                iv_frag2.setLayoutParams(layoutParams2);
                layoutParams3.width = 10;
                layoutParams3.height = 10;
                iv_frag3.setLayoutParams(layoutParams3);
                layoutParams4.width = 20;
                layoutParams4.height = 10;
                iv_frag4.setLayoutParams(layoutParams4);
                iv_frag1.setBackgroundResource(R.drawable.iv_sport_fragment_gray);
                iv_frag2.setBackgroundResource(R.drawable.iv_sport_fragment_gray);
                iv_frag3.setBackgroundResource(R.drawable.iv_sport_fragment_gray);
                iv_frag4.setBackgroundResource(R.drawable.iv_sport_fragment_black);
                params1.setMargins(0, 0, 0, DensityUtil.dip2px(30));
                params2.setMargins(0, 0, 0, DensityUtil.dip2px(20));
                params3.setMargins(0, 0, 0, DensityUtil.dip2px(10));
                params4.setMargins(0, 0, 0, 0);
                tv_first.setLayoutParams(params1);
                tv_second.setLayoutParams(params2);
                tv_three.setLayoutParams(params3);
                tv_four.setLayoutParams(params4);
                break;
        }
    }

    @Event(value = {R.id.btn_bottom, R.id.iv_music})
    private void getEvent(View view) {
        switch (view.getId()) {
            case R.id.btn_bottom:
                int cur = viewPager.getCurrentItem();
                switch (cur) {
                    case 0:
                        walkFragment.jumpToShare();
                        break;
                    case 1:
                        runFragment.startRun();
                        break;
                    case 2:
                        jumpActvity(ExerciseActivity.class);
                        break;
                    case 3:
                        rideFragment.startRide();
                        break;
                }
                break;
            case R.id.iv_music:
                jumpActvity(PlayMusicActivity.class);
                break;
        }
    }

    /**
     * 选择某页
     *
     * @param position 页面的位置
     */
    private void selectPage(int position) {
        // 将所有的tab的icon变成灰色的
        for (int i = 0; i < 4; i++) {
            iv_first.setBackgroundResource(R.mipmap.train_hp_icon_walk_grey);
            iv_second.setBackgroundResource(R.mipmap.train_hp_icon_run_grey);
            iv_three.setBackgroundResource(R.mipmap.train_hp_icon_fitness_grey);
            iv_four.setBackgroundResource(R.mipmap.train_hp_icon_ride_grey);
            tv_first.setTextColor(ContextCompat.getColor(getActivity(), R.color.text_color_gray));
            tv_second.setTextColor(ContextCompat.getColor(getActivity(), R.color.text_color_gray));
            tv_three.setTextColor(ContextCompat.getColor(getActivity(), R.color.text_color_gray));
            tv_four.setTextColor(ContextCompat.getColor(getActivity(), R.color.text_color_gray));
        }
        // 切换页面
//        viewPager.setCurrentItem(position, false);
        LinearLayout.LayoutParams paramsBig = new LinearLayout.LayoutParams(DensityUtil.dip2px(48), DensityUtil.dip2px(48));
        LinearLayout.LayoutParams paramsSmall = new LinearLayout.LayoutParams(DensityUtil.dip2px(30), DensityUtil.dip2px(30));


        // 改变图标
        switch (position) {
            case 0:
                btn_bottom.setText(R.string.share_record);
                iv_first.setLayoutParams(paramsBig);
                iv_first.setBackgroundResource(R.mipmap.train_hp_icon_walk);
                iv_second.setLayoutParams(paramsSmall);
                iv_three.setLayoutParams(paramsSmall);
                iv_four.setLayoutParams(paramsSmall);
                tv_first.setTextColor(ContextCompat.getColor(getActivity(), R.color.text_color_3));
                break;
            case 1:
                btn_bottom.setText(R.string.start_run);
                iv_second.setBackgroundResource(R.mipmap.train_hp_icon_run);
                iv_first.setLayoutParams(paramsSmall);
                iv_second.setLayoutParams(paramsBig);
                iv_three.setLayoutParams(paramsSmall);
                iv_four.setLayoutParams(paramsSmall);
                tv_second.setTextColor(ContextCompat.getColor(getActivity(), R.color.text_color_3));
                break;
            case 2:
                btn_bottom.setText(R.string.start_exercise);
                iv_first.setLayoutParams(paramsSmall);
                iv_second.setLayoutParams(paramsSmall);
                iv_three.setLayoutParams(paramsBig);
                iv_four.setLayoutParams(paramsSmall);
                iv_three.setBackgroundResource(R.mipmap.train_hp_icon_fitness);
                tv_three.setTextColor(ContextCompat.getColor(getActivity(), R.color.text_color_3));
                break;
            case 3:
                btn_bottom.setText(R.string.start_ride);
                iv_first.setLayoutParams(paramsSmall);
                iv_second.setLayoutParams(paramsSmall);
                iv_three.setLayoutParams(paramsSmall);
                iv_four.setLayoutParams(paramsBig);
                iv_four.setBackgroundResource(R.mipmap.train_hp_icon_ride);
                tv_four.setTextColor(ContextCompat.getColor(getActivity(), R.color.text_color_3));
                break;
        }
    }

    View.OnClickListener listener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            int position = 0;
            switch (view.getId()) {
                case R.id.ly_first:
                    position = 0;
                    break;
                case R.id.ly_second:
                    position = 1;
                    break;
                case R.id.ly_three:
                    position = 2;
                    break;
                case R.id.ly_four:
                    position = 3;
                    break;
            }
//            selectPage(position);
            viewPager.setCurrentItem(position);
            startAnim(position);
            initFourScrollPic(position);
        }
    };

    private void startAnim(int position) {
        Animation animation;
        if (currentPage > position) {
            animation = AnimationUtils.loadAnimation(getContext(), R.anim.frg_left_to_right);
        } else {
            animation = AnimationUtils.loadAnimation(getContext(), R.anim.frg_right_to_left);
        }
        viewPager.startAnimation(animation);
        currentPage = position;
    }

    public void getExerciseData(List<ExerciseRes> res) {
        for (ExerciseRes exerciseRes : res) {

            switch (exerciseRes.getType()) {
                case 0:

                    break;
                case 1:
                    runFragment.updateDate(exerciseRes);
                    break;
                case 2:
                    exerciseFragment.updateDate(exerciseRes);
                    break;
                case 3:
                    rideFragment.updateDate(exerciseRes);
                    break;
                default:
                    break;
            }
        }
    }
}
