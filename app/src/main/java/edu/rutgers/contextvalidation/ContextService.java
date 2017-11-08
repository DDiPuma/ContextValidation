package edu.rutgers.contextvalidation;

import android.app.job.JobParameters;
import android.app.job.JobService;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.wifi.ScanResult;
import android.net.wifi.WifiManager;
import android.os.BatteryManager;
import android.util.Log;

import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.List;

public class ContextService extends JobService {
    private BroadcastReceiver mWifiReceiver;
    private IntentFilter mWifiScanFilter;
    private WifiManager mWifiManager;

    private boolean mIsWifiDataAvailable;

    private int mChargeStatus;
    private BATTERY_LEVEL mBatteryLevel;
    private int mDayOfWeek;
    private DAY_PERIOD mTimeOfDay;

    private JobParameters mParams;

    @Override
    public boolean onStartJob(JobParameters jobParameters) {
        mWifiReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                wifiScanComplete();
            }
        };
        mWifiScanFilter = new IntentFilter(WifiManager.SCAN_RESULTS_AVAILABLE_ACTION);

        // Null out all data
        mIsWifiDataAvailable = false;
        mChargeStatus = -1;
        mBatteryLevel = BATTERY_LEVEL.INVALID;
        mDayOfWeek = -1;
        mTimeOfDay = DAY_PERIOD.INVALID;

        mParams = jobParameters;

        mWifiManager = (WifiManager) getSystemService(Context.WIFI_SERVICE);

        Log.i("ContextService", "Starting job");

        // Start scan for WiFi state
        getApplicationContext().registerReceiver(mWifiReceiver, mWifiScanFilter);
        mWifiManager.startScan();

        // Get battery data
        IntentFilter ifilter = new IntentFilter(Intent.ACTION_BATTERY_CHANGED);
        Intent batteryStatus = getApplicationContext().registerReceiver(null, ifilter);
        int mChargeStatus = batteryStatus.getIntExtra(BatteryManager.EXTRA_STATUS, -1);
        int batLevel = batteryStatus.getIntExtra(BatteryManager.EXTRA_LEVEL, -1);
        int batScale = batteryStatus.getIntExtra(BatteryManager.EXTRA_SCALE, -1);
        float batteryPct = batLevel / (float) batScale;

        BATTERY_LEVEL mBatteryLevel = BATTERY_LEVEL.FULL;

        if (batteryPct < 0.35) {
            mBatteryLevel = BATTERY_LEVEL.LOW;
        }
        else if (batteryPct < 0.65) {
            mBatteryLevel = BATTERY_LEVEL.MEDIUM;
        }
        else if (batteryPct < 0.85) {
            mBatteryLevel = BATTERY_LEVEL.HIGH;
        }

        // Get date/time/day data
        Calendar cal = new GregorianCalendar();
        int mDayOfWeek = cal.get(Calendar.DAY_OF_WEEK);
        int hour = cal.get(Calendar.HOUR_OF_DAY);

        DAY_PERIOD mTimeOfDay = DAY_PERIOD.NIGHT;

        if (hour >= 7 && hour < 11) {
            mTimeOfDay = DAY_PERIOD.MORNING;
        }
        else if (hour >= 11 && hour < 13) {
            mTimeOfDay = DAY_PERIOD.NOON;
        }
        else if (hour >= 13 && hour < 18) {
            mTimeOfDay = DAY_PERIOD.AFTERNOON;
        }
        else if (hour >= 18 && hour < 21) {
            mTimeOfDay = DAY_PERIOD.EVENING;
        }

        Log.i("ContextService", "Battery level is: " + Float.toString(batteryPct));

        // TODO - deal with cell ID and location area code

        return false;
    }

    @Override
    public boolean onStopJob(JobParameters jobParameters) {
        getApplicationContext().unregisterReceiver(mWifiReceiver);

        // Schedule another job
        return false;
    }

    public void wifiScanComplete() {
        getApplicationContext().unregisterReceiver(mWifiReceiver);

        List<ScanResult> results = mWifiManager.getScanResults();
        for (int i = 0; i < results.size(); ++i) {
            String macAddress;
            int rssi;

            ScanResult result = results.get(i);
            if (result != null) {
                macAddress = result.BSSID;
                rssi = result.level;
            } else {
                macAddress = null;
                rssi = 0;
            }

            Log.i("ContextService", "MAC Address: " + macAddress + ", RSSI: " + Integer.toString(rssi));
        }
        // TODO - store scan data

        jobFinished(mParams, false);
    }


}
