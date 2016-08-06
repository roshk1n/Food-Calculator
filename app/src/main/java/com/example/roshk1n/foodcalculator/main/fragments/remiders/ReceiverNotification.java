package com.example.roshk1n.foodcalculator.main.fragments.remiders;

import android.app.AlarmManager;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.PowerManager;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import com.example.roshk1n.foodcalculator.MyApplication;
import com.example.roshk1n.foodcalculator.R;
import com.example.roshk1n.foodcalculator.main.MainActivity;
import com.example.roshk1n.foodcalculator.realm.ReminderReaml;

import java.util.Calendar;
import java.util.Date;

/**
 * Created by roshk1n on 8/3/2016.
 */
public class ReceiverNotification extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {

        PowerManager pm = (PowerManager) context.getSystemService(Context.POWER_SERVICE);
        PowerManager.WakeLock wl = pm.newWakeLock(PowerManager.SCREEN_DIM_WAKE_LOCK | PowerManager.ACQUIRE_CAUSES_WAKEUP, "TAG");
        wl.acquire(15000);

        int notify_id = intent.getExtras().getInt("notify_id");
        String name = intent.getExtras().getString("name");

        Intent intent1 = new Intent(context,MainActivity.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(context, 0,
                intent1, 0);
        NotificationCompat.Builder mBuilder =
                new NotificationCompat.Builder(context)
                        .setAutoCancel(true)
                        .setTicker("Time to eat :)")
                        .setDefaults(Notification.DEFAULT_ALL)
                        .setSmallIcon(R.drawable.ic_add_white_24dp)
                        .setContentTitle("Food Calculator")
                        .setContentText("You long time without " + name + ". Time to eat :)")
                        .setContentIntent(pendingIntent);

        NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.notify(notify_id, mBuilder.build());
    }

    public void createNotification(Context context, int positionAdapter) {

        ReminderReaml reminderReaml = (new RemindersPresenterImpl()).getNotification(positionAdapter);
        Date timeNotify = new Date(reminderReaml.getTime());
        String nameNotification = reminderReaml.getName();

        AlarmManager am=(AlarmManager)context.getSystemService(Context.ALARM_SERVICE);
        Intent intent = new Intent(context, ReceiverNotification.class);
        intent.setAction("android.intent.action.CLICK1");
        intent.putExtra("notify_id", positionAdapter);
        intent.putExtra("name", nameNotification);
        PendingIntent contentIntent = PendingIntent.getBroadcast(context, positionAdapter, intent, 0);

        Calendar calendarNow = Calendar.getInstance();
        calendarNow.setTimeInMillis(System.currentTimeMillis());

        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(System.currentTimeMillis());
        calendar.set(Calendar.HOUR_OF_DAY,timeNotify.getHours());
        calendar.set(Calendar.MINUTE,timeNotify.getMinutes());
        calendar.set(Calendar.SECOND,0);
        calendar.set(Calendar.HOUR_OF_DAY,timeNotify.getHours());
        calendar.set(Calendar.MINUTE,timeNotify.getMinutes());
        calendar.set(Calendar.SECOND,0);

        Log.d("My",calendarNow.getTime().getHours()+ " send:" + timeNotify.getHours());
        if(calendarNow.getTime().getHours() > timeNotify.getHours()) {
            if(calendarNow.getTime().getHours() == timeNotify.getHours()
                    && calendarNow.getTime().getMinutes() > timeNotify.getMinutes()) {
                am.setRepeating(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis() + AlarmManager.INTERVAL_DAY
                        , AlarmManager.INTERVAL_DAY, contentIntent);

            } else {
                am.setRepeating(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis()
                        , AlarmManager.INTERVAL_DAY, contentIntent);
            }
            am.setRepeating(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis() + AlarmManager.INTERVAL_DAY
                    , AlarmManager.INTERVAL_DAY, contentIntent);

        } else {
            am.setRepeating(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis()
                    , AlarmManager.INTERVAL_DAY, contentIntent);
        }
    }

    public void cancelNotification(Context context,int positionAdapter) {

        AlarmManager am = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        Intent intent = new Intent(context, ReceiverNotification.class);
        intent.setAction("android.intent.action.CLICK1");
        PendingIntent senderstop = PendingIntent.getBroadcast(context,
                positionAdapter, intent, 0);
        am.cancel(senderstop);
    }
}
