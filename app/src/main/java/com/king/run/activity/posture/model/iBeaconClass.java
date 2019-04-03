package com.king.run.activity.posture.model;

import android.bluetooth.BluetoothDevice;
import android.util.Log;

import com.king.run.util.Utils;
import com.litesuits.orm.db.annotation.Column;
import com.litesuits.orm.db.annotation.PrimaryKey;
import com.litesuits.orm.db.annotation.Table;
import com.litesuits.orm.db.enums.AssignType;

import java.io.Serializable;

/**
 * 代码改自https://github.com/RadiusNetworks/android-ibeacon-service/blob/master/src
 * /main/java/com/radiusnetworks/ibeacon/IBeacon.java
 *
 * @author gvzhang
 */
public class iBeaconClass {
    private final static String TAG = "iBeaconClass";

    @Table("iBeacon")
    static public class iBeacon implements Serializable {
        // 指定自增，每个对象需要有一个主键
        @PrimaryKey(AssignType.AUTO_INCREMENT)
        private int id;
        @Column("name")
        public String name;
        @Column("major")
        public int major;
        @Column("minor")
        public int minor;
        @Column("proximityUuid")
        public String proximityUuid;
        @Column("bluetoothAddress")
        public String bluetoothAddress;
        @Column("txPower")
        public int txPower;
        @Column("rssi")
        public int rssi;
        @Column("isIbeacon")
        public boolean isIbeacon;
        @Column("isConnect")
        public boolean isConnect;

        public boolean isConnect() {
            return isConnect;
        }

        public void setConnect(boolean connect) {
            isConnect = connect;
        }

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public int getMajor() {
            return major;
        }

        public void setMajor(int major) {
            this.major = major;
        }

        public int getMinor() {
            return minor;
        }

        public void setMinor(int minor) {
            this.minor = minor;
        }

        public String getProximityUuid() {
            return proximityUuid;
        }

        public void setProximityUuid(String proximityUuid) {
            this.proximityUuid = proximityUuid;
        }

        public String getBluetoothAddress() {
            return bluetoothAddress;
        }

        public void setBluetoothAddress(String bluetoothAddress) {
            this.bluetoothAddress = bluetoothAddress;
        }

        public int getTxPower() {
            return txPower;
        }

        public void setTxPower(int txPower) {
            this.txPower = txPower;
        }

        public int getRssi() {
            return rssi;
        }

        public void setRssi(int rssi) {
            this.rssi = rssi;
        }

        public boolean isIbeacon() {
            return isIbeacon;
        }

        public void setIbeacon(boolean ibeacon) {
            isIbeacon = ibeacon;
        }
    }

    public static iBeacon fromScanData(BluetoothDevice device, int rssi,
                                       byte[] scanData) {

        int startByte = 2;
        boolean patternFound = false;

        Log.i(TAG, "rssi = " + rssi);
        Log.i(TAG, "scanData.length = " + scanData.length);

        while (startByte <= 5) {
            if (((int) scanData[startByte + 2] & 0xff) == 0x02
                    && ((int) scanData[startByte + 3] & 0xff) == 0x15) {
                // yes! This is an iBeacon
                patternFound = true;
                break;
            } else if (((int) scanData[startByte] & 0xff) == 0x2d
                    && ((int) scanData[startByte + 1] & 0xff) == 0x24
                    && ((int) scanData[startByte + 2] & 0xff) == 0xbf
                    && ((int) scanData[startByte + 3] & 0xff) == 0x16) {
                iBeacon iBeacon = new iBeacon();
                iBeacon.major = 0;
                iBeacon.minor = 0;
                iBeacon.proximityUuid = "00000000-0000-0000-0000-000000000000";
                iBeacon.txPower = -55;
                iBeacon.isIbeacon = patternFound;
                return iBeacon;
            } else if (((int) scanData[startByte] & 0xff) == 0xad
                    && ((int) scanData[startByte + 1] & 0xff) == 0x77
                    && ((int) scanData[startByte + 2] & 0xff) == 0x00
                    && ((int) scanData[startByte + 3] & 0xff) == 0xc6) {

                iBeacon iBeacon = new iBeacon();
                iBeacon.major = 0;
                iBeacon.minor = 0;
                iBeacon.proximityUuid = "00000000-0000-0000-0000-000000000000";
                iBeacon.txPower = -55;
                iBeacon.isIbeacon = patternFound;
                return iBeacon;
            }
            startByte++;
        }

        if (patternFound == false) {
            // This is not an iBeacon
            if (device != null) {
                iBeacon iBeacon = new iBeacon();

                iBeacon.major = 0;
                iBeacon.minor = 0;
                iBeacon.proximityUuid = Utils.bytesToHexString(scanData);
                iBeacon.txPower = 0;
                iBeacon.rssi = rssi;
                iBeacon.bluetoothAddress = device.getAddress();
                iBeacon.name = device.getName();
                iBeacon.isIbeacon = patternFound;
                return iBeacon;
            }
            return null;
        }

        iBeacon iBeacon = new iBeacon();

        iBeacon.major = (scanData[startByte + 20] & 0xff) * 0x100
                + (scanData[startByte + 21] & 0xff);
        iBeacon.minor = (scanData[startByte + 22] & 0xff) * 0x100
                + (scanData[startByte + 23] & 0xff);
        iBeacon.txPower = (int) scanData[startByte + 24]; // this one is signed
        iBeacon.rssi = rssi;

        // AirLocate:
        // 02 01 1a 1a ff 4c 00 02 15 # Apple's fixed iBeacon advertising prefix
        // e2 c5 6d b5 df fb 48 d2 b0 60 d0 f5 a7 10 96 e0 # iBeacon profile
        // uuid
        // 00 00 # major
        // 00 00 # minor
        // c5 # The 2's complement of the calibrated Tx Power

        // Estimote:
        // 02 01 1a 11 07 2d 24 bf 16
        // 394b31ba3f486415ab376e5c0f09457374696d6f7465426561636f6e00000000000000000000000000000000000000000000000000

        byte[] proximityUuidBytes = new byte[16];
        System.arraycopy(scanData, startByte + 4, proximityUuidBytes, 0, 16);
        String hexString = bytesToHexString(proximityUuidBytes);
        StringBuilder sb = new StringBuilder();
        sb.append(hexString.substring(0, 8));
        sb.append("-");
        sb.append(hexString.substring(8, 12));
        sb.append("-");
        sb.append(hexString.substring(12, 16));
        sb.append("-");
        sb.append(hexString.substring(16, 20));
        sb.append("-");
        sb.append(hexString.substring(20, 32));
        iBeacon.proximityUuid = sb.toString();

        if (device != null) {
            iBeacon.bluetoothAddress = device.getAddress();
            iBeacon.name = device.getName();
        }

        iBeacon.isIbeacon = patternFound;
        return iBeacon;
    }

    private static String bytesToHexString(byte[] src) {
        StringBuilder stringBuilder = new StringBuilder("");
        if (src == null || src.length <= 0) {
            return null;
        }
        for (int i = 0; i < src.length; i++) {
            int v = src[i] & 0xFF;
            String hv = Integer.toHexString(v);
            if (hv.length() < 2) {
                stringBuilder.append(0);
            }
            stringBuilder.append(hv);
        }
        return stringBuilder.toString();
    }
}
