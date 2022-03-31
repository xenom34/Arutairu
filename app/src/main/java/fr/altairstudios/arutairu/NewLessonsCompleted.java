package fr.altairstudios.arutairu;

import java.io.Serializable;
import java.util.HashMap;

public class NewLessonsCompleted implements Serializable {
    private HashMap<String, Boolean> completedHashed;

    void reinitLesson(int lesson){
        completedHashed = new HashMap<>();
    }

    void addCompleted(String word, boolean status){
        completedHashed.put(word, status);
    }

    boolean isCompleted(String word){
        return completedHashed.containsKey(word) ? completedHashed.get(word) : false;
    }


    public NewLessonsCompleted() {
        completedHashed = new HashMap<>();
    }
}
