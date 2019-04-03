package com.king.run.base;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.king.run.R;
import com.king.run.util.StringUtil;
import com.king.run.view.photoView.HackyViewPager;

import java.util.ArrayList;
import java.util.List;


public class ImagePagerActivity extends BaseActivity {
    private static final String STATE_POSITION = "STATE_POSITION";
    public static final String EXTRA_IMAGE_INDEX = "image_index";
    public static final String EXTRA_IMAGE_URLS = "image_urls";
    public static final String EXTRA_IMAGE_PRE_URLS = "image_pre_urls";

    private HackyViewPager mPager;
    private int pagerPosition;
    private TextView indicator;

    /**
     * 显示单张图片
     */
    public static void showOnePic(Activity activity, String preUrl, String picUrl) {
        if (StringUtil.isBlank(picUrl))
            return;
        ArrayList<String> urls = new ArrayList<>();
        urls.add(picUrl);
        ArrayList<String> preUrls = new ArrayList<>();
        preUrls.add(preUrl);
        showPics(activity, urls, preUrls, 0);
    }

    public static void showPics(Activity activity,
                                ArrayList<String> picUrls, ArrayList<String> preUrls, int showIndex) {
        if (picUrls == null || picUrls.isEmpty())
            return;
        Intent intent = new Intent(activity, ImagePagerActivity.class);
        intent.putStringArrayListExtra(ImagePagerActivity.EXTRA_IMAGE_URLS, picUrls);
        intent.putStringArrayListExtra(ImagePagerActivity.EXTRA_IMAGE_PRE_URLS, preUrls);
        intent.putExtra(ImagePagerActivity.EXTRA_IMAGE_INDEX, showIndex);
        activity.startActivity(intent);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.image_detail_pager);

        pagerPosition = getIntent().getIntExtra(EXTRA_IMAGE_INDEX, 0);
        ArrayList<String> urls = getIntent().getStringArrayListExtra(EXTRA_IMAGE_URLS);
        ArrayList<String> preUrls = getIntent().getStringArrayListExtra(EXTRA_IMAGE_PRE_URLS);
        mPager = (HackyViewPager) findViewById(R.id.pager);
        ImagePagerAdapter mAdapter = new ImagePagerAdapter(getSupportFragmentManager(), urls, preUrls);
        mPager.setAdapter(mAdapter);
        indicator = (TextView) findViewById(R.id.indicator);

        boolean isShowIndicator = urls.size() > 1;
        if (!isShowIndicator) {
            indicator.setVisibility(View.GONE);
        }

        CharSequence text = getString(R.string.viewpager_indicator, 1, mPager
                .getAdapter().getCount());
        indicator.setText(text);
        // 更新下标
        mPager.setOnPageChangeListener(new OnPageChangeListener() {

            @Override
            public void onPageScrollStateChanged(int arg0) {
            }

            @Override
            public void onPageScrolled(int arg0, float arg1, int arg2) {
            }

            @Override
            public void onPageSelected(int arg0) {
                CharSequence text = getString(R.string.viewpager_indicator,
                        arg0 + 1, mPager.getAdapter().getCount());
                indicator.setText(text);
            }

        });
        if (savedInstanceState != null) {
            pagerPosition = savedInstanceState.getInt(STATE_POSITION);
        }

        mPager.setCurrentItem(pagerPosition);
//        transStatusBar();
        transNavigationBarStatusBar();
//        hideStatusBar();
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        outState.putInt(STATE_POSITION, mPager.getCurrentItem());
    }

    private class ImagePagerAdapter extends FragmentStatePagerAdapter {

        public List<String> urls;
        public List<String> preUrls;

        public ImagePagerAdapter(FragmentManager fm, List<String> urls, List<String> preUrls) {
            super(fm);
            this.urls = urls;
            this.preUrls = preUrls;
        }

        @Override
        public int getCount() {
            return urls == null ? 0 : urls.size();
        }

        @Override
        public Fragment getItem(int position) {
            String url = urls.get(position);
            String preUrl = url;
            if (preUrls != null && position < preUrls.size())
                preUrl = preUrls.get(position);
            return ImageDetailFragment.newInstance(url, preUrl);
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            super.destroyItem(container, position, object);
        }
    }
}