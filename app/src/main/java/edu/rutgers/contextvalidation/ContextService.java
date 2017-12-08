package edu.rutgers.contextvalidation;

import android.Manifest;
import android.app.job.JobParameters;
import android.app.job.JobService;
import android.arch.persistence.room.Room;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.net.wifi.ScanResult;
import android.net.wifi.WifiManager;
import android.os.BatteryManager;
import android.support.v4.app.ActivityCompat;
import android.telephony.CellIdentityCdma;
import android.telephony.CellIdentityGsm;
import android.telephony.CellIdentityLte;
import android.telephony.CellIdentityWcdma;
import android.telephony.CellInfo;
import android.telephony.CellInfoCdma;
import android.telephony.CellInfoGsm;
import android.telephony.CellInfoLte;
import android.telephony.CellInfoWcdma;
import android.telephony.TelephonyManager;
import android.util.Log;

import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.List;

public class ContextService extends JobService {
    private BroadcastReceiver mWifiReceiver;
    private IntentFilter mWifiScanFilter;
    private WifiManager mWifiManager;

    private ContextDatabase db;
    private ContextFeatures features;

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


        mParams = jobParameters;

        mWifiManager = (WifiManager) getApplicationContext().getSystemService(Context.WIFI_SERVICE);

        Log.i("ContextService", "Starting job");

        // Start scan for WiFi state
        getApplicationContext().registerReceiver(mWifiReceiver, mWifiScanFilter);
        mWifiManager.startScan();

        // Open the database
        db = Room.databaseBuilder(getApplicationContext(),
                ContextDatabase.class, "context-database").allowMainThreadQueries().build();
        // Generate a new database row
        features = new ContextFeatures();

        // Get battery data
        IntentFilter ifilter = new IntentFilter(Intent.ACTION_BATTERY_CHANGED);
        Intent batteryStatus = getApplicationContext().registerReceiver(null, ifilter);
        features.batteryState = batteryStatus.getIntExtra(BatteryManager.EXTRA_STATUS, -1);
        int batLevel = batteryStatus.getIntExtra(BatteryManager.EXTRA_LEVEL, -1);
        int batScale = batteryStatus.getIntExtra(BatteryManager.EXTRA_SCALE, -1);
        float batteryPct = batLevel / (float) batScale;

        features.batteryLevel = BATTERY_LEVEL.FULL.ordinal();

        if (batteryPct < 0.35) {
            features.batteryLevel = BATTERY_LEVEL.LOW.ordinal();
        } else if (batteryPct < 0.65) {
            features.batteryLevel = BATTERY_LEVEL.MEDIUM.ordinal();
        } else if (batteryPct < 0.85) {
            features.batteryLevel = BATTERY_LEVEL.HIGH.ordinal();
        }

        // Get date/time/day data
        Calendar cal = new GregorianCalendar();

        features.timestamp = cal.getTime().getTime();

        features.dayOfWeek = cal.get(Calendar.DAY_OF_WEEK);
        int hour = cal.get(Calendar.HOUR_OF_DAY);

        features.timeOfDay = DAY_PERIOD.NIGHT.ordinal();

        if (hour >= 7 && hour < 11) {
            features.timeOfDay = DAY_PERIOD.MORNING.ordinal();
        } else if (hour >= 11 && hour < 13) {
            features.timeOfDay = DAY_PERIOD.NOON.ordinal();
        } else if (hour >= 13 && hour < 18) {
            features.timeOfDay = DAY_PERIOD.AFTERNOON.ordinal();
        } else if (hour >= 18 && hour < 21) {
            features.timeOfDay = DAY_PERIOD.EVENING.ordinal();
        }

        // Assume there are no cell towers (this protects us from having no access to location data)
        int numberOfCellTowers = 0;

        TelephonyManager telephonyManager = (TelephonyManager) getApplicationContext().
                getSystemService(Context.TELEPHONY_SERVICE);
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            List<CellInfo> cellInfos = telephonyManager.getAllCellInfo();
            numberOfCellTowers = cellInfos.size();

