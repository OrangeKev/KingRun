/*
 * Copyright (C) 2013 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.amobletool.bluetooth.le;

import android.annotation.SuppressLint;
import android.app.ListActivity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothGatt;
import android.bluetooth.BluetoothGattCharacteristic;
import android.bluetooth.BluetoothGattDescriptor;
import android.bluetooth.BluetoothGattService;
import android.bluetooth.BluetoothManager;
import android.bluetooth.le.ScanCallback;
import android.bluetooth.le.ScanFilter;
import android.bluetooth.le.ScanResult;
import android.bluetooth.le.ScanSettings;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.ParcelUuid;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import com.amobletool.bluetooth.le.BluetoothLeClass.OnDataAvailableListener;
import com.amobletool.bluetooth.le.BluetoothLeClass.OnServiceDiscoverListener;
import com.amobletool.bluetooth.le.iBeaconClass.iBeacon;
import com.amobletool.bluetooth.le.util.StringUtil;
import com.amoheartrate.bluetooth.le.R;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * Activity for scanning and displaying available Bluetooth LE devices.
 */
// public class DeviceScanActivity extends ListActivity implements
// View.OnClickListener
public class DeviceScanActivity extends ListActivity {
    private final static String TAG = "DeviceScanActivity";// DeviceScanActivity.class.getSimpleName();
    public static final int REFRESH = 0x000001;
    private final static int REQUEST_CODE = 1;


    public static String UUID_CHAR3 = "00002a6c-0000-1000-8000-00805f9b34fb";
    public static String UUID_CHAR4 = "00002a06-0000-1000-8000-00805f9b34fb";
    public static String UUID_CHAR5 = "00002a07-0000-1000-8000-00805f9b34fb";
    public static String UUID_CHAR6 = "00002a6d-0000-1000-8000-00805f9b34fb";
    public static String UUID_LOGIN_BUND = "6e402a06-b5a3-f393-e0a9-e50e24dcca9e";
    public static String UUID_LOGIN_SEND_POWER = "6e402a07-b5a3-f393-e0a9-e50e24dcca9e";
    public static String UUID_LOGIN_JIBU = "6e402a6d-b5a3-f393-e0a9-e50e24dcca9e";


    static BluetoothGattCharacteristic gattCharacteristic_char3_LLS = null;
    static BluetoothGattCharacteristic gattCharacteristic_char4_IAS = null;
    static BluetoothGattCharacteristic gattCharacteristic_char5_TXP = null;
    static BluetoothGattCharacteristic gattCharacteristic_char6_JIBU = null;

    private LeDeviceListAdapter mLeDeviceListAdapter = null;
    // 搜索BLE终端
    private BluetoothAdapter mBluetoothAdapter;
    // 读写BLE终端
    static private BluetoothLeClass mBLE;
    public String bluetoothAddress;
    public String strJibuCount = "";

    private boolean mScanning;
    private Handler mHandler = null;
    private Button btn;

    private MyThread mythread = null;
    private byte color = 0;

    private int mRssi;

    private static final long SCAN_PERIOD = 100000;

    @SuppressLint("HandlerLeak")
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getActionBar().setTitle("正在扫描设备中...");

        if (!getPackageManager().hasSystemFeature(
                PackageManager.FEATURE_BLUETOOTH_LE)) {
            Toast.makeText(this, R.string.ble_not_supported, Toast.LENGTH_SHORT)
                    .show();
            finish();
        } else {
            Log.i(TAG, "initialize Bluetooth, has BLE system");
        }

        // Initializes a Bluetooth adapter. For API level 18 and above, get a
        // reference to
        // BluetoothAdapter through BluetoothManager.
        final BluetoothManager bluetoothManager = (BluetoothManager) getSystemService(Context.BLUETOOTH_SERVICE);
        mBluetoothAdapter = bluetoothManager.getAdapter();

        // Checks if Bluetooth is supported on the device.
        if (mBluetoothAdapter == null) {
            Toast.makeText(this, R.string.error_bluetooth_not_supported,
                    Toast.LENGTH_SHORT).show();
            finish();
            return;
        } else {
            Log.i(TAG, "mBluetoothAdapter = " + mBluetoothAdapter);
        }

        mBluetoothAdapter.enable();
        Log.i(TAG, "mBluetoothAdapter.enable");

        mBLE = new BluetoothLeClass(this);
        if (!mBLE.initialize()) {
            Log.e(TAG, "Unable to initialize Bluetooth");
            finish();
        }
        Log.i(TAG, "mBLE = e" + mBLE);

