package com.king.run.activity.posture;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.ScaleAnimation;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.king.run.R;
import com.king.run.activity.posture.model.iBeaconClass;
import com.king.run.base.BaseFragment;
import com.king.run.util.Utils;

import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.Event;
import org.xutils.view.annotation.ViewInject;

import java.util.List;

import steps.teller.step.utils.DbUtils;

/**
 * 作者：shuizi_wade on 2017/8/18 16:26
 * 邮箱：674618016@qq.com
 */
@ContentView(R.layout.fragment_posture)
public class PostureFragment extends BaseFragment {
    @ViewInject(R.id.ly_walk)
    LinearLayout ly_walk;
    @ViewInject(R.id.ly_run)
    LinearLayout ly_run;
    @ViewInject(R.id.ly_shoe)
    LinearLayout ly_shoe;
    @ViewInject(R.id.ly_teach)
    LinearLayout ly_teach;
    @ViewInject(R.id.ly_location)
    LinearLayout ly_location;

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initViews();
    }

    private void initViews() {
        int width = Utils.getDisplayWidth(getActivity());
        int height = Utils.getDisplayHeight(getActivity());
        RelativeLayout.LayoutParams paramsWalk = (RelativeLayout.LayoutParams) ly_walk.getLayoutParams();
        RelativeLayout.LayoutParams paramsRun = (RelativeLayout.LayoutParams) ly_run.getLayoutParams();
        RelativeLayout.LayoutParams paramsShoe = (RelativeLayout.LayoutParams) ly_shoe.getLayoutParams();
        RelativeLayout.LayoutParams paramsTeach = (RelativeLayout.LayoutParams) ly_teach.getLayoutParams();
        RelativeLayout.LayoutParams paramsLocation = (RelativeLayout.LayoutParams) ly_location.getLayoutParams();

        paramsRun.setMargins((int) (width * 0.099), 0, 0, (int) (height * 0.227));
        paramsRun.width = (int) (width * 0.383);
        paramsRun.height = (int) (width * 0.383);
        ly_run.setLayoutParams(paramsRun);

        paramsWalk.setMargins(0, 0, 0, (int) (0.385 * height));
        paramsWalk.width = (int) (width * 0.369);
        paramsWalk.height = (int) (width * 0.369);
        ly_walk.setLayoutParams(paramsWalk);

        paramsLocation.setMargins(0, 0, 0, (int) (0.216 * height));
        paramsLocation.width = (int) (width * 0.314);
        paramsLocation.height = (int) (width * 0.314);
        ly_location.setLayoutParams(paramsLocation);

        paramsTeach.setMargins(0, 0, 0, (int) (0.317 * height));
        paramsTeach.width = (int) (width * 0.375);
        paramsTeach.height = (int) (width * 0.375);
        ly_teach.setLayoutParams(paramsTeach);

        paramsShoe.setMargins(0, 0, 0, (int) (0.341 * height));
        paramsShoe.width = (int) (width * 0.494);
        paramsShoe.height = (int) (width * 0.494);
        ly_shoe.setLayoutParams(paramsShoe);
        initAnimation(ly_teach);
        initAnimation(ly_run);
        initAnimation(ly_location);
        initAnimation(ly_shoe);
        initAnimation(ly_walk);
    }

    private void initAnimation(View view) {
        AnimationSet animationSet = new AnimationSet(true);
        ScaleAnimation scaleAnimation = new ScaleAnimation(0, 1f, 0, 1f, Animation.RELATIVE_TO_SELF, 2f, Animation.RELATIVE_TO_SELF, 1f);
        animationSet.addAnimation(scaleAnimation);
        animationSet.setDuration(1500);
        view.startAnimation(animationSet);
    }

    @Event(value = {R.id.ly_teach, R.id.ly_shoe, R.id.ly_run, R.id.ly_walk})
    private void getEvent(View view) {
        switch (view.getId()) {
            case R.id.ly_teach:
                jumpActvity(CourseActivity.class);
                break;
            case R.id.ly_shoe:
                shoe();
                break;
            case R.id.ly_run:
                jumpActvity(RunPostureActivity.class);
                break;
            case R.id.ly_walk:
                jumpActvity(RunPostureActivity.class);
                break;
        }
    }

    private void shoe() {
        List<iBeaconClass.iBeacon> list = DbUtils.getQueryAll(iBeaconClass.iBeacon.class);
        boolean isCon = false;
        for (iBeaconClass.iBeacon iBeacon : list) {
            if (iBeacon.isConnect)
                isCon = true;
            else isCon = false;
        }
        if (isCon)
            jumpActvity(MyShoesActivity.class);
        else {
            final BluetoothManager bluetoothManager = (BluetoothManager) getActivity().getSystemService(Context.BLUETOOTH_SERVICE);
            BluetoothAdapter mBluetoothAdapter = bluetoothManager.getAdapter();
            // 如果本地蓝牙没有开启，则开启
            if (!mBluetoothAdapter.isEnabled()) {
                Intent mIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
                startActivityForResult(mIntent, 1);
            } else
                jumpActvity(ConnectDeviceActivity.class, "posture");
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1) {
            if (resultCode == getActivity().RESULT_OK) {
                jumpActvity(ConnectDeviceActivity.class, "posture");
            } else if (resultCode == getActivity().RESULT_CANCELED) {
                senToa("不允许蓝牙开启");
            }
        }
    }
}
