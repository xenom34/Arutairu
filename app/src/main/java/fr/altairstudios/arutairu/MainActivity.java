package fr.altairstudios.arutairu;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.speech.tts.TextToSpeech;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Objects;

public class MainActivity extends AppCompatActivity {
    private ImageView mAltair, mBg;
    private SharedPreferences sharedPreferences;
    private ImageView mCurrentLanguage;
    private com.google.android.material.textview.MaterialTextView mWelcome;
    private TextView mArutairu;
    public static final String ARUTAIRU_SHARED_PREFS = "ArutairuSharedPrefs";
    private com.google.android.material.button.MaterialButton mStart, mRequired;
    private Animation fadeAltair2, fadeAltair3;
    private final ShuffleBg shuffleBg = new ShuffleBg();
    private boolean executed = false;
    private TextToSpeech textToSpeech;
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

        /*FirebaseMessaging.getInstance().getToken()
                .addOnCompleteListener(new OnCompleteListener<String>() {
                    @Override
                    public void onComplete(@NonNull Task<String> task) {
                        if (!task.isSuccessful()) {
                            Log.w("FKEY", "Fetching FCM registration token failed", task.getException());
                            return;
                        }

                        // Get new FCM registration token
                        String token = task.getResult();

                        // Log and toast
                        Log.d("FKEY", token);
                    }
                });*/

        Log.d("HEY !", String.valueOf(sharedPreferences.getBoolean("POLARIS", false)));

            /*mInterstitialAd = new InterstitialAd(this);
            mInterstitialAd.setAdUnitId("ca-app-pub-9369103706924521/9128046879");
            mInterstitialAd.loadAd(new AdRequest.Builder()
                    .addKeyword(getString(R.string.japanKWords))
                    .addKeyword("nihongo")
                    .addKeyword("tokyo")
                    .addKeyword("manga")
                    .addKeyword("anime")
                    .addKeyword(getString(R.string.gameKWord))
                    .addKeyword(getString(R.string.languageKWord))
                    .addKeyword(getString(R.string.learnKWord))
                    .addKeyword(getString(R.string.travelKWord)).build());
            mInterstitialAd.setAdListener(new AdListener(){
                @Override
                public void onAdClosed() {
                    Intent intent = new Intent(getBaseContext(), HomeActivity.class);
                    startActivity(intent);
                    finish();


                }
            });*/

        conf.setLocale(new Locale(Objects.requireNonNull(sharedPreferences.getString("LOCALE", Locale.getDefault().getLanguage()))));

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

