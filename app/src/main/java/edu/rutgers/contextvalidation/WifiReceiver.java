package edu.rutgers.contextvalidation;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

public class WifiReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        // TODO - filter intents to only accept wifiscancomplete events or whatever

        // TODO - construct an intent to send relevant data to the ContextService
    }
}
