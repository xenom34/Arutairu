package fr.altairstudios.arutairu;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

public class ListActivity extends AppCompatActivity {

    private com.google.android.material.card.MaterialCardView mStartNumbers, mStartJobs, mStartBody, mStartPeople;
    private LessonsStorage lessonsStorage = new LessonsStorage();
    private boolean firstExec;
    public static final String ARUTAIRU_SHARED_PREFS = "ArutairuSharedPrefs";
    public static final String FIRST_EXEC = "first";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list);

        SharedPreferences sharedPreferences = getSharedPreferences(ARUTAIRU_SHARED_PREFS, MODE_PRIVATE);
        sharedPreferences.getBoolean(FIRST_EXEC, true);

        mStartNumbers = findViewById(R.id.btnNumbers);
        mStartBody = findViewById(R.id.btnBody);
        mStartJobs = findViewById(R.id.btnJobs);
        mStartPeople = findViewById(R.id.btnPeople);

        mStartNumbers.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), Revision.class);
                intent.putExtra("LESSON", 1);
                intent.putExtra("MAX", getResources().getStringArray(lessonsStorage.getJpRes(LessonsStorage.NUMBERS)).length);
                startActivity(intent);
            }
        });

        mStartPeople.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), Revision.class);
                intent.putExtra("LESSON", 2);
                intent.putExtra("MAX", getResources().getStringArray(lessonsStorage.getJpRes(LessonsStorage.PEOPLE)).length);
                startActivity(intent);
            }
        });

        mStartJobs.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), Revision.class);
                intent.putExtra("LESSON", 3);
                intent.putExtra("MAX", getResources().getStringArray(lessonsStorage.getJpRes(LessonsStorage.JOBS)).length);
                startActivity(intent);
            }
        });

        mStartBody.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), Revision.class);
                intent.putExtra("LESSON", 4);
                intent.putExtra("MAX", getResources().getStringArray(lessonsStorage.getJpRes(LessonsStorage.BODY)).length);
                startActivity(intent);
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        
        showDialog();
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


        builder.setPositiveButton("Lancez-vous !", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                SharedPreferences sharedPreferences = getSharedPreferences(ARUTAIRU_SHARED_PREFS, MODE_PRIVATE);
                sharedPreferences.edit().putBoolean(FIRST_EXEC, false).apply();
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
