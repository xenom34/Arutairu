package fr.altairstudios.arutairu;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.ads.AdError;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.FullScreenContentCallback;
import com.google.android.gms.ads.LoadAdError;
import com.google.android.gms.ads.interstitial.InterstitialAd;
import com.google.android.gms.ads.interstitial.InterstitialAdLoadCallback;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textview.MaterialTextView;

import java.util.Locale;
import java.util.Objects;
import java.util.Random;

public class Exercise extends AppCompatActivity {
    private TextInputEditText mAnswer;
    private MaterialButton mSubmit;
    private ImageView mcheck;
    SelectedItemList selectedItemList;
    private InterstitialAd mInterstitialAd;
    public static final String ARUTAIRU_SHARED_PREFS = "ArutairuSharedPrefs";
    private MaterialTextView mText, mState;
    private final LessonsStorage lessonsStorage = new LessonsStorage();
    private int state = 0;
    private boolean mistake;
    private boolean completed = true;
    private int max, lesson;
    String[] mEnglish, mRomaji, mJpn;
    private String mAnswerText;
    LessonsCompleted lessonsCompleted;
    private SharedPreferences sharedPreferences;
    private AdRequest adRequest;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        sharedPreferences = getSharedPreferences(ARUTAIRU_SHARED_PREFS, MODE_PRIVATE);

        if (!sharedPreferences.getBoolean("POLARIS", false)){
            setContentView(R.layout.activity_exercise);
            AdView mAdView = findViewById(R.id.adViewExercise);
            adRequest = new AdRequest.Builder()
                    .addKeyword(getString(R.string.japanKWords))
                    .addKeyword("nihongo")
                    .addKeyword("tokyo")
                    .addKeyword("manga")
                    .addKeyword("anime")
                    .addKeyword(getString(R.string.gameKWord))
                    .addKeyword(getString(R.string.languageKWord))
                    .addKeyword(getString(R.string.learnKWord))
                    .addKeyword(getString(R.string.travelKWord)).build();
            InterstitialAd.load(this,"ca-app-pub-9369103706924521/2427690661", adRequest, new InterstitialAdLoadCallback() {
                @Override
                public void onAdLoaded(@NonNull InterstitialAd interstitialAd) {
                    // The mInterstitialAd reference will be null until
                    // an ad is loaded.
                    mInterstitialAd = interstitialAd;
                    mInterstitialAd.setFullScreenContentCallback(new FullScreenContentCallback(){
                        @Override
                        public void onAdDismissedFullScreenContent() {
                            // Called when fullscreen content is dismissed.
                            Log.d("TAG", "The ad was dismissed.");
                        }

                        @Override
                        public void onAdFailedToShowFullScreenContent(@NonNull AdError adError) {
                            // Called when fullscreen content failed to show.
                            Log.d("TAG", "The ad failed to show.");
                        }

                        @Override
                        public void onAdShowedFullScreenContent() {
                            // Called when fullscreen content is shown.
                            // Make sure to set your reference to null so you don't
                            // show it a second time.
                            mInterstitialAd = null;
                            Log.d("TAG", "The ad was shown.");
                        }
                    });
                }

                @Override
                public void onAdFailedToLoad(@NonNull LoadAdError loadAdError) {
                    // Handle the error
                    mInterstitialAd = null;
                }
            });
            mAdView.loadAd(adRequest);
        }else{
            setContentView(R.layout.activity_exercise_no_ads);
        }

        lessonsCompleted = (LessonsCompleted) getIntent().getSerializableExtra("COMPLETED");

        if (!getIntent().getBooleanExtra("RETRIEVE", false)){
            lesson = getIntent().getIntExtra("LESSON", Integer.MAX_VALUE);
            mEnglish = getResources().getStringArray(lessonsStorage.getSrcRes(lesson));
            mJpn = getResources().getStringArray(lessonsStorage.getJpRes(lesson));
            //mRomaji = getResources().getStringArray(lessonsStorage.getRmRes(lesson));
            max = getIntent().getIntExtra("MAX", Integer.MAX_VALUE);
        }else{
            lesson = getIntent().getIntExtra("CHAPTER", Integer.MAX_VALUE);
            selectedItemList = (SelectedItemList) getIntent().getSerializableExtra("LESSON");
            assert selectedItemList != null;
            mEnglish = selectedItemList.getmFrench().toArray(new String[0]);
            mJpn = selectedItemList.getmJP().toArray(new String[0]);
            //mRomaji = selectedItemList.getmRomaji().toArray(new String[0]);
        }

        max = getIntent().getIntExtra("MAX", Integer.MAX_VALUE);

        mcheck = findViewById(R.id.checkExercise);

