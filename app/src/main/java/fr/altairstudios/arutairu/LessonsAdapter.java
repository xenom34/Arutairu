package fr.altairstudios.arutairu;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.ArrayList;

public class LessonsAdapter extends ArrayAdapter<Lesson> {
    Context context;
    ArrayList<Lesson> lessons;
    int mLesson;
    LessonsCompleted mLessonsCompleted;
    SelectedItemList selectedItemList = new SelectedItemList();

    public LessonsAdapter(@NonNull Context context, ArrayList<Lesson> lessons, int lesson, LessonsCompleted lessonsCompleted) {
        super(context, R.layout.word_item_recycler, R.id.chooserJP, lessons);
        this.lessons = lessons;
        this.context = context;
        this.mLesson = lesson;
        this.mLessonsCompleted = lessonsCompleted;
    }

    @NonNull
    @Override
    public View getView(final int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        LayoutInflater layoutInflater = (LayoutInflater)getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = layoutInflater.inflate(R.layout.lessons_item_recycler, parent, false);

        TextView mJp = view.findViewById(R.id.chooser);

        mJp.setText(lessons.get(position).getTitle());
        //mFr.setText(wordsFr.get(position));

        return view;
    }
}
