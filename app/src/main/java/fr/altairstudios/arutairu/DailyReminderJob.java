package fr.altairstudios.arutairu;

import android.app.Notification;
import android.app.PendingIntent;
import android.app.job.JobInfo;
import android.app.job.JobParameters;
import android.app.job.JobScheduler;
import android.app.job.JobService;
import android.content.ComponentName;
import android.content.Intent;
import android.content.SharedPreferences;

import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import java.util.Calendar;

public class DailyReminderJob extends JobService {
    public static final String ARUTAIRU_SHARED_PREFS = "ArutairuSharedPrefs";
    private SharedPreferences sharedPreferences;
    @Override
    public boolean onStartJob(JobParameters params) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                sharedPreferences = getSharedPreferences(ARUTAIRU_SHARED_PREFS, MODE_PRIVATE);
                if (sharedPreferences.getBoolean("NOTIFS", false)){
                    NotificationManagerCompat nManager = NotificationManagerCompat.from(getApplicationContext());

                    Intent dailySurprise = new Intent(getApplicationContext(), MainActivity.class);

                    PendingIntent pendingIntent = PendingIntent.getActivity(getApplicationContext(), 100, dailySurprise, PendingIntent.FLAG_UPDATE_CURRENT);
                    Notification notification = new NotificationCompat.Builder(getApplicationContext(), "Daily Reminder")
                            .setSmallIcon(R.drawable.ic_stat_name)
                            .setContentText("こんにちは！ お元気ですか?")
                            .setContentTitle(getApplicationContext().getString(R.string.time_to_learn))
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
        });

        return false;
    }

    @Override
    public boolean onStopJob(JobParameters params) {
        return false;
    }
}
