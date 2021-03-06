package com.example.roshk1n.foodcalculator.broadcastReceivers;

import android.app.AlarmManager;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.PowerManager;
import android.support.v4.app.NotificationCompat;

import com.example.roshk1n.foodcalculator.activities.LoginActivity;
import com.example.roshk1n.foodcalculator.manageres.LocalDataBaseManager;
import com.example.roshk1n.foodcalculator.R;
import com.example.roshk1n.foodcalculator.rest.model.ndbApi.response.Reminder;

import java.util.Calendar;

public class ReceiverNotification extends BroadcastReceiver {
    private final static String NOTIFICATION_ID = "notify_id";
    private final static String NOTIFICATION_NAME = "notify_name";
    private final static String ACTION = "android.intent.action.receiverNotification";

    @Override
    public void onReceive(Context context, Intent intent) {
        PowerManager pm = (PowerManager) context.getSystemService(Context.POWER_SERVICE);
        PowerManager.WakeLock wl = pm.newWakeLock(PowerManager.SCREEN_DIM_WAKE_LOCK | PowerManager.ACQUIRE_CAUSES_WAKEUP, "myTAG");
        wl.acquire(15000);

        int notify_id = intent.getExtras().getInt(NOTIFICATION_ID);
        String name = intent.getExtras().getString(NOTIFICATION_NAME);

        Intent intent1 = new Intent(context, LoginActivity.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(context, 0,
                intent1, 0);
        NotificationCompat.Builder mBuilder =
                new NotificationCompat.Builder(context)
                        .setAutoCancel(true)
                        .setTicker(context.getString(R.string.time_to_eat))
                        .setDefaults(Notification.DEFAULT_ALL)
                        .setSmallIcon(R.drawable.ic_add_white_24dp)
                        .setContentTitle("Food Calculator")
                        .setContentText(context.getString(R.string.notification, name))
                        .setContentIntent(pendingIntent);

        NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.notify(notify_id, mBuilder.build());
    }

    public void createNotification(Context context, int positionAdapter) {
        Reminder reminder = LocalDataBaseManager.getNotification(positionAdapter);
        Calendar timeNotify = Calendar.getInstance();
        timeNotify.setTimeInMillis(reminder.getTime());
        String nameNotification = reminder.getName();

        AlarmManager am = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        Intent intent = new Intent(context, ReceiverNotification.class);
        intent.setAction(ACTION);
        intent.putExtra(NOTIFICATION_ID, positionAdapter);
        intent.putExtra(NOTIFICATION_NAME, nameNotification);
        PendingIntent contentIntent = PendingIntent.getBroadcast(context, positionAdapter, intent, 0);

        Calendar calendarNow = Calendar.getInstance();
        calendarNow.setTimeInMillis(System.currentTimeMillis());

        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(System.currentTimeMillis());
        calendar.set(Calendar.HOUR_OF_DAY, timeNotify.get(Calendar.HOUR_OF_DAY));
        calendar.set(Calendar.MINUTE, timeNotify.get(Calendar.MINUTE));
        calendar.set(Calendar.SECOND, timeNotify.get(Calendar.SECOND));

        if (calendarNow.getTime().getTime() >= calendar.getTime().getTime()) {
            am.setRepeating(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis() + AlarmManager.INTERVAL_DAY
                    , AlarmManager.INTERVAL_DAY, contentIntent);
        } else {

            am.setRepeating(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis()
                    , AlarmManager.INTERVAL_DAY, contentIntent);
        }
    }

    public void cancelNotification(Context context, int positionAdapter) {

        AlarmManager am = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        Intent intent = new Intent(context, ReceiverNotification.class);
        intent.setAction(ACTION);
        PendingIntent stopNotify = PendingIntent.getBroadcast(context,
                positionAdapter, intent, 0);
        am.cancel(stopNotify);
    }
}
