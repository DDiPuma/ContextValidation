package edu.rutgers.contextvalidation;

import android.app.job.JobParameters;
import android.app.job.JobService;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.wifi.WifiManager;
import android.os.BatteryManager;
import android.os.IBinder;

import java.util.Calendar;
import java.util.GregorianCalendar;

public class ContextService extends JobService {
    private Context mContext;
    private final WifiReceiver mWifiReceiver;
    private final IntentFilter mWifiScanFilter;

    private boolean mIsWifiDataAvailable;

    private int mChargeStatus;
    private BATTERY_LEVEL mBatteryLevel;
    private int mDayOfWeek;
    private DAY_PERIOD mTimeOfDay;

    public ContextService() {
        super();
        mWifiReceiver = new WifiReceiver();
        mWifiReceiver.setmContextService(this);
        mWifiScanFilter = new IntentFilter(WifiManager.SCAN_RESULTS_AVAILABLE_ACTION);
        mContext = null;
    }

    public void setmContext(Context context) { mContext = context; }

    @Override
    public boolean onStartJob(JobParameters jobParameters) {
        // Null out all data
        mIsWifiDataAvailable = false;
        mChargeStatus = -1;
        mBatteryLevel = BATTERY_LEVEL.INVALID;
        mDayOfWeek = -1;
        mTimeOfDay = DAY_PERIOD.INVALID;


        // Start scan for WiFi state
        mContext.registerReceiver(mWifiReceiver, mWifiScanFilter);
        WifiManager wifiManager = (WifiManager) mContext.getSystemService(Context.WIFI_SERVICE);
        wifiManager.startScan();

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

        // TODO - deal with cell ID and location area code

        return false;
    }

    @Override
    public boolean onStopJob(JobParameters jobParameters) {
        unregisterReceiver(mWifiReceiver);

        // TODO - send data to database
        return false;
    }

    public void wifiScanComplete() {
        // TODO - handle data
    }


}