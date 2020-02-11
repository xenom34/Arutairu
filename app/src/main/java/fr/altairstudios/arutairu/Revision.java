package fr.altairstudios.arutairu;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.media.AudioAttributes;
import android.os.Bundle;
import android.speech.tts.TextToSpeech;
import android.speech.tts.Voice;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.util.Locale;

public class Revision extends AppCompatActivity {
    private com.google.android.material.floatingactionbutton.FloatingActionButton mSound;
    TextToSpeech t1;
    private int state = 0;
    private int max;
    String[] mEnglish, mRomaji, mJpn;
    private Button mNext;
    private TextView mShowJpn, mShowEnglish, mShowRomaji, mCount;
    private LessonsStorage lessonsStorage = new LessonsStorage();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_revision);

        max = getIntent().getIntExtra("MAX", Integer.MAX_VALUE);

        mSound = findViewById(R.id.sound);
        mEnglish = getResources().getStringArray(lessonsStorage.getSrcRes(LessonsStorage.NUMBERS));
        mJpn = getResources().getStringArray(lessonsStorage.getJpRes(LessonsStorage.NUMBERS));
        mRomaji = getResources().getStringArray(lessonsStorage.getRmRes(LessonsStorage.NUMBERS));
        mNext = findViewById(R.id.next);
        mShowRomaji = findViewById(R.id.romaji);
        mShowEnglish = findViewById(R.id.english);
        mShowJpn = findViewById(R.id.jpn);
        mCount = findViewById(R.id.count);

        mNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                state++;
                if (state != max){
                    refresh();
                }else{
                    Intent intent = new Intent(getApplicationContext(), Exercise.class);
                    startActivity(intent);
                    intent.putExtra("MAX", max);
                    intent.putExtra("LESSON", 1);
                    finish();
                }
            }
        });

        t1=new TextToSpeech(getApplicationContext(), new TextToSpeech.OnInitListener() {
            @Override
            public void onInit(int status) {
                if(status != TextToSpeech.ERROR) {
                    t1.setLanguage(Locale.JAPAN);
                }
            }
        });

        mSound.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                t1.speak(mShowJpn.getText(), TextToSpeech.QUEUE_FLUSH, null, "1");
            }
        });

        refresh();

    }

    private void refresh() {
        mShowJpn.setText(mJpn[state]);
        mShowEnglish.setText(mEnglish[state]);
        mShowRomaji.setText(mRomaji[state]);
        mCount.setText(String.valueOf(state+1));
    }
}
