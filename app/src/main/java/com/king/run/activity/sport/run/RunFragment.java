package com.king.run.activity.sport.run;


import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.king.run.R;
import com.king.run.activity.sport.BaseSportFragment;
import com.king.run.activity.sport.pickviewdata.DataModel;
import com.king.run.activity.sport.pickviewdata.GoalBean;
import com.king.run.activity.statistics.LineChartActivity;
import com.king.run.commen.Constants;
import com.king.run.intface.GoalListener;
import com.king.run.intface.iml.GoalIml;
import com.king.run.model.http.res.ExerciseRes;
import com.king.run.util.StepAlgorithm;
import com.king.run.util.Utils;
import com.king.run.view.DialCircleView2;
import com.king.run.view.RunTypeDialog;
import com.lvfq.pickerview.OptionsPickerView;
import com.lvfq.pickerview.listener.OnSureListener;

import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.Event;
import org.xutils.view.annotation.ViewInject;

import java.text.DecimalFormat;
import java.util.ArrayList;

@ContentView(R.layout.fragment_run)
public class RunFragment extends BaseSportFragment implements View.OnClickListener, OptionsPickerView.OnOptionsSelectListener,
        OnSureListener {

    @ViewInject(R.id.step_arcview)
    private DialCircleView2 dcView;
    @ViewInject(R.id.tv_goal_setting)
    private TextView tvGoalSetting;
    private OptionsPickerView pvOptions;

    private ArrayList<GoalBean> options1Items = new ArrayList<GoalBean>();
    private ArrayList<ArrayList<String>> options2Items = new ArrayList<ArrayList<String>>();
    private int[] indexs = new int[]{-1, -1, -1};

    private boolean isSelecedGoal;
    private int goalType = -1;
    private String goalValue = "0";
    private DecimalFormat df = new DecimalFormat("#.##");
    private ExerciseRes exerciseRes;

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        showOptions();
        initViews();
        sportListener.currentWay(Constants.TYPE_RUN);
        if (exerciseRes != null)
            setDcView();
    }

    private void initViews() {
        dcView.setOnCircleClick(new DialCircleView2.CircleClickInterface() {
            @Override
            public void onClick() {
                jumpActvity(LineChartActivity.class);
            }
        });
    }


    @Override
    public void onResume() {
        super.onResume();
    }


    @Event(value = {R.id.btn_start_run, R.id.step_arcview})
    private void getEvent(View view) {
        switch (view.getId()) {
            case R.id.step_arcview:
//                jumpActvity(LineChartActivity.class);
                break;
            case R.id.btn_start_run:
                startRun();
                break;
            default:
                break;
        }
    }

    public void startRun() {
        new RunTypeDialog(getActivity(), new RunTypeDialog.ClickListener() {
            @Override
            public void inRun() {
                Bundle bundle = new Bundle();
                bundle.putInt("run_way", 0);
                if (goalType == -1) {
                    jumpBundleActvity(PrepareSportActivity.class, bundle);
                } else {
                    bundle.putInt(GoalListener.GOAL_TYPE, goalType);
                    bundle.putDouble(GoalListener.GOAL_VALUE, GoalIml.getInstance().getGoalValue(goalType, goalValue));
                    jumpBundleActvity(PrepareSportActivity.class, bundle);
                }
            }

            @Override
            public void outRun() {
                Bundle bundle = new Bundle();
                if (goalType == -1) {
                    bundle.putInt("run_way", 1);
                } else {
                    bundle.putInt("run_way", 1);
                    bundle.putInt(GoalListener.GOAL_TYPE, goalType);
                    bundle.putDouble(GoalListener.GOAL_VALUE, GoalIml.getInstance().getGoalValue(goalType, goalValue));
                }
                jumpBundleActvity(PrepareSportActivity.class, bundle);
            }
        }).show();
    }

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
        //点击弹出选项选择器
        tvGoalSetting.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.tv_goal_setting:
                pvOptions.show();
                break;
            default:
                break;
        }
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

//        sp.setParam(SharedParams.KEY_GOAL_SETTING, goalSetting);//保存目标设置
    }


    @Override
    public void onClickSure(boolean b) {
        isSelecedGoal = b;
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
        if (!speed.equals("0"))
            speed = "0";
        if (null != dcView)
            dcView.change(Utils.getPercent(km, 300), kcal + "", speed, km + "");
    }
}
