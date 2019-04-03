package com.king.run.activity.sport.exercise;


import android.os.Bundle;
import android.view.View;

import com.king.run.R;
import com.king.run.activity.sport.BaseSportFragment;
import com.king.run.activity.statistics.LineChartActivity;
import com.king.run.commen.Constants;
import com.king.run.model.http.res.ExerciseRes;
import com.king.run.util.StepAlgorithm;
import com.king.run.util.Utils;
import com.king.run.view.DialCircleView2;

import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.Event;
import org.xutils.view.annotation.ViewInject;

@ContentView(R.layout.fragment_exercise)
public class ExerciseFragment extends BaseSportFragment {
    @ViewInject(R.id.step_arcview)
    DialCircleView2 test2View;
    private ExerciseRes exerciseRes;

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initViews();
        sportListener.currentWay(Constants.TYPE_EXERCISE);
        if (exerciseRes != null)
            setDcView();
        else
            test2View.change2(0, true, "0", "0");
    }

    private void initViews() {
        test2View.setOnCircleClick(new DialCircleView2.CircleClickInterface() {
            @Override
            public void onClick() {
                jumpActvity(LineChartActivity.class);
            }
        });
    }
    @Event(value = { R.id.btn_start_exercise, R.id.step_arcview})
    private void getEvent(View view) {
        switch (view.getId()) {
            case R.id.btn_start_exercise:
//                jumpActvity(SpeedDetailsActivity.class);
                jumpActvity(ExerciseActivity.class);
//                jumpActvity(SurfaceViewTest.class);
                break;
            case R.id.step_arcview:
//                jumpActvity(LineChartActivity.class);
                break;
            default:
                break;
        }
    }

    public void updateDate(ExerciseRes exerciseRes) {
        this.exerciseRes = exerciseRes;
        setDcView();
    }

    private void setDcView() {
        double km = exerciseRes.getKilometre();
        int kcal = exerciseRes.getCalorie();
        int second = exerciseRes.getSecond();
        String speed = StepAlgorithm.getSpeedByDis(km * 1000, second);

        try {
            String[] speeds = speed.split("'");
            if (speeds.length > 2) {
                speed = "0";
            } else if (speeds.length == 2 && (Integer.valueOf(speeds[0]) > 30)) {
                speed = "0";
            }
        } catch (NumberFormatException e) {
            e.printStackTrace();
            speed = "0";
        }
        if (null != test2View) {
            test2View.change2(Utils.getPercent(km, 20), true, kcal + "", second + "");
        }
    }
}
