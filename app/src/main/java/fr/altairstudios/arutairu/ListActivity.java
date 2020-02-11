package fr.altairstudios.arutairu;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

public class ListActivity extends AppCompatActivity {

    private FloatingActionButton mStartNumbers;
    private LessonsStorage lessonsStorage = new LessonsStorage();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list);

        mStartNumbers = findViewById(R.id.btnNumbers);

        mStartNumbers.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), Revision.class);
                intent.putExtra("LESSON", 1);
                intent.putExtra("MAX", getResources().getStringArray(lessonsStorage.getJpRes(LessonsStorage.NUMBERS)).length);
                startActivity(intent);
            }
        });
    }
}
