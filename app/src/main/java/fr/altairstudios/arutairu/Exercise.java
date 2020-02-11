package fr.altairstudios.arutairu;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
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
    private MaterialTextView mText, mState;
    private LessonsStorage lessonsStorage = new LessonsStorage();
    private int state = 0;
    private int max;
    String[] mEnglish, mRomaji, mJpn;
    private String mAnswerText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_exercise);

        max = getResources().getStringArray(lessonsStorage.getJpRes(LessonsStorage.NUMBERS)).length;

        mEnglish = getResources().getStringArray(lessonsStorage.getSrcRes(LessonsStorage.NUMBERS));
        mJpn = getResources().getStringArray(lessonsStorage.getJpRes(LessonsStorage.NUMBERS));

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
                            }else{
                                Intent intent = new Intent(getApplicationContext(), ListActivity.class);
                            }
                            mSubmit.setBackgroundColor(getResources().getColor(R.color.grey));
                        }
                    },1000);
                }else{
                    showDialog();

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
