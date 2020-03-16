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

    private com.google.android.material.card.MaterialCardView mStartNumbers, mStartJobs, mStartBody, mStartPeople, mStartFamily, mStartAnimals, mStartPlants, mStartCrop, mStartFood, mStartDrinks, mStartSeasoning, mStartWeek, mStartWeather, mStartDirections, mStartMaterials, mStartMeasures, mStartSociety, mStartHome, mStartTools, mStartStationery, mStartClothes, mStartTransport, mStartLanguages, mStartMedias, mStartColors, mStartOther, mStartAbstract;
    private LessonsStorage lessonsStorage = new LessonsStorage();
    private boolean firstExec;
    public static final String ARUTAIRU_SHARED_PREFS = "ArutairuSharedPrefs";
    public static final String FIRST_EXEC = "first";
    SharedPreferences sharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list);

        mStartNumbers = findViewById(R.id.btnNumbers);
        mStartBody = findViewById(R.id.btnBody);
        mStartJobs = findViewById(R.id.btnJobs);
        mStartPeople = findViewById(R.id.btnPeople);
        mStartFamily = findViewById(R.id.btnFamily);
        mStartAnimals = findViewById(R.id.btnAnimals);
        mStartPlants = findViewById(R.id.btnPlants);
        mStartCrop = findViewById(R.id.btnCrops);
        mStartFood = findViewById(R.id.btnFood);
        mStartDrinks = findViewById(R.id.btnDrink);
        mStartSeasoning = findViewById(R.id.btnSeasoning);
        mStartWeek = findViewById(R.id.btnDays);
        mStartWeather = findViewById(R.id.btnWeather);
        mStartDirections = findViewById(R.id.btnDirections);
        mStartMaterials = findViewById(R.id.btnMaterials);
        mStartMeasures = findViewById(R.id.btnWeights);
        mStartSociety = findViewById(R.id.btnSociety);
        mStartHome = findViewById(R.id.btnHome);
        mStartTools = findViewById(R.id.btnTools);
        mStartStationery = findViewById(R.id.btnStationery);
        mStartClothes = findViewById(R.id.btnClothes);
        mStartTransport = findViewById(R.id.btnTransport);
        mStartLanguages = findViewById(R.id.btnLanguage);
        mStartMedias = findViewById(R.id.btnMedia);
        mStartColors = findViewById(R.id.btnColors);
        mStartOther = findViewById(R.id.btnOthers);
        mStartAbstract = findViewById(R.id.btnAbstract);

        mStartNumbers.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), Revision.class);
                intent.putExtra("LESSON", LessonsStorage.NUMBERS);
                intent.putExtra("MAX", getResources().getStringArray(lessonsStorage.getJpRes(LessonsStorage.NUMBERS)).length);
                startActivity(intent);
                finish();
            }
        });

        mStartPeople.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), Revision.class);
                intent.putExtra("LESSON", LessonsStorage.PEOPLE);
                intent.putExtra("MAX", getResources().getStringArray(lessonsStorage.getJpRes(LessonsStorage.PEOPLE)).length);
                startActivity(intent);
                finish();
            }
        });

        mStartJobs.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), Revision.class);
                intent.putExtra("LESSON", LessonsStorage.JOBS);
                intent.putExtra("MAX", getResources().getStringArray(lessonsStorage.getJpRes(LessonsStorage.JOBS)).length);
                startActivity(intent);
                finish();
            }
        });

        mStartBody.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), Revision.class);
                intent.putExtra("LESSON", LessonsStorage.BODY);
                intent.putExtra("MAX", getResources().getStringArray(lessonsStorage.getJpRes(LessonsStorage.BODY)).length);
                startActivity(intent);
                finish();
            }
        });

        mStartFamily.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), Revision.class);
                intent.putExtra("LESSON", LessonsStorage.FAMILY);
                intent.putExtra("MAX", getResources().getStringArray(lessonsStorage.getJpRes(LessonsStorage.FAMILY)).length);
                startActivity(intent);
                finish();
            }
        });

        mStartAnimals.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), Revision.class);
                intent.putExtra("LESSON", LessonsStorage.ANIMALS);
                intent.putExtra("MAX", getResources().getStringArray(lessonsStorage.getJpRes(LessonsStorage.ANIMALS)).length);
                startActivity(intent);
                finish();
            }
        });

        mStartPlants.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), Revision.class);
                intent.putExtra("LESSON", LessonsStorage.PLANTS);
                intent.putExtra("MAX", getResources().getStringArray(lessonsStorage.getJpRes(LessonsStorage.PLANTS)).length);
                startActivity(intent);
                finish();
            }
        });

        mStartCrop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), Revision.class);
                intent.putExtra("LESSON", LessonsStorage.CROPS);
                intent.putExtra("MAX", getResources().getStringArray(lessonsStorage.getJpRes(LessonsStorage.CROPS)).length);
                startActivity(intent);
                finish();
            }
        });

        mStartFood.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), Revision.class);
                intent.putExtra("LESSON", LessonsStorage.FOOD);
                intent.putExtra("MAX", getResources().getStringArray(lessonsStorage.getJpRes(LessonsStorage.FOOD)).length);
                startActivity(intent);
                finish();
            }
        });

        mStartDrinks.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), Revision.class);
                intent.putExtra("LESSON", LessonsStorage.DRINK);
                intent.putExtra("MAX", getResources().getStringArray(lessonsStorage.getJpRes(LessonsStorage.DRINK)).length);
                startActivity(intent);
                finish();
            }
        });

        mStartSeasoning.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), Revision.class);
                intent.putExtra("LESSON", LessonsStorage.SEASONING);
                intent.putExtra("MAX", getResources().getStringArray(lessonsStorage.getJpRes(LessonsStorage.SEASONING)).length);
                startActivity(intent);
                finish();
            }
        });

        mStartWeek.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), Revision.class);
                intent.putExtra("LESSON", LessonsStorage.DAYS);
                intent.putExtra("MAX", getResources().getStringArray(lessonsStorage.getJpRes(LessonsStorage.DAYS)).length);
                startActivity(intent);
                finish();
            }
        });

        mStartWeather.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), Revision.class);
                intent.putExtra("LESSON", LessonsStorage.WEATHER);
                intent.putExtra("MAX", getResources().getStringArray(lessonsStorage.getJpRes(LessonsStorage.WEATHER)).length);
                startActivity(intent);
                finish();
            }
        });

        mStartDirections.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), Revision.class);
                intent.putExtra("LESSON", LessonsStorage.DIRECTIONS);
                intent.putExtra("MAX", getResources().getStringArray(lessonsStorage.getJpRes(LessonsStorage.DIRECTIONS)).length);
                startActivity(intent);
                finish();
            }
        });

        mStartMaterials.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), Revision.class);
                intent.putExtra("LESSON", LessonsStorage.MATERIALS);
                intent.putExtra("MAX", getResources().getStringArray(lessonsStorage.getJpRes(LessonsStorage.MATERIALS)).length);
                startActivity(intent);
                finish();
            }
        });

        mStartMeasures.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), Revision.class);
                intent.putExtra("LESSON", LessonsStorage.WEIGHTS);
                intent.putExtra("MAX", getResources().getStringArray(lessonsStorage.getJpRes(LessonsStorage.WEIGHTS)).length);
                startActivity(intent);
                finish();
            }
        });

        mStartSociety.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), Revision.class);
                intent.putExtra("LESSON", LessonsStorage.SOCIETY);
                intent.putExtra("MAX", getResources().getStringArray(lessonsStorage.getJpRes(LessonsStorage.SOCIETY)).length);
                startActivity(intent);
                finish();
            }
        });

        mStartHome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), Revision.class);
                intent.putExtra("LESSON", LessonsStorage.HOME);
                intent.putExtra("MAX", getResources().getStringArray(lessonsStorage.getJpRes(LessonsStorage.HOME)).length);
                startActivity(intent);
                finish();
            }
        });

        mStartTools.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), Revision.class);
                intent.putExtra("LESSON", LessonsStorage.TOOLS);
                intent.putExtra("MAX", getResources().getStringArray(lessonsStorage.getJpRes(LessonsStorage.TOOLS)).length);
                startActivity(intent);
                finish();
            }
        });

        mStartStationery.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), Revision.class);
                intent.putExtra("LESSON", LessonsStorage.STATIONERY);
                intent.putExtra("MAX", getResources().getStringArray(lessonsStorage.getJpRes(LessonsStorage.STATIONERY)).length);
                startActivity(intent);
                finish();
            }
        });

        mStartClothes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), Revision.class);
                intent.putExtra("LESSON", LessonsStorage.CLOTHES);
                intent.putExtra("MAX", getResources().getStringArray(lessonsStorage.getJpRes(LessonsStorage.CLOTHES)).length);
                startActivity(intent);
                finish();
            }
        });

        mStartTransport.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), Revision.class);
                intent.putExtra("LESSON", LessonsStorage.TRANSPORT);
                intent.putExtra("MAX", getResources().getStringArray(lessonsStorage.getJpRes(LessonsStorage.TRANSPORT)).length);
                startActivity(intent);
                finish();
            }
        });

        mStartLanguages.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), Revision.class);
                intent.putExtra("LESSON", LessonsStorage.LANGUAGE);
                intent.putExtra("MAX", getResources().getStringArray(lessonsStorage.getJpRes(LessonsStorage.LANGUAGE)).length);
                startActivity(intent);
                finish();
            }
        });

        mStartMedias.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), Revision.class);
                intent.putExtra("LESSON", LessonsStorage.MEDIA);
                intent.putExtra("MAX", getResources().getStringArray(lessonsStorage.getJpRes(LessonsStorage.MEDIA)).length);
                startActivity(intent);
                finish();
            }
        });

        mStartColors.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), Revision.class);
                intent.putExtra("LESSON", LessonsStorage.COLORS);
                intent.putExtra("MAX", getResources().getStringArray(lessonsStorage.getJpRes(LessonsStorage.COLORS)).length);
                startActivity(intent);
                finish();
            }
        });

        mStartOther.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), Revision.class);
                intent.putExtra("LESSON", LessonsStorage.OTHERS);
                intent.putExtra("MAX", getResources().getStringArray(lessonsStorage.getJpRes(LessonsStorage.OTHERS)).length);
                startActivity(intent);
                finish();
            }
        });

        mStartAbstract.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), Revision.class);
                intent.putExtra("LESSON", LessonsStorage.ABSTRACT);
                intent.putExtra("MAX", getResources().getStringArray(lessonsStorage.getJpRes(LessonsStorage.ABSTRACT)).length);
                startActivity(intent);
                finish();
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();

        sharedPreferences = getSharedPreferences(ARUTAIRU_SHARED_PREFS, MODE_PRIVATE);

        if(sharedPreferences.getBoolean(FIRST_EXEC, true)){
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
