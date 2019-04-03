package com.king.run.activity.sport.exercise;

import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ListView;

import com.king.run.R;
import com.king.run.activity.sport.exercise.adapter.RightStrategyAdapter;
import com.king.run.activity.sport.exercise.adapter.StrategyAdapter;
import com.king.run.activity.sport.exercise.model.Strategy;
import com.king.run.base.BaseActivity;

import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.ViewInject;

import java.util.ArrayList;
import java.util.List;

@ContentView(R.layout.activity_recommended_strategy)
public class RecommendedStrategyActivity extends BaseActivity {
    private List<Strategy> strategyLists = new ArrayList<>();
    @ViewInject(R.id.lv_left)
    ListView lv_left;
    @ViewInject(R.id.gv_right)
    GridView gv_right;
    private StrategyAdapter strategyAdapter;
    private RightStrategyAdapter rightAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        init();
        initViews();
    }

    private void initViews() {
        title_tv_title.setText(R.string.recommended_strategy);
        rightAdapter = new RightStrategyAdapter(context);
        gv_right.setAdapter(rightAdapter);
        for (int i = 0; i < 6; i++) {
            Strategy str = new Strategy();
            if (i == 0) {
                str.setChecked(true);
            }
            str.setName("减脂");
            strategyLists.add(str);
        }
        strategyAdapter = new StrategyAdapter(context, strategyLists);
        lv_left.setAdapter(strategyAdapter);
        lv_left.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                for (int j = 0; j < strategyLists.size(); j++) {
                    Strategy s = strategyLists.get(j);
                    if (j == i)
                        s.setChecked(true);
                    else
                        s.setChecked(false);
                }
                strategyAdapter.setList(strategyLists);
                strategyAdapter.notifyDataSetChanged();
            }
        });
        gv_right.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                jumpActvity(StrategyDetailsActivity.class);
            }
        });
    }
}
