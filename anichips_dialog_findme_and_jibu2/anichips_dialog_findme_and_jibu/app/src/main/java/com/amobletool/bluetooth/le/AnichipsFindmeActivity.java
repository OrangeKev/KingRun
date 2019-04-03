package com.amobletool.bluetooth.le;


import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.ScrollView;
import android.widget.TextView;

import com.amoheartrate.bluetooth.le.R;

public class AnichipsFindmeActivity extends Activity implements View.OnClickListener {

    private final static String TAG = "FindmeActivity";

    static TextView Text_Recv;
    static String Str_Recv;

    static String ReciveStr;
    static ScrollView scrollView;
    static Handler mHandler = new Handler();

    static TextView textview_recive_send_info;
    static TextView tv_char_txp;
    static int Totol_Send_bytes = 0;
    static int Totol_recv_bytes = 0;
    static String SendString = "1";
    static String strTxp;
    static int flag = 0;
    static TextView tv_char_uuid;
    static int rssiUpdate = 0;
    static TextView tv_char_jibu;
    static TextView tv_char_response;
    static String strJibu;
    static String strWriteRsp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        setContentView(R.layout.other);
        getActionBar().setTitle("anichips-dialog-findme-demo");

        findViewById(R.id.button_send).setOnClickListener(this);
        findViewById(R.id.button_bonding).setOnClickListener(this);
        findViewById(R.id.button_auth).setOnClickListener(this);
        findViewById(R.id.button_time).setOnClickListener(this);
        findViewById(R.id.button_about).setOnClickListener(this);
        findViewById(R.id.button_rssi).setOnClickListener(this);
        findViewById(R.id.button_unbonding).setOnClickListener(this);


        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();
        String mac_addr = bundle.getString("mac_addr");
        String char_uuid = bundle.getString("char_uuid");
        String strJibuCount = bundle.getString("jibuCount");

        TextView tv_mac_addr = (TextView) this
                .findViewById(R.id.textview_mac_addr);
        tv_char_uuid = (TextView) this
                .findViewById(R.id.textview_char_uuid);
        tv_char_txp = (TextView) this
                .findViewById(R.id.textview_txp);
        tv_char_jibu = (TextView) this
                .findViewById(R.id.textview_char_jibu);
        tv_char_response = (TextView) this
                .findViewById(R.id.textview_char_response);

        tv_mac_addr.setText("DEVICE:" + mac_addr);
        tv_char_uuid.setText("RSSI:" + char_uuid);
        tv_char_txp.setText("DIALOG TX POWER:" + "");
        tv_char_jibu.setText("DIALOG JIBU:" + strJibuCount);
        tv_char_response.setText("write rsp:");

        textview_recive_send_info = (TextView) this
                .findViewById(R.id.textview_recive_send_info);

        TextView text2 = (TextView) this.findViewById(R.id.edit_text);
        text2.setText(SendString);

        Totol_Send_bytes = 0;
        Totol_recv_bytes = 0;
        update_display_send_recv_info(Totol_Send_bytes, Totol_recv_bytes);

    }

    @Override
    public void onClick(View arg0) {
        // TODO Auto-generated method stub
        switch (arg0.getId()) {
            case R.id.button_send:
                TextView text2 = (TextView) this.findViewById(R.id.edit_text);
                if (text2.length() > 0) {
                    String s1 = text2.getText().toString();
                    DeviceScanActivity.writeChar4_IAS(s1);
                    Totol_Send_bytes += s1.length();
                    update_display_send_recv_info(Totol_Send_bytes,
                            Totol_recv_bytes);


                }
                break;

            case R.id.button_about:
                DeviceScanActivity.read_char5_txp();
                break;

            case R.id.button_bonding:
                DeviceScanActivity.writeChar4_bonding("");
                break;
            case R.id.button_time:
                DeviceScanActivity.writeChar4_time("");
                break;
            case R.id.button_auth:
                DeviceScanActivity.writeChar4_auth("");
                break;

            case R.id.button_rssi:
                DeviceScanActivity.getRssi();
                break;

            case R.id.button_unbonding:
                DeviceScanActivity.writeChar4_unbonding();
                break;

        }
    }

    public static synchronized void char6_display(String str, byte[] data,
                                                  String uuid) {


         if (uuid.equals(DeviceScanActivity.UUID_LOGIN_SEND_POWER)) {

            byte[] midbytes = str.getBytes();
            strTxp = Utils.bytesToHexString(midbytes);
            Log.i(TAG, "txpread = " + str);
            flag = 1;
        }

        if (uuid.equals(DeviceScanActivity.UUID_LOGIN_JIBU)) {
            int time_tick;
            int jibuCount;
            if (data[0] == 0) {
                time_tick = (data[1] & 0xff) | ((data[2] << 8) & 0xff00) | ((data[3] << 24) >>> 8) | (data[4] << 24);
                jibuCount = (data[5] & 0xff) | ((data[6] << 8) & 0xff00) | ((data[7] << 24) >>> 8) | (data[8] << 24);//byteArrayToInt(data);
                strJibu = "" + time_tick + ":" + jibuCount;
                Log.i(TAG, "jibunotify = " + strJibu);
                flag = 2;
            } else if (data[0] == 1) {
                strWriteRsp = "" + data[1];
                flag = 3;
            }
            //ByteArrayInputStream bintput = new ByteArrayInputStream(data);
            //DataInputStream dintput = new DataInputStream(bintput);
            //jibuCount = dintput.readInt();
        }

        mHandler.post(new Runnable() {
            @Override
            public synchronized void run() {
                if (flag == 1) {
                    update_display_TXP_info(strTxp);
                    flag = 0;
                }

                if (flag == 2) {
                    update_display_JIBU_info(strJibu);
                    flag = 0;
                }

                if (flag == 3) {
                    update_display_rsp_info(strWriteRsp);
                    flag = 0;
                }

            }
        });
    }


    public static synchronized void rssi_display(int rssi) {

        Log.i(TAG, "rssi-get = " + rssi);
        rssiUpdate = rssi;
        mHandler.post(new Runnable() {
            @Override
            public synchronized void run() {
                tv_char_uuid.setText("RSSI:" + rssiUpdate);
            }
        });
    }

    public synchronized static String GetLastData() {
        String string = Str_Recv;
        return string;
    }

    public synchronized static void update_display_send_recv_info(int send,
                                                                  int recv) {
        String info1 = String.format("共发送%4d 次告警命令[1或2]", send);
        textview_recive_send_info.setText(info1);
    }

    public synchronized static void update_display_TXP_info(String txp) {
        tv_char_txp.setText("DIALOG TX POWER:" + txp);
    }

    public synchronized static void update_display_JIBU_info(String jibu) {
        tv_char_jibu.setText("DIALOG jibu:" + jibu);
    }

    public synchronized static void update_display_rsp_info(String rsp) {
        tv_char_response.setText("write rsp:" + rsp);
    }

    public static int byteArrayToInt(byte[] res) {
        int targets = (res[0] & 0xff) | ((res[1] << 8) & 0xff00) // | 表示安位或
                | ((res[2] << 24) >>> 8) | (res[3] << 24);
        return targets;
    }

}
