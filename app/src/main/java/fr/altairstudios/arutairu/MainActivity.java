package fr.altairstudios.arutairu;

import android.app.AlertDialog;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.initialization.InitializationStatus;
import com.google.android.gms.ads.initialization.OnInitializationCompleteListener;

import java.util.Locale;

public class MainActivity extends AppCompatActivity {
    private ImageView mAltair, mBg;
    private SharedPreferences sharedPreferences;
    private ImageView mCurrentLanguage;
    private com.google.android.material.textview.MaterialTextView mWelcome;
    private TextView mArutairu;
    public static final String ARUTAIRU_SHARED_PREFS = "ArutairuSharedPrefs";
    private com.google.android.material.button.MaterialButton mStart, mRequired;
    private Animation fadeAltair2, fadeAltair3;
    private ShuffleBg shuffleBg = new ShuffleBg();
    private boolean executed = false;
    static int VERSION_CODE = 35;

    @Override
    protected void onStop() {
        super.onStop();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Resources res = getResources();
        DisplayMetrics dm = res.getDisplayMetrics();
        Configuration conf = res.getConfiguration();
        sharedPreferences = getSharedPreferences(ARUTAIRU_SHARED_PREFS, MODE_PRIVATE);

        conf.setLocale(new Locale(sharedPreferences.getString("LOCALE", Locale.getDefault().getLanguage())));

        if(sharedPreferences.getBoolean("RAN", true)){
            sharedPreferences.edit().putBoolean("RAN", false).apply();
            sharedPreferences.edit().putString("LOCALE", Locale.getDefault().getLanguage()).apply();
        }

        if (sharedPreferences.getInt("VERSION", 0) != VERSION_CODE){
            sharedPreferences.edit().putInt("VERSION", VERSION_CODE).apply();
            sharedPreferences.edit().putInt("STATE", LessonsStorage.PEOPLE).apply();
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O && sharedPreferences.getBoolean("NSET", true)) {
                CharSequence name = "Daily Reminder";
                int importance = NotificationManager.IMPORTANCE_HIGH;
                NotificationChannel channel = new NotificationChannel("Daily Reminder", name, importance);
                // Register the channel with the system; you can't change the importance
                // or other notification behaviors after this
                NotificationManager notificationManager = getSystemService(NotificationManager.class);
                notificationManager.createNotificationChannel(channel);
                sharedPreferences.edit().putBoolean("NSET", false).apply();
            }
        }

        res.updateConfiguration(conf, dm);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        fadeAltair2 = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.fade2);
        fadeAltair3 = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.fade3);

        mAltair = findViewById(R.id.altair);
        mBg = findViewById(R.id.bg);
        mRequired = findViewById(R.id.tst);
        mCurrentLanguage = findViewById(R.id.logo);
        mStart = findViewById(R.id.startBtn);
        mArutairu = findViewById(R.id.txtArutairu);
        mWelcome = findViewById(R.id.welcomeTxtLogo);
        mAltair.setVisibility(View.INVISIBLE);
        mArutairu.setAlpha(0f);
        mWelcome.setAlpha(0f);
        mRequired.setAlpha(0f);
        mStart.setAlpha(0f);
        mCurrentLanguage.setAlpha(0f);
        mBg.setImageResource(shuffleBg.choose());
        mBg.setAlpha(0f);

        mStart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getBaseContext(), HomeActivity.class);
                startActivity(intent);
                finish();
            }
        });

        mRequired.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                languageDialog();
            }
        });
    }

    private void languageDialog() {
        //before inflating the custom alert dialog layout, we will get the current activity viewgroup
        ViewGroup viewGroup = findViewById(android.R.id.content);

        //then we will inflate the custom alert dialog xml that we created
        View dialogView = LayoutInflater.from(this).inflate(R.layout.language_dialog, viewGroup, false);

        View french = dialogView.findViewById(R.id.french);
        View english = dialogView.findViewById(R.id.english);

        french.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setLanguage("fr");
            }
        });

        english.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setLanguage("en");
            }
        });

        //Now we need an AlertDialog.Builder object
        AlertDialog.Builder builder = new AlertDialog.Builder(this);

        //setting the view of the builder to our custom view that we already inflated
        builder.setView(dialogView);

        builder.setOnDismissListener(new DialogInterface.OnDismissListener() {
            @Override
            public void onDismiss(DialogInterface dialogInterface) {
                //mSpam = 0;
            }
        });

        //finally creating the alert dialog and displaying it
        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }

    private void setLanguage(String lang) {
        sharedPreferences.edit().putString("LOCALE", lang).apply();
        Resources res = getResources();
        DisplayMetrics dm = res.getDisplayMetrics();
        Configuration conf = res.getConfiguration();

        conf.setLocale(new Locale(lang));

        res.updateConfiguration(conf, dm);
        Intent intent = new Intent(getBaseContext(), MainActivity.class);
        startActivity(intent);
        finish();
    }


    @Override
    protected void onStart() {
        super.onStart();

        MobileAds.initialize(this, new OnInitializationCompleteListener() {
            @Override
            public void onInitializationComplete(InitializationStatus initializationStatus) {

            }
        });

        Animation fadeAltair =
                AnimationUtils.loadAnimation(getApplicationContext(),
                        R.anim.fade);
        mAltair.startAnimation(fadeAltair);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                mArutairu.setAlpha(1f);
                mStart.setAlpha(1f);
                mWelcome.setAlpha(1f);
                mRequired.setAlpha(1f);
                mCurrentLanguage.setAlpha(1f);
                mBg.setAlpha(0.5f);
                mRequired.startAnimation(fadeAltair2);
                mArutairu.startAnimation(fadeAltair2);
                mStart.startAnimation(fadeAltair2);
                mCurrentLanguage.startAnimation(fadeAltair2);
                mWelcome.startAnimation(fadeAltair2);
                mBg.startAnimation(fadeAltair3);
            }
        },3000);


    }
}
