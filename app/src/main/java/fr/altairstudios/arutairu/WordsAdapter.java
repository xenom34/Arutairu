package fr.altairstudios.arutairu;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.ArrayList;

public class WordsAdapter extends ArrayAdapter<String> {
    Context context;
    ArrayList<String> words;
    ArrayList<String> wordsFr;
    int mLesson;
    LessonsCompleted mLessonsCompleted;
    SelectedItemList selectedItemList = new SelectedItemList();

    public WordsAdapter(@NonNull Context context, ArrayList<String> words, ArrayList<String> wordsFr, int lesson, LessonsCompleted lessonsCompleted) {
        super(context, R.layout.word_item_recycler, R.id.chooserJP, words);
        this.words = words;
        this.wordsFr = wordsFr;
        this.context = context;
        this.mLesson = lesson;
        this.mLessonsCompleted = lessonsCompleted;
    }

    @NonNull
    @Override
    public View getView(final int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        LayoutInflater layoutInflater = (LayoutInflater)getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = layoutInflater.inflate(R.layout.word_item_recycler, parent, false);

        TextView mJp = view.findViewById(R.id.chooserJP);
        TextView mFr = view.findViewById(R.id.chooserFR);
        CheckBox mCheckBox = view.findViewById(R.id.checkBox);
        mCheckBox.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if(isChecked){
                if (!selectedItemList.getSelected().contains(position)){
                    selectedItemList.addSelected(position);
                }
            }else{
                selectedItemList.removeSelected(position);
            }
        });
        mCheckBox.setChecked(selectedItemList.isSelected(position));

        mJp.setText(words.get(position));
        mFr.setText(wordsFr.get(position));
        if (mLessonsCompleted.isCompleted(mLesson+1, position)){
            mJp.setTextColor(Color.argb(255,14,163,83));
            mFr.setTextColor(Color.argb(255,14,163,83));
        }

        return view;
    }
}
