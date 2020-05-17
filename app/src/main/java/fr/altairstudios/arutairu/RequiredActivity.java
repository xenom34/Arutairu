package fr.altairstudios.arutairu;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.provider.Settings;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;

import com.google.android.material.card.MaterialCardView;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Vector;

public class RequiredActivity extends AppCompatActivity {

    private Vector<MaterialCardView> cards = new Vector<>();
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
        setContentView(R.layout.activity_required);


        sharedPreferences = getSharedPreferences(ARUTAIRU_SHARED_PREFS, MODE_PRIVATE);
        firstExec = sharedPreferences.getBoolean(FIRST_EXEC, true);

        cards.add((com.google.android.material.card.MaterialCardView) findViewById(R.id.btnHiragana));
        cards.add((com.google.android.material.card.MaterialCardView) findViewById(R.id.btnDakuten));
        cards.add((com.google.android.material.card.MaterialCardView) findViewById(R.id.btnKata));
        cards.add((com.google.android.material.card.MaterialCardView) findViewById(R.id.btnDakutenKata));
        cards.add((com.google.android.material.card.MaterialCardView) findViewById(R.id.btnNumbers));

        progresses.add((ProgressBar) findViewById(R.id.hiraganaProgress));
        progresses.add((ProgressBar) findViewById(R.id.dakutenProgress));
        progresses.add((ProgressBar) findViewById(R.id.KataProgress));
        progresses.add((ProgressBar) findViewById(R.id.dakutenKataProgress));
        progresses.add((ProgressBar) findViewById(R.id.numbersProgress));

        checks.add((ImageView) findViewById(R.id.checkHiragana));
        checks.add((ImageView) findViewById(R.id.checkDakuten));
        checks.add((ImageView) findViewById(R.id.checkKata));
        checks.add((ImageView) findViewById(R.id.checkDakutenKata));
        checks.add((ImageView) findViewById(R.id.checkNumbers));

        for (int i = 0; i < LessonsStorage.NUMBERS; i++) {
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
        } catch (
                IOException e) {
            lessonsCompleted = new LessonsCompleted();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    void refresh(){

        for (int i = 0; i < LessonsStorage.NUMBERS; i++) {
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


        builder.setPositiveButton("Configurer !", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                startActivity(new Intent(getApplicationContext(), ListActivity.class));
                finish();
                startActivityForResult(new Intent(Settings.ACTION_INPUT_METHOD_SUBTYPE_SETTINGS), 0);
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
        final ArrayList<String> wordsFr = new ArrayList<>(Arrays.asList(getResources().getStringArray(lessonsStorage.getSrcRes(chapter+1))));
        final ArrayList<String> words = new ArrayList<>(Arrays.asList(getResources().getStringArray(lessonsStorage.getJpRes(chapter+1))));
        final WordsAdapter wordsAdapter = new WordsAdapter(this, words, wordsFr, chapter, lessonsCompleted);
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
