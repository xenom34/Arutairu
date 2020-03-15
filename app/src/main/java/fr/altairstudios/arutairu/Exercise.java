package fr.altairstudios.arutairu;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textview.MaterialTextView;

public class Exercise extends AppCompatActivity {
    private TextInputEditText mAnswer;
    private MaterialButton mSubmit;
    public static final String ARUTAIRU_SHARED_PREFS = "ArutairuSharedPrefs";
    private MaterialTextView mText, mState;
    private LessonsStorage lessonsStorage = new LessonsStorage();
    private int state = 0;
    private boolean completed = true;
    private int max, lesson;
    String[] mEnglish, mRomaji, mJpn;
    private String mAnswerText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_exercise);

        lesson = getIntent().getIntExtra("LESSON", Integer.MAX_VALUE);
        max = getResources().getStringArray(lessonsStorage.getJpRes(lesson)).length;

        mEnglish = getResources().getStringArray(lessonsStorage.getSrcRes(lesson));
        mJpn = getResources().getStringArray(lessonsStorage.getJpRes(lesson));

        mAnswer = findViewById(R.id.Answer);
        mSubmit = findViewById(R.id.submitBtn);
        mText = findViewById(R.id.txtArutairu);
        mState = findViewById(R.id.state);
        mSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(mAnswer.getText().toString().equals(mJpn[state])){
                    mSubmit.setBackgroundColor(getResources().getColor(R.color.green));
                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            state++;
                            if (state != max){
                                refresh();
                                mAnswer.setText("");
                            }else{
                                SharedPreferences sharedPreferences = getSharedPreferences(ARUTAIRU_SHARED_PREFS, MODE_PRIVATE);
                                sharedPreferences.edit().putBoolean(Integer.toString(lesson), completed).apply();
                                Intent intent = new Intent(getApplicationContext(), ListActivity.class);
                                startActivity(intent);
                                finish();
                            }
                            mSubmit.setBackgroundColor(getResources().getColor(R.color.grey));
                        }
                    },1000);
                }else{
                    showDialog();
                    completed = false;

                }
            }
        });

        refresh();
    }

    private void refresh() {
        mText.setText(mEnglish[state]);
        String s = state+1+"/"+max;
        mState.setText(s);
    }

    private void showDialog() {

        //Now we need an AlertDialog.Builder object
        AlertDialog.Builder builder = new AlertDialog.Builder(this);

        builder.setTitle(mJpn[state]);


        builder.setPositiveButton("OK !", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

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
}
