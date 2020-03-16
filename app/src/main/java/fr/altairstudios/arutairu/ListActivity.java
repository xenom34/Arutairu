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
import android.widget.ImageView;
import android.widget.ProgressBar;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.List;
import java.util.Vector;

public class ListActivity extends AppCompatActivity {

    private Vector<com.google.android.material.card.MaterialCardView> cards = new Vector<>();
    private LessonsStorage lessonsStorage = new LessonsStorage();
    private boolean firstExec;
    private Vector<ImageView> checks = new Vector<>();
    private ProgressBar mProgressNumbers, mProgressJobs, mProgressBody, mProgressPeople, mProgressFamily, mProgressAnimals, mProgressPlants, mProgressCrop, mProgressFood, mProgressDrinks, mProgressSeasoning, mProgressWeek, mProgressWeather, mProgressDirections, mProgressMaterials, mProgressMeasures, mProgressSociety, mProgressHome, mProgressTools, mProgressStationery, mProgressClothes, mProgressTransport, mProgressLanguages, mProgressMedias, mProgressColor, mProgressOther, mProgressAbstract;
    public static final String ARUTAIRU_SHARED_PREFS = "ArutairuSharedPrefs";
    public static final String FIRST_EXEC = "first";
    static boolean waitingForData = false;
    SharedPreferences sharedPreferences;
    LessonsCompleted lessonsCompleted = new LessonsCompleted();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list);

        cards.add((com.google.android.material.card.MaterialCardView) findViewById(R.id.btnNumbers));
        cards.add((com.google.android.material.card.MaterialCardView) findViewById(R.id.btnBody));
        cards.add((com.google.android.material.card.MaterialCardView) findViewById(R.id.btnJobs));
        cards.add((com.google.android.material.card.MaterialCardView) findViewById(R.id.btnPeople));
        cards.add((com.google.android.material.card.MaterialCardView) findViewById(R.id.btnFamily));
        cards.add((com.google.android.material.card.MaterialCardView) findViewById(R.id.btnAnimals));
        cards.add((com.google.android.material.card.MaterialCardView) findViewById(R.id.btnPlants));
        cards.add((com.google.android.material.card.MaterialCardView) findViewById(R.id.btnCrops));
        cards.add((com.google.android.material.card.MaterialCardView) findViewById(R.id.btnFood));
        cards.add((com.google.android.material.card.MaterialCardView) findViewById(R.id.btnDrink));
        cards.add((com.google.android.material.card.MaterialCardView) findViewById(R.id.btnSeasoning));
        cards.add((com.google.android.material.card.MaterialCardView) findViewById(R.id.btnDays));
        cards.add((com.google.android.material.card.MaterialCardView) findViewById(R.id.btnWeather));
        cards.add((com.google.android.material.card.MaterialCardView) findViewById(R.id.btnDirections));
        cards.add((com.google.android.material.card.MaterialCardView) findViewById(R.id.btnMaterials));
        cards.add((com.google.android.material.card.MaterialCardView) findViewById(R.id.btnWeights));
        cards.add((com.google.android.material.card.MaterialCardView) findViewById(R.id.btnSociety));
        cards.add((com.google.android.material.card.MaterialCardView) findViewById(R.id.btnHome));
        cards.add((com.google.android.material.card.MaterialCardView) findViewById(R.id.btnTools));
        cards.add((com.google.android.material.card.MaterialCardView) findViewById(R.id.btnStationery));
        cards.add((com.google.android.material.card.MaterialCardView) findViewById(R.id.btnClothes));
        cards.add((com.google.android.material.card.MaterialCardView) findViewById(R.id.btnTransport));
        cards.add((com.google.android.material.card.MaterialCardView) findViewById(R.id.btnLanguage));
        cards.add((com.google.android.material.card.MaterialCardView) findViewById(R.id.btnMedia));
        cards.add((com.google.android.material.card.MaterialCardView) findViewById(R.id.btnColors));
        cards.add((com.google.android.material.card.MaterialCardView) findViewById(R.id.btnOthers));
        cards.add((com.google.android.material.card.MaterialCardView) findViewById(R.id.btnAbstract));

        mProgressNumbers = findViewById(R.id.numbersProgress);
        mProgressBody = findViewById(R.id.bodyProgress);
        mProgressJobs = findViewById(R.id.jobsProgress);
        mProgressPeople = findViewById(R.id.peopleProgress);
        mProgressFamily = findViewById(R.id.familyProgress);
        mProgressAnimals = findViewById(R.id.animalsProgress);
        mProgressPlants = findViewById(R.id.plantsProgress);
        mProgressCrop = findViewById(R.id.cropProgress);
        mProgressFood = findViewById(R.id.foodProgress);
        mProgressDrinks = findViewById(R.id.drinksProgress);
        mProgressSeasoning = findViewById(R.id.seasoningProgress);
        mProgressWeek = findViewById(R.id.weeksProgress);
        mProgressWeather = findViewById(R.id.weatherProgress);
        mProgressDirections = findViewById(R.id.directionsProgress);
        mProgressMaterials = findViewById(R.id.materialsProgress);
        mProgressMeasures = findViewById(R.id.measuresProgress);
        mProgressSociety = findViewById(R.id.societyProgress);
        mProgressHome = findViewById(R.id.homeProgress);
        mProgressTools = findViewById(R.id.toolsProgress);
        mProgressStationery = findViewById(R.id.stationeryProgress);
        mProgressClothes = findViewById(R.id.clothesProgress);
        mProgressTransport = findViewById(R.id.transportProgress);
        mProgressLanguages = findViewById(R.id.languageProgress);
        mProgressMedias = findViewById(R.id.mediasProgress);
        mProgressColor = findViewById(R.id.colorsProgress);
        mProgressOther = findViewById(R.id.othersProgress);
        mProgressAbstract = findViewById(R.id.abstractProgress);

        checks.add((ImageView) findViewById(R.id.checkNumbers));
        checks.add((ImageView) findViewById(R.id.checkPeople));
        checks.add((ImageView) findViewById(R.id.checkJobs));
        checks.add((ImageView) findViewById(R.id.checkBody));
        checks.add((ImageView) findViewById(R.id.checkFamily));
        checks.add((ImageView) findViewById(R.id.checkAnimals));
        checks.add((ImageView) findViewById(R.id.checkPlants));
        checks.add((ImageView) findViewById(R.id.checkCrops));
        checks.add((ImageView) findViewById(R.id.checkFood));
        checks.add((ImageView) findViewById(R.id.checkDrinks));
        checks.add((ImageView) findViewById(R.id.checkSeasoning));
        checks.add((ImageView) findViewById(R.id.checkWeek));
        checks.add((ImageView) findViewById(R.id.checkWeather));
        checks.add((ImageView) findViewById(R.id.checkDirections));
        checks.add((ImageView) findViewById(R.id.checkMaterials));
        checks.add((ImageView) findViewById(R.id.checkMeasures));
        checks.add((ImageView) findViewById(R.id.checkSociety));
        checks.add((ImageView) findViewById(R.id.checkHome));
        checks.add((ImageView) findViewById(R.id.checkTools));
        checks.add((ImageView) findViewById(R.id.checkStationery));
        checks.add((ImageView) findViewById(R.id.checkClothes));
        checks.add((ImageView) findViewById(R.id.checkTransport));
        checks.add((ImageView) findViewById(R.id.checkLanguage));
        checks.add((ImageView) findViewById(R.id.checkMedias));
        checks.add((ImageView) findViewById(R.id.checkColors));
        checks.add((ImageView) findViewById(R.id.checkOthers));
        checks.add((ImageView) findViewById(R.id.checkAbstract));


        /*mStartNumbers.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), Revision.class);
                intent.putExtra("LESSON", LessonsStorage.NUMBERS);
                intent.putExtra("MAX", getResources().getStringArray(lessonsStorage.getJpRes(LessonsStorage.NUMBERS)).length);
                intent.putExtra("COMPLETED", lessonsCompleted);
                waitingForData = true;
                startActivity(intent);
                finish();
            }
        });*/

        for (int i = 0; i < LessonsStorage.TOTAL; i++) {
            final int finalI = i;
            cards.elementAt(i).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(getApplicationContext(), Revision.class);
                    intent.putExtra("LESSON", finalI +1);
                    intent.putExtra("MAX", getResources().getStringArray(lessonsStorage.getJpRes(finalI+1)).length);
                    intent.putExtra("COMPLETED", lessonsCompleted);
                    waitingForData = true;
                    startActivity(intent);
                    finish();
                }
            });
        }

        for (ImageView imageView:checks) {
            imageView.setAlpha(0f);
        }
    }

    void refresh(){
        mProgressNumbers.setProgress((lessonsCompleted.howManyCompleted(LessonsStorage.NUMBERS)/getResources().getStringArray(lessonsStorage.getJpRes(LessonsStorage.NUMBERS)).length)*100);
        checks.elementAt(0).setAlpha(1f);
    }

    @Override
    protected void onStart() {
        super.onStart();

        if (waitingForData){
            lessonsCompleted = (LessonsCompleted) getIntent().getSerializableExtra("COMPLETED");
            refresh();
            waitingForData = false;
        }

        sharedPreferences = getSharedPreferences(ARUTAIRU_SHARED_PREFS, MODE_PRIVATE);

        firstExec = sharedPreferences.getBoolean(FIRST_EXEC, true);

        if(firstExec){
            showDialog();
        }

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
