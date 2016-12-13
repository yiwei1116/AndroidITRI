package org.tabc.living3.ble;

import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothManager;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Handler;
import android.util.Log;

import org.tabc.living3.ble.advertising.ADPayloadParser;
import org.tabc.living3.ble.advertising.ADStructure;
import org.tabc.living3.ble.advertising.LocalName;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.SortedMap;
import java.util.TreeMap;

/**
 * Created by n500 on 2015/10/29.
 */
public class BLEScannerWrapper {
    private static final String TAG = "BLEScannerWrapper";
    private static final boolean D = true;

    private static final int REQUEST_ENABLE_BT = 1;
    // Stops scanning after 10 seconds.
    private static final int SCAN_PERIOD = 1000;
    private static final int SCAN_PAUSE_PERIOD = 5000;
    private static final int RSSI_THRESHOLD = -70;

    private Activity mActivity;
    private Handler mActivityHandler;
    private BluetoothAdapter mBluetoothAdapter;
    private Map<String, Integer> mPowerLevelRecordMap;
    private SortedMap<Integer, BluetoothDevice> mRSSILeDevicesMap;
    private boolean mScanning;
    private int mScanPeriod = SCAN_PERIOD;
    private int mScanPause = SCAN_PAUSE_PERIOD;
    private Handler bleHandler;
    private boolean isThreadRunning;
    private int powerLevelWarningThreshold;
    private int mRSSIThreshold = RSSI_THRESHOLD;

    private String mScanDeviceName;

    public BLEScannerWrapper(Activity pActivity, Handler pHandler) throws Exception {
        mActivity = pActivity;
        mActivityHandler = pHandler;

        bleHandler = new Handler();

        mPowerLevelRecordMap = new HashMap<String, Integer>();
        // create a sorted descending activity_map,
        mRSSILeDevicesMap = new TreeMap<Integer, BluetoothDevice>(Collections.reverseOrder());

        // Use this check to determine whether BLE is supported on the device.  Then you can
        // selectively disable BLE-related features.
        if (!pActivity.getPackageManager().hasSystemFeature(PackageManager.FEATURE_BLUETOOTH_LE)) {
            throw new Exception(BLEModule.ERROR_BLE_NOT_SUPPORTED);
        }

        // Initializes a Bluetooth adapter.  For API level 18 and above, get a reference to
        // BluetoothAdapter through BluetoothManager.
        final BluetoothManager bluetoothManager =
                (BluetoothManager) pActivity.getSystemService(Context.BLUETOOTH_SERVICE);
        mBluetoothAdapter = bluetoothManager.getAdapter();

        // Checks if Bluetooth is supported on the device.
        if (mBluetoothAdapter == null) {
            throw new Exception(BLEModule.ERROR_BLE_NOT_SUPPORTED);
        }
    }

    public void startBLEScan() {
        if (!mBluetoothAdapter.isEnabled()) {
            if (!mBluetoothAdapter.isEnabled()) {
                Intent enableBtIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
                mActivity.startActivityForResult(enableBtIntent, REQUEST_ENABLE_BT);
            }
        }
        // create a thread to scan BLE device
        isThreadRunning = true;
        new Thread(new Runnable() {
            @Override
            public void run() {
                // TODO Auto-generated method stub
                while (isThreadRunning) {
                    try{
                        mRSSILeDevicesMap.clear();
                        scanLeDevice(true);
                        Thread.sleep(BLEScannerWrapper.this.mScanPeriod + BLEScannerWrapper.this.mScanPause);
                    }
                    catch(Exception e){
                        e.printStackTrace();
                    }
                }
                Log.d(TAG, "Thread exit");
            }
        }).start();
    }

    public void stopBLEScan() {
        isThreadRunning = false;
    }

    public void setScanDeviceName(String pName) {
        mScanDeviceName = pName;
    }

    public void setScanPeriod(int pScanPeriod) {
        this.mScanPeriod = pScanPeriod;
    }

    public void setScanPause(int pScanPause) {
        this.mScanPause = pScanPause;
    }

    public void setRSSIThreshold(int pRSSIThreshold) {
        this.mRSSIThreshold = pRSSIThreshold;
    }

    /**
     *
     * @return Map data structure with key = mac address and value = power level (0..100)
     */
    public Map<String, Integer> getPowerInfo() {
        return mPowerLevelRecordMap;
    }