        mBLE.setOnServiceDiscoverListener(mOnServiceDiscover);

        mBLE.setOnDataAvailableListener(mOnDataAvailable);

        mHandler = new Handler() {
            int count = 0;

            @Override
            public void handleMessage(Message msg) {
                if (msg.what == REFRESH) {
                    count++;
                    if (count == 0)
                        getActionBar().setTitle("正在拼命扫描设备中.");
                    else if (count == 1)
                        getActionBar().setTitle("正在拼命扫描设备中..");
                    else if (count == 2)
                        getActionBar().setTitle("正在拼命扫描设备中...");
                    else if (count == 3)
                        getActionBar().setTitle("正在拼命扫描设备中....");
                    else if (count == 4)
                        getActionBar().setTitle("正在拼命扫描设备中.....");
                    else if (count == 5)
                        getActionBar().setTitle("正在拼命扫描设备中......");
                    else if (count == 6)
                        getActionBar().setTitle("正在拼命扫描设备中.......");
                    else {
                        count = 0;
                        getActionBar().setTitle("正在拼命扫描设备中........");

                    }
                }
                super.handleMessage(msg);
            }
        };

        new MyThread().start();
    }

    /**
     * 设置链路断开时的警告级别
     */
    static public void writeChar3_LLS(String string) {

        Log.i(TAG, "gattCharacteristic_char3 = " + gattCharacteristic_char3_LLS);
        if (gattCharacteristic_char3_LLS != null) {
            boolean bRet = gattCharacteristic_char3_LLS.setValue(string);
            mBLE.writeCharacteristic(gattCharacteristic_char3_LLS);

        }
    }

    /**
     * 立即发出告警1或者2，让设备发出告警指示
     */
    static public void writeChar4_IAS(String string) {
        Log.i(TAG, "gattCharacteristic_char4 = " + gattCharacteristic_char4_IAS);
        if (gattCharacteristic_char4_IAS != null) {
            byte[] dataProtocol = new byte[10];
            dataProtocol[0] = 0x1;
            dataProtocol[1] = (byte) Integer.parseInt(string);
            gattCharacteristic_char4_IAS.setValue(dataProtocol);
            mBLE.writeCharacteristic(gattCharacteristic_char4_IAS);

        }
    }


    static public void writeChar4_bonding(String string) {
        Log.i(TAG, "gattCharacteristic_char4 = " + gattCharacteristic_char4_IAS);
        if (gattCharacteristic_char4_IAS != null) {

            byte[] dataProtocol = new byte[10];
            dataProtocol[0] = 0x2;
            dataProtocol[1] = 'w';
            dataProtocol[2] = 'w';
            dataProtocol[3] = 'w';
            dataProtocol[4] = 'w';
            dataProtocol[5] = 'w';
            dataProtocol[6] = 'w';
            dataProtocol[7] = 'w';
            dataProtocol[8] = 'w';
            dataProtocol[9] = 'w';
            gattCharacteristic_char4_IAS.setValue(dataProtocol);
            mBLE.writeCharacteristic(gattCharacteristic_char4_IAS);

        }
    }

    static public void writeChar4_auth(String string) {
        Log.i(TAG, "gattCharacteristic_char4 = " + gattCharacteristic_char4_IAS);
        if (gattCharacteristic_char4_IAS != null) {

            byte[] dataProtocol = new byte[10];
            dataProtocol[0] = 0x3;
            dataProtocol[1] = 'w';
            dataProtocol[2] = 'w';
            dataProtocol[3] = 'w';
            dataProtocol[4] = 'w';
            dataProtocol[5] = 'w';
            dataProtocol[6] = 'w';
            dataProtocol[7] = 'w';
            dataProtocol[8] = 'w';
            dataProtocol[9] = 'w';
            gattCharacteristic_char4_IAS.setValue(dataProtocol);
            mBLE.writeCharacteristic(gattCharacteristic_char4_IAS);

        }
    }

    static public void writeChar4_unbonding() {
        Log.i(TAG, "gattCharacteristic_char4 = " + gattCharacteristic_char4_IAS);
        if (gattCharacteristic_char4_IAS != null) {

            byte[] dataProtocol = new byte[10];
            dataProtocol[0] = 0x4;
            dataProtocol[1] = 'w';
            dataProtocol[2] = 'w';
            dataProtocol[3] = 'w';
            dataProtocol[4] = 'w';
            dataProtocol[5] = 'w';
            dataProtocol[6] = 'w';
            dataProtocol[7] = 'w';
            dataProtocol[8] = 'w';
            dataProtocol[9] = 'w';
            gattCharacteristic_char4_IAS.setValue(dataProtocol);
            mBLE.writeCharacteristic(gattCharacteristic_char4_IAS);

        }
    }

    static public void writeChar4_time(String string) {
        Log.i(TAG, "gattCharacteristic_char4 = " + gattCharacteristic_char4_IAS);
        if (gattCharacteristic_char4_IAS != null) {

            byte[] dataProtocol = new byte[10];
            dataProtocol[0] = 0x0;
            dataProtocol[1] = (byte) Integer.parseInt("80", 16);
            dataProtocol[2] = (byte) Integer.parseInt("99", 16);
            dataProtocol[3] = (byte) Integer.parseInt("40", 16);
            dataProtocol[4] = (byte) Integer.parseInt("00", 16);
            dataProtocol[5] = 0;
            dataProtocol[6] = 0;
            dataProtocol[7] = 0;
            dataProtocol[8] = 0;
            dataProtocol[9] = 0;
            gattCharacteristic_char4_IAS.setValue(dataProtocol);
            mBLE.writeCharacteristic(gattCharacteristic_char4_IAS);

        }
    }

    /*读出设备的发射功率*/
    static public void read_char5_txp() {
        byte[] writeValue = new byte[1];
        Log.i(TAG, "readCharacteristic = ");
        if (gattCharacteristic_char5_TXP != null) {
            mBLE.readCharacteristic(gattCharacteristic_char5_TXP);
        }

    }

    public class MyThread extends Thread {
        public void run() {
            while (!Thread.currentThread().isInterrupted()) {

                Message msg = new Message();
                msg.what = REFRESH;
                //mHandler.sendMessage(msg);
                try {
                    Thread.sleep(200);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public void DisplayStart() {
        Log.i(TAG, "DisplayStart+++");

        if (mythread == null) {
            mythread = new MyThread();
            mythread.start();

        } else {

        }
    }


    static public void getRssi() {
        if (mBLE.mBluetoothGatt.connect()) {
            Log.i(TAG, "readRemoteRssi");
            mBLE.mBluetoothGatt.readRemoteRssi();
        }
    }

    public void DisplayStop() {
        if (mythread != null) {
            // mythread.setThread(false);
            // delay(3000);
        }
        Log.i(TAG, "DisplayStop---");
    }

    @Override
    protected void onResume() {
        Log.i(TAG, "---> onResume");
        super.onResume();
        mBLE.close();
        // Initializes list view adapter.
        mLeDeviceListAdapter = new LeDeviceListAdapter(this);
        setListAdapter(mLeDeviceListAdapter);
        scanLeDevice(true);
    }

    @Override
    protected void onPause() {
        Log.i(TAG, "---> onPause");
        super.onPause();
        // scanLeDevice(false);
        // mLeDeviceListAdapter.clear();
        // mBLE.disconnect();
    }

    @Override
    protected void onStop() {
        Log.i(TAG, "---> onStop");
        super.onStop();
        DisplayStop();
        // mBLE.close();
    }

    @Override
    protected void onDestroy() {
        Log.i(TAG, "---> onDestroy");
        super.onDestroy();
        Log.e(TAG, "start onDestroy~~~");
        scanLeDevice(false);
        mBLE.disconnect();
        mBLE.close();
    }

    @Override
    protected void onListItemClick(ListView l, View v, int position, long id) {
        final iBeacon device = mLeDeviceListAdapter.getDevice(position);
        if (device == null)
            return;
        if (mScanning) {
            mBluetoothAdapter.stopLeScan(mLeScanCallback);
            mScanning = false;
        }

        mRssi = device.rssi;

        Log.i(TAG, "mBluetoothAdapter.enable");
        bluetoothAddress = device.bluetoothAddress;
        boolean bRet = mBLE.connect(device.bluetoothAddress);

        Log.i(TAG, "connect bRet = " + bRet);

        Toast toast = Toast.makeText(getApplicationContext(), "正在连接设备并获取服务中",
                Toast.LENGTH_LONG);
        toast.setGravity(Gravity.CENTER, 0, 0);
        toast.show();
    }

    private void scanLeDevice(final boolean enable) {
        if (enable) {
            mScanning = true;
//            UUID u = UUID.fromString(UUID_CHAR3);
//            UUID u2 = UUID.fromString(UUID_CHAR4);
//            UUID u3 = UUID.fromString(UUID_CHAR5);
//            UUID u4 = UUID.fromString(UUID_CHAR6);
//            UUID[] us = new UUID[]{u, u2, u3, u4};
//            UUID uuid = UUID.randomUUID("1802");
//            System.out.println(uuid);

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                String uuid1 = "0000180f-0000-1000-8000-00805f9b34fb";
//                String uuid2 = "0000180a-0000-1000-8000-00805f9b34fb";
                ScanFilter scanFilter1 = new ScanFilter.Builder().setServiceUuid(new ParcelUuid(UUID.fromString(uuid1))).build();
//                ScanFilter scanFilter2 = new ScanFilter.Builder().setServiceUuid(new ParcelUuid(UUID.fromString(uuid2))).build();
                ScanSettings scanSettings = new ScanSettings.Builder().build();
                List<ScanFilter> scanFilters = new ArrayList<>();
                scanFilters.add(scanFilter1);
//                scanFilters.add(scanFilter2);
                mBluetoothAdapter.getBluetoothLeScanner().startScan(scanFilters, scanSettings, scanCallback);
            } else {
                mBluetoothAdapter.startLeScan(mLeScanCallback);
            }
        } else {
            mScanning = false;
            mBluetoothAdapter.stopLeScan(mLeScanCallback);
        }
        invalidateOptionsMenu();
    }

    /**
     * callback for discover the ble devices
     */
    private BluetoothLeClass.OnServiceDiscoverListener mOnServiceDiscover = new OnServiceDiscoverListener() {

        @Override
        public void onServiceDiscover(BluetoothGatt gatt) {
            displayGattServices(mBLE.getSupportedGattServices());
        }

    };


    private BluetoothLeClass.OnDataAvailableListener mOnDataAvailable = new OnDataAvailableListener() {
        @Override
        public void onCharacteristicRead(BluetoothGatt gatt,
                                         BluetoothGattCharacteristic characteristic, int status) {
            Log.e(TAG,
                    "onCharRead " + gatt.getDevice().getName() + " read "
                            + characteristic.getUuid().toString() + " -> "
                            + Utils.bytesToHexString(characteristic.getValue()));

            AnichipsFindmeActivity.char6_display(Utils.bytesToString(characteristic
                    .getValue()), characteristic.getValue(), characteristic
                    .getUuid().toString());
        }

        @Override
        public void onCharacteristicWrite(BluetoothGatt gatt,
                                          BluetoothGattCharacteristic characteristic) {
            Log.e(TAG, "onCharWrite " + gatt.getDevice().getName() + " write "
                    + characteristic.getUuid().toString() + " -> "
                    + new String(characteristic.getValue()));


            AnichipsFindmeActivity.char6_display(Utils.bytesToString(characteristic
                    .getValue()), characteristic.getValue(), characteristic
                    .getUuid().toString());
        }

        public void onReadRemoteRssi(BluetoothGatt gatt, int rssi, int status) {
            Log.i(TAG, "receive rss cb " + rssi);
            AnichipsFindmeActivity.rssi_display(rssi);
        }


    };
    private ScanCallback scanCallback = new ScanCallback() {
        @Override
        public void onScanResult(int callbackType, ScanResult result) {
            super.onScanResult(callbackType, result);
            final List<ParcelUuid> listS = result.getScanRecord().getServiceUuids();
            final iBeacon ibeacon = iBeaconClass.fromScanData(result.getDevice(), result.getRssi(),
                    result.getScanRecord().getBytes());
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
//                    if (null != ibeacon && StringUtil.isNotBlank(ibeacon.name)
//                            && ibeacon.name.contains("shoe")) {
//                         printHexString(scanRecord);
//                        String uuid = new BigInteger(1, scanRecord).toString(16);
                        for (int i = 0; i < listS.size(); i++) {
                            System.out.println("uuid" + listS.get(i).getUuid());
                        }

                        mLeDeviceListAdapter.addDevice(ibeacon);
                        mLeDeviceListAdapter.notifyDataSetChanged();
                    }
//                }
            });

            // rssi
//            Log.i(TAG, "rssi = " + rssi);
//            Log.i(TAG, "mac = " + device.getAddress());
//            Log.i(TAG, "scanRecord.length = " + scanRecord.length);
        }

        @Override
        public void onBatchScanResults(List<ScanResult> results) {
            super.onBatchScanResults(results);
        }

        @Override
        public void onScanFailed(int errorCode) {
            super.onScanFailed(errorCode);
        }
    };

    // Device scan callback.
    private BluetoothAdapter.LeScanCallback mLeScanCallback = new BluetoothAdapter.LeScanCallback() {

        @Override
        public void onLeScan(final BluetoothDevice device, int rssi,
                             final byte[] scanRecord) {

            final iBeacon ibeacon = iBeaconClass.fromScanData(device, rssi,
                    scanRecord);
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    if (null != ibeacon && StringUtil.isNotBlank(ibeacon.name)
                            && ibeacon.name.contains("Ani")) {
//                         printHexString(scanRecord);
//                        String uuid = new BigInteger(1, scanRecord).toString(16);
//                        System.out.println(uuid);
                        mLeDeviceListAdapter.addDevice(ibeacon);
                        mLeDeviceListAdapter.notifyDataSetChanged();
                    }
                }
            });

            // rssi
            Log.i(TAG, "rssi = " + rssi);
            Log.i(TAG, "mac = " + device.getAddress());
            Log.i(TAG, "scanRecord.length = " + scanRecord.length);
        }
    };

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

    private void displayGattServices(List<BluetoothGattService> gattServices) {
        if (gattServices == null)
            return;
        BluetoothGattCharacteristic Characteristic_cur = null;

        for (BluetoothGattService gattService : gattServices) {

            int type = gattService.getType();
            Log.e(TAG, "-->service type:" + Utils.getServiceType(type));
            Log.e(TAG, "-->includedServices size:"
                    + gattService.getIncludedServices().size());
            Log.e(TAG, "-->service uuid:" + gattService.getUuid());


            List<BluetoothGattCharacteristic> gattCharacteristics = gattService
                    .getCharacteristics();
            for (final BluetoothGattCharacteristic gattCharacteristic : gattCharacteristics) {
                Log.e(TAG, "---->char uuid:" + gattCharacteristic.getUuid());

                int permission = gattCharacteristic.getPermissions();
                Log.e(TAG,
                        "---->char permission:"
                                + Utils.getCharPermission(permission));

                int property = gattCharacteristic.getProperties();
                Log.e(TAG,
                        "---->char property:"
                                + Utils.getCharPropertie(property));

                byte[] data = gattCharacteristic.getValue();
                if (data != null && data.length > 0) {
                    Log.e(TAG, "---->char value:" + new String(data));
                }

                Log.i("wyqhuangxin", gattCharacteristic.getUuid().toString());


                if (gattCharacteristic.getUuid().toString().equals(UUID_CHAR3)) {
                    gattCharacteristic_char3_LLS = gattCharacteristic;
                    Characteristic_cur = gattCharacteristic;

                }

                if (gattCharacteristic.getUuid().toString().equals(UUID_LOGIN_BUND)) {
                    gattCharacteristic_char4_IAS = gattCharacteristic;
                    Characteristic_cur = gattCharacteristic;
                }

                if (gattCharacteristic.getUuid().toString().equals(UUID_LOGIN_SEND_POWER)) {
                    gattCharacteristic_char5_TXP = gattCharacteristic;
                    Characteristic_cur = gattCharacteristic;
                }

                if (gattCharacteristic.getUuid().toString().equals(UUID_LOGIN_JIBU)) {
                    gattCharacteristic_char6_JIBU = gattCharacteristic;
                    Characteristic_cur = gattCharacteristic;
//                    strJibuCount = gattCharacteristic.getValue().toString();
                    mBLE.setCharacteristicNotification(gattCharacteristic, true);

                    Log.i(TAG, "+++++++++UUID_JIBU");
                }

                List<BluetoothGattDescriptor> gattDescriptors = gattCharacteristic.getDescriptors();

                for (BluetoothGattDescriptor gattDescriptor : gattDescriptors) {
                    Log.e(TAG, "-------->desc uuid:" + gattDescriptor.getUuid());
                    int descPermission = gattDescriptor.getPermissions();
                    Log.e(TAG,
                            "-------->desc permission:"
                                    + Utils.getDescPermission(descPermission));

                    byte[] desData = gattDescriptor.getValue();
                    Log.e(TAG,
                            "-------->lalalantf:");
                    if (desData != null && desData.length > 0) {
//                        strJibuCount = gattCharacteristic.getValue().toString();
//                        Log.e(TAG, "-------->desc value:" + new String(desData));
                    }
                    Log.e(TAG,
                            "-------->lalalafinish:");
                }
            }
        }//

        Intent intent = new Intent();
        intent.setClass(DeviceScanActivity.this, AnichipsFindmeActivity.class);
        intent.putExtra("mac_addr", bluetoothAddress);

        intent.putExtra("char_uuid", mRssi + "");
        intent.putExtra("jibuCount", strJibuCount);
        startActivityForResult(intent, REQUEST_CODE);

    }
}
