package com.king.run.activity.sport.ride;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.king.run.R;
import com.king.run.activity.sport.BaseSportFragment;
import com.king.run.activity.sport.pickviewdata.DataModel;
import com.king.run.activity.sport.pickviewdata.GoalBean;
import com.king.run.activity.sport.run.PrepareSportActivity;
import com.king.run.activity.statistics.LineChartActivity;
import com.king.run.commen.Constants;
import com.king.run.intface.GoalListener;
import com.king.run.intface.iml.GoalIml;
import com.king.run.model.http.res.ExerciseRes;
import com.king.run.util.StepAlgorithm;
import com.king.run.util.Utils;
import com.king.run.view.DialCircleView2;
import com.lvfq.pickerview.OptionsPickerView;
import com.lvfq.pickerview.listener.OnSureListener;

import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.Event;
import org.xutils.view.annotation.ViewInject;

import java.util.ArrayList;

@ContentView(R.layout.fragment_ride)
public class RideFragment extends BaseSportFragment implements OnSureListener, OptionsPickerView.OnOptionsSelectListener {
    @ViewInject(R.id.step_arcview)
    private DialCircleView2 dcView;
    @ViewInject(R.id.tv_goal_setting)
    TextView tvGoalSetting;
    private ExerciseRes exerciseRes;

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initViews();
        sportListener.currentWay(Constants.TYPE_RIDE);
        setDcView();
    }

    private void initViews() {
        showOptions();
        dcView.setOnCircleClick(new DialCircleView2.CircleClickInterface() {
            @Override
            public void onClick() {
                jumpActvity(LineChartActivity.class);
            }
        });
    }

    @Event(value = {R.id.btn_start_ride, R.id.step_arcview, R.id.tv_goal_setting})
    private void getEvent(View view) {
        switch (view.getId()) {
            case R.id.btn_start_ride:
                startRide();
                break;
            case R.id.step_arcview:
//                jumpActvity(LineChartActivity.class);
                break;
            case R.id.tv_goal_setting:
                pvOptions.show();
                break;
            default:
                break;
        }
    }

    public void startRide() {
        Bundle bundle = new Bundle();
        if (goalType != -1) {
            bundle.putInt(GoalListener.GOAL_TYPE, goalType);
            bundle.putDouble(GoalListener.GOAL_VALUE, GoalIml.getInstance().getGoalValue(goalType, goalValue));
        }
        bundle.putInt("run_way", 2);
        jumpBundleActvity(PrepareSportActivity.class, bundle);
    }

    private OptionsPickerView pvOptions;
    private boolean isSelecedGoal;
    private int goalType = -1;
    private String goalValue;
    private ArrayList<GoalBean> options1Items = new ArrayList<>();
    private ArrayList<ArrayList<String>> options2Items = new ArrayList<>();

    private void showOptions() {
        //选项选择器
        pvOptions = new OptionsPickerView(getActivity());
        DataModel.initData(options1Items, options2Items);
        pvOptions.setPicker(options1Items, options2Items, true);
        //设置选择的三级单位
        pvOptions.setCyclic(false, false, false);
        //设置默认选中的二级项目
        //监听确定选择按钮
        pvOptions.setSelectOptions(0, 0, 0);
        pvOptions.setOnoptionsSelectListener(this);
        pvOptions.setOnSureListener(this);
    }

    @Override
    public void onClickSure(boolean b) {
        isSelecedGoal = b;
    }

    @Override
    public void onOptionsSelect(int options1, int option2, int options3) {
        if (!isSelecedGoal) {
//            sp.remove(SharedParams.KEY_GOAL_SETTING);
            tvGoalSetting.setText("目标设置>>");
            return;
        }
        goalType = GoalIml.getInstance().getGoalType(options1);
        //返回的分别是三个级别的选中位置
        String tx = options1Items.get(options1).getPickerViewText()
                + " " + options2Items.get(options1).get(option2);
        tvGoalSetting.setText(tx);
        goalValue = options2Items.get(options1).get(option2);
    }

    public void updateDate(ExerciseRes exerciseRes) {
        this.exerciseRes = exerciseRes;
        setDcView();
    }

    private void setDcView() {
        if (null != exerciseRes) {
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
//        if (!speed.equals("0"))
//            speed = speed + "\"";
            if (null != dcView)
                dcView.change(Utils.getPercent(km, 300), kcal + "", speed, km + "");
        }
    }
}
