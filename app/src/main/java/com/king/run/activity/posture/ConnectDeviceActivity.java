package com.king.run.activity.posture;

import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.LinearInterpolator;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.king.run.R;
import com.king.run.activity.posture.model.iBeaconClass;
import com.king.run.base.BaseActivity;
import com.king.run.receiver.BlueToothConnectService;
import com.king.run.util.PrefName;
import com.litesuits.orm.LiteOrm;

import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.Event;
import org.xutils.view.annotation.ViewInject;

import java.util.List;

import steps.teller.step.utils.DbUtils;

@ContentView(R.layout.activity_connect_device)
public class ConnectDeviceActivity extends BaseActivity {

    @ViewInject(R.id.iv_connect)
    ImageView iv_connect;
    @ViewInject(R.id.tv_connect)
    TextView tv_connect;
    @ViewInject(R.id.ly_select)
    LinearLayout ly_select;
    @ViewInject(R.id.btn_start_sport)
    Button btn_start_sport;
    private BlueToothConnectReceiver blueToothConnectReceiver;
    private BlueToothConnectService.MyBinder myBinder;
    private String action;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        init();
        initViews();
        initBlueService();
        initBlueToothConnectReceiver();
    }

    private void initBlueToothConnectReceiver() {
        //动态注册本地广播接收器
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(PrefName.BLUETOOTH_CONNECT_ACTION);
        blueToothConnectReceiver = new BlueToothConnectReceiver();
        registerReceiver(blueToothConnectReceiver, intentFilter);
    }

    private void initBlueService() {
        Intent intent = new Intent();
        intent.setClass(context, BlueToothConnectService.class);
        startService(intent);
        bindService(intent, mServiceConnection, Context.BIND_AUTO_CREATE);
    }

    private void initViews() {
        action = getIntent().getAction();
        startAnimotionClockWise(iv_connect);
        ly_select.setVisibility(View.GONE);
        btn_start_sport.setVisibility(View.GONE);
        tv_connect.setVisibility(View.VISIBLE);
//        new Thread(new ThreadShow()).start();
    }

    @Override
    protected void onDestroy() {
        unbindService(mServiceConnection);
        unregisterReceiver(blueToothConnectReceiver);
        blueToothConnectReceiver = null;
        super.onDestroy();
    }

    @Event(value = {R.id.btn_start_sport})
    private void getEvent(View view) {
        switch (view.getId()) {
            case R.id.btn_start_sport:
                if (action.equals("list"))
                    setResult(RESULT_OK);
                else
                    jumpActvity(MyShoesActivity.class);
                finish();
                break;

        }
    }


    public String printHexString(byte[] b) {
        String a = "";
        for (int i = 0; i < b.length; i++) {
            String hex = Integer.toHexString(b[i] & 0xFF);
            if (hex.length() == 1) {
                hex = '0' + hex;
            }

            a = a + hex;
        }
        return a;
    }

    /**
     * 顺时针
     *
     * @param view
     */
    private void startAnimotionClockWise(ImageView view) {
        //动画
        Animation animation = AnimationUtils.loadAnimation(context, R.anim.img_animation_clockwise);
        LinearInterpolator lin = new LinearInterpolator();//设置动画匀速运动
        animation.setInterpolator(lin);
        view.startAnimation(animation);
    }

//    @SuppressLint("HandlerLeak")
//    Handler handler = new Handler() {
//        public void handleMessage(Message msg) {
//            switch (msg.what) {
//                case 1:
//                    ly_select.setVisibility(View.VISIBLE);
//                    btn_start_sport.setVisibility(View.VISIBLE);
//                    tv_connect.setVisibility(View.GONE);
//                    iv_connect.clearAnimation();
//                    break;
//            }
//        }
//    };

    private ServiceConnection mServiceConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            myBinder = (BlueToothConnectService.MyBinder) service;
            Log.d("", "Service与Activity已连接");
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {

        }
    };

    class BlueToothConnectReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            iBeaconClass.iBeacon iBeacon = (iBeaconClass.iBeacon) intent.getSerializableExtra("iBeacon");
            iBeacon.setConnect(true);
            LiteOrm liteOrm = DbUtils.getLiteOrm();
            if (liteOrm != null) {
                List<iBeaconClass.iBeacon> list = DbUtils.getQueryAll(iBeaconClass.iBeacon.class);
                boolean isContains = false;
                for (iBeaconClass.iBeacon iBeacon1 : list) {
                    if (iBeacon1.bluetoothAddress.equals(iBeacon.bluetoothAddress)) {
                        isContains = true;
                    } else isContains = false;
                }
                if (isContains) {
                    DbUtils.update(iBeacon);
                } else
                    liteOrm.insert(iBeacon);
            }
            ly_select.setVisibility(View.VISIBLE);
            btn_start_sport.setVisibility(View.VISIBLE);
            tv_connect.setVisibility(View.GONE);
            iv_connect.clearAnimation();
            myBinder.writeChar4_bonding();
        }
    }

//    // 线程类
//    class ThreadShow implements Runnable {
//
//        @Override
//        public void run() {
//            // TODO Auto-generated method stub
//            while (true) {
//                try {
//                    Thread.sleep(2000);
//                    Message msg = new Message();
//                    msg.what = 1;
//                    handler.sendMessage(msg);
//                    System.out.println("send...");
//                } catch (Exception e) {
//                    // TODO Auto-generated catch block
//                    e.printStackTrace();
//                    System.out.println("thread error...");
//                }
//            }
//        }
//    }
}
