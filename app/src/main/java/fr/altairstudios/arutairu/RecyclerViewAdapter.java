package fr.altairstudios.arutairu;

import android.content.Context;
import android.graphics.Color;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerViewAdapter.ViewHolder>{
    private static final String TAG = "RecyclerViewAdapter";

    private int lesson;
    private ArrayList<String> mWords;
    private LessonsCompleted lessonsCompleted;

    public RecyclerViewAdapter(int _lesson, ArrayList<String> mWords, LessonsCompleted completed) {
        this.lesson = _lesson;
        this.mWords = mWords;
        this.lessonsCompleted = completed;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.word_item_recycler, parent, false);
        ViewHolder holder = new ViewHolder(view);

        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        holder.mJp.setText(mWords.get(position));

            if(lessonsCompleted.isCompleted(lesson, position)){
                holder.mJp.setTextColor(Color.argb(255,14,163,83));
            }

        holder.mParentLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CheckBox mCheckBox;
                mCheckBox = v.findViewById(R.id.checkBox);
                mCheckBox.setChecked(!mCheckBox.isChecked());
            }
        });

    }

    @Override
    public int getItemCount() {
        return mWords.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        TextView mJp;
        LinearLayout mParentLayout;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            mJp = itemView.findViewById(R.id.chooserJP);
            mParentLayout = itemView.findViewById(R.id.parentLayout);
        }
    }
}
