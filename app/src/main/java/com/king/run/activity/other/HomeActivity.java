package com.king.run.activity.other;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.king.run.R;
import com.king.run.activity.circle.CircleFragment;
import com.king.run.activity.mine.MineFragment;
import com.king.run.activity.mine.model.RemindData;
import com.king.run.activity.posture.PostureFragment;
import com.king.run.activity.posture.model.iBeaconClass;
import com.king.run.activity.sport.BaseSportFragment;
import com.king.run.activity.sport.SportFragment;
import com.king.run.base.BaseActivity;
import com.king.run.intface.DataChangedListener;
import com.king.run.intface.http.ReqBack;
import com.king.run.intface.http.iml.SportIml;
import com.king.run.model.http.res.ExerciseRes;
import com.king.run.util.PrefName;
import com.king.run.util.PreferencesUtils;
import com.king.run.util.StartRemind;
import com.king.run.util.StepAlgorithm;
import com.king.run.util.StringUtil;
import com.king.run.util.TimeUtil;
import com.king.run.util.Url;

import org.xutils.http.RequestParams;
import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.ViewInject;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import steps.teller.step.UpdateUiCallBack;
import steps.teller.step.bean.StepData;
import steps.teller.step.service.StepService;
import steps.teller.step.utils.DbUtils;
import steps.teller.step.utils.SharedPreferencesUtils;

/**
 * 作者：shuizi_wade on 2017/8/18 16:25
 * 邮箱：674618016@qq.com
 */
