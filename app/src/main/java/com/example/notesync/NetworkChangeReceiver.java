package com.example.notesync;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.widget.Toast;

public class NetworkChangeReceiver extends BroadcastReceiver {
    private final SyncListener syncListener;

    public NetworkChangeReceiver(SyncListener syncListener) {
        this.syncListener = syncListener;
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        if (NetworkUtils.isNetworkAvailable(context)) {
            Toast.makeText(context, "Internet connection restored. Syncing notes...", Toast.LENGTH_SHORT).show();
            syncListener.onNetworkConnected();
        }
    }

    public interface SyncListener {
        void onNetworkConnected();
    }
}