            for (int i = 0; i < numberOfCellTowers && i < Constants.MAXIMUM_CELL_NETWORKS; ++i) {
                CellInfo ci = cellInfos.get(i);
                Class c = cellInfos.get(i).getClass();

                int networkType = NETWORK_TYPE.NONE.ordinal();
                int cellId = 0;
                int locationAreaCode = 0;
                int networkId = 0;
                int basestationId = 0;
                int systemId = 0;

                if (c.equals(CellInfoGsm.class)) {
                    CellInfoGsm cellInfo = (CellInfoGsm) ci;
                    CellIdentityGsm cellIdentity = cellInfo.getCellIdentity();

                    networkType = NETWORK_TYPE.GSM.ordinal();
                    cellId = cellIdentity.getCid();
                    locationAreaCode = cellIdentity.getLac();

                } else if (c.equals(CellInfoCdma.class)) {
                    CellInfoCdma cellInfo = (CellInfoCdma) ci;
                    CellIdentityCdma cellIdentity = cellInfo.getCellIdentity();

                    networkType = NETWORK_TYPE.CDMA.ordinal();
                    networkId = cellIdentity.getNetworkId();
                    basestationId = cellIdentity.getBasestationId();
                    systemId = cellIdentity.getSystemId();

                } else if (c.equals(CellInfoLte.class)) {
                    CellInfoLte cellInfo = (CellInfoLte) ci;
                    CellIdentityLte cellIdentity = cellInfo.getCellIdentity();

                    networkType = NETWORK_TYPE.LTE.ordinal();
                    cellId = cellIdentity.getCi();

                } else if (c.equals(CellInfoWcdma.class)) {
                    CellInfoWcdma cellInfo = (CellInfoWcdma) ci;
                    CellIdentityWcdma cellIdentity = cellInfo.getCellIdentity();

                    networkType = NETWORK_TYPE.WCDMA.ordinal();
                    cellId = cellIdentity.getCid();
                    locationAreaCode = cellIdentity.getLac();

                }

                CellNetwork newNetwork = new CellNetwork();
                newNetwork.bid = basestationId;
                newNetwork.cid = cellId;
                newNetwork.lac = locationAreaCode;
                newNetwork.networkType = networkType;
                newNetwork.nid = networkId;
                newNetwork.sid = systemId;

                features.cellNetwork = newNetwork;

                //features.setCellNetwork(i, newNetwork);
            }
        }

        // Fill in the rest of the database with empty networks
        for (int i = numberOfCellTowers; i < Constants.MAXIMUM_CELL_NETWORKS; ++i) {
            CellNetwork nullNetwork = new CellNetwork();

            nullNetwork.bid = 0;
            nullNetwork.cid = 0;
            nullNetwork.lac = 0;
            nullNetwork.networkType = NETWORK_TYPE.NONE.ordinal();
            nullNetwork.nid = 0;
            nullNetwork.sid = 0;

            features.cellNetwork = nullNetwork;

            //features.setCellNetwork(i, nullNetwork);
        }

        return false;
    }

    @Override
    public boolean onStopJob(JobParameters jobParameters) {
        getApplicationContext().unregisterReceiver(mWifiReceiver);

        // Schedule another job
        return false;
    }

    private void wifiScanComplete() {
        getApplicationContext().unregisterReceiver(mWifiReceiver);

        List<ScanResult> results = mWifiManager.getScanResults();
        for (int i = 0; i < results.size() && i < Constants.MAXIMUM_WIFI_NETWORKS; ++i) {
            String macAddress;
            int rssi;

            ScanResult result = results.get(i);
            if (result != null) {
                macAddress = result.BSSID;
                rssi = result.level;
            } else {
                macAddress = "";
                rssi = 0;
            }

            WifiNetwork newNetwork = new WifiNetwork();
            newNetwork.macAddress = macAddress;
            newNetwork.rssi = rssi;

            features.wifiNetwork = newNetwork;

            //features.setWifiNetwork(i, newNetwork);
        }
        for (int i = results.size(); i < Constants.MAXIMUM_WIFI_NETWORKS; ++i) {
            WifiNetwork nullNetwork = new WifiNetwork();
            nullNetwork.macAddress = "";
            nullNetwork.rssi = 0;

            features.wifiNetwork = nullNetwork;

            //features.setWifiNetwork(i, nullNetwork);
        }

        // Finally, we can store to the database
        db.contextDao().insertContextFeatures(features);

        Log.i("ContextService", "Finished storing to database!");

        jobFinished(mParams, false);
    }


}
