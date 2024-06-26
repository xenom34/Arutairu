package fr.altairstudios.arutairu;

import android.app.AlarmManager;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.util.Log;

import androidx.core.app.ActivityCompat;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import java.util.Calendar;

import static android.content.Context.MODE_PRIVATE;

public class WakeUpConfig extends BroadcastReceiver {

    public static final String ARUTAIRU_SHARED_PREFS = "ArutairuSharedPrefs";
    private SharedPreferences sharedPreferences;

    @Override
    public void onReceive(Context context, Intent intent) {
        sharedPreferences = context.getSharedPreferences(ARUTAIRU_SHARED_PREFS, MODE_PRIVATE);
        enableNotification(context, sharedPreferences.getInt("HOURS", 12), sharedPreferences.getInt("MIN", 0));
        if (/*sharedPreferences.getBoolean("NOTIFS", false) && */sharedPreferences.getBoolean(String.valueOf(BuildConfig.VERSION_CODE), true)) {
            Log.d("NOTIFS", "WE'RE IN !");

            NotificationManagerCompat nManager = NotificationManagerCompat.from(context);

            if (sharedPreferences.getBoolean("GENERAL", true) && android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
                CharSequence name = "General";
                int importance = NotificationManager.IMPORTANCE_DEFAULT;
                NotificationChannel channel;
                channel = new NotificationChannel("General", name, importance);
                // Register the channel with the system; you can't change the importance
                // or other notification behaviors after this
                nManager.createNotificationChannel(channel);
                sharedPreferences.edit().putBoolean("GENERAL", false).apply();
            }
            Intent dailySurprise = new Intent(context, MainActivity.class);

            PendingIntent pendingIntent = PendingIntent.getActivity(context, 200, dailySurprise, PendingIntent.FLAG_IMMUTABLE);
            Notification notification = new NotificationCompat.Builder(context, "General")
                    .setSmallIcon(R.drawable.ic_stat_name)
                    .setContentText(context.getString(R.string.message))
                    .setContentTitle(context.getString(R.string.messagecontent))
                    .setPriority(NotificationCompat.PRIORITY_HIGH)
                    .setCategory(NotificationCompat.CATEGORY_EVENT)
                    .setContentIntent(pendingIntent)
                    .setStyle(new NotificationCompat.BigTextStyle()
                            .bigText(context.getString(R.string.message)))
                    .setAutoCancel(true)
                    .build();

            if (ActivityCompat.checkSelfPermission(context, android.Manifest.permission.POST_NOTIFICATIONS) != PackageManager.PERMISSION_GRANTED) {
                // TODO: Consider calling
                //    ActivityCompat#requestPermissions
                // here to request the missing permissions, and then overriding
                //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                //                                          int[] grantResults)
                // to handle the case where the user grants the permission. See the documentation
                // for ActivityCompat#requestPermissions for more details.
                return;
            }
            nManager.notify(200, notification);
            sharedPreferences.edit().putBoolean(String.valueOf(BuildConfig.VERSION_CODE), false).apply();
        }
    }

    private void enableNotification(Context context, int hour, int minute) {
        if (sharedPreferences.getBoolean("NOTIFS", false)){
            Intent intent = new Intent(context, DailyReminderBroadcast.class);
            PendingIntent pendingIntent = PendingIntent.getBroadcast(context, 100, intent, PendingIntent.FLAG_IMMUTABLE);

            AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);

            Calendar calendar = Calendar.getInstance();
            calendar.setTimeInMillis(System.currentTimeMillis());
            calendar.set(Calendar.HOUR_OF_DAY, hour);
            calendar.set(Calendar.MINUTE, minute);

            assert alarmManager != null;
            alarmManager.setRepeating(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(),
                    AlarmManager.INTERVAL_DAY, pendingIntent);
        }
    }
}