        fadeAltair2 = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.slowfadein);
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

        mStart.setOnClickListener(v -> {

                Intent intent = new Intent(getBaseContext(), HomeActivity.class);
                startActivity(intent);
                finish();

        });

        mRequired.setOnClickListener(v -> languageDialog());
    }

    private void languageDialog() {
        //before inflating the custom alert dialog layout, we will get the current activity viewgroup
        ViewGroup viewGroup = findViewById(android.R.id.content);

        //then we will inflate the custom alert dialog xml that we created
        View dialogView = LayoutInflater.from(this).inflate(R.layout.language_dialog, viewGroup, false);

        View french = dialogView.findViewById(R.id.french);
        View english = dialogView.findViewById(R.id.english);

        french.setOnClickListener(v -> setLanguage("fr"));

        english.setOnClickListener(v -> setLanguage("en"));

        //Now we need an AlertDialog.Builder object
        AlertDialog.Builder builder = new AlertDialog.Builder(this);

        //setting the view of the builder to our custom view that we already inflated
        builder.setView(dialogView);

        builder.setOnDismissListener(dialogInterface -> {
            //mSpam = 0;
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

    private void showTtsCheck(int dialogs) {
        //before inflating the custom alert dialog layout, we will get the current activity viewgroup
        ViewGroup viewGroup = findViewById(android.R.id.content);
        View dialogView;

        //then we will inflate the custom alert dialog xml that we created!
        switch (dialogs){
            case 1:
                dialogView = LayoutInflater.from(this).inflate(R.layout.check_tts_dialog, viewGroup, false);
                break;
            case 2:
                dialogView = LayoutInflater.from(this).inflate(R.layout.tts_not_available, viewGroup, false);
                break;
            case 3:
                dialogView = LayoutInflater.from(this).inflate(R.layout.tts_available_dialog, viewGroup, false);
                break;
            default:
                dialogView = LayoutInflater.from(this).inflate(R.layout.unexpected_error_dialog, viewGroup, false);
                break;
        }

        //Now we need an AlertDialog.Builder object
        AlertDialog.Builder builder = new AlertDialog.Builder(this);

        //setting the view of the builder to our custom view that we already inflated
        builder.setView(dialogView);

        builder.setCancelable(false);
        if (dialogs != 1){
            if (dialogs == 3) {
                builder.setPositiveButton(R.string.super_tts, (dialog, which) -> showMenu());
            }else{
                builder.setNegativeButton(R.string.settings_tts, (dialog, which) -> {
                    textToSpeech.stop();
                    textToSpeech.shutdown();
                    Intent installIntent = new Intent();
                    installIntent.setAction(
                            TextToSpeech.Engine.ACTION_INSTALL_TTS_DATA);
                    startActivity(installIntent);
                    showMenu();
                });
                builder.setPositiveButton(R.string.continue_tts, (dialog, which) -> showMenu());
                builder.setNeutralButton(R.string.download, (dialog, which) -> {
                    Uri uri = Uri.parse("market://details?id=com.google.android.tts");
                    Intent goToMarket = new Intent(Intent.ACTION_VIEW, uri);
                    // To count with Play market backstack, After pressing back button,
                    // to taken back to our application, we need to add following flags to intent.
                    goToMarket.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY |
                            Intent.FLAG_ACTIVITY_NEW_DOCUMENT |
                            Intent.FLAG_ACTIVITY_MULTIPLE_TASK);
                    try {
                        startActivity(goToMarket);
                    } catch (ActivityNotFoundException e) {
                        startActivity(new Intent(Intent.ACTION_VIEW,
                                Uri.parse("https://play.google.com/store/apps/details?id=com.google.android.tts")));
                    }
                });
            }
        }

        //finally creating the alert dialog and displaying it
        final AlertDialog alertDialog = builder.create();
        alertDialog.show();

        if (dialogs == 1){
            new Handler().postDelayed(() -> textToSpeech = new TextToSpeech(getApplicationContext(), status -> {
                if (status == TextToSpeech.SUCCESS && textToSpeech.isLanguageAvailable(Locale.JAPANESE) != TextToSpeech.LANG_NOT_SUPPORTED) {
                    textToSpeech.setLanguage(Locale.JAPANESE);
                    textToSpeech.setSpeechRate(0.9f);
                    showTtsCheck(3);
                    alertDialog.hide();
                } else if (textToSpeech.isLanguageAvailable(Locale.JAPANESE) == TextToSpeech.LANG_NOT_SUPPORTED) {
                    showTtsCheck(2);
                    alertDialog.hide();
                } else if (textToSpeech.isLanguageAvailable(Locale.JAPANESE) == TextToSpeech.LANG_AVAILABLE) {
                    showAskForDownloadTts();
                    alertDialog.hide();
                } else {
                    showTtsCheck(0);
                }
            }), 3000);
        }
        sharedPreferences.edit().putBoolean("TTS", false).apply();
        //alertDialog.hide();
    }

    private void showAskForDownloadTts() {
            //before inflating the custom alert dialog layout, we will get the current activity viewgroup
            ViewGroup viewGroup = findViewById(android.R.id.content);

            //then we will inflate the custom alert dialog xml that we created
            View dialogView = LayoutInflater.from(this).inflate(R.layout.tts_not_available, viewGroup, false);

            //Now we need an AlertDialog.Builder object
            AlertDialog.Builder builder = new AlertDialog.Builder(this);

            //setting the view of the builder to our custom view that we already inflated
            builder.setView(dialogView);


            builder.setPositiveButton(R.string.understood, (dialogInterface, i) -> sharedPreferences.edit().putBoolean("ERROR_SHOWN", true).apply());

            builder.setOnDismissListener(dialogInterface -> {
                //mSpam = 0;
                sharedPreferences.edit().putBoolean("ERROR_SHOWN", true).apply();
            });

            //finally creating the alert dialog and displaying it
            AlertDialog alertDialog = builder.create();
            alertDialog.show();
    }


    @Override
    protected void onStart() {
        super.onStart();

        if (!executed) {

            Animation fadeAltair =
                    AnimationUtils.loadAnimation(getApplicationContext(),
                            R.anim.fade);
            mAltair.startAnimation(fadeAltair);
            mStart.setClickable(false);
            mRequired.setClickable(false);

            new Handler().postDelayed(() -> {
                if (sharedPreferences.getBoolean("TTS", true)){
                    showTtsCheck(1);
                }else{
                    showMenu();
                }
            }, 1500);
        }

    }

    private void showMenu() {
        new Handler().postDelayed(() -> {
            mRequired.setClickable(true);
            mStart.setClickable(true);
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
            executed = true;
        }, 2000);
    }
}
