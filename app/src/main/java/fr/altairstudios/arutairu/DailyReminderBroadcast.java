package fr.altairstudios.arutairu;

import android.app.Notification;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;

import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import static android.content.Context.MODE_PRIVATE;

public class DailyReminderBroadcast extends BroadcastReceiver {
    public static final String ARUTAIRU_SHARED_PREFS = "ArutairuSharedPrefs";
    private SharedPreferences sharedPreferences;

    @Override
    public void onReceive(final Context context, Intent intent) {
        //android.os.Debug.waitForDebugger();
        sharedPreferences = context.getSharedPreferences(ARUTAIRU_SHARED_PREFS, MODE_PRIVATE);
        //Toast.makeText(context, "DING DONG", Toast.LENGTH_SHORT).show();

        if (sharedPreferences.getBoolean("NOTIFS", false)){
            NotificationManagerCompat nManager = NotificationManagerCompat.from(context);

            Intent dailySurprise = new Intent(context, MainActivity.class);

            PendingIntent pendingIntent = PendingIntent.getActivity(context, 100, dailySurprise, PendingIntent.FLAG_UPDATE_CURRENT);
            Notification notification = new NotificationCompat.Builder(context, "Daily Reminder")
                    .setSmallIcon(R.drawable.ic_stat_name)
                    .setContentText("こんにちは！ お元気ですか?")
                    .setContentTitle(context.getString(R.string.time_to_learn))
                    .setPriority(NotificationCompat.PRIORITY_HIGH)
                    .setCategory(NotificationCompat.CATEGORY_EVENT)
                    .setContentIntent(pendingIntent)
                    .setStyle(new NotificationCompat.BigTextStyle()
                            .bigText("こんにちは！ お元気ですか?"))
                    .setAutoCancel(true)
                    .build();

            nManager.notify(100,notification);
        }
    }
}
