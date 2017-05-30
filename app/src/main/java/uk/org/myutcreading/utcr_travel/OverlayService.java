package uk.org.myutcreading.utcr_travel;

import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.IBinder;
import android.provider.Settings;
import android.util.Log;

import static uk.org.myutcreading.utcr_travel.app_maps.mContext;
import static uk.org.myutcreading.utcr_travel.app_maps.service;
import static uk.org.myutcreading.utcr_travel.app_update.context;

/**
 * Created by ashcon on 5/17/17.
 */

public class OverlayService extends Service {
    private static final String TAG = OverlayService.class.getSimpleName();

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        showOverlayActivity(getApplication());
        registerOverlayReceiver();
        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public void onDestroy() {
        System.out.println("SERVICE KILLED - NO LONGER SHOWING ON LOCK SCREEN");
        unregisterOverlayReceiver();
        super.onDestroy();
    }
    private void registerOverlayReceiver() {
        IntentFilter filter = new IntentFilter();
        filter.addAction(Intent.ACTION_SCREEN_ON);
        registerReceiver(overlayReceiver, filter);
    }

    private void unregisterOverlayReceiver() {
        unregisterReceiver(overlayReceiver);
    }
    private BroadcastReceiver overlayReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            Log.d(TAG, "[onReceive]" + action);
            //showOverlayActivity(context);
        }
    };

    private void showOverlayActivity(Context context) {
        Intent intent = new Intent(context, OverlayActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(intent);
    }
}
