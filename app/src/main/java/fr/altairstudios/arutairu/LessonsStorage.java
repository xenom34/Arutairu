package fr.altairstudios.arutairu;

import android.util.SparseArray;

import java.util.HashMap;

public class LessonsStorage {
    private SparseArray<Lesson> storage = new SparseArray<>();
    public static final int NUMBERS = 1;

    public LessonsStorage(){
        storage.put(1, new Lesson(R.array.numbersJP, R.array.numbersRM, R.array.numbersFR));
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
