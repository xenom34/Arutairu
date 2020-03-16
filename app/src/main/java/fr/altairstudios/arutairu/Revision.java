package fr.altairstudios.arutairu;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.AudioAttributes;
import android.os.Bundle;
import android.speech.tts.TextToSpeech;
import android.speech.tts.Voice;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import java.util.Locale;

public class Revision extends AppCompatActivity {
    private com.google.android.material.floatingactionbutton.FloatingActionButton mSound;
    TextToSpeech t1;
    private int state = 0;
    private int max, lesson;
    String[] mEnglish, mRomaji, mJpn;
    private Button mNext;
    LessonsCompleted lessonsCompleted;
    private TextView mShowJpn, mShowEnglish, mShowRomaji, mCount;
    private LessonsStorage lessonsStorage = new LessonsStorage();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_revision);

        lessonsCompleted = (LessonsCompleted) getIntent().getSerializableExtra("COMPLETED");

        max = getIntent().getIntExtra("MAX", Integer.MAX_VALUE);
        lesson = getIntent().getIntExtra("LESSON", Integer.MAX_VALUE);

        mSound = findViewById(R.id.sound);
        mEnglish = getResources().getStringArray(lessonsStorage.getSrcRes(lesson));
        mJpn = getResources().getStringArray(lessonsStorage.getJpRes(lesson));
        mRomaji = getResources().getStringArray(lessonsStorage.getRmRes(lesson));
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
                    intent.putExtra("MAX", max);
                    intent.putExtra("LESSON", lesson);
                    intent.putExtra("COMPLETED", lessonsCompleted);
                    startActivity(intent);
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

    private void showDialog(){
        //before inflating the custom alert dialog layout, we will get the current activity viewgroup
        ViewGroup viewGroup = findViewById(android.R.id.content);

        //Now we need an AlertDialog.Builder object
        android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(this);

        //setting the view of the builder to our custom view that we already inflated

        builder.setPositiveButton("Oui", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                Intent intent = new Intent(getApplicationContext(), ListActivity.class);
                intent.putExtra("COMPLETED", lessonsCompleted);
                startActivity(intent);
                finish();
            }
        });

        builder.setNegativeButton("Non", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        });

        builder.setTitle("Quitter la leçons ?");

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
    public void onBackPressed() {
        showDialog();
    }

    private void refresh() {
        mShowJpn.setText(mJpn[state]);
        mShowEnglish.setText(mEnglish[state]);
        mShowRomaji.setText(mRomaji[state]);
        String s = state+1+"/"+max;
        mCount.setText(s);
    }
}
