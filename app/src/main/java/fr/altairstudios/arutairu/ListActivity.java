package fr.altairstudios.arutairu;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;

import androidx.appcompat.app.AppCompatActivity;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Vector;

public class ListActivity extends AppCompatActivity {

    private Vector<com.google.android.material.card.MaterialCardView> cards = new Vector<>();
    private LessonsStorage lessonsStorage = new LessonsStorage();
    private boolean firstExec;
    private Vector<ImageView> checks = new Vector<>();
    private Vector<ProgressBar> progresses = new Vector<>();
    public static final String ARUTAIRU_SHARED_PREFS = "ArutairuSharedPrefs";
    public static final String FIRST_EXEC = "first";
    static boolean waitingForData = false;
    SharedPreferences sharedPreferences;
    LessonsCompleted lessonsCompleted;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list);

        sharedPreferences = getSharedPreferences(ARUTAIRU_SHARED_PREFS, MODE_PRIVATE);

        firstExec = sharedPreferences.getBoolean(FIRST_EXEC, true);

        cards.add((com.google.android.material.card.MaterialCardView) findViewById(R.id.btnHiragana));
        cards.add((com.google.android.material.card.MaterialCardView) findViewById(R.id.btnDakuten));
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

        progresses.add((ProgressBar) findViewById(R.id.hiraganaProgress));
        progresses.add((ProgressBar) findViewById(R.id.dakutenProgress));
        progresses.add((ProgressBar) findViewById(R.id.numbersProgress));
        progresses.add((ProgressBar) findViewById(R.id.bodyProgress));
        progresses.add((ProgressBar) findViewById(R.id.jobsProgress));
        progresses.add((ProgressBar) findViewById(R.id.peopleProgress));
        progresses.add((ProgressBar) findViewById(R.id.familyProgress));
        progresses.add((ProgressBar) findViewById(R.id.animalsProgress));
        progresses.add((ProgressBar) findViewById(R.id.plantsProgress));
        progresses.add((ProgressBar) findViewById(R.id.cropProgress));
        progresses.add((ProgressBar) findViewById(R.id.foodProgress));
        progresses.add((ProgressBar) findViewById(R.id.drinksProgress));
        progresses.add((ProgressBar) findViewById(R.id.seasoningProgress));
        progresses.add((ProgressBar) findViewById(R.id.weeksProgress));
        progresses.add((ProgressBar) findViewById(R.id.weatherProgress));
        progresses.add((ProgressBar) findViewById(R.id.directionsProgress));
        progresses.add((ProgressBar) findViewById(R.id.materialsProgress));
        progresses.add((ProgressBar) findViewById(R.id.measuresProgress));
        progresses.add((ProgressBar) findViewById(R.id.societyProgress));
        progresses.add((ProgressBar) findViewById(R.id.homeProgress));
        progresses.add((ProgressBar) findViewById(R.id.toolsProgress));
        progresses.add((ProgressBar) findViewById(R.id.stationeryProgress));
        progresses.add((ProgressBar) findViewById(R.id.clothesProgress));
        progresses.add((ProgressBar) findViewById(R.id.transportProgress));
        progresses.add((ProgressBar) findViewById(R.id.languageProgress));
        progresses.add((ProgressBar) findViewById(R.id.mediasProgress));
        progresses.add((ProgressBar) findViewById(R.id.colorsProgress));
        progresses.add((ProgressBar) findViewById(R.id.othersProgress));
        progresses.add((ProgressBar) findViewById(R.id.abstractProgress));

        checks.add((ImageView) findViewById(R.id.checkHiragana));
        checks.add((ImageView) findViewById(R.id.checkDakuten));
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

        for (int i = 0; i < LessonsStorage.TOTAL; i++) {
            final int finalI = i;
            cards.elementAt(i).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    Intent intent = new Intent(getApplicationContext(), Revision.class);
                    intent.putExtra("LESSON", finalI +1);
                    intent.putExtra("MAX", getResources().getStringArray(lessonsStorage.getJpRes(finalI+1)).length);
                    intent.putExtra("COMPLETED", lessonsCompleted);
                    intent.putExtra("RETRIEVE", false);
                    waitingForData = true;
                    startActivity(intent);
                    finish();

                }
            });
            cards.elementAt(i).setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    showChoosingWord(finalI);
                    return false;
                }
            });


        }

        for (ImageView imageView:checks) {
            imageView.setAlpha(0f);
        }

        try {
            loadCompleted();
        } catch (IOException e) {
            lessonsCompleted = new LessonsCompleted();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    void refresh(){

        for (int i = 0; i < LessonsStorage.TOTAL; i++) {
            float progress = (float)(lessonsCompleted.howManyCompleted(i+1))/(float)(getResources().getStringArray(lessonsStorage.getJpRes(i+1)).length)*100;
            progresses.elementAt(i).setProgress((int)progress);
            if (progress == 100)
                checks.elementAt(i).setAlpha(1f);
        }
    }

    void saveCompleted() throws IOException {
        FileOutputStream fos = getApplicationContext().openFileOutput("save.dat", Context.MODE_PRIVATE);
        ObjectOutputStream os = new ObjectOutputStream(fos);
        os.writeObject(lessonsCompleted);
        os.close();
        fos.close();
    }

    void loadCompleted() throws IOException, ClassNotFoundException {
        FileInputStream fis = getApplicationContext().openFileInput("save.dat");
        ObjectInputStream is = new ObjectInputStream(fis);
        lessonsCompleted = (LessonsCompleted) is.readObject();
        is.close();
        fis.close();
    }

    @Override
    protected void onStart() {
        super.onStart();

        refresh();

        if (waitingForData){
            lessonsCompleted = (LessonsCompleted) getIntent().getSerializableExtra("COMPLETED");
            try {
                saveCompleted();
            } catch (IOException e) {
                e.printStackTrace();
            }
            refresh();
            waitingForData = false;
        }

        if(firstExec){
            showDialog();
        }

    }

    private void keyboardDialog() {
        //before inflating the custom alert dialog layout, we will get the current activity viewgroup
        ViewGroup viewGroup = findViewById(android.R.id.content);

        //then we will inflate the custom alert dialog xml that we created
        View dialogView = LayoutInflater.from(this).inflate(R.layout.keyboard_dialog, viewGroup, false);

        //Now we need an AlertDialog.Builder object
        AlertDialog.Builder builder = new AlertDialog.Builder(this);

        //setting the view of the builder to our custom view that we already inflated
        builder.setView(dialogView);


        builder.setPositiveButton("D'accord !", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
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
                keyboardDialog();
            }
        });

        builder.setOnDismissListener(new DialogInterface.OnDismissListener() {
            @Override
            public void onDismiss(DialogInterface dialogInterface) {
                //mSpam = 0;
                SharedPreferences sharedPreferences = getSharedPreferences(ARUTAIRU_SHARED_PREFS, MODE_PRIVATE);
                sharedPreferences.edit().putBoolean(FIRST_EXEC, false).apply();
                keyboardDialog();
            }
        });

        //finally creating the alert dialog and displaying it
        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }

    private void showChoosingWord(final int chapter) {
        //before inflating the custom alert dialog layout, we will get the current activity viewgroup
        ViewGroup viewGroup = findViewById(android.R.id.content);

        //then we will inflate the custom alert dialog xml that we created
        View dialogView = LayoutInflater.from(this).inflate(R.layout.dialog_choosing_word, viewGroup, false);

        ListView listView = dialogView.findViewById(R.id.listWords);
        final ArrayList<String> words = new ArrayList<>(Arrays.asList(getResources().getStringArray(lessonsStorage.getJpRes(chapter+1))));
        final WordsAdapter wordsAdapter = new WordsAdapter(this, words, chapter, lessonsCompleted);
        listView.setAdapter(wordsAdapter);
        //Now we need an AlertDialog.Builder object
        AlertDialog.Builder builder = new AlertDialog.Builder(this);

        //setting the view of the builder to our custom view that we already inflated
        builder.setView(dialogView);


        builder.setPositiveButton("Lancez-vous !", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                if(!wordsAdapter.selectedItemList.getSelected().isEmpty()) {
                    SelectedItemList selectedItemList = wordsAdapter.selectedItemList;
                    String[] tempJP = getResources().getStringArray(lessonsStorage.getJpRes(chapter + 1));
                    String[] tempRomaji = getResources().getStringArray(lessonsStorage.getRmRes(chapter + 1));
                    String[] tempFr = getResources().getStringArray(lessonsStorage.getSrcRes(chapter + 1));
                    for (int j : selectedItemList.getSelected()) {
                        selectedItemList.addJp(tempJP[j]);
                        selectedItemList.addRomaji(tempRomaji[j]);
                        selectedItemList.addFrench(tempFr[j]);
                        selectedItemList.addCorrespondingIndex(j);
                    }
                    selectedItemList.setCorrespondingLesson(chapter);

                    Intent intent = new Intent(getApplicationContext(), Revision.class);
                    intent.putExtra("LESSON", selectedItemList);
                    intent.putExtra("COMPLETED", lessonsCompleted);
                    intent.putExtra("RETRIEVE", true);
                    waitingForData = true;
                    startActivity(intent);
                    finish();
                }
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
