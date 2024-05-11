package fr.altairstudios.arutairu;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.media.AudioManager;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.activity.OnBackPressedCallback;
import androidx.activity.OnBackPressedDispatcher;
import androidx.annotation.ColorInt;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import com.google.android.material.card.MaterialCardView;

import java.util.Objects;
import java.util.Random;

public class FlashCardsActivity extends AppCompatActivity {
    private Boolean mFirst, mNextAutoState, mIsPlayingAudio, mShowError, isTtsActive, mAnswer;
    private OnBackPressedDispatcher onBackPressedDispatcher;

    private final Object o = new Object();
    private Animation fadeAltair2, fadeAltair3;
    private int state = 0;
    private SharedPreferences sharedPreferences;
    private int max, lesson;
    //private AdView mAdView;
    String[] mEnglish, mRomaji, mJpn, mTts;
    private Button mBack;
    private ImageButton mNext;
    private LessonsCompleted lessonsCompleted;
    SelectedItemList selectedItemList;
    private TextView mShowJpn, mShowEnglish, mShowRomaji, mCount;
    private final LessonsStorage lessonsStorage = new LessonsStorage();
    private boolean stopped = true;
    private Thread execute, fExecute;
    private AudioManager am;
    private View mBackground;
    private int focusStatus;
    private MaterialCardView mCard;
    boolean polaris = false;
    public static final String ARUTAIRU_SHARED_PREFS = "ArutairuSharedPrefs";

