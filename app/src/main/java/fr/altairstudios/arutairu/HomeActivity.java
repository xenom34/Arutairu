package fr.altairstudios.arutairu;

import android.app.Activity;
import android.app.AlarmManager;
import android.app.AlertDialog;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.TimePickerDialog;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.ColorStateList;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.speech.tts.TextToSpeech;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.navigation.ui.AppBarConfiguration;

import com.android.billingclient.api.BillingClient;
import com.android.billingclient.api.BillingClientStateListener;
import com.android.billingclient.api.BillingFlowParams;
import com.android.billingclient.api.BillingResult;
import com.android.billingclient.api.Purchase;
import com.android.billingclient.api.PurchasesUpdatedListener;
import com.android.billingclient.api.SkuDetails;
import com.android.billingclient.api.SkuDetailsParams;
import com.android.billingclient.api.SkuDetailsResponseListener;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.navigation.NavigationView;
import com.google.android.material.textview.MaterialTextView;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;
import java.util.Random;
import java.util.Vector;

public class HomeActivity extends AppCompatActivity {

    private AppBarConfiguration mAppBarConfiguration;
    private DrawerLayout drawerLayout;
    private LessonsStorage lessonsStorage = new LessonsStorage();
    private int maxWords;
    private boolean firstExec, revisionDialog;
    public static final String ARUTAIRU_SHARED_PREFS = "ArutairuSharedPrefs";
    public static final String FIRST_EXEC = "firsts1";
    public static final String FIRST_REVISION = "revision";
    static boolean waitingForData = false;
    SharedPreferences sharedPreferences;
    LessonsCompleted lessonsCompleted;
    private int state;
    private RadioGroup radioGroup;
    private RadioButton mKanji, mKanas, mRomajis;
    private ImageView topImage;
    private TextView mTitle, mTextProgress, mNbLearn;
    private ProgressBar mProgress;
    private Button mPractice, mTest, mRevision;
    private NavigationView navigationView;
    private MaterialTextView mUnavailableKanji;
    private PurchasesUpdatedListener purchaseUpdateListener = new PurchasesUpdatedListener() {
        @Override
        public void onPurchasesUpdated(BillingResult billingResult, List<Purchase> purchases) {
            if (!(billingResult.getResponseCode() == BillingClient.BillingResponseCode.SERVICE_TIMEOUT || billingResult.getResponseCode() == BillingClient.BillingResponseCode.SERVICE_UNAVAILABLE)) {


                // To be implemented in a later section.
                if (billingResult.getResponseCode() == BillingClient.BillingResponseCode.OK) {
                    if (purchases.get(0).getPurchaseState() == Purchase.PurchaseState.PURCHASED) {
                        Toast.makeText(getApplicationContext(), "PURCHASED", Toast.LENGTH_SHORT).show();
                        sharedPreferences.edit().putBoolean("POLARIS", true).apply();
                        showThanksDialog();
                    }
                } else if (billingResult.getResponseCode() == BillingClient.BillingResponseCode.ITEM_ALREADY_OWNED) {
                    Toast.makeText(getApplicationContext(), "Vous avez déjà ce produit", Toast.LENGTH_SHORT).show();
                    sharedPreferences.edit().putBoolean("POLARIS", true).apply();
                } else {
                    sharedPreferences.edit().putBoolean("POLARIS", false).apply();
                }
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        final Resources res = getResources();
        DisplayMetrics dm = res.getDisplayMetrics();
        Configuration conf = res.getConfiguration();
        sharedPreferences = getSharedPreferences(ARUTAIRU_SHARED_PREFS, MODE_PRIVATE);


        conf.setLocale(new Locale(sharedPreferences.getString("LOCALE", Locale.getDefault().getLanguage())));

        res.updateConfiguration(conf, dm);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        Toolbar toolbar = findViewById(R.id.toolbar);
        toolbar.setBackgroundColor(getResources().getColor(R.color.darkGrey));
        setSupportActionBar(toolbar);
        drawerLayout = findViewById(R.id.drawer_layout);
        radioGroup = findViewById(R.id.radioGroup);
        topImage = findViewById(R.id.imageView2);
        mTitle = findViewById(R.id.textView2);
        mKanji = findViewById(R.id.radio_kanji);
        mKanas = findViewById(R.id.radio_kana);
        mRomajis = findViewById(R.id.radio_romaji);
        mUnavailableKanji = findViewById(R.id.choiceUnavailable);
        mTextProgress = findViewById(R.id.textProgress);
        mProgress = findViewById(R.id.progressBar);
        mPractice = findViewById(R.id.practice);
        mRevision = findViewById(R.id.list);
        mTest = findViewById(R.id.evaluation);
        try {
            loadCompleted();
        } catch (IOException e) {
            lessonsCompleted = new LessonsCompleted();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        navigationView = findViewById(R.id.nav_view);
        mNbLearn = navigationView.getHeaderView(0).findViewById(R.id.nbLearn);
        navigationView.setCheckedItem(R.id.nav_people);
        final Activity activity = this;
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                drawerLayout.closeDrawer(GravityCompat.START);

                if (item.getItemId() == R.id.nav_keyboard){
                    keyboardDialog();
                    return false;
                }else if (item.getItemId() == R.id.nav_rate){
                    rateThisApp();
                    return false;
                }else if(item.getItemId() == R.id.nav_mail){
                    sendEmail();
                    return false;
                }else if(item.getItemId() == R.id.nav_no_ads){
                    if (sharedPreferences.getBoolean("POLARIS", false)){
                        Toast.makeText(getApplicationContext(), R.string.already_purchased, Toast.LENGTH_SHORT).show();
                    }else {
                        final BillingClient mBillingClient;

                        mBillingClient = BillingClient.newBuilder(activity)
                                .setListener(purchaseUpdateListener)
                                .enablePendingPurchases()
                                .build();

                        mBillingClient.startConnection(new BillingClientStateListener() {
                            @Override
                            public void onBillingSetupFinished(@NonNull BillingResult billingResult) {
                                if (billingResult.getResponseCode() == BillingClient.BillingResponseCode.OK) {
                                    // The BillingClient is ready. You can query purchases here.
                                    List<String> skuList = new ArrayList<>();
                                    skuList.add("no_ads");
                                    SkuDetailsParams.Builder params = SkuDetailsParams.newBuilder();
                                    params.setSkusList(skuList).setType(BillingClient.SkuType.INAPP);
                                    mBillingClient.querySkuDetailsAsync(params.build(),
                                            new SkuDetailsResponseListener() {
                                                @Override
                                                public void onSkuDetailsResponse(@NonNull BillingResult billingResult,
                                                                                 List<SkuDetails> skuDetailsList) {
                                                    int responseCode = 1000;
                                                    if (skuDetailsList.isEmpty()) {
                                                        Toast.makeText(getApplicationContext(), "Connexion impossible", Toast.LENGTH_SHORT).show();
                                                    } else {
                                                        BillingFlowParams billingFlowParams = BillingFlowParams.newBuilder()
                                                                .setSkuDetails(skuDetailsList.get(0))
                                                                .build();
                                                        responseCode = mBillingClient.launchBillingFlow(activity, billingFlowParams).getResponseCode();
                                                    }
                                                    if (responseCode == BillingClient.BillingResponseCode.ITEM_ALREADY_OWNED) {
                                                        Toast.makeText(activity, "LICENSE OK", Toast.LENGTH_SHORT).show();
                                                        sharedPreferences.edit().putBoolean("POLARIS", true).apply();
                                                    } else {
                                                        //Toast.makeText(activity, R.string.canceled, Toast.LENGTH_SHORT).show();
                                                    }
                                                }
                                            });
                                } else if (billingResult.getResponseCode() == BillingClient.BillingResponseCode.SERVICE_UNAVAILABLE) {
                                    Toast.makeText(getApplicationContext(), "Service indisponible", Toast.LENGTH_SHORT).show();
                                } else if (billingResult.getResponseCode() == BillingClient.BillingResponseCode.SERVICE_DISCONNECTED) {
                                    Toast.makeText(getApplicationContext(), "Service déconnecté", Toast.LENGTH_SHORT).show();
                                } else if (billingResult.getResponseCode() == BillingClient.BillingResponseCode.SERVICE_TIMEOUT) {
                                    Toast.makeText(getApplicationContext(), "Timeout", Toast.LENGTH_SHORT).show();
                                } else {
                                    Toast.makeText(getApplicationContext(), "Erreur", Toast.LENGTH_SHORT).show();
                                }
                            }

                            @Override
                            public void onBillingServiceDisconnected() {
                                // Try to restart the connection on the next request to
                                // Google Play by calling the startConnection() method.
                            }
                        });
                    }

                    return false;
                }else if(item.getItemId() == R.id.nav_tts){
                    loadVoices();
                    return false;
                }else if(item.getItemId() == R.id.nav_notification){
                    showNotificationDialog();
                    return false;
                }else{
                    initMenu(item.getItemId());
                    refresh(state, item.getTitle());
                }
                return true;
            }
        });

        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.

        final ActionBar actionBar = getSupportActionBar();
        assert actionBar != null;
        actionBar.setTitle(R.string.le_ons);
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setHomeAsUpIndicator(R.drawable.ic_menu_black_24dp);

        sharedPreferences = getSharedPreferences(ARUTAIRU_SHARED_PREFS, MODE_PRIVATE);

        firstExec = sharedPreferences.getBoolean(FIRST_EXEC, true);

    }

