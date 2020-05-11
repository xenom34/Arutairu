package fr.altairstudios.arutairu;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.os.Bundle;
import android.provider.Settings;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.Menu;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.navigation.NavigationView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.core.view.GravityCompat;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Random;
import java.util.Vector;

public class HomeActivity extends AppCompatActivity {

    private AppBarConfiguration mAppBarConfiguration;
    private DrawerLayout drawerLayout;
    private LessonsStorage lessonsStorage = new LessonsStorage();
    private boolean firstExec;
    public static final String ARUTAIRU_SHARED_PREFS = "ArutairuSharedPrefs";
    public static final String FIRST_EXEC = "first";
    static boolean waitingForData = false;
    SharedPreferences sharedPreferences;
    LessonsCompleted lessonsCompleted;
    private int state;
    private ImageView topImage;
    private TextView mTitle, mTextProgress, mNbLearn;
    private ProgressBar mProgress;
    private Button mPractice, mTest, mRevision;
    private NavigationView navigationView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        Toolbar toolbar = findViewById(R.id.toolbar);
        toolbar.setBackgroundColor(getResources().getColor(R.color.darkGrey));
        setSupportActionBar(toolbar);
        drawerLayout = findViewById(R.id.drawer_layout);
        topImage = findViewById(R.id.imageView2);
        mTitle = findViewById(R.id.textView2);
        mTextProgress = findViewById(R.id.textProgress);
        mProgress = findViewById(R.id.progressBar);
        mPractice = findViewById(R.id.practice);
        mRevision = findViewById(R.id.list);
        mTest = findViewById(R.id.evaluation);
        try {
            loadCompleted();
        } catch (IOException e) {
            lessonsCompleted = new LessonsCompleted();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        navigationView = findViewById(R.id.nav_view);
        mNbLearn = navigationView.getHeaderView(0).findViewById(R.id.nbLearn);
        navigationView.setCheckedItem(R.id.nav_people);
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                drawerLayout.closeDrawer(GravityCompat.START);

                initMenu(item.getItemId());

                refresh(item.getItemId(), item.getTitle());
                return true;
            }
        });

        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.

        final ActionBar actionBar = getSupportActionBar();
        assert actionBar != null;
        actionBar.setTitle("Leçons");
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setHomeAsUpIndicator(R.drawable.ic_menu_black_24dp);

        sharedPreferences = getSharedPreferences(ARUTAIRU_SHARED_PREFS, MODE_PRIVATE);

        firstExec = sharedPreferences.getBoolean(FIRST_EXEC, true);

    }

    private void refresh(int item, CharSequence title) {
        mTitle.setText(title);
        mTest.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SelectedItemList selectedItemList = new SelectedItemList();
                String[] tempJP = getResources().getStringArray(lessonsStorage.getJpRes(state));
                String[] tempRomaji = getResources().getStringArray(lessonsStorage.getRmRes(state));
                String[] tempFr = getResources().getStringArray(lessonsStorage.getSrcRes(state));
                Random random = new Random();
                int randomNumber;
                Vector<Integer> indexes = new Vector<>();

                for (int i = 0; i != tempJP.length; i++){
                    indexes.add(i);
                }

                for (int i = 0; i != tempJP.length; i++){
                    randomNumber = indexes.elementAt(random.nextInt(indexes.size()));
                    selectedItemList.addJp(tempJP[randomNumber]);
                    selectedItemList.addRomaji(tempRomaji[randomNumber]);
                    selectedItemList.addFrench(tempFr[randomNumber]);
                    selectedItemList.addCorrespondingIndex(randomNumber);
                    indexes.removeElement(randomNumber);
                }
                selectedItemList.setCorrespondingLesson(state);

                Intent intent = new Intent(getApplicationContext(), Exercise.class);
                intent.putExtra("LESSON", selectedItemList);
                intent.putExtra("COMPLETED", lessonsCompleted);
                intent.putExtra("RETRIEVE", true);
                intent.putExtra("SAVE", true);
                intent.putExtra("CHAPTER", state);
                intent.putExtra("REVISION", false);
                intent.putExtra("MAX", tempJP.length);
                waitingForData = true;
                startActivity(intent);
                finish();
            }
        });
        mPractice.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showChoosingWord(state-1);
            }
        });
        float progress = (float)(lessonsCompleted.howManyCompleted(state))/(float)(getResources().getStringArray(lessonsStorage.getJpRes(state)).length)*100;
        mProgress.setProgress((int)progress);
        mTextProgress.setText((int)progress+"%");
        mRevision.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), Revision.class);
                intent.putExtra("LESSON", state);
                intent.putExtra("MAX", getResources().getStringArray(lessonsStorage.getJpRes(state)).length);
                intent.putExtra("COMPLETED", lessonsCompleted);
                intent.putExtra("RETRIEVE", false);
                intent.putExtra("REVISION", true);
                waitingForData = false;
                startActivity(intent);
                finish();
            }
        });
        if(progress == 100)
            mProgress.setProgressTintList(ColorStateList.valueOf(Color.GREEN));
    }

    private void initMenu(int id){
        switch (id){
            case R.id.nav_hiragana:
                topImage.setImageResource(R.drawable.hiragana);
                state = LessonsStorage.HIRAGANA;
                break;
            case R.id.nav_hiragana_d:
                topImage.setImageResource(R.drawable.dakuten);
                state = LessonsStorage.DAKUTEN;
                break;
            case R.id.nav_katakana:
                topImage.setImageResource(R.drawable.katakana);
                state = LessonsStorage.KATAKANA;
                break;
            case R.id.nav_katakana_d:
                topImage.setImageResource(R.drawable.dakutenkata);
                state = LessonsStorage.DAKUTENKATA;
                break;
            case R.id.nav_numbers:
                topImage.setImageResource(R.drawable.numbers);
                state = LessonsStorage.NUMBERS;
                break;
            case R.id.nav_people:
                topImage.setImageResource(R.drawable.people);
                state = LessonsStorage.PEOPLE;
                break;
            case R.id.nav_jobs:
                state = LessonsStorage.JOBS;
                topImage.setImageResource(R.drawable.jobs);
                break;
            case R.id.nav_body:
                state = LessonsStorage.BODY;
                topImage.setImageResource(R.drawable.body);
                break;
            case R.id.nav_family:
                state = LessonsStorage.FAMILY;
                topImage.setImageResource(R.drawable.family);
                break;
            case R.id.nav_animals:
                state = LessonsStorage.ANIMALS;
                topImage.setImageResource(R.drawable.animals);
                break;
            case R.id.nav_plants:
                state = LessonsStorage.PLANTS;
                topImage.setImageResource(R.drawable.plants);
                break;
            case R.id.nav_crop:
                state = LessonsStorage.CROPS;
                topImage.setImageResource(R.drawable.crop);
                break;
            case R.id.nav_food:
                topImage.setImageResource(R.drawable.food);
                state = LessonsStorage.FOOD;
                break;
            case R.id.nav_drinks:
                topImage.setImageResource(R.drawable.drinks);
                state = LessonsStorage.DRINK;
                break;
            case R.id.nav_seasoning:
                topImage.setImageResource(R.drawable.seasoning);
                state = LessonsStorage.SEASONING;
                break;
            case R.id.nav_weeks:
                topImage.setImageResource(R.drawable.week);
                state = LessonsStorage.DAYS;
                break;
            case R.id.nav_weather:
                topImage.setImageResource(R.drawable.weather);
                state = LessonsStorage.WEATHER;
                break;
            case R.id.nav_directions:
                topImage.setImageResource(R.drawable.directions);
                state = LessonsStorage.DIRECTIONS;
                break;
            case R.id.nav_materials:
                topImage.setImageResource(R.drawable.materials);
                state = LessonsStorage.MATERIALS;
                break;
            case R.id.nav_weights:
                topImage.setImageResource(R.drawable.measures);
                state = LessonsStorage.WEIGHTS;
                break;
            case R.id.nav_society:
                topImage.setImageResource(R.drawable.society);
                state = LessonsStorage.SOCIETY;
                break;
            case R.id.nav_homes:
                topImage.setImageResource(R.drawable.home);
                state = LessonsStorage.HOME;
                break;
            case R.id.nav_tools:
                topImage.setImageResource(R.drawable.tools);
                state = LessonsStorage.TOOLS;
                break;
            case R.id.nav_stationery:
                topImage.setImageResource(R.drawable.stationery);
                state = LessonsStorage.STATIONERY;
                break;
            case R.id.nav_clothes:
                topImage.setImageResource(R.drawable.clothes);
                state = LessonsStorage.CLOTHES;
                break;
            case R.id.nav_transport:
                topImage.setImageResource(R.drawable.transport);
                state = LessonsStorage.TRANSPORT;
                break;
            case R.id.nav_language:
                topImage.setImageResource(R.drawable.language);
                state = LessonsStorage.LANGUAGE;
                break;
            case R.id.nav_media:
                topImage.setImageResource(R.drawable.medias);
                state = LessonsStorage.MEDIA;
                break;
            case R.id.nav_colors:
                topImage.setImageResource(R.drawable.colors);
                state = LessonsStorage.COLORS;
                break;
            case R.id.nav_other:
                topImage.setImageResource(R.drawable.others);
                state = LessonsStorage.OTHERS;
                break;
            case R.id.nav_abstract:
                topImage.setImageResource(R.drawable.abstracts);
                state = LessonsStorage.ABSTRACT;
                break;
        }

        sharedPreferences.edit().putInt("STATE", state).apply();
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
                startActivity(new Intent(getApplicationContext(), HomeActivity.class));
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

    @Override
    protected void onStart() {
        super.onStart();

        if (waitingForData){
            lessonsCompleted = (LessonsCompleted) getIntent().getSerializableExtra("COMPLETED");
            try {
                saveCompleted();
            } catch (IOException e) {
                e.printStackTrace();
            }
            waitingForData = false;
        }

        if(firstExec){
            showDialog();
        }
        state = sharedPreferences.getInt("STATE", 6);

        checkingFirstMenuItem(state);
        String s = "Nombres de mots appris : " + lessonsCompleted.totalCompleted();
        mNbLearn.setText(s);

    }

    void checkingFirstMenuItem(int element){
        switch (element){
            case LessonsStorage.HIRAGANA:
                navigationView.setCheckedItem(R.id.nav_hiragana);
                initMenu(R.id.nav_hiragana);
                refresh(state, "Hiragana");
                break;
            case LessonsStorage.DAKUTEN:
                navigationView.setCheckedItem(R.id.nav_hiragana_d);
                initMenu(R.id.nav_hiragana_d);
                refresh(state, "Dakuten Hiragana");
                break;
            case LessonsStorage.KATAKANA:
                navigationView.setCheckedItem(R.id.nav_katakana);
                initMenu(R.id.nav_katakana);
                refresh(state, "Katakana");
                break;
            case LessonsStorage.DAKUTENKATA:
                navigationView.setCheckedItem(R.id.nav_katakana_d);
                initMenu(R.id.nav_katakana_d);
                refresh(state, "Dakuten Katakana");
                break;
            case LessonsStorage.NUMBERS:
                navigationView.setCheckedItem(R.id.nav_numbers);
                initMenu(R.id.nav_numbers);
                refresh(state, "Les nombres");
                break;
            case LessonsStorage.PEOPLE:
                navigationView.setCheckedItem(R.id.nav_people);
                initMenu(R.id.nav_people);
                refresh(state, "Les gens");
                break;
            case LessonsStorage.JOBS:
                navigationView.setCheckedItem(R.id.nav_jobs);
                initMenu(R.id.nav_people);
                refresh(state, "Les métiers");
                break;
            case LessonsStorage.BODY:
                navigationView.setCheckedItem(R.id.nav_body);
                initMenu(R.id.nav_body);
                refresh(state, "Le corps");
                break;
            case LessonsStorage.FAMILY:
                navigationView.setCheckedItem(R.id.nav_family);
                initMenu(R.id.nav_family);
                refresh(state, "La famille");
                break;
            case LessonsStorage.ANIMALS:
                navigationView.setCheckedItem(R.id.nav_animals);
                initMenu(R.id.nav_animals);
                refresh(state, "Les animaux");
                break;
            case LessonsStorage.PLANTS:
                navigationView.setCheckedItem(R.id.nav_plants);
                initMenu(R.id.nav_plants);
                refresh(state, "Les plantes");
                break;
            case LessonsStorage.CROPS:
                navigationView.setCheckedItem(R.id.nav_crop);
                initMenu(R.id.nav_crop);
                refresh(state, "L'agriculture");
                break;
            case LessonsStorage.FOOD:
                navigationView.setCheckedItem(R.id.nav_food);
                initMenu(R.id.nav_food);
                refresh(state, "La nourriture");
                break;
            case LessonsStorage.DRINK:
                navigationView.setCheckedItem(R.id.nav_drinks);
                initMenu(R.id.nav_drinks);
                refresh(state, "Les boissons");
                break;
            case LessonsStorage.SEASONING:
                navigationView.setCheckedItem(R.id.nav_seasoning);
                initMenu(R.id.nav_seasoning);
                refresh(state, "Les assaisonnements");
                break;
            case LessonsStorage.DAYS:
                navigationView.setCheckedItem(R.id.nav_weeks);
                initMenu(R.id.nav_weeks);
                refresh(state, "Les jours");
                break;
            case LessonsStorage.WEATHER:
                navigationView.setCheckedItem(R.id.nav_weather);
                initMenu(R.id.nav_weather);
                refresh(state, "La météo");
                break;
            case LessonsStorage.DIRECTIONS:
                navigationView.setCheckedItem(R.id.nav_directions);
                initMenu(R.id.nav_directions);
                refresh(state, "Les directions");
                break;
            case LessonsStorage.MATERIALS:
                navigationView.setCheckedItem(R.id.nav_materials);
                initMenu(R.id.nav_materials);
                refresh(state, "Les matériaux");
                break;
            case LessonsStorage.WEIGHTS:
                navigationView.setCheckedItem(R.id.nav_weights);
                initMenu(R.id.nav_weights);
                refresh(state, "Les mesures");
                break;
            case LessonsStorage.SOCIETY:
                navigationView.setCheckedItem(R.id.nav_society);
                initMenu(R.id.nav_society);
                refresh(state, "La société");
                break;
            case LessonsStorage.HOME:
                navigationView.setCheckedItem(R.id.nav_homes);
                initMenu(R.id.nav_homes);
                refresh(state, "La maison");
                break;
            case LessonsStorage.TOOLS:
                navigationView.setCheckedItem(R.id.nav_tools);
                initMenu(R.id.nav_tools);
                refresh(state, "Les outils");
                break;
            case LessonsStorage.STATIONERY:
                navigationView.setCheckedItem(R.id.nav_stationery);
                initMenu(R.id.nav_stationery);
                refresh(state, "La papeterie");
                break;
            case LessonsStorage.CLOTHES:
                navigationView.setCheckedItem(R.id.nav_clothes);
                initMenu(R.id.nav_clothes);
                refresh(state, "Les vêtements");
                break;
            case LessonsStorage.TRANSPORT:
                navigationView.setCheckedItem(R.id.nav_transport);
                initMenu(R.id.nav_transport);
                refresh(state, "Les transports");
                break;
            case LessonsStorage.LANGUAGE:
                navigationView.setCheckedItem(R.id.nav_language);
                initMenu(R.id.nav_language);
                refresh(state, "Les langues");
                break;
            case LessonsStorage.MEDIA:
                navigationView.setCheckedItem(R.id.nav_media);
                initMenu(R.id.nav_media);
                refresh(state, "Les médias");
                break;
            case LessonsStorage.COLORS:
                navigationView.setCheckedItem(R.id.nav_colors);
                initMenu(R.id.nav_colors);
                refresh(state, "Les couleurs");
                break;
            case LessonsStorage.OTHERS:
                navigationView.setCheckedItem(R.id.nav_other);
                initMenu(R.id.nav_other);
                refresh(state, "Autres");
                break;
            case LessonsStorage.ABSTRACT:
                navigationView.setCheckedItem(R.id.nav_abstract);
                initMenu(R.id.nav_abstract);
                refresh(state, "Abstrait");
                break;
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
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        switch (item.getItemId()){

            case android.R.id.home:
                drawerLayout.openDrawer(GravityCompat.START);
                return true;

        }
        return super.onOptionsItemSelected(item);
    }
}
