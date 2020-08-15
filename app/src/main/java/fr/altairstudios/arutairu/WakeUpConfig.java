package fr.altairstudios.arutairu;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.widget.Toast;

import java.util.Calendar;

import static android.content.Context.MODE_PRIVATE;

public class WakeUpConfig extends BroadcastReceiver {

    public static final String ARUTAIRU_SHARED_PREFS = "ArutairuSharedPrefs";
    private SharedPreferences sharedPreferences;

    @Override
    public void onReceive(Context context, Intent intent) {
        Toast.makeText(context, "ARUTAIRU !!", Toast.LENGTH_SHORT).show();
        sharedPreferences = context.getSharedPreferences(ARUTAIRU_SHARED_PREFS, MODE_PRIVATE);
        enableNotification(context, sharedPreferences.getInt("HOURS", 12), sharedPreferences.getInt("MIN",0));
    }

    private void enableNotification(Context context, int hour, int minute) {
        if (sharedPreferences.getBoolean("NOTIFS", false)){
            Intent intent = new Intent(context, DailyReminderBroadcast.class);
            PendingIntent pendingIntent = PendingIntent.getBroadcast(context, 100, intent, 0);

            AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);

            Calendar calendar = Calendar.getInstance();
            calendar.setTimeInMillis(System.currentTimeMillis());
            calendar.set(Calendar.HOUR_OF_DAY, hour);
            calendar.set(Calendar.MINUTE, minute);

            assert alarmManager != null;
            alarmManager.setRepeating(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(),
                    60*1000, pendingIntent);
        }
    }
}
