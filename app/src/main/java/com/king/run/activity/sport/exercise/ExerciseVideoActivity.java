package com.king.run.activity.sport.exercise;

import android.os.Bundle;
import android.widget.ListView;

import com.king.run.R;
import com.king.run.activity.sport.exercise.adapter.RecommendedStrategyAdapter;
import com.king.run.base.BaseActivity;

import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.ViewInject;

@ContentView(R.layout.activity_exercise_video)
public class ExerciseVideoActivity extends BaseActivity {
    private RecommendedStrategyAdapter adapter;
    @ViewInject(R.id.lv_recommended_strategy)
    ListView lv_recommended_strategy;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        init();
        initViews();
    }

    private void initViews() {
        title_tv_title.setText(R.string.exercise_video);
        adapter = new RecommendedStrategyAdapter(context);
        lv_recommended_strategy.setAdapter(adapter);
    }
}