@ContentView(R.layout.activity_home)
public class HomeActivity extends BaseActivity implements View.OnClickListener, BaseSportFragment.SportListener, ReqBack {
    @ViewInject(R.id.iv_sport)
    ImageView iv_sport;
    @ViewInject(R.id.iv_circle)
    ImageView iv_circle;
    @ViewInject(R.id.iv_posture)
    ImageView iv_posture;
    @ViewInject(R.id.iv_mine)
    ImageView iv_mine;
    @ViewInject(R.id.tv_sport)
    TextView tv_sport;
    @ViewInject(R.id.tv_circle)
    TextView tv_circle;
    @ViewInject(R.id.tv_posture)
    TextView tv_posture;
    @ViewInject(R.id.tv_mine)
    TextView tv_mine;
    private SportFragment sportFragment;
    private CircleFragment circleFragment;
    private PostureFragment postureFragment;
    private MineFragment mineFragment;
    private int mIndex;
    private Fragment[] mFragments;
    private FragmentTransaction transaction;
    private SharedPreferencesUtils sp;
    private DecimalFormat df = new DecimalFormat("#.##");
    private int mStepCount;
    private boolean isBind = false;
    private StepService stepService;
    private List<ExerciseRes> res = new ArrayList<>();
    public final int TYPE_WALK = 0;
    public final int TYPE_RUN = 1;
    public final int TYPE_EXSISTES = 2;
    public final int TYPE_BIKE = 3;
    private static final int AVATAR_CODE = 40;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initTitleBar();
        initViews();
        initAlarm();
        uploadData();
        initData();
    }

    private void initAlarm() {
        Calendar calendar = Calendar.getInstance();
        int week = calendar.get(Calendar.DAY_OF_WEEK);
        if (DbUtils.getLiteOrm() == null) {
            DbUtils.createDb(this, "jingzhi");
        }
        List<RemindData> list = DbUtils.getQueryAll(RemindData.class);
        for (int i = 0; i < list.size(); i++) {
            RemindData remindData = list.get(i);
            if (remindData.getRepet().contains(week + "") && PrefName.getTrainSwitch(context)) {
                StartRemind.start(context, remindData);
            }
        }
    }

    public void checkVoiceStorgeCamera() {
        if (checkCameraStorageAudioPermission()) {
            circleFragment.tekeVideo();
        }
    }

    @Override
    protected void gotCameraStorageAudioPermissionResult(boolean isGrant) {
        super.gotCameraStorageAudioPermissionResult(isGrant);
        if (isGrant)
            circleFragment.tekeVideo();
        else
            senToa(R.string.permission_camera_storage_audio);
    }

    @Override
    protected void onResume() {
        super.onResume();
        SportIml.getInstance(this).exerciseData(PrefName.getToken(this), this);
    }

    @Override
    public void onSuccess(String result) {
        try {
            List<ExerciseRes> res = JSON.parseArray(result, ExerciseRes.class);
            sportFragment.getExerciseData(res);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onError(String error) {
//        Toast.makeText(this, "获取失败", Toast.LENGTH_SHORT).show();
    }

    private void initViews() {
        transaction = getSupportFragmentManager().beginTransaction();
        sportFragment = new SportFragment();
        circleFragment = new CircleFragment();
        postureFragment = new PostureFragment();
        mineFragment = new MineFragment();
        mFragments = new Fragment[]{sportFragment, circleFragment, postureFragment, mineFragment};
        transaction.add(R.id.container, sportFragment);
        transaction.commit();
        setIndexSelected(0);
        findViewById(R.id.ly_sport).setOnClickListener(this);
        findViewById(R.id.ly_circle).setOnClickListener(this);
        findViewById(R.id.ly_posture).setOnClickListener(this);
        findViewById(R.id.ly_mine).setOnClickListener(this);
        selectSport();
    }


    private void initData() {
        sp = new SharedPreferencesUtils(this);
        //获取用户设置的计划锻炼步数，没有设置过的话默认7000
        String planWalk_QTY = (String) sp.getParam("planWalk_QTY", "7000");
        //设置当前步数为0
//        cc.setCurrentCount(Integer.parseInt(planWalk_QTY), 0);
        setupService();

    }


    /**
     * 开启计步服务
     */
    private void setupService() {
        Intent intent = new Intent(this, StepService.class);
        isBind = bindService(intent, conn, Context.BIND_AUTO_CREATE);
        startService(intent);
    }

    /**
     * 用于查询应用服务（application Service）的状态的一种interface，
     * 更详细的信息可以参考Service 和 context.bindService()中的描述，
     * 和许多来自系统的回调方式一样，ServiceConnection的方法都是进程的主线程中调用的。
     */
    ServiceConnection conn = new ServiceConnection() {
        /**
         * 在建立起于Service的连接时会调用该方法，目前Android是通过IBind机制实现与服务的连接。
         * @param name 实际所连接到的Service组件名称
         * @param service 服务的通信信道的IBind，可以通过Service访问对应服务
         */
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            stepService = ((StepService.StepBinder) service).getService();
            //设置初始化数据
            String planWalk_QTY = (String) sp.getParam("planWalk_QTY", "7000");
//            cc.setCurrentCount(Integer.parseInt(planWalk_QTY), stepService.getStepCount());
            updateUI(stepService.getStepCount());
            mStepCount = stepService.getStepCount();
            //设置步数监听回调
            stepService.registerCallback(new UpdateUiCallBack() {
                @Override
                public void updateUi(int stepCount) {
                    String planWalk_QTY = (String) sp.getParam("planWalk_QTY", "7000");
//                    cc.setCurrentCount(Integer.parseInt(planWalk_QTY), stepCount);
                    mStepCount = stepCount;
                    updateUI(stepCount);

                }
            });
        }

        /**
         * 当与Service之间的连接丢失的时候会调用该方法，
         * 这种情况经常发生在Service所在的进程崩溃或者被Kill的时候调用，
         * 此方法不会移除与Service的连接，当服务重新启动的时候仍然会调用 onServiceConnected()。
         * @param name 丢失连接的组件名称
         */
        @Override
        public void onServiceDisconnected(ComponentName name) {

        }
    };
    private DataChangedListener dataChangedListener;

    public void setDataChangedListener(DataChangedListener listener) {
        this.dataChangedListener = listener;
        updateUI(mStepCount);
    }

    @Override
    public void currentWay(int type) {

    }

    public void updateUI(int stepCount) {
        if (sportFragment != null) {
            //下面数据暂时除了stepCount其他都是假数据
            int percent = (int) (stepCount / 2 * 0.01);
            if (percent > 100) {
                percent = 100;
            }
            double dis = StepAlgorithm.getDisDouble(stepCount);
            if (dataChangedListener != null)
                dataChangedListener.dataChanged(stepCount, percent, df.format(dis),
                        StepAlgorithm.getKcal(65, dis), stepCount + "");
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (isBind) {
            this.unbindService(conn);
        }
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.ly_sport:
                setIndexSelected(0);
                selectSport();
                break;
            case R.id.ly_circle:
                setIndexSelected(1);
                selectCircle();
                break;
            case R.id.ly_posture:
                setIndexSelected(2);
                selectPosture();
                break;
            case R.id.ly_mine:
                setIndexSelected(3);
                selectMine();
                break;
        }
    }

    private void selectSport() {
        tv_sport.setTextColor(ContextCompat.getColor(context, R.color.text_color_black_common));
        iv_sport.setBackgroundResource(R.mipmap.bottombar_icon_train_selected);
        tv_circle.setTextColor(ContextCompat.getColor(context, R.color.text_color_gray_common));
        iv_circle.setBackgroundResource(R.mipmap.bottombar_icon_social_normal);
        tv_posture.setTextColor(ContextCompat.getColor(context, R.color.text_color_gray_common));
        iv_posture.setBackgroundResource(R.mipmap.bottombar_icon_posture_normal);
        tv_mine.setTextColor(ContextCompat.getColor(context, R.color.text_color_gray_common));
        iv_mine.setBackgroundResource(R.mipmap.bottombar_icon_mine_normal);
    }

    private void selectCircle() {
        tv_sport.setTextColor(ContextCompat.getColor(context, R.color.text_color_gray_common));
        iv_sport.setBackgroundResource(R.mipmap.bottombar_icon_train_normal);
        tv_circle.setTextColor(ContextCompat.getColor(context, R.color.text_color_black_common));
        iv_circle.setBackgroundResource(R.mipmap.bottombar_icon_social_selected);
        tv_posture.setTextColor(ContextCompat.getColor(context, R.color.text_color_gray_common));
        iv_posture.setBackgroundResource(R.mipmap.bottombar_icon_posture_normal);
        tv_mine.setTextColor(ContextCompat.getColor(context, R.color.text_color_gray_common));
        iv_mine.setBackgroundResource(R.mipmap.bottombar_icon_mine_normal);
    }

    private void selectPosture() {
        tv_sport.setTextColor(ContextCompat.getColor(context, R.color.text_color_gray_common));
        iv_sport.setBackgroundResource(R.mipmap.bottombar_icon_train_normal);
        tv_circle.setTextColor(ContextCompat.getColor(context, R.color.text_color_gray_common));
        iv_circle.setBackgroundResource(R.mipmap.bottombar_icon_social_normal);
        tv_posture.setTextColor(ContextCompat.getColor(context, R.color.text_color_black_common));
        iv_posture.setBackgroundResource(R.mipmap.bottombar_icon_posture_selected);
        tv_mine.setTextColor(ContextCompat.getColor(context, R.color.text_color_gray_common));
        iv_mine.setBackgroundResource(R.mipmap.bottombar_icon_mine_normal);
    }

    private void selectMine() {
        tv_sport.setTextColor(ContextCompat.getColor(context, R.color.text_color_gray_common));
        iv_sport.setBackgroundResource(R.mipmap.bottombar_icon_train_normal);
        tv_circle.setTextColor(ContextCompat.getColor(context, R.color.text_color_gray_common));
        iv_circle.setBackgroundResource(R.mipmap.bottombar_icon_social_normal);
        tv_posture.setTextColor(ContextCompat.getColor(context, R.color.text_color_gray_common));
        iv_posture.setBackgroundResource(R.mipmap.bottombar_icon_posture_normal);
        tv_mine.setTextColor(ContextCompat.getColor(context, R.color.text_color_black_common));
        iv_mine.setBackgroundResource(R.mipmap.bottombar_icon_mine_selected);
    }

    public void setIndexSelected(int index) {
        if (mIndex == index) {
            return;
        }
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction ft = fragmentManager.beginTransaction();
        //隐藏
        ft.hide(mFragments[mIndex]);
        //判断是否添加
        if (!mFragments[index].isAdded()) {
            ft.add(R.id.container, mFragments[index]).show(mFragments[index]);
        } else {
            ft.show(mFragments[index]);
        }
        ft.commit();
        //再次赋值
        mIndex = index;
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            Intent home = new Intent(Intent.ACTION_MAIN);
            home.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            home.addCategory(Intent.CATEGORY_HOME);
            startActivity(home);
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    private void uploadData() {

        PreferencesUtils.putString(this, "toDay", "");

        Calendar calendar = Calendar.getInstance();
//        calendar.set(Calendar.DATE, calendar.get(Calendar.DATE) - 1);
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        String time = dateFormat.format(calendar.getTime());
        if (DbUtils.getLiteOrm() == null) {
            DbUtils.createDb(this, "jingzhi");
        }
        //更新所有蓝牙设备
        List<iBeaconClass.iBeacon> lists = DbUtils.getQueryAll(iBeaconClass.iBeacon.class);
        for (iBeaconClass.iBeacon iBeacon : lists) {
            iBeacon.setConnect(false);
        }
        DbUtils.updateALL(lists);
        //获取前一天的数据，用于提交给后台
        List<StepData> list = DbUtils.getQueryAll(StepData.class);
        if (list.size() == 0 || list.isEmpty()) {
            Log.v("", "出错了！");
        } else if (list.size() > 0) {
            StepData stepData;
            if (list.get(list.size() - 1).getToday().equals(time)) {
                stepData = list.get(list.size() - 1);
//            } else {
//            stepData = list.get(list.size() - 1);
//            }
                String step = stepData.getStep();
                long timeUp = TimeUtil.getDayToMseconds(stepData.getToday());
                calendar.set(Calendar.DATE, calendar.get(Calendar.DATE) - 1);
                RequestParams params = new RequestParams(Url.UPLOAD_DATA_URL);
                Map<String, String> map = new HashMap<>();
                map.put("type", "0");
                map.put("step", step);
                map.put("target", "0");
                map.put("time", System.currentTimeMillis() + "");
                map.put("token", PrefName.getToken(context));
                map.put("kilometre", StepAlgorithm.getDisString(Integer.parseInt(step)));
                map.put("calorie", StepAlgorithm.getKcal(Double.parseDouble(PrefName.getWeight(context)), StepAlgorithm.getDisDouble(Integer.parseInt(step))));
                httpPostJSONNoDia("upload", params, map);
            }
        } else {
//            Log.v("", "出错了！");
        }
    }

    public void initLocation() {
        if (checkLocationPermission()) {
            if (null != circleFragment)
                circleFragment.initLocation();
        }
    }

    private String from;

    public void initLocaionSport(String from) {
        this.from = from;
        if (checkLocationPermission()) {
            if (null != sportFragment) {
                sportFragment.initByGotLocationPermission();
            }
        }
    }

    @Override
    protected void gotLocationPermissionResult(boolean isGrant) {
        super.gotLocationPermissionResult(isGrant);
        if (isGrant)
            if (StringUtil.isNotBlank(from))
                sportFragment.initByGotLocationPermission();
            else
                circleFragment.initLocation();
        else
            senToa(R.string.permission_location);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode != RESULT_OK) return;
        switch (requestCode) {
            case AVATAR_CODE:
                circleFragment.onActivityResult(requestCode, resultCode, data);
                break;
        }
    }
}