    private float convertDpToPx(Context context, float dp) {
        return dp * context.getResources().getDisplayMetrics().density;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        sharedPreferences = getSharedPreferences(ARUTAIRU_SHARED_PREFS, MODE_PRIVATE);
        //polaris = sharedPreferences.getBoolean("POLARIS", false);
        polaris = true;
        mAnswer = false;
        fadeAltair2 = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.fadeout);
        fadeAltair3 = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.fadein);

        onBackPressedDispatcher = getOnBackPressedDispatcher();
        onBackPressedDispatcher.addCallback(this, new OnBackPressedCallback(true) {
            @Override
            public void handleOnBackPressed() {
                showDialog();
            }
        });

        setContentView(R.layout.activity_flash_cards);


        mNextAutoState = false;
        mIsPlayingAudio = false;
        isTtsActive = false;

        am = (AudioManager) getSystemService(Context.AUDIO_SERVICE);

        mFirst  = getIntent().getBooleanExtra("FIRST", false);

        //mAdView = findViewById(R.id.adViewRevision);
        //AdRequest adRequest = new AdRequest.Builder().build();
        //mAdView.loadAd(adRequest);

        lessonsCompleted = (LessonsCompleted) getIntent().getSerializableExtra("COMPLETED");

        if (!getIntent().getBooleanExtra("RETRIEVE", false)){
            max = getIntent().getIntExtra("MAX", Integer.MAX_VALUE);
            lesson = getIntent().getIntExtra("LESSON", Integer.MAX_VALUE);
            if(Objects.equals(getIntent().getStringExtra("LOCALE"), "fr")){
                mEnglish = getResources().getStringArray(lessonsStorage.getSrcRes(lesson));
            }else{
                mEnglish = getResources().getStringArray(lessonsStorage.getEnRes(lesson));
            }
            mTts = getResources().getStringArray(lessonsStorage.getJpRes(lesson));
            if(getIntent().getBooleanExtra("KANJI", false)){
                mJpn = getResources().getStringArray(lessonsStorage.getKjRes(lesson));
            }else if (getIntent().getBooleanExtra("WITH_ROMAJI", false)){
                mJpn = getResources().getStringArray(lessonsStorage.getRmRes(lesson));
            }else{
                mJpn = getResources().getStringArray(lessonsStorage.getJpRes(lesson));
            }
            if(getIntent().getBooleanExtra("ROMAJI", false)){
                mRomaji = getResources().getStringArray(lessonsStorage.getRmRes(lesson));
            }
        }else{
            selectedItemList = (SelectedItemList) getIntent().getSerializableExtra("LESSON");
            assert selectedItemList != null;
            mTts = selectedItemList.getmTts().toArray(new String[0]);
            mEnglish = selectedItemList.getmFrench().toArray(new String[0]);
            mJpn = selectedItemList.getmJP().toArray(new String[0]);
            mRomaji = selectedItemList.getmRomaji().toArray(new String[0]);
            max = mEnglish.length;
        }
        mNext = findViewById(R.id.next);

        mCard = findViewById(R.id.materialCardView);
        mBack = findViewById(R.id.back);
        mBackground = findViewById(R.id.background);
        mShowRomaji = findViewById(R.id.romaji);
        mShowEnglish = findViewById(R.id.english);
        mShowJpn = findViewById(R.id.jpn);
        mCount = findViewById(R.id.count);
        //mStop = findViewById(R.id.stop);
        //mStop.setVisibility(View.INVISIBLE);
        //mStop.setClickable(false);

        mNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mNext.setClickable(false);
                mBack.setClickable(false);
                mBackground.setBackgroundColor(getResources().getColor(R.color.green));
                lessonsCompleted.addCompleted(mJpn[state],true);
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        mBackground.setBackgroundColor(getResources().getColor(R.color.cardview_dark_background));
                        mNext.setClickable(true);
                        mBack.setClickable(true);
                        up();
                    }
                },500);
            }
        });

        mCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!mAnswer){
                    mShowJpn.startAnimation(fadeAltair2);
                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            mShowJpn.setVisibility(View.INVISIBLE);
                            mShowEnglish.startAnimation(fadeAltair3);
                            mShowEnglish.setVisibility(View.VISIBLE);
                        }
                    },200);
                }else{
                    mShowEnglish.startAnimation(fadeAltair2);
                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            mShowEnglish.setVisibility(View.INVISIBLE);
                            mShowJpn.startAnimation(fadeAltair3);
                            mShowJpn.setVisibility(View.VISIBLE);
                        }
                    },200);
                }
                mAnswer = !mAnswer;
            }
        });

        //mStop.setOnClickListener(v -> stopAudio());

        mBack.setOnClickListener(v -> {
            mNext.setClickable(false);
            mBack.setClickable(false);
            mBackground.setBackgroundColor(getResources().getColor(R.color.red));
            lessonsCompleted.addCompleted(mJpn[state],false);
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    mBackground.setBackgroundColor(getResources().getColor(R.color.cardview_dark_background));
                    mNext.setClickable(true);
                    mBack.setClickable(true);
                    up();
                }
            },500);
        });

        refresh();

    }



    private boolean requestAudioFocusForMyApp() {
        // Request audio focus for playback
        focusStatus = am.requestAudioFocus(null,
                // Use the music stream.
                AudioManager.STREAM_MUSIC,
                // Request permanent focus.
                AudioManager.AUDIOFOCUS_GAIN_TRANSIENT_MAY_DUCK);

        if (focusStatus == AudioManager.AUDIOFOCUS_REQUEST_GRANTED) {
            Log.d("AudioFocus", "Audio focus received");
            return true;
        } else {
            Log.d("AudioFocus", "Audio focus NOT received");
            return false;
        }
    }

    private void showError() {
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

    private void up() {
        state++;
        if (state != max){
            refresh();
        }else{
            if(getIntent().getBooleanExtra("REVISION", false)){
                Intent intent = new Intent(getApplicationContext(), HomeActivity.class);
                if (!sharedPreferences.getBoolean("POLARIS", false)){

                        intent.putExtra("COMPLETED", lessonsCompleted);
                        startActivity(intent);
                        finish();

                }else{
                    intent.putExtra("COMPLETED", lessonsCompleted);
                    startActivity(intent);
                    finish();
                }
            }else{
                Intent intent = new Intent(getApplicationContext(), Exercise.class);
                intent.putExtra("MAX", max);
                if(getIntent().getBooleanExtra("RETRIEVE", false)){
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
                    intent.putExtra("RETRIEVE", true);
                    intent.putExtra("LESSON", shuffledList);
                }else{
                    intent.putExtra("LESSON", lesson);
                    intent.putExtra("RETRIEVE", false);
                }
                intent.putExtra("PRACTICE", true);
                intent.putExtra("COMPLETED", lessonsCompleted);
                startActivity(intent);
                finish();
            }
        }
    }

    @Override
    protected void onStart() {
        super.onStart();

        mShowRomaji.setVisibility(View.INVISIBLE);
        mShowJpn.setVisibility(View.INVISIBLE);

        if(mFirst){
            showTuto(1);

        }
    }

    private void showTuto(final int message) {

        //before inflating the custom alert dialog layout, we will get the current activity viewgroup
        ViewGroup viewGroup = findViewById(android.R.id.content);
        View dialogView;

        //then we will inflate the custom alert dialog xml that we created
        switch (message){
            case 1:
                dialogView = LayoutInflater.from(this).inflate(R.layout.dialog_revision, viewGroup, false);
                break;
            case 2:
                dialogView = LayoutInflater.from(this).inflate(R.layout.play_words_revision_dialog, viewGroup, false);
                break;
            default:
                throw new IllegalStateException("Unexpected value: " + message);
        }

        //Now we need an AlertDialog.Builder object
        AlertDialog.Builder builder = new AlertDialog.Builder(this);

        //setting the view of the builder to our custom view that we already inflated
        builder.setView(dialogView);


        builder.setPositiveButton(R.string.understood, (dialogInterface, i) -> {
            if (message != 2){
                if (mShowError){
                    showError();
                }else{
                    showTuto(2);
                }
            }
        });

        builder.setOnDismissListener(dialogInterface -> {
            //mSpam = 0;
        });

        //finally creating the alert dialog and displaying it
        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }

    private void showDialog(){
        //before inflating the custom alert dialog layout, we will get the current activity viewgroup
        ViewGroup viewGroup = findViewById(android.R.id.content);

        //Now we need an AlertDialog.Builder object
        android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(this);

        //setting the view of the builder to our custom view that we already inflated

        builder.setPositiveButton(R.string.yes, (dialogInterface, i) -> {
            state = max-1;
            up();
        });

        builder.setNegativeButton(R.string.no, (dialog, which) -> {

        });

        builder.setTitle(R.string.exitlesson);

        builder.setOnDismissListener(dialogInterface -> {
            //mSpam = 0;
        });

        //finally creating the alert dialog and displaying it
        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }

    private void refresh() {
        mShowJpn.setVisibility(View.INVISIBLE);
        mShowEnglish.setVisibility(View.VISIBLE);
        mAnswer = true;
        if (state >= max)
            state = max-1;
        while (lessonsCompleted.isCompleted(mJpn[state]) && state >= max){
            state++;
        }
        mShowJpn.setText(mJpn[state]);
        mShowEnglish.setText(mEnglish[state]);
        if (getIntent().getBooleanExtra("ROMAJI", false)){
            mShowRomaji.setText(mRomaji[state]);
        }
        String s = state+1+"/"+max;
        mCount.setText(s);
    }
}
