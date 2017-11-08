package edu.rutgers.contextvalidation;

import android.Manifest;
import android.app.job.JobInfo;
import android.app.job.JobScheduler;
import android.content.ComponentName;
import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

public class TimingSelection extends AppCompatActivity {

    private final static int PERMISSIONS_REQUEST_ACCESS_COARSE_LOCATION = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_timing_selection);

        // We need to explicitly request location permissions for cell data
        // Here, thisActivity is the current activity
        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_COARSE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {

            ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.ACCESS_COARSE_LOCATION},
                        PERMISSIONS_REQUEST_ACCESS_COARSE_LOCATION);
        }

        // Set up GUI stuff

        Log.i("TimingSelection", "Attempting to schedule job");

        JobScheduler jobScheduler = (JobScheduler) getSystemService(Context.JOB_SCHEDULER_SERVICE);

        JobInfo jobInfo = new JobInfo.Builder( 1,
                new ComponentName( getPackageName(),
                        ContextService.class.getName() ) )
                .setMinimumLatency(5000).build();

        jobScheduler.schedule(jobInfo);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {
        switch (requestCode) {
            case PERMISSIONS_REQUEST_ACCESS_COARSE_LOCATION: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                    // permission was granted, yay! Do the
                    // contacts-related task you need to do.

                } else {

                    // permission denied, boo! Disable the
                    // functionality that depends on this permission.
                }
                return;
            }
        }
    }

    // TODO - Deal with persistent job schedule settings

    // TODO - Set up button listeners which schedule the job at a user configurable interval

    // TODO - create classes for database and database rows
}
