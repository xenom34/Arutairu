package fr.altairstudios.arutairu;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.AudioManager;
import android.os.Bundle;
import android.speech.tts.TextToSpeech;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.ads.AdError;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.FullScreenContentCallback;
import com.google.android.gms.ads.LoadAdError;
import com.google.android.gms.ads.interstitial.InterstitialAd;
import com.google.android.gms.ads.interstitial.InterstitialAdLoadCallback;
import com.google.android.material.snackbar.Snackbar;

import java.util.Locale;
import java.util.Objects;
import java.util.Random;

public class Revision extends AppCompatActivity {
    private com.google.android.material.floatingactionbutton.FloatingActionButton mSound;
    TextToSpeech t1, t2;
    private Boolean mFirst, mNextAutoState, mIsPlayingAudio, mShowError, isTtsActive;
    private final Object o = new Object();
    private InterstitialAd mInterstitialAd;
    private int state = 0;
    private SharedPreferences sharedPreferences;
    private int max, lesson;
    //private AdView mAdView;
    String[] mEnglish, mRomaji, mJpn, mTts;
    private Button mNext, mBack, mStop;
    LessonsCompleted lessonsCompleted;
    SelectedItemList selectedItemList;
    private TextView mShowJpn, mShowEnglish, mShowRomaji, mCount;
    private final LessonsStorage lessonsStorage = new LessonsStorage();
    private boolean stopped = true;
    private Thread execute, fExecute;
    private AudioManager am;
    private int focusStatus;
    private AdView mAdView;
    private AdRequest adRequest;
    boolean polaris = false;
    public static final String ARUTAIRU_SHARED_PREFS = "ArutairuSharedPrefs";