    private void loadVoices() {
            showWarningTts();
    }

    public void pickADate(){
        Calendar cldr = Calendar.getInstance();
        int hour = cldr.get(Calendar.HOUR_OF_DAY);
        int minutes = cldr.get(Calendar.MINUTE);
        // time picker dialog
        TimePickerDialog picker;
        picker = new TimePickerDialog(this,
                new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker tp, int sHour, int sMinute) {
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O && sharedPreferences.getBoolean("NSET", true)) {
                            CharSequence name = "Daily Reminder";
                            int importance = NotificationManager.IMPORTANCE_DEFAULT;
                            NotificationChannel channel = new NotificationChannel("Daily Reminder", name, importance);
                            // Register the channel with the system; you can't change the importance
                            // or other notification behaviors after this
                            NotificationManager notificationManager = getSystemService(NotificationManager.class);
                            notificationManager.createNotificationChannel(channel);
                            sharedPreferences.edit().putBoolean("NSET", false).apply();
                        }
                        enableNotification(sHour, sMinute);

                    }
                }, hour, minutes, true);
        picker.show();
    }

    private void sendEmail() {
        String[] TO = {"contact@altair-studios.fr"};
        Intent emailIntent = new Intent(Intent.ACTION_SEND);
        emailIntent.setData(Uri.parse("mailto:contact@altair-studios.fr"));
        emailIntent.setType("message/rfc822");

        Toast.makeText(getApplicationContext(), "contact@altair-studios.fr", Toast.LENGTH_LONG).show();


        emailIntent.putExtra(Intent.EXTRA_EMAIL, TO);

        try {
            startActivity(Intent.createChooser(emailIntent, getString(R.string.send_mail)));
        } catch (android.content.ActivityNotFoundException ex) {
            Toast.makeText(this,
                    "There is no email client installed.", Toast.LENGTH_SHORT).show();
        }
    }

    private void rateThisApp(){
        Uri uri = Uri.parse("market://details?id=" + getApplicationContext().getPackageName());
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
                    Uri.parse("http://play.google.com/store/apps/details?id=" + getApplicationContext().getPackageName())));
        }
    }

    private void refresh(int item, CharSequence title) {
        mTitle.setText(title);
        mTest.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SelectedItemList selectedItemList = new SelectedItemList();
                String[] tempJP;
                String[] tempRomaji;
                if (mKanji.isChecked()){
                    tempJP = getResources().getStringArray(lessonsStorage.getKjRes(state));
                }else if (mRomajis.isChecked()){
                    tempJP = getResources().getStringArray(lessonsStorage.getRmRes(state));
                }else{
                    tempJP = getResources().getStringArray(lessonsStorage.getJpRes(state));
                }
                boolean romaji;
                if(lessonsStorage.haveRomaji(state)){
                    tempRomaji = getResources().getStringArray(lessonsStorage.getRmRes(state));
                    romaji = true;
                }else{
                    tempRomaji = null;
                    romaji = false;
                }
                String[] tempFr;
                if(sharedPreferences.getString("LOCALE", "en").equals("fr")){
                    tempFr = getResources().getStringArray(lessonsStorage.getSrcRes(state));
                }else{
                    tempFr = getResources().getStringArray(lessonsStorage.getEnRes(state));
                }
                Random random = new Random();
                int randomNumber;
                Vector<Integer> indexes = new Vector<>();

                for (int i = 0; i != tempJP.length; i++){
                        indexes.add(i);
                }

                int limit = 20;

                if (tempJP.length < 20)
                    limit = tempJP.length;

                if (tempJP.length-lessonsCompleted.howManyCompleted(state) <20){
                    limit = tempJP.length-lessonsCompleted.howManyCompleted(state);
                }

                for (int i = 0; i != limit; i++){
                    do{
                        randomNumber = indexes.elementAt(random.nextInt(indexes.size()));
                    }while (lessonsCompleted.isCompleted(state, randomNumber));
                    selectedItemList.addJp(tempJP[randomNumber]);
                    assert tempRomaji != null;
                    selectedItemList.addFrench(tempFr[randomNumber]);
                    selectedItemList.addCorrespondingIndex(randomNumber);
                    indexes.removeElement(randomNumber);
                }
                selectedItemList.setCorrespondingLesson(state);

                Intent intent = new Intent(getApplicationContext(), Exercise.class);
                intent.putExtra("LESSON", selectedItemList);
                intent.putExtra("COMPLETED", lessonsCompleted);
                intent.putExtra("RETRIEVE", true);
                intent.putExtra("ROMAJI", romaji);
                intent.putExtra("SAVE", true);
                intent.putExtra("CHAPTER", state);
                intent.putExtra("REVISION", false);
                intent.putExtra("MAX", limit);
                waitingForData = true;
                startActivity(intent);
                finish();
            }
        });
        mPractice.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selector();
            }
        });
        float progress = (float)(lessonsCompleted.howManyCompleted(state))/(float)(getResources().getStringArray(lessonsStorage.getJpRes(state)).length)*100;
        mProgress.setProgress((int)progress);
        mTextProgress.setText((int)progress+"%");
        mRevision.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), Revision.class);
                intent.putExtra("LESSON", state);
                intent.putExtra("MAX", getResources().getStringArray(lessonsStorage.getJpRes(state)).length);
                intent.putExtra("COMPLETED", lessonsCompleted);
                intent.putExtra("RETRIEVE", false);
                intent.putExtra("REVISION", true);
                if(!lessonsStorage.haveRomaji(state)){
                    intent.putExtra("ROMAJI", false);
                }else{
                    intent.putExtra("ROMAJI", true);
                }
                intent.putExtra("LOCALE", sharedPreferences.getString("LOCALE", "en"));
                intent.putExtra("FIRST", revisionDialog);
                intent.putExtra("KANJI", mKanji.isChecked());
                intent.putExtra("WITH_ROMAJI", mRomajis.isChecked());
                sharedPreferences.edit().putBoolean(FIRST_REVISION, false).apply();
                waitingForData = false;
                startActivity(intent);
                finish();
            }
        });
        if(progress == 100){
            mProgress.setProgressTintList(ColorStateList.valueOf(Color.GREEN));
            mTest.setClickable(false);
        }
        else{
            mProgress.setProgressTintList(ColorStateList.valueOf(Color.RED));
            mTest.setClickable(true);
        }
    }

    private void initMenu(int id){
        switch (id){
            case R.id.nav_hiragana:
                topImage.setImageResource(R.drawable.hiragana);
                state = LessonsStorage.HIRAGANA;
                break;
            case R.id.nav_katakana:
                topImage.setImageResource(R.drawable.katakana);
                state = LessonsStorage.KATAKANA;
                break;
            case R.id.nav_numbers:
                topImage.setImageResource(R.drawable.numbers);
                state = LessonsStorage.NUMBERS;
                break;
            case R.id.nav_people:
                topImage.setImageResource(R.drawable.people);
                state = LessonsStorage.PEOPLE;
                break;
            case R.id.nav_jobs:
                state = LessonsStorage.JOBS;
                topImage.setImageResource(R.drawable.jobs);
                break;
            case R.id.nav_body:
                state = LessonsStorage.BODY;
                topImage.setImageResource(R.drawable.body);
                break;
            case R.id.nav_family:
                state = LessonsStorage.FAMILY;
                topImage.setImageResource(R.drawable.family);
                break;
            case R.id.nav_animals:
                state = LessonsStorage.ANIMALS;
                topImage.setImageResource(R.drawable.animals);
                break;
            case R.id.nav_plants:
                state = LessonsStorage.PLANTS;
                topImage.setImageResource(R.drawable.plants);
                break;
            case R.id.nav_crop:
                state = LessonsStorage.CROPS;
                topImage.setImageResource(R.drawable.crop);
                break;
            case R.id.nav_food:
                topImage.setImageResource(R.drawable.food);
                state = LessonsStorage.FOOD;
                break;
            case R.id.nav_drinks:
                topImage.setImageResource(R.drawable.drinks);
                state = LessonsStorage.DRINK;
                break;
            case R.id.nav_weeks:
                topImage.setImageResource(R.drawable.week);
                state = LessonsStorage.DAYS;
                break;
            case R.id.nav_weather:
                topImage.setImageResource(R.drawable.weather);
                state = LessonsStorage.WEATHER;
                break;
            case R.id.nav_directions:
                topImage.setImageResource(R.drawable.directions);
                state = LessonsStorage.DIRECTIONS;
                break;
            case R.id.nav_materials:
                topImage.setImageResource(R.drawable.materials);
                state = LessonsStorage.MATERIALS;
                break;
            case R.id.nav_weights:
                topImage.setImageResource(R.drawable.measures);
                state = LessonsStorage.WEIGHTS;
                break;
            case R.id.nav_society:
                topImage.setImageResource(R.drawable.society);
                state = LessonsStorage.SOCIETY;
                break;
            case R.id.nav_homes:
                topImage.setImageResource(R.drawable.home);
                state = LessonsStorage.HOME;
                break;
            case R.id.nav_tools:
                topImage.setImageResource(R.drawable.tools);
                state = LessonsStorage.TOOLS;
                break;
            case R.id.nav_stationery:
                topImage.setImageResource(R.drawable.stationery);
                state = LessonsStorage.STATIONERY;
                break;
            case R.id.nav_clothes:
                topImage.setImageResource(R.drawable.clothes);
                state = LessonsStorage.CLOTHES;
                break;
            case R.id.nav_transport:
                topImage.setImageResource(R.drawable.transport);
                state = LessonsStorage.TRANSPORT;
                break;
            case R.id.nav_language:
                topImage.setImageResource(R.drawable.language);
                state = LessonsStorage.LANGUAGE;
                break;
            case R.id.nav_media:
                topImage.setImageResource(R.drawable.medias);
                state = LessonsStorage.MEDIA;
                break;
            case R.id.nav_colors:
                topImage.setImageResource(R.drawable.colors);
                state = LessonsStorage.COLORS;
                break;
            case R.id.nav_subject:
                topImage.setImageResource(R.drawable.subject);
                state = LessonsStorage.SUBJECT;
                break;
            case R.id.nav_time:
                topImage.setImageResource(R.drawable.time);
                state = LessonsStorage.TIME;
                break;
            case R.id.nav_n5:
                topImage.setImageResource(R.drawable.n5);
                state = LessonsStorage.N5;
                break;
            case R.id.nav_n4:
                topImage.setImageResource(R.drawable.n4);
                state = LessonsStorage.N4;
                break;
            case R.id.nav_n3:
                topImage.setImageResource(R.drawable.n3);
                state = LessonsStorage.N3;
                break;
            case R.id.nav_n2:
                topImage.setImageResource(R.drawable.n2);
                state = LessonsStorage.N2;
                break;
            case R.id.nav_n1:
                topImage.setImageResource(R.drawable.n1);
                state = LessonsStorage.N1;
                break;
            case R.id.nav_k11:
                topImage.setImageResource(R.drawable.n1);
                state = LessonsStorage.K1;
                break;
            case R.id.nav_k2:
                topImage.setImageResource(R.drawable.n2);
                state = LessonsStorage.K2;
                break;
            case R.id.nav_k3:
                topImage.setImageResource(R.drawable.n3);
                state = LessonsStorage.K3;
                break;
            case R.id.nav_k4:
                topImage.setImageResource(R.drawable.n4);
                state = LessonsStorage.K4;
                break;
            case R.id.nav_k5:
                topImage.setImageResource(R.drawable.n5);
                state = LessonsStorage.K5;
                break;
            case R.id.nav_radicals:
                topImage.setImageResource(R.drawable.two);
                state = LessonsStorage.RADICALS;
                break;
            case R.id.nav_conversation:
                topImage.setImageResource(R.drawable.tokyo);
                state = LessonsStorage.SENTENCES;
                break;
            case R.id.nav_geography:
                topImage.setImageResource(R.drawable.map);
                state = LessonsStorage.GEOGRAPHY;
                break;
        }

        setSwitch(state);

        sharedPreferences.edit().putInt("STATE", state).apply();
    }

    private void setSwitch(int state) {
        if(lessonsStorage.haveKanji(state)){
            mKanas.setVisibility(View.VISIBLE);
            radioGroup.setClickable(true);
            mUnavailableKanji.setVisibility(View.INVISIBLE);
            mRomajis.setVisibility(View.VISIBLE);
            mKanji.setVisibility(View.VISIBLE);
        }else {
            mRomajis.setVisibility(View.INVISIBLE);
            radioGroup.setClickable(false);
            mUnavailableKanji.setVisibility(View.VISIBLE);
            mKanas.setChecked(true);
            mKanas.setVisibility(View.INVISIBLE);
            mKanji.setVisibility(View.INVISIBLE);
        }
    }


    private void keyboardDialog() {
        //before inflating the custom alert dialog layout, we will get the current activity viewgroup
        ViewGroup viewGroup = findViewById(android.R.id.content);

        //then we will inflate the custom alert dialog xml that we created
        View dialogView = LayoutInflater.from(this).inflate(R.layout.keyboard_dialog, viewGroup, false);

        //Now we need an AlertDialog.Builder object
        AlertDialog.Builder builder = new AlertDialog.Builder(this);

        //setting the view of the builder to our custom view that we already inflated
        builder.setView(dialogView);

        MaterialButton mConfigure = dialogView.findViewById(R.id.configure);
        MaterialButton mGboard = dialogView.findViewById(R.id.gboard);

        mConfigure.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                InputMethodManager imeManager = (InputMethodManager) getApplicationContext().getSystemService(INPUT_METHOD_SERVICE);
                assert imeManager != null;
                imeManager.showInputMethodPicker();
            }
        });

        mGboard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent browse = new Intent( Intent.ACTION_VIEW , Uri.parse( "https://support.google.com/gboard/answer/7068494?co=GENIE.Platform%3DAndroid&hl=en" ) );
                startActivity( browse );
            }
        });

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

    private void showChoosingWord(final int chapter) {
        //before inflating the custom alert dialog layout, we will get the current activity viewgroup
        ViewGroup viewGroup = findViewById(android.R.id.content);

        //then we will inflate the custom alert dialog xml that we created
        View dialogView = LayoutInflater.from(this).inflate(R.layout.dialog_choosing_word, viewGroup, false);

        ListView listView = dialogView.findViewById(R.id.listWords);
        ArrayList<String> words;
        if (mKanji.isChecked()){
            words = new ArrayList<>(Arrays.asList(getResources().getStringArray(lessonsStorage.getKjRes(chapter+1))));
        }else if (mRomajis.isChecked()){
            words = new ArrayList<>(Arrays.asList(getResources().getStringArray(lessonsStorage.getRmRes(chapter+1))));
        }else{
            words = new ArrayList<>(Arrays.asList(getResources().getStringArray(lessonsStorage.getJpRes(chapter+1))));
        }
        ArrayList<String> wordsFr;
        if(sharedPreferences.getString("LOCALE", "en").equals("fr")){
            wordsFr = new ArrayList<>(Arrays.asList(getResources().getStringArray(lessonsStorage.getSrcRes(chapter+1))));
        }else {
            wordsFr = new ArrayList<>(Arrays.asList(getResources().getStringArray(lessonsStorage.getEnRes(chapter+1))));
        }
        final WordsAdapter wordsAdapter = new WordsAdapter(this, words,wordsFr, chapter, lessonsCompleted);
        listView.setAdapter(wordsAdapter);
        //Now we need an AlertDialog.Builder object
        AlertDialog.Builder builder = new AlertDialog.Builder(this);

        //setting the view of the builder to our custom view that we already inflated
        builder.setView(dialogView);


        builder.setPositiveButton(R.string.go, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                if(!wordsAdapter.selectedItemList.getSelected().isEmpty()) {
                    SelectedItemList selectedItemList = wordsAdapter.selectedItemList;
                    String[] tempJP;
                    if (mKanji.isChecked()){
                        tempJP = getResources().getStringArray(lessonsStorage.getKjRes(chapter + 1));
                    }else if (mRomajis.isChecked()){
                        tempJP = getResources().getStringArray(lessonsStorage.getRmRes(chapter + 1));
                    }else{
                        tempJP = getResources().getStringArray(lessonsStorage.getJpRes(chapter + 1));
                    }
                    boolean romaji;
                    String[] tempRomaji;
                    if (lessonsStorage.haveRomaji(chapter + 1)){
                        tempRomaji = getResources().getStringArray(lessonsStorage.getRmRes(chapter + 1));
                        romaji = true;
                    }else{
                        tempRomaji = new String[tempJP.length+3];
                        for (int e = 0; e!= tempJP.length +2; e++){
                            tempRomaji[e] = "Romaji not available";
                        }
                        romaji = false;
                    }
                    String[] tempFr;
                    String[] tempTts;
                    if(sharedPreferences.getString("LOCALE", "en").equals("fr")){
                        tempFr = getResources().getStringArray(lessonsStorage.getSrcRes(chapter + 1));
                    }else{
                        tempFr = getResources().getStringArray(lessonsStorage.getEnRes(chapter + 1));
                    }
                    tempTts = getResources().getStringArray(lessonsStorage.getJpRes(chapter+1));
                    Random random = new Random();
                    int randomNumber;
                    Vector<Integer> indexes = new Vector<>(selectedItemList.getSelected());
                    int size = indexes.size();

                    for (int k = 0; k != size; k++) {
                        randomNumber = indexes.elementAt(random.nextInt(indexes.size()));
                        selectedItemList.addJp(tempJP[randomNumber]);
                        selectedItemList.addRomaji(tempRomaji[randomNumber]);
                        selectedItemList.addFrench(tempFr[randomNumber]);
                        selectedItemList.addCorrespondingIndex(randomNumber);
                        selectedItemList.addTts(tempTts[randomNumber]);
                        indexes.removeElement(randomNumber);
                    }
                    selectedItemList.setCorrespondingLesson(chapter);

                    Intent intent = new Intent(getApplicationContext(), Revision.class);
                    intent.putExtra("LESSON", selectedItemList);
                    intent.putExtra("COMPLETED", lessonsCompleted);
                    intent.putExtra("RETRIEVE", true);
                    intent.putExtra("FIRST", revisionDialog);
                    intent.putExtra("ROMAJI", romaji);
                    sharedPreferences.edit().putBoolean(FIRST_REVISION, false).apply();
                    waitingForData = false;
                    startActivity(intent);
                    finish();
                }
            }
        });

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

    private void selector() {
        //before inflating the custom alert dialog layout, we will get the current activity viewgroup
        ViewGroup viewGroup = findViewById(android.R.id.content);

        //then we will inflate the custom alert dialog xml that we created
        final View dialogView = LayoutInflater.from(this).inflate(R.layout.dialog_selector, viewGroup, false);
        final String[] tempJP;
        if (mKanji.isChecked()){
            tempJP = getResources().getStringArray(lessonsStorage.getKjRes(state));
        }else if (mRomajis.isChecked()){
            tempJP = getResources().getStringArray(lessonsStorage.getRmRes(state));
        }else{
            tempJP = getResources().getStringArray(lessonsStorage.getJpRes(state));
        }
        final String[] tempRomaji;
        boolean romaji;
        if (lessonsStorage.haveRomaji(state)){
            tempRomaji = getResources().getStringArray(lessonsStorage.getRmRes(state));
            romaji = true;
        }else{
            tempRomaji = null;
        }
        final String[] tempFr;
        final String[] tempTts;
        if (sharedPreferences.getString("LOCALE", "en").equals("fr")){
            tempFr = getResources().getStringArray(lessonsStorage.getSrcRes(state));
        }else{
            tempFr = getResources().getStringArray(lessonsStorage.getEnRes(state));
        }
        final TextView min = dialogView.findViewById(R.id.min);
        final TextView max = dialogView.findViewById(R.id.max);
        final TextView nb = dialogView.findViewById(R.id.nb);
        tempTts = getResources().getStringArray(lessonsStorage.getJpRes(state));

        min.setText(1+"");
        max.setText(tempJP.length+"");

        //Now we need an AlertDialog.Builder object
        AlertDialog.Builder builder = new AlertDialog.Builder(this);

        //setting the view of the builder to our custom view that we already inflated
        builder.setView(dialogView);
        builder.setNeutralButton(R.string.selector, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                showChoosingWord(state-1);
            }
        });

        builder.setPositiveButton(R.string.ctipar, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                try {
                    if (!(Integer.parseInt(min.getText() + "") < 1 || Integer.parseInt(max.getText() + "") > tempJP.length || Integer.parseInt(min.getText() + "") > Integer.parseInt(max.getText() + "") || Integer.parseInt(nb.getText() + "") < 1 || Integer.parseInt(nb.getText() + "") > tempJP.length)) {
                        SelectedItemList selector = new SelectedItemList();

                        Random random = new Random();
                        int randomNumber;
                        Vector<Integer> indexes = new Vector<>();

                        for (int i = Integer.parseInt(min.getText() + "") - 1; i != Integer.parseInt(max.getText() + ""); i++) {
                            indexes.add(i);
                        }

                        int size = Integer.parseInt(nb.getText().toString());

                        for (int i = 0; i != size; i++) {
                            randomNumber = indexes.elementAt(random.nextInt(indexes.size()));
                            selector.addJp(tempJP[randomNumber]);
                            if (lessonsStorage.haveRomaji(state)) {
                                selector.addRomaji(tempRomaji[randomNumber]);
                            }
                            selector.addFrench(tempFr[randomNumber]);
                            selector.addTts(tempTts[randomNumber]);
                            selector.addCorrespondingIndex(randomNumber);
                            indexes.removeElement(randomNumber);
                        }

                        selector.setCorrespondingLesson(state);

                        Intent intent = new Intent(getApplicationContext(), Revision.class);
                        intent.putExtra("LESSON", selector);
                        intent.putExtra("COMPLETED", lessonsCompleted);
                        intent.putExtra("RETRIEVE", true);
                        if (lessonsStorage.haveRomaji(state)) {
                            intent.putExtra("ROMAJI", true);
                        } else {
                            intent.putExtra("ROMAJI", false);
                        }
                        intent.putExtra("FIRST", revisionDialog);
                        sharedPreferences.edit().putBoolean(FIRST_REVISION, false).apply();
                        waitingForData = false;
                        startActivity(intent);
                        finish();
                    }
                }catch (Exception e){
                    showErrorDialog();
                    Log.d("DIALOG", e.getMessage());
                }
            }
        });

        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }

    private void showWarningTts() {
        //before inflating the custom alert dialog layout, we will get the current activity viewgroup
        ViewGroup viewGroup = findViewById(android.R.id.content);

        //then we will inflate the custom alert dialog xml that we created
        View dialogView = LayoutInflater.from(this).inflate(R.layout.warning_tts_dialog, viewGroup, false);

        //Now we need an AlertDialog.Builder object
        AlertDialog.Builder builder = new AlertDialog.Builder(this);

        //setting the view of the builder to our custom view that we already inflated
        builder.setView(dialogView);


        builder.setPositiveButton(R.string.go, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                Intent installIntent = new Intent();
                installIntent.setAction(
                        TextToSpeech.Engine.ACTION_INSTALL_TTS_DATA);
                startActivity(installIntent);
            }
        });

        builder.setOnDismissListener(new DialogInterface.OnDismissListener() {
            @Override
            public void onDismiss(DialogInterface dialogInterface) {
            }
        });

        //finally creating the alert dialog and displaying it
        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }

    private void showThanksDialog() {
        //before inflating the custom alert dialog layout, we will get the current activity viewgroup
        ViewGroup viewGroup = findViewById(android.R.id.content);

        //then we will inflate the custom alert dialog xml that we created
        View dialogView = LayoutInflater.from(this).inflate(R.layout.purchased_ok_dialog, viewGroup, false);

        //Now we need an AlertDialog.Builder object
        AlertDialog.Builder builder = new AlertDialog.Builder(this);

        //setting the view of the builder to our custom view that we already inflated
        builder.setView(dialogView);


        builder.setPositiveButton(R.string.continue_tts, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

            }
        });

        builder.setOnDismissListener(new DialogInterface.OnDismissListener() {
            @Override
            public void onDismiss(DialogInterface dialogInterface) {

            }
        });

        //finally creating the alert dialog and displaying it
        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }

    private void showDialog() {
        //before inflating the custom alert dialog layout, we will get the current activity viewgroup
        ViewGroup viewGroup = findViewById(android.R.id.content);

        //then we will inflate the custom alert dialog xml that we created
        View dialogView = LayoutInflater.from(this).inflate(R.layout.welcome_dialog, viewGroup, false);

        //Now we need an AlertDialog.Builder object
        AlertDialog.Builder builder = new AlertDialog.Builder(this);

        //setting the view of the builder to our custom view that we already inflated
        builder.setView(dialogView);


        builder.setPositiveButton(R.string.go, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                SharedPreferences sharedPreferences = getSharedPreferences(ARUTAIRU_SHARED_PREFS, MODE_PRIVATE);
                sharedPreferences.edit().putBoolean(FIRST_EXEC, false).apply();
                firstExec = false;
            }
        });

        builder.setOnDismissListener(new DialogInterface.OnDismissListener() {
            @Override
            public void onDismiss(DialogInterface dialogInterface) {
                //mSpam = 0;
                SharedPreferences sharedPreferences = getSharedPreferences(ARUTAIRU_SHARED_PREFS, MODE_PRIVATE);
                sharedPreferences.edit().putBoolean(FIRST_EXEC, false).apply();
                firstExec = false;
            }
        });

        //finally creating the alert dialog and displaying it
        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }

    private void showNotificationDialog() {
        //before inflating the custom alert dialog layout, we will get the current activity viewgroup
        ViewGroup viewGroup = findViewById(android.R.id.content);

        //then we will inflate the custom alert dialog xml that we created
        View dialogView = LayoutInflater.from(this).inflate(R.layout.notifications, viewGroup, false);

        //Now we need an AlertDialog.Builder object
        AlertDialog.Builder builder = new AlertDialog.Builder(this);

        //setting the view of the builder to our custom view that we already inflated
        builder.setView(dialogView);


        builder.setPositiveButton(R.string.reminder_time, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
               pickADate();

            }
        });
        
        builder.setNegativeButton(R.string.no, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                sharedPreferences.edit().putBoolean("NOTIFS", false).apply();
            }
        });

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

    private void enableNotification(int hour, int minute) {
        Intent intent = new Intent(getBaseContext(), DailyReminderBroadcast.class);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(getBaseContext(), 100, intent, 0);

        AlarmManager alarmManager = (AlarmManager) getBaseContext().getSystemService(ALARM_SERVICE);

        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(System.currentTimeMillis());
        calendar.set(Calendar.HOUR_OF_DAY, hour);
        calendar.set(Calendar.MINUTE, minute);

        assert alarmManager != null;
        alarmManager.setRepeating(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(),
                AlarmManager.INTERVAL_DAY, pendingIntent);


        sharedPreferences.edit().putLong("CALENDAR_VALUE", calendar.getTimeInMillis()).apply();
        sharedPreferences.edit().putBoolean("NOTIFS", true).apply();
        sharedPreferences.edit().putInt("HOURS", hour).apply();
        sharedPreferences.edit().putInt("MIN", minute).apply();
        sharedPreferences.edit().putBoolean("MESSAGE1", false).apply();
        Toast.makeText(this, R.string.reminder_set, Toast.LENGTH_SHORT).show();
    }

    private void showErrorDialog() {
        //before inflating the custom alert dialog layout, we will get the current activity viewgroup
        ViewGroup viewGroup = findViewById(android.R.id.content);

        //then we will inflate the custom alert dialog xml that we created
        View dialogView = LayoutInflater.from(this).inflate(R.layout.bad_input_dialog, viewGroup, false);

        //Now we need an AlertDialog.Builder object
        AlertDialog.Builder builder = new AlertDialog.Builder(this);

        //setting the view of the builder to our custom view that we already inflated
        builder.setView(dialogView);


        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                selector();
            }
        });

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

    @Override
    protected void onStart() {
        super.onStart();

        if (waitingForData){
            lessonsCompleted = (LessonsCompleted) getIntent().getSerializableExtra("COMPLETED");
            try {
                saveCompleted();
            } catch (IOException e) {
                e.printStackTrace();
                waitingForData = false;
            }
            waitingForData = false;
        }

        if(firstExec){
            showDialog();
            revisionDialog = true;
            sharedPreferences.edit().putBoolean(FIRST_REVISION, true).apply();
        }
        revisionDialog = sharedPreferences.getBoolean(FIRST_REVISION, true);
        state = sharedPreferences.getInt("STATE", 6);
        setSwitch(state);

        checkingFirstMenuItem(state);
        String s = getResources().getString(R.string.nombres_de_mots_appris) + lessonsCompleted.totalCompleted();
        mNbLearn.setText(s);

    }

    void checkingFirstMenuItem(int element){
        switch (element){
            case LessonsStorage.HIRAGANA:
                navigationView.setCheckedItem(R.id.nav_hiragana);
                initMenu(R.id.nav_hiragana);
                refresh(state, getResources().getString(R.string.hiragana));
                break;
            case LessonsStorage.KATAKANA:
                navigationView.setCheckedItem(R.id.nav_katakana);
                initMenu(R.id.nav_katakana);
                refresh(state, getResources().getString(R.string.katakana));
                break;
            case LessonsStorage.NUMBERS:
                navigationView.setCheckedItem(R.id.nav_numbers);
                initMenu(R.id.nav_numbers);
                refresh(state, getResources().getString(R.string.les_nombres));
                break;
            case LessonsStorage.PEOPLE:
                navigationView.setCheckedItem(R.id.nav_people);
                initMenu(R.id.nav_people);
                refresh(state, getResources().getString(R.string.les_gens));
                break;
            case LessonsStorage.JOBS:
                navigationView.setCheckedItem(R.id.nav_jobs);
                initMenu(R.id.nav_jobs);
                refresh(state, getResources().getString(R.string.les_m_tiers));
                break;
            case LessonsStorage.BODY:
                navigationView.setCheckedItem(R.id.nav_body);
                initMenu(R.id.nav_body);
                refresh(state, getResources().getString(R.string.le_corps));
                break;
            case LessonsStorage.FAMILY:
                navigationView.setCheckedItem(R.id.nav_family);
                initMenu(R.id.nav_family);
                refresh(state, getResources().getString(R.string.la_famille));
                break;
            case LessonsStorage.ANIMALS:
                navigationView.setCheckedItem(R.id.nav_animals);
                initMenu(R.id.nav_animals);
                refresh(state, getResources().getString(R.string.les_animaux));
                break;
            case LessonsStorage.PLANTS:
                navigationView.setCheckedItem(R.id.nav_plants);
                initMenu(R.id.nav_plants);
                refresh(state, getResources().getString(R.string.les_plantes));
                break;
            case LessonsStorage.CROPS:
                navigationView.setCheckedItem(R.id.nav_crop);
                initMenu(R.id.nav_crop);
                refresh(state, getResources().getString(R.string.l_agricuclture));
                break;
            case LessonsStorage.FOOD:
                navigationView.setCheckedItem(R.id.nav_food);
                initMenu(R.id.nav_food);
                refresh(state, getResources().getString(R.string.la_nourriture));
                break;
            case LessonsStorage.DRINK:
                navigationView.setCheckedItem(R.id.nav_drinks);
                initMenu(R.id.nav_drinks);
                refresh(state, getResources().getString(R.string.les_boissons));
                break;
            case LessonsStorage.DAYS:
                navigationView.setCheckedItem(R.id.nav_weeks);
                initMenu(R.id.nav_weeks);
                refresh(state, getResources().getString(R.string.les_jours));
                break;
            case LessonsStorage.WEATHER:
                navigationView.setCheckedItem(R.id.nav_weather);
                initMenu(R.id.nav_weather);
                refresh(state, getResources().getString(R.string.la_m_t_o));
                break;
            case LessonsStorage.DIRECTIONS:
                navigationView.setCheckedItem(R.id.nav_directions);
                initMenu(R.id.nav_directions);
                refresh(state, getResources().getString(R.string.les_directions));
                break;
            case LessonsStorage.MATERIALS:
                navigationView.setCheckedItem(R.id.nav_materials);
                initMenu(R.id.nav_materials);
                refresh(state, getResources().getString(R.string.les_m_t_riaux));
                break;
            case LessonsStorage.WEIGHTS:
                navigationView.setCheckedItem(R.id.nav_weights);
                initMenu(R.id.nav_weights);
                refresh(state, getResources().getString(R.string.les_mesures));
                break;
            case LessonsStorage.SOCIETY:
                navigationView.setCheckedItem(R.id.nav_society);
                initMenu(R.id.nav_society);
                refresh(state, getResources().getString(R.string.la_soci_t));
                break;
            case LessonsStorage.HOME:
                navigationView.setCheckedItem(R.id.nav_homes);
                initMenu(R.id.nav_homes);
                refresh(state, getResources().getString(R.string.la_maison));
                break;
            case LessonsStorage.TOOLS:
                navigationView.setCheckedItem(R.id.nav_tools);
                initMenu(R.id.nav_tools);
                refresh(state, getResources().getString(R.string.les_outils));
                break;
            case LessonsStorage.STATIONERY:
                navigationView.setCheckedItem(R.id.nav_stationery);
                initMenu(R.id.nav_stationery);
                refresh(state, getResources().getString(R.string.la_papeterie));
                break;
            case LessonsStorage.CLOTHES:
                navigationView.setCheckedItem(R.id.nav_clothes);
                initMenu(R.id.nav_clothes);
                refresh(state, getResources().getString(R.string.les_v_tements));
                break;
            case LessonsStorage.TRANSPORT:
                navigationView.setCheckedItem(R.id.nav_transport);
                initMenu(R.id.nav_transport);
                refresh(state, getResources().getString(R.string.les_transports));
                break;
            case LessonsStorage.LANGUAGE:
                navigationView.setCheckedItem(R.id.nav_language);
                initMenu(R.id.nav_language);
                refresh(state, getResources().getString(R.string.les_langues));
                break;
            case LessonsStorage.MEDIA:
                navigationView.setCheckedItem(R.id.nav_media);
                initMenu(R.id.nav_media);
                refresh(state, getResources().getString(R.string.les_m_dias));
                break;
            case LessonsStorage.COLORS:
                navigationView.setCheckedItem(R.id.nav_colors);
                initMenu(R.id.nav_colors);
                refresh(state, getResources().getString(R.string.les_couleurs));
                break;
            case LessonsStorage.SUBJECT:
                navigationView.setCheckedItem(R.id.nav_subject);
                initMenu(R.id.nav_subject);
                refresh(state, getResources().getString(R.string.sujets_d_tudes));
                break;
            case LessonsStorage.TIME:
                navigationView.setCheckedItem(R.id.nav_time);
                initMenu(R.id.nav_time);
                refresh(state, getResources().getString(R.string.le_temps));
                break;
            case LessonsStorage.N5:
                navigationView.setCheckedItem(R.id.nav_n5);
                initMenu(R.id.nav_n5);
                refresh(state, getResources().getString(R.string.jlpt_novice));
                break;
            case LessonsStorage.N4:
                navigationView.setCheckedItem(R.id.nav_n4);
                initMenu(R.id.nav_n4);
                refresh(state, getResources().getString(R.string.jlpt_d_butant));
                break;
            case LessonsStorage.N3:
                navigationView.setCheckedItem(R.id.nav_n3);
                initMenu(R.id.nav_n3);
                refresh(state, getResources().getString(R.string.jlpt_interm_diaire));
                break;
            case LessonsStorage.N2:
                navigationView.setCheckedItem(R.id.nav_n2);
                initMenu(R.id.nav_n2);
                refresh(state, getResources().getString(R.string.jlpt_avanc));
                break;
            case LessonsStorage.N1:
                navigationView.setCheckedItem(R.id.nav_n1);
                initMenu(R.id.nav_n1);
                refresh(state, getResources().getString(R.string.jlpt_expert));
                break;
            case LessonsStorage.K1:
                navigationView.setCheckedItem(R.id.nav_k11);
                initMenu(R.id.nav_k11);
                refresh(state, getResources().getString(R.string.kanji_expert));
                break;
            case LessonsStorage.K2:
                navigationView.setCheckedItem(R.id.nav_k2);
                initMenu(R.id.nav_k2);
                refresh(state, getResources().getString(R.string.kanji_avanc));
                break;
            case LessonsStorage.K3:
                navigationView.setCheckedItem(R.id.nav_k3);
                initMenu(R.id.nav_k3);
                refresh(state, getResources().getString(R.string.kanji_interm_diaire));
                break;
            case LessonsStorage.K4:
                navigationView.setCheckedItem(R.id.nav_k4);
                initMenu(R.id.nav_k3);
                refresh(state, getResources().getString(R.string.kanji_d_butant));
                break;
            case LessonsStorage.K5:
                navigationView.setCheckedItem(R.id.nav_k5);
                initMenu(R.id.nav_k5);
                refresh(state, getResources().getString(R.string.kanji_novice));
                break;
            case LessonsStorage.RADICALS:
                navigationView.setCheckedItem(R.id.nav_radicals);
                initMenu(R.id.nav_radicals);
                refresh(state, getResources().getString(R.string.les_radicaux));
                break;
            case LessonsStorage.SENTENCES:
                navigationView.setCheckedItem(R.id.nav_conversation);
                initMenu(R.id.nav_conversation);
                refresh(state, getResources().getString(R.string.useful_sentences));
                break;
            case LessonsStorage.GEOGRAPHY:
                navigationView.setCheckedItem(R.id.nav_geography);
                initMenu(R.id.nav_geography);
                refresh(state, getResources().getString(R.string.geography));
                break;
        }
    }

    @Override
    protected void onRestart() {
        super.onRestart();
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    void saveCompleted() throws IOException {
        FileOutputStream fos = getApplicationContext().openFileOutput("saves.dat", Context.MODE_PRIVATE);
        ObjectOutputStream os = new ObjectOutputStream(fos);
        os.writeObject(lessonsCompleted);
        os.close();
        fos.close();
    }

    void loadCompleted() throws IOException, ClassNotFoundException {
        FileInputStream fis = getApplicationContext().openFileInput("saves.dat");
        ObjectInputStream is = new ObjectInputStream(fis);
        lessonsCompleted = (LessonsCompleted) is.readObject();
        is.close();
        fis.close();
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        switch (item.getItemId()){

            case android.R.id.home:
                drawerLayout.openDrawer(GravityCompat.START);
                return true;

        }
        return super.onOptionsItemSelected(item);
    }
}
