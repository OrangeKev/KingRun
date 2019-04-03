package com.king.run.receiver;

import android.app.Service;
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
import android.os.Binder;
import android.os.Build;
import android.os.Handler;
import android.os.IBinder;
import android.os.ParcelUuid;
import android.util.Log;
import android.view.Gravity;
import android.widget.Toast;

import com.king.run.R;
import com.king.run.activity.posture.BlueTooth.BluetoothLeClass;
import com.king.run.activity.posture.model.iBeaconClass;
import com.king.run.util.PrefName;
import com.king.run.util.Utils;
import com.orhanobut.logger.Logger;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * 作者：shuizi_wade on 2018/3/22 14:13
 * 邮箱：674618016@qq.com
 */
public class BlueToothConnectService extends Service {
    private final static String TAG = "BuleToothConnectService";// DeviceScanActivity.class.getSimpleName();
    public MyBinder myBinder = new MyBinder();
    public static String UUID_CHAR3 = "00002a6c-0000-1000-8000-00805f9b34fb";
    public static String UUID_LOGIN_BUND = "6e402a06-b5a3-f393-e0a9-e50e24dcca9e";
    public static String UUID_LOGIN_SEND_POWER = "6e402a07-b5a3-f393-e0a9-e50e24dcca9e";
    public static String UUID_LOGIN_JIBU = "6e402a6d-b5a3-f393-e0a9-e50e24dcca9e";

    static BluetoothGattCharacteristic gattCharacteristic_char3_LLS = null;
    static BluetoothGattCharacteristic gattCharacteristic_char4_IAS = null;
    static BluetoothGattCharacteristic gattCharacteristic_char5_TXP = null;
    static BluetoothGattCharacteristic gattCharacteristic_char6_JIBU = null;

    //    private LeDeviceListAdapter mLeDeviceListAdapter = null;
    // 搜索BLE终端
    private BluetoothAdapter mBluetoothAdapter;
    // 读写BLE终端
    static private BluetoothLeClass mBLE;
    static int flag = 0;
    static Handler mHandler = new Handler();
    static int rssiUpdate = 0;
    static String strJibu;
    static String strWriteRsp;
    static String strTxp;
    private boolean mScanning;
    private int mRssi;
    public String bluetoothAddress;
    private boolean isAuth = true;
    private iBeaconClass.iBeacon mIBeacon;

    @Override
    public IBinder onBind(Intent intent) {
        init();
        return myBinder;
    }

    private void init() {
        if (!getPackageManager().hasSystemFeature(
                PackageManager.FEATURE_BLUETOOTH_LE)) {
            Toast.makeText(this, R.string.ble_not_supported, Toast.LENGTH_LONG).show();
        } else {
            Log.i(TAG, "initialize Bluetooth, has BLE system");
        }
        final BluetoothManager bluetoothManager = (BluetoothManager) getSystemService(Context.BLUETOOTH_SERVICE);
        mBluetoothAdapter = bluetoothManager.getAdapter();

        // Checks if Bluetooth is supported on the device.
        if (mBluetoothAdapter == null) {
            Toast.makeText(this, R.string.error_bluetooth_not_supported, Toast.LENGTH_LONG).show();
            return;
        } else {
            Log.i(TAG, "mBluetoothAdapter = " + mBluetoothAdapter);
        }
        mBluetoothAdapter.enable();
        Log.i(TAG, "mBluetoothAdapter.enable");

        mBLE = new BluetoothLeClass(this);
        if (!mBLE.initialize()) {
            Log.e(TAG, "Unable to initialize Bluetooth");
//            finish();
        }
        Log.i(TAG, "mBLE = e" + mBLE);

        mBLE.setOnServiceDiscoverListener(mOnServiceDiscover);

        mBLE.setOnDataAvailableListener(mOnDataAvailable);
        scanLeDevice(true);
    }

