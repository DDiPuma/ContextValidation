package edu.rutgers.contextvalidation;

import android.app.job.JobInfo;
import android.app.job.JobScheduler;
import android.content.ComponentName;
import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

public class TimingSelection extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_timing_selection);

        // Set up GUI stuff

        Log.i("TimingSelection", "Attempting to schedule job");

        JobScheduler jobScheduler = (JobScheduler) getSystemService(Context.JOB_SCHEDULER_SERVICE);

        JobInfo jobInfo = new JobInfo.Builder( 1,
                new ComponentName( getPackageName(),
                        ContextService.class.getName() ) )
                .setMinimumLatency(5000).build();

        jobScheduler.schedule(jobInfo);
    }

    // TODO - Deal with persistent job schedule settings

    // TODO - Set up button listeners which schedule the job at a user configurable interval

    // TODO - create classes for database and database rows
}
