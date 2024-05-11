package fr.altairstudios.arutairu;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Vector;

class LessonsCompleted implements Serializable {
    private final Vector<Vector<Integer>> completed;

    private HashMap<String, Boolean> completedHashed;

    void reinitLesson(){
        completedHashed = new HashMap<>();
    }

    void addCompleted(String word, boolean status){
        completedHashed.put(word, status);
    }

    boolean isCompleted(String word){
        return completedHashed.containsKey(word) ? completedHashed.get(word) : false;
    }

    void reinitLesson(int lesson){
        completed.set(lesson-1, new Vector<>());
        completedHashed.clear();
    }

    void addCompleted(int lesson, int word){
        if (completed.get(lesson-1) == null){
            completed.add(lesson-1, new Vector<>());
        }
        completed.get(lesson-1).add(word);
    }

    int totalCompleted(){
        int i = 0;
        for (Vector<Integer> count:completed) {
            i+=count.size();
        }
        return i;
    }

    LessonsCompleted() {
        completed = new Vector<>();
        completedHashed = new HashMap<>();
        for (int i = 0; i < 60; i++) {
            completed.add(new Vector<>());
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
