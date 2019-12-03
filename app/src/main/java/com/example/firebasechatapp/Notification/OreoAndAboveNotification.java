package com.example.firebasechatapp.Notification;

import android.annotation.TargetApi;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.ContextWrapper;
import android.net.Uri;
import android.os.Build;

import androidx.annotation.RequiresApi;

public class OreoAndAboveNotification extends ContextWrapper {


    private static final String NAME = "FirebaseAPP";
    private static final String ID = "some_id";

    private NotificationManager notificationManager;

    public OreoAndAboveNotification(Context base) {
        super(base);
        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.O) {
            creatChannel();
        }


    }

    @TargetApi(Build.VERSION_CODES.O)
    private void creatChannel() {

        NotificationChannel notificationChannel = new NotificationChannel(ID, NAME, NotificationManager.IMPORTANCE_DEFAULT);
        notificationChannel.enableLights(true);
        notificationChannel.enableVibration(true);
        notificationChannel.setLockscreenVisibility(Notification.VISIBILITY_PRIVATE);

        getmanager().createNotificationChannel(notificationChannel);
    }

    public NotificationManager getmanager() {
        if (notificationManager == null) {
            notificationManager=(NotificationManager)getSystemService(Context.NOTIFICATION_SERVICE);

        }
        return notificationManager;
    }
    @RequiresApi(api = Build.VERSION_CODES.O)
    public  Notification.Builder getONotification(
    String title,
    String body,
    PendingIntent pendingIntent,
    Uri soundUri,
    String icon){
        return new Notification.Builder(getApplicationContext(),ID)
                .setContentIntent(pendingIntent)
                .setContentTitle(title)
                .setContentText(body)
                .setSound(soundUri)
                .setAutoCancel(true)
                .setSmallIcon(Integer.parseInt(icon));

    }

}
