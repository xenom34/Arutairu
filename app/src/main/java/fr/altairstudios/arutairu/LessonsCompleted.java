package fr.altairstudios.arutairu;

import android.util.SparseArray;
import android.util.SparseBooleanArray;

import java.io.Serializable;
import java.sql.Struct;
import java.util.Vector;

class LessonsCompleted implements Serializable {
    private Vector<Vector<Integer>> completed;

    void addCompleted(int lesson, int word){
        if (completed.get(lesson-1) == null){
            completed.add(lesson-1, new Vector<Integer>());
        }
        completed.get(lesson-1).add(word);
    }

    public LessonsCompleted() {
         completed = new Vector<>();
        for (int i = 0; i < LessonsStorage.TOTAL; i++) {
            completed.add(new Vector<Integer>());
        }
    }

    boolean isCompleted(int lesson, int word){
        for (Integer i: completed.get(lesson-1)) {
            if (i.equals(word))
                return true;
        }
        return false;
    }

    int howManyCompleted(int lesson){
        int i = 0;

        for (Integer j: completed.get(lesson-1)) {
            i++;
        }
        return i;
    }


}