    /**
     * Set the threshold of the battery power left. Battery power is from 0 to 100.
     * Under the threshold will send BLE_LOW_POWER_WARNING to handler
     * @param pLevel
     */
    public void setPowerLevelWarningThreshold(int pLevel) {
        powerLevelWarningThreshold = pLevel;
    }

    /**
     * return the bluetooth device with the highest RSSI value
     * @return
     */
    public BluetoothDevice getTheHighestRssiDevice() {
        if (!mRSSILeDevicesMap.isEmpty()) {
            return mRSSILeDevicesMap.get(mRSSILeDevicesMap.firstKey());
        }
        return null;
    }

    /**
     * return the bluetooth device with the lowest RSSI value
     * @return
     */
    public BluetoothDevice getTheLowestRssiDevice() {
        if (!mRSSILeDevicesMap.isEmpty()) {
            return mRSSILeDevicesMap.get(mRSSILeDevicesMap.lastKey());
        }
        return null;
    }

    /**
     * scan BLE devices
     * @param enable
     */
    private void scanLeDevice(final boolean enable) {
        if (enable) {
            // Stops scanning after a pre-defined(10 seconds) scan period.
            final boolean b = bleHandler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    // scan complete
                    mScanning = false;
                    mBluetoothAdapter.stopLeScan(mLeScanCallback);
                    mScanning = false;
                    //invalidateOptionsMenu();
                    Log.d(TAG, "Scan result size: " + mRSSILeDevicesMap.size());

                    if (mActivityHandler != null) {
                        mActivityHandler.obtainMessage(BLEModule.BLE_SCAN_DONE).sendToTarget();
                    }
                }
            }, BLEScannerWrapper.this.mScanPeriod);

            mScanning = true;
            mBluetoothAdapter.startLeScan(mLeScanCallback);
        } else {
            mScanning = false;
            mBluetoothAdapter.stopLeScan(mLeScanCallback);
        }
        //invalidateOptionsMenu();
    }

    // Device scan callback.
    private BluetoothAdapter.LeScanCallback mLeScanCallback = new BluetoothAdapter.LeScanCallback() {
        @Override
        public void onLeScan(final BluetoothDevice device, final int rssi, final byte[] scanRecord) {
            mActivity.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    ADStructure aTempADStructure = null;
                    boolean isBLEfound = false;
                    String aMacAddress;

                    List<ADStructure> structures = ADPayloadParser.getInstance().parse(scanRecord);

                    for (ADStructure aADStructure : structures) {
                        switch (aADStructure.getType()) {
                            case BLEType.EBLE_MANDATA:
                                // found iBeacon. temporary store manufacture data for power data
                                aTempADStructure = aADStructure;
                                break;
                            case BLEType.EBLE_LOCALNAME:
                                // convert to local name
                                LocalName aLocalName = (LocalName)aADStructure;

                                // if the BLE device
                                if (mScanDeviceName == null || mScanDeviceName.equals("") || aLocalName.getLocalName().equalsIgnoreCase(mScanDeviceName)) {
                                    //Log.d(TAG, "name: " + aLocalName.getLocalName() + ", device mac: " + device.getAddress());

                                    for (SortedMap.Entry<Integer, BluetoothDevice> aBluetoothDeviceEnrty : mRSSILeDevicesMap.entrySet()) {
                                        // get bluetooth device
                                        BluetoothDevice aBluetoothDevice = aBluetoothDeviceEnrty.getValue();

                                        if (aBluetoothDevice.getAddress().equals(device.getAddress())) {
                                            // find old device, remove it first
                                            mRSSILeDevicesMap.remove(aBluetoothDeviceEnrty.getKey());
                                            break;
                                        }
                                    }

                                    if (rssi >= mRSSIThreshold) {
                                        // if RSSI signal is strong enough
                                        mRSSILeDevicesMap.put(rssi, device);
                                    }

                                    isBLEfound = true;
                                }
                                break;
                        }
                    } // for
/*
                    if (isBLEfound) {
                        IBeacon aIBeacon = (IBeacon)aTempADStructure;
                        //Log.d(TAG, "ADStructure data = " + aTempADStructure.toString());
                        //Log.d(TAG, "IBeacon data = " + aIBeacon.toString());
                        if (mActivityHandler != null && (aIBeacon.getPower() < powerLevelWarningThreshold)) {
                            mActivityHandler.obtainMessage(BLEModule.BLE_LOW_POWER_WARNING, device).sendToTarget();
                        }
                        // store power level in the app temporarily
                        mPowerLevelRecordMap.put(device.getAddress(), aIBeacon.getPower());
                    }
*/                }
            });
        }
    };
}
