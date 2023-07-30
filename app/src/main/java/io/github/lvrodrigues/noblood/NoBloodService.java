package io.github.lvrodrigues.noblood;

import android.app.Notification;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.os.Build;
import android.os.IBinder;
import android.util.Log;

import androidx.annotation.Nullable;

public class NoBloodService extends Service {

    private static final String LOGTAG = "NoBloodService";

    private static final int ONGOING_NOTIFICATION_ID = 1;

    @Override
    public void onCreate() {
        super.onCreate();
        Log.i(LOGTAG, "NoBloodService criado.");
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.d(LOGTAG, "NoBloodService inicializando...");
        notifyServiceRunning();
        return START_REDELIVER_INTENT; // super.onStartCommand(intent, flags, startId);
    }

    private void notifyServiceRunning() {
        Intent notificationIntent = new Intent(this, MainActivity.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, notificationIntent, PendingIntent.FLAG_IMMUTABLE);

        Notification notification = null;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            Notification.Builder builder = new Notification.Builder(this, NoBloodApplication.CHANNEL_ID)
                    .setContentTitle(getText(R.string.app_name))
                    .setContentText(getText(R.string.notification_message))
                    .setSmallIcon(R.mipmap.ic_launcher)
                    .setContentIntent(pendingIntent)
                    .setTicker(getText(R.string.app_name))
                    .setVisibility(Notification.VISIBILITY_PUBLIC)
                    .setOngoing(true);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
                builder = builder.setForegroundServiceBehavior(Notification.FOREGROUND_SERVICE_IMMEDIATE);
            }
            notification = builder.build();
        }
        startForeground(ONGOING_NOTIFICATION_ID, notification);
    }

    @Override
    public void onDestroy() {
        stopForeground(STOP_FOREGROUND_REMOVE);
        super.onDestroy();
        Log.d(LOGTAG, "NoBloodService finalizado.");
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
}
