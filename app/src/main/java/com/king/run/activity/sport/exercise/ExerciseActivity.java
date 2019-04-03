package com.king.run.activity.sport.exercise;

import android.os.Bundle;
import android.view.View;
import android.widget.GridView;
import android.widget.ListView;

import com.king.run.R;
import com.king.run.activity.sport.exercise.adapter.ExerciseGvAdapter;
import com.king.run.activity.sport.exercise.adapter.ExerciseLvAdapter;
import com.king.run.base.BaseActivity;

import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.Event;
import org.xutils.view.annotation.ViewInject;

@ContentView(R.layout.activity_exercise)
public class ExerciseActivity extends BaseActivity {
    private ExerciseGvAdapter gvAdapter;
    private ExerciseLvAdapter lvAdapter;
    @ViewInject(R.id.gv_recommended_strategy)
    GridView gv_recommended_strategy;
    @ViewInject(R.id.lv_exercise_video)
    ListView lv_exercise_video;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        init();
        initViews();
    }

    private void initViews() {
        title_tv_title.setText(R.string.exercise);
        gvAdapter = new ExerciseGvAdapter(context);
        gv_recommended_strategy.setAdapter(gvAdapter);
        lvAdapter = new ExerciseLvAdapter(context);
        lv_exercise_video.setAdapter(lvAdapter);
    }

    @Event(value = {R.id.ly_recommended_strategy, R.id.ly_exercise_video})
    private void getEvent(View view) {
        switch (view.getId()) {
            case R.id.ly_recommended_strategy:
                jumpActvity(RecommendedStrategyActivity.class);
                break;
            case R.id.ly_exercise_video:
                jumpActvity(ExerciseVideoActivity.class);
                break;
        }
    }
}
