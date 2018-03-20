package com.example.gautamkarnik.onyourbike.helpers;

import android.app.Activity;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import com.example.gautamkarnik.onyourbike.R;

/**
 * Created by gautamkarnik on 2015-04-03.
 */
public class Notify {
    private static String CLASS_NAME;
    private static final int MESSAGE_ID = 1;
    private final NotificationManager manager;
    private final Context context;
    public int smallIcon = R.drawable.ic_stat_notification;

    public Notify(Activity activity) {
        CLASS_NAME = getClass().getName();
        manager = (NotificationManager) activity.getSystemService(Context.NOTIFICATION_SERVICE);
        context = activity.getApplicationContext();
    }

    private Notification create(String title, String message, long when) {
        Log.d(CLASS_NAME, "create()");

        // needed to support Gingerbread
        PendingIntent intent = PendingIntent.getActivity(context, 0, new Intent(), 0);

        ///Note must supply icon otherwise notification doesn't show
        Notification notification = new NotificationCompat.Builder(context)
                .setContentTitle(title)
                .setContentText(message)
                .setWhen(when)
                .setSmallIcon(smallIcon)
                .build();
        notification.contentIntent = intent;
        return notification;
    }

    public void notify(String title, String message) {
        Log.d(CLASS_NAME, "notify()");
        Notification notification = create(title, message, System.currentTimeMillis());
        manager.notify(MESSAGE_ID, notification);
    }

    public void notify(String title, String message, long when) {
        Log.d(CLASS_NAME, "notify()");
        Notification notification = create(title,message,when);
        manager.notify(MESSAGE_ID, notification);
    }

}