        mAnswer = findViewById(R.id.Answer);
        mSubmit = findViewById(R.id.submitBtn);
        mText = findViewById(R.id.txtArutairu);
        mState = findViewById(R.id.state);
        mAnswer.setOnKeyListener((v, keyCode, event) -> {
            if ((event.getAction() == KeyEvent.ACTION_DOWN) &&
                    (event.getKeyCode() == KeyEvent.KEYCODE_ENTER)) {
                // Perform action on key press
                check();
                return true;
            }
            return false;
        });
        mSubmit.setOnClickListener(v -> check());

        mAnswer.setTextLocale(new Locale("jp"));

        refresh();
    }

    private void check(){

        char[] japChar = mJpn[state].trim().toCharArray();
        char[] frChar = Objects.requireNonNull(mAnswer.getText()).toString().trim().toCharArray();

        if(getIntent().getBooleanExtra("SAVE", false)){
            if(Objects.requireNonNull(mAnswer.getText()).toString().trim().equalsIgnoreCase(mJpn[state].trim())){

                if (completed && !lessonsCompleted.isCompleted(lesson, selectedItemList.getCorrespondingIndex(state))){
                    lessonsCompleted.addCompleted(lesson, selectedItemList.getCorrespondingIndex(state));
                }
                mSubmit.setClickable(false);
                mSubmit.setBackgroundColor(getResources().getColor(R.color.green));
                new Handler().postDelayed(() -> {
                    state++;
                    if (state != max){
                        completed = true;
                        refresh();
                        mAnswer.setText("");
                    }else{
                        congrats();
                    }
                    mSubmit.setBackgroundColor(getResources().getColor(R.color.grey));
                    mSubmit.setClickable(true);
                },1000);
            }else{
                completed = false;
                showDialog();

            }
        }else{
            if(Objects.requireNonNull(mAnswer.getText()).toString().trim().equalsIgnoreCase(mJpn[state].trim())){

                //if (completed && !lessonsCompleted.isCompleted(selectedItemList.getCorrespondingLesson()+1, selectedItemList.getCorrespondingIndex(state))){
                //lessonsCompleted.addCompleted(selectedItemList.getCorrespondingLesson()+1, selectedItemList.getCorrespondingIndex(state));
                //}

                mSubmit.setBackgroundColor(getResources().getColor(R.color.green));
                mSubmit.setClickable(false);
                new Handler().postDelayed(() -> {
                    state++;
                    if (state != max){
                        completed = true;
                        refresh();
                        mAnswer.setText("");
                    }else{
                        congrats();
                    }
                    mSubmit.setBackgroundColor(getResources().getColor(R.color.grey));
                    mSubmit.setClickable(true);
                },1000);
            }else{
                completed = false;
                showDialog();

            }

        }
    }

    private void congrats() {

        //before inflating the custom alert dialog layout, we will get the current activity viewgroup
        ViewGroup viewGroup = findViewById(android.R.id.content);

        //then we will inflate the custom alert dialog xml that we created
        View dialogView = LayoutInflater.from(this).inflate(R.layout.congrats_dialog, viewGroup, false);

        //Now we need an AlertDialog.Builder object
        AlertDialog.Builder builder = new AlertDialog.Builder(this);

        //setting the view of the builder to our custom view that we already inflated
        builder.setView(dialogView);
        builder.setCancelable(false);


        builder.setPositiveButton(R.string.backlessons, (dialogInterface, i) -> {
            if (!sharedPreferences.getBoolean("POLARIS", false)){
                if (mInterstitialAd != null) {
                    SharedPreferences sharedPreferences = getSharedPreferences(ARUTAIRU_SHARED_PREFS, MODE_PRIVATE);
                    sharedPreferences.edit().putBoolean(Integer.toString(lesson), completed).apply();
                    Intent intent = new Intent(getApplicationContext(), HomeActivity.class);
                    intent.putExtra("COMPLETED", lessonsCompleted);
                    startActivity(intent);
                    mInterstitialAd.show(Exercise.this);
                    finish();
                } else {
                    Log.d("TAG", "The interstitial wasn't loaded yet.");
                    SharedPreferences sharedPreferences = getSharedPreferences(ARUTAIRU_SHARED_PREFS, MODE_PRIVATE);
                    sharedPreferences.edit().putBoolean(Integer.toString(lesson), completed).apply();
                    Intent intent = new Intent(getApplicationContext(), HomeActivity.class);
                    intent.putExtra("COMPLETED", lessonsCompleted);
                    startActivity(intent);
                    finish();
                }
            }else{
                SharedPreferences sharedPreferences = getSharedPreferences(ARUTAIRU_SHARED_PREFS, MODE_PRIVATE);
                sharedPreferences.edit().putBoolean(Integer.toString(lesson), completed).apply();
                Intent intent = new Intent(getApplicationContext(), HomeActivity.class);
                intent.putExtra("COMPLETED", lessonsCompleted);
                startActivity(intent);
                finish();
            }
        });

        if (getIntent().getBooleanExtra("PRACTICE", false)){
            builder.setNegativeButton(R.string.refaire, (dialog, which) -> {
                SelectedItemList shuffledList = new SelectedItemList();
                Random random = new Random();
                int randomNumber;

                int size = selectedItemList.getmFrench().size();

                for (int i = 0; i != size; i++){
                    randomNumber = random.nextInt(selectedItemList.getmFrench().size());
                    shuffledList.addJp(selectedItemList.getmJP().elementAt(randomNumber));
                    shuffledList.addRomaji(selectedItemList.getmRomaji().elementAt(randomNumber));
                    shuffledList.addFrench(selectedItemList.getmFrench().elementAt(randomNumber));
                    shuffledList.addCorrespondingIndex(selectedItemList.getCorrespondingIndex(randomNumber));


                    selectedItemList.getmFrench().remove(randomNumber);
                    selectedItemList.getmJP().remove(randomNumber);
                    selectedItemList.getmRomaji().remove(randomNumber);
                    selectedItemList.removeCorrespondingIndex(randomNumber);
                    //randomNumber = selectedItemList.getSelected().elementAt(random.nextInt(indexes.size()));
                    //selector.addJp(tempJP[randomNumber]);
                    //if (lessonsStorage.haveRomaji(state)) {
                    //    selector.addRomaji(tempRomaji[randomNumber]);
                    //}
                    //selector.addFrench(tempFr[randomNumber]);
                    //selector.addCorrespondingIndex(randomNumber);
                    //indexes.removeElement(randomNumber);
                }
                selectedItemList = shuffledList;
                mEnglish = selectedItemList.getmFrench().toArray(new String[0]);
                mJpn = selectedItemList.getmJP().toArray(new String[0]);
                state = 0;
                mAnswer.setText("");
                refresh();
            });
        }

        //finally creating the alert dialog and displaying it
        AlertDialog alertDialog = builder.create();
        alertDialog.show();

    }

    private void exit() {
        //before inflating the custom alert dialog layout, we will get the current activity viewgroup
        ViewGroup viewGroup = findViewById(android.R.id.content);

        //then we will inflate the custom alert dialog xml that we created

        //Now we need an AlertDialog.Builder object
        android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(this);

        //setting the view of the builder to our custom view that we already inflated


        builder.setPositiveButton(R.string.yes, (dialogInterface, i) -> {
            if (sharedPreferences.getBoolean("POLARIS", false)){
                if (mInterstitialAd != null) {
                    Intent intent = new Intent(getApplicationContext(), HomeActivity.class);
                    intent.putExtra("COMPLETED", lessonsCompleted);
                    startActivity(intent);
                    mInterstitialAd.show(Exercise.this);
                }else{
                    Intent intent = new Intent(getApplicationContext(), HomeActivity.class);
                    intent.putExtra("COMPLETED", lessonsCompleted);
                    startActivity(intent);
                }
                finish();
            }else{
                Intent intent = new Intent(getApplicationContext(), HomeActivity.class);
                intent.putExtra("COMPLETED", lessonsCompleted);
                startActivity(intent);
                finish();
            }
        });

        builder.setTitle(R.string.exitlesson);

        builder.setNegativeButton(R.string.no, (dialog, which) -> {

        });

        builder.setOnDismissListener(dialogInterface -> {
            //mSpam = 0;
        });

        //finally creating the alert dialog and displaying it
        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }

    @Override
    public void onBackPressed() {
        exit();

    }

    private void refresh() {
        if(!getIntent().getBooleanExtra("RETRIEVE", false)){
            if (lessonsCompleted.isCompleted(lesson, state)){
                mcheck.setAlpha(1f);
            }else{
                mcheck.setAlpha(0f);
            }
        }else{

            if(getIntent().getBooleanExtra("CUSTOM", false)){
                if(lessonsCompleted.isCompleted(selectedItemList.getCorrespondingLesson()+1, selectedItemList.getCorrespondingIndex(state))){
                    mcheck.setAlpha(1f);
                }else{
                    mcheck.setAlpha(0f);
                }
            }else{
                mcheck.setAlpha(0f);
            }
        }
        mText.setText(mEnglish[state]);
        String s = state+1+"/"+max;
        mState.setText(s);
    }

    private void showDialog() {

        //Now we need an AlertDialog.Builder object
        AlertDialog.Builder builder = new AlertDialog.Builder(this);

        builder.setTitle(mJpn[state]);


        builder.setPositiveButton("OK !", (dialog, which) -> {

        });

        builder.setOnDismissListener(dialogInterface -> {
            //mSpam = 0;
        });

        //finally creating the alert dialog and displaying it
        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }
}
