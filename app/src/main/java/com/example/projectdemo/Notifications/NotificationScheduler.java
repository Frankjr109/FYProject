package com.example.projectdemo.Notifications;

import android.app.AlarmManager;
        import android.app.PendingIntent;
        import android.content.Context;
        import android.content.Intent;
        import android.os.SystemClock;
import android.util.Log;

public class NotificationScheduler {
    private static final long TWO_DAYS_IN_MILLIS = 1 * 24 * 60 * 60 * 1000;

    public static void scheduleNotification(Context context, String title, String text, long targetDate) {
        long triggerTime = targetDate + 30 * 1000 ;

        Log.i("TESTING/// ", triggerTime+"");
        Intent intent = new Intent(context, NotificationReceiver.class);
        intent.putExtra("title", title);
        intent.putExtra("text", text);

        PendingIntent pendingIntent = PendingIntent.getBroadcast(context, 0, intent, PendingIntent.FLAG_IMMUTABLE);

        AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        alarmManager.set(AlarmManager.ELAPSED_REALTIME_WAKEUP, SystemClock.elapsedRealtime() + triggerTime, pendingIntent);
    }
}
