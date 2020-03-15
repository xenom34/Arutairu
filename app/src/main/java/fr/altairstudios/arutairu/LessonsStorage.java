package fr.altairstudios.arutairu;

import android.util.SparseArray;

import java.util.HashMap;

public class LessonsStorage {
    private SparseArray<Lesson> storage = new SparseArray<>();
    public static final int NUMBERS = 1;
    public static final int PEOPLE = 2;
    public static final int JOBS = 3;
    public static final int BODY = 4;


    public LessonsStorage() {
        storage.put(1, new Lesson(R.array.numbersJP, R.array.numbersRM, R.array.numbersFR));
        storage.put(2, new Lesson(R.array.peopleJS, R.array.peopleRM, R.array.peopleFR));
        storage.put(3, new Lesson(R.array.jobsJP, R.array.jobsRM, R.array.jobsFR));
        storage.put(4, new Lesson(R.array.bodyJP, R.array.bodyRM, R.array.bodyFR));
    }
    public int getJpRes(int i){
        return storage.get(i).getJpn();
    }

    public int getRmRes(int i){
        return storage.get(i).getRom();
    }

    public int getSrcRes(int i){
        return storage.get(i).getSrc();
    }


}
