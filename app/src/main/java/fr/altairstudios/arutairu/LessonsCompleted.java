package fr.altairstudios.arutairu;

import android.util.SparseArray;
import android.util.SparseBooleanArray;

import java.io.Serializable;
import java.sql.Struct;
import java.util.Vector;

class LessonsCompleted implements Serializable {
    private Vector<Vector<Integer>> completed;

    void addCompleted(int lesson, int word){
        if (completed.get(lesson) == null){
            completed.add(lesson, new Vector<Integer>());
        }
        completed.get(lesson).add(word);
    }

    public LessonsCompleted() {
         completed = new Vector<>();
        for (int i = 0; i < LessonsStorage.TOTAL; i++) {
            completed.add(new Vector<Integer>());
        }
    }

    boolean isCompleted(int lesson, int word){
        for (Integer i: completed.get(lesson)) {
            if (i.equals(word))
                return true;
        }
        return false;
    }

    int howManyCompleted(int lesson){
        int i = 0;

        for (Integer j: completed.get(lesson)) {
            i++;
        }
        return i;
    }


}
