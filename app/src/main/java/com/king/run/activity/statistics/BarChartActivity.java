package com.king.run.activity.statistics;

import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.widget.ImageView;

import com.king.run.R;
import com.king.run.activity.statistics.adapter.MyFgAdapter;
import com.king.run.activity.statistics.fragment.MonthBarFg;
import com.king.run.activity.statistics.fragment.WeekBarFg;
import com.king.run.base.BaseActivity;
import com.king.run.intface.http.ReqBack;
import com.king.run.intface.http.iml.SportIml;
import com.king.run.model.http.req.StatisticsBarReq;
import com.king.run.util.DateUtils;
import com.king.run.util.PrefName;
import com.king.run.util.ViewRefUtil;

import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.ViewInject;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * @author xwk
 */
@ContentView(R.layout.activity_bar_chart)
public class BarChartActivity extends BaseActivity {

    @ViewInject(R.id.tab_layout_bar)
    private TabLayout tabLayout;
    @ViewInject(R.id.view_pager_bar)
    private ViewPager viewPager;
    @ViewInject(R.id.iv_back)
    private ImageView ivBack;

    private WeekBarFg weekBarFg;
    private MonthBarFg monthBarFg;
    private ArrayList<Fragment> frags;
    private String[] tabNames;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initTitleBar();
        initViews();
    }

    private void initViews() {
        ivBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        weekBarFg = new WeekBarFg();
        monthBarFg = new MonthBarFg();
        frags = new ArrayList<>();
        frags.add(weekBarFg);
        frags.add(monthBarFg);

        tabNames = new String[]{" " + getString(R.string.unit_week) + " ", " " + getString(R.string.unit_month) + " "};
        MyFgAdapter fgAdapter = new MyFgAdapter(
                getSupportFragmentManager(), frags, tabNames);
        viewPager.setAdapter(fgAdapter);
        tabLayout.setupWithViewPager(viewPager);
        ViewRefUtil.reflex(tabLayout);
    }


}
