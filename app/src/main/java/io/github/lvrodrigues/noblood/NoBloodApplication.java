package io.github.lvrodrigues.noblood;

import android.app.Application;
import android.app.NotificationChannel;
import android.app.NotificationManager;

public class NoBloodApplication extends Application {

    public static final String CHANNEL_ID   = "NoBloodServiceChannel";

    @Override
    public void onCreate() {
        super.onCreate();
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel(
                    CHANNEL_ID,
                    getString(R.string.channel_name),
                    NotificationManager.IMPORTANCE_LOW);
            NotificationManager manager = getSystemService(NotificationManager.class);
            manager.createNotificationChannel(channel);
        }
    }
}