    private void scanLeDevice(final boolean enable) {
        if (enable) {
            mScanning = true;
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
//        invalidateOptionsMenu();
    }

    /**
     * callback for discover the ble devices
     */
    private BluetoothLeClass.OnServiceDiscoverListener mOnServiceDiscover = new BluetoothLeClass.OnServiceDiscoverListener() {

        @Override
        public void onServiceDiscover(BluetoothGatt gatt) {
            displayGattServices(mBLE.getSupportedGattServices());
        }
    };


    private BluetoothLeClass.OnDataAvailableListener mOnDataAvailable = new BluetoothLeClass.OnDataAvailableListener() {
        @Override
        public void onCharacteristicRead(BluetoothGatt gatt,
                                         BluetoothGattCharacteristic characteristic, int status) {
            Log.e(TAG,
                    "onCharRead " + gatt.getDevice().getName() + " read "
                            + characteristic.getUuid().toString() + " -> "
                            + Utils.bytesToHexString(characteristic.getValue()));

            char6_display(Utils.bytesToString(characteristic
                    .getValue()), characteristic.getValue(), characteristic
                    .getUuid().toString());
        }

        @Override
        public void onCharacteristicWrite(BluetoothGatt gatt,
                                          BluetoothGattCharacteristic characteristic) {
            Log.e(TAG, "onCharWrite " + gatt.getDevice().getName() + " write "
                    + characteristic.getUuid().toString() + " -> "
                    + new String(characteristic.getValue()));
            if (isAuth) {
                isAuth = false;
                myBinder.writeChar4_auth();
            }
            char6_display(Utils.bytesToString(characteristic
                    .getValue()), characteristic.getValue(), characteristic
                    .getUuid().toString());
        }

        public void onReadRemoteRssi(BluetoothGatt gatt, int rssi, int status) {
            Log.i(TAG, "receive rss cb " + rssi);
            rssi_display(rssi);
        }


    };

    @Override
    public void onDestroy() {
        super.onDestroy();
        mBLE.disconnect();
        mBLE.close();
        scanLeDevice(false);
    }

    private ScanCallback scanCallback = new ScanCallback() {
        @Override
        public void onScanResult(int callbackType, ScanResult result) {
            super.onScanResult(callbackType, result);
            final List<ParcelUuid> listS = result.getScanRecord().getServiceUuids();
            final iBeaconClass.iBeacon ibeacon = iBeaconClass.fromScanData(result.getDevice(), result.getRssi(),
                    result.getScanRecord().getBytes());
            connectBlueTooth(ibeacon);
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
            final iBeaconClass.iBeacon ibeacon = iBeaconClass.fromScanData(device, rssi, scanRecord);
            new Thread(new Runnable() {
                @Override
                public void run() {
                    connectBlueTooth(ibeacon);
                }
            }).start();
        }
    };

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
        }
        Intent intent = new Intent(PrefName.BLUETOOTH_CONNECT_ACTION);
        intent.putExtra("iBeacon", (Serializable) mIBeacon);
        sendBroadcast(intent);
//        writeChar4_bonding();
    }

    private void connectBlueTooth(iBeaconClass.iBeacon device) {
        mIBeacon = device;
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

    public static synchronized void char6_display(String str, byte[] data,
                                                  String uuid) {
        if (uuid.equals(UUID_LOGIN_SEND_POWER)) {
            byte[] midbytes = str.getBytes();
            strTxp = Utils.bytesToHexString(midbytes);
            Log.i(TAG, "txpread = " + str);
            flag = 1;
        }
        if (uuid.equals(UUID_LOGIN_JIBU)) {
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
//                    update_display_TXP_info(strTxp);
                    flag = 0;
                }
                if (flag == 2) {
//                    update_display_JIBU_info(strJibu);
                    flag = 0;
                }
                if (flag == 3) {
//                    update_display_rsp_info(strWriteRsp);
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
//                tv_char_uuid.setText("RSSI:" + rssiUpdate);
            }
        });
    }


    public class MyBinder extends Binder {
        public BlueToothConnectService getService() {
            return BlueToothConnectService.this;
        }

        public void writeChar4_bonding() {
            Log.i(TAG, "writeChar4_bonding;gattCharacteristic_char4 = " + gattCharacteristic_char4_IAS);
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

        public void writeChar4_auth() {
            Log.i(TAG, "writeChar4_auth;gattCharacteristic_char4 = " + gattCharacteristic_char4_IAS);
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
    }
}
