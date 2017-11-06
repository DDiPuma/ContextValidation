package edu.rutgers.contextvalidation;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

public class WifiReceiver extends BroadcastReceiver {
    ContextService mContextService;

    public WifiReceiver(ContextService contextService) {
        mContextService = contextService;
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        mContextService.wifiScanComplete();
    }
}