    private float convertDpToPx(Context context, float dp) {
        return dp * context.getResources().getDisplayMetrics().density;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        sharedPreferences = getSharedPreferences(ARUTAIRU_SHARED_PREFS, MODE_PRIVATE);
        polaris = sharedPreferences.getBoolean("POLARIS", false);

        if (!sharedPreferences.getBoolean("POLARIS", false)){
            setContentView(R.layout.activity_revision);
            mAdView = findViewById(R.id.adViewRevision);
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
            mAdView.loadAd(adRequest);

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
                        public void onAdFailedToShowFullScreenContent(AdError adError) {
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
        }else{
            setContentView(R.layout.activity_revision_no_ads);
        }
        mNextAutoState = false;
        mIsPlayingAudio = false;
        isTtsActive = false;

        am = (AudioManager) getSystemService(Context.AUDIO_SERVICE);

        mFirst  = getIntent().getBooleanExtra("FIRST", false);

        //mAdView = findViewById(R.id.adViewRevision);
        //AdRequest adRequest = new AdRequest.Builder().build();
        //mAdView.loadAd(adRequest);

        lessonsCompleted = (LessonsCompleted) getIntent().getSerializableExtra("COMPLETED");

        mSound = findViewById(R.id.sound);
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
        mBack = findViewById(R.id.back);
        mShowRomaji = findViewById(R.id.romaji);
        mShowEnglish = findViewById(R.id.english);
        mShowJpn = findViewById(R.id.jpn);
        mCount = findViewById(R.id.count);
        mStop = findViewById(R.id.stop);
        mStop.setVisibility(View.INVISIBLE);
        mStop.setClickable(false);

        mNext.setOnClickListener(v -> up());

        mNext.setOnLongClickListener(v -> {
            state = max-3;
            up();
            return true;
        });

        mStop.setOnClickListener(v -> stopAudio());



        mBack.setOnLongClickListener(v -> {
            state = -1;
            up();
            return true;
        });

        mBack.setOnClickListener(v -> {
            state--;
            if (state != -1){
                refresh();
            }else{
                state++;
            }
        });
        
        t1=new TextToSpeech(getApplicationContext(), status -> {
            mShowError = t1.isLanguageAvailable(Locale.JAPANESE) == TextToSpeech.LANG_NOT_SUPPORTED && !sharedPreferences.getBoolean("ERROR_SHOWN", false);

            if(status != TextToSpeech.ERROR) {
                t1.setLanguage(Locale.JAPANESE);
                t1.setSpeechRate(0.9f);
            }
        });

        t2 = new TextToSpeech(getApplicationContext(), status -> {
            if(status != TextToSpeech.ERROR) {
                t2.setLanguage(new Locale(Objects.requireNonNull(sharedPreferences.getString("LOCALE", Locale.getDefault().getLanguage()))));
                t2.setSpeechRate(1f);
            }
        });

        mSound.setOnClickListener(v -> {
                //Log.d("TEST", String.valueOf(t1.isLanguageAvailable(Locale.JAPAN)));
            new Thread(() -> {
                if (!isTtsActive) {
                    requestAudioFocusForMyApp();
                    t1.speak(mTts[state], TextToSpeech.QUEUE_FLUSH, null, "1");
                    do {
                        isTtsActive = t1.isSpeaking();
                    } while (isTtsActive);
                    releaseAudioFocusForMyApp();
                    Snackbar snackbar = Snackbar.make(findViewById(R.id.revisionactivity), mShowRomaji.getText(), Snackbar.LENGTH_SHORT);
                    View view = snackbar.getView();
                    FrameLayout.LayoutParams params = (FrameLayout.LayoutParams) view.getLayoutParams();
                    params.gravity = Gravity.TOP;
                    if (!polaris) {
                        params.topMargin = (int) convertDpToPx(getApplicationContext(), 96);
                    } else {
                        params.topMargin = (int) convertDpToPx(getApplicationContext(), 48);
                    }

                    view.setLayoutParams(params);
                    snackbar.show();
                }
            }).start();
        });

        mSound.setOnLongClickListener(v -> {
            mIsPlayingAudio = true;
            mNext.setClickable(false);
            mBack.setClickable(false);
            mNext.setVisibility(View.INVISIBLE);
            mBack.setVisibility(View.INVISIBLE);
            mStop.setClickable(true);
            mStop.setVisibility(View.VISIBLE);
            mSound.setClickable(false);
            mSound.setVisibility(View.INVISIBLE);
            findViewById(R.id.play).setVisibility(View.INVISIBLE);
            stopped = false;

            fExecute = new Thread(() -> {
                if (requestAudioFocusForMyApp()) {
                    while (!stopped) {
                        synchronized (o) {
                            try {
                                play();
                                o.wait();
                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            }
                        }

                    }
                }
            });

            fExecute.start();
            return true;
        });

        if (!getIntent().getBooleanExtra("ROMAJI", false)){
            mSound.setClickable(false);
            mSound.setVisibility(View.INVISIBLE);
        }

        refresh();

    }

    private void stopAudio() {
        if (!stopped) {
            mNext.setVisibility(View.VISIBLE);
            mBack.setVisibility(View.VISIBLE);
            mNext.setClickable(true);
            mNext.setClickable(true);
            mStop.setClickable(false);
            stopped = true;
            mStop.setVisibility(View.INVISIBLE);
            releaseAudioFocusForMyApp();
            while (!execute.isInterrupted()) {
                execute.interrupt();
            }
            while (!fExecute.isInterrupted()) {
                fExecute.interrupt();
            }
            mSound.setClickable(true);
            mSound.setVisibility(View.VISIBLE);
            findViewById(R.id.play).setVisibility(View.VISIBLE);
        }
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

    void releaseAudioFocusForMyApp() {
        AudioManager am = (AudioManager) getSystemService(Context.AUDIO_SERVICE);
        am.abandonAudioFocus(null);
    }

    private void play() {
        execute = new Thread(() -> {
            synchronized (o) {
                try {
                    requestAudioFocusForMyApp();
                    boolean isTtsActive = true;
                    t2.speak(mShowEnglish.getText(), TextToSpeech.QUEUE_FLUSH, null, "1");
                    do {
                        isTtsActive = t2.isSpeaking();
                    } while (isTtsActive);
                    Thread.sleep(1500);
                    t1.speak(mTts[state], TextToSpeech.QUEUE_FLUSH, null, "1");
                    do {
                        isTtsActive = t1.isSpeaking();
                    } while (isTtsActive);
                    Thread.sleep(1500);
                    t1.speak(mTts[state], TextToSpeech.QUEUE_FLUSH, null, "1");
                    do {
                        isTtsActive = t1.isSpeaking();
                    } while (isTtsActive);
                    Thread.sleep(1500);
                    mNextAutoState = true;
                    runOnUiThread(() -> {
                        if (state + 1 != max) {
                            up();
                        } else {
                            state = 0;
                            refresh();
                        }
                    });
                    Thread.sleep(500);
                    o.notifyAll();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        });

        execute.start();
    }

    @Override
    protected void onPause() {
        super.onPause();

        if (mIsPlayingAudio){
            stopAudio();
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
                if (!sharedPreferences.getBoolean("POLARIS", false)){
                    if(mInterstitialAd != null){
                        mInterstitialAd.show(Revision.this);
                    }else{
                        Intent intent = new Intent(getApplicationContext(), HomeActivity.class);
                        startActivity(intent);
                        finish();
                    }
                }else{
                    Intent intent = new Intent(getApplicationContext(), HomeActivity.class);
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

        if (!getIntent().getBooleanExtra("ROMAJI", true)){
            mSound.setVisibility(View.INVISIBLE);
            mStop.setClickable(false);
            findViewById(R.id.play).setVisibility(View.INVISIBLE);
        }

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
            if (focusStatus == AudioManager.AUDIOFOCUS_REQUEST_GRANTED) {
                releaseAudioFocusForMyApp();
            }
            if(execute!=null){
                //execute.interrupt();
                stopAudio();
            }
            stopAudio();
            if (!sharedPreferences.getBoolean("POLARIS", false)){
                if (mInterstitialAd != null) {
                    mInterstitialAd.show(Revision.this);
                }else{
                    Intent intent = new Intent(getApplicationContext(), HomeActivity.class);
                    startActivity(intent);
                    finish();
                }
            }else{
                Intent intent = new Intent(getApplicationContext(), HomeActivity.class);
                startActivity(intent);
                finish();
            }
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

    @Override
    public void onBackPressed() {
        showDialog();
    }

    private void refresh() {
        mShowJpn.setText(mJpn[state]);
        mShowEnglish.setText(mEnglish[state]);
        if (getIntent().getBooleanExtra("ROMAJI", false)){
            mShowRomaji.setText(mRomaji[state]);
        }
        String s = state+1+"/"+max;
        mCount.setText(s);
    }
}
