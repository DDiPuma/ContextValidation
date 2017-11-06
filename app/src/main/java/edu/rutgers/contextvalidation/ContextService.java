package edu.rutgers.contextvalidation;

import android.app.job.JobParameters;
import android.app.job.JobService;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.wifi.WifiManager;
import android.os.BatteryManager;
import android.os.IBinder;

import java.util.Calendar;

public class ContextService extends JobService {
    private final WifiReceiver mWifiReceiver;
    private final IntentFilter mWifiScanFilter;

    public ContextService() {
        mWifiReceiver = new WifiReceiver();
        mWifiScanFilter = new IntentFilter(WifiManager.SCAN_RESULTS_AVAILABLE_ACTION);
    }

    @Override
    public boolean onStartJob(JobParameters jobParameters) {
        // Start scan for WiFi state
        registerReceiver(mWifiReceiver, mWifiScanFilter);
        WifiManager wifiManager = (WifiManager) this.getSystemService(WIFI_SERVICE);
        wifiManager.startScan();

        // Get battery data
        IntentFilter ifilter = new IntentFilter(Intent.ACTION_BATTERY_CHANGED);
        Intent batteryStatus = getApplicationContext().registerReceiver(null, ifilter);
        int chargeStatus = batteryStatus.getIntExtra(BatteryManager.EXTRA_STATUS, -1);
        int batLevel = batteryStatus.getIntExtra(BatteryManager.EXTRA_LEVEL, -1);
        int batScale = batteryStatus.getIntExtra(BatteryManager.EXTRA_SCALE, -1);
        float batteryPct = batLevel / (float) batScale;

        BATTERY_LEVEL batteryLevel = BATTERY_LEVEL.FULL;

        if (batteryPct < 0.35) {
            batteryLevel = BATTERY_LEVEL.LOW;
        }
        else if (batteryPct < 0.65) {
            batteryLevel = BATTERY_LEVEL.MEDIUM;
        }
        else if (batteryPct < 0.85) {
            batteryLevel = BATTERY_LEVEL.HIGH;
        }

        // Get date/time/day data
        Calendar cal = new Calendar();
        int dayOfWeek = cal.get(Calendar.DAY_OF_WEEK);
        int hour = cal.get(Calendar.HOUR_OF_DAY);

        DAY_PERIOD timeOfDay = DAY_PERIOD.NIGHT;

        if (hour >= 7 && hour < 11) {
            timeOfDay = DAY_PERIOD.MORNING;
        }
        else if (hour >= 11 && hour < 13) {
            timeOfDay = DAY_PERIOD.NOON;
        }
        else if (hour >= 13 && hour < 18) {
            timeOfDay = DAY_PERIOD.AFTERNOON;
        }
        else if (hour >= 18 && hour < 21) {
            timeOfDay = DAY_PERIOD.EVENING;
        }

        // TODO - store dayOfWeek, timeOfDay, chargeStatus, and batteryLevel somehow for later use in the database

        return false;
    }

    @Override
    public boolean onStopJob(JobParameters jobParameters) {
        unregisterReceiver(mWifiReceiver);

        // TODO - send data to database
        return false;
    }

    // TODO - create subclass that listens for intent from WifiReceiver


}
