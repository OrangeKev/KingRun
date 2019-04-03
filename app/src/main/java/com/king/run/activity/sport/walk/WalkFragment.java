package com.king.run.activity.sport.walk;


import android.os.Bundle;
import android.view.View;

import com.king.run.R;
import com.king.run.activity.other.HomeActivity;
import com.king.run.activity.sport.BaseSportFragment;
import com.king.run.activity.statistics.LineChartActivity;
import com.king.run.commen.Constants;
import com.king.run.intface.DataChangedListener;
import com.king.run.view.DialCircleView2;

import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.Event;
import org.xutils.view.annotation.ViewInject;

@ContentView(R.layout.fragment_walk)
public class WalkFragment extends BaseSportFragment implements DataChangedListener {
    @ViewInject(R.id.step_arcview)
    private DialCircleView2 dcView;
    private int stepCount;
    private String km, kcal;

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initViews();
        ((HomeActivity) getActivity()).setDataChangedListener(this);
        sportListener.currentWay(Constants.TYPE_WALK);
    }

    private void initViews() {
        dcView.setOnCircleClick(new DialCircleView2.CircleClickInterface() {
            @Override
            public void onClick() {
                jumpActvity(LineChartActivity.class);
            }
        });
    }


    @Event(value = {R.id.btn_share_record, R.id.step_arcview})
    private void getEvent(View view) {
        switch (view.getId()) {
            case R.id.btn_share_record:
//                Class cls = (ActivityManager.getInstance().getTopActivity()).getClass();
//                senToa(cls + "");
                break;
            case R.id.step_arcview:
//                jumpActvity(LineChartActivity.class);
                break;
        }
    }

    public void jumpToShare() {
        Bundle bundle = new Bundle();
        bundle.putInt("step", stepCount);
        bundle.putString("km", km);
        bundle.putString("kcal", kcal);
        jumpBundleActvity(ShareRecordActivity.class, bundle);
    }

    @Override
    public void dataChanged(int stepCount, int percent, String km, String kcal, String time) {
        this.stepCount = stepCount;
        this.km = km;
        this.kcal = kcal;
        dcView.change(percent, km, kcal, time);
    }
}
