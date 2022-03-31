package fr.altairstudios.arutairu;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.ArrayList;
import java.util.Random;
import java.util.Vector;

import static android.content.Context.MODE_PRIVATE;

public class LessonsAdapter extends ArrayAdapter<Lesson> {
    private final boolean revisionDialog;
    private Context context;
    private ArrayList<Lesson> lessons;
    private Activity activity;
    public static final String FIRST_REVISION = "revision";
    public static final String ARUTAIRU_SHARED_PREFS = "ArutairuSharedPrefs";
    private SharedPreferences sharedPreferences;

    public LessonsAdapter(@NonNull Context context, ArrayList<Lesson> lessons, Activity activity) {
        super(context, R.layout.lessons_item_recycler, R.id.lessonItem, lessons);
        this.lessons = lessons;
        this.context = context;
        this.activity = activity;
        sharedPreferences = activity.getSharedPreferences(ARUTAIRU_SHARED_PREFS, MODE_PRIVATE);
        revisionDialog = sharedPreferences.getBoolean(FIRST_REVISION, true);

    }

    @NonNull
    @Override
    public View getView(final int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        LayoutInflater layoutInflater = (LayoutInflater)getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = layoutInflater.inflate(R.layout.lessons_item_recycler, parent, false);

        TextView mJp = view.findViewById(R.id.lessonItem);
        TextView mSec = view.findViewById(R.id.lessonItemSec);
        ImageButton mDelete = view.findViewById(R.id.delete);

        View view1 = view.findViewById(R.id.parentLayoutLesson);

        view1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selector(position);
            }
        });

        mDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        mJp.setText(lessons.get(position).getTitle());
        mSec.setText(lessons.get(position).getJpnArray().length + context.getString(R.string.mots));

        return view;
    }

    private void selector(int state) {
        //before inflating the custom alert dialog layout, we will get the current activity viewgroup
        ViewGroup viewGroup = activity.findViewById(android.R.id.content);

        //then we will inflate the custom alert dialog xml that we created
        final View dialogView = LayoutInflater.from(activity).inflate(R.layout.dialog_selector, viewGroup, false);
        final String[] tempJP = lessons.get(state).getJpnArray();
        final String[] tempRomaji = lessons.get(state).getRomArray();
        final String[] tempFr = lessons.get(state).getSrcArray();

        final TextView min = dialogView.findViewById(R.id.min);
        final TextView max = dialogView.findViewById(R.id.max);
        final TextView nb = dialogView.findViewById(R.id.nb);

        min.setText("1");
        max.setText(String.valueOf(tempJP.length));

        //Now we need an AlertDialog.Builder object
        AlertDialog.Builder builder = new AlertDialog.Builder(activity);

        //setting the view of the builder to our custom view that we already inflated
        builder.setView(dialogView);
        //builder.setNeutralButton(R.string.selector, (dialog, which) -> showChoosingWord(state-1));

        builder.setPositiveButton(R.string.ctipar, (dialog, which) -> {
            try {
                if (!(Integer.parseInt(min.getText() + "") < 1 || Integer.parseInt(max.getText() + "") > tempJP.length || Integer.parseInt(min.getText() + "") > Integer.parseInt(max.getText() + "") || Integer.parseInt(nb.getText() + "") < 1 || Integer.parseInt(nb.getText() + "") > tempJP.length)) {
                    SelectedItemList selector = new SelectedItemList();

                    Random random = new Random();
                    int randomNumber;
                    Vector<Integer> indexes = new Vector<>();

                    for (int i = Integer.parseInt(min.getText() + "") - 1; i != Integer.parseInt(max.getText() + ""); i++) {
                        indexes.add(i);
                    }

                    int size = Integer.parseInt(nb.getText().toString());

                    for (int i = 0; i != size; i++) {
                        randomNumber = indexes.elementAt(random.nextInt(indexes.size()));
                        selector.addJp(tempJP[randomNumber]);
                        selector.addRomaji(tempRomaji[randomNumber]);
                        selector.addFrench(tempFr[randomNumber]);
                        selector.addTts(tempJP[randomNumber]);
                        selector.addCorrespondingIndex(randomNumber);
                        indexes.removeElement(randomNumber);
                    }

                    selector.setCorrespondingLesson(state);

                    Intent intent = new Intent(activity, Revision.class);
                    intent.putExtra("LESSON", selector);
                    intent.putExtra("RETRIEVE", true);
                    intent.putExtra("ROMAJI", true);
                    intent.putExtra("FIRST", revisionDialog);
                    intent.putExtra("CUSTOM", true);
                    sharedPreferences.edit().putBoolean(FIRST_REVISION, false).apply();
                    activity.startActivity(intent);
                    activity.finish();
                }
            }catch (Exception e){
                //showErrorDialog();
                Log.d("DIALOG", e.getMessage());
            }
        });

        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }
}
