package edu.rutgers.contextvalidation;

import android.app.job.JobParameters;
import android.app.job.JobService;
import android.content.Intent;
import android.net.wifi.WifiManager;
import android.os.IBinder;

public class ContextService extends JobService {
    public ContextService() {
    }

    @Override
    public boolean onStartJob(JobParameters jobParameters) {
        // TODO - create listeners for WiFi, sensors; start WiFi scan
        WifiReceiver wifiReceiver = new WifiReceiver();

        WifiManager wifiManager = (WifiManager) getApplicationContext().getSystemService(WIFI_SERVICE);
        wifiManager.startScan();

        return false;
    }

    @Override
    public boolean onStopJob(JobParameters jobParameters) {
        // TODO - kill off listeners and send data to database
        return false;
    }

    // TODO - create subclass that listens for data intents
    // subclass will store data each time it receives it
    // After storing, it will check for completeness
    // If complete, the job is considered finished, and onStopJob is called

}
